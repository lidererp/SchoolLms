package com.schoolmanagementsystem.SchoolManagementSystem.utility;

import java.time.LocalDate;

public class AcademicYearGenerator {


    public static String generateAcademicYear(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }

        int startYear;
        int sessionStartMonth = 6; // June is typical start month in India

        if (date.getMonthValue() < sessionStartMonth) {
            startYear = date.getYear() - 1;
        } else {
            startYear = date.getYear();
        }

        int endYear = startYear + 1;
        return startYear + "-" + endYear;
    }

    // Default method for current year
    public static String generateAcademicYear() {
        return generateAcademicYear(LocalDate.now());
    }

    // Generate next academic year from a given academic year string
    public static String generateNextAcademicYear(String previousAcademicYear) {
        if (!isValidFormat(previousAcademicYear)) {
            throw new IllegalArgumentException("Invalid academic year format: " + previousAcademicYear);
        }
        String[] years = previousAcademicYear.split("-");
        int startYear = Integer.parseInt(years[0]) + 1;
        int endYear = Integer.parseInt(years[1]) + 1;
        return startYear + "-" + endYear;
    }

    // Check valid format
    public static boolean isValidFormat(String academicYear) {
        return academicYear != null && academicYear.matches("\\d{4}-\\d{4}");
    }
}
