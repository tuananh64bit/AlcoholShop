package com.example.alcoholshop.servlet;

import com.example.alcoholshop.dao.ContactDAO;
import com.example.alcoholshop.dao.impl.ContactDAOImpl;
import com.example.alcoholshop.model.ContactMessage;
import com.example.alcoholshop.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Contact servlet to handle contact form submissions
 */
@WebServlet("/contact")
public class ContactServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ContactServlet.class);
    
    private ContactDAO contactDAO;
    
    @Override
    public void init() throws ServletException {
        contactDAO = new ContactDAOImpl();
        logger.info("ContactServlet initialized");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Show contact form
        request.getRequestDispatcher("/pages/contact.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get form parameters
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String subject = request.getParameter("subject");
        String message = request.getParameter("message");
        
        Map<String, String> errors = new HashMap<>();
        
        // Validate input
        validateContactForm(name, email, phone, subject, message, errors);
        
        if (!errors.isEmpty()) {
            // Return to contact form with errors
            request.setAttribute("errors", errors);
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);
            request.setAttribute("subject", subject);
            request.setAttribute("message", message);
            request.getRequestDispatcher("/pages/contact.jsp").forward(request, response);
            return;
        }
        
        try {
            // Create contact message
            ContactMessage contactMessage = new ContactMessage();
            contactMessage.setName(ValidationUtil.sanitize(name));
            contactMessage.setEmail(ValidationUtil.sanitize(email));
            contactMessage.setPhone(ValidationUtil.sanitize(phone));
            contactMessage.setSubject(ValidationUtil.sanitize(subject));
            contactMessage.setMessage(ValidationUtil.sanitize(message));
            
            // Save to database
            if (contactDAO.create(contactMessage)) {
                logger.info("Contact message saved from: " + name + " (" + email + ")");
                request.setAttribute("success", "Thank you for your message! We will get back to you soon.");
            } else {
                logger.error("Failed to save contact message from: " + name + " (" + email + ")");
                request.setAttribute("error", "Unable to send message. Please try again later.");
            }
            
            request.getRequestDispatcher("/pages/contact.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.error("Error processing contact form", e);
            request.setAttribute("error", "Unable to send message. Please try again later.");
            request.getRequestDispatcher("/pages/contact.jsp").forward(request, response);
        }
    }
    
    /**
     * Validate contact form data
     */
    private void validateContactForm(String name, String email, String phone, 
                                   String subject, String message, Map<String, String> errors) {
        
        // Validate name
        if (name == null || name.trim().isEmpty()) {
            errors.put("name", "Name is required");
        } else if (name.trim().length() < 2) {
            errors.put("name", "Name must be at least 2 characters");
        }
        
        // Validate email
        if (email == null || email.trim().isEmpty()) {
            errors.put("email", "Email is required");
        } else if (!ValidationUtil.isValidEmail(email)) {
            errors.put("email", "Invalid email format");
        }
        
        // Validate phone (optional)
        if (phone != null && !phone.trim().isEmpty() && !ValidationUtil.isValidPhone(phone)) {
            errors.put("phone", "Invalid phone number format");
        }
        
        // Validate subject
        if (subject == null || subject.trim().isEmpty()) {
            errors.put("subject", "Subject is required");
        } else if (subject.trim().length() < 5) {
            errors.put("subject", "Subject must be at least 5 characters");
        }
        
        // Validate message
        if (message == null || message.trim().isEmpty()) {
            errors.put("message", "Message is required");
        } else if (message.trim().length() < 10) {
            errors.put("message", "Message must be at least 10 characters");
        } else if (message.trim().length() > 1000) {
            errors.put("message", "Message must be less than 1000 characters");
        }
    }
}

