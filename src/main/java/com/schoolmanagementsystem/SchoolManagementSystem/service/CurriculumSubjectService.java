package com.schoolmanagementsystem.SchoolManagementSystem.service;

import com.schoolmanagementsystem.SchoolManagementSystem.dtos.CurriculumSubjectDTO;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.Curriculum;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.CurriculumSubject;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.Subject;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurriculumSubjectService {


    private final SubjectRepository subjectRepository;
    private final SyllabusService syllabusService;

    public CurriculumSubject buildFromDTO(CurriculumSubjectDTO dto, Curriculum curriculum) {
        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found: " + dto.getSubjectId()));

        CurriculumSubject cs = new CurriculumSubject();
        cs.setCurriculum(curriculum);
        cs.setSubject(subject);

        dto.getSyllabusList().forEach(s -> cs.getSyllabusList().add(syllabusService.buildFromDTO(s, cs)));

        return cs;
    }


}
