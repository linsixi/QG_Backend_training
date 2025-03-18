package com.linsixi.main;

import com.linsixi.model.User;
import com.linsixi.dao.UserDAO;
import com.linsixi.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class Test {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            // 获取数据库连接
            connection = DatabaseUtil.getConnection();
            System.out.println("数据库连接成功！");
        } catch (SQLException e) {
            System.err.println("数据库连接失败！");
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (connection != null) {
                    connection.close();
                    System.out.println("数据库连接已关闭！");
                }
            } catch (SQLException e) {
                System.err.println("关闭数据库连接时发生错误！");
                e.printStackTrace();
            }
        }

        // 测试插入用户
        User newUser = new User();
        newUser.setUsername("方源");
        newUser.setPassword("11233");
        newUser.setRole("admin");
        boolean insertResult = UserDAO.insertUser(newUser);
        System.out.println("Insert User Result: " + insertResult);

        User deleteUser = new User();
        deleteUser = UserDAO.getUserByUsername("方源");
        if (deleteUser == null) {
            System.out.println("用户不存在");
        } else {
            boolean deleteResult = UserDAO.deleteUser(deleteUser.getId());
            System.out.println("Delete User Result: " + deleteResult);
        }
    }
}
