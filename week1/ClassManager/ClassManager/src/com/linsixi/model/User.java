package com.linsixi.model;

public class User {
    private int id;
    private String username;
    private String password;
    private String role; // 可以是 "admin" 或 "student"

    // 无参构造函数
    public User() {
    }

    // 有参构造函数
    private User(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getter 和 Setter 方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public String printRole() {
        return role.equals("admin") ? "管理员" : "学生";
    }

    public void setRole(String role) {
        this.role = role;
    }
}
