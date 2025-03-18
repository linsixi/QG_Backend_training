package com.linsixi.model;

public class Course {
    private int courseId;
    private String courseName;
    private String courseDescription;
    private int credits;
    private String status; // 例如 "open" 或 "closed"

    // 无参构造方法
    public Course() {
    }

    // 有参构造方法
    public Course(int courseId, String courseName, String courseDescription, int credits, String status) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.credits = credits;
        this.status = status;
    }

    // Getter 和 Setter 方法
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
