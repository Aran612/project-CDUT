package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * CV工作经历对象 ms_cv_experience（exp_type 1=学术经历 2=行业/实务经历）
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
public class MsCvExperience extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 工作经历ID */
    private Long experienceId;

    /** 教师ID */
    private Long professorId;

    /** 经历类型（1=学术 2=行业/实务） */
    private String expType;

    /** 起止时间 */
    private String period;

    /** 单位（学术经历填“单位与职务”） */
    private String organization;

    /** 职务/岗位（行业经历用） */
    private String role;

    /** 主要职责/主要业绩 */
    private String description;

    /** 排序 */
    private Integer sortOrder;

    /** 状态（0正常 1停用） */
    private String status;

    public void setExperienceId(Long experienceId)
    {
        this.experienceId = experienceId;
    }

    public Long getExperienceId()
    {
        return experienceId;
    }

    public void setProfessorId(Long professorId)
    {
        this.professorId = professorId;
    }

    public Long getProfessorId()
    {
        return professorId;
    }

    public void setExpType(String expType)
    {
        this.expType = expType;
    }

    public String getExpType()
    {
        return expType;
    }

    public void setPeriod(String period)
    {
        this.period = period;
    }

    public String getPeriod()
    {
        return period;
    }

    public void setOrganization(String organization)
    {
        this.organization = organization;
    }

    public String getOrganization()
    {
        return organization;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    public String getRole()
    {
        return role;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
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
            .append("experienceId", getExperienceId())
            .append("professorId", getProfessorId())
            .append("expType", getExpType())
            .append("period", getPeriod())
            .append("organization", getOrganization())
            .append("role", getRole())
            .append("description", getDescription())
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
