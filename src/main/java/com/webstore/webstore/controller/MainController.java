package com.webstore.webstore.controller;

import com.webstore.webstore.repository.ProductRepository;
import com.webstore.webstore.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public MainController(UserRepository userRepository,
                          ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/")
    public String index(Model model) {

        model.addAttribute("usersCount", userRepository.count());
        model.addAttribute("productsCount", productRepository.count());

        return "index";
    }
}