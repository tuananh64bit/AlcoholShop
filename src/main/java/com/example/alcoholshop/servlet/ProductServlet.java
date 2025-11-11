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
 * Product servlet to handle product listing and details
 */
@WebServlet("/products")
public class ProductServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ProductServlet.class);
    
    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;
    
    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAOImpl();
        categoryDAO = new CategoryDAOImpl();
        logger.info("ProductServlet initialized");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String categoryId = request.getParameter("category");
        String search = request.getParameter("search");
        
        try {
            // Show product list
            showProductList(request, response, categoryId, search);
            
        } catch (Exception e) {
            logger.error("Error in ProductServlet", e);
            request.setAttribute("error", "Unable to load products. Please try again later.");
            request.getRequestDispatcher("/pages/error/500.jsp").forward(request, response);
        }
    }
    
    /**
     * Show product list page
     */
    private void showProductList(HttpServletRequest request, HttpServletResponse response, 
                                String categoryId, String search) throws ServletException, IOException {
        
        List<Product> products;
        String pageTitle = "All Products";
        
        if (search != null && !search.trim().isEmpty()) {
            // Search products
            products = productDAO.search(search.trim());
            pageTitle = "Search Results for: " + search;
            request.setAttribute("searchKeyword", search.trim());
        } else if (categoryId != null && !categoryId.isEmpty()) {
            // Filter by category
            try {
                int catId = Integer.parseInt(categoryId);
                products = productDAO.findByCategory(catId);
                
                // Get category name
                Category category = categoryDAO.findById(catId);
                if (category != null) {
                    pageTitle = category.getName();
                    request.setAttribute("selectedCategory", category);
                }
            } catch (NumberFormatException e) {
                logger.error("Invalid category ID: " + categoryId, e);
                products = productDAO.findAll();
            }
        } else {
            // Show all products
            products = productDAO.findAll();
        }
        
        // Get all categories for filter
        List<Category> categories = categoryDAO.findAll();
        
        request.setAttribute("products", products);
        request.setAttribute("categories", categories);
        request.setAttribute("pageTitle", pageTitle);
        
        logger.info("Showing product list with {} products", products.size());
        request.getRequestDispatcher("/pages/product-listing.jsp").forward(request, response);
    }
}

