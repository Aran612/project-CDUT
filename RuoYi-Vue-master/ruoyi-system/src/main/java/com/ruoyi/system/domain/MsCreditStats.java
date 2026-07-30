package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 学分统计结果对象（用于返回统计数据）
 *
 * @author CDUT-ALPHA
 * @date 2026-07-26
 */
public class MsCreditStats
{
    /** 统计类型（teacherType=师资类型统计, major=专业汇总, research=研究型占比, student=学生学分明细） */
    private String statsType;

    /** 学期 */
    private String semester;

    /** 师资类型编码 */
    private String teacherType;

    /** 师资类型名称 */
    private String teacherTypeName;

    /** 所属院系/专业 */
    private String department;

    /** 教授ID */
    private Long professorId;

    /** 教授姓名 */
    private String professorName;

    /** 职称 */
    private String title;

    /** 学生学号 */
    private String studentId;

    /** 学生姓名 */
    private String studentName;

    /** 课程ID */
    private Long courseId;

    /** 课程名称 */
    private String courseName;

    /** 课程学分 */
    private BigDecimal creditPerCourse;

    /** 学生数量 */
    private Integer studentCount;

    /** 计算积分（=课程学分×学生数量） */
    private BigDecimal calculatedScore;

    /** 是否研究型课程 */
    private String isResearch;

    /** 记录数量 */
    private Integer recordCount;

    /** 总学分 */
    private BigDecimal totalCredits;

    /** 研究型学分 */
    private BigDecimal researchCredits;

    /** 研究型学分占比 */
    private BigDecimal researchRatio;

    /** 目标占比（40%） */
    private BigDecimal targetRatio = new BigDecimal("0.40");

    /** 是否达标 */
    private Boolean isTargetMet;

    /** 备注 */
    private String remark;

    /** 扩展数据（用于传递额外统计信息） */
    private Map<String, Object> extraData;

    public String getStatsType()
    {
        return statsType;
    }

    public void setStatsType(String statsType)
    {
        this.statsType = statsType;
    }

    public String getSemester()
    {
        return semester;
    }

    public void setSemester(String semester)
    {
        this.semester = semester;
    }

    public String getTeacherType()
    {
        return teacherType;
    }

    public void setTeacherType(String teacherType)
    {
        this.teacherType = teacherType;
    }

    public String getTeacherTypeName()
    {
        return teacherTypeName;
    }

    public void setTeacherTypeName(String teacherTypeName)
    {
        this.teacherTypeName = teacherTypeName;
    }

    public String getDepartment()
    {
        return department;
    }

    public void setDepartment(String department)
    {
        this.department = department;
    }

    public Long getProfessorId()
    {
        return professorId;
    }

    public void setProfessorId(Long professorId)
    {
        this.professorId = professorId;
    }

    public String getProfessorName()
    {
        return professorName;
    }

    public void setProfessorName(String professorName)
    {
        this.professorName = professorName;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getStudentId()
    {
        return studentId;
    }

    public void setStudentId(String studentId)
    {
        this.studentId = studentId;
    }

    public String getStudentName()
    {
        return studentName;
    }

    public void setStudentName(String studentName)
    {
        this.studentName = studentName;
    }

    public Long getCourseId()
    {
        return courseId;
    }

    public void setCourseId(Long courseId)
    {
        this.courseId = courseId;
    }

    public String getCourseName()
    {
        return courseName;
    }

    public void setCourseName(String courseName)
    {
        this.courseName = courseName;
    }

    public BigDecimal getCreditPerCourse()
    {
        return creditPerCourse;
    }

    public void setCreditPerCourse(BigDecimal creditPerCourse)
    {
        this.creditPerCourse = creditPerCourse;
    }

    public Integer getStudentCount()
    {
        return studentCount;
    }

    public void setStudentCount(Integer studentCount)
    {
        this.studentCount = studentCount;
    }

    public BigDecimal getCalculatedScore()
    {
        return calculatedScore;
    }

    public void setCalculatedScore(BigDecimal calculatedScore)
    {
        this.calculatedScore = calculatedScore;
    }

    public String getIsResearch()
    {
        return isResearch;
    }

    public void setIsResearch(String isResearch)
    {
        this.isResearch = isResearch;
    }

    public Integer getRecordCount()
    {
        return recordCount;
    }

    public void setRecordCount(Integer recordCount)
    {
        this.recordCount = recordCount;
    }

    public BigDecimal getTotalCredits()
    {
        return totalCredits;
    }

    public void setTotalCredits(BigDecimal totalCredits)
    {
        this.totalCredits = totalCredits;
    }

    public BigDecimal getResearchCredits()
    {
        return researchCredits;
    }

    public void setResearchCredits(BigDecimal researchCredits)
    {
        this.researchCredits = researchCredits;
    }

    public BigDecimal getResearchRatio()
    {
        return researchRatio;
    }

    public void setResearchRatio(BigDecimal researchRatio)
    {
        this.researchRatio = researchRatio;
    }

    public BigDecimal getTargetRatio()
    {
        return targetRatio;
    }

    public void setTargetRatio(BigDecimal targetRatio)
    {
        this.targetRatio = targetRatio;
    }

    public Boolean getIsTargetMet()
    {
        return isTargetMet;
    }

    public void setIsTargetMet(Boolean isTargetMet)
    {
        this.isTargetMet = isTargetMet;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public Map<String, Object> getExtraData()
    {
        return extraData;
    }

    public void setExtraData(Map<String, Object> extraData)
    {
        this.extraData = extraData;
    }
}
