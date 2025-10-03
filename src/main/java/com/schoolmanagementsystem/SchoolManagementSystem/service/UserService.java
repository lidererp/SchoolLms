package com.schoolmanagementsystem.SchoolManagementSystem.service;

import com.schoolmanagementsystem.SchoolManagementSystem.GlobalExceptionHandler.ResourceNotFoundException;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.User;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.UserRepository;
import com.schoolmanagementsystem.SchoolManagementSystem.utility.TokenGenerator;
import org.springframework.messaging.MessagingException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TemplateEngine templateEngine;
    private final EmailService emailService;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            TemplateEngine templateEngine, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.templateEngine = templateEngine;
        this.emailService = emailService;
    }

    public List<User> getUsersWithCursor(Long cursor) {
        return (cursor == null) ? userRepository.findTop10ByOrderByIdDesc() : userRepository.findTop10ByIdGreaterThanOrderByIdDesc(cursor);
    }


    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus("new");
        return userRepository.save(user);

    }

    @Transactional
    public User updateUser(Long id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setName(userDetails.getName());
            user.setEmail(userDetails.getEmail());
            user.setRole(userDetails.getRole());
            user.setUserCode(userDetails.getUserCode());
            // Update password if provided and encode it
            if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            }
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


    // Send password reset email
    public void sendPasswordResetEmail(String email) {
        // Find user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate a reset token
//        String resetToken = UUID.randomUUID().toString();
        String resetToken = TokenGenerator.generateNumericToken();
        LocalDateTime expirationDate = LocalDateTime.now().plusHours(1); // Token expires in 1 hour

        // Store the reset token and expiration date in the user entity
        user.setResetToken(resetToken);
        user.setTokenExpirationDate(expirationDate);
        userRepository.save(user);

        // Create the reset password URL
        String resetUrl = buildResetUrl(resetToken);

        // Call the sendResetPasswordEmail method from EmailService to send the email
        try {
            emailService.sendResetPasswordEmail(user.getEmail(), resetToken, resetUrl);
        } catch (MessagingException | jakarta.mail.MessagingException e) {
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }

    // Build the reset password URL
    private String buildResetUrl(String token) {
        return "http://localhost:3000/reset-password";
    }


    @Transactional
    // Reset password method (use this after validating the reset token)
    public boolean resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

        if (user.getTokenExpirationDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired");
        }

        // Update the user's password
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null); // Clear the reset token after use
        user.setTokenExpirationDate(null); // Clear the expiration date
        userRepository.save(user);
        return true;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    @Transactional
    public User updateUserAndReflectInStaff(Long userId, User updatedUserData) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userid", userId));

        // Update user fields
        existingUser.setName(updatedUserData.getName());
//        existingUser.setEmail(updatedUserData.getEmail());
        existingUser.setUserCode(updatedUserData.getUserCode());
//        existingUser.setRole(updatedUserData.getRole());
        existingUser.setPassword(passwordEncoder.encode(updatedUserData.getPassword()));
        existingUser.setStatus(updatedUserData.getStatus());

        userRepository.save(existingUser);
        return existingUser;
    }


}

