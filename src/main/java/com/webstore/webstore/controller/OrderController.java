package com.webstore.webstore.controller;

import com.webstore.webstore.entity.CartItem;
import com.webstore.webstore.entity.Order;
import com.webstore.webstore.entity.OrderItem;
import com.webstore.webstore.entity.User;
import com.webstore.webstore.service.CartService;
import com.webstore.webstore.service.OrderService;
import com.webstore.webstore.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class OrderController {

    private final CartService cartService;
    private final OrderService orderService;
    private final UserService userService;

    public OrderController(CartService cartService, OrderService orderService, UserService userService) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/order")
    public String orderPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByUsernameEntity(userDetails.getUsername());
        List<CartItem> cartItems = cartService.getCartItems(user);

        if (cartItems.isEmpty()) {
            model.addAttribute("empty", true);
            return "order";
        }

        model.addAttribute("items", cartItems);

        BigDecimal total = cartItems.stream()
                .map(item -> item.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("total", total);

        return "order";
    }

    @PostMapping("/order/confirm")
    public String confirmOrder(@AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByUsernameEntity(userDetails.getUsername());
        List<CartItem> cartItems = cartService.getCartItems(user);

        if (cartItems.isEmpty()) {
            return "redirect:/cart?error=empty";
        }

        // теперь вызываем реальный OrderService.createOrder(user)
        Order order = orderService.createOrder(user);

        // добавляем товары
        for (CartItem cart : cartItems) {
            orderService.addProductToOrder(order.getId(),
                    cart.getProduct().getId(),
                    cart.getQuantity());
        }

        // очищаем корзину
        cartService.clearCart(user);

        return "redirect:/order/success?id=" + order.getId();
    }

    @GetMapping("/order/success")
    public String orderSuccess(Long id, Model model) {

        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        model.addAttribute("order", order);

        return "order_success";
    }
}