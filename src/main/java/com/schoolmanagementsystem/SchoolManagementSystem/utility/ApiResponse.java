package com.schoolmanagementsystem.SchoolManagementSystem.utility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {


    private String message;
    private int status;
    private LocalDateTime timestamp;

    public static ApiResponse of(String message, int status) {
        return ApiResponse.builder()
                .message(message)
                .status(status)
                .timestamp(LocalDateTime.now())
                .build();
    }


}

