package com.linsixi.util;

import com.alibaba.druid.pool.DruidDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseUtil {
    private static final DruidDataSource dataSource;

    static {
        dataSource = new DruidDataSource();
        Properties properties = new Properties();
        try (InputStream inputStream = DatabaseUtil.class.getClassLoader().getResourceAsStream("theDruid.properties")) {
            if (inputStream == null) {
                throw new IOException("theDruid.properties file not found!");
            }
            properties.load(inputStream);
            dataSource.setDriverClassName(properties.getProperty("driverClassName"));
            dataSource.setUrl(properties.getProperty("url"));
            dataSource.setUsername(properties.getProperty("username"));
            dataSource.setPassword(properties.getProperty("password"));
            dataSource.setInitialSize(Integer.parseInt(properties.getProperty("initialSize")));
            dataSource.setMinIdle(Integer.parseInt(properties.getProperty("minIdle")));
            dataSource.setMaxActive(Integer.parseInt(properties.getProperty("maxActive")));
            dataSource.setMaxWait(Long.parseLong(properties.getProperty("maxWait")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 获取数据库连接
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
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
