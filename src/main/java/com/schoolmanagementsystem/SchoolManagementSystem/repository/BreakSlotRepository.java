package com.schoolmanagementsystem.SchoolManagementSystem.repository;

import com.schoolmanagementsystem.SchoolManagementSystem.entity.BreakSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BreakSlotRepository extends JpaRepository<BreakSlot, Long> {


    List<BreakSlot> findByTimetableConfig_Id(Long timetableConfigId);

}
