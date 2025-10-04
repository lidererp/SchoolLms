package com.schoolmanagementsystem.SchoolManagementSystem.service;

import com.schoolmanagementsystem.SchoolManagementSystem.GlobalExceptionHandler.DuplicateResourceException;
import com.schoolmanagementsystem.SchoolManagementSystem.GlobalExceptionHandler.ResourceNotFoundException;
import com.schoolmanagementsystem.SchoolManagementSystem.dtos.ClassDTO;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.Section;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.Standard;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.SectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SectionService {


    private final SectionRepository sectionRepository;
    private final StandardService standardService;

    public SectionService(SectionRepository sectionRepository, StandardService standardService) {
        this.sectionRepository = sectionRepository;
        this.standardService = standardService;
    }

    public Section createSection(Long standardId, Section section) {
        Standard standard = standardService.getStandardById(standardId);

        if (sectionRepository.existsByStandardIdAndSectionName(standardId, section.getSectionName())) {
            throw new DuplicateResourceException("Section", "sectionName", section.getSectionName());

        }

        section.setStandard(standard);
        return sectionRepository.save(section);
    }

    public List<Section> getSectionsByStandard(Long standardId) {
        return sectionRepository.findByStandardId(standardId);
    }

    public Section getSectionById(Long id) {
        return sectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Section", "id", id));
    }

    public void deleteSection(Long id) {
        Section section = getSectionById(id);
        sectionRepository.delete(section);
    }

    public Section updateSection(Long standardId, Long sectionId, Section sectionDetails) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found with id " ,"sectionId", sectionId));

        if (!section.getStandard().getId().equals(standardId)) {
            throw new IllegalArgumentException("Section " + sectionId + " does not belong to Standard " + standardId);
        }

        section.setSectionName(sectionDetails.getSectionName());
        return sectionRepository.save(section);
    }

    public List<ClassDTO> getSectionDisplayNames() {
        return sectionRepository.findAll()
                .stream()
                .map(s -> new ClassDTO(s.getId(), s.getStandard().getClassName() + "-" + s.getSectionName()))
                .collect(Collectors.toList());
    }


}