package com.webstore.webstore.controller;

import com.webstore.webstore.entity.Product;
import com.webstore.webstore.service.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final ProductService productService;

    public AdminController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String adminProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/products";
    }

    @GetMapping("/products/new")
    public String newProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin/product-form";
    }

    @PostMapping("/products")
    public String createProduct(@RequestParam String name,
                                @RequestParam String description,
                                @RequestParam BigDecimal price,
                                @RequestParam String imagePath,
                                @RequestParam Integer stock,
                                RedirectAttributes redirectAttributes) {
        Product product = new Product(name, description, price, imagePath, stock);
        productService.saveProduct(product);

        redirectAttributes.addFlashAttribute("success", "Товар успешно создан");
        return "redirect:/admin/products";
    }

    @GetMapping("/products/{id}/edit")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Товар не найден"));

        model.addAttribute("product", product);
        return "admin/product-form";
    }

    @PostMapping("/products/{id}")
    public String updateProduct(@PathVariable Long id,
                                @RequestParam String name,
                                @RequestParam String description,
                                @RequestParam BigDecimal price,
                                @RequestParam String imagePath,
                                @RequestParam Integer stock,
                                RedirectAttributes redirectAttributes) {
        Product productDetails = new Product(name, description, price, imagePath, stock);
        productService.updateProduct(id, productDetails);

        redirectAttributes.addFlashAttribute("success", "Товар успешно обновлен");
        return "redirect:/admin/products";
    }

    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("success", "Товар успешно удален");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/products";
    }
}