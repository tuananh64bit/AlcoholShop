package com.example.alcoholshop.dao;

import com.example.alcoholshop.model.Product;
import java.util.List;

/**
 * Data Access Object interface for Product operations
 */
public interface ProductDAO {
    
    /**
     * Get all products
     */
    List<Product> findAll();
    
    /**
     * Get product by ID
     */
    Product findById(int id);
    
    /**
     * Get product by code
     */
    Product findByCode(String code);
    
    /**
     * Get products by category
     */
    List<Product> findByCategory(int categoryId);
    
    /**
     * Search products by name or description
     */
    List<Product> search(String keyword);
    
    /**
     * Get featured products (newest or most popular)
     */
    List<Product> findFeatured(int limit);
    
    /**
     * Get products with stock
     */
    List<Product> findInStock();
    
    /**
     * Create new product
     */
    boolean create(Product product);
    
    /**
     * Update existing product
     */
    boolean update(Product product);
    
    /**
     * Delete product by ID
     */
    boolean delete(int id);
    
    /**
     * Update product stock
     */
    boolean updateStock(int productId, int newStock);
    
    /**
     * Decrease product stock by quantity
     */
    boolean decreaseStock(int productId, int quantity);
    
    /**
     * Check if product has sufficient stock
     */
    boolean hasStock(int productId, int quantity);
    
    /**
     * Get total product count
     */
    int getTotalCount();
    
    /**
     * Get product count by category
     */
    int getCountByCategory(int categoryId);
}

