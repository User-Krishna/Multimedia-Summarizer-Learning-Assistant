package com.project.audio_summarizer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

@WebServlet("/GrammarCorrectionServlet")
public class GrammarCorrectionServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Read the JSON request body
        BufferedReader reader = request.getReader();
        String requestBody = reader.lines().collect(Collectors.joining(System.lineSeparator()));

        try {
            // Parse the JSON to extract the text
            JSONObject jsonRequest = (JSONObject) new JSONParser().parse(requestBody);
            String text = (String) jsonRequest.get("text");

            // Send the text to the Gemini API for grammar correction
            String correctedText = sendToGeminiAPI(text);

            // Prepare the JSON response with the corrected text
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("correctedText", correctedText);

            // Send the JSON response back to the client
            response.setContentType("application/json");
            response.getWriter().write(jsonResponse.toJSONString());

        } catch (ParseException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Invalid JSON format in request body\"}");
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private String sendToGeminiAPI(String text) throws IOException {
        URL url = new URL("http://localhost:8080/api/chat");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // Prepare the JSON request body
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("prompt", "Correct the grammar of the provided text and return only the corrected version as I am using you as an API to correct grammar of text. Do not include any explanations, metadata, or additional information, as the response will be displayed directly. Ensure the grammar is corrected even if the sentences are nonsensical: " + text);

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

        // Since the response is plain text, return it directly
        return response.toString();
    }
}