package com.example.alcoholshop.servlet.admin;

import com.example.alcoholshop.dao.ProductDAO;
import com.example.alcoholshop.dao.CategoryDAO;
import com.example.alcoholshop.dao.impl.ProductDAOImpl;
import com.example.alcoholshop.dao.impl.CategoryDAOImpl;
import com.example.alcoholshop.model.Product;
import com.example.alcoholshop.model.Category;
import com.example.alcoholshop.model.UserAccount;
import com.example.alcoholshop.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Admin product management servlet
 */
@MultipartConfig
@WebServlet("/admin/products")
public class AdminProductServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(AdminProductServlet.class);
    
    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;
    
    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAOImpl();
        categoryDAO = new CategoryDAOImpl();
        logger.info("AdminProductServlet initialized");
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
        if (currentUser == null || !currentUser.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/pages/error/403.jsp");
            return;
        }
        
        String action = request.getParameter("action");
        
        try {
            if ("edit".equals(action)) {
                showEditForm(request, response);
            } else if ("delete".equals(action)) {
                deleteProduct(request, response);
            } else if ("add".equals(action)) {
                // show create product form
                showCreateForm(request, response);
             } else {
                 showProductList(request, response);
             }

         } catch (Exception e) {
             logger.error("Error in AdminProductServlet", e);
             request.setAttribute("error", "Unable to process request. Please try again later.");
             request.getRequestDispatcher("/pages/error/500.jsp").forward(request, response);
         }
     }

    /**
     * Show create product form
     */
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        List<Category> categories = categoryDAO.findAll();
        request.setAttribute("categories", categories);

        // provide list of image filenames available under /static/images
        List<String> imageFiles = listImageFiles();
        request.setAttribute("imageFiles", imageFiles);
        try {
            request.getRequestDispatcher("/pages/admin/product-create.jsp").forward(request, response);
        } catch (ServletException e) {
            logger.error("ServletException forwarding to product create form", e);
            throw new IOException(e);
        }
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
        if (currentUser == null || !currentUser.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/pages/error/403.jsp");
            return;
        }
        
        String action = request.getParameter("action");
        
        try {
            if ("create".equals(action)) {
                createProduct(request, response);
            } else if ("update".equals(action)) {
                updateProduct(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/products");
            }
            
        } catch (Exception e) {
            logger.error("Error in AdminProductServlet POST", e);
            request.setAttribute("error", "Unable to process request. Please try again later.");
            request.getRequestDispatcher("/pages/error/500.jsp").forward(request, response);
        }
    }
    
    /**
     * Show product list — load full list and allow client-side filtering in JSP
     */
    private void showProductList(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // Always load the full product list from DB; filtering handled client-side in JSP
        List<Product> products = productDAO.findAll();
        List<Category> categories = categoryDAO.findAll();

        request.setAttribute("products", products);
        request.setAttribute("categories", categories);
        request.setAttribute("totalProducts", products != null ? products.size() : 0);

        try {
            request.getRequestDispatcher("/pages/admin/products.jsp").forward(request, response);
        } catch (ServletException e) {
            logger.error("ServletException forwarding to product list", e);
            throw new IOException(e);
        }
    }
    
    /**
     * Show edit form
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

         String productId = request.getParameter("id");
         if (productId == null || productId.isEmpty()) {
             response.sendRedirect(request.getContextPath() + "/admin/products");
             return;
         }

         try {
             int id = Integer.parseInt(productId);
             Product product = productDAO.findById(id);

             if (product == null) {
                 request.setAttribute("error", "Product not found");
                 response.sendRedirect(request.getContextPath() + "/admin/products");
                 return;
             }

             List<Category> categories = categoryDAO.findAll();

             request.setAttribute("product", product);
             request.setAttribute("categories", categories);

            // provide list of image filenames available under /static/images
            List<String> imageFiles = listImageFiles();
            request.setAttribute("imageFiles", imageFiles);
            try {
                request.getRequestDispatcher("/pages/admin/product-edit.jsp").forward(request, response);
            } catch (ServletException e) {
                logger.error("ServletException forwarding to product edit", e);
                throw new IOException(e);
            }

         } catch (NumberFormatException e) {
             logger.error("Invalid product ID: " + productId, e);
             response.sendRedirect(request.getContextPath() + "/admin/products");
         }
     }

    /**
     * List image filenames under webapp/static/images
     */
    private List<String> listImageFiles() {
        List<String> images = new ArrayList<>();
        try {
            String realPath = getServletContext().getRealPath("/static/images/products");
            if (realPath != null) {
                File dir = new File(realPath);
                if (dir.exists() && dir.isDirectory()) {
                    File[] files = dir.listFiles();
                    if (files != null) {
                        for (File f : files) {
                            if (f.isFile()) {
                                String name = f.getName().toLowerCase();
                                if (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".gif")) {
                                    images.add(f.getName());
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("Unable to list images in /static/images", e);
        }
        return images;
    }

    /**
     * Create new product
     */
    private void createProduct(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // Get form parameters
        String code = request.getParameter("code");
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String priceStr = request.getParameter("price");
        String stockStr = request.getParameter("stock");
        String image = request.getParameter("image");
        String categoryIdStr = request.getParameter("categoryId");
        String alcoholPercentageStr = request.getParameter("alcoholPercentage");
        
        // Handle optional uploaded image file: form field name `imageFile`
        try {
            Part imagePart = request.getPart("imageFile");
            if (imagePart != null && imagePart.getSize() > 0) {
                String submitted = Path.of(imagePart.getSubmittedFileName()).getFileName().toString();
                // save to webapp/static/images/products
                String imagesDir = getServletContext().getRealPath("/static/images/products");
                if (imagesDir != null) {
                    File dir = new File(imagesDir);
                    if (!dir.exists()) {
                        if (!dir.mkdirs()) {
                            logger.warn("Could not create images directory: {}", imagesDir);
                        }
                    }
                    Path target = dir.toPath().resolve(submitted);
                    try (InputStream in = imagePart.getInputStream()) {
                        Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
                        image = submitted; // set image filename for DB
                    } catch (IOException e) {
                        logger.warn("Failed to save uploaded image", e);
                    }
                }
            }
        } catch (IllegalStateException | IOException | ServletException e) {
            logger.debug("No image uploaded or upload failed", e);
        }

        Map<String, String> errors = new HashMap<>();
        
        // Validate input
        validateProduct(code, name, description, priceStr, stockStr, categoryIdStr, 
                       alcoholPercentageStr, errors);
        
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("code", code);
            request.setAttribute("name", name);
            request.setAttribute("description", description);
            request.setAttribute("price", priceStr);
            request.setAttribute("stock", stockStr);
            request.setAttribute("image", image);
            request.setAttribute("categoryId", categoryIdStr);
            request.setAttribute("alcoholPercentage", alcoholPercentageStr);
            
            List<Category> categories = categoryDAO.findAll();
            request.setAttribute("categories", categories);
            // also provide imageFiles for the select
            List<String> imageFiles = listImageFiles();
            request.setAttribute("imageFiles", imageFiles);
            try {
                request.getRequestDispatcher("/pages/admin/product-create.jsp").forward(request, response);
            } catch (ServletException e) {
                logger.error("ServletException forwarding to product create", e);
                throw new IOException(e);
            }
            return;
        }
        
        try {
            // Create product
            Product product = new Product();
            product.setCode(code);
            product.setName(name);
            product.setDescription(description);
            product.setPrice(new BigDecimal(priceStr));
            product.setStock(Integer.parseInt(stockStr));
            product.setImage(image);
            product.setCategoryId(Integer.parseInt(categoryIdStr));
            product.setAlcoholPercentage(new BigDecimal(alcoholPercentageStr));
            
            if (productDAO.create(product)) {
                logger.info("Product created successfully: " + product.getName());
                request.setAttribute("success", "Product created successfully");
            } else {
                logger.error("Failed to create product: " + product.getName());
                request.setAttribute("error", "Failed to create product");
            }
            
            response.sendRedirect(request.getContextPath() + "/admin/products");
            
        } catch (NumberFormatException e) {
            logger.error("Invalid number format in product creation", e);
            request.setAttribute("error", "Invalid number format");
            response.sendRedirect(request.getContextPath() + "/admin/products");
        }
    }
    
    /**
     * Update existing product
     */
    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String productIdStr = request.getParameter("id");
        if (productIdStr == null || productIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/products");
            return;
        }
        
        try {
            int productId = Integer.parseInt(productIdStr);
            
            // Get form parameters
            String code = request.getParameter("code");
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String priceStr = request.getParameter("price");
            String stockStr = request.getParameter("stock");
            String image = request.getParameter("image");
            String categoryIdStr = request.getParameter("categoryId");
            String alcoholPercentageStr = request.getParameter("alcoholPercentage");

            // Handle optional uploaded image file: form field name `imageFile`
            try {
                Part imagePart = request.getPart("imageFile");
                if (imagePart != null && imagePart.getSize() > 0) {
                    String submitted = Path.of(imagePart.getSubmittedFileName()).getFileName().toString();
                    String imagesDir = getServletContext().getRealPath("/static/images/products");
                    if (imagesDir != null) {
                        File dir = new File(imagesDir);
                        if (!dir.exists()) {
                            if (!dir.mkdirs()) {
                                logger.warn("Could not create images directory: {}", imagesDir);
                            }
                        }
                        Path target = dir.toPath().resolve(submitted);
                        try (InputStream in = imagePart.getInputStream()) {
                            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
                            image = submitted; // set image filename for DB
                        } catch (IOException e) {
                            logger.warn("Failed to save uploaded image", e);
                        }
                    }
                }
            } catch (IllegalStateException | IOException | ServletException e) {
                logger.debug("No image uploaded or upload failed", e);
            }

            Map<String, String> errors = new HashMap<>();
            
            // Validate input
            validateProduct(code, name, description, priceStr, stockStr, categoryIdStr, 
                           alcoholPercentageStr, errors);
            
            if (!errors.isEmpty()) {
                // Forward back to edit form with errors and current values
                request.setAttribute("errors", errors);
                Product product = new Product();
                product.setId(productId);
                product.setCode(code);
                product.setName(name);
                product.setDescription(description);
                try {
                    product.setPrice(new BigDecimal(priceStr));
                } catch (Exception ex) { /* ignore */ }
                try {
                    product.setStock(Integer.parseInt(stockStr));
                } catch (Exception ex) { /* ignore */ }
                product.setImage(image);
                try {
                    product.setCategoryId(Integer.parseInt(categoryIdStr));
                } catch (Exception ex) { /* ignore */ }
                try {
                    product.setAlcoholPercentage(new BigDecimal(alcoholPercentageStr));
                } catch (Exception ex) { /* ignore */ }

                List<Category> categories = categoryDAO.findAll();
                request.setAttribute("categories", categories);
                request.setAttribute("product", product);
                // provide imageFiles for the select
                List<String> imageFiles = listImageFiles();
                request.setAttribute("imageFiles", imageFiles);
                try {
                    request.getRequestDispatcher("/pages/admin/product-edit.jsp").forward(request, response);
                } catch (ServletException e) {
                    logger.error("ServletException forwarding to product edit", e);
                    throw new IOException(e);
                }
                return;
            }
            
            // Update product
            Product product = productDAO.findById(productId);
            if (product == null) {
                request.setAttribute("error", "Product not found");
                response.sendRedirect(request.getContextPath() + "/admin/products");
                return;
            }
            
            product.setCode(code);
            product.setName(name);
            product.setDescription(description);
            product.setPrice(new BigDecimal(priceStr));
            product.setStock(Integer.parseInt(stockStr));
            // Only update image if a new filename was provided (select) or upload succeeded
            if (image != null && !image.trim().isEmpty()) {
                product.setImage(image);
            }
            product.setCategoryId(Integer.parseInt(categoryIdStr));
            product.setAlcoholPercentage(new BigDecimal(alcoholPercentageStr));
            
            if (productDAO.update(product)) {
                logger.info("Product updated successfully: " + product.getName());
                request.setAttribute("success", "Product updated successfully");
            } else {
                logger.error("Failed to update product: " + product.getName());
                request.setAttribute("error", "Failed to update product");
            }
            
            response.sendRedirect(request.getContextPath() + "/admin/products");
            
        } catch (NumberFormatException e) {
            logger.error("Invalid product ID: " + productIdStr, e);
            response.sendRedirect(request.getContextPath() + "/admin/products");
        }
    }
    
    /**
     * Delete product
     */
    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String productIdStr = request.getParameter("id");
        if (productIdStr == null || productIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/products");
            return;
        }
        
        try {
            int productId = Integer.parseInt(productIdStr);
            
            if (productDAO.delete(productId)) {
                logger.info("Product deleted successfully: " + productId);
                request.setAttribute("success", "Product deleted successfully");
            } else {
                logger.error("Failed to delete product: " + productId);
                request.setAttribute("error", "Failed to delete product");
            }
            
            response.sendRedirect(request.getContextPath() + "/admin/products");
            
        } catch (NumberFormatException e) {
            logger.error("Invalid product ID: " + productIdStr, e);
            response.sendRedirect(request.getContextPath() + "/admin/products");
        }
    }
    
    /**
     * Validate product form data
     */
    private void validateProduct(String code, String name, String description, String priceStr,
                               String stockStr, String categoryIdStr, String alcoholPercentageStr,
                               Map<String, String> errors) {
        // Validate code
        if (code == null || code.trim().isEmpty()) {
            errors.put("code", "Product code is required");
        } else if (code.trim().length() < 3) {
            errors.put("code", "Product code must be at least 3 characters");
        }

        // Validate name
        if (name == null || name.trim().isEmpty()) {
            errors.put("name", "Product name is required");
        } else if (name.trim().length() < 3) {
            errors.put("name", "Product name must be at least 3 characters");
        }

        // Validate description
        if (description == null || description.trim().isEmpty()) {
            errors.put("description", "Product description is required");
        } else if (description.trim().length() < 10) {
            errors.put("description", "Product description must be at least 10 characters");
        }

        // Validate price
        if (priceStr == null || priceStr.trim().isEmpty()) {
            errors.put("price", "Price is required");
        } else {
            try {
                new BigDecimal(priceStr);
                // optional: use ValidationUtil.isValidPrice(priceStr) if available
                if (!ValidationUtil.isValidPrice(priceStr)) {
                    errors.put("price", "Invalid price format");
                }
            } catch (Exception e) {
                errors.put("price", "Invalid price format");
            }
        }

        // Validate stock
        if (stockStr == null || stockStr.trim().isEmpty()) {
            errors.put("stock", "Stock is required");
        } else if (!ValidationUtil.isValidStock(stockStr)) {
            errors.put("stock", "Invalid stock format");
        }

        // Validate category
        if (categoryIdStr == null || categoryIdStr.trim().isEmpty()) {
            errors.put("categoryId", "Category is required");
        } else {
            try {
                int categoryId = Integer.parseInt(categoryIdStr);
                if (categoryDAO.findById(categoryId) == null) {
                    errors.put("categoryId", "Invalid category");
                }
            } catch (NumberFormatException e) {
                errors.put("categoryId", "Invalid category format");
            }
        }

        // Validate alcohol percentage
        if (alcoholPercentageStr == null || alcoholPercentageStr.trim().isEmpty()) {
            errors.put("alcoholPercentage", "Alcohol percentage is required");
        } else if (!ValidationUtil.isValidAlcoholPercentage(alcoholPercentageStr)) {
            errors.put("alcoholPercentage", "Invalid alcohol percentage (0-100)");
        }
    }
}
