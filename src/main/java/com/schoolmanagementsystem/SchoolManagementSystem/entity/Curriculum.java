package com.schoolmanagementsystem.SchoolManagementSystem.entity;

import com.schoolmanagementsystem.SchoolManagementSystem.enums.CurriculumScope;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
public class Curriculum {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Scope type is required")
    private CurriculumScope scopeType; // STANDARD or SECTION

    @Column(nullable = false)
    @NotNull(message = "Scope ID is required")
    private Long scopeId; // standardId or sectionId

    @Column(nullable = false)
    @NotBlank(message = "Academic year is required")
    private String academicYear;

    @OneToMany(mappedBy = "curriculum", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CurriculumSubject> curriculumSubjects = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;


}