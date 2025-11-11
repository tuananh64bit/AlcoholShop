package com.example.alcoholshop.servlet;

import com.example.alcoholshop.dao.ProductDAO;
import com.example.alcoholshop.dao.CategoryDAO;
import com.example.alcoholshop.dao.impl.ProductDAOImpl;
import com.example.alcoholshop.dao.impl.CategoryDAOImpl;
import com.example.alcoholshop.model.Product;
import com.example.alcoholshop.model.Category;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Home servlet to display featured products and categories
 */
@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(HomeServlet.class);
    
    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;
    
    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAOImpl();
        categoryDAO = new CategoryDAOImpl();
        logger.info("HomeServlet initialized");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Get featured products (newest 6 products)
            List<Product> featuredProducts = productDAO.findFeatured(6);
            request.setAttribute("featuredProducts", featuredProducts);
            
            // Get all categories for navigation
            List<Category> categories = categoryDAO.findAll();
            request.setAttribute("categories", categories);
            
            // Get total product count
            int totalProducts = productDAO.getTotalCount();
            request.setAttribute("totalProducts", totalProducts);
            
            logger.info("Loaded home page with {} featured products and {} categories", 
                       featuredProducts.size(), categories.size());
            
            // Forward to home page
            request.getRequestDispatcher("/pages/home.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.error("Error loading home page", e);
            request.setAttribute("error", "Unable to load home page. Please try again later.");
            request.getRequestDispatcher("/pages/error/500.jsp").forward(request, response);
        }
    }
}

