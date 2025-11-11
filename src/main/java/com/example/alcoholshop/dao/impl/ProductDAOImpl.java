package com.example.alcoholshop.dao.impl;

import com.example.alcoholshop.dao.ProductDAO;
import com.example.alcoholshop.model.Product;
import com.example.alcoholshop.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of ProductDAO using JDBC
 */
public class ProductDAOImpl implements ProductDAO {
    private static final Logger logger = LoggerFactory.getLogger(ProductDAOImpl.class);
    
    @Override
    public List<Product> findAll() {
        String sql = "SELECT p.*, c.name as category_name FROM product p " +
                   "LEFT JOIN category c ON p.category_id = c.id " +
                   "ORDER BY p.created_at DESC";
        
        List<Product> products = new ArrayList<>();
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding all products", e);
        }
        
        return products;
    }
    
    @Override
    public Product findById(int id) {
        String sql = "SELECT p.*, c.name as category_name FROM product p " +
                     "LEFT JOIN category c ON p.category_id = c.id " +
                     "WHERE p.id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduct(rs);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error finding product by ID: " + id, e);
        }
        
        return null;
    }
    
    @Override
    public Product findByCode(String code) {
        String sql = "SELECT p.*, c.name as category_name FROM product p " +
                     "LEFT JOIN category c ON p.category_id = c.id " +
                     "WHERE p.code = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, code);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduct(rs);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error finding product by code: " + code, e);
        }
        
        return null;
    }
    
    @Override
    public List<Product> findByCategory(int categoryId) {
        String sql = "SELECT p.*, c.name as category_name FROM product p " +
                     "LEFT JOIN category c ON p.category_id = c.id " +
                     "WHERE p.category_id = ? " +
                     "ORDER BY p.created_at DESC";
        
        List<Product> products = new ArrayList<>();
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, categoryId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error finding products by category: " + categoryId, e);
        }
        
        return products;
    }
    
    @Override
    public List<Product> search(String keyword) {
        String sql = "SELECT p.*, c.name as category_name FROM product p " +
                     "LEFT JOIN category c ON p.category_id = c.id " +
                     "WHERE p.name LIKE ? OR p.description LIKE ? " +
                     "ORDER BY p.created_at DESC";
        
        List<Product> products = new ArrayList<>();
        String searchPattern = "%" + keyword + "%";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error searching products with keyword: " + keyword, e);
        }
        
        return products;
    }
    
    @Override
    public List<Product> findFeatured(int limit) {
        String sql = "SELECT p.*, c.name as category_name FROM product p " +
                     "LEFT JOIN category c ON p.category_id = c.id " +
                     "WHERE p.stock > 0 " +
                     "ORDER BY p.created_at DESC " +
                     "LIMIT ?";
        
        List<Product> products = new ArrayList<>();
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error finding featured products", e);
        }
        
        return products;
    }
    
    @Override
    public List<Product> findInStock() {
        String sql = "SELECT p.*, c.name as category_name FROM product p " +
                     "LEFT JOIN category c ON p.category_id = c.id " +
                     "WHERE p.stock > 0 " +
                     "ORDER BY p.created_at DESC";
        
        List<Product> products = new ArrayList<>();
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding products in stock", e);
        }
        
        return products;
    }
    
    @Override
    public boolean create(Product product) {
        String sql = "INSERT INTO product (code, name, description, price, stock, image, category_id, alcohol_percentage) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, product.getCode());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getDescription());
            stmt.setBigDecimal(4, product.getPrice());
            stmt.setInt(5, product.getStock());
            stmt.setString(6, product.getImage());
            stmt.setInt(7, product.getCategoryId());
            stmt.setBigDecimal(8, product.getAlcoholPercentage());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        product.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            
        } catch (SQLException e) {
            logger.error("Error creating product", e);
        }
        
        return false;
    }
    
    @Override
    public boolean update(Product product) {
        String sql = "UPDATE product SET code = ?, name = ?, description = ?, price = ?, " +
                "stock = ?, image = ?, category_id = ?, alcohol_percentage = ? WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, product.getCode());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getDescription());
            stmt.setBigDecimal(4, product.getPrice());
            stmt.setInt(5, product.getStock());
            stmt.setString(6, product.getImage());
            stmt.setInt(7, product.getCategoryId());
            stmt.setBigDecimal(8, product.getAlcoholPercentage());
            stmt.setInt(9, product.getId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            logger.error("Error updating product: " + product.getId(), e);
        }
        
        return false;
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM product WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            logger.error("Error deleting product: " + id, e);
        }
        
        return false;
    }
    
    @Override
    public boolean updateStock(int productId, int newStock) {
        String sql = "UPDATE product SET stock = ? WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, newStock);
            stmt.setInt(2, productId);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            logger.error("Error updating stock for product: " + productId, e);
        }
        
        return false;
    }
    
    @Override
    public boolean decreaseStock(int productId, int quantity) {
        String sql = "UPDATE product SET stock = stock - ? WHERE id = ? AND stock >= ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, quantity);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantity);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            logger.error("Error decreasing stock for product: " + productId, e);
        }
        
        return false;
    }
    
    @Override
    public boolean hasStock(int productId, int quantity) {
        String sql = "SELECT stock FROM product WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, productId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int stock = rs.getInt("stock");
                    return stock >= quantity;
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error checking stock for product: " + productId, e);
        }
        
        return false;
    }
    
    @Override
    public int getTotalCount() {
        String sql = "SELECT COUNT(*) FROM product";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            logger.error("Error getting total product count", e);
        }
        
        return 0;
    }
    
    @Override
    public int getCountByCategory(int categoryId) {
        String sql = "SELECT COUNT(*) FROM product WHERE category_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, categoryId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error getting product count by category: " + categoryId, e);
        }
        
        return 0;
    }
    
    /**
     * Map ResultSet to Product object
     */
    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setCode(rs.getString("code"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setStock(rs.getInt("stock"));
        product.setImage(rs.getString("image"));
        product.setCategoryId(rs.getInt("category_id"));
        product.setCategoryName(rs.getString("category_name"));
        product.setAlcoholPercentage(rs.getBigDecimal("alcohol_percentage"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            product.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return product;
    }
}

