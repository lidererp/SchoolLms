package com.schoolmanagementsystem.SchoolManagementSystem.service;

import com.schoolmanagementsystem.SchoolManagementSystem.GlobalExceptionHandler.ResourceNotFoundException;
import com.schoolmanagementsystem.SchoolManagementSystem.dtos.AssignSectionRequestDTO;
import com.schoolmanagementsystem.SchoolManagementSystem.dtos.StudentDTO;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.*;
import com.schoolmanagementsystem.SchoolManagementSystem.enums.AcademicStatus;
import com.schoolmanagementsystem.SchoolManagementSystem.enums.StudentStatus;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.*;
import com.schoolmanagementsystem.SchoolManagementSystem.utility.AcademicYearGenerator;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {


    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final StudentRepository studentRepository;
    private final StandardRepository standardRepository;
    private final AcademicRecordRepository academicRecordRepository;
    private final UserRepository userRepository;
    private final SectionRepository sectionRepository;
    private final EmailService emailService;

    public StudentService(StudentRepository studentRepository, StandardRepository standardRepository, AcademicRecordRepository academicRecordRepository, UserRepository userRepository, SectionRepository sectionRepository, EmailService emailService) {
        this.studentRepository = studentRepository;
        this.standardRepository = standardRepository;
        this.academicRecordRepository = academicRecordRepository;
        this.userRepository = userRepository;
        this.sectionRepository = sectionRepository;
        this.emailService = emailService;
    }

    @Transactional
    public Student createStudent(Student student) throws MessagingException {

        // 1. Check if email already exists
        if (studentRepository.existsByStudentEmail(student.getStudentEmail())) {
            throw new IllegalArgumentException("Student with email '" + student.getStudentEmail() + "' already exists");
        }

        // 2. Ensure standard exists
        Standard standard = standardRepository.findById(student.getStandard().getId())
                .orElseThrow(() -> new IllegalArgumentException("Standard not found with ID: " + student.getStandard().getId()));
        student.setStandard(standard);

        // 3. Generate random password
        String rawPassword = generateRandomPassword();
        student.setPassword(rawPassword); // store raw password for email

        // 4. Save student
        Student savedStudent = studentRepository.save(student);

        // 5. Create academic record
        AcademicRecord record = new AcademicRecord();
        record.setStudent(savedStudent);
        record.setSection(savedStudent.getSection());
        record.setStatus(AcademicStatus.ACTIVE);
        academicRecordRepository.save(record); // academicYear auto-generated

        // 6. Create user for login
        User user = new User();
        user.setName(savedStudent.getStudentName());
        user.setEmail(savedStudent.getStudentEmail());
        user.setPassword(passwordEncoder.encode(rawPassword)); // encode for login
        user.setUserCode(savedStudent.getAdmissionNumber());
        user.setRole("STUDENT");
        userRepository.save(user);

        // 7. Validate dates
        if (savedStudent.getSchoolJoiningDate() != null && savedStudent.getAdmissionDate() != null) {
            if (savedStudent.getSchoolJoiningDate().isBefore(savedStudent.getAdmissionDate())) {
                throw new IllegalArgumentException("School joining date cannot be before admission date");
            }
        }

        // 8. Send email with credentials
        emailService.sendStudentCredentials(
                savedStudent.getStudentEmail(),
                savedStudent.getStudentName(),
                rawPassword
        );

        return savedStudent;
    }



    private String generateRandomPassword() {
        // Random 6-letter word (uppercase)
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int index = (int) (Math.random() * letters.length());
            word.append(letters.charAt(index));
        }

        // Random 4-digit number
        int number = 1000 + (int) (Math.random() * 9000);
        return word.toString() + number;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }


    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
    }
    @Transactional(readOnly = true)
    public List<StudentDTO> getStudentsWithCursorDescDTO(Long cursor, int size) {
        Pageable pageable = PageRequest.of(0, size);
        if (cursor == null) cursor = Long.MAX_VALUE;

        List<Student> students = studentRepository.findWithCursorDesc(cursor, pageable);

        return students.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    @Transactional
    public Student updateStudent(Long id, Student updatedStudent) {

        // Fetch existing student
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));

        // Optional: Check if the updated email or admission number already exists for another student
        if (!existing.getStudentEmail().equals(updatedStudent.getStudentEmail())
                && studentRepository.existsByStudentEmail(updatedStudent.getStudentEmail())) {
            throw new IllegalArgumentException("Email '" + updatedStudent.getStudentEmail() + "' is already in use");
        }

        if (!existing.getAdmissionNumber().equals(updatedStudent.getAdmissionNumber())
                && studentRepository.existsByAdmissionNumber(updatedStudent.getAdmissionNumber())) {
            throw new IllegalArgumentException("Admission Number '" + updatedStudent.getAdmissionNumber() + "' is already in use");
        }
        if (updatedStudent.getSchoolJoiningDate() != null && updatedStudent.getAdmissionDate() != null) {
            if (updatedStudent.getSchoolJoiningDate().isBefore(updatedStudent.getAdmissionDate())) {
                throw new IllegalArgumentException("School joining date cannot be before admission date");
            }
        }
        // Update fields
        existing.setStudentName(updatedStudent.getStudentName());
        existing.setStandard(updatedStudent.getStandard());
        existing.setStudentProfileImage(updatedStudent.getStudentProfileImage());
