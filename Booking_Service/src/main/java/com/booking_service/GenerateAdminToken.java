package com.booking_service;

import com.booking_service.security.JwtUtil;

public class GenerateAdminToken {
    public static void main(String[] args) {
        JwtUtil jwtUtil = new JwtUtil();

        // Use your base64-encoded secret here (same as in application.properties)
        String secret = "11xnW7c3G4y92VYOpZqriVKxjGl7iNHACzUVQn3f6p9YZLHY0lIz8tGPr7oHm9uWVYq/M6epq3/Z2tzhrqZnzQ==";

        String adminToken = jwtUtil.generateToken("Prasanna205@gmail.com", "ROLE_ADMIN", secret);
        System.out.println("Admin Token: " + adminToken);
    }
}
