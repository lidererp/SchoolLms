package com.schoolmanagementsystem.SchoolManagementSystem.GlobalExceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {


    private String message;
    private Object details;  // This can be a String or a List depending on the error type
    private String timestamp;
    private int statusCode;


}
