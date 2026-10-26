-- ============================================================================
-- 迁移脚本：教师 CV 简历系统 + AoL 课程图谱模块
-- 版本：v1.3.0
-- 内容：
--   A. CV 模块 7 张表（档案/教育/经历/资格/授课/职业发展/社会服务）
--   B. AoL 课程图谱 7 张表（项目/CG/LG/图谱课程/覆盖格/测评/课程委员会）
--   C. 菜单与权限（CV 2600 段，AoL 2700 段）
--   D. 系统参数 cv.year.window（CV 科研成果统计年限，默认 6 年）
--   E. AoL 预置种子数据（工商管理学士 BBM：5 CG + 12 LG + 5 测评，源自
--      《1.1 AACSB Advanced Curriculum Map-Template.xlsx》）
-- 幂等性：可重复执行（CREATE IF NOT EXISTS / INSERT IGNORE / NOT EXISTS 防重）
-- 日期：2026-10-26
-- ============================================================================

-- ---------------------------------------------------------------------------
-- A. CV 模块表
-- ---------------------------------------------------------------------------

-- A1. CV 档案主表（每位教师一条，服务端首次访问自动创建）
CREATE TABLE IF NOT EXISTS `ms_cv_profile` (
  `cv_id`           BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'CV档案ID',
  `professor_id`    BIGINT       NOT NULL                COMMENT '教师ID（ms_professor）',
  `cdut_id`         VARCHAR(50)  DEFAULT NULL            COMMENT 'CDUT工号（员工编号）',
  `photo_url`       VARCHAR(500) DEFAULT NULL            COMMENT '证件照URL',
  `phone`           VARCHAR(50)  DEFAULT NULL            COMMENT '联系电话',
  `position`        VARCHAR(100) DEFAULT NULL            COMMENT '岗位/职务',
  `employment_type` CHAR(1)      DEFAULT '1'             COMMENT '任教类型（1=本院全职 2=外院兼职）',
  `aacsb_class`     CHAR(2)      DEFAULT NULL            COMMENT 'AACSB师资分类（SA/PA/SP/IP/A）',
  `employer`        VARCHAR(300) DEFAULT 'College of Management Science, Chengdu University of Technology' COMMENT '所属单位',
  `address`         VARCHAR(300) DEFAULT 'Erxianqiao Subdistrict, Chenghua District, Chengdu, Sichuan, China' COMMENT '通讯地址',
  `areas_of_interest` TEXT       DEFAULT NULL            COMMENT '研究与教学领域（每行一条，3-5条）',
  `other_info`      VARCHAR(1000) DEFAULT NULL           COMMENT '博士后/访学经历补充（CV第3部分）',
  `declare_flag`    CHAR(1)      DEFAULT '0'             COMMENT '已确认真实性声明（0否 1是）',
  `declare_time`    DATETIME     DEFAULT NULL            COMMENT '声明确认时间',
  `status`          CHAR(1)      DEFAULT '0'             COMMENT '状态（0正常 1停用）',
  `create_by`       VARCHAR(64)  DEFAULT ''              COMMENT '创建者',
  `create_time`     DATETIME     DEFAULT NULL            COMMENT '创建时间',
  `update_by`       VARCHAR(64)  DEFAULT ''              COMMENT '更新者',
  `update_time`     DATETIME     DEFAULT NULL            COMMENT '更新时间',
  `remark`          VARCHAR(500) DEFAULT NULL            COMMENT '备注',
  PRIMARY KEY (`cv_id`),
  UNIQUE KEY `uk_cv_professor` (`professor_id`),
  KEY `idx_cv_employment` (`employment_type`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='教师CV档案表';

-- A2. CV 教育背景表（CV第3部分）
CREATE TABLE IF NOT EXISTS `ms_cv_education` (
  `education_id`  BIGINT      NOT NULL AUTO_INCREMENT COMMENT '教育背景ID',
  `professor_id`  BIGINT      NOT NULL                COMMENT '教师ID',
  `degree`        VARCHAR(50) DEFAULT NULL            COMMENT '学位（博士Ph.D./硕士Master/学士Bachelor等）',
  `major`         VARCHAR(200) DEFAULT NULL           COMMENT '专业',
  `institution`   VARCHAR(300) DEFAULT NULL           COMMENT '授予院校',
  `grad_year`     VARCHAR(10) DEFAULT NULL            COMMENT '年份',
  `is_highest`    CHAR(1)     DEFAULT '0'             COMMENT '最高学位（0否 1是）',
  `sort_order`    INT         DEFAULT 0               COMMENT '排序',
  `status`        CHAR(1)     DEFAULT '0'             COMMENT '状态（0正常 1停用）',
  `create_by`     VARCHAR(64) DEFAULT ''              COMMENT '创建者',
  `create_time`   DATETIME    DEFAULT NULL            COMMENT '创建时间',
  `update_by`     VARCHAR(64) DEFAULT ''              COMMENT '更新者',
  `update_time`   DATETIME    DEFAULT NULL            COMMENT '更新时间',
  `remark`        VARCHAR(500) DEFAULT NULL           COMMENT '备注',
  PRIMARY KEY (`education_id`),
  KEY `idx_edu_professor` (`professor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='CV教育背景表';

-- A3. CV 工作经历表（CV第4部分：exp_type 1=学术经历 2=行业/实务经历）
CREATE TABLE IF NOT EXISTS `ms_cv_experience` (
  `experience_id` BIGINT       NOT NULL AUTO_INCREMENT COMMENT '工作经历ID',
  `professor_id`  BIGINT       NOT NULL                COMMENT '教师ID',
  `exp_type`      CHAR(1)      DEFAULT '1'             COMMENT '经历类型（1=学术 2=行业/实务）',
  `period`        VARCHAR(100) DEFAULT NULL            COMMENT '起止时间',
  `organization`  VARCHAR(300) DEFAULT NULL            COMMENT '单位（学术经历填“单位与职务”）',
  `role`          VARCHAR(200) DEFAULT NULL            COMMENT '职务/岗位（行业经历用）',
  `description`   VARCHAR(1000) DEFAULT NULL           COMMENT '主要职责/主要业绩',
  `sort_order`    INT          DEFAULT 0               COMMENT '排序',
  `status`        CHAR(1)      DEFAULT '0'             COMMENT '状态（0正常 1停用）',
  `create_by`     VARCHAR(64)  DEFAULT ''              COMMENT '创建者',
  `create_time`   DATETIME     DEFAULT NULL            COMMENT '创建时间',
  `update_by`     VARCHAR(64)  DEFAULT ''              COMMENT '更新者',
  `update_time`   DATETIME     DEFAULT NULL            COMMENT '更新时间',
  `remark`        VARCHAR(500) DEFAULT NULL            COMMENT '备注',
  PRIMARY KEY (`experience_id`),
  KEY `idx_exp_professor` (`professor_id`),
  KEY `idx_exp_type` (`exp_type`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='CV工作经历表';

-- A4. CV 职业资格表（CV第5部分）
CREATE TABLE IF NOT EXISTS `ms_cv_certification` (
  `cert_id`      BIGINT       NOT NULL AUTO_INCREMENT COMMENT '职业资格ID',
  `professor_id` BIGINT       NOT NULL                COMMENT '教师ID',
  `cert_name`    VARCHAR(300) DEFAULT NULL            COMMENT '证书名称（如CPA/CFA/ACCA/FRM）',
  `issuing_body` VARCHAR(300) DEFAULT NULL            COMMENT '颁发机构',
  `cert_year`    VARCHAR(10)  DEFAULT NULL            COMMENT '取得年份',
  `status_text`  VARCHAR(50)  DEFAULT '有效'          COMMENT '现状（有效/已更新）',
  `sort_order`   INT          DEFAULT 0               COMMENT '排序',
  `status`       CHAR(1)      DEFAULT '0'             COMMENT '状态（0正常 1停用）',
  `create_by`    VARCHAR(64)  DEFAULT ''              COMMENT '创建者',
  `create_time`  DATETIME     DEFAULT NULL            COMMENT '创建时间',
  `update_by`    VARCHAR(64)  DEFAULT ''              COMMENT '更新者',
  `update_time`  DATETIME     DEFAULT NULL            COMMENT '更新时间',
  `remark`       VARCHAR(500) DEFAULT NULL            COMMENT '备注',
  PRIMARY KEY (`cert_id`),
  KEY `idx_cert_professor` (`professor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='CV职业资格表';

-- A5. CV 授课记录表（CV第7部分；可从 ms_teaching_record 同步，教师补充层次/角色/周学时）
CREATE TABLE IF NOT EXISTS `ms_cv_course` (
  `cv_course_id`  BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'CV授课记录ID',
  `professor_id`  BIGINT       NOT NULL                COMMENT '教师ID',
  `course_name`   VARCHAR(300) DEFAULT NULL            COMMENT '课程名称（中英文）',
  `level_name`    VARCHAR(50)  DEFAULT NULL            COMMENT '层次（本科/硕士/MBA）',
  `academic_year` VARCHAR(20)  DEFAULT NULL            COMMENT '学年（如2025-2026）',
  `teach_role`    VARCHAR(20)  DEFAULT '主讲'          COMMENT '角色（主讲/助教）',
  `weekly_hours`  VARCHAR(20)  DEFAULT NULL            COMMENT '周学时',
  `source`        CHAR(1)      DEFAULT '2'             COMMENT '数据来源（1=授课记录同步 2=手工）',
  `sort_order`    INT          DEFAULT 0               COMMENT '排序',
  `status`        CHAR(1)      DEFAULT '0'             COMMENT '状态（0正常 1停用）',
  `create_by`     VARCHAR(64)  DEFAULT ''              COMMENT '创建者',
  `create_time`   DATETIME     DEFAULT NULL            COMMENT '创建时间',
  `update_by`     VARCHAR(64)  DEFAULT ''              COMMENT '更新者',
  `update_time`   DATETIME     DEFAULT NULL            COMMENT '更新时间',
  `remark`        VARCHAR(500) DEFAULT NULL            COMMENT '备注',
  PRIMARY KEY (`cv_course_id`),
  KEY `idx_cvcourse_professor` (`professor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='CV授课记录表';

-- A6. CV 职业发展表（CV第9部分）
CREATE TABLE IF NOT EXISTS `ms_cv_development` (
  `dev_id`       BIGINT       NOT NULL AUTO_INCREMENT COMMENT '职业发展ID',
  `professor_id` BIGINT       NOT NULL                COMMENT '教师ID',
  `period`       VARCHAR(100) DEFAULT NULL            COMMENT '时间',
  `activity`     VARCHAR(500) DEFAULT NULL            COMMENT '活动内容（研修/行业实践/资格更新）',
  `organizer`    VARCHAR(300) DEFAULT NULL            COMMENT '组织单位',
  `hours`        VARCHAR(50)  DEFAULT NULL            COMMENT '学时/时长',
  `sort_order`   INT          DEFAULT 0               COMMENT '排序',
  `status`       CHAR(1)      DEFAULT '0'             COMMENT '状态（0正常 1停用）',
  `create_by`    VARCHAR(64)  DEFAULT ''              COMMENT '创建者',
  `create_time`  DATETIME     DEFAULT NULL            COMMENT '创建时间',
  `update_by`    VARCHAR(64)  DEFAULT ''              COMMENT '更新者',
  `update_time`  DATETIME     DEFAULT NULL            COMMENT '更新时间',
  `remark`       VARCHAR(500) DEFAULT NULL            COMMENT '备注',
  PRIMARY KEY (`dev_id`),
  KEY `idx_dev_professor` (`professor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='CV职业发展表';

-- A7. CV 社会服务表（CV第10部分）
CREATE TABLE IF NOT EXISTS `ms_cv_service` (
  `service_id`     BIGINT       NOT NULL AUTO_INCREMENT COMMENT '社会服务ID',
  `professor_id`   BIGINT       NOT NULL                COMMENT '教师ID',
  `period`         VARCHAR(100) DEFAULT NULL            COMMENT '时间',
  `service_content` VARCHAR(500) DEFAULT NULL           COMMENT '服务内容（审稿/学会/评审/咨询/培训等）',
  `organization`   VARCHAR(300) DEFAULT NULL            COMMENT '相关单位',
  `sort_order`     INT          DEFAULT 0               COMMENT '排序',
  `status`         CHAR(1)      DEFAULT '0'             COMMENT '状态（0正常 1停用）',
  `create_by`      VARCHAR(64)  DEFAULT ''              COMMENT '创建者',
  `create_time`    DATETIME     DEFAULT NULL            COMMENT '创建时间',
  `update_by`      VARCHAR(64)  DEFAULT ''              COMMENT '更新者',
  `update_time`    DATETIME     DEFAULT NULL            COMMENT '更新时间',
  `remark`         VARCHAR(500) DEFAULT NULL            COMMENT '备注',
  PRIMARY KEY (`service_id`),
  KEY `idx_svc_professor` (`professor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='CV社会服务表';

-- ---------------------------------------------------------------------------
-- B. AoL 课程图谱模块表
-- ---------------------------------------------------------------------------

-- B1. 学位项目表
CREATE TABLE IF NOT EXISTS `aol_program` (
  `program_id`      BIGINT       NOT NULL AUTO_INCREMENT COMMENT '学位项目ID',
  `program_name`    VARCHAR(200) NOT NULL                COMMENT '项目名称（中文）',
  `program_name_en` VARCHAR(200) DEFAULT NULL            COMMENT '项目名称（英文）',
  `degree_level`    CHAR(1)      DEFAULT '1'             COMMENT '学位层次（1=本科 2=硕士 3=博士）',
  `status`          CHAR(1)      DEFAULT '0'             COMMENT '状态（0正常 1停用）',
  `create_by`       VARCHAR(64)  DEFAULT ''              COMMENT '创建者',
  `create_time`     DATETIME     DEFAULT NULL            COMMENT '创建时间',
  `update_by`       VARCHAR(64)  DEFAULT ''              COMMENT '更新者',
  `update_time`     DATETIME     DEFAULT NULL            COMMENT '更新时间',
  `remark`          VARCHAR(500) DEFAULT NULL            COMMENT '备注',
  PRIMARY KEY (`program_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='AoL学位项目表';

-- B2. 能力目标表（CG，Competence Goal）
CREATE TABLE IF NOT EXISTS `aol_cg` (
  `cg_id`          BIGINT        NOT NULL AUTO_INCREMENT COMMENT '能力目标ID',
  `program_id`     BIGINT        NOT NULL                COMMENT '学位项目ID',
  `cg_code`        VARCHAR(10)   NOT NULL                COMMENT '编号（CG1/CG2...）',
  `cg_name`        VARCHAR(200)  DEFAULT NULL            COMMENT '名称（中文）',
  `cg_name_en`     VARCHAR(200)  DEFAULT NULL            COMMENT '名称（英文）',
  `description`    VARCHAR(1000) DEFAULT NULL            COMMENT '描述（中文）',
  `description_en` VARCHAR(1000) DEFAULT NULL            COMMENT '描述（英文）',
  `sort_order`     INT           DEFAULT 0               COMMENT '排序',
  `status`         CHAR(1)       DEFAULT '0'             COMMENT '状态（0正常 1停用）',
  `create_by`      VARCHAR(64)   DEFAULT ''              COMMENT '创建者',
  `create_time`    DATETIME      DEFAULT NULL            COMMENT '创建时间',
  `update_by`      VARCHAR(64)   DEFAULT ''              COMMENT '更新者',
  `update_time`    DATETIME      DEFAULT NULL            COMMENT '更新时间',
  `remark`         VARCHAR(500)  DEFAULT NULL            COMMENT '备注',
  PRIMARY KEY (`cg_id`),
  KEY `idx_cg_program` (`program_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='AoL能力目标表';

-- B3. 学习目标表（LG，Learning Objective；含布鲁姆层级）
CREATE TABLE IF NOT EXISTS `aol_lg` (
  `lg_id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '学习目标ID',
  `cg_id`          BIGINT       NOT NULL                COMMENT '能力目标ID',
  `lg_code`        VARCHAR(10)  NOT NULL                COMMENT '编号（1.1/1.2...）',
  `description`    VARCHAR(500) DEFAULT NULL            COMMENT '描述（中文）',
  `description_en` VARCHAR(500) DEFAULT NULL            COMMENT '描述（英文）',
  `bloom_level`    CHAR(2)      DEFAULT NULL            COMMENT '布鲁姆层级（L1记忆 L2理解 L3应用 L4分析 L5评价 L6创造）',
  `bloom_verbs`    VARCHAR(200) DEFAULT NULL            COMMENT '布鲁姆行为动词（顿号分隔）',
  `sort_order`     INT          DEFAULT 0               COMMENT '排序',
  `status`         CHAR(1)      DEFAULT '0'             COMMENT '状态（0正常 1停用）',
  `create_by`      VARCHAR(64)  DEFAULT ''              COMMENT '创建者',
  `create_time`    DATETIME     DEFAULT NULL            COMMENT '创建时间',
  `update_by`      VARCHAR(64)  DEFAULT ''              COMMENT '更新者',
  `update_time`    DATETIME     DEFAULT NULL            COMMENT '更新时间',
  `remark`         VARCHAR(500) DEFAULT NULL            COMMENT '备注',
  PRIMARY KEY (`lg_id`),
  KEY `idx_lg_cg` (`cg_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='AoL学习目标表';

-- B4. 图谱课程表（进入课程图谱的课程行，关联 ms_course）
CREATE TABLE IF NOT EXISTS `aol_map_course` (
  `map_course_id`  BIGINT       NOT NULL AUTO_INCREMENT COMMENT '图谱课程ID',
  `program_id`     BIGINT       NOT NULL                COMMENT '学位项目ID',
  `course_id`      BIGINT       DEFAULT NULL            COMMENT '课程ID（ms_course，可为空便于先建图谱）',
  `course_name`    VARCHAR(200) DEFAULT NULL            COMMENT '课程名称（中文，冗余）',
  `course_name_en` VARCHAR(200) DEFAULT NULL            COMMENT '课程名称（英文）',
  `year_level`     CHAR(1)      DEFAULT NULL            COMMENT '开课年级（1/2/3...）',
  `course_kind`    VARCHAR(100) DEFAULT NULL            COMMENT '课程性质（Core必修/Elective选修/Capstone顶点）',
  `primary_lgs`    VARCHAR(200) DEFAULT NULL            COMMENT '主要支撑学习目标（如1.1,2.1,3.1）',
  `sort_order`     INT          DEFAULT 0               COMMENT '排序',
  `status`         CHAR(1)      DEFAULT '0'             COMMENT '状态（0正常 1停用）',
  `create_by`      VARCHAR(64)  DEFAULT ''              COMMENT '创建者',
  `create_time`    DATETIME     DEFAULT NULL            COMMENT '创建时间',
  `update_by`      VARCHAR(64)  DEFAULT ''              COMMENT '更新者',
  `update_time`    DATETIME     DEFAULT NULL            COMMENT '更新时间',
  `remark`         VARCHAR(500) DEFAULT NULL            COMMENT '备注',
  PRIMARY KEY (`map_course_id`),
  KEY `idx_mc_program` (`program_id`),
  KEY `idx_mc_course` (`course_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='AoL图谱课程表';

-- B5. 覆盖矩阵明细表（课程 × LG 的 I/D/M 覆盖格）
CREATE TABLE IF NOT EXISTS `aol_map_cell` (
  `cell_id`        BIGINT   NOT NULL AUTO_INCREMENT COMMENT '覆盖格ID',
  `map_course_id`  BIGINT   NOT NULL                COMMENT '图谱课程ID',
  `lg_id`          BIGINT   NOT NULL                COMMENT '学习目标ID',
  `coverage_level` CHAR(1)  DEFAULT NULL            COMMENT '覆盖层级（I=引入 D=发展 M=精通）',
  PRIMARY KEY (`cell_id`),
  UNIQUE KEY `uk_cell` (`map_course_id`, `lg_id`),
  KEY `idx_cell_lg` (`lg_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='AoL覆盖矩阵明细表';

-- B6. 直接与间接测评方式表（按CG配置）
CREATE TABLE IF NOT EXISTS `aol_assessment` (
  `assess_id`         BIGINT        NOT NULL AUTO_INCREMENT COMMENT '测评ID',
  `program_id`        BIGINT        NOT NULL                COMMENT '学位项目ID',
  `cg_id`             BIGINT        NOT NULL                COMMENT '能力目标ID',
  `lgs_assessed`      VARCHAR(200)  DEFAULT NULL            COMMENT '所评估的LG（如1.1,1.2,1.3）',
  `direct_measures`   VARCHAR(1000) DEFAULT NULL            COMMENT '直接测评措施（每行一条）',
  `rubric_threshold`  VARCHAR(500)  DEFAULT NULL            COMMENT '评分标准与阈值',
  `indirect_measures` VARCHAR(1000) DEFAULT NULL            COMMENT '间接测评措施（每行一条）',
  `frequency`         VARCHAR(50)   DEFAULT NULL            COMMENT '频率（每年/每2年）',
  `sort_order`        INT           DEFAULT 0               COMMENT '排序',
  `status`            CHAR(1)       DEFAULT '0'             COMMENT '状态（0正常 1停用）',
  `create_by`         VARCHAR(64)   DEFAULT ''              COMMENT '创建者',
  `create_time`       DATETIME      DEFAULT NULL            COMMENT '创建时间',
  `update_by`         VARCHAR(64)   DEFAULT ''              COMMENT '更新者',
  `update_time`       DATETIME      DEFAULT NULL            COMMENT '更新时间',
  `remark`            VARCHAR(500)  DEFAULT NULL            COMMENT '备注',
  PRIMARY KEY (`assess_id`),
  KEY `idx_as_program` (`program_id`),
  KEY `idx_as_cg` (`cg_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='AoL测评方式表';

-- B7. 课程AoL委员会表（课程 ↔ 教师成员，1-3人）
CREATE TABLE IF NOT EXISTS `aol_course_committee` (
  `committee_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '委员会成员ID',
  `program_id`   BIGINT NOT NULL                COMMENT '学位项目ID',
  `map_course_id` BIGINT NOT NULL               COMMENT '图谱课程ID',
  `professor_id` BIGINT NOT NULL                COMMENT '教师ID（ms_professor）',
  `member_order` INT    DEFAULT 1               COMMENT '成员序号（1-3）',
  PRIMARY KEY (`committee_id`),
  UNIQUE KEY `uk_committee` (`map_course_id`, `professor_id`),
  KEY `idx_cmt_professor` (`professor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='AoL课程委员会表';

-- ---------------------------------------------------------------------------
-- C. 菜单与权限（CV=2600段，AoL=2700段）
-- ---------------------------------------------------------------------------

-- CV 一级目录
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (2600, '教师简历', 0, 10, 'cv', NULL, '', '', 1, 0, 'M', '0', '0', '', 'form', 'admin', NOW(), '', NULL, '教师CV简历目录');

INSERT IGNORE INTO `sys_menu` VALUES
(2601, '我的简历', 2600, 1, 'myCv', 'cv/myCv/index', '', '', 1, 0, 'C', '0', '0', 'cv:profile:view', 'user', 'admin', NOW(), '', NULL, '教师本人CV填写页'),
(2602, '简历管理', 2600, 2, 'manage', 'cv/manage/index', '', '', 1, 0, 'C', '0', '0', 'cv:manage:list', 'peoples', 'admin', NOW(), '', NULL, 'CV管理员/认证办页面');

INSERT IGNORE INTO `sys_menu` VALUES
(2603, '基本信息编辑', 2601, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'cv:profile:edit', '#', 'admin', NOW(), '', NULL, ''),
(2604, '成果新增',     2601, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'cv:research:add', '#', 'admin', NOW(), '', NULL, '教师在CV中新增科研成果写入科研库'),
(2605, 'CV导出',       2601, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'cv:export',       '#', 'admin', NOW(), '', NULL, '导出Word版CV'),
(2606, '授课同步',     2601, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'cv:course:sync',  '#', 'admin', NOW(), '', NULL, '从授课记录同步CV第7部分'),
(2607, '简历代录',     2602, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'cv:manage:edit',  '#', 'admin', NOW(), '', NULL, '管理员代教师录入CV'),
(2608, '管理导出',     2602, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'cv:export',       '#', 'admin', NOW(), '', NULL, '管理员导出教师CV');

-- AoL 一级目录
INSERT IGNORE INTO `sys_menu` VALUES
(2700, 'AoL学习保障', 0, 11, 'aol', NULL, '', '', 1, 0, 'M', '0', '0', '', 'education', 'admin', NOW(), '', NULL, 'AoL课程图谱目录');

INSERT IGNORE INTO `sys_menu` VALUES
(2701, '学位项目与目标', 2700, 1, 'program',    'aol/program/index',    '', '', 1, 0, 'C', '0', '0', 'aol:program:list',    'tree', 'admin', NOW(), '', NULL, '学位项目/CG/LG维护'),
(2702, '课程图谱',       2700, 2, 'courseMap',  'aol/courseMap/index',  '', '', 1, 0, 'C', '0', '0', 'aol:map:list',        'clipboard', 'admin', NOW(), '', NULL, '课程覆盖矩阵I/D/M'),
(2703, '测评方式',       2700, 3, 'assessment', 'aol/assessment/index', '', '', 1, 0, 'C', '0', '0', 'aol:assessment:list', 'documentation', 'admin', NOW(), '', NULL, '直接/间接测评措施'),
(2704, '课程委员会',     2700, 4, 'committee',  'aol/committee/index',  '', '', 1, 0, 'C', '0', '0', 'aol:committee:list',  'peoples', 'admin', NOW(), '', NULL, '课程AoL委员会成员');

INSERT IGNORE INTO `sys_menu` VALUES
(2705, '项目查询', 2701, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'aol:program:query',  '#', 'admin', NOW(), '', NULL, ''),
(2706, '项目新增', 2701, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'aol:program:add',    '#', 'admin', NOW(), '', NULL, '含CG/LG新增'),
(2707, '项目修改', 2701, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'aol:program:edit',   '#', 'admin', NOW(), '', NULL, '含CG/LG修改'),
(2708, '项目删除', 2701, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'aol:program:remove', '#', 'admin', NOW(), '', NULL, '含CG/LG删除'),
(2709, '图谱查询', 2702, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'aol:map:query',      '#', 'admin', NOW(), '', NULL, ''),
(2710, '图谱新增', 2702, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'aol:map:add',        '#', 'admin', NOW(), '', NULL, '含覆盖格维护'),
(2711, '图谱修改', 2702, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'aol:map:edit',       '#', 'admin', NOW(), '', NULL, '含覆盖格维护'),
(2712, '图谱删除', 2702, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'aol:map:remove',     '#', 'admin', NOW(), '', NULL, ''),
(2713, '测评查询', 2703, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'aol:assessment:query',  '#', 'admin', NOW(), '', NULL, ''),
(2714, '测评新增', 2703, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'aol:assessment:add',    '#', 'admin', NOW(), '', NULL, ''),
(2715, '测评修改', 2703, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'aol:assessment:edit',   '#', 'admin', NOW(), '', NULL, ''),
(2716, '测评删除', 2703, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'aol:assessment:remove', '#', 'admin', NOW(), '', NULL, ''),
(2717, '委员会查询', 2704, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'aol:committee:query',  '#', 'admin', NOW(), '', NULL, ''),
(2718, '委员会新增', 2704, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'aol:committee:add',    '#', 'admin', NOW(), '', NULL, ''),
(2719, '委员会修改', 2704, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'aol:committee:edit',   '#', 'admin', NOW(), '', NULL, ''),
(2720, '委员会删除', 2704, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'aol:committee:remove', '#', 'admin', NOW(), '', NULL, ''),
(2721, '图谱导出', 2702, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'aol:export', '#', 'admin', NOW(), '', NULL, '导出课程图谱xlsx/docx/Syllabus');

-- ---------------------------------------------------------------------------
-- D. 系统参数：CV 科研成果统计年限（默认6年，可在【系统管理-参数设置】调整）
-- ---------------------------------------------------------------------------
INSERT INTO `sys_config` (`config_name`, `config_key`, `config_value`, `config_type`, `create_by`, `create_time`, `remark`)
SELECT 'CV科研成果统计年限', 'cv.year.window', '6', 'N', 'admin', NOW(), '教师CV自动导入科研成果的时间窗口（年），按认证周期可调整'
WHERE NOT EXISTS (SELECT 1 FROM `sys_config` WHERE `config_key` = 'cv.year.window');

-- ---------------------------------------------------------------------------
-- E. AoL 预置种子数据：工商管理学士（BBM）项目，源自官方模板
-- ---------------------------------------------------------------------------
INSERT INTO `aol_program` (`program_id`, `program_name`, `program_name_en`, `degree_level`, `status`, `create_by`, `create_time`, `remark`)
SELECT 1, '工商管理学士', 'Business Management (BBM)', '1', '0', 'admin', NOW(), '源自AACSB Advanced Curriculum Map模板'
WHERE NOT EXISTS (SELECT 1 FROM `aol_program` WHERE `program_id` = 1);

INSERT INTO `aol_cg` (`cg_id`, `program_id`, `cg_code`, `cg_name`, `cg_name_en`, `description`, `description_en`, `sort_order`, `status`, `create_by`, `create_time`)
SELECT t.cg_id, 1, t.cg_code, t.cg_name, t.cg_name_en, t.description, t.description_en, t.sort_order, '0', 'admin', NOW() FROM (
  SELECT 1 AS cg_id, 'CG1' AS cg_code, '分析与批判性思维' AS cg_name, 'Analytical & Critical Thinking' AS cg_name_en,
         '毕业生能够运用定量与定性推理方法解决复杂的商业问题' AS description,
         'Graduates apply quantitative and qualitative reasoning to solve complex business problems.' AS description_en, 1 AS sort_order UNION ALL
  SELECT 2, 'CG2', '伦理领导力与社会责任', 'Ethical Leadership & Social Responsibility',
         '毕业生能够识别决策中的伦理维度，并在多元情境中以诚信引领',
         'Graduates recognise ethical dimensions of decisions and lead with integrity in diverse contexts.', 2 UNION ALL
  SELECT 3, 'CG3', '沟通与协作', 'Communication & Collaboration',
         '毕业生能够在多元文化环境中进行有说服力的沟通并高效协作',
         'Graduates communicate persuasively and collaborate effectively in multicultural environments.', 3 UNION ALL
  SELECT 4, 'CG4', '全球商业洞察力', 'Global Business Acumen',
         '毕业生能够解读国际商业环境并运用全球化战略思维',
         'Graduates interpret international business environments and apply global strategic thinking.', 4 UNION ALL
  SELECT 5, 'CG5', '创新与数字化转型', 'Innovation & Digital Transformation',
         '毕业生能够运用设计思维与数字化工具创建并评估新的商业模式',
         'Graduates apply design thinking and digital tools to create and evaluate new business models.', 5
) t
WHERE NOT EXISTS (SELECT 1 FROM `aol_cg` WHERE `cg_id` = t.cg_id);

INSERT INTO `aol_lg` (`lg_id`, `cg_id`, `lg_code`, `description`, `description_en`, `bloom_level`, `bloom_verbs`, `sort_order`, `status`, `create_by`, `create_time`)
SELECT t.lg_id, t.cg_id, t.lg_code, t.description, t.description_en, t.bloom_level, t.bloom_verbs, t.sort_order, '0', 'admin', NOW() FROM (
  SELECT 11 AS lg_id, 1 AS cg_id, '1.1' AS lg_code, '运用相关数据源识别并界定商业问题' AS description, 'Identify and frame business problems using relevant data sources' AS description_en, 'L4' AS bloom_level, '识别、界定、诊断' AS bloom_verbs, 1 AS sort_order UNION ALL
  SELECT 12, 1, '1.2', '应用统计与财务分析技术', 'Apply statistical and financial analysis techniques', 'L3', '计算、建模、解读', 2 UNION ALL
  SELECT 13, 1, '1.3', '评估证据并得出有依据的结论', 'Evaluate evidence and draw justified conclusions', 'L5', '批判、论证、综合', 3 UNION ALL
  SELECT 21, 2, '2.1', '运用伦理框架应对利益相关者困境', 'Apply ethical frameworks to stakeholder dilemmas', 'L3', '应用、推理、辩护', 1 UNION ALL
  SELECT 22, 2, '2.2', '评估企业社会责任与ESG影响', 'Evaluate corporate social responsibility and ESG implications', 'L5', '评估、判断、建议', 2 UNION ALL
  SELECT 31, 3, '3.1', '面向不同受众撰写清晰的商业报告', 'Produce clear written business reports for varied audiences', 'L3', '写作、组织、沟通', 1 UNION ALL
  SELECT 32, 3, '3.2', '借助视觉辅助进行结构化的口头陈述', 'Deliver structured oral presentations with visual support', 'L3', '陈述、说服、表达', 2 UNION ALL
  SELECT 33, 3, '3.3', '展现有效的团队合作与冲突解决能力', 'Demonstrate effective teamwork and conflict resolution', 'L4', '协作、解决、协商', 3 UNION ALL
  SELECT 41, 4, '4.1', '评估全球市场中的宏观环境力量', 'Assess macro-environmental forces in global markets', 'L4', '分析、比较、评估', 1 UNION ALL
  SELECT 42, 4, '4.2', '制定市场进入与国际战略方案', 'Develop market entry and international strategy proposals', 'L6', '设计、开发、提出', 2 UNION ALL
  SELECT 51, 5, '5.1', '利用数字技术分析并改进业务流程', 'Utilise digital technologies to analyse and improve business processes', 'L3', '使用、实施、优化', 1 UNION ALL
  SELECT 52, 5, '5.2', '设计并推介创新商业解决方案', 'Design and pitch an innovative business solution', 'L6', '设计、推介、原型开发', 2
) t
WHERE NOT EXISTS (SELECT 1 FROM `aol_lg` WHERE `lg_id` = t.lg_id);

INSERT INTO `aol_assessment` (`assess_id`, `program_id`, `cg_id`, `lgs_assessed`, `direct_measures`, `rubric_threshold`, `indirect_measures`, `frequency`, `sort_order`, `status`, `create_by`, `create_time`)
SELECT t.assess_id, 1, t.cg_id, t.lgs_assessed, t.direct_measures, t.rubric_threshold, t.indirect_measures, t.frequency, t.sort_order, '0', 'admin', NOW() FROM (
  SELECT 1 AS assess_id, 1 AS cg_id, '1.1, 1.2, 1.3' AS lgs_assessed,
         '案例分析考试\n定量方法项目' AS direct_measures,
         'AAC&U 定量素养评分标准——≥70% 的学生达到“里程碑（Milestone）”及以上水平' AS rubric_threshold,
         '毕业生调查\n用人单位调查' AS indirect_measures, '每年' AS frequency, 1 AS sort_order UNION ALL
  SELECT 2, 2, '2.1, 2.2', '伦理案例书面论文\nCSR审计报告', '项目自制伦理评分标准——≥75% 达到“熟练（Proficient）”及以上', '校友调查\n同行评价', '每2年', 2 UNION ALL
  SELECT 3, 3, '3.1, 3.2, 3.3', '书面报告（评分）\n口头陈述（评分）', 'AAC&U 书面与口头沟通评分标准——≥70% 达到“里程碑3”', '用人单位反馈表\n学生自评', '每年', 3 UNION ALL
  SELECT 4, 4, '4.1, 4.2', '国际战略报告\n小组案例汇报', '项目自制全球商业洞察力评分标准——≥70% 达到“熟练”', '海外学习反思报告\n毕业生调查（全球胜任力）', '每年', 4 UNION ALL
  SELECT 5, 5, '5.1, 5.2', '顶点课程创新推介\n数字工具项目', '项目自制创新评分标准——≥70% 达到“熟练”及以上', '校友调查（数字技能）\n行业专家小组反馈', '每2年', 5
) t
WHERE NOT EXISTS (SELECT 1 FROM `aol_assessment` WHERE `assess_id` = t.assess_id);

-- ============================================================================
-- 迁移完成
-- ============================================================================
