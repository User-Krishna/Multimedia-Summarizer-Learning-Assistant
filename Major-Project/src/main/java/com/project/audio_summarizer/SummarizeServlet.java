package com.project.audio_summarizer;

import org.json.simple.JSONObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

@WebServlet("/SummarizeServlet")
public class SummarizeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Read the text and summary length from the form
        String text = request.getParameter("text");
        String summaryLength = request.getParameter("summaryLength");

        try {
            // Prepare the prompt based on the summary type
            String prompt = "";
            switch (summaryLength) {
                case "1":
                    prompt = "Provide a short summary of the following text: " + text;
                    break;
                case "2":
                    prompt = "Provide a medium-length summary of the following text: " + text;
                    break;
                case "3":
                    prompt = "Provide a detailed summary of the following text: " + text;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid summary length");
            }

            // Send the text to the Gemini API for summarization
            String summary = sendToGeminiAPI(prompt);

            // Store the summary in session to pass it to the displaySummary.jsp page
            HttpSession session = request.getSession();
            session.setAttribute("summary", summary);
            session.setAttribute("filename", "Speech");

            // Forward the request to displaySummary.jsp
            request.getRequestDispatcher("displaySummary.jsp").forward(request, response);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private String sendToGeminiAPI(String prompt) throws IOException, URISyntaxException {
        URI uri = new URI("http://localhost:8080/api/chat");
        HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // Prepare the JSON request body
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("prompt", prompt);

        // Send the request to the Gemini API
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonRequest.toJSONString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Read the response from the Gemini API
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

     // Log the raw response for debugging
        System.out.println("Raw response from Gemini API: " + response.toString());

        // Trim the "Bot: " prefix if present
        String responseText = response.toString();
        if (responseText.startsWith("Bot: ")) {
            responseText = responseText.substring(5);
        }

        return responseText;
    }
}