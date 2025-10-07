package com.schoolmanagementsystem.SchoolManagementSystem.service;

import com.schoolmanagementsystem.SchoolManagementSystem.GlobalExceptionHandler.DuplicateResourceException;
import com.schoolmanagementsystem.SchoolManagementSystem.GlobalExceptionHandler.ResourceNotFoundException;
import com.schoolmanagementsystem.SchoolManagementSystem.dtos.CurriculumRequestDTO;
import com.schoolmanagementsystem.SchoolManagementSystem.dtos.CurriculumResponseDTO;
import com.schoolmanagementsystem.SchoolManagementSystem.dtos.CurriculumSubjectResponseDTO;
import com.schoolmanagementsystem.SchoolManagementSystem.dtos.SyllabusResponseDTO;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.Curriculum;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.CurriculumSubject;
import com.schoolmanagementsystem.SchoolManagementSystem.enums.CurriculumScope;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.CurriculumRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CurriculumService {


    private final CurriculumRepository curriculumRepository;
    private final CurriculumSubjectService curriculumSubjectService;

    public CurriculumService(CurriculumRepository curriculumRepository, CurriculumSubjectService curriculumSubjectService) {
        this.curriculumRepository = curriculumRepository;
        this.curriculumSubjectService = curriculumSubjectService;
    }

    @Transactional
    public String createCurriculum(CurriculumRequestDTO dto) {

        // Prevent duplicate curriculum for same scope/year
        checkDuplicateCurriculum(dto.getScopeType(), dto.getScopeId(), dto.getAcademicYear(), null);
        checkAcademicYearFormat(dto.getAcademicYear());

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

        curriculum.setScopeType(dto.getScopeType());
        curriculum.setScopeId(dto.getScopeId());
        curriculum.setAcademicYear(dto.getAcademicYear());

        curriculum.getCurriculumSubjects().clear();
        dto.getCurriculumSubjects().forEach(subDTO -> {
            CurriculumSubject subject = curriculumSubjectService.buildFromDTO(subDTO, curriculum);
            curriculum.getCurriculumSubjects().add(subject);
        });

        Curriculum saved = curriculumRepository.save(curriculum);
        return toDTO(saved);

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


}