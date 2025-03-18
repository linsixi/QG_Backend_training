# 学生选课管理系统

## 1. 系统概述
学生选课管理系统是一个基于Java的简单应用系统，用于管理学生信息、课程信息以及选课操作。系统支持用户注册、登录，并根据不同角色（管理员或学生）提供不同的功能菜单。

### 1.1 功能模块
- **用户管理**：包括用户注册、登录。
- **学生管理**：包括学生信息录入、修改、查询。
- **课程管理**：包括课程信息录入、修改、查询。
- **选课管理**：包括学生选课、退课、查看已选课程等。

---

## 2. 数据库设计

### 2.1 表结构
#### 用户表 (`users`)
| 字段名   | 类型         | 描述                       |
| -------- | ------------ | -------------------------- |
| id       | INT          | 主键，自增                 |
| username | VARCHAR(50)  | 用户名，唯一               |
| password | VARCHAR(100) | 密码                       |
| role     | ENUM         | 角色，`admin` 或 `student` |

#### 学生表 (`students`)
| 字段名      | 类型         | 描述                     |
| ----------- | ------------ | ------------------------ |
| id          | INT          | 主键，自增               |
| student_num | VARCHAR(20)  | 学号，唯一               |
| name        | VARCHAR(100) | 姓名                     |
| sex         | ENUM         | 性别，`male` 或 `female` |
| grade       | VARCHAR(10)  | 年级                     |
| phone       | VARCHAR(15)  | 手机号                   |
| user_id     | INT          | 外键，关联 `users.id`    |

#### 课程表 (`courses`)
| 字段名             | 类型         | 描述                     |
| ------------------ | ------------ | ------------------------ |
| course_id          | INT          | 主键，自增               |
| course_name        | VARCHAR(100) | 课程名称                 |
| course_description | TEXT         | 课程简介                 |
| credits            | INT          | 学分                     |
| status             | ENUM         | 状态，`open` 或 `closed` |

#### 选课表 (`student_courses`)
| 字段名     | 类型 | 描述                           |
| ---------- | ---- | ------------------------------ |
| student_id | INT  | 外键，关联 `students.id`       |
| course_id  | INT  | 外键，关联 `courses.course_id` |
| status     | ENUM | 状态，`enrolled` 或 `dropped`  |

---

## 3. 系统架构

### 3.1 模块划分
系统分为以下几个主要模块：
1. **DAO层**：数据访问对象，负责与数据库交互。
   - `UserDAO.java`：用户相关操作。
   - `StudentDAO.java`：学生相关操作。
   - `CourseDAO.java`：课程相关操作。
   - `StudentCourseDAO.java`：选课相关操作。
2. **Controller层**：业务逻辑处理。
   - `UserController.java`：用户管理相关逻辑。
   - `StudentController.java`：学生管理相关逻辑。
3. **Model层**：实体类定义。
   - `User.java`：用户实体。
   - `Student.java`：学生实体。
   - `Course.java`：课程实体。
4. **Util层**：工具类。
   - `DatabaseUtil.java`：数据库连接及事务管理工具。
5. **Main层**：程序入口。
   - `Main.java`：主程序入口，包含登录、注册、菜单等功能。

---

## 4. 核心功能实现

### 4.1 用户注册与登录
- **注册流程**：
  1. 输入用户名、密码、确认密码。
  2. 选择角色（学生或管理员）。
  3. 调用 `UserDAO.insertUser()` 方法插入用户信息。
  4. 如果是学生角色，调用 `StudentController.registerStudent()` 方法录入学生信息。

- **登录流程**：
  1. 输入用户名和密码。
  2. 调用 `UserDAO.getUserByUsername()` 方法验证用户是否存在。
  3. 根据角色进入不同菜单（学生或管理员）。

### 4.2 学生管理
- **添加学生**：
  1. 输入学生姓名、学号、性别、年级、手机号。
  2. 调用 `StudentDAO.insertStudent()` 方法插入学生信息。

- **修改学生信息**：
  1. 输入需要修改的学生ID。
  2. 修改手机号或其他信息。
  3. 调用 `StudentDAO.updateStudent()` 方法更新学生信息。

### 4.3 课程管理
- **添加课程**：
  1. 输入课程名称、简介、学分、状态。
  2. 调用 `CourseDAO.insertCourse()` 方法插入课程信息。

- **修改课程信息**：
  1. 输入需要修改的课程ID。
  2. 修改课程名称、简介、学分或状态。
  3. 调用 `CourseDAO.updateCourse()` 方法更新课程信息。

### 4.4 选课管理
- **选课**：
  1. 查看可选课程列表。
  2. 输入课程ID进行选课。
  3. 调用 `StudentCourseDAO.enrollStudentInCourse()` 方法插入选课记录。

- **退课**：
  1. 查看已选课程列表。
  2. 输入课程ID进行退课。
  3. 调用 `StudentCourseDAO.dropStudentFromCourse()` 方法更新选课状态为“dropped”。

---

## 5. 异常处理
系统在关键操作中加入了异常捕获机制，确保程序运行的稳定性。例如：
- 数据库连接失败时抛出 `SQLException`。
- 用户输入非法数据时提示重新输入。

---

## 6. 运行环境
- **开发语言**：Java
- **数据库**：MySQL
- **依赖库**：JDBC驱动

---

## 7. 使用说明
1. 启动程序后，进入主界面。
2. 选择“注册”创建新用户，或选择“登录”使用已有账号。
3. 根据角色进入对应菜单，执行相关操作。
