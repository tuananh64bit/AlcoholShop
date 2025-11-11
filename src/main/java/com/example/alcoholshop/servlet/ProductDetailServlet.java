package com.example.alcoholshop.servlet;

import com.example.alcoholshop.dao.ProductDAO;
import com.example.alcoholshop.dao.impl.ProductDAOImpl;
import com.example.alcoholshop.model.Product;
import com.example.alcoholshop.util.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

/**
 * Product detail servlet to handle individual product pages
 */
@WebServlet("/product")
public class ProductDetailServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ProductDetailServlet.class);
    
    private ProductDAO productDAO;
    
    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAOImpl();
        logger.info("ProductDetailServlet initialized");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Quick DB connectivity check: if DB is not available, show 500 with helpful message
        try (Connection conn = DBUtil.getConnection()) {
            // Ensure connection is valid; this also uses the variable so IDE won't warn
            if (!conn.isValid(2)) {
                throw new IOException("DB connection is not valid");
            }
        } catch (Exception e) {
            logger.error("Database connection unavailable", e);
            request.setAttribute("error", "Database connection is not available. Please check database configuration and ensure the database is running.");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            request.getRequestDispatcher("/pages/error/500.jsp").forward(request, response);
            return;
        }

        String productId = request.getParameter("id");
        
        if (productId == null || productId.isEmpty()) {
            logger.warn("Product ID not provided");
            // Redirect to products listing if no id provided (more user-friendly than 404)
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }
        
        try {
            int id = Integer.parseInt(productId);
            Product product = productDAO.findById(id);
            
            if (product == null) {
                logger.warn("Product not found with ID: " + id);
                request.setAttribute("error", "Product not found");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                request.getRequestDispatcher("/pages/error/404.jsp").forward(request, response);
                return;
            }
            
            // Get related products from same category
            List<Product> relatedProducts = productDAO.findByCategory(product.getCategoryId());
            relatedProducts.removeIf(p -> p.getId() == product.getId()); // Remove current product
            if (relatedProducts.size() > 4) {
                relatedProducts = relatedProducts.subList(0, 4); // Limit to 4 products
            }
            
            request.setAttribute("product", product);
            request.setAttribute("relatedProducts", relatedProducts);
            
            logger.info("Showing product detail for: " + product.getName());
            request.getRequestDispatcher("/pages/product-detail.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            logger.error("Invalid product ID: " + productId, e);
            request.setAttribute("error", "Invalid product ID");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            request.getRequestDispatcher("/pages/error/404.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error("Error loading product detail", e);
            request.setAttribute("error", "Unable to load product. Please try again later.");
            request.getRequestDispatcher("/pages/error/500.jsp").forward(request, response);
        }
    }
}
