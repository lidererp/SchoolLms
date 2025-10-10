package com.schoolmanagementsystem.SchoolManagementSystem.dtos;

import com.schoolmanagementsystem.SchoolManagementSystem.enums.StudentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class  StudentDTO {


    private Long id;

    private String studentName;

    private Long standardId;

    private String standardName;

    private String studentProfile;

    private LocalDate admissionDate;

    private LocalDate dob;

    private String gender;

    private String castName;

    private String identificationMarks;

    private String previousSchoolName;

    private String religion;

    private String bloodGroup;

    private String aadharId;

    private String communityCertificate;

    private String birthCertificate;

    private String transferCertificate;

    private String fatherName;

    private String fatherOccupation;

    private String fatherPhoneNumber;

    private BigDecimal fatherIncome;

    private String motherName;

    private String motherOccupation;

    private BigDecimal motherIncome;

    private String motherPhoneNumber;

    private String currentAddress;

    private String admissionNumber;

    private StudentStatus status;

    private boolean useVan;

    private String studentEmail;

    private String fatherEmail;

    private String motherEmail;

    private String pincode;

    private String citizenship;

    private String motherEducation;

    private String fatherEducation;

    private String phoneNumber;

    private LocalDate schoolJoiningDate;

    private Long sectionId;

    private String sectionName;

    private String passWord;

}
