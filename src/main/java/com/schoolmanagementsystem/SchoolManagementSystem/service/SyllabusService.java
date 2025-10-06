package com.schoolmanagementsystem.SchoolManagementSystem.service;

import com.schoolmanagementsystem.SchoolManagementSystem.dtos.SyllabusDTO;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.CurriculumSubject;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.Syllabus;
import org.springframework.stereotype.Service;

@Service
public class SyllabusService {


    public Syllabus buildFromDTO(SyllabusDTO dto, CurriculumSubject parent) {
        Syllabus syllabus = new Syllabus();
        syllabus.setCurriculumSubject(parent);
        syllabus.setUnitNumber(dto.getUnitNumber());
        syllabus.setUnitTitle(dto.getUnitTitle());
        syllabus.setTopics(dto.getTopics());
        syllabus.setEstimatedHours(dto.getEstimatedHours());
        return syllabus;
    }


}