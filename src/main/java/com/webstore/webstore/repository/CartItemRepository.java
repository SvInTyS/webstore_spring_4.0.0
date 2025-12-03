package com.webstore.webstore.repository;

import com.webstore.webstore.entity.CartItem;
import com.webstore.webstore.entity.User;
import com.webstore.webstore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUser(User user);

    Optional<CartItem> findByUserAndProduct(User user, Product product);

    void deleteByUser(User user);
}