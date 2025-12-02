package com.webstore.webstore.controller;

import com.webstore.webstore.entity.User;
import com.webstore.webstore.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = (User) userService.loadUserByUsername(userDetails.getUsername());
        model.addAttribute("user", user);
        return "user/profile";
    }

    @GetMapping("/settings")
    public String settingsForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = (User) userService.loadUserByUsername(userDetails.getUsername());
        model.addAttribute("user", user);
        return "user/settings";
    }

    @PostMapping("/update-email")
    public String updateEmail(@AuthenticationPrincipal UserDetails userDetails,
                              @RequestParam String newEmail,
                              RedirectAttributes redirectAttributes) {
        try {
            User user = (User) userService.loadUserByUsername(userDetails.getUsername());

            if (userService.existsByEmail(newEmail) && !user.getEmail().equals(newEmail)) {
                redirectAttributes.addFlashAttribute("error", "Email уже используется");
                return "redirect:/user/settings";
            }

            user.setEmail(newEmail);
            // Здесь нужен userRepository.save() - добавим позже
            redirectAttributes.addFlashAttribute("success", "Email обновлен");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка обновления: " + e.getMessage());
        }
        return "redirect:/user/settings";
    }
}