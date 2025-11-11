package com.example.alcoholshop.dao;

import com.example.alcoholshop.model.Order;
import java.util.List;

/**
 * Data Access Object interface for Order operations
 */
public interface OrderDAO {
    
    /**
     * Get all orders
     */
    List<Order> findAll();
    
    /**
     * Get order by ID
     */
    Order findById(int id);
    
    /**
     * Get orders by user ID
     */
    List<Order> findByUserId(int userId);
    
    /**
     * Get orders by status
     */
    List<Order> findByStatus(String status);
    
    /**
     * Create new order
     */
    boolean create(Order order);
    
    /**
     * Update existing order
     */
    boolean update(Order order);
    
    /**
     * Update order status
     */
    boolean updateStatus(int orderId, String status);
    
    /**
     * Delete order by ID
     */
    boolean delete(int id);
    
    /**
     * Get total order count
     */
    int getTotalCount();
    
    /**
     * Get order count by status
     */
    int getCountByStatus(String status);
    
    /**
     * Get order count by user
     */
    int getCountByUser(int userId);
}

