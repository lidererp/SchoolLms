package com.schoolmanagementsystem.SchoolManagementSystem.service;

import com.schoolmanagementsystem.SchoolManagementSystem.entity.BreakSlot;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.BreakSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BreakSlotService {


    private final BreakSlotRepository breakSlotRepository;

    public BreakSlotService(BreakSlotRepository breakSlotRepository) {
        this.breakSlotRepository = breakSlotRepository;
    }

    @Transactional
    public BreakSlot createBreakSlot(BreakSlot breakSlot) {
        return breakSlotRepository.save(breakSlot);
    }

    public List<BreakSlot> getAllBreakSlots() {
        return breakSlotRepository.findAll();
    }

    public BreakSlot getBreakSlot(Long id) {
        return breakSlotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BreakSlot not found"));
    }

    @Transactional
    public BreakSlot updateBreakSlot(Long id, BreakSlot breakSlot) {
        BreakSlot existing = getBreakSlot(id);
        existing.setBreakName(breakSlot.getBreakName());
        existing.setStartTime(breakSlot.getStartTime());
        existing.setEndTime(breakSlot.getEndTime());
        existing.setDayOfWeek(breakSlot.getDayOfWeek());
        return breakSlotRepository.save(existing);
    }

    @Transactional
    public void deleteBreakSlot(Long id) {
        breakSlotRepository.deleteById(id);
    }


}
