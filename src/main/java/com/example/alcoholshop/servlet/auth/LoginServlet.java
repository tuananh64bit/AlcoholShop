package com.example.alcoholshop.servlet.auth;

import com.example.alcoholshop.dao.UserDAO;
import com.example.alcoholshop.dao.impl.UserDAOImpl;
import com.example.alcoholshop.model.UserAccount;
import com.example.alcoholshop.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Login servlet to handle user authentication
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);
    
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAOImpl();
        logger.info("LoginServlet initialized");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("currentUser") != null) {
            // User is already logged in, redirect to home
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        // Show login form
        request.getRequestDispatcher("/pages/auth/login.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get form parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");
        
        Map<String, String> errors = new HashMap<>();
        
        // Validate input
        if (username == null || username.trim().isEmpty()) {
            errors.put("username", "Username is required");
        }
        
        if (password == null || password.isEmpty()) {
            errors.put("password", "Password is required");
        }
        
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("username", username);
            request.getRequestDispatcher("/pages/auth/login.jsp").forward(request, response);
            return;
        }
        
        try {
            // Find user by username
            UserAccount user = userDAO.findByUsername(username.trim());
            
            if (user == null) {
                logger.warn("Login attempt with non-existent username: " + username);
                errors.put("username", "Invalid username or password");
                request.setAttribute("errors", errors);
                request.setAttribute("username", username);
                request.getRequestDispatcher("/pages/auth/login.jsp").forward(request, response);
                return;
            }
            
            // Verify password
            if (!BCrypt.checkpw(password, user.getPasswordHash())) {
                logger.warn("Login attempt with invalid password for user: " + username);
                errors.put("password", "Invalid username or password");
                request.setAttribute("errors", errors);
                request.setAttribute("username", username);
                request.getRequestDispatcher("/pages/auth/login.jsp").forward(request, response);
                return;
            }
            
            // Check if user is adult (18+)
            if (!user.isAdult()) {
                logger.warn("Login attempt by underage user: " + username);
                errors.put("age", "You must be at least 18 years old to access this site");
                request.setAttribute("errors", errors);
                request.setAttribute("username", username);
                request.getRequestDispatcher("/pages/auth/login.jsp").forward(request, response);
                return;
            }
            
            // Login successful
            HttpSession session = request.getSession(true);
            session.setAttribute("currentUser", user);
            
            // Set session timeout based on remember me
            if ("on".equals(rememberMe)) {
                session.setMaxInactiveInterval(7 * 24 * 60 * 60); // 7 days
            } else {
                session.setMaxInactiveInterval(30 * 60); // 30 minutes
            }
            
            logger.info("User logged in successfully: " + username + " (Role: " + user.getRole() + ")");
            
            // Redirect to original URL or home page
            String originalURL = (String) session.getAttribute("originalURL");
            if (originalURL != null && !originalURL.isEmpty()) {
                session.removeAttribute("originalURL");
                response.sendRedirect(originalURL);
            } else {
                response.sendRedirect(request.getContextPath() + "/");
            }
            
        } catch (Exception e) {
            logger.error("Error during login", e);
            request.setAttribute("error", "Login failed. Please try again later.");
            request.getRequestDispatcher("/pages/auth/login.jsp").forward(request, response);
        }
    }
}

