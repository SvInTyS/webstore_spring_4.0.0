package com.webstore.webstore.service;

import com.webstore.webstore.entity.CartItem;
import com.webstore.webstore.entity.Product;
import com.webstore.webstore.entity.User;
import com.webstore.webstore.repository.CartItemRepository;
import com.webstore.webstore.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartService(CartItemRepository cartItemRepository,
                       ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    public List<CartItem> getCartItems(User user) {
        return cartItemRepository.findByUser(user);
    }

    @Transactional
    public void addToCart(User user, Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Товар не найден"));

        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Недостаточно товара на складе");
        }

        CartItem item = cartItemRepository.findByUserAndProduct(user, product)
                .orElse(new CartItem(user, product, 0));

        item.setQuantity(item.getQuantity() + quantity);
        cartItemRepository.save(item);
    }

    @Transactional
    public void removeItem(Long itemId) {
        cartItemRepository.deleteById(itemId);
    }

    @Transactional
    public void clearCart(User user) {
        cartItemRepository.deleteByUser(user);
    }
}