package com.schoolmanagementsystem.SchoolManagementSystem.service;

import com.schoolmanagementsystem.SchoolManagementSystem.dtos.SectionSubjectsRequest;
import com.schoolmanagementsystem.SchoolManagementSystem.dtos.SectionSubjectsResponse;
import com.schoolmanagementsystem.SchoolManagementSystem.dtos.SubjectAssignment;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.Section;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.SectionSubject;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.Subject;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.SectionRepository;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.SectionSubjectRepository;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SectionSubjectService {


    private final SectionSubjectRepository sectionSubjectRepository;
    private final SectionRepository sectionRepository;
    private final SubjectRepository subjectRepository;

    public SectionSubjectService(SectionSubjectRepository sectionSubjectRepository,
                                 SectionRepository sectionRepository,
                                 SubjectRepository subjectRepository) {
        this.sectionSubjectRepository = sectionSubjectRepository;
        this.sectionRepository = sectionRepository;
        this.subjectRepository = subjectRepository;
    }

    public List<SectionSubjectsResponse> assignSubjectsToSection(SectionSubjectsRequest request) {
        Section section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> new RuntimeException("Section not found with id: " + request.getSectionId()));

        List<SectionSubject> sectionSubjects = new ArrayList<>();

        for (SubjectAssignment assignment : request.getSubjects()) {
            Subject subject = subjectRepository.findById(assignment.getSubjectId())
                    .orElseThrow(() -> new RuntimeException("Subject not found with id: " + assignment.getSubjectId()));

            // Check if subject already assigned
            if (!sectionSubjectRepository.existsBySectionIdAndSubjectIdAndAcademicYear(
                    section.getId(), subject.getId(), request.getAcademicYear())) {

                SectionSubject sectionSubject = new SectionSubject();
                sectionSubject.setSection(section);
                sectionSubject.setSubject(subject);
                sectionSubject.setAcademicYear(request.getAcademicYear());

                sectionSubjects.add(sectionSubjectRepository.save(sectionSubject));
            }
        }

        return toResponseDTO(sectionSubjects);
    }

    public List<SectionSubjectsResponse> getSectionSubjects(Long sectionId) {
        List<SectionSubject> sectionSubjects = sectionSubjectRepository.findBySectionId(sectionId);
        return toResponseDTO(sectionSubjects);
    }

    public List<SectionSubjectsResponse> getSectionSubjectsByAcademicYear(Long sectionId, String academicYear) {
        List<SectionSubject> sectionSubjects = sectionSubjectRepository.findBySectionIdAndAcademicYear(sectionId, academicYear);
        return toResponseDTO(sectionSubjects);
    }

    public void removeSubjectFromSection(Long sectionSubjectId) {
        sectionSubjectRepository.deleteById(sectionSubjectId);
    }

    private List<SectionSubjectsResponse> toResponseDTO(List<SectionSubject> sectionSubjects) {
        return sectionSubjects.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    private SectionSubjectsResponse toResponseDTO(SectionSubject sectionSubject) {
        SectionSubjectsResponse response = new SectionSubjectsResponse();
        response.setId(sectionSubject.getId());
        response.setSectionId(sectionSubject.getSection().getId());
        response.setSectionName(sectionSubject.getSection().getSectionName());
        response.setStandardName(sectionSubject.getSection().getStandard().getClassName());
        response.setSubjectId(sectionSubject.getSubject().getId());
        response.setSubjectName(sectionSubject.getSubject().getSubjectName());
        response.setSubjectCode(sectionSubject.getSubject().getSubjectCode());
        response.setAcademicYear(sectionSubject.getAcademicYear());
        response.setCreatedAt(sectionSubject.getCreatedAt());
        return response;
    }


}