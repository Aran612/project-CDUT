package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * CV教育背景对象 ms_cv_education
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
public class MsCvEducation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 教育背景ID */
    private Long educationId;

    /** 教师ID */
    private Long professorId;

    /** 学位（博士Ph.D./硕士Master/学士Bachelor等） */
    private String degree;

    /** 专业 */
    private String major;

    /** 授予院校 */
    private String institution;

    /** 年份 */
    private String gradYear;

    /** 最高学位（0否 1是） */
    private String isHighest;

    /** 排序 */
    private Integer sortOrder;

    /** 状态（0正常 1停用） */
    private String status;

    public void setEducationId(Long educationId)
    {
        this.educationId = educationId;
    }

    public Long getEducationId()
    {
        return educationId;
    }

    public void setProfessorId(Long professorId)
    {
        this.professorId = professorId;
    }

    public Long getProfessorId()
    {
        return professorId;
    }

    public void setDegree(String degree)
    {
        this.degree = degree;
    }

    public String getDegree()
    {
        return degree;
    }

    public void setMajor(String major)
    {
        this.major = major;
    }

    public String getMajor()
    {
        return major;
    }

    public void setInstitution(String institution)
    {
        this.institution = institution;
    }

    public String getInstitution()
    {
        return institution;
    }

    public void setGradYear(String gradYear)
    {
        this.gradYear = gradYear;
    }

    public String getGradYear()
    {
        return gradYear;
    }

    public void setIsHighest(String isHighest)
    {
        this.isHighest = isHighest;
    }

    public String getIsHighest()
    {
        return isHighest;
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
            .append("educationId", getEducationId())
            .append("professorId", getProfessorId())
            .append("degree", getDegree())
            .append("major", getMajor())
            .append("institution", getInstitution())
            .append("gradYear", getGradYear())
            .append("isHighest", getIsHighest())
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
