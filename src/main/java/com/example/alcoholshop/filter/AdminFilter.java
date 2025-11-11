package com.example.alcoholshop.filter;

import com.example.alcoholshop.model.UserAccount;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Filter to check admin role for admin resources
 */
public class AdminFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(AdminFilter.class);
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("AdminFilter initialized");
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = requestURI.substring(contextPath.length());
        
        logger.debug("AdminFilter checking path: " + path);
        
        // Check if user is logged in
        UserAccount currentUser = null;
        if (session != null) {
            currentUser = (UserAccount) session.getAttribute("currentUser");
        }
        
        if (currentUser == null) {
            logger.info("Unauthenticated access attempt to admin area: " + path);
            
            // Store the original URL for redirect after login
            session = httpRequest.getSession(true);
            session.setAttribute("originalURL", requestURI);
            
            // Redirect to login page
            httpResponse.sendRedirect(contextPath + "/pages/auth/login.jsp");
            return;
        }
        
        // Check if user has admin role
        if (!currentUser.isAdmin()) {
            logger.warn("Non-admin user access attempt to admin area: " + path + " by user: " + currentUser.getUsername());
            
            // Redirect to 403 error page
            httpResponse.sendRedirect(contextPath + "/pages/error/403.jsp");
            return;
        }
        
        logger.debug("Admin user access to: " + path + " by user: " + currentUser.getUsername());
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        logger.info("AdminFilter destroyed");
    }
}

