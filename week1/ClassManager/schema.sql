-- 创建用户表
CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(100) NOT NULL,
                       role ENUM('admin', 'student') NOT NULL
);

-- 创建学生表
CREATE TABLE students (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          student_num VARCHAR(20) NOT NULL UNIQUE,
                          name VARCHAR(100) NOT NULL,
                          sex ENUM('male', 'female') NOT NULL,
                          grade VARCHAR(10) NOT NULL,
                          phone VARCHAR(15),
                          user_id INT NOT NULL,
                          FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 创建课程表
CREATE TABLE courses (
                         course_id INT AUTO_INCREMENT PRIMARY KEY,
                         course_name VARCHAR(100) NOT NULL,
                         course_description TEXT,
                         credits INT NOT NULL,
                         status ENUM('open', 'closed') NOT NULL
);

-- 创建选课表
CREATE TABLE student_courses (
                                 student_id INT NOT NULL,
                                 course_id INT NOT NULL,
                                 status ENUM('enrolled', 'dropped') NOT NULL,
                                 PRIMARY KEY (student_id, course_id),
                                 FOREIGN KEY (student_id) REFERENCES students(id),
                                 FOREIGN KEY (course_id) REFERENCES courses(course_id)
);
