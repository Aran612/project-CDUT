package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;
import com.ruoyi.system.domain.AolCourseCommittee;

/**
 * AoL课程委员会Mapper接口
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
public interface AolCourseCommitteeMapper
{
    /**
     * 查询委员会成员（关联教师姓名、工号、课程名称）
     */
    public AolCourseCommittee selectAolCourseCommitteeByCommitteeId(Long committeeId);

    /**
     * 查询委员会成员列表（按项目/图谱课程查询）
     */
    public List<AolCourseCommittee> selectAolCourseCommitteeList(AolCourseCommittee aolCourseCommittee);

    /**
     * 新增委员会成员
     */
    public int insertAolCourseCommittee(AolCourseCommittee aolCourseCommittee);

    /**
     * 修改委员会成员
     */
    public int updateAolCourseCommittee(AolCourseCommittee aolCourseCommittee);

    /**
     * 删除委员会成员
     */
    public int deleteAolCourseCommitteeByCommitteeId(Long committeeId);

    /**
     * 批量删除委员会成员
     */
    public int deleteAolCourseCommitteeByCommitteeIds(Long[] committeeIds);

    /**
     * 按图谱课程批量删除委员会成员
     */
    public int deleteAolCourseCommitteeByMapCourseIds(Long[] mapCourseIds);

    /**
     * 查询教师下拉选项（教师ID/姓名/工号）
     */
    public List<Map<String, Object>> selectTeacherOptions();
}
