package com.webstore.webstore.controller;

import com.webstore.webstore.entity.User;
import com.webstore.webstore.service.CartService;
import com.webstore.webstore.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;


@Controller
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping("/cart")
    public String viewCart(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByUsernameEntity(userDetails.getUsername());

        var cartItems = cartService.getCartItems(user);

        model.addAttribute("items", cartItems);
        BigDecimal total = cartItems.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("total", total);


        return "cart";
    }

    @PostMapping("/cart/add/{productId}")
    public String addToCart(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") int quantity,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = userService.findByUsernameEntity(userDetails.getUsername());
        cartService.addToCart(user, productId, quantity);

        return "redirect:/cart";
    }

    @PostMapping("/cart/remove/{itemId}")
    public String removeItem(@PathVariable Long itemId) {
        cartService.removeItem(itemId);
        return "redirect:/cart";
    }

    @PostMapping("/cart/clear")
    public String clear(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsernameEntity(userDetails.getUsername());
        cartService.clearCart(user);
        return "redirect:/cart";
    }
}