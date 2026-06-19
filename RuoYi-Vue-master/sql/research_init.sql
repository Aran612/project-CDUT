-- ----------------------------
-- 科研管理系统初始化SQL
-- 包含：教授信息表、论文等级字典表、论文主表、积分规则表
-- ----------------------------

-- ----------------------------
-- 1. 教授信息表
-- ----------------------------
DROP TABLE IF EXISTS `ms_professor`;
CREATE TABLE `ms_professor` (
  `professor_id` bigint NOT NULL AUTO_INCREMENT COMMENT '教授ID',
  `user_id` bigint DEFAULT NULL COMMENT '关联系统用户ID',
  `name` varchar(50) NOT NULL COMMENT '姓名',
  `department` varchar(200) DEFAULT NULL COMMENT '所属系/部门',
  `title` varchar(50) DEFAULT NULL COMMENT '职称',
  `research_field` varchar(300) DEFAULT NULL COMMENT '研究方向',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `total_score` int DEFAULT 0 COMMENT '累计科研积分',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`professor_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='教授信息表';

-- ----------------------------
-- 2. 论文等级字典表
-- ----------------------------
DROP TABLE IF EXISTS `ms_paper_grade`;
CREATE TABLE `ms_paper_grade` (
  `grade_id` bigint NOT NULL AUTO_INCREMENT COMMENT '等级ID',
  `grade_code` varchar(20) NOT NULL COMMENT '等级编码',
  `grade_name` varchar(50) NOT NULL COMMENT '等级名称',
  `base_score` int NOT NULL DEFAULT 0 COMMENT '基础积分',
  `sort_order` int DEFAULT 0 COMMENT '排序',
  `for_evaluation_only` char(1) DEFAULT '0' COMMENT '仅用于岗位聘期考核（0否 1是）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`grade_id`),
  UNIQUE KEY `uk_grade_code` (`grade_code`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='论文等级字典表';

-- 预置论文等级数据（依据：成理校发〔2025〕8号 第八条 研究论文）
INSERT INTO `ms_paper_grade` (`grade_id`, `grade_code`, `grade_name`, `base_score`, `sort_order`, `for_evaluation_only`, `status`, `create_by`, `create_time`, `remark`) VALUES
(1,  'NSC',             'N/S/C正刊',              5000, 1,  '0', '0', 'admin', sysdate(), 'Nature/Science/Cell正刊'),
(2,  'NSC_SUB',         'N/S/C子刊/PNAS',          1000, 2,  '0', '0', 'admin', sysdate(), 'Nature/Science/Cell子刊，PNAS'),
(3,  'NI',              'NI指数期刊',               600, 3,  '0', '0', 'admin', sysdate(), 'Nature Index收录期刊'),
(4,  'SCIE_SSCI_Q1',    'SCIE/SSCI一区',            400, 4,  '0', '0', 'admin', sysdate(), '中科院大类一区期刊'),
(5,  'SCIE_SSCI_Q2',    'SCIE/SSCI二区',            200, 5,  '0', '0', 'admin', sysdate(), '中科院大类二区期刊'),
(6,  'CSSCI_A1',        'CSSCI A1级',              200, 6,  '0', '0', 'admin', sysdate(), '哲学社科类A1级期刊'),
(7,  'AHCI',            'A&HCI',                   150, 7,  '0', '0', 'admin', sysdate(), '艺术与人文科学引文索引'),
(8,  'SCIE_SSCI_Q3',    'SCIE/SSCI三区',            100, 8,  '1', '0', 'admin', sysdate(), '仅用于岗位聘期考核'),
(9,  'CSSCI_A2',        'CSSCI A2级/T1',           100, 9,  '0', '0', 'admin', sysdate(), '哲学社科类A2级期刊/国内顶级T1'),
(10, 'SCI_SSCI_OTHER',  'SCI/SSCI其他',             50, 10, '1', '0', 'admin', sysdate(), '仅用于岗位聘期考核'),
(11, 'CSSCI_B',         'CSSCI B级',                50, 11, '1', '0', 'admin', sysdate(), '人大复印资料全文转载/新华文摘论点摘要等，仅用于岗位聘期考核'),
(12, 'EI',              'EI收录',                   30, 12, '1', '0', 'admin', sysdate(), '工程索引核心期刊，仅用于岗位聘期考核'),
(13, 'CSSCI_EXT',       'CSSCI扩展版/北大核心',      30, 13, '1', '0', 'admin', sysdate(), '仅用于岗位聘期考核'),
(14, 'REGULAR',         '普通期刊',                  5, 14, '0', '0', 'admin', sysdate(), '其他公开发行期刊');

-- ----------------------------
-- 3. 论文主表
-- ----------------------------
DROP TABLE IF EXISTS `ms_paper`;
CREATE TABLE `ms_paper` (
  `paper_id` bigint NOT NULL AUTO_INCREMENT COMMENT '论文ID',
  `title` varchar(500) NOT NULL COMMENT '论文名称',
  `grade_id` bigint DEFAULT NULL COMMENT '论文等级ID',
  `professor_id` bigint DEFAULT NULL COMMENT '教授ID',
  `author_position` varchar(20) DEFAULT NULL COMMENT '作者身份（第一作者/通讯作者/其他）',
  `journal_name` varchar(300) DEFAULT NULL COMMENT '期刊/会议名称',
  `publish_date` date DEFAULT NULL COMMENT '发表时间',
  `doi` varchar(200) DEFAULT NULL COMMENT 'DOI号',
  `paper_url` varchar(500) DEFAULT NULL COMMENT '论文链接',
  `abstract_text` text COMMENT '摘要',
  `actual_score` int DEFAULT 0 COMMENT '实际积分',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`paper_id`),
  KEY `idx_grade_id` (`grade_id`),
  KEY `idx_professor_id` (`professor_id`),
  KEY `idx_publish_date` (`publish_date`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='论文主表';

-- ----------------------------
-- 4. 积分规则表
-- ----------------------------
DROP TABLE IF EXISTS `ms_scoring_rule`;
CREATE TABLE `ms_scoring_rule` (
  `rule_id` bigint NOT NULL AUTO_INCREMENT COMMENT '规则ID',
  `grade_id` bigint NOT NULL COMMENT '论文等级ID',
  `position_type` varchar(20) NOT NULL COMMENT '作者身份',
  `coefficient` decimal(3,2) NOT NULL DEFAULT 1.00 COMMENT '积分系数',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`rule_id`),
  KEY `idx_grade_id` (`grade_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='积分规则表';

-- 预置积分规则（所有等级 × 五种作者身份）
-- 依据：成理校发〔2025〕8号 — 第二、三作者分别取分值的1/2、1/3，通讯作者按第二作者计
INSERT INTO `ms_scoring_rule` (`grade_id`, `position_type`, `coefficient`, `status`, `create_by`, `create_time`, `remark`) VALUES
-- N/S/C正刊 (grade_id=1)
(1,  '第一作者',   1.00, '0', 'admin', sysdate(), 'NSC-第一作者'),
(1,  '通讯作者',   0.50, '0', 'admin', sysdate(), 'NSC-通讯作者(按第二作者计)'),
(1,  '第二作者',   0.50, '0', 'admin', sysdate(), 'NSC-第二作者'),
(1,  '第三作者',   0.33, '0', 'admin', sysdate(), 'NSC-第三作者'),
(1,  '其他作者',   0.20, '0', 'admin', sysdate(), 'NSC-其他作者'),
-- N/S/C子刊/PNAS (grade_id=2)
(2,  '第一作者',   1.00, '0', 'admin', sysdate(), 'NSC子刊-第一作者'),
(2,  '通讯作者',   0.50, '0', 'admin', sysdate(), 'NSC子刊-通讯作者(按第二作者计)'),
(2,  '第二作者',   0.50, '0', 'admin', sysdate(), 'NSC子刊-第二作者'),
(2,  '第三作者',   0.33, '0', 'admin', sysdate(), 'NSC子刊-第三作者'),
(2,  '其他作者',   0.20, '0', 'admin', sysdate(), 'NSC子刊-其他作者'),
-- NI指数期刊 (grade_id=3)
(3,  '第一作者',   1.00, '0', 'admin', sysdate(), 'NI指数-第一作者'),
(3,  '通讯作者',   0.50, '0', 'admin', sysdate(), 'NI指数-通讯作者(按第二作者计)'),
(3,  '第二作者',   0.50, '0', 'admin', sysdate(), 'NI指数-第二作者'),
(3,  '第三作者',   0.33, '0', 'admin', sysdate(), 'NI指数-第三作者'),
(3,  '其他作者',   0.20, '0', 'admin', sysdate(), 'NI指数-其他作者'),
-- SCIE/SSCI一区 (grade_id=4)
(4,  '第一作者',   1.00, '0', 'admin', sysdate(), 'SCIE/SSCI一区-第一作者'),
(4,  '通讯作者',   0.50, '0', 'admin', sysdate(), 'SCIE/SSCI一区-通讯作者(按第二作者计)'),
(4,  '第二作者',   0.50, '0', 'admin', sysdate(), 'SCIE/SSCI一区-第二作者'),
(4,  '第三作者',   0.33, '0', 'admin', sysdate(), 'SCIE/SSCI一区-第三作者'),
(4,  '其他作者',   0.20, '0', 'admin', sysdate(), 'SCIE/SSCI一区-其他作者'),
-- SCIE/SSCI二区 (grade_id=5)
(5,  '第一作者',   1.00, '0', 'admin', sysdate(), 'SCIE/SSCI二区-第一作者'),
(5,  '通讯作者',   0.50, '0', 'admin', sysdate(), 'SCIE/SSCI二区-通讯作者(按第二作者计)'),
(5,  '第二作者',   0.50, '0', 'admin', sysdate(), 'SCIE/SSCI二区-第二作者'),
(5,  '第三作者',   0.33, '0', 'admin', sysdate(), 'SCIE/SSCI二区-第三作者'),
(5,  '其他作者',   0.20, '0', 'admin', sysdate(), 'SCIE/SSCI二区-其他作者'),
-- CSSCI A1级 (grade_id=6)
(6,  '第一作者',   1.00, '0', 'admin', sysdate(), 'CSSCI A1-第一作者'),
(6,  '通讯作者',   0.50, '0', 'admin', sysdate(), 'CSSCI A1-通讯作者(按第二作者计)'),
(6,  '第二作者',   0.50, '0', 'admin', sysdate(), 'CSSCI A1-第二作者'),
(6,  '第三作者',   0.33, '0', 'admin', sysdate(), 'CSSCI A1-第三作者'),
(6,  '其他作者',   0.20, '0', 'admin', sysdate(), 'CSSCI A1-其他作者'),
-- A&HCI (grade_id=7)
(7,  '第一作者',   1.00, '0', 'admin', sysdate(), 'A&HCI-第一作者'),
(7,  '通讯作者',   0.50, '0', 'admin', sysdate(), 'A&HCI-通讯作者(按第二作者计)'),
(7,  '第二作者',   0.50, '0', 'admin', sysdate(), 'A&HCI-第二作者'),
(7,  '第三作者',   0.33, '0', 'admin', sysdate(), 'A&HCI-第三作者'),
(7,  '其他作者',   0.20, '0', 'admin', sysdate(), 'A&HCI-其他作者'),
-- SCIE/SSCI三区 (grade_id=8)
(8,  '第一作者',   1.00, '0', 'admin', sysdate(), 'SCIE/SSCI三区-第一作者'),
(8,  '通讯作者',   0.50, '0', 'admin', sysdate(), 'SCIE/SSCI三区-通讯作者(按第二作者计)'),
(8,  '第二作者',   0.50, '0', 'admin', sysdate(), 'SCIE/SSCI三区-第二作者'),
(8,  '第三作者',   0.33, '0', 'admin', sysdate(), 'SCIE/SSCI三区-第三作者'),
(8,  '其他作者',   0.20, '0', 'admin', sysdate(), 'SCIE/SSCI三区-其他作者'),
-- CSSCI A2级/T1 (grade_id=9)
(9,  '第一作者',   1.00, '0', 'admin', sysdate(), 'CSSCI A2/T1-第一作者'),
(9,  '通讯作者',   0.50, '0', 'admin', sysdate(), 'CSSCI A2/T1-通讯作者(按第二作者计)'),
(9,  '第二作者',   0.50, '0', 'admin', sysdate(), 'CSSCI A2/T1-第二作者'),
(9,  '第三作者',   0.33, '0', 'admin', sysdate(), 'CSSCI A2/T1-第三作者'),
(9,  '其他作者',   0.20, '0', 'admin', sysdate(), 'CSSCI A2/T1-其他作者'),
-- SCI/SSCI其他 (grade_id=10)
(10, '第一作者',   1.00, '0', 'admin', sysdate(), 'SCI/SSCI其他-第一作者'),
(10, '通讯作者',   0.50, '0', 'admin', sysdate(), 'SCI/SSCI其他-通讯作者(按第二作者计)'),
(10, '第二作者',   0.50, '0', 'admin', sysdate(), 'SCI/SSCI其他-第二作者'),
(10, '第三作者',   0.33, '0', 'admin', sysdate(), 'SCI/SSCI其他-第三作者'),
(10, '其他作者',   0.20, '0', 'admin', sysdate(), 'SCI/SSCI其他-其他作者'),
-- CSSCI B级 (grade_id=11)
(11, '第一作者',   1.00, '0', 'admin', sysdate(), 'CSSCI B级-第一作者'),
(11, '通讯作者',   0.50, '0', 'admin', sysdate(), 'CSSCI B级-通讯作者(按第二作者计)'),
(11, '第二作者',   0.50, '0', 'admin', sysdate(), 'CSSCI B级-第二作者'),
(11, '第三作者',   0.33, '0', 'admin', sysdate(), 'CSSCI B级-第三作者'),
(11, '其他作者',   0.20, '0', 'admin', sysdate(), 'CSSCI B级-其他作者'),
-- EI收录 (grade_id=12)
(12, '第一作者',   1.00, '0', 'admin', sysdate(), 'EI收录-第一作者'),
(12, '通讯作者',   0.50, '0', 'admin', sysdate(), 'EI收录-通讯作者(按第二作者计)'),
(12, '第二作者',   0.50, '0', 'admin', sysdate(), 'EI收录-第二作者'),
(12, '第三作者',   0.33, '0', 'admin', sysdate(), 'EI收录-第三作者'),
(12, '其他作者',   0.20, '0', 'admin', sysdate(), 'EI收录-其他作者'),
-- CSSCI扩展版/北大核心 (grade_id=13)
(13, '第一作者',   1.00, '0', 'admin', sysdate(), 'CSSCI扩展/北大核心-第一作者'),
(13, '通讯作者',   0.50, '0', 'admin', sysdate(), 'CSSCI扩展/北大核心-通讯作者(按第二作者计)'),
(13, '第二作者',   0.50, '0', 'admin', sysdate(), 'CSSCI扩展/北大核心-第二作者'),
(13, '第三作者',   0.33, '0', 'admin', sysdate(), 'CSSCI扩展/北大核心-第三作者'),
(13, '其他作者',   0.20, '0', 'admin', sysdate(), 'CSSCI扩展/北大核心-其他作者'),
-- 普通期刊 (grade_id=14)
(14, '第一作者',   1.00, '0', 'admin', sysdate(), '普通期刊-第一作者'),
(14, '通讯作者',   0.50, '0', 'admin', sysdate(), '普通期刊-通讯作者(按第二作者计)'),
(14, '第二作者',   0.50, '0', 'admin', sysdate(), '普通期刊-第二作者'),
(14, '第三作者',   0.33, '0', 'admin', sysdate(), '普通期刊-第三作者'),
(14, '其他作者',   0.20, '0', 'admin', sysdate(), '普通期刊-其他作者');

-- ----------------------------
-- 5. 科研管理系统菜单
-- ----------------------------
-- 一级菜单
INSERT INTO `sys_menu` VALUES ('2000', '科研管理', '0', '9', 'research', NULL, '', '', 1, 0, 'M', '0', '0', '', 'chart', 'admin', sysdate(), '', NULL, '科研管理目录');

-- 二级菜单
INSERT INTO `sys_menu` VALUES ('2001', '教授管理', '2000', '1', 'professor', 'research/professor/index', '', '', 1, 0, 'C', '0', '0', 'research:professor:list', 'user', 'admin', sysdate(), '', NULL, '教授管理菜单');
INSERT INTO `sys_menu` VALUES ('2002', '论文管理', '2000', '2', 'paper',     'research/paper/index',     '', '', 1, 0, 'C', '0', '0', 'research:paper:list',     'document', 'admin', sysdate(), '', NULL, '论文管理菜单');
INSERT INTO `sys_menu` VALUES ('2003', '论文等级', '2000', '3', 'grade',     'research/grade/index',     '', '', 1, 0, 'C', '0', '0', 'research:grade:list',     'star', 'admin', sysdate(), '', NULL, '论文等级菜单');
INSERT INTO `sys_menu` VALUES ('2004', '科研统计', '2000', '4', 'stats',     'research/stats/index',     '', '', 1, 0, 'C', '0', '0', 'research:stats:view',     'data', 'admin', sysdate(), '', NULL, '科研统计菜单');

-- 教授管理按钮
INSERT INTO `sys_menu` VALUES ('2005', '教授查询', '2001', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'research:professor:query',  '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2006', '教授新增', '2001', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'research:professor:add',    '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2007', '教授修改', '2001', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'research:professor:edit',   '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2008', '教授删除', '2001', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'research:professor:remove', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2009', '教授导出', '2001', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'research:professor:export', '#', 'admin', sysdate(), '', NULL, '');

-- 论文管理按钮
INSERT INTO `sys_menu` VALUES ('2010', '论文查询', '2002', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'research:paper:query',  '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2011', '论文新增', '2002', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'research:paper:add',    '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2012', '论文修改', '2002', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'research:paper:edit',   '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2013', '论文删除', '2002', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'research:paper:remove', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2014', '论文导出', '2002', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'research:paper:export', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2015', '论文导入', '2002', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'research:paper:import', '#', 'admin', sysdate(), '', NULL, '');

-- 论文等级按钮
INSERT INTO `sys_menu` VALUES ('2016', '等级查询', '2003', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'research:grade:query',  '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2017', '等级新增', '2003', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'research:grade:add',    '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2018', '等级修改', '2003', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'research:grade:edit',   '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2019', '等级删除', '2003', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'research:grade:remove', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2020', '等级导出', '2003', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'research:grade:export', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2021', '统计查看', '2004', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'research:stats:view',   '#', 'admin', sysdate(), '', NULL, '');

-- ----------------------------
-- 6. 科研项目等级字典表
-- ----------------------------
DROP TABLE IF EXISTS `ms_project_grade`;
CREATE TABLE `ms_project_grade` (
  `grade_id` bigint NOT NULL AUTO_INCREMENT COMMENT '等级ID',
  `grade_code` varchar(30) NOT NULL COMMENT '等级编码',
  `grade_name` varchar(100) NOT NULL COMMENT '等级名称',
  `base_score` int NOT NULL DEFAULT 0 COMMENT '基础积分',
  `project_category` varchar(20) DEFAULT NULL COMMENT '项目类别（自然科学/人文社科）',
  `sort_order` int DEFAULT 0 COMMENT '排序',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`grade_id`),
  UNIQUE KEY `uk_grade_code` (`grade_code`)
) ENGINE=InnoDB AUTO_INCREMENT=200 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='科研项目等级字典表';

-- 预置项目等级数据（依据：成理校发〔2021〕51号 第四条）
INSERT INTO `ms_project_grade` (`grade_id`, `grade_code`, `grade_name`, `base_score`, `project_category`, `sort_order`, `status`, `create_by`, `create_time`, `remark`) VALUES
-- 自然科学类
(1,  'NSFC_MAJOR',      '国自然重大项目/创新群体/基础科学中心',     5000, '自然科学', 1,  '0', 'admin', sysdate(), '经费≥1000万'),
(2,  'NSFC_DISTINGUISH', '国家杰出青年科学基金',                      3000, '自然科学', 2,  '0', 'admin', sysdate(), ''),
(3,  'NSFC_EXCELLENT',   '国家优秀(海外)青年科学基金',                2000, '自然科学', 3,  '0', 'admin', sysdate(), ''),
(4,  'NSFC_KEY',         '国自然重点/国际合作/重大研究计划/重大仪器',  800,  '自然科学', 4,  '0', 'admin', sysdate(), '经费200-1000万'),
(5,  'NSFC_GENERAL',     '国自然面上/青年/地区/外国青年学者',          300,  '自然科学', 5,  '0', 'admin', sysdate(), '经费20-200万'),
(6,  'NSFC_EMERGENCY',   '国自然应急管理/合作交流/数学天元',           100,  '自然科学', 6,  '0', 'admin', sysdate(), '经费<20万'),
(7,  'NKRDP_PROJECT',    '国家重点研发计划/重大专项(项目级)',          400,  '自然科学', 7,  '0', 'admin', sysdate(), ''),
(8,  'NKRDP_TOPIC',      '国家重点研发计划/重大专项(课题级)',          400,  '自然科学', 8,  '0', 'admin', sysdate(), '经费≥400万'),
(9,  'NKRDP_SUB2',       '国家重点研发计划专题(200-400万)',            200,  '自然科学', 9,  '0', 'admin', sysdate(), ''),
(10, 'NKRDP_SUB1',       '国家重点研发计划专题(100-200万)',            200,  '自然科学', 10, '0', 'admin', sysdate(), ''),
(11, 'NKRDP_SUB0',       '国家重点研发计划专题(<100万)',               100,  '自然科学', 11, '0', 'admin', sysdate(), ''),
(12, 'DEFENSE_173',      '国防科技173计划技术领域基金(一般项目)',      300,  '自然科学', 12, '0', 'admin', sysdate(), ''),
(13, 'MINISTRY_300',     '其他国家部委局科技项目(≥300万)',             300,  '自然科学', 13, '0', 'admin', sysdate(), ''),
(14, 'MINISTRY_100',     '其他国家部委局科技项目(<100万)',              75,  '自然科学', 14, '0', 'admin', sysdate(), ''),
(15, 'MOST_PLATFORM',    '科技部基地和人才/国际合作专项',              200,  '自然科学', 15, '0', 'admin', sysdate(), ''),
(16, 'PROV_TEAM',        '省科技厅科技创新研究团队',                   80,  '自然科学', 16, '0', 'admin', sysdate(), ''),
(17, 'PROV_YOUTH_TEAM',  '省科技厅青年科技创新研究团队(培育)',         30,  '自然科学', 17, '0', 'admin', sysdate(), ''),
(18, 'PROV_DISTINGUISH', '省杰出青年科技人才',                         60,  '自然科学', 18, '0', 'admin', sysdate(), ''),
(19, 'PROV_300',         '省科技厅项目(≥300万)',                      300,  '自然科学', 19, '0', 'admin', sysdate(), ''),
(20, 'PROV_150',         '省科技厅项目(100-300万)',                   150,  '自然科学', 20, '0', 'admin', sysdate(), ''),
(21, 'PROV_75',          '省科技厅/成都市项目(50-100万)',              75,  '自然科学', 21, '0', 'admin', sysdate(), ''),
(22, 'PROV_30',          '省科技厅项目(<50万)',                        30,  '自然科学', 22, '0', 'admin', sysdate(), ''),
(23, 'OTHER_300',        '其他纵向项目(≥300万)',                      300,  '自然科学', 23, '0', 'admin', sysdate(), ''),
(24, 'OTHER_100',        '其他纵向项目(100-300万)',                   100,  '自然科学', 24, '0', 'admin', sysdate(), ''),
(25, 'OTHER_50',         '其他纵向项目(50-100万)',                     50,  '自然科学', 25, '0', 'admin', sysdate(), ''),
(26, 'OTHER_25',         '其他纵向项目(25-50万)',                      25,  '自然科学', 26, '0', 'admin', sysdate(), ''),
(27, 'HORIZONTAL',       '横向项目(每万元0.75分)',                      0,  '自然科学', 27, '0', 'admin', sysdate(), '按到账经费每万元计0.75分'),
-- 人文社科类
(28, 'NSSF_MAJOR',       '国家社科基金重大招标项目',                  2000, '人文社科', 28, '0', 'admin', sysdate(), '含专项重大/冷门绝学团队'),
(29, 'NSSF_KEY',         '国家社科基金重点项目',                       800,  '人文社科', 29, '0', 'admin', sysdate(), '含后期资助重点/单列学科重点'),
(30, 'NSSF_GENERAL',     '国家社科基金一般/青年/西部/后期资助',        300,  '人文社科', 30, '0', 'admin', sysdate(), '含专项工程一般/冷门绝学个人'),
(31, 'NSSF_SUB',         '国家社科基金重大项目子题',                   100,  '人文社科', 31, '0', 'admin', sysdate(), ''),
(32, 'MOE_MAJOR',        '教育部哲学社科重大课题攻关项目',            2000,  '人文社科', 32, '0', 'admin', sysdate(), ''),
(33, 'MOE_KEY',          '教育部后期资助重大/重点基地重大',            300,  '人文社科', 33, '0', 'admin', sysdate(), ''),
(34, 'MOE_GENERAL',      '教育部人文社科一般项目',                     100,  '人文社科', 34, '0', 'admin', sysdate(), '含规划基金/青年/西部等'),
(35, 'MINISTRY_SOCIAL',  '中央其他部委局社科项目',                     100,  '人文社科', 35, '0', 'admin', sysdate(), ''),
(36, 'PROV_SS_MAJOR',    '省社科规划重大项目',                         50,  '人文社科', 36, '0', 'admin', sysdate(), ''),
(37, 'PROV_SS_KEY',      '省社科规划重点项目',                         30,  '人文社科', 37, '0', 'admin', sysdate(), ''),
(38, 'PROV_SS_GENERAL',  '省社科规划一般项目',                         20,  '人文社科', 38, '0', 'admin', sysdate(), '含年度/普及/后期资助/专项'),
(39, 'PROV_SOFT_MAJOR',  '省科技厅软科学重大/工作支撑类',             100,  '人文社科', 39, '0', 'admin', sysdate(), ''),
(40, 'PROV_SOFT_GENERAL','省科技厅软科学重点/面上',                    50,  '人文社科', 40, '0', 'admin', sysdate(), ''),
(41, 'OTHER_PROV_SOCIAL','其他省级社科项目',                           20,  '人文社科', 41, '0', 'admin', sysdate(), '含发改委/政务调研/智库/教育科研等'),
(42, 'CITY_SOCIAL',      '成都市社科联/科技局项目',                    20,  '人文社科', 42, '0', 'admin', sysdate(), ''),
(43, 'PLATFORM_SOCIAL',  '各类研究平台社科项目',                       10,  '人文社科', 43, '0', 'admin', sysdate(), '省级哲社重点基地开放/省部级实验室开放'),
(44, 'SOCIAL_HORIZONTAL','人文社科横向项目(每万元1分)',                  0,  '人文社科', 44, '0', 'admin', sysdate(), '按到账经费每万元计1分');

-- ----------------------------
-- 7. 科研项目主表
-- ----------------------------
DROP TABLE IF EXISTS `ms_research_project`;
CREATE TABLE `ms_research_project` (
  `project_id` bigint NOT NULL AUTO_INCREMENT COMMENT '项目ID',
  `project_name` varchar(500) NOT NULL COMMENT '项目名称',
  `grade_id` bigint DEFAULT NULL COMMENT '项目等级ID',
  `professor_id` bigint DEFAULT NULL COMMENT '负责人教授ID',
  `role` varchar(20) DEFAULT '主持' COMMENT '承担角色（主持/参与）',
  `funding_amount` decimal(12,2) DEFAULT 0 COMMENT '经费(万元)',
  `approval_number` varchar(100) DEFAULT NULL COMMENT '批准号',
  `project_source` varchar(300) DEFAULT NULL COMMENT '项目来源',
  `start_date` date DEFAULT NULL COMMENT '开始日期',
  `end_date` date DEFAULT NULL COMMENT '结束日期',
  `actual_score` int DEFAULT 0 COMMENT '实际积分',
  `status` char(1) DEFAULT '0' COMMENT '状态（0在研 1结题 2终止）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`project_id`),
  KEY `idx_grade_id` (`grade_id`),
  KEY `idx_professor_id` (`professor_id`),
  KEY `idx_start_date` (`start_date`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='科研项目主表';

-- ----------------------------
-- 8. 科研项目菜单
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('2100', '科研项目', '2000', '5', 'project', 'research/project/index', '', '', 1, 0, 'C', '0', '0', 'research:project:list', 'component', 'admin', sysdate(), '', NULL, '科研项目菜单');
INSERT INTO `sys_menu` VALUES ('2101', '项目查询', '2100', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'research:project:query',  '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2102', '项目新增', '2100', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'research:project:add',    '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2103', '项目修改', '2100', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'research:project:edit',   '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2104', '项目删除', '2100', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'research:project:remove', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2105', '项目导出', '2100', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'research:project:export', '#', 'admin', sysdate(), '', NULL, '');

-- 项目等级管理菜单
INSERT INTO `sys_menu` VALUES ('2110', '项目等级', '2000', '6', 'projectGrade', 'research/projectGrade/index', '', '', 1, 0, 'C', '0', '0', 'research:projectGrade:list', 'star', 'admin', sysdate(), '', NULL, '项目等级菜单');
INSERT INTO `sys_menu` VALUES ('2111', '等级查询', '2110', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'research:projectGrade:query',  '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2112', '等级新增', '2110', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'research:projectGrade:add',    '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2113', '等级修改', '2110', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'research:projectGrade:edit',   '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2114', '等级删除', '2110', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'research:projectGrade:remove', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2115', '等级导出', '2110', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'research:projectGrade:export', '#', 'admin', sysdate(), '', NULL, '');

-- ----------------------------
-- 9. 专利等级字典表
-- ----------------------------
DROP TABLE IF EXISTS `ms_patent_grade`;
CREATE TABLE `ms_patent_grade` (
  `grade_id` bigint NOT NULL AUTO_INCREMENT COMMENT '等级ID',
  `grade_code` varchar(30) NOT NULL COMMENT '等级编码',
  `grade_name` varchar(100) NOT NULL COMMENT '等级名称',
  `base_score` int NOT NULL DEFAULT 0 COMMENT '基础积分',
  `for_evaluation_only` char(1) DEFAULT '0' COMMENT '仅用于岗位聘期考核（0否 1是）',
  `sort_order` int DEFAULT 0 COMMENT '排序',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`grade_id`),
  UNIQUE KEY `uk_grade_code` (`grade_code`)
) ENGINE=InnoDB AUTO_INCREMENT=300 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='专利等级字典表';

-- 预置专利等级数据（依据：成理校发〔2025〕8号 第七条 知识产权）
INSERT INTO `ms_patent_grade` (`grade_id`, `grade_code`, `grade_name`, `base_score`, `for_evaluation_only`, `sort_order`, `status`, `create_by`, `create_time`, `remark`) VALUES
(1, 'PATENT_US_JP_KR_EP',  '美日韩欧授权发明专利',            200, '0', 1, '0', 'admin', sysdate(), '每获得一个国家授权'),
(2, 'PATENT_CN_INVENT',    '中国授权发明专利',                30, '1', 2, '0', 'admin', sysdate(), '仅用于岗位聘期考核'),
(3, 'PATENT_OTHER_INTL',   '其他涉外授权发明专利',            30,  '0', 3, '0', 'admin', sysdate(), ''),
(4, 'PATENT_UTILITY',      '实用新型专利',                    15,  '0', 4, '0', 'admin', sysdate(), ''),
(5, 'PATENT_DESIGN',       '外观设计专利',                    10,  '0', 5, '0', 'admin', sysdate(), ''),
(6, 'SOFTWARE_COPYRIGHT',  '计算机软件著作权',                 10,  '0', 6, '0', 'admin', sysdate(), ''),
(7, 'IC_LAYOUT',           '集成电路布图设计',                 10,  '0', 7, '0', 'admin', sysdate(), '');

-- ----------------------------
-- 10. 专利主表
-- ----------------------------
DROP TABLE IF EXISTS `ms_patent`;
CREATE TABLE `ms_patent` (
  `patent_id` bigint NOT NULL AUTO_INCREMENT COMMENT '专利ID',
  `patent_name` varchar(500) NOT NULL COMMENT '专利名称',
  `grade_id` bigint DEFAULT NULL COMMENT '专利等级ID',
  `professor_id` bigint DEFAULT NULL COMMENT '发明人教授ID',
  `inventor_rank` varchar(20) DEFAULT '第一发明人' COMMENT '发明人排名',
  `patent_number` varchar(100) DEFAULT NULL COMMENT '专利号',
  `patent_country` varchar(50) DEFAULT '中国' COMMENT '授权国家/地区',
  `application_date` date DEFAULT NULL COMMENT '申请日期',
  `grant_date` date DEFAULT NULL COMMENT '授权日期',
  `actual_score` int DEFAULT 0 COMMENT '实际积分',
  `status` char(1) DEFAULT '0' COMMENT '状态（0有效 1失效）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`patent_id`),
  KEY `idx_grade_id` (`grade_id`),
  KEY `idx_professor_id` (`professor_id`),
  KEY `idx_grant_date` (`grant_date`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='专利主表';

-- ----------------------------
-- 11. 专利管理菜单
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('2200', '专利管理', '2000', '7', 'patent', 'research/patent/index', '', '', 1, 0, 'C', '0', '0', 'research:patent:list', 'lock', 'admin', sysdate(), '', NULL, '专利管理菜单');
INSERT INTO `sys_menu` VALUES ('2201', '专利查询', '2200', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'research:patent:query',  '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2202', '专利新增', '2200', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'research:patent:add',    '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2203', '专利修改', '2200', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'research:patent:edit',   '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2204', '专利删除', '2200', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'research:patent:remove', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2205', '专利导出', '2200', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'research:patent:export', '#', 'admin', sysdate(), '', NULL, '');

-- ----------------------------
-- 12. 著作等级字典表
-- ----------------------------
DROP TABLE IF EXISTS `ms_book_grade`;
CREATE TABLE `ms_book_grade` (
  `grade_id` bigint NOT NULL AUTO_INCREMENT COMMENT '等级ID',
  `grade_code` varchar(30) NOT NULL COMMENT '等级编码',
  `grade_name` varchar(120) NOT NULL COMMENT '等级名称',
  `base_score` int NOT NULL DEFAULT 0 COMMENT '基础积分',
  `category` varchar(20) DEFAULT NULL COMMENT '学科类别（自然科学/人文社科/通用）',
  `sort_order` int DEFAULT 0 COMMENT '排序',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`grade_id`),
  UNIQUE KEY `uk_grade_code` (`grade_code`)
) ENGINE=InnoDB AUTO_INCREMENT=300 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='著作等级字典表';

-- 预置著作等级数据（依据：成理校发〔2025〕8号 第六条 + 成理校发〔2021〕51号 第七条）
INSERT INTO `ms_book_grade` (`grade_id`, `grade_code`, `grade_name`, `base_score`, `category`, `sort_order`, `status`, `create_by`, `create_time`, `remark`) VALUES
(1,  'BOOK_THEME_PUB',      '中宣部主题出版重点出版物',          500, '人文社科', 1, '0', 'admin', sysdate(), ''),
(2,  'BOOK_NATL_PREMIUM',    '中国出版政府奖/国家哲社成果文库',  300, '人文社科', 2, '0', 'admin', sysdate(), '含结项优秀之国家社科重大/教育部重大出版的著作'),
(3,  'BOOK_NATL_KEY',        '国家重点出版物规划/思政文库',      300, '人文社科', 3, '0', 'admin', sysdate(), '含结项优秀之国社科重点/冷门绝学团队等出版的著作'),
(4,  'BOOK_INTL_KNOWN',      '国外知名出版社出版著作',           200, '通用',    4, '0', 'admin', sysdate(), '人文社科需≥10万字'),
(5,  'BOOK_NATL_FUNDED',     '国家出版基金/丝路书香等资助出版物', 200, '人文社科', 5, '0', 'admin', sysdate(), '含国家科学技术学术著作出版基金资助'),
(6,  'BOOK_NATL_GENERAL',    '国社科一般/青年/西部项目等出版著作', 200, '人文社科', 6, '0', 'admin', sysdate(), '结项优秀'),
(7,  'BOOK_CENTRAL_PUB',     '中央级/优秀出版社出版著作',        100, '通用',    7, '0', 'admin', sysdate(), '人文社科需≥10万字'),
(8,  'BOOK_OTHER_PUB',       '国内其他出版社出版专著',            50,  '通用',    8, '0', 'admin', sysdate(), '人文社科需≥10万字'),
(9,  'BOOK_OTHER_WORK',      '国内其他出版社其他著作类成果',      50,  '通用',    9, '0', 'admin', sysdate(), '专著以外的著作'),
(10, 'BOOK_COMPILATION',     '编著',                              20,  '通用',    10, '0', 'admin', sysdate(), '境内出版社出版编著'),
(11, 'BOOK_TEXTBOOK_NATL',   '国家级规划教材',                   200,  '通用',    11, '0', 'admin', sysdate(), ''),
(12, 'BOOK_TEXTBOOK_PROV',   '省部级规划教材',                   100,  '通用',    12, '0', 'admin', sysdate(), ''),
(13, 'BOOK_TEXTBOOK_OTHER',  '其他正式出版教材',                  30,  '通用',    13, '0', 'admin', sysdate(), '');

-- ----------------------------
-- 13. 著作主表
-- ----------------------------
DROP TABLE IF EXISTS `ms_book`;
CREATE TABLE `ms_book` (
  `book_id` bigint NOT NULL AUTO_INCREMENT COMMENT '著作ID',
  `book_name` varchar(500) NOT NULL COMMENT '著作名称',
  `grade_id` bigint DEFAULT NULL COMMENT '著作等级ID',
  `professor_id` bigint DEFAULT NULL COMMENT '作者教授ID',
  `author_role` varchar(20) DEFAULT '著' COMMENT '作者角色（著/编著/译/主编/副主编/参编）',
  `publisher` varchar(300) DEFAULT NULL COMMENT '出版社',
  `publish_date` date DEFAULT NULL COMMENT '出版日期',
  `word_count` int DEFAULT 0 COMMENT '字数(万)',
  `isbn` varchar(50) DEFAULT NULL COMMENT 'ISBN号',
  `actual_score` int DEFAULT 0 COMMENT '实际积分',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`book_id`),
  KEY `idx_grade_id` (`grade_id`),
  KEY `idx_professor_id` (`professor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='著作主表';

-- 著作管理菜单
INSERT INTO `sys_menu` VALUES ('2300', '著作管理', '2000', '8', 'book', 'research/book/index', '', '', 1, 0, 'C', '0', '0', 'research:book:list', 'notebook', 'admin', sysdate(), '', NULL, '著作管理菜单');
INSERT INTO `sys_menu` VALUES ('2301', '著作查询', '2300', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'research:book:query',  '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2302', '著作新增', '2300', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'research:book:add',    '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2303', '著作修改', '2300', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'research:book:edit',   '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2304', '著作删除', '2300', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'research:book:remove', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2305', '著作导出', '2300', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'research:book:export', '#', 'admin', sysdate(), '', NULL, '');

-- ----------------------------
-- 14. 获奖等级字典表
-- ----------------------------
DROP TABLE IF EXISTS `ms_award_grade`;
CREATE TABLE `ms_award_grade` (
  `grade_id` bigint NOT NULL AUTO_INCREMENT COMMENT '等级ID',
  `grade_code` varchar(30) NOT NULL COMMENT '等级编码',
  `grade_name` varchar(120) NOT NULL COMMENT '等级名称',
  `base_score` int NOT NULL DEFAULT 0 COMMENT '基础积分',
  `award_category` varchar(30) DEFAULT NULL COMMENT '获奖大类（自然科学/人文社科/教学成果/专利奖/艺术体育）',
  `sort_order` int DEFAULT 0 COMMENT '排序',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`grade_id`),
  UNIQUE KEY `uk_grade_code` (`grade_code`)
) ENGINE=InnoDB AUTO_INCREMENT=400 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='获奖等级字典表';

-- 预置获奖等级数据（依据：成理校发〔2025〕8号 第五条/第九条/第十条）
INSERT INTO `ms_award_grade` (`grade_id`, `grade_code`, `grade_name`, `base_score`, `award_category`, `sort_order`, `status`, `create_by`, `create_time`, `remark`) VALUES
-- 自然科学类成果奖
(1,  'AWARD_NS_SPECIAL',      '国家自然科学/技术发明/科技进步奖 特等奖', 10000, '自然科学', 1, '0', 'admin', sysdate(), ''),
(2,  'AWARD_NS_FIRST',        '国家自然科学/技术发明/科技进步奖 一等奖',  5000,  '自然科学', 2, '0', 'admin', sysdate(), ''),
(3,  'AWARD_NS_SECOND',       '国家自然科学/技术发明/科技进步奖 二等奖',  3000,  '自然科学', 3, '0', 'admin', sysdate(), ''),
(4,  'AWARD_CNPATENT_GOLD',   '中国专利奖 金奖',                          3000,  '专利奖',   4, '0', 'admin', sysdate(), ''),
(5,  'AWARD_CNPATENT_SILVER', '中国专利奖 银奖',                          2000,  '专利奖',   5, '0', 'admin', sysdate(), ''),
(6,  'AWARD_CNPATENT_EXCEL',  '中国专利奖 优秀奖',                        1000,  '专利奖',   6, '0', 'admin', sysdate(), ''),
(7,  'AWARD_PROV_NS_SPECIAL', '省级自然科学/技术发明/科技进步奖 特等奖',   3000,  '自然科学', 7, '0', 'admin', sysdate(), ''),
(8,  'AWARD_PROV_NS_FIRST',   '省级自然科学/技术发明/科技进步奖 一等奖',   1000,  '自然科学', 8, '0', 'admin', sysdate(), ''),
(9,  'AWARD_PROV_NS_SECOND',  '省级自然科学/技术发明/科技进步奖 二等奖',   600,   '自然科学', 9, '0', 'admin', sysdate(), ''),
(10, 'AWARD_PROV_NS_THIRD',   '省级自然科学/技术发明/科技进步奖 三等奖',   400,   '自然科学', 10,'0', 'admin', sysdate(), ''),
(11, 'AWARD_PROV_PAT_SPECIAL','省部级专利奖 特等奖',                       800,   '专利奖',   11,'0', 'admin', sysdate(), ''),
(12, 'AWARD_PROV_PAT_FIRST',  '省部级专利奖 一等奖',                       500,   '专利奖',   12,'0', 'admin', sysdate(), ''),
(13, 'AWARD_PROV_PAT_SECOND', '省部级专利奖 二等奖',                       300,   '专利奖',   13,'0', 'admin', sysdate(), ''),
(14, 'AWARD_PROV_PAT_THIRD',  '省部级专利奖 三等奖',                       200,   '专利奖',   14,'0', 'admin', sysdate(), ''),
-- 人文社科类成果奖
(15, 'AWARD_MOE_SPECIAL',     '教育部高校科研优秀成果奖(人文社科) 特等奖', 6000,  '人文社科', 15,'0', 'admin', sysdate(), ''),
(16, 'AWARD_MOE_FIRST',       '教育部高校科研优秀成果奖(人文社科) 一等奖', 4000,  '人文社科', 16,'0', 'admin', sysdate(), ''),
(17, 'AWARD_MOE_SECOND',      '教育部高校科研优秀成果奖(人文社科) 二等奖', 2000,  '人文社科', 17,'0', 'admin', sysdate(), ''),
(18, 'AWARD_MOE_THIRD',       '教育部高校科研优秀成果奖(人文社科) 三等奖', 1000,  '人文社科', 18,'0', 'admin', sysdate(), '含青年成果奖'),
(19, 'AWARD_CENTRAL_SS_FIRST','中央部委社科优秀成果奖 特等/一等奖',        1500,  '人文社科', 19,'0', 'admin', sysdate(), ''),
(20, 'AWARD_PROV_SS_HONOR',   '省级社科优秀成果奖 荣誉奖',                 500,   '人文社科', 20,'0', 'admin', sysdate(), ''),
(21, 'AWARD_PROV_SS_FIRST',   '省级社科优秀成果奖 一等奖',                 300,   '人文社科', 21,'0', 'admin', sysdate(), ''),
(22, 'AWARD_PROV_SS_SECOND',  '省级社科优秀成果奖 二等奖',                 200,   '人文社科', 22,'0', 'admin', sysdate(), ''),
(23, 'AWARD_PROV_SS_THIRD',   '省级社科优秀成果奖 三等奖',                 100,   '人文社科', 23,'0', 'admin', sysdate(), ''),
(24, 'AWARD_CITY_SS_FIRST',   '副省级城市社科优秀成果奖 一等奖',           300,   '人文社科', 24,'0', 'admin', sysdate(), ''),
-- 教学成果奖
(25, 'AWARD_TEACH_NATL_SPEC', '国家教学成果奖 特等奖',                     20000, '教学成果', 25,'0', 'admin', sysdate(), ''),
(26, 'AWARD_TEACH_NATL_FIRST','国家教学成果奖 一等奖',                     10000, '教学成果', 26,'0', 'admin', sysdate(), ''),
(27, 'AWARD_TEACH_NATL_SEC',  '国家教学成果奖 二等奖',                      5000,  '教学成果', 27,'0', 'admin', sysdate(), ''),
(28, 'AWARD_TEACH_PROV_SPEC', '省教学成果奖/教育部教科项目 特等奖',         2000,  '教学成果', 28,'0', 'admin', sysdate(), ''),
(29, 'AWARD_TEACH_PROV_FIRST','省教学成果奖/教育部教科项目 一等奖',         1000,  '教学成果', 29,'0', 'admin', sysdate(), ''),
(30, 'AWARD_TEACH_PROV_SEC',  '省教学成果奖/教育部教科项目 二等奖',         600,   '教学成果', 30,'0', 'admin', sysdate(), ''),
(31, 'AWARD_TEACH_SOCIETY',   '中国高教学会/专业学位教指委/校级 特等奖',    300,   '教学成果', 31,'0', 'admin', sysdate(), ''),
(32, 'AWARD_TEACH_OTHER',     '其他教学研究成果奖',                         100,   '教学成果', 32,'0', 'admin', sysdate(), '');

-- ----------------------------
-- 15. 获奖主表
-- ----------------------------
DROP TABLE IF EXISTS `ms_award`;
CREATE TABLE `ms_award` (
  `award_id` bigint NOT NULL AUTO_INCREMENT COMMENT '获奖ID',
  `award_name` varchar(500) NOT NULL COMMENT '获奖名称',
  `grade_id` bigint DEFAULT NULL COMMENT '获奖等级ID',
  `professor_id` bigint DEFAULT NULL COMMENT '获奖人教授ID',
  `award_rank` varchar(20) DEFAULT '第一完成人' COMMENT '完成人排名',
  `award_org` varchar(300) DEFAULT NULL COMMENT '颁发单位',
  `award_date` date DEFAULT NULL COMMENT '获奖日期',
  `cert_number` varchar(100) DEFAULT NULL COMMENT '证书编号',
  `actual_score` int DEFAULT 0 COMMENT '实际积分',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`award_id`),
  KEY `idx_grade_id` (`grade_id`),
  KEY `idx_professor_id` (`professor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='获奖主表';

-- 获奖管理菜单
INSERT INTO `sys_menu` VALUES ('2400', '获奖管理', '2000', '9', 'award', 'research/award/index', '', '', 1, 0, 'C', '0', '0', 'research:award:list', 'medal', 'admin', sysdate(), '', NULL, '获奖管理菜单');
INSERT INTO `sys_menu` VALUES ('2401', '获奖查询', '2400', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'research:award:query',  '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2402', '获奖新增', '2400', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'research:award:add',    '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2403', '获奖修改', '2400', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'research:award:edit',   '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2404', '获奖删除', '2400', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'research:award:remove', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu` VALUES ('2405', '获奖导出', '2400', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'research:award:export', '#', 'admin', sysdate(), '', NULL, '');

-- 导入权限按钮（各子模块）
INSERT INTO `sys_menu` VALUES ('2500', '教授导入', '2001', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'research:professor:import', '#', 'admin', sysdate(), '', NULL, '教授导入按钮');
INSERT INTO `sys_menu` VALUES ('2502', '项目导入', '2100', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'research:project:import', '#', 'admin', sysdate(), '', NULL, '项目导入按钮');
INSERT INTO `sys_menu` VALUES ('2504', '专利导入', '2200', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'research:patent:import', '#', 'admin', sysdate(), '', NULL, '专利导入按钮');
INSERT INTO `sys_menu` VALUES ('2506', '著作导入', '2300', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'research:book:import', '#', 'admin', sysdate(), '', NULL, '著作导入按钮');
INSERT INTO `sys_menu` VALUES ('2508', '获奖导入', '2400', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'research:award:import', '#', 'admin', sysdate(), '', NULL, '获奖导入按钮');

-- 一键导入模块
INSERT INTO `sys_menu` VALUES ('2510', '一键导入', '2000', '11', 'batchImport', 'research/batchImport/index', '', '', 1, 0, 'C', '0', '0', 'research:batchImport:list', 'upload', 'admin', sysdate(), '', NULL, '一键导入菜单');
INSERT INTO `sys_menu` VALUES ('2511', '一键导入', '2510', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'research:batchImport:import', '#', 'admin', sysdate(), '', NULL, '一键导入按钮');
