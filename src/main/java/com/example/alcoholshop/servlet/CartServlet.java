package com.example.alcoholshop.servlet;

import com.example.alcoholshop.dao.ProductDAO;
import com.example.alcoholshop.dao.impl.ProductDAOImpl;
import com.example.alcoholshop.model.Product;
import com.example.alcoholshop.model.UserAccount;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cart servlet to handle shopping cart operations
 */
@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CartServlet.class);
    
    private ProductDAO productDAO;
    
    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAOImpl();
        logger.info("CartServlet initialized");
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
        
        // Get cart from session
        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }
        
        // Get cart items with product details
        List<CartItem> cartItems = getCartItems(cart);
        BigDecimal total = calculateTotal(cartItems);
        
        request.setAttribute("cartItems", cartItems);
        request.setAttribute("total", total);
        request.setAttribute("itemCount", cart.size());
        
        logger.info("Showing cart for user: " + currentUser.getUsername() + " with " + cart.size() + " items");
        request.getRequestDispatcher("/pages/cart.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("add".equals(action)) {
            addToCart(request, response);
        } else if ("update".equals(action)) {
            updateCart(request, response);
        } else if ("remove".equals(action)) {
            removeFromCart(request, response);
        } else if ("clear".equals(action)) {
            clearCart(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }
    
    /**
     * Add product to cart
     */
    private void addToCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            
            if (quantity <= 0) {
                request.setAttribute("error", "Quantity must be greater than 0");
                response.sendRedirect(request.getContextPath() + "/product?id=" + productId);
                return;
            }
            
            // Check product availability
            Product product = productDAO.findById(productId);
            if (product == null) {
                request.setAttribute("error", "Product not found");
                response.sendRedirect(request.getContextPath() + "/products");
                return;
            }
            
            if (!product.hasStock(quantity)) {
                request.setAttribute("error", "Insufficient stock. Available: " + product.getStock());
                response.sendRedirect(request.getContextPath() + "/product?id=" + productId);
                return;
            }
            
            // Add to session cart
            HttpSession session = request.getSession(true);
            @SuppressWarnings("unchecked")
            Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
            if (cart == null) {
                cart = new HashMap<>();
                session.setAttribute("cart", cart);
            }
            
            int currentQuantity = cart.getOrDefault(productId, 0);
            cart.put(productId, currentQuantity + quantity);
            
            logger.info("Added product " + productId + " (quantity: " + quantity + ") to cart");
            
            // Redirect back to product page or cart
            String redirectUrl = request.getParameter("redirect");
            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                response.sendRedirect(redirectUrl);
            } else {
                response.sendRedirect(request.getContextPath() + "/cart");
            }
            
        } catch (NumberFormatException e) {
            logger.error("Invalid product ID or quantity", e);
            request.setAttribute("error", "Invalid product ID or quantity");
            response.sendRedirect(request.getContextPath() + "/products");
        } catch (Exception e) {
            logger.error("Error adding to cart", e);
            request.setAttribute("error", "Unable to add item to cart");
            response.sendRedirect(request.getContextPath() + "/products");
        }
    }
    
    /**
     * Update cart item quantity
     */
    private void updateCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            
            HttpSession session = request.getSession(false);
            if (session == null) {
                response.sendRedirect(request.getContextPath() + "/pages/auth/login.jsp");
                return;
            }
            
            @SuppressWarnings("unchecked")
            Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
            if (cart == null) {
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }
            
            if (quantity <= 0) {
                cart.remove(productId);
            } else {
                // Check stock availability
                Product product = productDAO.findById(productId);
                if (product != null && product.hasStock(quantity)) {
                    cart.put(productId, quantity);
                } else {
                    request.setAttribute("error", "Insufficient stock");
                }
            }
            
            logger.info("Updated cart item " + productId + " to quantity: " + quantity);
            response.sendRedirect(request.getContextPath() + "/cart");
            
        } catch (NumberFormatException e) {
            logger.error("Invalid product ID or quantity", e);
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }
    
    /**
     * Remove item from cart
     */
    private void removeFromCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            
            HttpSession session = request.getSession(false);
            if (session == null) {
                response.sendRedirect(request.getContextPath() + "/pages/auth/login.jsp");
                return;
            }
            
            @SuppressWarnings("unchecked")
            Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
            if (cart != null) {
                cart.remove(productId);
                logger.info("Removed product " + productId + " from cart");
            }
            
            response.sendRedirect(request.getContextPath() + "/cart");
            
        } catch (NumberFormatException e) {
            logger.error("Invalid product ID", e);
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }
    
    /**
     * Clear entire cart
     */
    private void clearCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("cart");
            logger.info("Cart cleared");
        }
        
        response.sendRedirect(request.getContextPath() + "/cart");
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
            if (product != null) {
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
     * Cart item class for display
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

