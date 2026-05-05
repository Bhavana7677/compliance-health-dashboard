package com.internship.tool.service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AiServiceClient {

    public static String callAIService(String inputText) {
        try {
            URL url = new URL("http://127.0.0.1:5000/generate-report");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInput = "{\"text\": \"" + inputText + "\"}";

            OutputStream os = conn.getOutputStream();
            os.write(jsonInput.getBytes());
            os.flush();
            os.close();

            return "AI processed: " + inputText;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: AI service not reachable";
        }
    }

    public static void main(String[] args) {
        String response = callAIService("Hello from Java");
        System.out.println("Response: " + response);
    }
}