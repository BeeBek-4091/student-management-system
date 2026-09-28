package com.sms.model;

public class Enrollment {
    private int id;
    private int studentId;
    private int courseId;
    private double marks;

    public Enrollment(int id, int studentId, int courseId, double marks) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.marks = marks;
    }

    public int getId() {
        return id;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }
}
