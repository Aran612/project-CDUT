package com.ruoyi.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 课程对象 ms_course
 *
 * @author CDUT-ALPHA
 * @date 2026-07-26
 */
public class MsCourse extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 课程ID */
    private Long courseId;

    /** 课程名称 */
    @Excel(name = "课程名称")
    private String courseName;

    /** 课程编号 */
    @Excel(name = "课程编号")
    private String courseCode;

    /** 课程学分 */
    @Excel(name = "课程学分")
    private BigDecimal creditValue;

    /** 课程类型（0=必修课 1=选修课 2=公共课） */
    @Excel(name = "课程类型", readConverterExp = "0=必修课,1=选修课,2=公共课")
    private String courseType;

    /** 开课院系ID */
    private Long departmentId;

    /** 开课院系名称 */
    @Excel(name = "开课院系")
    private String departmentName;

    /** 总学时 */
    @Excel(name = "总学时")
    private Integer totalHours;

    /** 状态（0=正常 1=停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

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

    public void setCourseCode(String courseCode)
    {
        this.courseCode = courseCode;
    }

    public String getCourseCode()
    {
        return courseCode;
    }

    public void setCreditValue(BigDecimal creditValue)
    {
        this.creditValue = creditValue;
    }

    public BigDecimal getCreditValue()
    {
        return creditValue;
    }

    public void setCourseType(String courseType)
    {
        this.courseType = courseType;
    }

    public String getCourseType()
    {
        return courseType;
    }

    public void setDepartmentId(Long departmentId)
    {
        this.departmentId = departmentId;
    }

    public Long getDepartmentId()
    {
        return departmentId;
    }

    public void setDepartmentName(String departmentName)
    {
        this.departmentName = departmentName;
    }

    public String getDepartmentName()
    {
        return departmentName;
    }

    public void setTotalHours(Integer totalHours)
    {
        this.totalHours = totalHours;
    }

    public Integer getTotalHours()
    {
        return totalHours;
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
            .append("courseId", getCourseId())
            .append("courseName", getCourseName())
            .append("courseCode", getCourseCode())
            .append("creditValue", getCreditValue())
            .append("courseType", getCourseType())
            .append("departmentId", getDepartmentId())
            .append("departmentName", getDepartmentName())
            .append("totalHours", getTotalHours())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
