package com.project;

import com.project.preprocess.TextPreprocessor;
import com.project.preprocess.TextRank;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

@WebServlet("/UploadServlet")
@MultipartConfig
public class UploadServlet extends HttpServlet {
	private static final String UPLOAD_DIR = "C:\\Users" + File.separator + "Krishna"+File.separator+"OneDrive"+ File.separator + "Desktop" + File.separator + "Major Project" + File.separator + "Major-Project" + File.separator + "src" + File.separator + "main" + File.separator + "webapp" + File.separator + "uploads";
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        cleanUploadsDirectory();
        int wordCount = getWordCountParameter(request);
        Part filePart = request.getPart("files");
        if (filePart != null) {
            String fileName = getFileName(filePart);
            if (fileName != null && !fileName.isEmpty()) {
                File file = new File(UPLOAD_DIR, fileName);
                filePart.write(file.getAbsolutePath());
                System.out.println("File uploaded to: " + file.getAbsolutePath());

                String fileExtension = getFileExtension(fileName);
                String fileContent = "";

                try {
                    switch (fileExtension) {
                        case "txt":
                            fileContent = FileContentExtractor.extractTxtContent(file);
                            break;
                        case "pdf":
                            fileContent = FileContentExtractor.extractPdfContent(file);
                            break;
                        case "doc":
                        case "docx":
                            fileContent = FileContentExtractor.extractDocContent(file);
                            break;
                        default:
                            System.out.println("Unsupported file type: " + fileExtension);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Preprocess and summarize the extracted content
                if (!fileContent.isEmpty()) {
                    try {
                        String summary = processAndSummarizeContent(fileContent, wordCount);

                        // Beautify the summary before displaying
                        String beautifiedSummary = beautifySummary(summary, wordCount);

                        // Store the beautified summary in session to pass it to the displaySummary.jsp page
                        HttpSession session = request.getSession();
                        session.setAttribute("summary", beautifiedSummary);
                        session.setAttribute("filename", fileName);

                        // Forward the request to displaySummary.jsp
                        request.getRequestDispatcher("displaySummary.jsp").forward(request, response);
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing the content for summarization.");
                        return;
                    }
                }
            }
        }
        response.sendRedirect("error.html");
    }

    private String processAndSummarizeContent(String fileContent, int wordCount) throws IOException {
        String sentenceModelPath = "C:\\Users" + File.separator + "Krishna"+File.separator+"OneDrive"+ File.separator + "Desktop" + File.separator + "Major Project" + File.separator + "Major-Project" + File.separator + "src" + File.separator + "main" + File.separator + "webapp" + File.separator + "models/opennlp-en-ud-ewt-sentence-1.2-2.5.0.bin";
        String tokenizerModelPath ="C:\\Users" + File.separator + "Krishna"+File.separator+"OneDrive" + File.separator + "Desktop" + File.separator + "Major Project" + File.separator + "Major-Project" + File.separator + "src" + File.separator + "main" + File.separator + "webapp" + File.separator + "models/opennlp-en-ud-ewt-tokens-1.2-2.5.0.bin";
        TextPreprocessor preprocessor = new TextPreprocessor(sentenceModelPath, tokenizerModelPath);

        String[] sentences = preprocessor.tokenizeSentences(fileContent);

        TextRank textRank = new TextRank();
        List<String> rankedSentences = textRank.rankSentences(Arrays.asList(sentences));

        String summary = generateSummaryWithWordCount(rankedSentences, wordCount);
        return summary;
    }

    private String generateSummaryWithWordCount(List<String> sentences, int wordCount) {
        StringBuilder summary = new StringBuilder();
        int currentWordCount = 0;
        for (String sentence : sentences) {
            int sentenceWordCount = sentence.split("\\s+").length;
            if (currentWordCount + sentenceWordCount <= wordCount) {
                summary.append(sentence).append(" ");
                currentWordCount += sentenceWordCount;
            } else {
                break;
            }
        }
        return summary.toString().trim();
    }

    private String beautifySummary(String summary, int wordCount) throws IOException, URISyntaxException {
        String prompt = "Please beautify the following summary,the provided summary is just the structure summarize and beautify to make it more polished and readable, while keeping the word count around " + wordCount + " words:\n\n" + summary;
        return sendToGeminiAPI(prompt);
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

    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("Content-Disposition");
        String[] contentDispositionHeader = contentDisposition.split(";");
        for (String cd : contentDispositionHeader) {
            if (cd.trim().startsWith("filename")) {
                return cd.substring(cd.indexOf("=") + 2, cd.length() - 1);
            }
        }
        return null;
    }

    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        }
        return null;
    }

    private void cleanUploadsDirectory() {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        } else {
            File[] files = uploadDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        file.delete();
                    }
                }
            }
        }
    }

    private int getWordCountParameter(HttpServletRequest request) {
        String wordCountStr = request.getParameter("wordCount");
        try {
            return Integer.parseInt(wordCountStr);
        } catch (NumberFormatException e) {
            return 100; // Default word count
        }
    }
}
