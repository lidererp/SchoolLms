package com.schoolmanagementsystem.SchoolManagementSystem.service;

import jakarta.mail.MessagingException;

public interface EmailSender {


    void sendWelcomeEmail(String toEmail, String firstName) throws MessagingException;

    void sendResetPasswordEmail(String toEmail, String resetToken, String resetUrl) throws MessagingException;


}
