package com.schoolmanagementsystem.SchoolManagementSystem.GlobalExceptionHandler;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class DuplicateResourceException extends RuntimeException {

    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;

    @Override
    public String getMessage() {
        return String.format("%s already exists with %s : '%s'", resourceName, fieldName, fieldValue);
    }

}