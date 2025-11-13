package com.example.alcoholshop.servlet.auth;

import com.example.alcoholshop.dao.UserDAO;
import com.example.alcoholshop.dao.impl.UserDAOImpl;
import com.example.alcoholshop.model.UserAccount;
import org.mindrot.jbcrypt.BCrypt;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/change-password")
public class ChangePasswordServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ChangePasswordServlet.class);
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAOImpl();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        UserAccount current = (UserAccount) session.getAttribute("currentUser");
        String oldPassword = req.getParameter("oldPassword");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        if (oldPassword == null || newPassword == null || confirmPassword == null) {
            resp.sendRedirect(req.getContextPath() + "/profile?error=missing_params");
            return;
        }

        // Verify old password
        if (!BCrypt.checkpw(oldPassword, current.getPasswordHash())) {
            resp.sendRedirect(req.getContextPath() + "/profile?error=old_password_incorrect");
            return;
        }

        // Basic new password checks
        if (newPassword.length() < 6) {
            resp.sendRedirect(req.getContextPath() + "/profile?error=new_password_too_short");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            resp.sendRedirect(req.getContextPath() + "/profile?error=passwords_do_not_match");
            return;
        }

        try {
            String newHash = BCrypt.hashpw(newPassword, BCrypt.gensalt());

            UserAccount updated = new UserAccount();
            updated.setId(current.getId());
            updated.setUsername(current.getUsername());
            updated.setFullname(current.getFullname());
            updated.setEmail(current.getEmail());
            updated.setBirthDate(current.getBirthDate());
            updated.setAddress(current.getAddress());
            updated.setRole(current.getRole());
            updated.setPasswordHash(newHash);

            boolean ok = userDAO.update(updated);
            if (ok) {
                UserAccount refreshed = userDAO.findById(updated.getId());
                if (refreshed != null) {
                    session.setAttribute("currentUser", refreshed);
                }
                resp.sendRedirect(req.getContextPath() + "/profile?pwSuccess=1");
            } else {
                logger.warn("Failed to update password for user: " + current.getId());
                resp.sendRedirect(req.getContextPath() + "/profile?error=update_failed");
            }
        } catch (Exception e) {
            logger.error("Error changing password", e);
            resp.sendRedirect(req.getContextPath() + "/profile?error=exception");
        }
    }
}
