package com.schoolmanagementsystem.SchoolManagementSystem.GlobalExceptionHandler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        // Create an error response with user-friendly details
        ErrorResponse errorResponse = new ErrorResponse(
                "Resource not found",
                String.format("%s not found with %s: '%s'", ex.getResourceName(), ex.getFieldName(), ex.getFieldValue()),
                LocalDateTime.now().toString(),
                HttpStatus.NOT_FOUND.value()

        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Handle validation errors for @Valid annotation (e.g., @NotBlank, @Size)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        // Extract the field error details
        List<String> errorMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> "The field '" + fieldError.getField() + "' " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());

        // Create the response
        ErrorResponse errorResponse = new ErrorResponse(
                "Validation error",
                errorMessages,  // List of errors for each field
                LocalDateTime.now().toString(),
                HttpStatus.BAD_REQUEST.value()

        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String errorTitle = "Data Validation Error";
        String errorDetail = "Invalid data provided. Please check your input.";
        HttpStatus status = HttpStatus.BAD_REQUEST;

        String rootMsg = ex.getMostSpecificCause().getMessage();

        // 1. Handle duplicate entries
        if (rootMsg.contains("Duplicate entry")) {
            errorTitle = "Duplicate Value";
            Optional<String> duplicateValue = extractBetweenQuotes(rootMsg);
            errorDetail = duplicateValue.isPresent()
                    ? String.format("Value '%s' already exists. Must be unique.", duplicateValue.get())
                    : "Duplicate value detected. Field must be unique.";
        }
        // 2. Handle foreign key violations
        else if (rootMsg.contains("foreign key constraint") || rootMsg.contains("a foreign key constraint fails")) {
            errorTitle = "Reference Error";
            Optional<String> refValue = extractBetweenQuotes(rootMsg);
            errorDetail = refValue.isPresent()
                    ? String.format("Invalid reference: %s doesn't exist", refValue.get())
                    : "Referenced entity doesn't exist. Please check your references.";
        }
        // 3. Handle null violations
        else if (rootMsg.contains("cannot be null") || rootMsg.contains("NULL not allowed")) {
            errorTitle = "Missing Required Field";
            Optional<String> columnName = extractColumnName(rootMsg);
            errorDetail = columnName.isPresent()
                    ? String.format("Field '%s' is required", columnName.get())
                    : "Required field is missing or null.";
        }

        return new ResponseEntity<>(
                new ErrorResponse(errorTitle, errorDetail, LocalDateTime.now().toString(), status.value()),
                status
        );
    }

    // Helper to extract text between single quotes
    private Optional<String> extractBetweenQuotes(String input) {
        Pattern pattern = Pattern.compile("'(.*?)'");
        Matcher matcher = pattern.matcher(input);
        return matcher.find() ? Optional.of(matcher.group(1)) : Optional.empty();
    }

    // Helper to extract column name from SQL errors
    private Optional<String> extractColumnName(String input) {
        // Try to find patterns like "column 'XYZ'"
        Pattern pattern = Pattern.compile("column ['\"](.*?)['\"]");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) return Optional.of(matcher.group(1));

        // Try to find patterns like "field 'XYZ'"
        pattern = Pattern.compile("field ['\"](.*?)['\"]");
        matcher = pattern.matcher(input);
        return matcher.find() ? Optional.of(matcher.group(1)) : Optional.empty();
    }


    // Handle database constraint violations (e.g., unique constraint violations)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        // Get the constraint violation message
        String message = ex.getMessage();
        String fieldName = extractFieldNameFromConstraint(message);
        System.out.println(ex + "ex");
        System.out.println(message + "message");
        // Create a user-friendly error message
        String customMessage = "The field '" + fieldName + "' must be unique.Please provide a unique value.";

        // Create the response
        ErrorResponse errorResponse = new ErrorResponse(
                "Database constraint violation",
                customMessage,
                LocalDateTime.now().toString(),
                HttpStatus.BAD_REQUEST.value()

        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Helper method to extract field name from the constraint violation message
    private String extractFieldNameFromConstraint(String message) {
        if (message != null && message.contains("propertyPath=")) {
            // Extract the field name from the message (e.g., "propertyPath=role")
            int startIndex = message.indexOf("propertyPath=") + "propertyPath=".length();
            int endIndex = message.indexOf(",", startIndex);

            if (endIndex > startIndex) {
                return message.substring(startIndex, endIndex).trim();
            }
        }
        return "unknown";
    }

    // Handle other generic exceptions (e.g., server errors)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "An unexpected error occurred",
                ex.getMessage(),
                LocalDateTime.now().toString(),
                HttpStatus.INTERNAL_SERVER_ERROR.value()

        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Access Denied",
                ex.getMessage(),
                LocalDateTime.now().toString(),
                HttpStatus.FORBIDDEN.value()  // Or HttpStatus.UNAUTHORIZED if appropriate
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFound(NoHandlerFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Not Found",
                "The requested URL '" + ex.getRequestURL() + "' was not found on this server.",
                LocalDateTime.now().toString(),
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        // Create a user-friendly error message
        ErrorResponse errorResponse = new ErrorResponse(
                "Method Not Allowed",
                "Request method '" + ex.getMethod() + "' not supported for this endpoint.",
                LocalDateTime.now().toString(),
                HttpStatus.METHOD_NOT_ALLOWED.value()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    // Handle MailException (e.g., errors while sending the email)
    @ExceptionHandler(MailException.class)
    public ResponseEntity<ErrorResponse> handleMailException(MailException ex) {
        // Log the exception
        ex.printStackTrace();

        // Return an error response
        ErrorResponse errorResponse = new ErrorResponse(
                "Email Sending Error",
                "Failed to send the email: " + ex.getMessage(),
                LocalDateTime.now().toString(),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Invalid Request",
                ex.getMessage(),
                LocalDateTime.now().toString(),
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


}
