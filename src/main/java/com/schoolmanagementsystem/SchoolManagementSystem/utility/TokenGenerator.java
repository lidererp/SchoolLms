package com.schoolmanagementsystem.SchoolManagementSystem.utility;

import java.security.SecureRandom;

public class TokenGenerator {


    private static final SecureRandom secureRandom = new SecureRandom();

    // Cryptographically secure random generator
    private static final int TOKEN_LENGTH = 6;  // Length of token, e.g., 6-digit OTP

    // Method to generate a numeric OTP-style token
    public static String generateNumericToken() {
        // Generate a random number with the desired length
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            token.append(secureRandom.nextInt(10));  // Generate a random digit from 0-9
        }
        return token.toString();  // Return the token as a string
    }


}
