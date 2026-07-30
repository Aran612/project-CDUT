package com.ruoyi.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 授课记录对象 ms_teaching_record
 *
 * @author CDUT-ALPHA
 * @date 2026-07-26
 */
public class MsTeachingRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 授课记录ID */
    private Long recordId;

    /** 课程ID */
    private Long courseId;

    /** 课程名称（冗余） */
    @Excel(name = "课程名称")
    private String courseName;

    /** 授课教授ID */
    private Long professorId;

    /** 教授姓名（冗余） */
    @Excel(name = "授课教授")
    private String professorName;

    /** 学期 */
    @Excel(name = "学期")
    private String semester;

    /** 选课学生数量 */
    @Excel(name = "学生数量")
    private Integer studentCount;

    /** 课程学分（冗余） */
    @Excel(name = "课程学分")
    private BigDecimal creditPerCourse;

    /** 计算积分（=课程学分×学生数量） */
    @Excel(name = "计算积分")
    private BigDecimal calculatedScore;

    /** 是否研究型课程（0=否 1=是） */
    @Excel(name = "是否研究型", readConverterExp = "0=否,1=是")
    private String isResearch;

    /** 状态（0=正常 1=停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setRecordId(Long recordId)
    {
        this.recordId = recordId;
    }

    public Long getRecordId()
    {
        return recordId;
    }

    public void setCourseId(Long courseId)
    {
        this.courseId = courseId;
    }

    public Long getCourseId()
    {
        return courseId;
    }

    public void setCourseName(String courseName)
    {
        this.courseName = courseName;
    }

    public String getCourseName()
    {
        return courseName;
    }

    public void setProfessorId(Long professorId)
    {
        this.professorId = professorId;
    }

    public Long getProfessorId()
    {
        return professorId;
    }

    public void setProfessorName(String professorName)
    {
        this.professorName = professorName;
    }

    public String getProfessorName()
    {
        return professorName;
    }

    public void setSemester(String semester)
    {
        this.semester = semester;
    }

    public String getSemester()
    {
        return semester;
    }

    public void setStudentCount(Integer studentCount)
    {
        this.studentCount = studentCount;
    }

    public Integer getStudentCount()
    {
        return studentCount;
    }

    public void setCreditPerCourse(BigDecimal creditPerCourse)
    {
        this.creditPerCourse = creditPerCourse;
    }

    public BigDecimal getCreditPerCourse()
    {
        return creditPerCourse;
    }

    public void setCalculatedScore(BigDecimal calculatedScore)
    {
        this.calculatedScore = calculatedScore;
    }

    public BigDecimal getCalculatedScore()
    {
        return calculatedScore;
    }

    public void setIsResearch(String isResearch)
    {
        this.isResearch = isResearch;
    }

    public String getIsResearch()
    {
        return isResearch;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("recordId", getRecordId())
            .append("courseId", getCourseId())
            .append("courseName", getCourseName())
            .append("professorId", getProfessorId())
            .append("professorName", getProfessorName())
            .append("semester", getSemester())
            .append("studentCount", getStudentCount())
            .append("creditPerCourse", getCreditPerCourse())
            .append("calculatedScore", getCalculatedScore())
            .append("isResearch", getIsResearch())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
