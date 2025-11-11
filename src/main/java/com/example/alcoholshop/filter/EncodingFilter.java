package com.example.alcoholshop.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Filter to set UTF-8 encoding for all requests and responses
 */
public class EncodingFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(EncodingFilter.class);
    private static final String ENCODING = "UTF-8";
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("EncodingFilter initialized with encoding: " + ENCODING);
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Set request encoding
        if (httpRequest.getCharacterEncoding() == null) {
            httpRequest.setCharacterEncoding(ENCODING);
        }
        
        // Set response encoding
        httpResponse.setCharacterEncoding(ENCODING);
        httpResponse.setContentType("text/html; charset=" + ENCODING);
        
        logger.debug("Set encoding to UTF-8 for request: " + httpRequest.getRequestURI());
        
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        logger.info("EncodingFilter destroyed");
    }
}

