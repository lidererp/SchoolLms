package com.schoolmanagementsystem.SchoolManagementSystem.repository;

import com.schoolmanagementsystem.SchoolManagementSystem.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {

}

