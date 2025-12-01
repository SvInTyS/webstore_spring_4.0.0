package com.webstore.webstore.repository;

import com.webstore.webstore.entity.Order;
import com.webstore.webstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByCreatedAtDesc(User user);
    List<Order> findByUser(User user);
}