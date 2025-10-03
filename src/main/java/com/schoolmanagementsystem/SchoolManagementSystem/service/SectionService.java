package com.schoolmanagementsystem.SchoolManagementSystem.service;

import com.schoolmanagementsystem.SchoolManagementSystem.entity.Section;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.Standard;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.SectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
            throw new RuntimeException("Section already exists in this standard");
        }

        section.setStandard(standard);
        return sectionRepository.save(section);
    }

    public List<Section> getSectionsByStandard(Long standardId) {
        return sectionRepository.findByStandardId(standardId);
    }

    public Section getSectionById(Long id) {
        return sectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Section not found with id: " + id));
    }

    public void deleteSection(Long id) {
        Section section = getSectionById(id);
        sectionRepository.delete(section);
    }


}