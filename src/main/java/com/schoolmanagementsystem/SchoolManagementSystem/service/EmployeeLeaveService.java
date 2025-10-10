package com.schoolmanagementsystem.SchoolManagementSystem.service;

import com.schoolmanagementsystem.SchoolManagementSystem.entity.EmployeeLeave;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.EmployeeLeaveRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeLeaveService {


    private final EmployeeLeaveRepository repository;

    public EmployeeLeaveService(EmployeeLeaveRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public EmployeeLeave createLeave(EmployeeLeave leave) {
        Optional<EmployeeLeave> existing = repository.findByEmployeeIdAndDate(leave.getEmployeeId(), leave.getDate());
        if (existing.isPresent()) {
            throw new RuntimeException("Leave already exists for this employee on this date.");
        }
        return repository.save(leave);
    }

    public EmployeeLeave getLeaveById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave record not found with id: " + id));
    }


    public List<EmployeeLeave> getAllLeaves() {
        return repository.findAll();
    }

    @Transactional
    public EmployeeLeave updateLeave(Long id, EmployeeLeave leave) {
        EmployeeLeave existingLeave = getLeaveById(id);
        existingLeave.setDate(leave.getDate());
        existingLeave.setOnLeave(leave.getOnLeave());
        existingLeave.setLoginDateTime(leave.getLoginDateTime());
        existingLeave.setLogoutDateTime(leave.getLogoutDateTime());
        return repository.save(existingLeave);
    }

    @Transactional
    public void deleteLeave(Long id) {
        EmployeeLeave existingLeave = getLeaveById(id);
        repository.delete(existingLeave);
    }

    @Transactional
    public List<EmployeeLeave> getLeavesWithPagination(Long cursor, int size) {
        Pageable pageable = PageRequest.of(0, size);
        if (cursor == null) {
            return repository.findAll(pageable).getContent();
        } else {
            return repository.findByIdAfter(cursor, pageable);
        }
    }


}

