package com.linsixi.dao;

import com.linsixi.model.Student;
import com.linsixi.model.User;
import com.linsixi.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public static boolean insertStudent(Student student) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet generatedKeys = null; // 生成的id值
        String sql = "INSERT INTO students (student_num, name, sex, grade, phone, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        String checkSql = "SELECT COUNT(*) FROM students WHERE student_num = ?";
        try {
            conn = DatabaseUtil.getConnection();
            DatabaseUtil.beginTrans(conn);

            // 检查学号是否已存在
            ps = conn.prepareStatement(checkSql);
            ps.setInt(1, student.getStudentNum());
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("学号已存在：" + student.getStudentNum() + " 插入失败");
                DatabaseUtil.rollbackTrans(conn);
                return false; // 学号已存在，返回false
            }

            // 插入学生数据
            ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS); // 确保返回生成的主键
            ps.setInt(1, student.getStudentNum());
            ps.setString(2, student.getName());
            ps.setString(3, student.getSex());
            ps.setString(4, student.getGrade());
            ps.setString(5, student.getPhone());
            ps.setInt(6, student.getUser().getId());
            int affectedRows = ps.executeUpdate();

            // 获取生成的 id 并赋值给 student.id
            if (affectedRows > 0) {
                generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    student.setId(generatedId); // 将生成的 id 赋值给 student.id
                    System.out.println("Generated ID: " + student.getId());
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

    public static boolean updateStudent(Student student) {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "UPDATE students SET student_num = ?, name = ?, sex = ?, grade = ?, phone = ?, user_id = ? WHERE user_id = ?";
        try {
            conn = DatabaseUtil.getConnection();
            DatabaseUtil.beginTrans(conn);//开始事务
            ps = conn.prepareStatement(sql);
            ps.setInt(1, student.getStudentNum());
            ps.setString(2, student.getName());
            ps.setString(3, student.getSex());
            ps.setString(4, student.getGrade());
            ps.setString(5, student.getPhone());
            ps.setInt(6, student.getUser().getId());
            ps.setInt(7, student.getUser().getId());
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

    public static boolean deleteStudent(int user_id) {
        String sql = "DELETE FROM students WHERE user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, user_id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Student getStudentByStudentNum(int studentNum) {
        String sql = "SELECT id, student_num, name, sex, grade, phone, user_id FROM students WHERE student_num = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentNum);
            ResultSet rs = ps.executeQuery();//查询
            if (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setStudentNum(rs.getInt("student_num"));
                student.setName(rs.getString("name"));
                student.setSex(rs.getString("sex"));
                student.setGrade(rs.getString("grade"));
                student.setPhone(rs.getString("phone"));
                User user = UserDAO.getUserById(rs.getInt("user_id"));
                student.setUser(user);
                return student;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Student getStudentByUser_Id(int user_id) {
        String sql = "SELECT id, student_num, name, sex, grade, phone, user_id FROM students WHERE user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, user_id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setStudentNum(rs.getInt("student_num"));
                student.setName(rs.getString("name"));
                student.setSex(rs.getString("sex"));
                student.setGrade(rs.getString("grade"));
                student.setPhone(rs.getString("phone"));
                User user = UserDAO.getUserById(rs.getInt("user_id"));
                student.setUser(user);
                return student;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Student getStudentByStudent_Id(int id) {
        String sql = "SELECT id, student_num, name, sex, grade, phone, user_id FROM students WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setStudentNum(rs.getInt("student_num"));
                student.setName(rs.getString("name"));
                student.setSex(rs.getString("sex"));
                student.setGrade(rs.getString("grade"));
                student.setPhone(rs.getString("phone"));
                User user = UserDAO.getUserById(rs.getInt("user_id"));
                student.setUser(user);
                return student;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Student> getAllStudents() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Student> students = new ArrayList<>();
        String sql = "SELECT id, student_num, name, sex, grade, phone, user_id FROM students";
        try {
            conn = DatabaseUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setStudentNum(rs.getInt("student_num"));
                student.setName(rs.getString("name"));
                student.setSex(rs.getString("sex"));
                student.setGrade(rs.getString("grade"));//暂时没做，因为没用到
                student.setPhone(rs.getString("phone"));
                User user = UserDAO.getUserById(rs.getInt("user_id"));
                student.setUser(user);
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeResources(conn, ps, rs);
        }
        return students;
    }

    public static List<Student> getStudentsByCourseId(int courseId) {
        String sql = "SELECT s.id, s.student_num, s.name, s.sex, s.grade, s.phone, s.user_id " +
                "FROM students s " +
                "JOIN student_courses sc ON s.id = sc.student_id " +
                "WHERE sc.course_id = ?";
        List<Student> students = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setStudentNum(rs.getInt("student_num"));
                student.setName(rs.getString("name"));
                student.setSex(rs.getString("sex"));
                student.setGrade(rs.getString("grade"));
                student.setPhone(rs.getString("phone"));
                User user = UserDAO.getUserById(rs.getInt("user_id"));
                student.setUser(user);
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
}


