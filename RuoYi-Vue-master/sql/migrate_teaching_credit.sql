-- ============================================================================
-- 课程教学学分系统建表脚本
-- 模块：课程管理、授课记录、学分统计、研究型学者评估
-- ============================================================================

-- 1. 课程表：存储课程基础信息和学分值
DROP TABLE IF EXISTS `ms_course`;
CREATE TABLE `ms_course` (
  `course_id`        BIGINT       NOT NULL AUTO_INCREMENT    COMMENT '课程ID',
  `course_name`      VARCHAR(200) NOT NULL                   COMMENT '课程名称',
  `course_code`      VARCHAR(50)  DEFAULT NULL               COMMENT '课程编号',
  `credit_value`     DECIMAL(4,1) NOT NULL                   COMMENT '课程学分（每门课的学分数，如3.0、4.0）',
  `course_type`      CHAR(1)      DEFAULT '0'                 COMMENT '课程类型（0=必修课 1=选修课 2=公共课）',
  `department_id`    BIGINT       DEFAULT NULL               COMMENT '开课院系ID',
  `department_name`  VARCHAR(100) DEFAULT NULL               COMMENT '开课院系名称（冗余）',
  `total_hours`      INT          DEFAULT NULL               COMMENT '总学时',
  `status`           CHAR(1)      DEFAULT '0'                 COMMENT '状态（0=正常 1=停用）',
  `create_by`        VARCHAR(64)  DEFAULT ''                 COMMENT '创建者',
  `create_time`      DATETIME     DEFAULT NULL               COMMENT '创建时间',
  `update_by`        VARCHAR(64)  DEFAULT ''                 COMMENT '更新者',
  `update_time`      DATETIME     DEFAULT NULL               COMMENT '更新时间',
  `remark`           VARCHAR(500) DEFAULT NULL               COMMENT '备注',
  PRIMARY KEY (`course_id`),
  UNIQUE KEY `uk_course_code` (`course_code`),
  KEY `idx_department_id` (`department_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='课程表';


-- 2. 授课记录表：记录每学期/每次授课的教授、选课学生数、计算积分
DROP TABLE IF EXISTS `ms_teaching_record`;
CREATE TABLE `ms_teaching_record` (
  `record_id`        BIGINT       NOT NULL AUTO_INCREMENT    COMMENT '授课记录ID',
  `course_id`        BIGINT       NOT NULL                   COMMENT '课程ID',
  `course_name`      VARCHAR(200) DEFAULT NULL               COMMENT '课程名称（冗余）',
  `professor_id`     BIGINT       NOT NULL                   COMMENT '授课教授ID',
  `professor_name`   VARCHAR(50)  DEFAULT NULL               COMMENT '教授姓名（冗余）',
  `semester`         VARCHAR(20)  DEFAULT NULL               COMMENT '学期（如 2025-2026-1）',
  `student_count`    INT          NOT NULL DEFAULT 0         COMMENT '选课学生数量',
  `credit_per_course` DECIMAL(4,1) DEFAULT NULL              COMMENT '课程学分（冗余，用于计算）',
  `calculated_score` DECIMAL(10,1) DEFAULT NULL              COMMENT '计算积分（=课程学分×学生数量）',
  `is_research`      CHAR(1)      DEFAULT '0'                 COMMENT '是否研究型课程（0=否 1=是）',
  `status`           CHAR(1)      DEFAULT '0'                 COMMENT '状态（0=正常 1=停用）',
  `create_by`        VARCHAR(64)  DEFAULT ''                 COMMENT '创建者',
  `create_time`      DATETIME     DEFAULT NULL               COMMENT '创建时间',
  `update_by`        VARCHAR(64)  DEFAULT ''                 COMMENT '更新者',
  `update_time`      DATETIME     DEFAULT NULL               COMMENT '更新时间',
  `remark`           VARCHAR(500) DEFAULT NULL               COMMENT '备注',
  PRIMARY KEY (`record_id`),
  KEY `idx_course_id`    (`course_id`),
  KEY `idx_professor_id` (`professor_id`),
  KEY `idx_semester`     (`semester`),
  KEY `idx_status`       (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='授课记录表';


-- 3. 学生学分记录表：跟踪每位学生累计获取的学分
DROP TABLE IF EXISTS `ms_student_credit`;
CREATE TABLE `ms_student_credit` (
  `credit_id`        BIGINT       NOT NULL AUTO_INCREMENT    COMMENT '学分记录ID',
  `student_name`     VARCHAR(50)  NOT NULL                   COMMENT '学生姓名',
  `student_id`       VARCHAR(50)  DEFAULT NULL               COMMENT '学生学号',
  `department_name`  VARCHAR(100) DEFAULT NULL               COMMENT '所属院系',
  `total_credits`    DECIMAL(10,1) DEFAULT 0                COMMENT '累计总学分',
  `semester`         VARCHAR(20)  DEFAULT NULL               COMMENT '学期',
  `status`           CHAR(1)      DEFAULT '0'                 COMMENT '状态（0=正常 1=停用）',
  `create_by`        VARCHAR(64)  DEFAULT ''                 COMMENT '创建者',
  `create_time`      DATETIME     DEFAULT NULL               COMMENT '创建时间',
  `update_by`        VARCHAR(64)  DEFAULT ''                 COMMENT '更新者',
  `update_time`      DATETIME     DEFAULT NULL               COMMENT '更新时间',
  `remark`           VARCHAR(500) DEFAULT NULL               COMMENT '备注',
  PRIMARY KEY (`credit_id`),
  KEY `idx_student_id`   (`student_id`),
  KEY `idx_student_name` (`student_name`),
  KEY `idx_status`       (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='学生学分记录表';


-- 4. 初始化示例课程数据
INSERT INTO `ms_course` (`course_name`, `course_code`, `credit_value`, `course_type`, `department_id`, `department_name`, `total_hours`, `status`, `create_by`, `create_time`, `remark`) VALUES
('高等数学', 'MATH101', 4.0, '0', 1, '数学与统计学院', 64, '0', 'admin', NOW(), '公共基础课'),
('线性代数', 'MATH201', 3.0, '0', 1, '数学与统计学院', 48, '0', 'admin', NOW(), '公共基础课'),
('概率论与数理统计', 'MATH301', 3.0, '0', 1, '数学与统计学院', 48, '0', 'admin', NOW(), '公共基础课'),
('程序设计与算法', 'CS101', 3.0, '0', 2, '计算机科学与技术学院', 48, '0', 'admin', NOW(), '专业基础课'),
('数据结构', 'CS201', 4.0, '0', 2, '计算机科学与技术学院', 64, '0', 'admin', NOW(), '专业核心课'),
('人工智能导论', 'CS301', 2.0, '1', 2, '计算机科学与技术学院', 32, '0', 'admin', NOW(), '专业选修课'),
('机器学习', 'CS401', 3.0, '1', 2, '计算机科学与技术学院', 48, '0', 'admin', NOW(), '专业选修课'),
('大学物理', 'PHY101', 4.0, '0', 3, '物理学院', 64, '0', 'admin', NOW(), '公共基础课'),
('英语', 'ENG101', 3.0, '0', 4, '外国语学院', 48, '0', 'admin', NOW(), '公共基础课'),
('思想政治', 'POL101', 2.0, '2', 5, '马克思主义学院', 32, '0', 'admin', NOW(), '公共课');


-- 5. 为教学学分模块插入菜单
-- 一级菜单：课程教学学分
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `query_param`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
('教学学分管理', 0, 9, 'teaching-credit', NULL, NULL, NULL, 1, 0, 'M', '0', '0', '', 'education', 'admin', NOW(), '', NULL, '教学学分系统一级菜单');

-- 获取刚插入的菜单ID（需要根据实际数据库自动生成）
-- 假设一级菜单ID为 @teaching_credit_menu_id，下面的子菜单使用该ID作为parent_id

-- 注意：以下菜单使用变量占位符，实际ID需根据上一步INSERT的结果调整
-- 建议在 MySQL 客户端中按顺序执行，先获取一级菜单ID：
-- SET @tc_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '教学学分管理' AND parent_id = 0 LIMIT 1);

SET @tc_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '教学学分管理' AND parent_id = 0 LIMIT 1);

-- 二级菜单：课程管理
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `query_param`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
('课程管理', @tc_menu_id, 1, 'course', 'research/course/index', NULL, NULL, 1, 0, 'C', '0', '0', 'research:course:list', 'list', 'admin', NOW(), '', NULL, '课程管理菜单');

SET @course_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '课程管理' AND parent_id = @tc_menu_id LIMIT 1);

-- 课程管理按钮
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `query_param`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
('课程查询', @course_menu_id, 1, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'research:course:query', '#', 'admin', NOW(), '', NULL, ''),
('课程新增', @course_menu_id, 2, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'research:course:add', '#', 'admin', NOW(), '', NULL, ''),
('课程修改', @course_menu_id, 3, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'research:course:edit', '#', 'admin', NOW(), '', NULL, ''),
('课程删除', @course_menu_id, 4, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'research:course:remove', '#', 'admin', NOW(), '', NULL, ''),
('课程导出', @course_menu_id, 5, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'research:course:export', '#', 'admin', NOW(), '', NULL, ''),
('课程导入', @course_menu_id, 6, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'research:course:import', '#', 'admin', NOW(), '', NULL, '');

-- 二级菜单：授课记录
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `query_param`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
('授课记录', @tc_menu_id, 2, 'record', 'research/record/index', NULL, NULL, 1, 0, 'C', '0', '0', 'research:record:list', 'form', 'admin', NOW(), '', NULL, '授课记录菜单');

SET @record_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '授课记录' AND parent_id = @tc_menu_id LIMIT 1);

INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `query_param`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
('记录查询', @record_menu_id, 1, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'research:record:query', '#', 'admin', NOW(), '', NULL, ''),
('记录新增', @record_menu_id, 2, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'research:record:add', '#', 'admin', NOW(), '', NULL, ''),
('记录修改', @record_menu_id, 3, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'research:record:edit', '#', 'admin', NOW(), '', NULL, ''),
('记录删除', @record_menu_id, 4, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'research:record:remove', '#', 'admin', NOW(), '', NULL, ''),
('记录导出', @record_menu_id, 5, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'research:record:export', '#', 'admin', NOW(), '', NULL, '');

-- 二级菜单：学分统计
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `query_param`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
('学分统计', @tc_menu_id, 3, 'stats', 'research/teachingStats/index', NULL, NULL, 1, 0, 'C', '0', '0', 'research:teachingStats:view', 'chart', 'admin', NOW(), '', NULL, '教学学分统计');

-- 二级菜单：研究型评估
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `query_param`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
('研究型评估', @tc_menu_id, 4, 'evaluation', 'research/evaluation/index', NULL, NULL, 1, 0, 'C', '0', '0', 'research:evaluation:view', 'peoples', 'admin', NOW(), '', NULL, '研究型学者评估');

-- 为管理员角色(role_id=1)授权以上所有新菜单
-- 需要根据实际插入的菜单ID动态授权
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 1, menu_id FROM sys_menu
WHERE perms LIKE 'research:course:%'
   OR perms LIKE 'research:record:%'
   OR perms = 'research:teachingStats:view'
   OR perms = 'research:evaluation:view'
   OR (parent_id = @tc_menu_id AND menu_type = 'C');

INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 1, @tc_menu_id;
