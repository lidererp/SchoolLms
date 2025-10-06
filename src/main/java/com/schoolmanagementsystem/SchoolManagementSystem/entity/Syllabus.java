package com.schoolmanagementsystem.SchoolManagementSystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Syllabus {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curriculum_subject_id", nullable = false)
    private CurriculumSubject curriculumSubject;

    @Column(nullable = false)
    private Integer unitNumber;

    @Column(nullable = false)
    private String unitTitle;

    @Column(nullable = false)
    private String topics;

    private Integer estimatedHours;


}

