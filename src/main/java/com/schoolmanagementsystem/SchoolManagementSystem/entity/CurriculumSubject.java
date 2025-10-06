package com.schoolmanagementsystem.SchoolManagementSystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurriculumSubject {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curriculum_id", nullable = false)
    @NotNull(message = "Curriculum must be provided")
    private Curriculum curriculum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    @NotNull(message = "Subject must be provided")
    private Subject subject;

    @OneToMany(mappedBy = "curriculumSubject", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotEmpty(message = "At least one syllabus must be provided")
    private List<Syllabus> syllabusList = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;


}
