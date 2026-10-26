package com.ruoyi.system.domain;

import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * AoL图谱课程对象 aol_map_course
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
public class AolMapCourse extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 图谱课程ID */
    private Long mapCourseId;

    /** 学位项目ID */
    private Long programId;

    /** 课程ID（ms_course，可为空） */
    private Long courseId;

    /** 课程名称（中文，冗余） */
    @Excel(name = "课程名称")
    private String courseName;

    /** 课程名称（英文） */
    @Excel(name = "英文名称")
    private String courseNameEn;

    /** 开课年级（1/2/3...） */
    private String yearLevel;

    /** 课程性质（Core必修/Elective选修/Capstone顶点） */
    @Excel(name = "课程性质")
    private String courseKind;

    /** 主要支撑学习目标（如1.1,2.1,3.1） */
    @Excel(name = "主要支撑学习目标")
    private String primaryLgs;

    /** 排序 */
    private Integer sortOrder;

    /** 状态（0正常 1停用） */
    private String status;

    /** 覆盖格明细（非表字段，查询用） */
    private List<AolMapCell> cells;

    public void setMapCourseId(Long mapCourseId)
    {
        this.mapCourseId = mapCourseId;
    }

    public Long getMapCourseId()
    {
        return mapCourseId;
    }

    public void setProgramId(Long programId)
    {
        this.programId = programId;
    }

    public Long getProgramId()
    {
        return programId;
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

    public void setCourseNameEn(String courseNameEn)
    {
        this.courseNameEn = courseNameEn;
    }

    public String getCourseNameEn()
    {
        return courseNameEn;
    }

    public void setYearLevel(String yearLevel)
    {
        this.yearLevel = yearLevel;
    }

    public String getYearLevel()
    {
        return yearLevel;
    }

    public void setCourseKind(String courseKind)
    {
        this.courseKind = courseKind;
    }

    public String getCourseKind()
    {
        return courseKind;
    }

    public void setPrimaryLgs(String primaryLgs)
    {
        this.primaryLgs = primaryLgs;
    }

    public String getPrimaryLgs()
    {
        return primaryLgs;
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

    public void setCells(List<AolMapCell> cells)
    {
        this.cells = cells;
    }

    public List<AolMapCell> getCells()
    {
        return cells;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("mapCourseId", getMapCourseId())
            .append("programId", getProgramId())
            .append("courseId", getCourseId())
            .append("courseName", getCourseName())
            .append("courseNameEn", getCourseNameEn())
            .append("yearLevel", getYearLevel())
            .append("courseKind", getCourseKind())
            .append("primaryLgs", getPrimaryLgs())
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
