package com.auca.student;

import com.auca.abstracts.AcademicMember;
import com.auca.department.Department;
import com.auca.exceptions.ValidationException;

import java.time.LocalDate;

public class Student extends AcademicMember {
    private String studentNumber;
    private String programName;
    private LocalDate admissionDate;
    private int studyYear;
    private String guardianPhone;
    private boolean scholarshipStatus;

    public Student(String personId, String firstName, String lastName, String email,
                   String phoneNumber, LocalDate dateOfBirth,
                   String memberNumber, Department department, LocalDate joinDate,
                   String academicStatus, String institutionalEmail, String advisorName,
                   String studentNumber, String programName, LocalDate admissionDate,
                   int studyYear, String guardianPhone, boolean scholarshipStatus) {
        super(personId, firstName, lastName, email, phoneNumber, dateOfBirth,
              memberNumber, department, joinDate, academicStatus, institutionalEmail, advisorName);
        this.studentNumber = studentNumber;
        this.programName = programName;
        this.admissionDate = admissionDate;
        this.studyYear = studyYear;
        this.guardianPhone = guardianPhone;
        this.scholarshipStatus = scholarshipStatus;
    }

    // Getters and setters
    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }

    public String getProgramName() { return programName; }
    public void setProgramName(String programName) { this.programName = programName; }

    public LocalDate getAdmissionDate() { return admissionDate; }
    public void setAdmissionDate(LocalDate admissionDate) { this.admissionDate = admissionDate; }

    public int getStudyYear() { return studyYear; }
    public void setStudyYear(int studyYear) { this.studyYear = studyYear; }

    public String getGuardianPhone() { return guardianPhone; }
    public void setGuardianPhone(String guardianPhone) { this.guardianPhone = guardianPhone; }

    public boolean isScholarshipStatus() { return scholarshipStatus; }
    public void setScholarshipStatus(boolean scholarshipStatus) { this.scholarshipStatus = scholarshipStatus; }

    @Override
    public String getRoleDescription() {
        return "Student in " + programName + ", Year " + studyYear;
    }

    @Override
    public String getAcademicCategory() {
        return "Undergraduate / Student Body";
    }

    @Override
    public void validate() throws ValidationException {
        validateAcademicFields();

        if (studentNumber == null || studentNumber.trim().isEmpty()) {
            throw new ValidationException("Student number cannot be blank.");
        }
        String sn = studentNumber.trim();
        if (sn.length() < MIN_ID_LENGTH || sn.length() > MAX_ID_LENGTH) {
            throw new ValidationException("Student number length invalid.");
        }

        if (programName == null || programName.trim().isEmpty()) {
            throw new ValidationException("Program name cannot be blank.");
        }
        String pn = programName.trim();
        if (pn.length() < MIN_NAME_LENGTH || pn.length() > 120) {
            throw new ValidationException("Program name length invalid.");
        }

        if (admissionDate == null) {
            throw new ValidationException("Admission date is required.");
        }
        if (admissionDate.isAfter(LocalDate.now().plusDays(30))) {
            throw new ValidationException("Admission date cannot be far in the future.");
        }

        if (studyYear < 1 || studyYear > 6) {
            throw new ValidationException("Study year must be between 1 and 6.");
        }

        if (guardianPhone == null || guardianPhone.trim().isEmpty()) {
            throw new ValidationException("Guardian phone cannot be blank.");
        }
        String gp = guardianPhone.trim();
        String digits = gp.startsWith("+") ? gp.substring(1) : gp;
        if (!digits.matches("\\d{10,15}")) {
            throw new ValidationException("Guardian phone must contain 10-15 digits with optional leading +.");
        }
    }

    @Override
    public void printDetails() {
        System.out.println("=== Student Details ===");
        System.out.println("Person ID         : " + personId);
        System.out.println("Name              : " + getDisplayName());
        System.out.println("Email             : " + email);
        System.out.println("Phone             : " + phoneNumber);
        System.out.println("DOB               : " + dateOfBirth);
        System.out.println("Member Number     : " + memberNumber);
        System.out.println("Department        : " + (department != null ? department.getDisplayName() : "N/A"));
        System.out.println("Join Date         : " + joinDate);
        System.out.println("Academic Status   : " + academicStatus);
        System.out.println("Inst. Email       : " + institutionalEmail);
        System.out.println("Advisor           : " + advisorName);
        System.out.println("Student Number    : " + studentNumber);
        System.out.println("Program           : " + programName);
        System.out.println("Admission Date    : " + admissionDate);
        System.out.println("Study Year        : " + studyYear);
        System.out.println("Guardian Phone    : " + guardianPhone);
        System.out.println("Scholarship       : " + (scholarshipStatus ? "YES" : "NO"));
        System.out.println("Role              : " + getRoleDescription());
        System.out.println("Category          : " + getAcademicCategory());
    }

    @Override
    public String getRegistrationSummary() {
        return "[STU] " + studentNumber + " - " + getDisplayName() + " (" + programName + ")";
    }

    @Override
    public String toString() {
        return getRegistrationSummary();
    }
}
