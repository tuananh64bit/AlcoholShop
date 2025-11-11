package com.example.alcoholshop.dao;

import com.example.alcoholshop.model.UserAccount;
import java.util.List;

/**
 * Data Access Object interface for User operations
 */
public interface UserDAO {
    
    /**
     * Get all users
     */
    List<UserAccount> findAll();
    
    /**
     * Get user by ID
     */
    UserAccount findById(int id);
    
    /**
     * Get user by username
     */
    UserAccount findByUsername(String username);
    
    /**
     * Get user by email
     */
    UserAccount findByEmail(String email);
    
    /**
     * Get users by role
     */
    List<UserAccount> findByRole(String role);
    
    /**
     * Create new user
     */
    boolean create(UserAccount user);
    
    /**
     * Update existing user
     */
    boolean update(UserAccount user);
    
    /**
     * Delete user by ID
     */
    boolean delete(int id);
    
    /**
     * Update user role
     */
    boolean updateRole(int userId, String role);
    
    /**
     * Check if username exists
     */
    boolean existsByUsername(String username);
    
    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);
    
    /**
     * Get total user count
     */
    int getTotalCount();
    
    /**
     * Get user count by role
     */
    int getCountByRole(String role);
}

