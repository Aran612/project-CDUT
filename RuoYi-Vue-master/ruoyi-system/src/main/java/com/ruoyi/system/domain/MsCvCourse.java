package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * CV授课记录对象 ms_cv_course（可从授课记录同步，教师补充层次/角色/周学时）
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
public class MsCvCourse extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** CV授课记录ID */
    private Long cvCourseId;

    /** 教师ID */
    private Long professorId;

    /** 课程名称（中英文） */
    private String courseName;

    /** 层次（本科/硕士/MBA） */
    private String levelName;

    /** 学年（如2025-2026） */
    private String academicYear;

    /** 角色（主讲/助教） */
    private String teachRole;

    /** 周学时 */
    private String weeklyHours;

    /** 数据来源（1=授课记录同步 2=手工） */
    private String source;

    /** 排序 */
    private Integer sortOrder;

    /** 状态（0正常 1停用） */
    private String status;

    public void setCvCourseId(Long cvCourseId)
    {
        this.cvCourseId = cvCourseId;
    }

    public Long getCvCourseId()
    {
        return cvCourseId;
    }

    public void setProfessorId(Long professorId)
    {
        this.professorId = professorId;
    }

    public Long getProfessorId()
    {
        return professorId;
    }

    public void setCourseName(String courseName)
    {
        this.courseName = courseName;
    }

    public String getCourseName()
    {
        return courseName;
    }

    public void setLevelName(String levelName)
    {
        this.levelName = levelName;
    }

    public String getLevelName()
    {
        return levelName;
    }

    public void setAcademicYear(String academicYear)
    {
        this.academicYear = academicYear;
    }

    public String getAcademicYear()
    {
        return academicYear;
    }

    public void setTeachRole(String teachRole)
    {
        this.teachRole = teachRole;
    }

    public String getTeachRole()
    {
        return teachRole;
    }

    public void setWeeklyHours(String weeklyHours)
    {
        this.weeklyHours = weeklyHours;
    }

    public String getWeeklyHours()
    {
        return weeklyHours;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    public String getSource()
    {
        return source;
    }

    public void setSortOrder(Integer sortOrder)
    {
        this.sortOrder = sortOrder;
    }

    public Integer getSortOrder()
    {
        return sortOrder;
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
            .append("cvCourseId", getCvCourseId())
            .append("professorId", getProfessorId())
            .append("courseName", getCourseName())
            .append("levelName", getLevelName())
            .append("academicYear", getAcademicYear())
            .append("teachRole", getTeachRole())
            .append("weeklyHours", getWeeklyHours())
            .append("source", getSource())
            .append("sortOrder", getSortOrder())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
