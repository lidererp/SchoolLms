package com.schoolmanagementsystem.SchoolManagementSystem.GlobalExceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceNotFoundException extends RuntimeException {


    private String resourceName;
    private String fieldName;
    private Object fieldValue;


}
