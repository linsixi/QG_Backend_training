package com.linsixi.main;

import com.linsixi.controller.StudentController;
import com.linsixi.controller.UserController;
import com.linsixi.dao.StudentDAO;
import com.linsixi.dao.UserDAO;
import com.linsixi.model.Student;
import com.linsixi.model.User;
import com.linsixi.util.DatabaseUtil;

import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static Scanner sc;

    public static void main(String[] args) {

        sc = new Scanner(System.in);

        while (true) {
            System.out.println("===========================");
            System.out.println("🎓 学生选课管理系统");
            System.out.println("===========================");
            System.out.println("1. 登录");
            System.out.println("2. 注册");
            System.out.println("3. 退出");
            System.out.print("请选择操作（输入 1-3）：");

            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    System.out.println("退出系统。");
                    sc.close();
                    return;
                default:
                    System.out.println("无效的选择，请重新输入。");
            }
        }
    }

    private static void register() {
        System.out.println("===== 用户注册 =====");
        User newUser = new User();
        while (true) {
            System.out.print("请输入用户名：");
            String username = sc.nextLine();
            if (UserDAO.getUserByUsername(username) == null) {
                newUser.setUsername(username);
                break;
            } else {
                System.out.println("用户名已存在，请重新输入");
            }
        }
        while (true) {
            System.out.print("请输入密码：");
            String password = sc.nextLine();
            System.out.print("请确认密码：");
            String password2 = sc.nextLine();
            if (password.equals(password2)) {
                System.out.println("设置成功");
                newUser.setPassword(password);
                break;
            } else {
                System.out.println("两次输入的密码不同，设置失败，请重新输入");
            }
        }

        System.out.print("请选择角色(输入 1 代表学生,2 代表管理员):");
        while (true) {
            try {
                int roleChoice = sc.nextInt(); // 尝试读取整数
                if (roleChoice == 1 || roleChoice == 2) {
                    newUser.setRole(roleChoice == 1 ? "student" : "admin");
                    break; // 输入有效，退出循环
                } else {
                    System.out.println("输入错误，请输入 1 或 2：");
                }
            } catch (InputMismatchException e) {
                // 捕获异常并提示用户重新输入
                System.out.println("输入无效，请输入数字 1 或 2：");
                sc.next(); // 清除非法输入
            }
        }
        boolean userRegister = UserController.registerUser(newUser);
        if (!userRegister) {
            System.out.println("注册失败，请重试。");
            return;
        }
        try {
            switch (newUser.getRole()) {
                case "student":
                    boolean studentRegister = StudentController.registerStudent(newUser);
                    if (!studentRegister) {
                        System.out.println("注册失败，请重试。");
                        return;
                    }
                    System.out.println("学生信息填写成功！");
                    System.out.println("学生账号注册成功！请返回主界面登录。");
                    break;
                case "admin":
                    System.out.println("管理员注册成功！请返回主界面登录。");
                    break;
                default:
                    System.out.println("无效的角色选择，请重新选择。");
                    return;
            }
        } catch (Exception e) {
            System.out.println("注册失败，请重试。");
            UserDAO.deleteUser(newUser.getId());
            return;
        }
    }


    private static void login() {
        User findUser = new User();
        while (true) {
            System.out.print("请输入用户名:");
            String username = sc.nextLine();
            findUser = UserDAO.getUserByUsername(username);
            if (UserDAO.getUserByUsername(username) == null) {
                System.out.println("该用户不存在,请重新输入");
            } else {
                break;
            }
        }
        while (true) {
            System.out.print("请输入密码:");
            String password = sc.nextLine();
            if (findUser.getPassword().equals(password)) {
                System.out.println("登录成功," + findUser.getUsername());
                break;
            } else {
                System.out.println("密码错误,请重新输入:");
            }
        }
        System.out.println("你的角色是:" + findUser.printRole());
        if (findUser.getRole().equals("admin")) {
            adminMenu(findUser);
        } else {
            Student student = StudentDAO.getStudentByUser_Id(findUser.getId());
            studentMenu(student);
        }
    }

    private static void studentMenu(Student student) {
        while (true) {
            System.out.println("===== 学生菜单 =====");
            System.out.println("1. 查看可选课程");
            System.out.println("2. 选择课程");
            System.out.println("3. 退选课程");
            System.out.println("4. 查看已选课程");
            System.out.println("5. 修改手机号");
            System.out.println("6. 退出");
            System.out.print("请选择操作(输入 1-6):");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    StudentController.viewAvailableCourses(student);
                    break;
                case 2:
                    StudentController.enrollInCourse(student);
                    break;
                case 3:
                    StudentController.dropCourse(student);
                    break;
                case 4:
                    StudentController.viewEnrolledCourses(student);
                    break;
                case 5:
                    StudentController.updatePhoneNumber(student);
                    break;
                case 6:
                    return;
                default:
                    System.out.println("选择无效,请重新输入");
            }
        }
    }

    private static void adminMenu(User user) {
        while (true) {
            System.out.println("===== 管理员菜单 =====");
            System.out.println("1. 查询所有学生");
            System.out.println("2. 修改学生手机号");
            System.out.println("3. 查询所有课程");
            System.out.println("4. 修改课程学分");
            System.out.println("5. 查询某课程的学生名单");
            System.out.println("6. 查询某学生的选课情况");
            System.out.println("7. 退出");
            System.out.print("请选择操作(输入 1-7):");

            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    UserController.viewAllStudents();
                    break;
                case 2:
                    UserController.updatePhoneNumber();
                    break;
                case 3:
                    UserController.viewAllCourses();
                    break;
                case 4:
                    UserController.viewAllCourses();//展示一下再修改学分
                    UserController.updateCourseCredits();
                    break;
                case 5:
                    UserController.viewAllStudents();//展示学生信息
                    UserController.viewCourseStudents();//查询某课程的学生名单
                    break;
                case 6:
                    UserController.viewStudentCourses();//查询某学生的选课情况
                    break;
                case 7:
                    return;
                default:
                    System.out.println("选择无效,请重新输入");
            }
        }
    }
}


