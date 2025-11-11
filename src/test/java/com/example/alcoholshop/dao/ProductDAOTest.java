package com.example.alcoholshop.dao;

import com.example.alcoholshop.dao.impl.ProductDAOImpl;
import com.example.alcoholshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ProductDAO
 */
@DisplayName("ProductDAO Tests")
public class ProductDAOTest {
    
    private ProductDAO productDAO;
    
    @BeforeEach
    void setUp() {
        productDAO = new ProductDAOImpl();
    }
    
    @Test
    @DisplayName("Should find all products")
    void testFindAll() {
        List<Product> products = productDAO.findAll();
        assertNotNull(products, "Products list should not be null");
        assertTrue(products.size() >= 30, "Should have at least 30 products");
    }
    
    @Test
    @DisplayName("Should find product by ID")
    void testFindById() {
        Product product = productDAO.findById(1);
        assertNotNull(product, "Product should not be null");
        assertEquals(1, product.getId(), "Product ID should match");
        assertNotNull(product.getName(), "Product name should not be null");
        assertNotNull(product.getCode(), "Product code should not be null");
    }
    
    @Test
    @DisplayName("Should find product by code")
    void testFindByCode() {
        Product product = productDAO.findByCode("P001");
        assertNotNull(product, "Product should not be null");
        assertEquals("P001", product.getCode(), "Product code should match");
    }
    
    @Test
    @DisplayName("Should find products by category")
    void testFindByCategory() {
        List<Product> products = productDAO.findByCategory(1);
        assertNotNull(products, "Products list should not be null");
        assertTrue(products.size() > 0, "Should have products in category");
        
        // Verify all products belong to the same category
        for (Product product : products) {
            assertEquals(1, product.getCategoryId(), "All products should belong to category 1");
        }
    }
    
    @Test
    @DisplayName("Should search products")
    void testSearch() {
        List<Product> products = productDAO.search("whiskey");
        assertNotNull(products, "Search results should not be null");
        assertTrue(products.size() > 0, "Should find products matching search term");
    }
    
    @Test
    @DisplayName("Should find featured products")
    void testFindFeatured() {
        List<Product> products = productDAO.findFeatured(6);
        assertNotNull(products, "Featured products should not be null");
        assertTrue(products.size() <= 6, "Should not exceed limit");
        assertTrue(products.size() > 0, "Should have featured products");
    }
    
    @Test
    @DisplayName("Should find products in stock")
    void testFindInStock() {
        List<Product> products = productDAO.findInStock();
        assertNotNull(products, "In-stock products should not be null");
        
        // Verify all products are in stock
        for (Product product : products) {
            assertTrue(product.getStock() > 0, "All products should be in stock");
        }
    }
    
    @Test
    @DisplayName("Should create new product")
    void testCreate() {
        Product product = new Product();
        product.setCode("TEST001");
        product.setName("Test Product");
        product.setDescription("Test product description");
        product.setPrice(new BigDecimal("29.99"));
        product.setStock(10);
        product.setImage("test.jpg");
        product.setCategoryId(1);
        product.setAlcoholPercentage(new BigDecimal("40.0"));
        
        boolean created = productDAO.create(product);
        assertTrue(created, "Product should be created successfully");
        assertTrue(product.getId() > 0, "Product should have an ID after creation");
    }
    
    @Test
    @DisplayName("Should update product")
    void testUpdate() {
        // First create a product
        Product product = new Product();
        product.setCode("TEST002");
        product.setName("Test Product 2");
        product.setDescription("Test product description 2");
        product.setPrice(new BigDecimal("39.99"));
        product.setStock(5);
        product.setImage("test2.jpg");
        product.setCategoryId(1);
        product.setAlcoholPercentage(new BigDecimal("35.0"));
        
        boolean created = productDAO.create(product);
        assertTrue(created, "Product should be created");
        
        // Update the product
        product.setName("Updated Test Product");
        product.setPrice(new BigDecimal("49.99"));
        
        boolean updated = productDAO.update(product);
        assertTrue(updated, "Product should be updated successfully");
    }
    
    @Test
    @DisplayName("Should check stock availability")
    void testHasStock() {
        Product product = productDAO.findById(1);
        if (product != null) {
            boolean hasStock = productDAO.hasStock(product.getId(), 1);
            assertTrue(hasStock, "Product should have stock for quantity 1");
        }
    }
    
    @Test
    @DisplayName("Should get total product count")
    void testGetTotalCount() {
        int totalCount = productDAO.getTotalCount();
        assertTrue(totalCount >= 30, "Should have at least 30 products");
    }
    
    @Test
    @DisplayName("Should get product count by category")
    void testGetCountByCategory() {
        int count = productDAO.getCountByCategory(1);
        assertTrue(count > 0, "Should have products in category 1");
    }
}

