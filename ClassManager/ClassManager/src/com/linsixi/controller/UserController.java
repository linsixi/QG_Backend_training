package com.linsixi.controller;

import com.linsixi.dao.CourseDAO;
import com.linsixi.dao.StudentCourseDAO;
import com.linsixi.model.Course;
import com.linsixi.model.User;
import com.linsixi.dao.UserDAO;
import com.linsixi.model.Student;
import com.linsixi.dao.StudentDAO;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UserController {
    private static Scanner sc;

    public static void viewStudentCourses() {
        int studentId;
        while (true) {
            System.out.print("请输入要查询的学生ID(输入0退出):");
            try {
                studentId = sc.nextInt();
                if (studentId == 0) {
                    System.out.println("取消查询操作");
                    return;
                } else {
                    Student student = StudentDAO.getStudentByStudent_Id(studentId);
                    if (student == null) {
                        System.out.println("没有找到该学生信息，请检查学生ID是否正确。");
                        continue;
                    }
                    List<Course> enrolledCourses = CourseDAO.getCoursesByStudentId(studentId);
                    if (enrolledCourses.isEmpty()) {
                        System.out.println("该学生未选课");
                        return;
                    }
                    System.out.println("该学生已选课程列表:");
                    for (Course course : enrolledCourses) {
                        System.out.println("课程ID:" + course.getCourseId() + ",课程名称:" + course.getCourseName() + ",开课状态:" + course.getStatus() + ",课程学分:" + course.getCredits());
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("输入无效,请输入数字");
                sc.next(); // 清除非法输入
            }
        }
    }

    public static void viewCourseStudents() {
        sc = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("请输入要查询的课程ID(或输入0退出): ");
                int courseId = sc.nextInt();
                if (courseId == 0) {
                    System.out.println("已退出");
                    break; // 用户选择退出
                }
                Course course = CourseDAO.getCourseById(courseId);
                if (course == null) {
                    System.out.println("没有找到该课程信息，请检查课程ID是否正确。");
                    continue;
                }
                List<Student> students = StudentDAO.getStudentsByCourseId(courseId);
                if (students.isEmpty()) {
                    System.out.println("该课程没有选修的学生。");
                } else {
                    System.out.println("选修课程 \"" + course.getCourseName() + "\" 的学生列表:");
                    for (Student student : students) {
                        System.out.println("ID: " + student.getId() + ", 学号: " + student.getStudentNum() +
                                ", 姓名: " + student.getName() + ", 性别: " + student.getSex() +
                                ", 年级: " + student.getGrade() + ", 手机号: " + student.getPhone());
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("输入无效，请输入数字。");
                sc.next(); // 清除非法输入
            }
        }
    }


    public static boolean registerUser(User user) {
        return UserDAO.insertUser(user);
    }

    public static void updateCourseCredits() {
        sc = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("请输入要修改学分的课程ID(或输入0退出):");
                int courseId = sc.nextInt();
                if (courseId == 0) {
                    System.out.println("已退出");
                    break; // 用户选择退出
                }
                Course findCourse = CourseDAO.getCourseById(courseId);
                if (findCourse == null) {
                    System.out.println("没有找到该课程信息，请检查课程ID是否正确。");
                    continue;
                }
                while (true) {
                    System.out.print("请输入新的学分,输入0返回上一级:");
                    int newCredit = sc.nextInt();
                    if (newCredit == 0) {
                        System.out.println("已返回上一级");
                        break; // 用户选择返回上一级
                    }
                    if (newCredit < 0) {
                        System.out.println("输入无效，请输入正整数学分。");
                    } else {
                        findCourse.setCredits(newCredit);
                        CourseDAO.updateCourse(findCourse);
                        System.out.println("学分更新成功。");
                        break; // 输入有效，退出循环
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("输入无效，请输入数字。");
                sc.next(); // 清除非法输入
            }
        }
    }


    public static void viewAllCourses() {
        List<Course> allCourses = CourseDAO.getAllCourses();
        if (allCourses.isEmpty()) {
            System.out.println("没有课程");
        } else {
            System.out.println("课程列表:");
            for (Course course : allCourses) {
                System.out.println("课程ID:" + course.getCourseId() + ",课程名称:" + course.getCourseName() + ",开课状态:" + course.getStatus() + ",课程学分:" + course.getCredits());
                System.out.println("课程简介:" + course.getCourseDescription());
            }
        }
    }


    public static void viewAllStudents() {
        List<Student> students = StudentDAO.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("没有学生信息");
        } else {
            System.out.println("所有学生信息:");
            for (Student student : students) {
                System.out.println("ID:" + student.getId() + ",学号:" + student.getStudentNum() +
                        ",姓名:" + student.getName() + ",性别:" + student.printSex() + ",手机号:" + student.getPhone());
            }
        }
    }

    public static void updatePhoneNumber() {
        sc = new Scanner(System.in);
        List<Student> students = StudentDAO.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("没有学生信息");
        } else {
            System.out.println("所有学生信息:");
            for (Student student : students) {
                System.out.println("ID:" + student.getId() + ",学号:" + student.getStudentNum() +
                        ",姓名:" + student.getName() + ",手机号:" + student.getPhone());
            }
        }
        System.out.print("请输入需要更改手机号的学生ID:");
        while (true) {
            try {
                int studentId = sc.nextInt();
                Student findStudent = StudentDAO.getStudentByStudent_Id(studentId);
                if (findStudent == null) {
                    System.out.print("没有该学生信息,或输入Id有误,请重新输入:");
                    continue;
                } else {
                    while (true) {
                        System.out.print("请输入新的手机号码:");
                        String newPhoneNumber = sc.next();
                        // 检查输入是否为11位纯数字
                        if (newPhoneNumber.matches("\\d{11}")) {
                            findStudent.setPhone(newPhoneNumber);
                            System.out.println("手机号码更新成功");
                            StudentDAO.updateStudent(findStudent);
                            break; // 输入有效，退出循环
                        } else {
                            System.out.println("输入无效,请输入11位数字的手机号码");
                        }
                    }
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.print("输入无效,请输入数字Id:");
                sc.next(); // 清除非法输入
            }
        }
    }


}

