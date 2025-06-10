package com.project.servlet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

@WebServlet("/GitHubTokenExchange")
public class GitHubOAuthServlet extends HttpServlet {

    private static final String CLIENT_ID = "156404900022-dde7kqg8bs8l29npn8fuau9mb549h10v.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "your_client_secret"; // Replace with your actual client secret
    private static final String TOKEN_URL = "https://github.com/login/oauth/access_token";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            // Parse the authorization code from the request
            String code = new JSONObject(request.getReader()).getString("code");

            // Prepare the request to exchange code for token
            URL url = new URL(TOKEN_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            String payload = String.format(
                "client_id=%s&client_secret=%s&code=%s",
                CLIENT_ID, CLIENT_SECRET, code
            );
            try (OutputStream os = connection.getOutputStream()) {
                os.write(payload.getBytes());
            }

            // Read the response from GitHub
            StringBuilder responseContent = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
            }

            // Parse the response JSON
            JSONObject jsonResponse = new JSONObject(responseContent.toString());
            String accessToken = jsonResponse.optString("access_token");

            if (accessToken.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Failed to exchange token\"}");
                return;
            }

            // Send the token back to the client
            response.setContentType("application/json");
            response.getWriter().write(new JSONObject().put("access_token", accessToken).toString());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.getWriter().write("{\"error\":\"An error occurred\"}");
            } catch (Exception ignored) {}
        }
    }
}