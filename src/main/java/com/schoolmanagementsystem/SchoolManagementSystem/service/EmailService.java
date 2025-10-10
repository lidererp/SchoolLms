package com.schoolmanagementsystem.SchoolManagementSystem.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
public class EmailService implements EmailSender {


    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendWelcomeEmail(String toEmail, String firstName) throws MessagingException {
        // Prepare Thymeleaf context with dynamic variables for the template
        Context context = new Context();
        context.setVariable("firstName", firstName);

        // Process the Thymeleaf template to get the email body
        String body = templateEngine.process("welcome-email-template", context);

        // Create MimeMessage to send the email
        var message = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(message, true);

        try {
            helper.setTo(toEmail);
            helper.setSubject("Welcome to the Team!");
            helper.setText(body, true); // true means treat the body as HTML

            // Send the email
            mailSender.send(message);
        } catch (MailException ex) {
            // Catch specific MailException and log it
            System.err.println("Failed to send email: " + ex.getMessage());
            ex.printStackTrace();  // Log stack trace for debugging
            throw new RuntimeException("Failed to send welcome email", ex);
        } catch (Exception ex) {
            // Catch other exceptions and propagate them
            ex.printStackTrace();
            throw new RuntimeException("Failed to send welcome email", ex);
        }
    }

    // New method for sending the password reset email

    @Override
    public void sendResetPasswordEmail(String toEmail, String resetToken, String resetUrl) throws MessagingException {
        try {
            // Prepare Thymeleaf context with dynamic variables for the template
            Context context = new Context();
            context.setVariable("resetUrl", resetUrl);
            context.setVariable("resetToken", resetToken);
            // Process the Thymeleaf template to get the email body
            String body = templateEngine.process("reset-password-template", context);

            // Create MimeMessage to send the email
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject("Password Reset Request");
            helper.setText(body, true); // true means treat the body as HTML

            // Send the email
            mailSender.send(message);
        } catch (MailException ex) {
            // Catch specific MailException and log it
            System.err.println("Failed to send reset password email: " + ex.getMessage());
            ex.printStackTrace();  // Log stack trace for debugging
            throw new RuntimeException("Failed to send reset password email", ex);
        } catch (Exception ex) {
            // Catch other exceptions and propagate them
            ex.printStackTrace();
            throw new RuntimeException("Failed to send reset password email", ex);
        }
    }

    public void sendLogoutAllConfirmationEmail(String toEmail, String logoutUrl) throws MessagingException {
        try {
            Context context = new Context();
            context.setVariable("resetUrl", logoutUrl);  // the button/link in the template uses this

            String body = templateEngine.process("logout-all-devices-template", context); // use your new template

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject("Confirm Logout from All Devices");
            helper.setText(body, true); // HTML content

            mailSender.send(message);
        } catch (MailException ex) {
            System.err.println("Failed to send logout confirmation email: " + ex.getMessage());
            ex.printStackTrace();
            throw new RuntimeException("Failed to send logout confirmation email", ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Failed to send logout confirmation email", ex);
        }
    }

    public void sendStudentCredentials(String toEmail, String studentName, String password) throws MessagingException {
        Context context = new Context();
        context.setVariable("studentName", studentName);
        context.setVariable("studentEmail", toEmail);
        context.setVariable("password", password);

        String body = templateEngine.process("student-email.html", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("Your Student Account Details");
        helper.setText(body, true); // true = HTML

        mailSender.send(message);
    }

}
