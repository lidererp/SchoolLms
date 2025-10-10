package com.schoolmanagementsystem.SchoolManagementSystem.entity;

import com.schoolmanagementsystem.SchoolManagementSystem.enums.StudentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Student {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Student name must not be blank
    @Column(nullable = false)
    @NotBlank(message = "Student name cannot be blank")
    private String studentName;

    // ✅ Recommended: Use @ManyToOne if Standard is an entity
    @ManyToOne
    @JoinColumn(name = "standard_id", nullable = false)
    @NotNull(message = "Standard is required")
    private Standard standard;

    @ManyToOne
    @JoinColumn(name = "section_id", nullable = true)
    private Section section;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Phone Number cannot be blank")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;

    private String placeOfBirth;

    @NotBlank(message = "Student email cannot be blank")
    @Email(message = "Student email must be a valid email address")
    private String studentEmail;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @NotBlank(message = "Pincode is required")
    @Pattern(regexp = "^[0-9]{6}$", message = "Pincode must be exactly 6 digits and contain only numbers")
    private String pinCode;


    private String motherTongue;


    private String studentProfileImage;

//    @DecimalMin(value = "0.0", inclusive = true, message = "Discount amount cannot be negative")
//    private BigDecimal discountAmount;

    // ✅ Admission date - cannot be null
    @Column(nullable = false)
    @NotNull(message = "Admission date is required")
    private LocalDate admissionDate = LocalDate.now();

    // ✅ Date of Birth - cannot be null
    @Column(nullable = false)
    @NotNull(message = "Date of Birth is required")
    @Past(message = "Date of birth must be a past date")
    private LocalDate dob;

    @Column(nullable = false)
    @NotBlank(message = "Gender is required")
    @Pattern(regexp = "Male|Female|Other", message = "Gender must be Male, Female, or Other")
    private String gender;


    @Column(nullable = false)
    @NotBlank(message = "Community name is required")
    private String communityName;

    // ✅ Identification marks - optional but useful
    @Column(nullable = false)
    @NotBlank(message = "Identification marks are required")
    private String identificationMarks;

    private String previousSchoolName;

    @Column(nullable = false)
    @NotBlank(message = "Religion is required")
    private String religion;

    private String bloodGroup;

    @Column(nullable = false)
    @NotBlank(message = "Aadhar card image is required")
    private String aadharCardImage;

    private String communityCertificate;

    private String birthCertificate;

    private String transferCertificate;

    // ✅ Parent details validation
    @Column(nullable = false)
    @NotBlank(message = "Father's name is required")
    private String fatherName;

    @NotBlank(message = "Father Occupation is required")
    private String fatherOccupation;

    @Pattern(regexp = "\\d{10}", message = "Father phone number must be 10 digits")
    @NotBlank(message = "Father Phone Number is required")
    private String fatherPhoneNumber;

    // Father Email (optional, validate format if provided)
    @Email(message = "Father email must be a valid email address")
    private String fatherEmail;

    // Father Education (optional)
    private String fatherEducation;

    // Father Income - required
    @Column(nullable = false)
    @NotNull(message = "Father's income is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Father's income cannot be negative")
    @DecimalMax(value = "999999999.99", message = "Father's income seems too high") // optional max
    @Digits(integer = 9, fraction = 2, message = "Father's income must be valid (up to 9 digits and 2 decimals)")
    private BigDecimal fatherIncome;

    // Mother details
    @NotBlank(message = "Mother name is required")
    private String motherName;

    private String motherOccupation;

    @Pattern(regexp = "\\d{10}", message = "Mother phone number must be 10 digits")
    private String motherPhoneNumber;

    // Mother Email (optional, validate format if provided)
    @Email(message = "Mother email must be a valid email address")
    private String motherEmail;

    // Mother Education (optional)
    private String motherEducation;

    // Mother Income - optional
    @DecimalMin(value = "0.0", inclusive = true, message = "Mother's income cannot be negative")
    @DecimalMax(value = "999999999.99", message = "Mother's income seems too high") // optional max
    @Digits(integer = 9, fraction = 2, message = "Mother's income must be valid (up to 9 digits and 2 decimals)")
    private BigDecimal motherIncome;


    @Column(nullable = false, length = 255)
    @NotBlank(message = "Current address is required")
    @Size(max = 255, message = "Current address must not exceed 255 characters")
    private String currentAddress;

    @Column(nullable = false, unique = true, length = 7) // length = 7 for "ADM2025"
    @NotBlank(message = "Admission Number is required")
    @Pattern(regexp = "^[A-Z]{3}\\d{4}$", message = "Admission Number must be in the format 'ABC1234'")
    private String admissionNumber;

    @Column(nullable = false)
    private boolean usesVan=false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudentStatus status = StudentStatus.ACTIVE;

    private LocalDate graduationDate;

    private LocalDate discontinuationDate;

    // Citizenship / Nationality
    @Column(nullable = false)
    @NotBlank(message = "Citizenship is required")
    private String citizenship;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AcademicRecord> academicRecords = new ArrayList<>();

    @Column(nullable = false)
    @NotNull(message = "School joining date is required")
    private LocalDate schoolJoiningDate;


}
