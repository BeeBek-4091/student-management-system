package com.sms.model;

public class Student implements Reportable {
    private int id;
    private String name;
    private String contact;

    public Student(int id, String name, String contact) {
        this.id = id;
        this.name = name;
        this.contact = contact;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toReportLine() {
        return "Student[ID=" + id + ", Name=" + name + ", Contact=" + contact + "]";
    }

    @Override
    public String toString() {
        return id + " | " + name + " | " + contact;
    }
}
