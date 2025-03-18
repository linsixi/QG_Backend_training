package com.linsixi.dao;

import com.linsixi.model.Course;
import com.linsixi.model.Student;
import com.linsixi.model.User;
import com.linsixi.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public static boolean insertCourse(Course course) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet generatedKeys = null; // 生成的id值
        String sql = "INSERT INTO courses (course_name, course_description, credits, status) VALUES (?, ?, ?, ?)";
        String checkSql = "SELECT COUNT(*) FROM courses WHERE course_name = ?";
        try {
            conn = DatabaseUtil.getConnection();
            DatabaseUtil.beginTrans(conn);

            // 检查课程名称是否已存在
            ps = conn.prepareStatement(checkSql);
            ps.setString(1, course.getCourseName());
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("课程名称已存在：" + course.getCourseName() + " 插入失败");
                DatabaseUtil.rollbackTrans(conn);
                return false; // 课程名称已存在，返回false
            }

            // 插入课程数据
            ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS); // 确保返回生成的主键
            ps.setString(1, course.getCourseName());
            ps.setString(2, course.getCourseDescription());
            ps.setInt(3, course.getCredits());
            ps.setString(4, course.getStatus());
            int affectedRows = ps.executeUpdate();

            // 获取生成的 id 并赋值给 course.id
            if (affectedRows > 0) {
                generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    course.setCourseId(generatedId); // 将生成的 id 赋值给 course.id
                    System.out.println("Generated ID: " + course.getCourseId());
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

    public static boolean updateCourse(Course course) {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "UPDATE courses SET course_name = ?, course_description = ?, credits = ?, status = ? WHERE course_id = ?";
        try {
            conn = DatabaseUtil.getConnection();
            DatabaseUtil.beginTrans(conn); // 开始事务
            ps = conn.prepareStatement(sql);
            ps.setString(1, course.getCourseName());
            ps.setString(2, course.getCourseDescription());
            ps.setInt(3, course.getCredits());
            ps.setString(4, course.getStatus());
            ps.setInt(5, course.getCourseId());
            int affectedRows = ps.executeUpdate(); // 提交sql更新
            DatabaseUtil.commitTrans(conn); // 成功提交事务
            return affectedRows > 0;
        } catch (SQLException e) {
            if (conn != null) {
                DatabaseUtil.rollbackTrans(conn); // 失败回滚
            }
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteCourse(int courseId) {
        String sql = "DELETE FROM courses WHERE course_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Course getCourseById(int courseId) {
        String sql = "SELECT course_id, course_name, course_description, credits, status FROM courses WHERE course_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Course course = new Course();
                course.setCourseId(rs.getInt("course_id"));
                course.setCourseName(rs.getString("course_name"));
                course.setCourseDescription(rs.getString("course_description"));
                course.setCredits(rs.getInt("credits"));
                course.setStatus(rs.getString("status"));
                return course;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Course> getAllCourses() {
        String sql = "SELECT course_id, course_name, course_description, credits, status FROM courses";
        List<Course> courses = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Course course = new Course();
                course.setCourseId(rs.getInt("course_id"));
                course.setCourseName(rs.getString("course_name"));
                course.setCourseDescription(rs.getString("course_description"));
                course.setCredits(rs.getInt("credits"));
                course.setStatus(rs.getString("status"));
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    public static List<Course> getAllAvailableCourses() {
        String sql = "SELECT course_id, course_name, course_description, credits, status FROM courses WHERE status = 'open'";
        List<Course> courses = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Course course = new Course();
                course.setCourseId(rs.getInt("course_id"));
                course.setCourseName(rs.getString("course_name"));
                course.setCourseDescription(rs.getString("course_description"));
                course.setCredits(rs.getInt("credits"));
                course.setStatus(rs.getString("status"));
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
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

    public static List<Course> getCoursesByStudentId(int studentId) {
        String sql = "SELECT c.course_id, c.course_name, c.course_description, c.credits, c.status " +
                "FROM courses c " +
                "JOIN student_courses sc ON c.course_id = sc.course_id " +
                "WHERE sc.student_id = ?";
        List<Course> courses = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Course course = new Course();
                course.setCourseId(rs.getInt("course_id"));
                course.setCourseName(rs.getString("course_name"));
                course.setCourseDescription(rs.getString("course_description"));
                course.setCredits(rs.getInt("credits"));
                course.setStatus(rs.getString("status"));
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }
}
