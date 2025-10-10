package com.schoolmanagementsystem.SchoolManagementSystem.service;

import com.schoolmanagementsystem.SchoolManagementSystem.entity.TimetableConfig;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.TimetableConfigRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TimetableConfigService {


    private final TimetableConfigRepository timetableConfigRepository;

    public TimetableConfigService(TimetableConfigRepository timetableConfigRepository) {
        this.timetableConfigRepository = timetableConfigRepository;
    }

    @Transactional
    public TimetableConfig createTimetableConfig(TimetableConfig timetableConfig) {

        return timetableConfigRepository.save(timetableConfig);
    }

    public List<TimetableConfig> getAllTimetableConfigs() {
        return timetableConfigRepository.findAll();
    }

    public TimetableConfig getTimetableConfig(Long id) {
        return timetableConfigRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TimetableConfig not found"));
    }

    @Transactional
    public TimetableConfig updateTimetableConfig(Long id, TimetableConfig updatedConfig) {
        TimetableConfig existingConfig = getTimetableConfig(id);

        existingConfig.setScope(updatedConfig.getScope());
        existingConfig.setSection(updatedConfig.getSection());
        existingConfig.setStandard(updatedConfig.getStandard());
        existingConfig.setAcademicYear(updatedConfig.getAcademicYear());
        existingConfig.setTotalPeriods(updatedConfig.getTotalPeriods());
        existingConfig.setStartTime(updatedConfig.getStartTime());
        existingConfig.setEndTime(updatedConfig.getEndTime());
        existingConfig.setBreaks(updatedConfig.getBreaks());

        return timetableConfigRepository.save(existingConfig);
    }

    public void deleteTimetableConfig(Long id) {
        timetableConfigRepository.deleteById(id);
    }


}

