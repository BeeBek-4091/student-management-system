package com.sms.dao;

import com.sms.model.Enrollment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {

    public void enrollStudent(int studentId, int courseId) throws SQLException {
        String sql = "INSERT INTO enrollments (student_id, course_id, marks) VALUES (?, ?, 0)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ps.executeUpdate();
        }
    }

    public void unenrollStudent(int studentId, int courseId) throws SQLException {
        String sql = "DELETE FROM enrollments WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ps.executeUpdate();
        }
    }

    public void updateMarks(int studentId, int courseId, double marks) throws SQLException {
        String sql = "UPDATE enrollments SET marks = ? WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, marks);
            ps.setInt(2, studentId);
            ps.setInt(3, courseId);
            ps.executeUpdate();
        }
    }

    public List<Enrollment> getEnrollmentsByCourse(int courseId) throws SQLException {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT * FROM enrollments WHERE course_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Enrollment(rs.getInt("id"), rs.getInt("student_id"),
                            rs.getInt("course_id"), rs.getDouble("marks")));
                }
            }
        }
        return list;
    }

    public List<Enrollment> getEnrollmentsByStudent(int studentId) throws SQLException {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT * FROM enrollments WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Enrollment(rs.getInt("id"), rs.getInt("student_id"),
                            rs.getInt("course_id"), rs.getDouble("marks")));
                }
            }
        }
        return list;
    }
}
