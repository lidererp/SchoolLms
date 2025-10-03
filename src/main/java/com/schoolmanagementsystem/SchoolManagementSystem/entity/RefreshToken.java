package com.schoolmanagementsystem.SchoolManagementSystem.entity;

import com.schoolmanagementsystem.SchoolManagementSystem.enums.TokenStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class RefreshToken {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    private Long userId;

    private Date expiryDate;

    private String userAgent;

    private String ipAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TokenStatus status = TokenStatus.ACTIVE;


}
