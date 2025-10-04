package com.schoolmanagementsystem.SchoolManagementSystem.service;

import com.schoolmanagementsystem.SchoolManagementSystem.GlobalExceptionHandler.DuplicateResourceException;
import com.schoolmanagementsystem.SchoolManagementSystem.GlobalExceptionHandler.ResourceNotFoundException;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.Subject;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SubjectService {


    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public Subject createSubject(Subject subject) {
        // Check if subject name already exists
        if (subjectRepository.findBySubjectName(subject.getSubjectName()).isPresent()) {
            throw new DuplicateResourceException("Subject", "subjectName", subject.getSubjectName());
        }

        // Check if subject code already exists
        if (subject.getSubjectCode() != null &&
                subjectRepository.findBySubjectCode(subject.getSubjectCode()).isPresent()) {
            throw new DuplicateResourceException("Subject", "subjectCode", subject.getSubjectCode());
        }

        return subjectRepository.save(subject);
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public Subject getSubjectById(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject", "id", id));
    }

    public Subject updateSubject(Long id, Subject subjectDetails) {
        Subject subject = getSubjectById(id);
        subject.setSubjectName(subjectDetails.getSubjectName());
        subject.setSubjectCode(subjectDetails.getSubjectCode());
        subject.setDescription(subjectDetails.getDescription());
        return subjectRepository.save(subject);
    }

    public void deleteSubject(Long id) {
        Subject subject = getSubjectById(id);
        subjectRepository.delete(subject);
    }

    public List<Subject> searchSubjectsByName(String name) {
        return subjectRepository.findBySubjectNameContainingIgnoreCase(name);
    }


}