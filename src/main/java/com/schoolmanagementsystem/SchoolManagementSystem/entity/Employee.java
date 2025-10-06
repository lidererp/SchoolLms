package com.schoolmanagementsystem.SchoolManagementSystem.entity;

import com.schoolmanagementsystem.SchoolManagementSystem.enums.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employees",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "nationalId")
        })
public class Employee {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Employee name is required")
    @Size(max = 100, message = "Employee name must be less than 100 characters")
    private String employeeName;

    @NotBlank(message = "Mobile number is required")
    @Size(max = 15, message = "Mobile number must be less than 15 digits")
    private String mobileNumber;

    @NotNull(message = "Employee role is required")
    @Enumerated(EnumType.STRING)
    private EmployeeRole employeeRole;

    @NotNull(message = "Monthly salary is required")
    @Positive(message = "Monthly salary must be greater than 0")
    private Double monthlySalary;

    @Size(max = 255, message = "Picture URL must be less than 255 characters")
    private String pictureUrl;

    @NotNull(message = "Date of joining is required")
    private LocalDate dateOfJoining;

    @NotBlank(message = "Relationship name is required")
    @Column(name = "relationship_name", length = 100, nullable = false)
    private String relationshipName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RelationshipType relationshipType = RelationshipType.OTHER;

    @NotNull(message = "Gender is required")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Size(max = 200, message = "Experience description too long")
    private String experience;

    @NotBlank(message = "National ID is required")
    @Size(max = 20)
    private String nationalId;

    @NotNull(message = "Religion is required")
    @Enumerated(EnumType.STRING)
    private Religion religion;


    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Size(max = 100)
    @Column(unique = true)
    private String email;

    @Size(max = 200)
    private String education;

    @NotNull(message = "Blood group is required")
    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;


    @Size(min = 10, max = 255, message = "Home address must be between 10 and 255 characters")
    private String homeAddress;

    @Size(max = 100)
    private String subjectToTeach;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;


}
