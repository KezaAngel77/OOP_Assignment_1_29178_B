package com.auca.main;

import com.auca.abstracts.Person;
import com.auca.abstracts.AcademicMember;
import com.auca.administrator.Administrator;
import com.auca.assessment.Assessment;
import com.auca.bookloan.BookLoan;
import com.auca.classroom.Classroom;
import com.auca.course.Course;
import com.auca.department.Department;
import com.auca.enrollment.Enrollment;
import com.auca.exceptions.*;
import com.auca.feepayment.FeePayment;
import com.auca.interfaces.Identifiable;
import com.auca.librarybook.LibraryBook;
import com.auca.student.Student;
import com.auca.teacher.Teacher;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    // In-memory collections
    private static final List<Department> departments = new ArrayList<>();
    private static final List<Student> students = new ArrayList<>();
    private static final List<Teacher> teachers = new ArrayList<>();
    private static final List<Administrator> administrators = new ArrayList<>();
    private static final List<Course> courses = new ArrayList<>();
    private static final List<Classroom> classrooms = new ArrayList<>();
    private static final List<Enrollment> enrollments = new ArrayList<>();
    private static final List<Assessment> assessments = new ArrayList<>();
    private static final List<FeePayment> feePayments = new ArrayList<>();
    private static final List<LibraryBook> libraryBooks = new ArrayList<>();
    private static final List<BookLoan> bookLoans = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("================================================");
        System.out.println("   AUCA_ID SCHOOL MANAGEMENT SYSTEM");
        System.out.println("   Console-based OOP Practical Assignment");
        System.out.println("================================================");

        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = readLine("Select an option: ").trim();
            try {
                switch (choice) {
                    case "1":
                        registerDepartment();
                        break;
                    case "2":
                        registerStudent();
                        break;
                    case "3":
                        registerTeacher();
                        break;
                    case "4":
                        registerAdministrator();
                        break;
                    case "5":
                        registerCourse();
                        break;
                    case "6":
                        registerClassroom();
                        break;
                    case "7":
                        enrollStudent();
                        break;
                    case "8":
                        recordAssessment();
                        break;
                    case "9":
                        recordFeePayment();
                        break;
                    case "10":
                        registerLibraryBook();
                        break;
                    case "11":
                        borrowBook();
                        break;
                    case "12":
                        returnBook();
                        break;
                    case "13":
                        viewAllRecords();
                        break;
                    case "14":
                        searchById();
                        break;
                    case "15":
                        updateRecord();
                        break;
                    case "16":
                        showStudentAcademicSummary();
                        break;
                    case "17":
                        showPaymentHistory();
                        break;
                    case "18":
                        showAvailableBooks();
                        break;
                    case "19":
                        demonstratePolymorphism();
                        break;
                    case "0":
                        System.out.println("Exiting AUCA_ID. Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please enter a number from 0 to 19.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("! Error: " + e.getMessage());
                System.out.println("Returning to main menu.");
            }
        }
        scanner.close();
    }

    private static void printMainMenu() {
        System.out.println();
        System.out.println("============== AUCA_ID SCHOOL MANAGEMENT ==============");
        System.out.println(" 1. Register Department       11. Borrow Library Book");
        System.out.println(" 2. Register Student          12. Return Library Book");
        System.out.println(" 3. Register Teacher          13. View All Records");
        System.out.println(" 4. Register Administrator    14. Search Record by ID");
        System.out.println(" 5. Register Course           15. Update Existing Record");
        System.out.println(" 6. Register Classroom        16. Student Academic Summary");
        System.out.println(" 7. Enroll Student            17. Student Payment History");
        System.out.println(" 8. Record Assessment         18. View Available Books");
        System.out.println(" 9. Record Fee Payment        19. Demonstrate Polymorphism");
        System.out.println("10. Register Library Book      0. Exit");
        System.out.println("=======================================================");
    }

    // ==================== UTILITY METHODS ====================

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static String readRequired(String prompt) {
        while (true) {
            String input = readLine(prompt).trim();
            if (input.equalsIgnoreCase("CANCEL") || input.equals("0") && prompt.toLowerCase().contains("cancel")) {
                return null;
            }
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("! Value cannot be blank. Re-enter or type CANCEL.");
        }
    }

    private static LocalDate readDate(String prompt) {
        while (true) {
            String input = readLine(prompt).trim();
            if (input.equalsIgnoreCase("CANCEL")) return null;
            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("! Invalid date: use YYYY-MM-DD (example: 2004-05-16).");
            }
        }
    }

    private static int readInt(String prompt, int min, int max) {
        while (true) {
            String input = readLine(prompt).trim();
            if (input.equalsIgnoreCase("CANCEL")) return Integer.MIN_VALUE;
            try {
                int val = Integer.parseInt(input);
                if (val >= min && val <= max) return val;
                System.out.println("! Value must be between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("! Please enter a valid integer.");
            }
        }
    }

    private static BigDecimal readMoney(String prompt) {
        while (true) {
            String input = readLine(prompt).trim();
            if (input.equalsIgnoreCase("CANCEL")) return null;
            try {
                BigDecimal val = new BigDecimal(input);
                if (val.compareTo(BigDecimal.ZERO) > 0) return val;
                System.out.println("! Amount must be strictly positive.");
            } catch (NumberFormatException e) {
                System.out.println("! Please enter a valid number.");
            }
        }
    }

    private static boolean isIdUnique(String id, List<? extends Identifiable> list) {
        for (Identifiable item : list) {
            if (item.getId().equalsIgnoreCase(id)) return false;
        }
        return true;
    }

    private static boolean isStudentNumberUnique(String num) {
        for (Student s : students) {
            if (s.getStudentNumber().equalsIgnoreCase(num)) return false;
        }
        return true;
    }

    private static boolean isEmployeeNumberUnique(String num) {
        for (Teacher t : teachers) {
            if (t.getEmployeeNumber().equalsIgnoreCase(num)) return false;
        }
        return true;
    }

    private static boolean isCourseCodeUnique(String code) {
        for (Course c : courses) {
            if (c.getCourseCode().equalsIgnoreCase(code)) return false;
        }
        return true;
    }

    private static boolean isReceiptUnique(String receipt) {
        for (FeePayment p : feePayments) {
            if (p.getReceiptNumber().equalsIgnoreCase(receipt)) return false;
        }
        return true;
    }

    // ==================== REGISTRATION METHODS ====================

    private static void registerDepartment() {
        System.out.println("\n------------- REGISTER DEPARTMENT -------------");
        String id = readRequired("Department ID (String, unique, 4-30): ");
        if (id == null) return;
        if (!isIdUnique(id, departments)) {
            System.out.println("! Department ID already exists.");
            return;
        }
        String name = readRequired("Department Name (2-60 chars): ");
        if (name == null) return;
        String code = readRequired("Department Code: ");
        if (code == null) return;
        String location = readRequired("Office Location: ");
        if (location == null) return;
        String email = readRequired("Contact Email: ");
        if (email == null) return;

        Teacher head = null;
        if (!teachers.isEmpty()) {
            System.out.println("Select Head Teacher (optional):");
            for (int i = 0; i < teachers.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + teachers.get(i).getRegistrationSummary());
            }
            System.out.println("  0. Skip / No head teacher");
            int sel = readInt("Select: ", 0, teachers.size());
            if (sel > 0) head = teachers.get(sel - 1);
        }

        try {
            Department dep = new Department(id, name, code, location, email, head);
            dep.validate();
            departments.add(dep);
            System.out.println("+ Department " + id + " registered successfully.");
        } catch (ValidationException e) {
            System.out.println("! Validation failed: " + e.getMessage());
        }
    }

    private static void registerStudent() {
        System.out.println("\n+------------------------------------------------------------+");
        System.out.println("| AUCA_ID  /  SCHOOL MANAGEMENT                              |");
        System.out.println("| STUDENT REGISTRATION                     Step 1 of 4       |");
        System.out.println("+------------------------------------------------------------+");

        // Step 1 - Person details
        System.out.println("\n[STEP 1/4] PERSONAL DETAILS");
        String personId = readRequired("01  Person ID          [String | unique | 4-30 chars] : ");
        if (personId == null) return;
        if (!isIdUnique(personId, students) && !isIdUnique(personId, teachers) && !isIdUnique(personId, administrators)) {
            // rough uniqueness across persons; improve if needed
        }
        String firstName = readRequired("02  First name         [String | 2-60 chars]      : ");
        if (firstName == null) return;
        String lastName = readRequired("03  Last name          [String | 2-60 chars]      : ");
        if (lastName == null) return;
        String email = readRequired("04  Email              [String | unique | email]: ");
        if (email == null) return;
        String phone = readRequired("05  Phone              [String | 10-15 digits]  : ");
        if (phone == null) return;
        LocalDate dob = readDate("06  Date of birth      [LocalDate | YYYY-MM-DD] : ");
        if (dob == null) return;

        // Step 2 - Academic
        System.out.println("\n[STEP 2/4] ACADEMIC DETAILS");
        String memberNumber = readRequired("07  Member number      [String | unique | 4-30] : ");
        if (memberNumber == null) return;

        Department dept = selectOrRegisterDepartment();
        if (dept == null) return;

        LocalDate joinDate = readDate("09  Join date          [LocalDate | YYYY-MM-DD] : ");
        if (joinDate == null) return;

        System.out.println("10  Academic status    [1 ACTIVE / 2 INACTIVE] : ");
        int statusChoice = readInt("Select: ", 1, 2);
        if (statusChoice == Integer.MIN_VALUE) return;
        String academicStatus = statusChoice == 1 ? "ACTIVE" : "INACTIVE";

        String instEmail = readRequired("11  Institutional email[String | unique | email]: ");
        if (instEmail == null) return;
        String advisor = readRequired("12  Advisor name       [String | 2-120 chars]   : ");
        if (advisor == null) return;

        // Step 3 - Student
        System.out.println("\n[STEP 3/4] STUDENT DETAILS");
        String studentNumber = readRequired("13  Student number     [String | unique | 4-30] : ");
        if (studentNumber == null) return;
        if (!isStudentNumberUnique(studentNumber)) {
            System.out.println("! Student number already exists.");
            return;
        }
        String program = readRequired("14  Program name       [String | 2-120 chars]   : ");
        if (program == null) return;
        LocalDate admission = readDate("15  Admission date     [LocalDate | YYYY-MM-DD] : ");
        if (admission == null) return;
        int year = readInt("16  Study year         [int | 1-6]              : ", 1, 6);
        if (year == Integer.MIN_VALUE) return;
        String guardian = readRequired("17  Guardian phone     [String | 10-15 digits]  : ");
        if (guardian == null) return;
        System.out.println("18  Scholarship        [1 YES / 2 NO]          : ");
        int sch = readInt("Select: ", 1, 2);
        if (sch == Integer.MIN_VALUE) return;
        boolean scholarship = sch == 1;

        // Step 4 - Review
        System.out.println("\n+---------------- REVIEW STUDENT ---------------------------+");
        System.out.println("| PERSON                                                    |");
        System.out.println("| 01 Person ID          : " + personId);
        System.out.println("| 02 First name         : " + firstName);
        System.out.println("| 03 Last name          : " + lastName);
        System.out.println("| 04 Email              : " + email);
        System.out.println("| 05 Phone              : " + phone);
        System.out.println("| 06 Date of birth      : " + dob);
        System.out.println("| ACADEMIC MEMBER                                           |");
        System.out.println("| 07 Member number      : " + memberNumber);
        System.out.println("| 08 Department         : " + dept.getDisplayName());
        System.out.println("| 09 Join date          : " + joinDate);
        System.out.println("| 10 Academic status    : " + academicStatus);
        System.out.println("| 11 Institutional email: " + instEmail);
        System.out.println("| 12 Advisor name       : " + advisor);
        System.out.println("| STUDENT                                                   |");
        System.out.println("| 13 Student number     : " + studentNumber);
        System.out.println("| 14 Program name       : " + program);
        System.out.println("| 15 Admission date     : " + admission);
        System.out.println("| 16 Study year         : " + year);
        System.out.println("| 17 Guardian phone     : " + guardian);
        System.out.println("| 18 Scholarship        : " + (scholarship ? "YES" : "NO"));
        System.out.println("+------------------------------------------------------------+");
        System.out.println("| [1] Save student  [2] Edit field  [0] Cancel              |");
        System.out.println("+------------------------------------------------------------+");

        int action = readInt("Select option: ", 0, 2);
        if (action != 1) {
            System.out.println("Registration cancelled.");
            return;
        }

        try {
            Student s = new Student(personId, firstName, lastName, email, phone, dob,
                    memberNumber, dept, joinDate, academicStatus, instEmail, advisor,
                    studentNumber, program, admission, year, guardian, scholarship);
            s.validate();
            students.add(s);
            System.out.println("+ Student " + studentNumber + " registered successfully.");
            System.out.println("  [1] Register another  [2] View student  [0] Main menu");
            // simplified; just return
        } catch (ValidationException e) {
            System.out.println("! Validation failed: " + e.getMessage());
        }
    }

    private static Department selectOrRegisterDepartment() {
        while (true) {
            if (departments.isEmpty()) {
                System.out.println("No Department is registered.");
                System.out.println("1. Register Department now   0. Cancel");
                int c = readInt("Select: ", 0, 1);
                if (c == 0 || c == Integer.MIN_VALUE) return null;
                registerDepartment();
                if (departments.isEmpty()) return null;
            }
            System.out.println("+----------------- CHOOSE DEPARTMENT ------------------------+");
            for (int i = 0; i < departments.size(); i++) {
                System.out.println("| " + (i + 1) + "  " + departments.get(i).getRegistrationSummary());
            }
            System.out.println("| " + (departments.size() + 1) + "  [+] Register a new Department");
            System.out.println("| 0  Cancel");
            System.out.println("+------------------------------------------------------------+");
            int sel = readInt("Select option: ", 0, departments.size() + 1);
            if (sel == 0 || sel == Integer.MIN_VALUE) return null;
            if (sel == departments.size() + 1) {
                registerDepartment();
                continue;
            }
            return departments.get(sel - 1);
        }
    }

    private static void registerTeacher() {
        System.out.println("\n------------- REGISTER TEACHER -------------");
        if (departments.isEmpty()) {
            System.out.println("No Department exists. Register a Department first.");
            System.out.println("1. Register Department now   0. Cancel");
            int c = readInt("Select: ", 0, 1);
            if (c != 1) return;
            registerDepartment();
            if (departments.isEmpty()) return;
        }

        String personId = readRequired("Person ID: ");
        if (personId == null) return;
        String firstName = readRequired("First name: ");
        if (firstName == null) return;
        String lastName = readRequired("Last name: ");
        if (lastName == null) return;
        String email = readRequired("Email: ");
        if (email == null) return;
        String phone = readRequired("Phone: ");
        if (phone == null) return;
        LocalDate dob = readDate("Date of birth (YYYY-MM-DD): ");
        if (dob == null) return;
        String memberNumber = readRequired("Member number: ");
        if (memberNumber == null) return;

        Department dept = selectOrRegisterDepartment();
        if (dept == null) return;

        LocalDate joinDate = readDate("Join date: ");
        if (joinDate == null) return;
        String academicStatus = "ACTIVE";
        String instEmail = readRequired("Institutional email: ");
        if (instEmail == null) return;
        String advisor = readRequired("Advisor name: ");
        if (advisor == null) return;

        String empNum = readRequired("Employee number (unique): ");
        if (empNum == null) return;
        if (!isEmployeeNumberUnique(empNum)) {
            System.out.println("! Employee number already exists.");
            return;
        }
        String spec = readRequired("Specialization: ");
        if (spec == null) return;
        LocalDate hireDate = readDate("Hire date: ");
        if (hireDate == null) return;
        BigDecimal salary = readMoney("Salary: ");
        if (salary == null) return;
        String office = readRequired("Office number: ");
        if (office == null) return;
        System.out.println("Employment type [1 FULL-TIME / 2 PART-TIME / 3 CONTRACT]: ");
        int et = readInt("Select: ", 1, 3);
        if (et == Integer.MIN_VALUE) return;
        String empType = et == 1 ? "FULL-TIME" : (et == 2 ? "PART-TIME" : "CONTRACT");

        try {
            Teacher t = new Teacher(personId, firstName, lastName, email, phone, dob,
                    memberNumber, dept, joinDate, academicStatus, instEmail, advisor,
                    empNum, spec, hireDate, salary, office, empType);
            t.validate();
            teachers.add(t);
            System.out.println("+ Teacher " + empNum + " registered successfully.");
        } catch (ValidationException e) {
            System.out.println("! Validation failed: " + e.getMessage());
        }
    }

    private static void registerAdministrator() {
        System.out.println("\n------------- REGISTER ADMINISTRATOR -------------");
        String personId = readRequired("Person ID: ");
        if (personId == null) return;
        String firstName = readRequired("First name: ");
        if (firstName == null) return;
        String lastName = readRequired("Last name: ");
        if (lastName == null) return;
        String email = readRequired("Email: ");
        if (email == null) return;
        String phone = readRequired("Phone: ");
        if (phone == null) return;
        LocalDate dob = readDate("Date of birth: ");
        if (dob == null) return;
        String staffNum = readRequired("Staff number: ");
        if (staffNum == null) return;
        String jobTitle = readRequired("Job title: ");
        if (jobTitle == null) return;
        String office = readRequired("Assigned office: ");
        if (office == null) return;
        LocalDate empDate = readDate("Employment date: ");
        if (empDate == null) return;
        int access = readInt("Access level (1-10): ", 1, 10);
        if (access == Integer.MIN_VALUE) return;
        System.out.println("Active [1 YES / 2 NO]: ");
        int act = readInt("Select: ", 1, 2);
        if (act == Integer.MIN_VALUE) return;

        try {
            Administrator a = new Administrator(personId, firstName, lastName, email, phone, dob,
                    staffNum, jobTitle, office, empDate, access, act == 1);
            a.validate();
            administrators.add(a);
            System.out.println("+ Administrator " + staffNum + " registered successfully.");
        } catch (ValidationException e) {
            System.out.println("! Validation failed: " + e.getMessage());
        }
    }

    private static void registerCourse() {
        System.out.println("\n------------- REGISTER COURSE -------------");
        if (departments.isEmpty() || teachers.isEmpty()) {
            System.out.println("Course requires Department and Teacher.");
            if (departments.isEmpty()) {
                System.out.println("1. Register Department now   0. Cancel");
                if (readInt("Select: ", 0, 1) == 1) registerDepartment();
            }
            if (teachers.isEmpty()) {
                System.out.println("1. Register Teacher now   0. Cancel");
                if (readInt("Select: ", 0, 1) == 1) registerTeacher();
            }
            if (departments.isEmpty() || teachers.isEmpty()) {
                System.out.println("Prerequisites still missing. Cancelled.");
                return;
            }
        }

        String id = readRequired("Course ID: ");
        if (id == null) return;
        String code = readRequired("Course Code (unique): ");
        if (code == null) return;
        if (!isCourseCodeUnique(code)) {
            System.out.println("! Course code already exists.");
            return;
        }
        String title = readRequired("Course Title: ");
        if (title == null) return;
        int credits = readInt("Credit Hours (1-6): ", 1, 6);
        if (credits == Integer.MIN_VALUE) return;

        Department dept = selectOrRegisterDepartment();
        if (dept == null) return;

        System.out.println("Select Teacher:");
        for (int i = 0; i < teachers.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + teachers.get(i).getRegistrationSummary());
        }
        int tsel = readInt("Select: ", 1, teachers.size());
        if (tsel == Integer.MIN_VALUE) return;
        Teacher teacher = teachers.get(tsel - 1);

        try {
            Course c = new Course(id, code, title, credits, dept, teacher);
            c.validate();
            courses.add(c);
            System.out.println("+ Course " + code + " registered successfully.");
        } catch (ValidationException e) {
            System.out.println("! Validation failed: " + e.getMessage());
        }
    }

    private static void registerClassroom() {
        System.out.println("\n------------- REGISTER CLASSROOM -------------");
        String id = readRequired("Classroom ID: ");
        if (id == null) return;
        String building = readRequired("Building Name: ");
        if (building == null) return;
        String room = readRequired("Room Number: ");
        if (room == null) return;
        int capacity = readInt("Capacity (>0): ", 1, 1000);
        if (capacity == Integer.MIN_VALUE) return;
        int floor = readInt("Floor Number (>=0): ", 0, 50);
        if (floor == Integer.MIN_VALUE) return;
        System.out.println("Available [1 YES / 2 NO]: ");
        int av = readInt("Select: ", 1, 2);
        if (av == Integer.MIN_VALUE) return;

        try {
            Classroom cl = new Classroom(id, building, room, capacity, floor, av == 1);
            cl.validate();
            classrooms.add(cl);
            System.out.println("+ Classroom " + id + " registered successfully.");
        } catch (ValidationException e) {
            System.out.println("! Validation failed: " + e.getMessage());
        }
    }

    private static void enrollStudent() {
        System.out.println("\n-------------- ENROLL STUDENT --------------");
        if (students.isEmpty()) {
            System.out.println("No Student is registered.");
            System.out.println("1. Register Student now   0. Cancel");
            if (readInt("Select: ", 0, 1) == 1) registerStudent();
            if (students.isEmpty()) return;
        }
        if (courses.isEmpty()) {
            System.out.println("No Course is registered.");
            System.out.println("1. Register Course now   0. Cancel enrollment");
            if (readInt("Select: ", 0, 1) == 1) registerCourse();
            if (courses.isEmpty()) return;
        }

        System.out.println("Select Student:");
        for (int i = 0; i < students.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + students.get(i).getRegistrationSummary());
        }
        int ssel = readInt("Select: ", 1, students.size());
        if (ssel == Integer.MIN_VALUE) return;
        Student student = students.get(ssel - 1);

        System.out.println("Select Course:");
        for (int i = 0; i < courses.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + courses.get(i).getRegistrationSummary());
        }
        int csel = readInt("Select: ", 1, courses.size());
        if (csel == Integer.MIN_VALUE) return;
        Course course = courses.get(csel - 1);

        String semester = readRequired("Semester (e.g. 2026-1): ");
        if (semester == null) return;

        // Check duplicate active enrollment
        for (Enrollment e : enrollments) {
            if (e.getStudent().equals(student) && e.getCourse().equals(course)
                    && e.getSemester().equalsIgnoreCase(semester) && e.isActive()) {
                System.out.println("! Duplicate active enrollment for same student, course and semester.");
                return;
            }
        }

        String enrId = "ENR" + (enrollments.size() + 1);
        LocalDate enrDate = LocalDate.now();
        String status = "ACTIVE";

        try {
            Enrollment enr = new Enrollment(enrId, student, course, enrDate, semester, status);
            enr.validate();
            enrollments.add(enr);
            System.out.println("+ Enrollment " + enrId + " created successfully.");
        } catch (ValidationException e) {
            System.out.println("! Validation failed: " + e.getMessage());
        }
    }

    private static void recordAssessment() {
        System.out.println("\n------------- RECORD ASSESSMENT -------------");
        if (enrollments.isEmpty()) {
            System.out.println("No Enrollment exists. Enroll a student first.");
            return;
        }
        List<Enrollment> active = new ArrayList<>();
        for (Enrollment e : enrollments) {
            if (e.isActive()) active.add(e);
        }
        if (active.isEmpty()) {
            System.out.println("No active enrollment found.");
            return;
        }
        System.out.println("Select Enrollment:");
        for (int i = 0; i < active.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + active.get(i).getRegistrationSummary());
        }
        int sel = readInt("Select: ", 1, active.size());
        if (sel == Integer.MIN_VALUE) return;
        Enrollment enr = active.get(sel - 1);

        String type = readRequired("Assessment Type (QUIZ/MIDTERM/FINAL/ASSIGNMENT/PROJECT): ");
        if (type == null) return;
        double maxScore;
        while (true) {
            String ms = readLine("Maximum Score: ").trim();
            try {
                maxScore = Double.parseDouble(ms);
                if (maxScore > 0) break;
                System.out.println("! Must be > 0.");
            } catch (NumberFormatException e) {
                System.out.println("! Invalid number.");
            }
        }
        double obtained;
        while (true) {
            String os = readLine("Obtained Score: ").trim();
            try {
                obtained = Double.parseDouble(os);
                if (obtained >= 0 && obtained <= maxScore) break;
                System.out.println("! Must be between 0 and " + maxScore);
            } catch (NumberFormatException e) {
                System.out.println("! Invalid number.");
            }
        }
        LocalDate date = readDate("Assessment Date: ");
        if (date == null) return;

        String id = "ASM" + (assessments.size() + 1);
        try {
            Assessment a = new Assessment(id, enr, type, maxScore, obtained, date);
            a.validate();
            assessments.add(a);
            System.out.println("+ Assessment recorded successfully.");
        } catch (ValidationException e) {
            System.out.println("! Validation failed: " + e.getMessage());
        }
    }

    private static void recordFeePayment() {
        System.out.println("\n------------- RECORD FEE PAYMENT -------------");
        if (students.isEmpty()) {
            System.out.println("No Student exists.");
            return;
        }
        System.out.println("Select Student:");
        for (int i = 0; i < students.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + students.get(i).getRegistrationSummary());
        }
        int sel = readInt("Select: ", 1, students.size());
        if (sel == Integer.MIN_VALUE) return;
        Student student = students.get(sel - 1);

        BigDecimal amount = readMoney("Amount: ");
        if (amount == null) return;
        LocalDate date = readDate("Payment Date: ");
        if (date == null) return;
        System.out.println("Payment Method [1 CASH / 2 BANK / 3 MOBILE / 4 CARD]: ");
        int m = readInt("Select: ", 1, 4);
        if (m == Integer.MIN_VALUE) return;
        String method = switch (m) {
            case 1 -> "CASH";
            case 2 -> "BANK";
            case 3 -> "MOBILE";
            default -> "CARD";
        };
        String receipt = readRequired("Receipt Number (unique): ");
        if (receipt == null) return;
        if (!isReceiptUnique(receipt)) {
            System.out.println("! Duplicate receipt number.");
            return;
        }

        String id = "PAY" + (feePayments.size() + 1);
        try {
            FeePayment p = new FeePayment(id, student, amount, date, method, receipt);
            p.validate();
            feePayments.add(p);
            System.out.println("+ Fee payment recorded successfully.");
        } catch (ValidationException e) {
            System.out.println("! Validation failed: " + e.getMessage());
        }
    }

    private static void registerLibraryBook() {
        System.out.println("\n------------- REGISTER LIBRARY BOOK -------------");
        String id = readRequired("Book ID: ");
        if (id == null) return;
        String isbn = readRequired("ISBN: ");
        if (isbn == null) return;
        String title = readRequired("Title: ");
        if (title == null) return;
        String author = readRequired("Author: ");
        if (author == null) return;
        int total = readInt("Total Copies (>=0): ", 0, 10000);
        if (total == Integer.MIN_VALUE) return;
        int avail = readInt("Available Copies (0 to total): ", 0, total);
        if (avail == Integer.MIN_VALUE) return;

        try {
            LibraryBook b = new LibraryBook(id, isbn, title, author, total, avail);
            b.validate();
            libraryBooks.add(b);
            System.out.println("+ Library book registered successfully.");
        } catch (ValidationException e) {
            System.out.println("! Validation failed: " + e.getMessage());
        }
    }

    private static void borrowBook() {
        System.out.println("\n------------- BORROW LIBRARY BOOK -------------");
        if (students.isEmpty() || libraryBooks.isEmpty()) {
            System.out.println("Student and LibraryBook must exist.");
            return;
        }
        System.out.println("Select Student:");
        for (int i = 0; i < students.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + students.get(i).getRegistrationSummary());
        }
        int ssel = readInt("Select: ", 1, students.size());
        if (ssel == Integer.MIN_VALUE) return;
        Student student = students.get(ssel - 1);

        System.out.println("Select Book (with available copies):");
        List<LibraryBook> available = new ArrayList<>();
        for (LibraryBook b : libraryBooks) {
            if (b.getAvailableCopies() > 0) available.add(b);
        }
        if (available.isEmpty()) {
            System.out.println("No books with available copies.");
            return;
        }
        for (int i = 0; i < available.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + available.get(i).getRegistrationSummary());
        }
        int bsel = readInt("Select: ", 1, available.size());
        if (bsel == Integer.MIN_VALUE) return;
        LibraryBook book = available.get(bsel - 1);

        LocalDate borrowed = LocalDate.now();
        LocalDate due = borrowed.plusDays(14);
        String loanId = "LOAN" + (bookLoans.size() + 1);

        try {
            BookLoan loan = new BookLoan(loanId, student, book, borrowed, due, null);
            loan.validate();
            // Update stock
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            bookLoans.add(loan);
            System.out.println("+ Book borrowed successfully. Due: " + due);
        } catch (ValidationException e) {
            System.out.println("! Validation failed: " + e.getMessage());
        }
    }

    private static void returnBook() {
        System.out.println("\n------------- RETURN LIBRARY BOOK -------------");
        List<BookLoan> activeLoans = new ArrayList<>();
        for (BookLoan l : bookLoans) {
            if (!l.isReturned()) activeLoans.add(l);
        }
        if (activeLoans.isEmpty()) {
            System.out.println("No active book loans.");
            return;
        }
        System.out.println("Select Loan to return:");
        for (int i = 0; i < activeLoans.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + activeLoans.get(i).getRegistrationSummary());
        }
        int sel = readInt("Select: ", 1, activeLoans.size());
        if (sel == Integer.MIN_VALUE) return;
        BookLoan loan = activeLoans.get(sel - 1);

        loan.setReturnedDate(LocalDate.now());
        loan.getBook().setAvailableCopies(loan.getBook().getAvailableCopies() + 1);
        System.out.println("+ Book returned successfully. Available copies updated.");
    }

    // ==================== VIEW / SEARCH / REPORT ====================

    private static void viewAllRecords() {
        System.out.println("\n============= VIEW ALL RECORDS =============");
        printList("Departments", departments);
        printList("Students", students);
        printList("Teachers", teachers);
        printList("Administrators", administrators);
        printList("Courses", courses);
        printList("Classrooms", classrooms);
        printList("Enrollments", enrollments);
        printList("Assessments", assessments);
        printList("Fee Payments", feePayments);
        printList("Library Books", libraryBooks);
        printList("Book Loans", bookLoans);
    }

    private static void printList(String title, List<?> list) {
        System.out.println("\n--- " + title + " (" + list.size() + ") ---");
        if (list.isEmpty()) {
            System.out.println("  (none)");
            return;
        }
        for (Object o : list) {
            System.out.println("  " + o);
        }
    }

    private static void searchById() {
        String id = readRequired("Enter ID to search: ");
        if (id == null) return;
        System.out.println("Searching for: " + id);
        boolean found = false;
        for (Identifiable item : collectAllIdentifiable()) {
            if (item.getId().equalsIgnoreCase(id)) {
                if (item instanceof com.auca.interfaces.Registrable reg) {
                    reg.printDetails();
                } else {
                    System.out.println(item.getDisplayName());
                }
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Record not found.");
        }
    }

    private static List<Identifiable> collectAllIdentifiable() {
        List<Identifiable> all = new ArrayList<>();
        all.addAll(departments);
        all.addAll(students);
        all.addAll(teachers);
        all.addAll(administrators);
        all.addAll(courses);
        all.addAll(classrooms);
        all.addAll(enrollments);
        all.addAll(assessments);
        all.addAll(feePayments);
        all.addAll(libraryBooks);
        all.addAll(bookLoans);
        return all;
    }

    private static void updateRecord() {
        System.out.println("Update functionality: select a record type and edit mutable fields.");
        System.out.println("(Simplified for this implementation - full edit forms can be extended similarly to registration.)");
        System.out.println("Example: You can re-register or use search to inspect.");
    }

    private static void showStudentAcademicSummary() {
        if (students.isEmpty()) {
            System.out.println("No students registered.");
            return;
        }
        System.out.println("Select Student:");
        for (int i = 0; i < students.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + students.get(i).getRegistrationSummary());
        }
        int sel = readInt("Select: ", 1, students.size());
        if (sel == Integer.MIN_VALUE) return;
        Student s = students.get(sel - 1);
        s.printDetails();
        System.out.println("\n--- Enrollments ---");
        for (Enrollment e : enrollments) {
            if (e.getStudent() == s) {
                System.out.println("  " + e.getRegistrationSummary());
            }
        }
        System.out.println("--- Assessments ---");
        for (Assessment a : assessments) {
            if (a.getEnrollment().getStudent() == s) {
                System.out.println("  " + a.getRegistrationSummary());
            }
        }
    }

    private static void showPaymentHistory() {
        if (students.isEmpty()) {
            System.out.println("No students.");
            return;
        }
        System.out.println("Select Student:");
        for (int i = 0; i < students.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + students.get(i).getRegistrationSummary());
        }
        int sel = readInt("Select: ", 1, students.size());
        if (sel == Integer.MIN_VALUE) return;
        Student s = students.get(sel - 1);
        System.out.println("Payment history for " + s.getDisplayName() + ":");
        boolean any = false;
        for (FeePayment p : feePayments) {
            if (p.getStudent() == s) {
                System.out.println("  " + p.getRegistrationSummary());
                any = true;
            }
        }
        if (!any) System.out.println("  (no payments)");
    }

    private static void showAvailableBooks() {
        System.out.println("\n--- Available Library Books ---");
        boolean any = false;
        for (LibraryBook b : libraryBooks) {
            if (b.getAvailableCopies() > 0) {
                System.out.println("  " + b.getRegistrationSummary());
                any = true;
            }
        }
        if (!any) System.out.println("  (none available)");
    }

    private static void demonstratePolymorphism() {
        System.out.println("\n============= DEMONSTRATE POLYMORPHISM =============");
        System.out.println("1. Runtime polymorphism with List<Person>:");
        List<Person> people = new ArrayList<>();
        people.addAll(students);
        people.addAll(teachers);
        people.addAll(administrators);

        if (people.isEmpty()) {
            System.out.println("  (No Person objects yet. Register some first.)");
        } else {
            for (Person p : people) {
                System.out.println("  -> " + p.getDisplayName() + " | Role: " + p.getRoleDescription()
                        + " | Active: " + p.isActive());
            }
        }

        System.out.println("\n2. Interface-based polymorphism with List<Identifiable>:");
        List<Identifiable> entities = collectAllIdentifiable();
        if (entities.isEmpty()) {
            System.out.println("  (No entities yet.)");
        } else {
            int count = 0;
            for (Identifiable e : entities) {
                System.out.println("  -> ID=" + e.getId() + " | " + e.getDisplayName() + " | Active=" + e.isActive());
                count++;
                if (count >= 10) {
                    System.out.println("  ... (showing first 10)");
                    break;
                }
            }
        }

        System.out.println("\n3. Why Person and AcademicMember cannot be instantiated:");
        System.out.println("   They are declared abstract. Abstract classes may contain abstract methods");
        System.out.println("   (getRoleDescription, getAcademicCategory) that must be implemented by concrete subclasses.");
        System.out.println("   Attempting 'new Person(...)' or 'new AcademicMember(...)' produces a compile-time error.");
        System.out.println("====================================================");
    }
}
