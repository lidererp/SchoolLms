package com.schoolmanagementsystem.SchoolManagementSystem.entity;


import com.schoolmanagementsystem.SchoolManagementSystem.enums.AcademicStatus;
import com.schoolmanagementsystem.SchoolManagementSystem.utility.AcademicYearGenerator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcademicRecord {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "section_id",nullable=true)
    private Section section;

    @Column(nullable = false)
    @NotBlank(message = "Academic year is required")
    private String academicYear = AcademicYearGenerator.generateAcademicYear();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AcademicStatus status = AcademicStatus.ACTIVE;


}
