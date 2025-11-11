package com.example.alcoholshop.dao;

import com.example.alcoholshop.model.Category;
import java.util.List;

/**
 * Data Access Object interface for Category operations
 */
public interface CategoryDAO {
    
    /**
     * Get all categories
     */
    List<Category> findAll();
    
    /**
     * Get category by ID
     */
    Category findById(int id);
    
    /**
     * Get category by name
     */
    Category findByName(String name);
    
    /**
     * Create new category
     */
    boolean create(Category category);
    
    /**
     * Update existing category
     */
    boolean update(Category category);
    
    /**
     * Delete category by ID
     */
    boolean delete(int id);
    
    /**
     * Get total category count
     */
    int getTotalCount();
}

