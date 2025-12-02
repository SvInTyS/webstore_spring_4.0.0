package com.webstore.webstore.service;

import com.webstore.webstore.entity.Product;
import com.webstore.webstore.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCase(query);
    }

    public List<Product> getProductsSortedByPriceAsc() {
        return productRepository.findByOrderByPriceAsc();
    }

    public List<Product> getProductsSortedByPriceDesc() {
        return productRepository.findByOrderByPriceDesc();
    }

    @Transactional
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Товар не найден с ID: " + id));

        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setImagePath(productDetails.getImagePath());
        product.setStock(productDetails.getStock());

        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Товар не найден с ID: " + id);
        }
        productRepository.deleteById(id);
    }
}