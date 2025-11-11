package com.example.alcoholshop.model;

import java.time.LocalDateTime;

/**
 * CartItem model class representing items in shopping cart
 */
public class CartItem {
    private int id;
    private int userId;
    private int productId;
    private String productName;
    private String productCode;
    private String productImage;
    private java.math.BigDecimal productPrice;
    private int quantity;
    private LocalDateTime addedAt;
    
    // Constructors
    public CartItem() {}
    
    public CartItem(int userId, int productId, int quantity) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getProductId() {
        return productId;
    }
    
    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public String getProductCode() {
        return productCode;
    }
    
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    
    public String getProductImage() {
        return productImage;
    }
    
    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
    
    public java.math.BigDecimal getProductPrice() {
        return productPrice;
    }
    
    public void setProductPrice(java.math.BigDecimal productPrice) {
        this.productPrice = productPrice;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public LocalDateTime getAddedAt() {
        return addedAt;
    }
    
    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }
    
    /**
     * Calculate subtotal for this cart item
     */
    public java.math.BigDecimal getSubtotal() {
        if (productPrice == null) {
            return java.math.BigDecimal.ZERO;
        }
        return productPrice.multiply(java.math.BigDecimal.valueOf(quantity));
    }
    
    /**
     * Get formatted product price
     */
    public String getFormattedProductPrice() {
        if (productPrice == null) {
            return "$0.00";
        }
        return String.format("$%.2f", productPrice);
    }
    
    /**
     * Get formatted subtotal
     */
    public String getFormattedSubtotal() {
        return String.format("$%.2f", getSubtotal());
    }
    
    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", userId=" + userId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", addedAt=" + addedAt +
                '}';
    }
}

