package com.schoolmanagementsystem.SchoolManagementSystem.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.schoolmanagementsystem.SchoolManagementSystem.enums.TimetableScope;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimetableConfig {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "scope", nullable = false)
    private TimetableScope scope;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

    @ManyToOne
    @JoinColumn(name = "standard_id")
    private Standard standard;

    @ManyToOne
    @JoinColumn(name = "academic_year_id", nullable = false)
    private AcademicYear academicYear;

    @Column(name = "total_periods", nullable = false)
    private Integer totalPeriods;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "include_saturday", nullable = false)
    private Boolean includeSaturday = false;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "timetable_config_id")
    private List<BreakSlot> breaks = new ArrayList<>();


}