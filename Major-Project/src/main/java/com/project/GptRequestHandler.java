package com.project;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class GptRequestHandler {

   
    public void sendRequest(String fileContent) throws IOException, URISyntaxException {
        // Construct the URL using URI
//        URI uri = new URI("http", null, "localhost", 8080, "/api/chat", null, null);
//        URL url = uri.toURL();
//        System.out.println("Inside SendRequest");       
//        String prompt = "Please generate the complete PlantUML grammar for the following diagrams based on the provided Java code. Ensure that all elements of the system are captured thoroughly, including all classes, methods, interactions, dependencies, and data flows. Be as detailed as possible in each diagram:\n\n"
//                
//                + "Java code:\n" + fileContent;
//        // Send HTTP request to chat API
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("POST");
//        connection.setRequestProperty("Content-Type", "application/json");
//        connection.setDoOutput(true);
//
//        try (OutputStream os = connection.getOutputStream()) {
//            os.write(prompt.getBytes(StandardCharsets.UTF_8));
//            os.flush();
//        }
//
//        // Read the response
//        StringBuilder responseBuilder = new StringBuilder();
//        try (Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8.name())) {
//            while (scanner.hasNext()) {
//                responseBuilder.append(scanner.nextLine()).append("\n");
//            }
//        }
//
//        String response = responseBuilder.toString();
//    }
//
//
//    private String extractBetweenMarkers(String text, String startMarker, String endMarker) {
//        int startIndex = text.indexOf(startMarker);
//        int endIndex = text.indexOf(endMarker, startIndex);
//        if (startIndex != -1 && endIndex != -1) {
//            return text.substring(startIndex + startMarker.length(), endIndex).trim();
//        }
//        return "";
    }

 
}