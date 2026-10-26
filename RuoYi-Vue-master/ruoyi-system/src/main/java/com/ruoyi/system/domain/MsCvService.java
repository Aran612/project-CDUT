package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * CV社会服务对象 ms_cv_service
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
public class MsCvService extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 社会服务ID */
    private Long serviceId;

    /** 教师ID */
    private Long professorId;

    /** 时间 */
    private String period;

    /** 服务内容（审稿/学会/评审/咨询/培训等） */
    private String serviceContent;

    /** 相关单位 */
    private String organization;

    /** 排序 */
    private Integer sortOrder;

    /** 状态（0正常 1停用） */
    private String status;

    public void setServiceId(Long serviceId)
    {
        this.serviceId = serviceId;
    }

    public Long getServiceId()
    {
        return serviceId;
    }

    public void setProfessorId(Long professorId)
    {
        this.professorId = professorId;
    }

    public Long getProfessorId()
    {
        return professorId;
    }

    public void setPeriod(String period)
    {
        this.period = period;
    }

    public String getPeriod()
    {
        return period;
    }

    public void setServiceContent(String serviceContent)
    {
        this.serviceContent = serviceContent;
    }

    public String getServiceContent()
    {
        return serviceContent;
    }

    public void setOrganization(String organization)
    {
        this.organization = organization;
    }

    public String getOrganization()
    {
        return organization;
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
            .append("serviceId", getServiceId())
            .append("professorId", getProfessorId())
            .append("period", getPeriod())
            .append("serviceContent", getServiceContent())
            .append("organization", getOrganization())
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
