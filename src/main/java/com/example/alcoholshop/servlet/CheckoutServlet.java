package com.example.alcoholshop.servlet;

import com.example.alcoholshop.dao.OrderDAO;
import com.example.alcoholshop.dao.ProductDAO;
import com.example.alcoholshop.dao.impl.OrderDAOImpl;
import com.example.alcoholshop.dao.impl.ProductDAOImpl;
import com.example.alcoholshop.model.Order;
import com.example.alcoholshop.model.OrderItem;
import com.example.alcoholshop.model.Product;
import com.example.alcoholshop.model.UserAccount;
import com.example.alcoholshop.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Checkout servlet to handle order processing
 */
@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutServlet.class);
    
    private OrderDAO orderDAO;
    private ProductDAO productDAO;
    
    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAOImpl();
        productDAO = new ProductDAOImpl();
        logger.info("CheckoutServlet initialized");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/pages/auth/login.jsp");
            return;
        }
        
        UserAccount currentUser = (UserAccount) session.getAttribute("currentUser");
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/pages/auth/login.jsp");
            return;
        }
        
        // Re-check age requirement
        if (!currentUser.isAdult()) {
            request.setAttribute("error", "You must be at least 18 years old to make a purchase");
            request.getRequestDispatcher("/pages/error/403.jsp").forward(request, response);
            return;
        }
        
        // Get cart from session
        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            request.setAttribute("error", "Your cart is empty");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }
        
        // Validate cart items and stock
        List<CartItem> cartItems = getCartItems(cart);
        if (cartItems.isEmpty()) {
            request.setAttribute("error", "No valid items in cart");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }
        
        BigDecimal total = calculateTotal(cartItems);
        
        request.setAttribute("cartItems", cartItems);
        request.setAttribute("total", total);
        request.setAttribute("user", currentUser);
        
        logger.info("Showing checkout for user: " + currentUser.getUsername() + " with " + cartItems.size() + " items");
        request.getRequestDispatcher("/pages/checkout.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/pages/auth/login.jsp");
            return;
        }
        
        UserAccount currentUser = (UserAccount) session.getAttribute("currentUser");
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/pages/auth/login.jsp");
            return;
        }
        
        // Re-check age requirement
        if (!currentUser.isAdult()) {
            request.setAttribute("error", "You must be at least 18 years old to make a purchase");
            request.getRequestDispatcher("/pages/error/403.jsp").forward(request, response);
            return;
        }
        
        // Get cart from session
        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            request.setAttribute("error", "Your cart is empty");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }
        
        try {
            // Process order in transaction
            Order order = processOrder(currentUser, cart);
            
            if (order != null) {
                // Clear cart
                session.removeAttribute("cart");
                
                logger.info("Order processed successfully: " + order.getId() + " for user: " + currentUser.getUsername());
                
                // Redirect to order confirmation
                response.sendRedirect(request.getContextPath() + "/pages/order-confirmation.jsp?orderId=" + order.getId());
            } else {
                request.setAttribute("error", "Unable to process order. Please try again.");
                request.getRequestDispatcher("/pages/checkout.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            logger.error("Error processing order for user: " + currentUser.getUsername(), e);
            request.setAttribute("error", "Unable to process order. Please try again later.");
            request.getRequestDispatcher("/pages/checkout.jsp").forward(request, response);
        }
    }
    
    /**
     * Process order with transaction
     */
    private Order processOrder(UserAccount user, Map<Integer, Integer> cart) {
        // This is a simplified version - in a real application, you would use proper transaction management
        try {
            // Get cart items with product details
            List<CartItem> cartItems = getCartItems(cart);
            if (cartItems.isEmpty()) {
                return null;
            }
            
            // Calculate total
            BigDecimal total = calculateTotal(cartItems);
            
            // Create order
            Order order = new Order();
            order.setUserId(user.getId());
            order.setTotal(total);
            order.setStatus("PENDING");
            
            if (!orderDAO.create(order)) {
                logger.error("Failed to create order for user: " + user.getUsername());
                return null;
            }
            
            // Create order items and update stock
            for (CartItem cartItem : cartItems) {
                // Create order item
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(order.getId());
                orderItem.setProductId(cartItem.getProductId());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPrice(cartItem.getProductPrice());
                
                // Update product stock
                if (!productDAO.decreaseStock(cartItem.getProductId(), cartItem.getQuantity())) {
                    logger.error("Failed to update stock for product: " + cartItem.getProductId());
                    // In a real application, you would rollback the transaction here
                    return null;
                }
            }
            
            return order;
            
        } catch (Exception e) {
            logger.error("Error processing order", e);
            return null;
        }
    }
    
    /**
     * Get cart items with product details
     */
    private List<CartItem> getCartItems(Map<Integer, Integer> cart) {
        List<CartItem> cartItems = new ArrayList<>();
        
        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
            int productId = entry.getKey();
            int quantity = entry.getValue();
            
            Product product = productDAO.findById(productId);
            if (product != null && product.hasStock(quantity)) {
                CartItem cartItem = new CartItem();
                cartItem.setProductId(productId);
                cartItem.setProductName(product.getName());
                cartItem.setProductCode(product.getCode());
                cartItem.setProductImage(product.getImage());
                cartItem.setProductPrice(product.getPrice());
                cartItem.setQuantity(quantity);
                
                cartItems.add(cartItem);
            }
        }
        
        return cartItems;
    }
    
    /**
     * Calculate cart total
     */
    private BigDecimal calculateTotal(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * Cart item class for checkout
     */
    public static class CartItem {
        private int productId;
        private String productName;
        private String productCode;
        private String productImage;
        private BigDecimal productPrice;
        private int quantity;
        
        // Getters and setters
        public int getProductId() { return productId; }
        public void setProductId(int productId) { this.productId = productId; }
        
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        
        public String getProductCode() { return productCode; }
        public void setProductCode(String productCode) { this.productCode = productCode; }
        
        public String getProductImage() { return productImage; }
        public void setProductImage(String productImage) { this.productImage = productImage; }
        
        public BigDecimal getProductPrice() { return productPrice; }
        public void setProductPrice(BigDecimal productPrice) { this.productPrice = productPrice; }
        
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        
        public BigDecimal getSubtotal() {
            if (productPrice == null) return BigDecimal.ZERO;
            return productPrice.multiply(BigDecimal.valueOf(quantity));
        }
        
        public String getFormattedProductPrice() {
            if (productPrice == null) return "$0.00";
            return String.format("$%.2f", productPrice);
        }
        
        public String getFormattedSubtotal() {
            return String.format("$%.2f", getSubtotal());
        }
    }
}

