package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.MsCvCourse;

/**
 * CV授课记录Mapper接口
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
public interface MsCvCourseMapper
{
    /**
     * 查询CV授课记录
     */
    public MsCvCourse selectMsCvCourseByCvCourseId(Long cvCourseId);

    /**
     * 查询CV授课记录列表
     */
    public List<MsCvCourse> selectMsCvCourseList(MsCvCourse msCvCourse);

    /**
     * 新增CV授课记录
     */
    public int insertMsCvCourse(MsCvCourse msCvCourse);

    /**
     * 修改CV授课记录
     */
    public int updateMsCvCourse(MsCvCourse msCvCourse);

    /**
     * 删除CV授课记录
     */
    public int deleteMsCvCourseByCvCourseId(Long cvCourseId);

    /**
     * 批量删除CV授课记录
     */
    public int deleteMsCvCourseByCvCourseIds(Long[] cvCourseIds);
}
