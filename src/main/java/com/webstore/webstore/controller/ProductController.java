package com.webstore.webstore.controller;

import com.webstore.webstore.entity.Product;
import com.webstore.webstore.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/catalog")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String catalog(@RequestParam(required = false) String search, Model model) {
        List<Product> products;

        if (search != null && !search.trim().isEmpty()) {
            products = productService.searchProducts(search);
            model.addAttribute("searchQuery", search);
        } else {
            products = productService.getAllProducts();
        }

        model.addAttribute("products", products);
        return "catalog";
    }

    @GetMapping("/{id}")
    public String productDetails(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Товар не найден"));

        model.addAttribute("product", product);
        return "product-details";
    }

    @GetMapping("/sort/price-asc")
    public String sortByPriceAsc(Model model) {
        model.addAttribute("products", productService.getProductsSortedByPriceAsc());
        return "catalog";
    }

    @GetMapping("/sort/price-desc")
    public String sortByPriceDesc(Model model) {
        model.addAttribute("products", productService.getProductsSortedByPriceDesc());
        return "catalog";
    }
}