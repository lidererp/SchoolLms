package com.schoolmanagementsystem.SchoolManagementSystem.service;

import com.schoolmanagementsystem.SchoolManagementSystem.entity.Scholarship;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.ScholarshipRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ScholarshipService {


    private final ScholarshipRepository repository;

    public ScholarshipService(ScholarshipRepository repository) {
        this.repository = repository;
    }

    public Scholarship save(Scholarship s) {
        s.setUpdatedDate(LocalDateTime.now());
        return repository.save(s);
    }

    @Transactional(readOnly = true)
    public List<Scholarship> getFirstPage(int size) {
        return repository.findAll(PageRequest.of(0, size, Sort.by("id").ascending())).getContent();
    }

    @Transactional(readOnly = true)
    public List<Scholarship> getScholarshipsAfterCursor(Long cursor, int size) {
        return repository.findByIdGreaterThanOrderByIdAsc(cursor, PageRequest.of(0, size));
    }

    @Transactional(readOnly = true)
    public Scholarship getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Scholarship updateScholarship(Long id, Scholarship s) {
        Scholarship existing = repository.findById(id).orElse(null);
        if (existing == null) return null;

        existing.setName(s.getName());
        existing.setType(s.getType());
        existing.setDescription(s.getDescription());
        existing.setAmount(s.getAmount());
        existing.setApplicableGrade(s.getApplicableGrade());
        existing.setMinPercentage(s.getMinPercentage());
        existing.setMaxFamilyIncome(s.getMaxFamilyIncome());
        existing.setActive(s.getActive());
        existing.setStartDate(s.getStartDate());
        existing.setEndDate(s.getEndDate());
        existing.setUpdatedDate(LocalDateTime.now());

        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }


}

