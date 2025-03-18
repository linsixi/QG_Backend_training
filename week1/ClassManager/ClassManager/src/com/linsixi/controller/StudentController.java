package com.linsixi.controller;

import com.linsixi.dao.CourseDAO;
import com.linsixi.dao.StudentCourseDAO;
import com.linsixi.dao.UserDAO;
import com.linsixi.dao.StudentDAO;

import com.linsixi.model.Course;
import com.linsixi.model.Student;
import com.linsixi.model.User;


import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;


public class StudentController {
    private static Scanner sc;

    public static void updatePhoneNumber(Student student) {
        sc = new Scanner(System.in);
        while (true) {
            System.out.print("请输入新的手机号码:");
            String newPhoneNumber = sc.nextLine();
            // 检查输入是否为11位纯数字
            if (newPhoneNumber.matches("\\d{11}")) {
                student.setPhone(newPhoneNumber);
                System.out.println("手机号码更新成功！");
                StudentDAO.updateStudent(student);
                break; // 输入有效，退出循环
            } else {
                System.out.println("输入无效,请输入11位数字的手机号码");
            }
        }
    }

    public static void viewEnrolledCourses(Student student) {
        List<Course> enrolledCourses = CourseDAO.getCoursesByStudentId(student.getId());

        if (enrolledCourses.isEmpty()) {
            System.out.println("您没有已选的课程");
            return;
        }

        System.out.println("已选课程列表:");
        for (int i = 0; i < enrolledCourses.size(); i++) {
            Course course = enrolledCourses.get(i);
            System.out.println("课程ID:" + course.getCourseId() + ",课程名称:" + course.getCourseName() + ",开课状态:" + course.getStatus() + ",课程学分:" + course.getCredits());
        }

    }

    public static void viewAvailableCourses(Student student) {
        List<Course> allCourses = CourseDAO.getAllAvailableCourses();
        List<Course> enrolledCourses = CourseDAO.getCoursesByStudentId(student.getId());
        List<Course> availableCourses = new ArrayList<>();

        for (int i = 0; i < allCourses.size(); i++) {
            Course course = allCourses.get(i);
            boolean isEnrolled = false;
            for (Course enrolledCourse : enrolledCourses) {
                if (enrolledCourse.getCourseId() == course.getCourseId()) {
                    isEnrolled = true;
                    break;
                }
            }
            if (!isEnrolled) {
                availableCourses.add(course);
            }
        }

        if (availableCourses.isEmpty()) {
            System.out.println("没有可选课程");
        } else {
            System.out.println("可选课程列表:");
            for (Course course : availableCourses) {
                System.out.println("课程ID:" + course.getCourseId() + ",课程名称:" + course.getCourseName() + ",开课状态:" + course.getStatus() + ",课程学分:" + course.getCredits());
                System.out.println("课程简介:" + course.getCourseDescription());
            }
        }
    }

    public static void dropCourse(Student student) {
        System.out.println("只有关闭的课程可以退选");
        List<Course> enrolledCourses = CourseDAO.getCoursesByStudentId(student.getId());

        if (enrolledCourses.isEmpty()) {
            System.out.println("您没有已选的课程");
            return;
        }

        System.out.println("已选课程列表:");
        for (int i = 0; i < enrolledCourses.size(); i++) {
            Course course = enrolledCourses.get(i);
            System.out.println("课程ID:" + course.getCourseId() + ",课程名称:" + course.getCourseName() + ",开课状态:" + course.getStatus());
        }

        sc = new Scanner(System.in);
        int courseId;
        while (true) {
            System.out.print("请输入要退选的课程ID(输入0退出):");
            try {
                courseId = sc.nextInt();
                if (courseId == 0) {
                    System.out.println("取消退选操作");
                    return;
                }
                boolean found = false;
                for (Course enrolledCourse : enrolledCourses) {
                    if (enrolledCourse.getCourseId() == courseId) {
                        found = true;
                        if ("close".equals(enrolledCourse.getStatus())) {
                            boolean dropped = StudentCourseDAO.deleteStudentCourse(student.getId(), courseId);
                            if (dropped) {
                                System.out.println("退选成功");
                                return;
                            } else {
                                System.out.println("退选失败,请重试");
                                break;
                            }
                        } else {
                            System.out.println("该课程已开课,不能退选");
                            break;
                        }
                    }
                }

                if (!found) {
                    System.out.println("输入的课程ID无效,请重新输入");
                }
            } catch (InputMismatchException e) {
                System.out.println("输入无效,请输入数字");
                sc.next(); // 清除非法输入
            }
        }
    }


