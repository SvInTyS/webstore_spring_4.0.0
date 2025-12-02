package com.webstore.webstore.controller;

import com.webstore.webstore.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error,
                        @RequestParam(required = false) String logout,
                        Model model) {
        if (error != null) {
            model.addAttribute("error", "Неверное имя пользователя или пароль");
        }
        if (logout != null) {
            model.addAttribute("message", "Вы успешно вышли из системы");
        }
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String email,
                           Model model) {
        try {
            // Валидация
            if (username == null || username.trim().isEmpty()) {
                model.addAttribute("error", "Имя пользователя обязательно");
                return "register";
            }

            if (password == null || password.length() < 6) {
                model.addAttribute("error", "Пароль должен содержать минимум 6 символов");
                return "register";
            }

            if (email == null || !email.contains("@")) {
                model.addAttribute("error", "Введите корректный email");
                return "register";
            }

            userService.registerUser(username, password, email);
            return "redirect:/login?registered";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            return "register";
        }
    }
}