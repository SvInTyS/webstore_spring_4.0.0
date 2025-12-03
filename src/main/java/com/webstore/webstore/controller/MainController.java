package com.webstore.webstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home() {
        return "index";
    }
    /*
   Удалил дубль метода
    @GetMapping("/login")
    public String login() {
        return "login";
    }
   */
    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "Application is running! Database check: " +
                "Users: " + userRepository.count() + ", " +
                "Products: " + productRepository.count();
    }
}