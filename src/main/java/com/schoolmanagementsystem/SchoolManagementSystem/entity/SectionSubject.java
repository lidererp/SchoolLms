package com.schoolmanagementsystem.SchoolManagementSystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "section_subjects",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"section_id", "subject_id", "academic_year"})
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectionSubject {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(name = "academic_year", nullable = false)
    private String academicYear;

    @CreationTimestamp
    private LocalDateTime createdAt;


}