package com.internship.tool.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class AiServiceClient {

    static int requestCount = 0;

    static int failedRequests = 0;

    static ArrayList<String> requestHistory =
            new ArrayList<>();

    public static String sanitizeInput(String input) {

        input = input.trim();

        input = input.replaceAll("[<>]", "");

        return input;
    }

    public static void writeLog(String fileName, String logMessage) {

        try {

            PrintWriter writer =
                    new PrintWriter(new FileWriter(fileName, true));

            writer.println(logMessage);

            writer.close();

        } catch (Exception e) {

            System.out.println("Log writing failed");
        }
    }

    public static String callAIService(String inputText) {

        inputText = sanitizeInput(inputText);

        requestCount++;

        requestHistory.add(inputText);

        if (inputText == null || inputText.trim().isEmpty()) {
            return "Error: Input cannot be empty";
        }

        if (inputText.length() > 100) {
            return "Error: Input too long";
        }

        try {

            long startTime = System.currentTimeMillis();

            URL url = new URL("http://127.0.0.1:5000/generate-report");

            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInput =
                    "{\"text\": \"" + inputText + "\"}";

            System.out.println("[" + LocalDateTime.now()
                    + "] Sending request: " + inputText);

            System.out.println("Total Requests: " + requestCount);

            System.out.println("Request History: "
                    + requestHistory);

            writeLog("success_logs.txt",
                    "[" + LocalDateTime.now()
                    + "] Sending request: " + inputText);

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

            System.out.println("[" + LocalDateTime.now()
                    + "] Received response: " + response);

            writeLog("success_logs.txt",
                    "[" + LocalDateTime.now()
                    + "] Received response: " + response);

            br.close();

            long endTime = System.currentTimeMillis();

            System.out.println("Response Time: "
                    + (endTime - startTime) + " ms");

            String cleanedText =
                    response.split("\"cleaned_text\":")[1]
                            .split(",")[0]
                            .replace("\"", "")
                            .trim();

            String message =
                    response.split("\"message\":")[1]
                            .replace("}", "")
                            .replace("\"", "")
                            .trim();

            return "Cleaned Text: "
                    + cleanedText
                    + " | Message: "
                    + message;

        } catch (Exception e) {

            String errorMessage =
                    "ERROR TYPE: "
                            + e.getClass().getSimpleName()
                            + " | MESSAGE: "
                            + e.getMessage();

            System.out.println(errorMessage);

            failedRequests++;

            System.out.println("Failed Requests: "
                    + failedRequests);

            writeLog("error_logs.txt", errorMessage);

            return "Error: AI service not reachable";
        }
    }

    public static void main(String[] args) {

        String response =
                callAIService("   <Test input 123>   ");

        System.out.println("Response: " + response);
    }
}