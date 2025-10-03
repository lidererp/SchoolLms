package com.schoolmanagementsystem.SchoolManagementSystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "user_table")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "email cannot be blank")
    @Size(min = 5, max = 70, message = "email length should be between 5 t0 70")
    @Email(message = "Invalid email format")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "role cannot be blank")
    private String role;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "userCode cannot be blank")
    private String userCode;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private String status;

    @Column(nullable = false)
    private String password;

    private String resetToken;

    private LocalDateTime tokenExpirationDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;


}

