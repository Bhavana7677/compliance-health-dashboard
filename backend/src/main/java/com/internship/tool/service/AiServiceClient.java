package com.internship.tool.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;

public class AiServiceClient {

    public static String callAIService(String inputText) {
     if (inputText == null || inputText.trim().isEmpty()) {
    return "Error: Input cannot be empty";
     }

     if (inputText.length() > 100) {
    return "Error: Input too long";
    }
        try {
            long startTime = System.currentTimeMillis();
            URL url = new URL("http://127.0.0.1:5000/generate-report");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInput = "{\"text\": \"" + inputText + "\"}";
            System.out.println("[" + LocalDateTime.now() + "] Sending request: " + inputText);

            OutputStream os = conn.getOutputStream();
            os.write(jsonInput.getBytes());
            os.flush();
            os.close();
            int statusCode = conn.getResponseCode();
            System.out.println("HTTP Status Code: " + statusCode);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );

            StringBuilder responseBuilder = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                responseBuilder.append(line);
            }

           String response = responseBuilder.toString();
           System.out.println("[" + LocalDateTime.now() + "] Received response: " + response);
           br.close();
           long endTime = System.currentTimeMillis();
           System.out.println("Response Time: " + (endTime - startTime) + " ms");

// clean JSON response
         return response;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: AI service not reachable";
        }
    }

    public static void main(String[] args) {
        String response = callAIService("");
        System.out.println("Response: " + response);
    }
}