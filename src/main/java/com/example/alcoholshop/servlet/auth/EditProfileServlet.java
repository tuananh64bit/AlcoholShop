package com.example.alcoholshop.servlet.auth;

import com.example.alcoholshop.dao.UserDAO;
import com.example.alcoholshop.dao.impl.UserDAOImpl;
import com.example.alcoholshop.model.UserAccount;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/edit-profile")
public class EditProfileServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(EditProfileServlet.class);
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            // Not logged in
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Forward to edit form
        req.getRequestDispatcher("/pages/edit-profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        UserAccount current = (UserAccount) session.getAttribute("currentUser");
        Map<String, String> errors = new HashMap<>();

        String fullname = req.getParameter("fullname");
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String birthDateStr = req.getParameter("birthDate");
        String address = req.getParameter("address");

        // Basic validation
        if (fullname == null || fullname.trim().isEmpty()) {
            errors.put("fullname", "Full name is required");
        }

        if (username == null || username.trim().isEmpty()) {
            errors.put("username", "Username is required");
        }

        if (email == null || email.trim().isEmpty()) {
            errors.put("email", "Email is required");
        }

        LocalDate birthDate = null;
        if (birthDateStr != null && !birthDateStr.trim().isEmpty()) {
            try {
                birthDate = LocalDate.parse(birthDateStr);
            } catch (DateTimeParseException e) {
                errors.put("birthDate", "Invalid birth date format");
            }
        }

        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            req.getRequestDispatcher("/pages/edit-profile.jsp").forward(req, resp);
            return;
        }

        try {
            // Update user object (keep passwordHash and role)
            UserAccount updated = new UserAccount();
            updated.setId(current.getId());
            updated.setUsername(username.trim());
            updated.setFullname(fullname.trim());
            updated.setEmail(email.trim());
            updated.setBirthDate(birthDate);
            updated.setAddress(address);
            // preserve existing passwordHash and role
            updated.setPasswordHash(current.getPasswordHash());
            updated.setRole(current.getRole());

            boolean ok = userDAO.update(updated);
            if (ok) {
                // Refresh currentUser in session
                UserAccount refreshed = userDAO.findById(updated.getId());
                if (refreshed != null) {
                    session.setAttribute("currentUser", refreshed);
                }
                // Forward back to edit form and show a success modal so user can confirm or stay on the page
                req.setAttribute("saved", "true");
                // forward will keep the updated session user available to the JSP
                req.getRequestDispatcher("/pages/edit-profile.jsp").forward(req, resp);
            } else {
                logger.warn("Failed to update user: " + updated.getId());
                req.setAttribute("error", "update_failed");
                req.getRequestDispatcher("/pages/edit-profile.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            logger.error("Error updating profile", e);
            req.setAttribute("error", "exception");
            req.getRequestDispatcher("/pages/edit-profile.jsp").forward(req, resp);
        }
    }
}
