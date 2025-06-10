package com.project.servlet;

import com.project.db.DatabaseConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Validate input fields
        if (name == null || name.isEmpty() || email == null || email.isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("errorMessage", "Please fill in all fields.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check if email already exists
            String checkQuery = "SELECT email FROM users WHERE email = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, email);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                    	String errorMessage = "This email is already registered. Please proceed with same email to login";
                        request.setAttribute("errorMessage", errorMessage);
                        request.getRequestDispatcher("index.jsp").forward(request, response);
                        return;
                    }
                }
            }

            // Insert new user into the database
            String query = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, password);
    

                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    // Successfully registered, redirect to login page
                    response.sendRedirect("index.jsp");
                } else {
                    // Failed to insert, forward back with error message
                    request.setAttribute("errorMessage", "Registration failed. Please try again.");
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Error during registration, forward back with error message
            request.setAttribute("errorMessage", "An error occurred during registration. Please try again later.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }
}