//        existing.setDiscountAmount(updatedStudent.getDiscountAmount());
        existing.setAdmissionDate(updatedStudent.getAdmissionDate());
        existing.setDob(updatedStudent.getDob());
        existing.setGender(updatedStudent.getGender());
        existing.setCommunityName(updatedStudent.getCommunityName());
        existing.setIdentificationMarks(updatedStudent.getIdentificationMarks());
        existing.setPreviousSchoolName(updatedStudent.getPreviousSchoolName());
        existing.setReligion(updatedStudent.getReligion());
        existing.setBloodGroup(updatedStudent.getBloodGroup());
        existing.setAadharCardImage(updatedStudent.getAadharCardImage());
        existing.setCommunityCertificate(updatedStudent.getCommunityCertificate());
        existing.setBirthCertificate(updatedStudent.getBirthCertificate());
        existing.setTransferCertificate(updatedStudent.getTransferCertificate());
        existing.setFatherName(updatedStudent.getFatherName());
        existing.setFatherOccupation(updatedStudent.getFatherOccupation());
        existing.setFatherPhoneNumber(updatedStudent.getFatherPhoneNumber());
        existing.setFatherIncome(updatedStudent.getFatherIncome());
        existing.setMotherName(updatedStudent.getMotherName());
        existing.setMotherOccupation(updatedStudent.getMotherOccupation());
        existing.setMotherIncome(updatedStudent.getMotherIncome());
        existing.setMotherPhoneNumber(updatedStudent.getMotherPhoneNumber());
        existing.setCurrentAddress(updatedStudent.getCurrentAddress());
        existing.setAdmissionNumber(updatedStudent.getAdmissionNumber());
        existing.setStatus(updatedStudent.getStatus());
        existing.setGraduationDate(updatedStudent.getGraduationDate());
        existing.setDiscontinuationDate(updatedStudent.getDiscontinuationDate());
        existing.setCitizenship(updatedStudent.getCitizenship());
        existing.setPassword(passwordEncoder.encode(updatedStudent.getPassword()));

        return studentRepository.save(existing);
    }


    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));

        // Cascade and orphanRemoval will automatically delete academic records
        studentRepository.delete(student);
    }

    @Transactional
    public Student updateStatus(Long studentId, StudentStatus newStatus) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setStatus(newStatus);

        switch (newStatus) {
            case ALUMNI:
                // ✅ Completed course – set graduation date
                student.setGraduationDate(LocalDate.now());
                student.setDiscontinuationDate(null); // clear if previously set
                break;

            case DISCONTINUED:
                // ✅ Permanently left before completion – set discontinuation date
                student.setDiscontinuationDate(LocalDate.now());
                student.setGraduationDate(null); // do not set graduation date
                break;

            case INACTIVE:
                // ✅ Temporarily inactive – no dates
                student.setGraduationDate(null);
                student.setDiscontinuationDate(null);
                break;

            default:
                // ✅ PENDING or ACTIVE – clear both dates
                student.setGraduationDate(null);
                student.setDiscontinuationDate(null);
        }

        return studentRepository.save(student);
    }



    @Transactional
    public int markStudentsAlumniByStandard(Long standardId) {
        List<Student> students = studentRepository.findByStandardId(standardId);

        for (Student student : students) {
            student.setStatus(StudentStatus.ALUMNI);
            student.setGraduationDate(LocalDate.now()); // optional
        }

        // ✅ Save all updated students
        studentRepository.saveAll(students);

        return students.size();
    }

    @Transactional(readOnly = true)
    public List<StudentDTO> getStudentsByStandardAndAcademicYear(Long standardId, String academicYear, Long sectionId) {
        List<Student> students;

        if (sectionId != null) {
            students = studentRepository.findByStandardIdAndAcademicYearAndSectionId(standardId, academicYear, sectionId);
        } else {
            students = studentRepository.findByStandardIdAndAcademicYear(standardId, academicYear);
        }

        return students.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    public  StudentDTO toDTO(Student s) {
        if (s == null) return null;

        return new StudentDTO(
                s.getId(),
                s.getStudentName(),
                s.getStandard() != null ? s.getStandard().getId() : null,
                s.getStandard() != null ? s.getStandard().getClassName() : null,
                s.getStudentProfileImage(),
                s.getAdmissionDate(),
                s.getDob(),
                s.getGender(),
                s.getCommunityName(),
                s.getIdentificationMarks(),
                s.getPreviousSchoolName(),
                s.getReligion(),
                s.getBloodGroup(),
                s.getAadharCardImage(),
                s.getCommunityCertificate(),
                s.getBirthCertificate(),
                s.getTransferCertificate(),
                s.getFatherName(),
                s.getFatherOccupation(),
                s.getFatherPhoneNumber(),
                s.getFatherIncome(),
                s.getMotherName(),
                s.getMotherOccupation(),
                s.getMotherIncome(),
                s.getMotherPhoneNumber(),
                s.getCurrentAddress(),
                s.getAdmissionNumber(),
                s.getStatus(),
                s.isUsesVan(),
                s.getStudentEmail(),
                s.getFatherEmail(),
                s.getMotherEmail(),
                s.getPinCode(),
                s.getCitizenship(),
                s.getMotherEducation(),
                s.getFatherEducation(),
                s.getPhoneNumber(),
                s.getSchoolJoiningDate(),
                s.getSection() != null ? s.getSection().getId() : null,       // sectionId
                s.getSection() != null ? s.getSection().getSectionName() : null,
                s.getPassword()// sectionName
        );
    }

    @Transactional
    public String assignSectionToStudents(AssignSectionRequestDTO request) {

        Section section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> new IllegalArgumentException("Section not found with ID: " + request.getSectionId()));

        List<Student> students = studentRepository.findAllById(request.getStudentIds());

        if (students.isEmpty()) {
            throw new IllegalArgumentException("No students found for the provided IDs: " + request.getStudentIds());
        }

        for (Student student : students) {
            student.setSection(section);
        }

        studentRepository.saveAll(students);

        return "Section '" + section.getSectionName() + "' assigned successfully to " + students.size() + " students.";
    }

    @Transactional
    public Student promoteStudent(Long studentId, Long newStandardId, Long newSectionId) {

        // 1️⃣ Fetch student
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));

        // 2️⃣ Fetch new Standard
        Standard newStandard = standardRepository.findById(newStandardId)
                .orElseThrow(() -> new ResourceNotFoundException("Standard", "id", newStandardId));

        // 3️⃣ Fetch new Section (optional)
        Section newSection = null;
        if (newSectionId != null) {
            newSection = sectionRepository.findById(newSectionId)
                    .orElseThrow(() -> new ResourceNotFoundException("Section", "id", newSectionId));
        }

        // 4️⃣ Update student's standard and section
        student.setStandard(newStandard);
        student.setSection(newSection);

        // 5️⃣ Get previous AcademicRecord (latest active one)
        AcademicRecord previousRecord = academicRecordRepository
                .findTopByStudentIdOrderByIdDesc(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("AcademicRecord", "studentId", studentId));

        // 6️⃣ Set previous record status to PROMOTED
        previousRecord.setStatus(AcademicStatus.PROMOTED);
        academicRecordRepository.save(previousRecord);

        // 7️⃣ Create new AcademicRecord for next year
        AcademicRecord newRecord = new AcademicRecord();
        newRecord.setStudent(student);// optional if AcademicRecord stores standard
        newRecord.setSection(newSection);
        newRecord.setStatus(AcademicStatus.ACTIVE);
        newRecord.setAcademicYear(AcademicYearGenerator.generateNextAcademicYear(previousRecord.getAcademicYear()));
        academicRecordRepository.save(newRecord);

        // 8️⃣ Save updated student
        return studentRepository.save(student);
    }



}
