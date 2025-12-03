package com.webstore.webstore.controller;

import com.webstore.webstore.entity.Order;
import com.webstore.webstore.entity.User;
import com.webstore.webstore.service.OrderService;
import com.webstore.webstore.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping
    public String myOrders(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByUsernameEntity(userDetails.getUsername());
        model.addAttribute("orders", orderService.getUserOrders(user));
        return "orders/list";
    }

    @GetMapping("/{id}")
    public String orderDetails(@PathVariable Long id,
                               @AuthenticationPrincipal UserDetails userDetails,
                               Model model) {
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден"));

        // Проверяем, что заказ принадлежит пользователю
        User user = userService.findByUsernameEntity(userDetails.getUsername());
        if (!order.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Доступ запрещен");
        }

        model.addAttribute("order", order);
        return "orders/details";
    }

    @PostMapping("/create")
    public String createOrder(@AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {
        User user = userService.findByUsernameEntity(userDetails.getUsername());
        Order order = orderService.createOrder(user);

        redirectAttributes.addFlashAttribute("success", "Заказ создан. Добавьте товары.");
        return "redirect:/orders/" + order.getId();
    }

    @PostMapping("/{id}/add-product")
    public String addProductToOrder(@PathVariable Long id,
                                    @RequestParam Long productId,
                                    @RequestParam Integer quantity,
                                    RedirectAttributes redirectAttributes) {
        try {
            orderService.addProductToOrder(id, productId, quantity);
            redirectAttributes.addFlashAttribute("success", "Товар добавлен в заказ");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/orders/" + id;
    }

    @PostMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable Long id,
                              @AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {
        // Проверка принадлежности заказа
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден"));

        User user = userService.findByUsernameEntity(userDetails.getUsername());
        if (!order.getUser().getId().equals(user.getId())) {
            redirectAttributes.addFlashAttribute("error", "Доступ запрещен");
            return "redirect:/orders";
        }

        orderService.cancelOrder(id);
        redirectAttributes.addFlashAttribute("success", "Заказ отменен");
        return "redirect:/orders";
    }
}