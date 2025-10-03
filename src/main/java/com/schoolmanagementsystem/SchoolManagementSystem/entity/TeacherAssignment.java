package com.schoolmanagementsystem.SchoolManagementSystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherAssignment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @Column(name = "teacher_name", nullable = false)
    private String teacherName;

    @Column(name = "academic_year", nullable = false)
    private String academicYear;

    @CreationTimestamp
    private LocalDateTime assignedDate;

    private LocalDateTime endDate;

    @Column(name = "is_current")
    private Boolean isCurrent = true;


}