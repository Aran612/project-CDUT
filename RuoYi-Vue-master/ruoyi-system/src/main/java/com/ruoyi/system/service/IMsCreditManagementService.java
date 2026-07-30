package com.ruoyi.system.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.system.domain.MsCreditStats;

/**
 * 学分管理Service接口
 *
 * @author CDUT-ALPHA
 * @date 2026-07-26
 */
public interface IMsCreditManagementService
{
    /**
     * 按师资类型统计学分
     */
    public List<Map<String, Object>> selectCreditByTeacherType(MsCreditStats stats);

    /**
     * 按专业汇总学分
     */
    public List<Map<String, Object>> selectCreditByDepartment(MsCreditStats stats);

    /**
     * 研究型占比分析（按专业）
     */
    public List<Map<String, Object>> selectResearchRatioByDepartment(MsCreditStats stats);

    /**
     * 研究型占比分析（按师资类型）
     */
    public List<Map<String, Object>> selectResearchRatioByTeacherType(MsCreditStats stats);

    /**
     * 学生学分明细（精确到每个学生）
     */
    public List<Map<String, Object>> selectStudentCreditDetail(MsCreditStats stats);

    /**
     * 按教授统计教学学分
     */
    public List<Map<String, Object>> selectCreditByProfessor(MsCreditStats stats);

    /**
     * 学分总览统计
     */
    public Map<String, Object> selectCreditOverview(MsCreditStats stats);

    /**
     * 获取所有学期列表
     */
    public List<String> selectSemesterList();

    /**
     * 获取所有专业/院系列表
     */
    public List<String> selectDepartmentList();

    /**
     * 获取师资类型字典
     */
    public Map<String, String> getTeacherTypeDict();
}
