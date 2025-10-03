package com.schoolmanagementsystem.SchoolManagementSystem.service;

import com.schoolmanagementsystem.SchoolManagementSystem.entity.Standard;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.StandardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StandardService {


    private final StandardRepository standardRepository;

    public StandardService(StandardRepository standardRepository) {
        this.standardRepository = standardRepository;
    }

    public Standard createStandard(Standard standard) {
        if (standardRepository.existsByClassName(standard.getClassName())) {
            throw new RuntimeException("Standard already exists: " + standard.getClassName());
        }
        return standardRepository.save(standard);
    }

    public List<Standard> getAllStandards() {
        return standardRepository.findAll();
    }

    public Standard getStandardById(Long id) {
        return standardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Standard not found with id: " + id));
    }

    public Standard updateStandard(Long id, Standard standardDetails) {
        Standard standard = getStandardById(id);

        if (standardDetails.getClassName() != null && !standardDetails.getClassName().isBlank()) {
            standard.setClassName(standardDetails.getClassName());
        }

        return standardRepository.save(standard);
    }

    public void deleteStandard(Long id) {
        Standard standard = getStandardById(id);
        standardRepository.delete(standard);
    }


}
