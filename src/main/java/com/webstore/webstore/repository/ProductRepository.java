package com.webstore.webstore.repository;

import com.webstore.webstore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByOrderByPriceAsc();
    List<Product> findByOrderByPriceDesc();
}