package com.schoolmanagementsystem.SchoolManagementSystem.service;

import com.schoolmanagementsystem.SchoolManagementSystem.GlobalExceptionHandler.DuplicateResourceException;
import com.schoolmanagementsystem.SchoolManagementSystem.GlobalExceptionHandler.ResourceNotFoundException;
import com.schoolmanagementsystem.SchoolManagementSystem.dtos.*;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.Curriculum;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.CurriculumSubject;
import com.schoolmanagementsystem.SchoolManagementSystem.enums.CurriculumScope;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.CurriculumRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CurriculumService {


    private final CurriculumRepository curriculumRepository;
    private final CurriculumSubjectService curriculumSubjectService;
    private final SyllabusService syllabusService;

    public CurriculumService(CurriculumRepository curriculumRepository, CurriculumSubjectService curriculumSubjectService, SyllabusService syllabusService) {
        this.curriculumRepository = curriculumRepository;
        this.curriculumSubjectService = curriculumSubjectService;
        this.syllabusService = syllabusService;
    }

    @Transactional
    public String createCurriculum(CurriculumRequestDTO dto) {

        // Prevent duplicate curriculum for same scope/year
        checkDuplicateCurriculum(dto.getScopeType(), dto.getScopeId(), dto.getAcademicYear(), null);
        checkAcademicYearFormat(dto.getAcademicYear());

        validateSyllabusUniqueness(dto.getCurriculumSubjects());

        Curriculum curriculum = new Curriculum();
        curriculum.setScopeType(dto.getScopeType());
        curriculum.setScopeId(dto.getScopeId());
        curriculum.setAcademicYear(dto.getAcademicYear());

        dto.getCurriculumSubjects().forEach(subDTO -> {
            CurriculumSubject subject = curriculumSubjectService.buildFromDTO(subDTO, curriculum);
            curriculum.getCurriculumSubjects().add(subject);
        });

        curriculumRepository.save(curriculum);
        return "Curriculum created successfully";

    }

    @Transactional(readOnly = true)
    public CurriculumResponseDTO getCurriculum(Long id) {

        return curriculumRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Curriculum", "id", id));

    }

    @Transactional(readOnly = true)
    public List<CurriculumResponseDTO> getAllCurriculums() {

        return curriculumRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();

    }

    @Transactional
    public CurriculumResponseDTO updateCurriculum(Long id, CurriculumRequestDTO dto) {

        Curriculum curriculum = curriculumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curriculum", "id", id));

        checkAcademicYearFormat(dto.getAcademicYear());

        checkDuplicateCurriculum(dto.getScopeType(), dto.getScopeId(), dto.getAcademicYear(), id);

        validateSyllabusUniqueness(dto.getCurriculumSubjects());

        curriculum.setScopeType(dto.getScopeType());
        curriculum.setScopeId(dto.getScopeId());
        curriculum.setAcademicYear(dto.getAcademicYear());

        Map<Long, CurriculumSubject> existingSubjects = curriculum.getCurriculumSubjects().stream()
                .collect(Collectors.toMap(s -> s.getSubject().getId(), s -> s));

        List<CurriculumSubject> updatedSubjects = new ArrayList<>();

        for (CurriculumSubjectDTO subDTO : dto.getCurriculumSubjects()) {
            CurriculumSubject subject;
            if (existingSubjects.containsKey(subDTO.getSubjectId())) {
                subject = existingSubjects.get(subDTO.getSubjectId());
                updateSyllabi(subject, subDTO.getSyllabusList());
            } else {
                subject = curriculumSubjectService.buildFromDTO(subDTO, curriculum);
            }
            updatedSubjects.add(subject);
        }

        curriculum.getCurriculumSubjects().clear();
        curriculum.getCurriculumSubjects().addAll(updatedSubjects);

        Curriculum saved = curriculumRepository.save(curriculum);
        return toDTO(saved);

    }


    private void updateSyllabi(CurriculumSubject subject, List<SyllabusDTO> syllabusDTOs) {
        subject.getSyllabusList().clear();
        syllabusDTOs.forEach(s -> subject.getSyllabusList().add(syllabusService.buildFromDTO(s, subject)));
    }

    @Transactional
    public void deleteCurriculum(Long id) {

        Curriculum curriculum = curriculumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curriculum", "id", id));
        curriculumRepository.delete(curriculum);

    }

    private void checkDuplicateCurriculum(CurriculumScope scopeType, Long scopeId, String academicYear, Long currentCurriculumId) {
        curriculumRepository.findByScopeTypeAndScopeIdAndAcademicYear(scopeType, scopeId, academicYear)
                .ifPresent(existing -> {
                    // Ignore itself during update
                    if (currentCurriculumId == null || !existing.getId().equals(currentCurriculumId)) {
                        throw new DuplicateResourceException(
                                "Curriculum",
                                "scopeType + scopeId + academicYear",
                                scopeType + " + " + scopeId + " + " + academicYear
                        );
                    }
                });
    }

    private void checkAcademicYearFormat(String academicYear) {
        if (!academicYear.matches("\\d{4}-\\d{4}")) {
            throw new IllegalArgumentException("Academic year must be in format YYYY-YYYY");
        }
    }

    private CurriculumResponseDTO toDTO(Curriculum curriculum) {

        CurriculumResponseDTO dto = new CurriculumResponseDTO();
        dto.setId(curriculum.getId());
        dto.setScopeType(curriculum.getScopeType());
        dto.setScopeId(curriculum.getScopeId());
        dto.setAcademicYear(curriculum.getAcademicYear());

        List<CurriculumSubjectResponseDTO> subjects = curriculum.getCurriculumSubjects().stream()
                .map(cs -> {
                    CurriculumSubjectResponseDTO csDto = new CurriculumSubjectResponseDTO();
                    csDto.setId(cs.getId());
                    csDto.setSubjectName(cs.getSubject().getSubjectName());

                    List<SyllabusResponseDTO> syllabusList = cs.getSyllabusList().stream()
                            .map(s -> {
                                SyllabusResponseDTO sDto = new SyllabusResponseDTO();
                                sDto.setUnitNumber(s.getUnitNumber());
                                sDto.setUnitTitle(s.getUnitTitle());
                                sDto.setTopics(s.getTopics());
                                sDto.setEstimatedHours(s.getEstimatedHours());
                                return sDto;
                            }).toList();

                    csDto.setSyllabusList(syllabusList);
                    return csDto;
                }).toList();

        dto.setCurriculumSubjects(subjects);
        return dto;

    }

    private void validateSyllabusUniqueness(List<CurriculumSubjectDTO> curriculumSubjects) {
        for (CurriculumSubjectDTO subjectDTO : curriculumSubjects) {
            if (subjectDTO.getSyllabusList() == null || subjectDTO.getSyllabusList().isEmpty()) {
                continue;
            }

            List<SyllabusDTO> syllabusList = subjectDTO.getSyllabusList();

            System.out.println("DEBUG: Validating subject " + subjectDTO.getSubjectId() + " with " + syllabusList.size() + " syllabus entries");

            // Check for duplicates based on unitNumber + unitTitle + topics
            Set<String> uniqueKeys = new HashSet<>();
            List<String> duplicates = new ArrayList<>();

            for (int i = 0; i < syllabusList.size(); i++) {
                SyllabusDTO syllabus = syllabusList.get(i);

                String unitTitle = syllabus.getUnitTitle() != null ? syllabus.getUnitTitle().trim() : "";
                String topics = syllabus.getTopics() != null ? syllabus.getTopics().trim() : "";

                // CREATE KEY WITH ALL THREE FIELDS
                String uniqueKey = String.format("%d||%s||%s",
                        syllabus.getUnitNumber(),
                        unitTitle.toLowerCase(),
                        topics.toLowerCase()
                );

                System.out.println("DEBUG: Syllabus " + i + " - Unit: " + syllabus.getUnitNumber() +
                        ", Title: '" + unitTitle + "', Topics: '" + topics + "', Key: '" + uniqueKey + "'");

                if (!uniqueKeys.add(uniqueKey)) {
                    duplicates.add(String.format("Unit %d: %s", syllabus.getUnitNumber(), unitTitle));
                }
            }

            if (!duplicates.isEmpty()) {
                throw new DuplicateResourceException(
                        "Syllabus",
                        "unitNumber + unitTitle + topics",
                        "Duplicate syllabus entries found in subject " + subjectDTO.getSubjectId() + ". " +
                                "Each syllabus entry must have a unique combination of unit number, title, and topics. " +
                                "Duplicates: " + duplicates
                );
            }
        }
    }


}