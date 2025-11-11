package com.example.alcoholshop.util;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

/**
 * Utility class for validation methods
 */
public class ValidationUtil {
    
    private static final String EMAIL_PATTERN = 
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    
    private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
    
    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return emailPattern.matcher(email).matches();
    }
    
    /**
     * Check if person is 18 or older
     */
    public static boolean isAdult(LocalDate birthDate) {
        if (birthDate == null) {
            return false;
        }
        
        LocalDate now = LocalDate.now();
        Period period = Period.between(birthDate, now);
        return period.getYears() >= 18;
    }
    
    /**
     * Validate password strength
     */
    public static boolean isStrongPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        // Check for at least one uppercase letter
        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        // Check for at least one lowercase letter
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        // Check for at least one digit
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        // Check for at least one special character
        boolean hasSpecial = password.chars().anyMatch(ch -> !Character.isLetterOrDigit(ch));
        
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }
    
    /**
     * Validate username format
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        // Username should be 3-20 characters, alphanumeric and underscore only
        String trimmed = username.trim();
        return trimmed.length() >= 3 && trimmed.length() <= 20 && 
               trimmed.matches("^[a-zA-Z0-9_]+$");
    }
    
    /**
     * Sanitize string input to prevent XSS
     */
    public static String sanitize(String input) {
        if (input == null) {
            return null;
        }
        
        return input.trim()
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#x27;")
                   .replace("&", "&amp;");
    }
    
    /**
     * Validate phone number format
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        
        // Remove all non-digit characters
        String digits = phone.replaceAll("\\D", "");
        
        // Check if it's a valid length (7-15 digits)
        return digits.length() >= 7 && digits.length() <= 15;
    }
    
    /**
     * Validate product price
     */
    public static boolean isValidPrice(String priceStr) {
        if (priceStr == null || priceStr.trim().isEmpty()) {
            return false;
        }
        
        try {
            double price = Double.parseDouble(priceStr);
            return price > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validate product stock
     */
    public static boolean isValidStock(String stockStr) {
        if (stockStr == null || stockStr.trim().isEmpty()) {
            return false;
        }
        
        try {
            int stock = Integer.parseInt(stockStr);
            return stock >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validate alcohol percentage
     */
    public static boolean isValidAlcoholPercentage(String percentageStr) {
        if (percentageStr == null || percentageStr.trim().isEmpty()) {
            return false;
        }
        
        try {
            double percentage = Double.parseDouble(percentageStr);
            return percentage >= 0 && percentage <= 100;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

