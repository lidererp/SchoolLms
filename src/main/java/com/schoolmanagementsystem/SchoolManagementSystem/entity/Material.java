package com.schoolmanagementsystem.SchoolManagementSystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Material {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "File name is required")
    @Size(max = 255, message = "File name must be at most 255 characters")
    private String fileName;

    @Size(min = 4, max = 50, message = "File type must be at most 50 characters")
    private String fileType;

    @NotBlank(message = "File URL is required")
    @Size(min = 4, max = 1000, message = "File URL must be at most 1000 characters")
    private String fileUrl;

    @Size(min = 4, max = 500, message = "Description must be at most 500 characters")
    private String description;

    @NotNull(message = "Upload time is required")
    private LocalDateTime uploadedAt;

    @ManyToOne
    @JoinColumn(name = "standard_id")
    private Standard standard;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

    @ManyToOne
    @JoinColumn(name = "academic_year_id", nullable = false)
    @NotNull(message = "Academic Year is required")
    private AcademicYear academicYear;


}

