package com.example.alcoholshop.dao.impl;

import com.example.alcoholshop.dao.ContactDAO;
import com.example.alcoholshop.model.ContactMessage;
import com.example.alcoholshop.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of ContactDAO using JDBC
 */
public class ContactDAOImpl implements ContactDAO {
    private static final Logger logger = LoggerFactory.getLogger(ContactDAOImpl.class);
    
    @Override
    public List<ContactMessage> findAll() {
        String sql = "SELECT * FROM contact_message ORDER BY created_at DESC";
        List<ContactMessage> messages = new ArrayList<>();
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                messages.add(mapResultSetToContactMessage(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding all contact messages", e);
        }
        
        return messages;
    }
    
    @Override
    public ContactMessage findById(int id) {
        String sql = "SELECT * FROM contact_message WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToContactMessage(rs);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error finding contact message by ID: " + id, e);
        }
        
        return null;
    }
    
    @Override
    public List<ContactMessage> findByEmail(String email) {
        String sql = "SELECT * FROM contact_message WHERE email = ? ORDER BY created_at DESC";
        List<ContactMessage> messages = new ArrayList<>();
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    messages.add(mapResultSetToContactMessage(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error finding contact messages by email: " + email, e);
        }
        
        return messages;
    }
    
    @Override
    public boolean create(ContactMessage contactMessage) {
        String sql = "INSERT INTO contact_message (name, email, phone, subject, message) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, contactMessage.getName());
            stmt.setString(2, contactMessage.getEmail());
            stmt.setString(3, contactMessage.getPhone());
            stmt.setString(4, contactMessage.getSubject());
            stmt.setString(5, contactMessage.getMessage());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        contactMessage.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            
        } catch (SQLException e) {
            logger.error("Error creating contact message", e);
        }
        
        return false;
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM contact_message WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            logger.error("Error deleting contact message: " + id, e);
        }
        
        return false;
    }
    
    @Override
    public int getTotalCount() {
        String sql = "SELECT COUNT(*) FROM contact_message";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            logger.error("Error getting total contact message count", e);
        }
        
        return 0;
    }
    
    /**
     * Map ResultSet to ContactMessage object
     */
    private ContactMessage mapResultSetToContactMessage(ResultSet rs) throws SQLException {
        ContactMessage message = new ContactMessage();
        message.setId(rs.getInt("id"));
        message.setName(rs.getString("name"));
        message.setEmail(rs.getString("email"));
        message.setPhone(rs.getString("phone"));
        message.setSubject(rs.getString("subject"));
        message.setMessage(rs.getString("message"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            message.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return message;
    }
}

