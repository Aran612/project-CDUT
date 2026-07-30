-- ============================================================================
-- 学分管理模块数据库迁移脚本
-- 模块：师资分类、学分统计、专业汇总、研究型学者评估
-- ============================================================================

-- 1. 扩展教授信息表：添加师资类型字段
ALTER TABLE `ms_professor` ADD COLUMN `teacher_type` CHAR(1) DEFAULT '3' COMMENT '师资类型（1=研究型学者 2=教学型教师 3=双师型教师 4=行政兼职）' AFTER `title`;
ALTER TABLE `ms_professor` ADD INDEX `idx_teacher_type` (`teacher_type`);

-- 2. 更新现有教授的师资类型（根据职称默认分类）
-- 教授/副教授默认为研究型学者，讲师为双师型，助教为教学型
UPDATE `ms_professor` SET `teacher_type` = '1' WHERE `title` IN ('教授', '副教授') AND `teacher_type` IS NULL;
UPDATE `ms_professor` SET `teacher_type` = '3' WHERE `title` = '讲师' AND `teacher_type` IS NULL;
UPDATE `ms_professor` SET `teacher_type` = '2' WHERE `title` = '助教' AND `teacher_type` IS NULL;


-- ============================================================================
-- 3. 学分管理菜单与权限
-- ============================================================================

-- 一级菜单：学分管理
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `query_param`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
('学分管理', 0, 10, 'credit-management', NULL, NULL, NULL, 1, 0, 'M', '0', '0', '', 'education', 'admin', NOW(), '', NULL, '学分管理一级菜单');

SET @credit_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '学分管理' AND parent_id = 0 LIMIT 1);

-- 二级菜单：师资分类管理
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `query_param`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
('师资分类管理', @credit_menu_id, 1, 'teacher-type', 'research/credit/teacherType/index', NULL, NULL, 1, 0, 'C', '0', '0', 'credit:teacherType:list', 'peoples', 'admin', NOW(), '', NULL, '师资分类管理');

SET @teacher_type_menu_id = (SELECT menu_id FROM sys_menu WHERE menu_name = '师资分类管理' AND parent_id = @credit_menu_id LIMIT 1);

-- 师资分类管理按钮
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `query_param`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
('分类查询', @teacher_type_menu_id, 1, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'credit:teacherType:query', '#', 'admin', NOW(), '', NULL, ''),
('分类修改', @teacher_type_menu_id, 2, '', NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'credit:teacherType:edit', '#', 'admin', NOW(), '', NULL, '');


-- 二级菜单：学分统计
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `query_param`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
('学分统计', @credit_menu_id, 2, 'credit-stats', 'research/credit/stats/index', NULL, NULL, 1, 0, 'C', '0', '0', 'credit:stats:view', 'chart', 'admin', NOW(), '', NULL, '学分统计');

-- 二级菜单：专业汇总
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `query_param`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
('专业学分汇总', @credit_menu_id, 3, 'major-summary', 'research/credit/majorSummary/index', NULL, NULL, 1, 0, 'C', '0', '0', 'credit:major:view', 'tree', 'admin', NOW(), '', NULL, '专业学分汇总');

-- 二级菜单：研究型占比分析
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `query_param`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
('研究型占比分析', @credit_menu_id, 4, 'research-ratio', 'research/credit/researchRatio/index', NULL, NULL, 1, 0, 'C', '0', '0', 'credit:research:view', 'star', 'admin', NOW(), '', NULL, '研究型学者占比分析');

-- 为管理员角色授权
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 1, menu_id FROM sys_menu
WHERE menu_id = @credit_menu_id
   OR perms LIKE 'credit:%';
