package com.sms;

import com.sms.dao.DBConnection;
import com.sms.exception.StudentNotFoundException;
import com.sms.model.Course;
import com.sms.model.Student;
import com.sms.service.StudentService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentService service = new StudentService();

    public static void main(String[] args) {
        DBConnection.initializeDatabase();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");

            try {
                switch (choice) {
                    case 1:
                        addStudent();
                        break;
                    case 2:
                        viewAllStudents();
                        break;
                    case 3:
                        updateStudent();
                        break;
                    case 4:
                        deleteStudent();
                        break;
                    case 5:
                        addCourse();
                        break;
                    case 6:
                        viewAllCourses();
                        break;
                    case 7:
                        enrollStudent();
                        break;
                    case 8:
                        unenrollStudent();
                        break;
                    case 9:
                        searchStudent();
                        break;
                    case 10:
                        listStudentsByCourse();
                        break;
                    case 11:
                        recordMarks();
                        break;
                    case 12:
                        service.printRankingReport();
                        break;
                    case 13:
                        running = false;
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            } catch (StudentNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n===== STUDENT MANAGEMENT SYSTEM =====");
        System.out.println("1. Add Student");
        System.out.println("2. View All Students");
        System.out.println("3. Update Student");
        System.out.println("4. Delete Student");
        System.out.println("5. Add Course");
        System.out.println("6. View All Courses");
        System.out.println("7. Enroll Student in Course");
        System.out.println("8. Unenroll Student from Course");
        System.out.println("9. Search Student (by ID or Name)");
        System.out.println("10. List Students by Course");
        System.out.println("11. Record/Update Marks");
        System.out.println("12. Class Ranking Report");
        System.out.println("13. Exit");
        System.out.println("======================================");
    }

    private static void addStudent() throws SQLException {
        String name = readString("Enter student name: ");
        String contact = readString("Enter contact info: ");
        service.addStudent(name, contact);
        System.out.println("Student added successfully.");
    }

    private static void viewAllStudents() throws SQLException {
        List<Student> students = service.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        System.out.println("ID | Name | Contact");
        for (Student s : students) {
            System.out.println(s);
        }
    }

    private static void updateStudent() throws SQLException {
        int id = readInt("Enter student ID to update: ");
        String name = readString("Enter new name: ");
        String contact = readString("Enter new contact info: ");
        service.updateStudent(id, name, contact);
        System.out.println("Student updated successfully.");
    }

    private static void deleteStudent() throws SQLException {
        int id = readInt("Enter student ID to delete: ");
        service.deleteStudent(id);
        System.out.println("Student deleted successfully.");
    }

    private static void addCourse() throws SQLException {
        String name = readString("Enter course name: ");
        service.addCourse(name);
        System.out.println("Course added successfully.");
    }

    private static void viewAllCourses() throws SQLException {
        List<Course> courses = service.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        System.out.println("ID | Name");
        for (Course c : courses) {
            System.out.println(c);
        }
    }

    private static void enrollStudent() throws SQLException {
        int studentId = readInt("Enter student ID: ");
        int courseId = readInt("Enter course ID: ");
        service.enrollStudent(studentId, courseId);
        System.out.println("Student enrolled successfully.");
    }

    private static void unenrollStudent() throws SQLException {
        int studentId = readInt("Enter student ID: ");
        int courseId = readInt("Enter course ID: ");
        service.unenrollStudent(studentId, courseId);
        System.out.println("Student unenrolled successfully.");
    }

    private static void searchStudent() throws SQLException, StudentNotFoundException {
        System.out.println("Search by: 1. ID   2. Name");
        int option = readInt("Choose an option: ");
        if (option == 1) {
            int id = readInt("Enter student ID: ");
            Student s = service.findStudentById(id);
            System.out.println(s);
        } else if (option == 2) {
            String name = readString("Enter name (or part of it): ");
            List<Student> results = service.searchStudentByName(name);
            if (results.isEmpty()) {
                System.out.println("No matching students found.");
            } else {
                for (Student s : results) {
                    System.out.println(s);
                }
            }
        } else {
            System.out.println("Invalid option.");
        }
    }

    private static void listStudentsByCourse() throws SQLException, StudentNotFoundException {
        int courseId = readInt("Enter course ID: ");
        List<Student> students = service.getStudentsByCourse(courseId);
        if (students.isEmpty()) {
            System.out.println("No students enrolled in this course.");
            return;
        }
        for (Student s : students) {
            System.out.println(s);
        }
    }

    private static void recordMarks() throws SQLException {
        int studentId = readInt("Enter student ID: ");
        int courseId = readInt("Enter course ID: ");
        double marks = readDouble("Enter marks: ");
        service.recordMarks(studentId, courseId, marks);
        System.out.println("Marks recorded successfully.");
    }

    // ---------- Input validation helpers ----------

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty. Please try again.");
        }
    }
}
