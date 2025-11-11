package com.example.alcoholshop.servlet;

import com.example.alcoholshop.util.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

@WebServlet("/health")
public class HealthServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(HealthServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            try (Connection conn = DBUtil.getConnection()) {
                boolean valid = conn.isValid(2);
                if (valid) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    out.write("{\"status\":\"ok\",\"db\":\"ok\"}");
                } else {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.write("{\"status\":\"error\",\"db\":\"invalid\"}");
                }
            } catch (Exception e) {
                logger.error("Health check failed - DB unavailable", e);
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.write("{\"status\":\"error\",\"db\":\"unavailable\",\"message\":\"" + e.getMessage() + "\"}");
            }
        }
    }
}

