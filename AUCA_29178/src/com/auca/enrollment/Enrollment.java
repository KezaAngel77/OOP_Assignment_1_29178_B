package com.auca.enrollment;

import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.exceptions.ValidationException;
import com.auca.student.Student;
import com.auca.course.Course;

import java.time.LocalDate;

public class Enrollment implements Identifiable, Registrable {
    private String enrollmentId;
    private Student student;
    private Course course;
    private LocalDate enrollmentDate;
    private String semester;
    private String enrollmentStatus;

    public Enrollment(String enrollmentId, Student student, Course course,
                      LocalDate enrollmentDate, String semester, String enrollmentStatus) {
        this.enrollmentId = enrollmentId;
        this.student = student;
        this.course = course;
        this.enrollmentDate = enrollmentDate;
        this.semester = semester;
        this.enrollmentStatus = enrollmentStatus;
    }

    // Getters and setters
    public String getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(String enrollmentId) { this.enrollmentId = enrollmentId; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public String getEnrollmentStatus() { return enrollmentStatus; }
    public void setEnrollmentStatus(String enrollmentStatus) { this.enrollmentStatus = enrollmentStatus; }

    @Override
    public String getId() { return enrollmentId; }

    @Override
    public String getDisplayName() {
        return (student != null ? student.getStudentNumber() : "?") + " -> " +
               (course != null ? course.getCourseCode() : "?") + " [" + semester + "]";
    }

    @Override
    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(enrollmentStatus);
    }

    @Override
    public void validate() throws ValidationException {
        if (enrollmentId == null || enrollmentId.trim().isEmpty()) {
            throw new ValidationException("Enrollment ID cannot be blank.");
        }
        if (student == null) {
            throw new ValidationException("Student is required.");
        }
        if (course == null) {
            throw new ValidationException("Course is required.");
        }
        if (enrollmentDate == null) {
            throw new ValidationException("Enrollment date is required.");
        }
        if (semester == null || semester.trim().isEmpty()) {
            throw new ValidationException("Semester cannot be blank.");
        }
        if (enrollmentStatus == null || enrollmentStatus.trim().isEmpty()) {
            throw new ValidationException("Enrollment status is required.");
        }
        String status = enrollmentStatus.trim().toUpperCase();
        if (!status.equals("ACTIVE") && !status.equals("COMPLETED") && !status.equals("WITHDRAWN")) {
            throw new ValidationException("Enrollment status must be ACTIVE, COMPLETED or WITHDRAWN.");
        }
        this.enrollmentStatus = status;
    }

    @Override
    public void printDetails() {
        System.out.println("=== Enrollment Details ===");
        System.out.println("ID         : " + enrollmentId);
        System.out.println("Student    : " + (student != null ? student.getRegistrationSummary() : "N/A"));
        System.out.println("Course     : " + (course != null ? course.getRegistrationSummary() : "N/A"));
        System.out.println("Date       : " + enrollmentDate);
        System.out.println("Semester   : " + semester);
        System.out.println("Status     : " + enrollmentStatus);
    }

    @Override
    public String getRegistrationSummary() {
        return "[ENR] " + enrollmentId + " - " + getDisplayName();
    }

    @Override
    public String toString() {
        return getRegistrationSummary();
    }
}
