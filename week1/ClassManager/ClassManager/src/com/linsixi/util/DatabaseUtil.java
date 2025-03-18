package com.linsixi.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/classmanager?useServerPrepStmts=true";
    private static final String USER = "root";
    private static final String PASSWORD = "qq1205631050";

    // 获取数据库连接
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // 释放资源
    public static void closeResources(Connection conn, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 开始事务
    public static void beginTrans(Connection conn) throws SQLException {
        conn.setAutoCommit(false);
    }

    // 提交事务
    public static void commitTrans(Connection conn) throws SQLException {
        conn.commit();
        conn.setAutoCommit(true);
    }

    // 回滚事务
    public static void rollbackTrans(Connection conn) {
        try {
            conn.rollback();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
