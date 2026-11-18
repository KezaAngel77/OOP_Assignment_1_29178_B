package com.auca.department;

import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.exceptions.ValidationException;
import com.auca.teacher.Teacher;

public class Department implements Identifiable, Registrable {
    private String departmentId;
    private String departmentName;
    private String departmentCode;
    private String officeLocation;
    private String contactEmail;
    private Teacher headTeacher; // optional at creation

    public Department(String departmentId, String departmentName, String departmentCode,
                      String officeLocation, String contactEmail, Teacher headTeacher) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.departmentCode = departmentCode;
        this.officeLocation = officeLocation;
        this.contactEmail = contactEmail;
        this.headTeacher = headTeacher;
    }

    // Getters and setters
    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public String getDepartmentCode() { return departmentCode; }
    public void setDepartmentCode(String departmentCode) { this.departmentCode = departmentCode; }

    public String getOfficeLocation() { return officeLocation; }
    public void setOfficeLocation(String officeLocation) { this.officeLocation = officeLocation; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public Teacher getHeadTeacher() { return headTeacher; }
    public void setHeadTeacher(Teacher headTeacher) { this.headTeacher = headTeacher; }

    @Override
    public String getId() {
        return departmentId;
    }

    @Override
    public String getDisplayName() {
        return departmentName + " (" + departmentCode + ")";
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void validate() throws ValidationException {
        if (departmentId == null || departmentId.trim().isEmpty()) {
            throw new ValidationException("Department ID cannot be blank.");
        }
        String id = departmentId.trim();
        if (id.length() < MIN_ID_LENGTH || id.length() > MAX_ID_LENGTH) {
            throw new ValidationException("Department ID length invalid.");
        }

        if (departmentName == null || departmentName.trim().isEmpty()) {
            throw new ValidationException("Department name cannot be blank.");
        }
        String name = departmentName.trim();
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            throw new ValidationException("Department name length invalid.");
        }

        if (departmentCode == null || departmentCode.trim().isEmpty()) {
            throw new ValidationException("Department code cannot be blank.");
        }

        if (officeLocation == null || officeLocation.trim().isEmpty()) {
            throw new ValidationException("Office location cannot be blank.");
        }

        if (contactEmail == null || contactEmail.trim().isEmpty()) {
            throw new ValidationException("Contact email cannot be blank.");
        }
        // headTeacher is optional
    }

    @Override
    public void printDetails() {
        System.out.println("=== Department Details ===");
        System.out.println("ID          : " + departmentId);
        System.out.println("Name        : " + departmentName);
        System.out.println("Code        : " + departmentCode);
        System.out.println("Location    : " + officeLocation);
        System.out.println("Email       : " + contactEmail);
        System.out.println("Head Teacher: " + (headTeacher != null ? headTeacher.getDisplayName() : "Not assigned"));
    }

    @Override
    public String getRegistrationSummary() {
        return "[DEP] " + departmentId + " - " + departmentName + " (" + departmentCode + ")";
    }

    @Override
    public String toString() {
        return getRegistrationSummary();
    }
}
