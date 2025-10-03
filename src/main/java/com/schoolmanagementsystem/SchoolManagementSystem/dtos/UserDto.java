package com.schoolmanagementsystem.SchoolManagementSystem.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {


    private Long id;

    private String name;

    private String email;

    private String role;

    private String userCode;

    private LocalDateTime createdAt;

    private String status;

    private List<String> authorities;


}

