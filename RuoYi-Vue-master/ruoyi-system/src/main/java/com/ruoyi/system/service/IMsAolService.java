package com.ruoyi.system.service;

import java.util.List;
import java.util.Map;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import com.ruoyi.system.domain.AolAssessment;
import com.ruoyi.system.domain.AolCg;
import com.ruoyi.system.domain.AolCourseCommittee;
import com.ruoyi.system.domain.AolLg;
import com.ruoyi.system.domain.AolMapCell;
import com.ruoyi.system.domain.AolMapCourse;
import com.ruoyi.system.domain.AolProgram;

/**
 * AoL课程图谱Service接口
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
public interface IMsAolService
{
    // ------------------------------------------------------------------
    // 学位项目
    // ------------------------------------------------------------------

    /**
     * 查询学位项目
     */
    public AolProgram selectAolProgramByProgramId(Long programId);

    /**
     * 查询学位项目列表
     */
    public List<AolProgram> selectAolProgramList(AolProgram aolProgram);

    /**
     * 新增学位项目
     */
    public int insertAolProgram(AolProgram aolProgram);

    /**
     * 修改学位项目
     */
    public int updateAolProgram(AolProgram aolProgram);

    /**
     * 批量删除学位项目
     */
    public int deleteAolProgramByProgramIds(Long[] programIds);

    // ------------------------------------------------------------------
    // 能力目标（CG）
    // ------------------------------------------------------------------

    /**
     * 查询能力目标
     */
    public AolCg selectAolCgByCgId(Long cgId);

    /**
     * 查询能力目标列表（按项目查询，排序号排序）
     */
    public List<AolCg> selectAolCgList(AolCg aolCg);

    /**
     * 新增能力目标
     */
    public int insertAolCg(AolCg aolCg);

    /**
     * 修改能力目标
     */
    public int updateAolCg(AolCg aolCg);

    /**
     * 批量删除能力目标
     */
    public int deleteAolCgByCgIds(Long[] cgIds);

    // ------------------------------------------------------------------
    // 学习目标（LG）
    // ------------------------------------------------------------------

    /**
     * 查询学习目标
     */
    public AolLg selectAolLgByLgId(Long lgId);

    /**
     * 查询学习目标列表（按能力目标查询，排序号排序）
     */
    public List<AolLg> selectAolLgList(AolLg aolLg);

    /**
     * 新增学习目标
     */
    public int insertAolLg(AolLg aolLg);

    /**
     * 修改学习目标
     */
    public int updateAolLg(AolLg aolLg);

    /**
     * 批量删除学习目标
     */
    public int deleteAolLgByLgIds(Long[] lgIds);

    // ------------------------------------------------------------------
    // 课程图谱
    // ------------------------------------------------------------------

    /**
     * 查询图谱课程列表（按项目查询，带出覆盖格明细）
     */
    public List<AolMapCourse> selectMapCourses(Long programId);

    /**
     * 查询图谱课程（带出覆盖格明细）
     */
    public AolMapCourse selectAolMapCourseByMapCourseId(Long mapCourseId);

    /**
     * 新增图谱课程
     */
    public int insertAolMapCourse(AolMapCourse aolMapCourse);

    /**
     * 修改图谱课程
     */
    public int updateAolMapCourse(AolMapCourse aolMapCourse);

    /**
     * 批量删除图谱课程（级联删除覆盖格与委员会成员）
     */
    public int deleteAolMapCourseByMapCourseIds(Long[] mapCourseIds);

    /**
     * 保存课程覆盖格（先删旧数据再批量插入，覆盖层级为空白的跳过）
     */
    public int saveCells(Long mapCourseId, List<AolMapCell> cells);

    // ------------------------------------------------------------------
    // 测评方式
    // ------------------------------------------------------------------

    /**
     * 查询测评方式
     */
    public AolAssessment selectAolAssessmentByAssessId(Long assessId);

    /**
     * 查询测评方式列表（按项目查询，排序号排序）
     */
    public List<AolAssessment> selectAolAssessmentList(AolAssessment aolAssessment);

    /**
     * 新增测评方式
     */
    public int insertAolAssessment(AolAssessment aolAssessment);

    /**
     * 修改测评方式
     */
    public int updateAolAssessment(AolAssessment aolAssessment);

    /**
     * 批量删除测评方式
     */
    public int deleteAolAssessmentByAssessIds(Long[] assessIds);

    // ------------------------------------------------------------------
    // 课程委员会
    // ------------------------------------------------------------------

    /**
     * 查询委员会成员
     */
    public AolCourseCommittee selectAolCourseCommitteeByCommitteeId(Long committeeId);

    /**
     * 查询委员会成员列表（关联教师姓名/工号/课程名称）
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
     * 批量删除委员会成员
     */
    public int deleteAolCourseCommitteeByCommitteeIds(Long[] committeeIds);

    /**
     * 查询教师下拉选项（教师ID/姓名/工号）
     */
    public List<Map<String, Object>> selectTeacherOptions();

    // ------------------------------------------------------------------
    // 导出
    // ------------------------------------------------------------------

    /**
     * 导出课程图谱Excel（5个Sheet，官方模板格式）
     */
    public XSSFWorkbook exportMapExcel(Long programId);

    /**
     * 导出课程图谱Word
     */
    public XWPFDocument exportMapWord(Long programId);

    /**
     * 导出课程大纲Syllabus Word
     */
    public XWPFDocument exportSyllabus(Long mapCourseId);
}
