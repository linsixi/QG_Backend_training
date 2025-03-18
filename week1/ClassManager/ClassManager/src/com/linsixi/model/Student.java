package com.linsixi.model;

import java.util.Objects;

public class Student {
    private int id;          // 学生ID，唯一标识符
    private int studentNum;  // 学号，唯一标识符
    private String name;     // 姓名
    private String sex;      // 性别
    private String grade;    // 年级
    private String phone;    // 手机号
    private User user;       // 用户信息

    // 无参构造函数
    public Student() {
    }

    // 有参构造函数
    private Student(int id, int studentNum, String name, String sex, String grade, String phone, User user) {
        this.id = id;
        this.studentNum = studentNum;
        this.name = name;
        this.sex = sex;
        this.grade = grade;
        this.phone = phone;
        this.user = user;
    }

    // Getter 和 Setter 方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(int studentNum) {
        this.studentNum = studentNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public String printSex() {
        return this.sex.equals("male") ? "男" : "女";
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
