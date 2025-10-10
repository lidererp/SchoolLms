package com.schoolmanagementsystem.SchoolManagementSystem.service;

import com.schoolmanagementsystem.SchoolManagementSystem.entity.*;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.BreakSlotRepository;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.SubjectAllocationRepository;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.TimetableConfigRepository;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.TimetableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class TimetableGenerationService {

    private final TimetableRepository timetableRepository;
    private final SubjectAllocationRepository subjectAllocationRepository;
    private final TimetableConfigRepository timetableConfigRepository;
    private final BreakSlotRepository breakSlotRepository;


    public TimetableGenerationService(TimetableRepository timetableRepository, SubjectAllocationRepository subjectAllocationRepository, TimetableConfigRepository timetableConfigRepository, BreakSlotRepository breakSlotRepository) {
        this.timetableRepository = timetableRepository;

        this.subjectAllocationRepository = subjectAllocationRepository;
        this.timetableConfigRepository = timetableConfigRepository;
        this.breakSlotRepository = breakSlotRepository;
    }

    @Transactional
    public Timetable generateTimetable(Long configId, Map<String, List<Long>> subjectAllocationMapping) {
        // 1️⃣ Fetch timetable config
        TimetableConfig config = timetableConfigRepository.findById(configId)
                .orElseThrow(() -> new RuntimeException("TimetableConfig not found with id " + configId));

        // 2️⃣ Fetch all breaks for this timetable config
        List<BreakSlot> breaks = breakSlotRepository.findByTimetableConfig_Id(configId);

        validateBreaksAndPeriods(config, breaks);

        // 3️⃣ Create timetable entity
        Timetable timetable = new Timetable();
        timetable.setTimetableConfig(config);

        List<TimetableEntry> entries = new ArrayList<>();

        // 4️⃣ Compute total break time in minutes
        long totalBreakMinutes = breaks.stream()
                .mapToLong(b -> java.time.Duration.between(b.getStartTime(), b.getEndTime()).toMinutes())
                .sum();

        // 5️⃣ Compute total available minutes for periods
        long totalMinutes = java.time.Duration.between(config.getStartTime(), config.getEndTime()).toMinutes();
        long periodMinutes = (totalMinutes - totalBreakMinutes) / config.getTotalPeriods();

        // 6️⃣ Loop through each day mapping
        for (Map.Entry<String, List<Long>> dayEntry : subjectAllocationMapping.entrySet()) {
            String day = dayEntry.getKey();
            List<Long> allocationIds = dayEntry.getValue();

            LocalTime periodStart = config.getStartTime();

            for (int i = 0; i < allocationIds.size(); i++) {
                final LocalTime periodStartForLambda = periodStart; // fix for lambda

                // Check if current period falls inside a break
                BreakSlot currentBreak = breaks.stream()
                        .filter(b -> !b.getStartTime().isAfter(periodStartForLambda) &&
                                periodStartForLambda.isBefore(b.getEndTime()))
                        .findFirst()
                        .orElse(null);

                if (currentBreak != null) {
                    periodStart = currentBreak.getEndTime(); // skip break time
                }

                Long subjectAllocationId = allocationIds.get(i);
                SubjectAllocation subjectAllocation = subjectAllocationRepository.findById(subjectAllocationId)
                        .orElseThrow(() -> new RuntimeException("SubjectAllocation not found: " + subjectAllocationId));

                TimetableEntry entry = new TimetableEntry();
                entry.setTimetable(timetable);
                entry.setDay(day);
                entry.setPeriodNumber(i + 1);
                entry.setStartTime(periodStart);
                entry.setEndTime(periodStart.plusMinutes(periodMinutes));
                entry.setIsBreak(false);
                entry.setSubjectAllocation(subjectAllocation);

                entries.add(entry);

                periodStart = periodStart.plusMinutes(periodMinutes);
            }
        }

        timetable.setEntries(entries);
        return timetableRepository.save(timetable);
    }

    @Transactional
    public Timetable editTimetable(Long timetableId, Long configId, Map<String, List<Long>> subjectAllocationMapping) {
        // 1️⃣ Fetch existing timetable
        Timetable timetable = timetableRepository.findById(timetableId)
                .orElseThrow(() -> new RuntimeException("Timetable not found with id " + timetableId));

        // 2️⃣ Fetch fresh timetable config
        TimetableConfig config = timetableConfigRepository.findById(configId)
                .orElseThrow(() -> new RuntimeException("TimetableConfig not found with id " + configId));

        // 3️⃣ Fetch breaks for this timetable config
        List<BreakSlot> breaks = breakSlotRepository.findByTimetableConfig_Id(config.getId());

        validateBreaksAndPeriods(config, breaks);

        List<TimetableEntry> entries = new ArrayList<>();

        // 4️⃣ Compute total break time in minutes
        long totalBreakMinutes = breaks.stream()
                .mapToLong(b -> java.time.Duration.between(b.getStartTime(), b.getEndTime()).toMinutes())
                .sum();

        // 5️⃣ Compute available minutes for periods
        long totalMinutes = java.time.Duration.between(config.getStartTime(), config.getEndTime()).toMinutes();
        long periodMinutes = (totalMinutes - totalBreakMinutes) / config.getTotalPeriods();

        // 6️⃣ Loop through day mapping
        for (Map.Entry<String, List<Long>> dayEntry : subjectAllocationMapping.entrySet()) {
            String day = dayEntry.getKey();
            List<Long> allocationIds = dayEntry.getValue();

            LocalTime periodStart = config.getStartTime();

            for (int i = 0; i < allocationIds.size(); i++) {
                final LocalTime periodStartForLambda = periodStart;

                // Skip breaks
                BreakSlot currentBreak = breaks.stream()
                        .filter(b -> !b.getStartTime().isAfter(periodStartForLambda) &&
                                periodStartForLambda.isBefore(b.getEndTime()))
                        .findFirst()
                        .orElse(null);

                if (currentBreak != null) {
                    periodStart = currentBreak.getEndTime();
                }

                Long subjectAllocationId = allocationIds.get(i);
                SubjectAllocation subjectAllocation = subjectAllocationRepository.findById(subjectAllocationId)
                        .orElseThrow(() -> new RuntimeException("SubjectAllocation not found: " + subjectAllocationId));

                TimetableEntry entry = new TimetableEntry();
                entry.setTimetable(timetable);
                entry.setDay(day);
                entry.setPeriodNumber(i + 1);
                entry.setStartTime(periodStart);
                entry.setEndTime(periodStart.plusMinutes(periodMinutes));
                entry.setIsBreak(false);
                entry.setSubjectAllocation(subjectAllocation);

                entries.add(entry);

                periodStart = periodStart.plusMinutes(periodMinutes);
            }
        }

        // 7️⃣ Replace existing entries
        timetable.getEntries().clear();
        timetable.getEntries().addAll(entries);

        // 8️⃣ Update timetable's config
        timetable.setTimetableConfig(config);

        return timetableRepository.save(timetable);
    }



    public List<Timetable> getAllTimetables() {
        return timetableRepository.findAll();
    }

    public Timetable getTimetableById(Long id) {
        return timetableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Timetable not found with id " + id));
    }

    private void validateBreaksAndPeriods(TimetableConfig config, List<BreakSlot> breaks) {
        if (breaks == null || breaks.isEmpty()) return;

        // ➡ Sort breaks by start time
        breaks.sort((b1, b2) -> b1.getStartTime().compareTo(b2.getStartTime()));

        // 1️⃣ Check for overlaps between breaks
        for (int i = 0; i < breaks.size() - 1; i++) {
            BreakSlot current = breaks.get(i);
            BreakSlot next = breaks.get(i + 1);

            if (!current.getEndTime().isBefore(next.getStartTime())) {
                throw new RuntimeException(String.format(
                        "Break overlap detected between '%s' (%s - %s) and '%s' (%s - %s)",
                        current.getBreakName(), current.getStartTime(), current.getEndTime(),
                        next.getBreakName(), next.getStartTime(), next.getEndTime()
                ));
            }
        }

        // 2️⃣ Check for overlaps between breaks and periods
        long totalBreakMinutes = breaks.stream()
                .mapToLong(b -> java.time.Duration.between(b.getStartTime(), b.getEndTime()).toMinutes())
                .sum();

        long totalMinutes = java.time.Duration.between(config.getStartTime(), config.getEndTime()).toMinutes();
        long periodMinutes = (totalMinutes - totalBreakMinutes) / config.getTotalPeriods();

        LocalTime periodStart = config.getStartTime();

        for (int period = 1; period <= config.getTotalPeriods(); period++) {
            LocalTime periodEnd = periodStart.plusMinutes(periodMinutes);

            for (BreakSlot breakSlot : breaks) {
                boolean overlap = breakSlot.getStartTime().isBefore(periodEnd) &&
                        breakSlot.getEndTime().isAfter(periodStart);

                if (overlap) {
                    throw new RuntimeException(
                            String.format("Break '%s' (%s - %s) overlaps with Period %d (%s - %s)",
                                    breakSlot.getBreakName(),
                                    breakSlot.getStartTime(),
                                    breakSlot.getEndTime(),
                                    period,
                                    periodStart,
                                    periodEnd)
                    );
                }
            }

            periodStart = periodEnd;
        }
    }



}
