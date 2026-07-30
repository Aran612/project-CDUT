package com.ruoyi.system.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.system.domain.MsCourse;

/**
 * 课程Service接口
 *
 * @author CDUT-ALPHA
 * @date 2026-07-26
 */
public interface IMsCourseService
{
    /**
     * 查询课程
     */
    public MsCourse selectMsCourseByCourseId(Long courseId);

    /**
     * 查询课程列表
     */
    public List<MsCourse> selectMsCourseList(MsCourse msCourse);

    /**
     * 查询所有课程
     */
    public List<MsCourse> selectAllMsCourse();

    /**
     * 新增课程
     */
    public int insertMsCourse(MsCourse msCourse);

    /**
     * 修改课程
     */
    public int updateMsCourse(MsCourse msCourse);

    /**
     * 删除课程
     */
    public int deleteMsCourseByCourseIds(Long[] courseIds);

    /**
     * 导入课程数据
     */
    public String importCourse(List<MsCourse> courseList, boolean updateSupport, String operName);

    /**
     * 按课程类型统计
     */
    public List<Map<String, Object>> countCourseByType();
}
