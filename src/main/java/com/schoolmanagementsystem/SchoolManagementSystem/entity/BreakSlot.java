package com.schoolmanagementsystem.SchoolManagementSystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BreakSlot {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "break_name", nullable = false)
    private String breakName;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "day_of_week")
    private String dayOfWeek; // Optional: "MONDAY", "TUESDAY", etc.

    @ManyToOne
    @JoinColumn(name = "timetable_config_id", nullable = false)
    @JsonBackReference
    private TimetableConfig timetableConfig;


}