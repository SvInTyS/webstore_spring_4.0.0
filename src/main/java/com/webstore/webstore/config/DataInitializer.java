package com.webstore.webstore.config;

import com.webstore.webstore.entity.Product;
import com.webstore.webstore.entity.Role;
import com.webstore.webstore.entity.User;
import com.webstore.webstore.repository.ProductRepository;
import com.webstore.webstore.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Arrays;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository,
                                      ProductRepository productRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            // Создаем администратора
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setEmail("admin@webstore.com");
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);
                System.out.println("Создан администратор: admin / admin123");
            }

            // Создаем тестового пользователя
            if (userRepository.findByUsername("user").isEmpty()) {
                User user = new User();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setEmail("user@webstore.com");
                user.setRole(Role.USER);
                userRepository.save(user);
                System.out.println("Создан пользователь: user / user123");
            }

            // Создаем тестовые товары, если их нет
            if (productRepository.count() == 0) {
                Product[] products = {
                        new Product("Ноутбук", "Мощный игровой ноутбук",
                                new BigDecimal("999.99"), "/images/laptop.jpg", 10),
                        new Product("Смартфон", "Флагманский смартфон",
                                new BigDecimal("799.99"), "/images/phone.jpg", 15),
                        new Product("Наушники", "Беспроводные наушники",
                                new BigDecimal("199.99"), "/images/headphones.jpg", 30),
                        new Product("Клавиатура", "Механическая клавиатура",
                                new BigDecimal("129.99"), "/images/keyboard.jpg", 25),
                        new Product("Монитор", "4K монитор 27 дюймов",
                                new BigDecimal("449.99"), "/images/monitor.jpg", 8)
                };

                productRepository.saveAll(Arrays.asList(products));
                System.out.println("Создано " + products.length + " тестовых товаров");
            }
        };
    }
}