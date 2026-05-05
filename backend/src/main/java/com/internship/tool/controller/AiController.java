package com.internship.tool.controller;

import com.internship.tool.service.AiServiceClient;

public class AiController {

    public static void main(String[] args) {
        String response = AiServiceClient.callAIService("Test input 123");
        System.out.println("Controller Response: " + response);
    }
}