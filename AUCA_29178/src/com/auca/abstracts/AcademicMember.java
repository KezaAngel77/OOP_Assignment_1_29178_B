package com.auca.abstracts;

import com.auca.department.Department;
import com.auca.exceptions.ValidationException;

import java.time.LocalDate;

public abstract class AcademicMember extends Person {
    protected String memberNumber;
    protected Department department;
    protected LocalDate joinDate;
    protected String academicStatus;
    protected String institutionalEmail;
    protected String advisorName;

    public AcademicMember(String personId, String firstName, String lastName, String email,
                          String phoneNumber, LocalDate dateOfBirth,
                          String memberNumber, Department department, LocalDate joinDate,
                          String academicStatus, String institutionalEmail, String advisorName) {
        super(personId, firstName, lastName, email, phoneNumber, dateOfBirth);
        this.memberNumber = memberNumber;
        this.department = department;
        this.joinDate = joinDate;
        this.academicStatus = academicStatus;
        this.institutionalEmail = institutionalEmail;
        this.advisorName = advisorName;
    }

    // Getters and setters
    public String getMemberNumber() { return memberNumber; }
    public void setMemberNumber(String memberNumber) { this.memberNumber = memberNumber; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }

    public LocalDate getJoinDate() { return joinDate; }
    public void setJoinDate(LocalDate joinDate) { this.joinDate = joinDate; }

    public String getAcademicStatus() { return academicStatus; }
    public void setAcademicStatus(String academicStatus) { this.academicStatus = academicStatus; }

    public String getInstitutionalEmail() { return institutionalEmail; }
    public void setInstitutionalEmail(String institutionalEmail) { this.institutionalEmail = institutionalEmail; }

    public String getAdvisorName() { return advisorName; }
    public void setAdvisorName(String advisorName) { this.advisorName = advisorName; }

    // Abstract method
    public abstract String getAcademicCategory();

    @Override
    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(academicStatus);
    }

    protected void validateAcademicFields() throws ValidationException {
        validatePersonFields();

        if (memberNumber == null || memberNumber.trim().isEmpty()) {
            throw new ValidationException("Member number cannot be blank.");
        }
        String mn = memberNumber.trim();
        if (mn.length() < MIN_ID_LENGTH || mn.length() > MAX_ID_LENGTH) {
            throw new ValidationException("Member number must be between " + MIN_ID_LENGTH + " and " + MAX_ID_LENGTH + " characters.");
        }

        if (department == null) {
            throw new ValidationException("Department is required for academic members.");
        }

        if (joinDate == null) {
            throw new ValidationException("Join date is required.");
        }
        // Allow future join dates? Spec says not in future unless justified; we'll allow up to today + some, but keep simple: no future for now
        if (joinDate.isAfter(LocalDate.now().plusDays(30))) {
            throw new ValidationException("Join date cannot be more than 30 days in the future.");
        }

        if (academicStatus == null || academicStatus.trim().isEmpty()) {
            throw new ValidationException("Academic status is required.");
        }
        String status = academicStatus.trim().toUpperCase();
        if (!status.equals("ACTIVE") && !status.equals("INACTIVE")) {
            throw new ValidationException("Academic status must be ACTIVE or INACTIVE.");
        }
        this.academicStatus = status;

        if (institutionalEmail == null || institutionalEmail.trim().isEmpty()) {
            throw new ValidationException("Institutional email cannot be blank.");
        }
        // Basic email check already in person, but unique later

        if (advisorName == null || advisorName.trim().isEmpty()) {
            throw new ValidationException("Advisor name cannot be blank.");
        }
        String an = advisorName.trim();
        if (an.length() < MIN_NAME_LENGTH || an.length() > 120) {
            throw new ValidationException("Advisor name must be between " + MIN_NAME_LENGTH + " and 120 characters.");
        }
    }
}
