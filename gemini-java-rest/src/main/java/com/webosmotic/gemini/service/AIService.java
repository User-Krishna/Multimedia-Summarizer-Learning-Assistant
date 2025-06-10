package com.webosmotic.gemini.service;

import org.apache.logging.log4j.util.Strings;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class AIService {

    private static final Logger logger = LoggerFactory.getLogger(AIService.class);

    private static final String GEMINI_MODEL = "gemini-1.5-flash";
    private static final String API_KEY ="AIzaSyCGybSb4KUXRodraGgBoAXNpjVJEEFq4U0";
    // another api key
    //private static final String API_KEY ="AIzaSyDxxa5DCcFpmSZdfjNChoLlDmm2n9hzs9g";

    private String conversationHistory = "";

    public String chat(String prompt) {
        // Adding previous conversation history
        String fullPrompt = prompt;

        // Add the old history for adding context
        if (!Strings.isBlank(conversationHistory)) {
            fullPrompt = conversationHistory + "\nUser: " + prompt;
        }

        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Prepare request entity
        fullPrompt = getPromptBody(fullPrompt);
        HttpEntity<String> requestEntity = new HttpEntity<>(fullPrompt, headers);

        // Perform HTTP POST request
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "https://generativelanguage.googleapis.com/v1beta/models/" + GEMINI_MODEL + ":generateContent?key=" + API_KEY,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        HttpStatus statusCode = responseEntity.getStatusCode();

        // Handle the response based on the status code
        if (statusCode == HttpStatus.OK) {
            String responseText = responseEntity.getBody();
            try {
                responseText = parseGeminiResponse(responseText);
                conversationHistory += "\nUser: " + prompt + "\nBot: " + responseText; // Update conversation history
            } catch (Exception e) {
                logger.error("Error in Parsing", e);
            }
            return responseText; // Return the parsed response text
        } else {
            throw new RuntimeException("API request failed with status code: " + statusCode + " and response: " + responseEntity.getBody());
        }
    }

    public String getPromptBody(String prompt) {
        // Create prompt for generating casual conversation
        JSONObject promptJson = new JSONObject();

        // Array to contain all the content-related data, including the text and role
        JSONArray contentsArray = new JSONArray();
        JSONObject contentsObject = new JSONObject();
        contentsObject.put("role", "user");

        // Array to hold the specific parts (or sections) of the user's input text
        JSONArray partsArray = new JSONArray();
        JSONObject partsObject = new JSONObject();
        partsObject.put("text", prompt);
        partsArray.add(partsObject);
        contentsObject.put("parts", partsArray);

        contentsArray.add(contentsObject);
        promptJson.put("contents", contentsArray);

        // Creating and setting generation configuration parameters such as temperature and topP
        JSONObject parametersJson = new JSONObject();
        parametersJson.put("temperature", 0.7); // Increase temperature for more casual responses
        parametersJson.put("topP", 0.9); // Adjust topP for more diverse responses
        promptJson.put("generationConfig", parametersJson);

        // Convert the JSON object to a JSON string
        return promptJson.toJSONString();
    }

    public String parseGeminiResponse(String jsonResponse) throws IOException, ParseException {
        // Parse the JSON string
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonResponse);

        // Get the "candidates" array
        JSONArray candidatesArray = (JSONArray) jsonObject.get("candidates");

        // Assuming there's only one candidate (index 0), extract its content
        JSONObject candidateObject = (JSONObject) candidatesArray.get(0);
        JSONObject contentObject = (JSONObject) candidateObject.get("content");

        // Get the "parts" array within the content
        JSONArray partsArray = (JSONArray) contentObject.get("parts");

        // Assuming there's only one part (index 0), extract its text
        JSONObject partObject = (JSONObject) partsArray.get(0);
        String responseText = (String) partObject.get("text");

        return responseText;
    }
}