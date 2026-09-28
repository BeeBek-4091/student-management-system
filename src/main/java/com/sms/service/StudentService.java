package com.sms.service;

import com.sms.dao.CourseDAO;
import com.sms.dao.EnrollmentDAO;
import com.sms.dao.StudentDAO;
import com.sms.exception.StudentNotFoundException;
import com.sms.model.Course;
import com.sms.model.Enrollment;
import com.sms.model.Student;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Business logic layer. Talks to the DAOs and does the small amount of
 * in-memory work (averages, sorting for the ranking report) using the
 * Collections Framework.
 */
public class StudentService {
    private final StudentDAO studentDAO = new StudentDAO();
    private final CourseDAO courseDAO = new CourseDAO();
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

    public void addStudent(String name, String contact) throws SQLException {
        studentDAO.addStudent(new Student(0, name, contact));
    }

    public List<Student> getAllStudents() throws SQLException {
        return studentDAO.getAllStudents();
    }

    public Student findStudentById(int id) throws SQLException, StudentNotFoundException {
        return studentDAO.getStudentById(id);
    }

    public List<Student> searchStudentByName(String name) throws SQLException {
        return studentDAO.searchByName(name);
    }

    public void updateStudent(int id, String name, String contact) throws SQLException {
        studentDAO.updateStudent(new Student(id, name, contact));
    }

    public void deleteStudent(int id) throws SQLException {
        studentDAO.deleteStudent(id);
    }

    public void addCourse(String name) throws SQLException {
        courseDAO.addCourse(new Course(0, name));
    }

    public List<Course> getAllCourses() throws SQLException {
        return courseDAO.getAllCourses();
    }

    public void enrollStudent(int studentId, int courseId) throws SQLException {
        enrollmentDAO.enrollStudent(studentId, courseId);
    }

    public void unenrollStudent(int studentId, int courseId) throws SQLException {
        enrollmentDAO.unenrollStudent(studentId, courseId);
    }

    public void recordMarks(int studentId, int courseId, double marks) throws SQLException {
        enrollmentDAO.updateMarks(studentId, courseId, marks);
    }

    public List<Student> getStudentsByCourse(int courseId) throws SQLException, StudentNotFoundException {
        List<Enrollment> enrollments = enrollmentDAO.getEnrollmentsByCourse(courseId);
        List<Student> students = new ArrayList<>();
        for (Enrollment e : enrollments) {
            students.add(studentDAO.getStudentById(e.getStudentId()));
        }
        return students;
    }

    public double computeAverage(int studentId) throws SQLException {
        List<Enrollment> enrollments = enrollmentDAO.getEnrollmentsByStudent(studentId);
        if (enrollments.isEmpty()) {
            return 0.0;
        }
        double total = 0;
        for (Enrollment e : enrollments) {
            total += e.getMarks();
        }
        return total / enrollments.size();
    }

    /**
     * Prints a class ranking report: every student sorted by average marks,
     * highest first. A HashMap is used for fast ID -> Student lookups while
     * the ranking (an ArrayList of IDs) is sorted with a Comparator.
     */
    public void printRankingReport() throws SQLException {
        List<Student> allStudents = studentDAO.getAllStudents();

        Map<Integer, Student> studentMap = new HashMap<>();
        Map<Integer, Double> averages = new HashMap<>();
        for (Student s : allStudents) {
            studentMap.put(s.getId(), s);
            averages.put(s.getId(), computeAverage(s.getId()));
        }

        List<Integer> ids = new ArrayList<>(studentMap.keySet());
        ids.sort((a, b) -> Double.compare(averages.get(b), averages.get(a)));

        System.out.println("\n===== CLASS RANKING REPORT =====");
        int rank = 1;
        for (Integer id : ids) {
            Student s = studentMap.get(id);
            System.out.printf("%d. %s (ID: %d) - Average: %.2f%n", rank, s.getName(), s.getId(), averages.get(id));
            rank++;
        }
        System.out.println("=================================\n");
    }
}