    public static void enrollInCourse(Student student) {
        List<Course> allCourses = CourseDAO.getAllAvailableCourses();
        List<Course> enrolledCourses = CourseDAO.getCoursesByStudentId(student.getId());
        if (enrolledCourses.size() >= 5) {
            System.out.println("已选课程已达5门,不可再选");
        }
        List<Course> availableCourses = new ArrayList<>();

        for (int i = 0; i < allCourses.size(); i++) {
            Course course = allCourses.get(i);
            boolean isEnrolled = false;
            for (Course enrolledCourse : enrolledCourses) {
                if (enrolledCourse.getCourseId() == course.getCourseId()) {
                    if ("open".equals(enrolledCourse.getStatus())) {
                        isEnrolled = true;
                        break;
                    } else if ("close".equals(enrolledCourse.getStatus())) {
                        // 如果课程状态为 close，则跳过排除
                        continue;
                    }
                }
            }
            if (!isEnrolled) {
                availableCourses.add(course);
            }
        }

        if (availableCourses.isEmpty()) {
            System.out.println("没有可选课程");
            return;
        }
        System.out.println("可选课程列表:");
        for (Course course : availableCourses) {
            System.out.println("课程ID:" + course.getCourseId() + ",课程名称:" + course.getCourseName());
        }

        sc = new Scanner(System.in);
        int courseId;
        while (true) {
            System.out.print("请输入要选择的课程ID(输入0退出):");
            try {
                courseId = sc.nextInt();
                if (courseId == 0) {
                    System.out.println("取消选课操作");
                    return;
                }
                Course selectedCourse = CourseDAO.getCourseById(courseId);
                if (selectedCourse != null) {
                    boolean enrolled = StudentCourseDAO.enrollStudentInCourse(student.getId(), courseId);
                    if (enrolled) {
                        System.out.println("选课成功");
                    } else {
                        System.out.println("选课失败,请重试");
                    }
                    return;
                } else {
                    System.out.println("输入的课程ID无效,请重新输入");
                }
            } catch (InputMismatchException e) {
                System.out.println("输入无效,请输入数字");
                sc.next(); // 清除非法输入
            }
        }
    }

    public static boolean registerStudent(User user)//注册用户时发现是学生，导入用户信息然后注册学生信息
    {

        sc = new Scanner(System.in);

        System.out.println("===== 学生信息填写 =====");
        Student newStudent = new Student();
        //输入姓名
        System.out.print("请输入姓名:");
        String studentName = sc.nextLine();
        newStudent.setName(studentName);

        //输入学号并检测单一性
        while (true) {
            System.out.print("请输入学号:");
            String studentNumInput = sc.nextLine(); // 先读取为字符串
            if (studentNumInput.matches("\\d+")) { // 检查是否为纯数字
                int studentNum = Integer.parseInt(studentNumInput); // 转换为整数
                try {
                    if (StudentDAO.getStudentByStudentNum(studentNum) == null) {
                        newStudent.setStudentNum(studentNum);
                        break;
                    } else {
                        System.out.println("学号已存在,请重新输入");
                    }
                } catch (Exception e) {
                    // 捕获异常并提示用户重新输入
                    System.out.println("输入无效,请输入学号数字(8位以内)");
                }
            } else {
                System.out.println("输入无效,请输入学号数字(8位以内)");
            }
        }

        //输入性别
        System.out.print("请选择性别(输入1 代表男,2 代表女):");
        while (true) {
            try {
                int sexChoice = sc.nextInt(); // 尝试读取整数
                if (sexChoice == 1 || sexChoice == 2) {
                    newStudent.setSex(sexChoice == 1 ? "male" : "female");
                    break; // 输入有效，退出循环
                } else {
                    System.out.print("输入错误,请输入 1 或 2:");
                }
            } catch (InputMismatchException e) {
                // 捕获异常并提示用户重新输入
                System.out.print("输入无效,请输入数字 1 或 2:");
                sc.next(); // 清除非法输入
            }
        }

        System.out.print("请输入年级:");
        String studentGrade = sc.nextLine();
        newStudent.setGrade(studentGrade);

        System.out.print("请输入手机号:");
        String studentPhone = sc.nextLine();
        newStudent.setPhone(studentPhone);

        newStudent.setUser(user);

        return StudentDAO.insertStudent(newStudent);
    }
}