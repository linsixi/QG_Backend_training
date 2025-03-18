package com.linsixi.dao;

import com.linsixi.model.User;
import com.linsixi.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public static boolean insertUser(User user) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet generatedKeys = null; // 生成的id值
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        String checkSql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try {
            conn = DatabaseUtil.getConnection();
            DatabaseUtil.beginTrans(conn);

            // 检查用户名是否已存在
            ps = conn.prepareStatement(checkSql);
            ps.setString(1, user.getUsername());
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("用户名已存在：" + user.getUsername() + "插入失败");
                DatabaseUtil.rollbackTrans(conn);
                return false; // 用户名已存在，返回false
            }

            // 插入用户数据
            ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS); // 确保返回生成的主键
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            int affectedRows = ps.executeUpdate();

            // 获取生成的 id 并赋值给 user.id
            if (affectedRows > 0) {
                generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    user.setId(generatedId); // 将生成的 id 赋值给 user.id
                    System.out.println("Generated ID: " + user.getId());
                } else {
                    System.out.println("无法获取生成的 ID");
                    DatabaseUtil.rollbackTrans(conn);
                    return false;
                }
            } else {
                System.out.println("插入失败，未生成任何行");
                DatabaseUtil.rollbackTrans(conn);
                return false;
            }

            DatabaseUtil.commitTrans(conn);
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                DatabaseUtil.rollbackTrans(conn);
            }
            e.printStackTrace();
            return false;
        }
    }


    public static boolean updateUser(User user) {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "UPDATE users SET username = ?, password = ?, role = ? WHERE id = ?";
        try {
            conn = DatabaseUtil.getConnection();
            DatabaseUtil.beginTrans(conn);//开始事务
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            ps.setInt(4, user.getId());
            int affectedRows = ps.executeUpdate();//提交sql更新
            DatabaseUtil.commitTrans(conn);//成功提交事务
            return affectedRows > 0;
        } catch (SQLException e) {
            if (conn != null) {
                DatabaseUtil.rollbackTrans(conn);//失败回滚
            }
            e.printStackTrace();
            return false;
        }
    }


    public static boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static User getUserByUsername(String username) {
        String sql = "SELECT id, username, password, role FROM users WHERE username = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();//查询
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User getUserById(int id) {
        String sql = "SELECT id, username, password, role FROM users WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
