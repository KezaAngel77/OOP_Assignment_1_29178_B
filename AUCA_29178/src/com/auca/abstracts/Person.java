package com.auca.abstracts;

import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.exceptions.ValidationException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public abstract class Person implements Identifiable, Registrable {
    protected String personId;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String phoneNumber;
    protected LocalDate dateOfBirth;

    public Person(String personId, String firstName, String lastName, String email,
                  String phoneNumber, LocalDate dateOfBirth) {
        this.personId = personId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
    }

    // Getters and setters
    public String getPersonId() { return personId; }
    public void setPersonId(String personId) { this.personId = personId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    // Identifiable
    @Override
    public String getId() {
        return personId;
    }

    @Override
    public String getDisplayName() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean isActive() {
        return true; // overridden in subclasses if needed
    }

    // Abstract method required
    public abstract String getRoleDescription();

    // Basic validation for Person fields
    protected void validatePersonFields() throws ValidationException {
        if (personId == null || personId.trim().isEmpty()) {
            throw new ValidationException("Person ID cannot be blank.");
        }
        String id = personId.trim();
        if (id.length() < MIN_ID_LENGTH || id.length() > MAX_ID_LENGTH) {
            throw new ValidationException("Person ID must be between " + MIN_ID_LENGTH + " and " + MAX_ID_LENGTH + " characters.");
        }

        if (firstName == null || firstName.trim().isEmpty()) {
            throw new ValidationException("First name cannot be blank.");
        }
        String fn = firstName.trim();
        if (fn.length() < MIN_NAME_LENGTH || fn.length() > MAX_NAME_LENGTH) {
            throw new ValidationException("First name must be between " + MIN_NAME_LENGTH + " and " + MAX_NAME_LENGTH + " characters.");
        }

        if (lastName == null || lastName.trim().isEmpty()) {
            throw new ValidationException("Last name cannot be blank.");
        }
        String ln = lastName.trim();
        if (ln.length() < MIN_NAME_LENGTH || ln.length() > MAX_NAME_LENGTH) {
            throw new ValidationException("Last name must be between " + MIN_NAME_LENGTH + " and " + MAX_NAME_LENGTH + " characters.");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email cannot be blank.");
        }
        String em = email.trim();
        if (em.length() > MAX_EMAIL_LENGTH) {
            throw new ValidationException("Email exceeds maximum length of " + MAX_EMAIL_LENGTH + ".");
        }
        if (!isValidEmail(em)) {
            throw new ValidationException("Invalid email format.");
        }

        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new ValidationException("Phone number cannot be blank.");
        }
        String ph = phoneNumber.trim();
        if (!isValidPhone(ph)) {
            throw new ValidationException("Phone number must contain 10-15 digits with optional leading +.");
        }

        if (dateOfBirth == null) {
            throw new ValidationException("Date of birth is required.");
        }
        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new ValidationException("Date of birth must be in the past.");
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

    private boolean isValidPhone(String phone) {
        // Optional +, then 10-15 digits
        String digits = phone.startsWith("+") ? phone.substring(1) : phone;
        return digits.matches("\\d{10,15}");
    }

    @Override
    public String toString() {
        return getDisplayName() + " (" + personId + ")";
    }
}
