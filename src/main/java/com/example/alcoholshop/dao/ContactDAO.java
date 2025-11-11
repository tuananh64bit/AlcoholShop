package com.example.alcoholshop.dao;

import com.example.alcoholshop.model.ContactMessage;
import java.util.List;

/**
 * Data Access Object interface for Contact operations
 */
public interface ContactDAO {
    
    /**
     * Get all contact messages
     */
    List<ContactMessage> findAll();
    
    /**
     * Get contact message by ID
     */
    ContactMessage findById(int id);
    
    /**
     * Get contact messages by email
     */
    List<ContactMessage> findByEmail(String email);
    
    /**
     * Create new contact message
     */
    boolean create(ContactMessage contactMessage);
    
    /**
     * Delete contact message by ID
     */
    boolean delete(int id);
    
    /**
     * Get total contact message count
     */
    int getTotalCount();
}

