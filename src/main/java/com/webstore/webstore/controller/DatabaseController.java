package com.webstore.webstore.controller;

import com.webstore.webstore.entity.User;
import com.webstore.webstore.entity.Product;
import com.webstore.webstore.repository.UserRepository;
import com.webstore.webstore.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class DatabaseController {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    // Конструктор с dependency injection
    public DatabaseController(UserRepository userRepository,
                              ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    /**
     * Перенаправление на H2 Console
     */
    @GetMapping("/db")
    public String databaseRedirect() {
        return "redirect:/h2-console";
    }

    /**
     * Страница с информацией о базе данных
     */
    @GetMapping("/db-info")
    public String databaseInfo(Model model) {
        model.addAttribute("dbUrl", "jdbc:h2:mem:webstore");
        model.addAttribute("dbUser", "sa");
        model.addAttribute("dbPassword", "");

        // Добавляем статистику
        long userCount = userRepository.count();
        long productCount = productRepository.count();

        model.addAttribute("userCount", userCount);
        model.addAttribute("productCount", productCount);

        return "db-info"; // Это будет искать файл templates/db-info.html
    }

    /**
     * Простой JSON endpoint для проверки базы данных
     * (открыть в браузере: http://localhost:8080/db-test)
     */
    @GetMapping("/db-test")
    @ResponseBody
    public String testDatabase() {
        long userCount = userRepository.count();
        long productCount = productRepository.count();

        return String.format("""
            Database Test Results:
            ---------------------
            Users in database: %d
            Products in database: %d
            Database URL: jdbc:h2:mem:webstore
            H2 Console: http://localhost:8080/h2-console
            """, userCount, productCount);
    }

    /**
     * Страница для просмотра всех пользователей (для отладки)
     */
    @GetMapping("/admin/view-users")
    public String viewUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin/view-users";
    }

    /**
     * Страница для просмотра всех товаров (для отладки)
     */
    @GetMapping("/admin/view-products")
    public String viewProducts(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "admin/view-products";
    }
}