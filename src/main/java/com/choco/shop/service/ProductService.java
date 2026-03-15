package com.choco.shop.service;

import com.choco.shop.entity.Product;
import java.util.List;
import java.util.Optional; // Added import for Optional

public interface ProductService {
    List<Product> findAll();

    Optional<Product> findById(Long id); // Changed return type to Optional<Product>

    // Removed findByCategoryId(Long categoryId); as it was not in the provided
    // snippet

    void save(Product product); // Changed return type from Product to void

    void deleteById(Long id);
}
