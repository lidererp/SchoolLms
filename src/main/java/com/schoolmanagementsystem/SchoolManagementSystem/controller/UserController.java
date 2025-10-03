package com.schoolmanagementsystem.SchoolManagementSystem.controller;

import com.schoolmanagementsystem.SchoolManagementSystem.GlobalExceptionHandler.ResourceNotFoundException;
import com.schoolmanagementsystem.SchoolManagementSystem.dtos.UserDto;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.User;
import com.schoolmanagementsystem.SchoolManagementSystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers(@RequestParam(required = false) Long cursor) {

        return userService.getUsersWithCursor(cursor);

    }


    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {

        User foundUser = userService.getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id)); // Throw exception if not found

        // Return the user if found
        return ResponseEntity.ok(foundUser);

    }


    // Create a new user
    @PostMapping("/create")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {

        User createdUser = userService.createUser(user);
        return ResponseEntity.status(201).body(createdUser);

    }

    // Update an existing user
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
//    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {

        try {
            User updatedUser = userService.updateUserAndReflectInStaff(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }

    }

    // Delete a user by ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build(); // HTTP 204 No Content
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }

    }

    // Endpoint for sending the password reset email
    @PostMapping("/forgot")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {

        userService.sendPasswordResetEmail(email);
        return ResponseEntity.ok("Password reset email sent!");

    }

    // Endpoint for resetting the password (user clicks the reset link)
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String token,          // From form input
            @RequestParam String newPassword     // From form input
    ) {

        boolean isResetSuccessful = userService.resetPassword(token, newPassword);
        return isResetSuccessful
                ? ResponseEntity.ok("Password reset successfully.")
                : ResponseEntity.badRequest().body("Invalid token.");

    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(Principal principal) {

        String email = principal.getName();
        User user = userService.getUserByEmail(email);

        List<String> authorities = new ArrayList<>();
        authorities.add("ROLE_" + user.getRole());


        UserDto dto = new UserDto(
                user.getId(), user.getName(), user.getEmail(),
                user.getRole(), user.getUserCode(), user.getCreatedAt(),
                user.getStatus(),
                authorities
        );

        return ResponseEntity.ok(dto);

    }


}
