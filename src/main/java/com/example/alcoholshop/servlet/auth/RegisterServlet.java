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
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Registration servlet to handle user registration
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(RegisterServlet.class);
    
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAOImpl();
        logger.info("RegisterServlet initialized");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Show registration form
        request.getRequestDispatcher("/pages/auth/register.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get form parameters
        String username = request.getParameter("username");
        String fullname = request.getParameter("fullname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String birthDateStr = request.getParameter("birthDate");
        String address = request.getParameter("address");
        
        Map<String, String> errors = new HashMap<>();
        
        // Validate input
        validateRegistration(username, fullname, email, password, confirmPassword, 
                           birthDateStr, address, errors);
        
        if (!errors.isEmpty()) {
            // Return to registration form with errors
            request.setAttribute("errors", errors);
            request.setAttribute("username", username);
            request.setAttribute("fullname", fullname);
            request.setAttribute("email", email);
            request.setAttribute("address", address);
            request.getRequestDispatcher("/pages/auth/register.jsp").forward(request, response);
            return;
        }
        
        try {
            // Parse birth date
            LocalDate birthDate = LocalDate.parse(birthDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            
            // Check age requirement (18+)
            if (!ValidationUtil.isAdult(birthDate)) {
                errors.put("birthDate", "You must be at least 18 years old to register");
                request.setAttribute("errors", errors);
                request.setAttribute("username", username);
                request.setAttribute("fullname", fullname);
                request.setAttribute("email", email);
                request.setAttribute("address", address);
                request.getRequestDispatcher("/pages/auth/register.jsp").forward(request, response);
                return;
            }
            
            // Hash password
            String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
            
            // Create user account
            UserAccount user = new UserAccount();
            user.setUsername(username);
            user.setFullname(fullname);
            user.setEmail(email);
            user.setPasswordHash(passwordHash);
            user.setRole("CUSTOMER");
            user.setBirthDate(birthDate);
            user.setAddress(address);
            
            // Save user to database
            if (userDAO.create(user)) {
                logger.info("User registered successfully: " + username);
                request.setAttribute("success", "Registration successful! Please login to continue.");
                request.getRequestDispatcher("/pages/auth/login.jsp").forward(request, response);
            } else {
                logger.error("Failed to create user: " + username);
                request.setAttribute("error", "Registration failed. Please try again.");
                request.getRequestDispatcher("/pages/auth/register.jsp").forward(request, response);
            }
            
        } catch (DateTimeParseException e) {
            logger.error("Invalid birth date format: " + birthDateStr, e);
            errors.put("birthDate", "Invalid date format");
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("/pages/auth/register.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error("Error during registration", e);
            request.setAttribute("error", "Registration failed. Please try again later.");
            request.getRequestDispatcher("/pages/auth/register.jsp").forward(request, response);
        }
    }
    
    /**
     * Validate registration form data
     */
    private void validateRegistration(String username, String fullname, String email, 
                                   String password, String confirmPassword, String birthDateStr,
                                   String address, Map<String, String> errors) {
        
        // Validate username
        if (username == null || username.trim().isEmpty()) {
            errors.put("username", "Username is required");
        } else if (username.length() < 3) {
            errors.put("username", "Username must be at least 3 characters");
        } else if (userDAO.existsByUsername(username)) {
            errors.put("username", "Username already exists");
        }
        
        // Validate fullname
        if (fullname == null || fullname.trim().isEmpty()) {
            errors.put("fullname", "Full name is required");
        }
        
        // Validate email
        if (email == null || email.trim().isEmpty()) {
            errors.put("email", "Email is required");
        } else if (!ValidationUtil.isValidEmail(email)) {
            errors.put("email", "Invalid email format");
        } else if (userDAO.existsByEmail(email)) {
            errors.put("email", "Email already exists");
        }
        
        // Validate password
        if (password == null || password.isEmpty()) {
            errors.put("password", "Password is required");
        } else if (password.length() < 8) {
            errors.put("password", "Password must be at least 8 characters");
        }
        
        // Validate confirm password
        if (confirmPassword == null || confirmPassword.isEmpty()) {
            errors.put("confirmPassword", "Please confirm your password");
        } else if (!password.equals(confirmPassword)) {
            errors.put("confirmPassword", "Passwords do not match");
        }
        
        // Validate birth date
        if (birthDateStr == null || birthDateStr.trim().isEmpty()) {
            errors.put("birthDate", "Birth date is required");
        }
        
        // Validate address
        if (address == null || address.trim().isEmpty()) {
            errors.put("address", "Address is required");
        }
    }
}

