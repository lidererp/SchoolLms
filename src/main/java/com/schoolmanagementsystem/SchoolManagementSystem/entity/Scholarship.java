package com.schoolmanagementsystem.SchoolManagementSystem.entity;

import com.schoolmanagementsystem.SchoolManagementSystem.enums.ScholarshipType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Scholarship {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Scholarship name is required")
    @Size(max = 100, message = "Scholarship name must be less than 100 characters")
    private String name;          // Scholarship name

    @NotNull(message = "Scholarship type is required")
    @Enumerated(EnumType.STRING)

    private ScholarshipType type;   // Academic, Sports, ARTS, STEM


    @Size(min = 3, max = 500, message = "Description must be less than 500 characters")
    private String description;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    @NotBlank(message = "Applicable grade is required")
    private String applicableGrade;

    @DecimalMin(value = "0.0", inclusive = true, message = "Minimum percentage cannot be negative")
    @DecimalMax(value = "100.0", message = "Minimum percentage cannot exceed 100")
    private Double minPercentage;

    @DecimalMin(value = "0.0", inclusive = true, message = "Maximum family income cannot be negative")
    private Double maxFamilyIncome;

    private Boolean active = true;

    @NotNull(message = "Start date and time are required")
    private LocalDateTime startDate;

    @NotNull(message = "End date and time are required")
    private LocalDateTime endDate;

    private LocalDateTime createdDate = LocalDateTime.now();

    private LocalDateTime updatedDate;



}
