package com.sms.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles the connection to the SQLite database and creates the
 * required tables the first time the app runs.
 *
 * SQLite is file-based and does not use a username/password, so there
 * are no credentials to keep out of source control here.
 */
public class DBConnection {
    private static final String DB_URL = "jdbc:sqlite:student_management.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        String createStudents = "CREATE TABLE IF NOT EXISTS students (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "contact TEXT)";

        String createCourses = "CREATE TABLE IF NOT EXISTS courses (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL)";

        String createEnrollments = "CREATE TABLE IF NOT EXISTS enrollments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id INTEGER NOT NULL, " +
                "course_id INTEGER NOT NULL, " +
                "marks REAL DEFAULT 0, " +
                "FOREIGN KEY (student_id) REFERENCES students(id), " +
                "FOREIGN KEY (course_id) REFERENCES courses(id))";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createStudents);
            stmt.execute(createCourses);
            stmt.execute(createEnrollments);
            seedSampleData(conn);
        } catch (SQLException e) {
            System.out.println("Database initialization failed: " + e.getMessage());
        }
    }

    /**
     * Inserts a small set of sample students, courses, and enrollments the
     * first time the app runs (only if the students table is still empty),
     * so there's something to see right away without typing it all in by hand.
     */
    private static void seedSampleData(Connection conn) throws SQLException {
        String countSql = "SELECT COUNT(*) FROM students";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(countSql)) {
            if (rs.next() && rs.getInt(1) > 0) {
                return; // Already has data, don't seed again.
            }
        }

        String insertStudent = "INSERT INTO students (name, contact) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertStudent)) {
            String[][] students = {
                    {"John Smith", "john.smith@mail.com"},
                    {"Emma Watson", "emma.watson@mail.com"},
                    {"Michael Lee", "michael.lee@mail.com"},
                    {"Sarah Johnson", "sarah.johnson@mail.com"},
                    {"David Kim", "david.kim@mail.com"}
            };
            for (String[] s : students) {
                ps.setString(1, s[0]);
                ps.setString(2, s[1]);
                ps.executeUpdate();
            }
        }

        String insertCourse = "INSERT INTO courses (name) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(insertCourse)) {
            String[] courses = {"Mathematics", "Physics", "Computer Science"};
            for (String c : courses) {
                ps.setString(1, c);
                ps.executeUpdate();
            }
        }

        // Student IDs 1-5 and Course IDs 1-3, since this only runs on a fresh database.
        String insertEnrollment = "INSERT INTO enrollments (student_id, course_id, marks) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertEnrollment)) {
            int[][] enrollments = {
                    {1, 1, 78}, {1, 3, 85},
                    {2, 1, 92}, {2, 2, 88},
                    {3, 2, 65}, {3, 3, 74},
                    {4, 1, 55}, {4, 3, 90},
                    {5, 2, 81}, {5, 3, 70}
            };
            for (int[] e : enrollments) {
                ps.setInt(1, e[0]);
                ps.setInt(2, e[1]);
                ps.setDouble(3, e[2]);
                ps.executeUpdate();
            }
        }
    }
}
