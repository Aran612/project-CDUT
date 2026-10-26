package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.AolMapCourse;

/**
 * AoL图谱课程Mapper接口
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
public interface AolMapCourseMapper
{
    /**
     * 查询图谱课程
     */
    public AolMapCourse selectAolMapCourseByMapCourseId(Long mapCourseId);

    /**
     * 查询图谱课程列表（按项目查询）
     */
    public List<AolMapCourse> selectAolMapCourseList(AolMapCourse aolMapCourse);

    /**
     * 新增图谱课程
     */
    public int insertAolMapCourse(AolMapCourse aolMapCourse);

    /**
     * 修改图谱课程
     */
    public int updateAolMapCourse(AolMapCourse aolMapCourse);

    /**
     * 删除图谱课程
     */
    public int deleteAolMapCourseByMapCourseId(Long mapCourseId);

    /**
     * 批量删除图谱课程
     */
    public int deleteAolMapCourseByMapCourseIds(Long[] mapCourseIds);
}
