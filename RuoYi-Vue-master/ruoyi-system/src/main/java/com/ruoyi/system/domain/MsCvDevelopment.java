package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * CV职业发展对象 ms_cv_development
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
public class MsCvDevelopment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 职业发展ID */
    private Long devId;

    /** 教师ID */
    private Long professorId;

    /** 时间 */
    private String period;

    /** 活动内容（研修/行业实践/资格更新） */
    private String activity;

    /** 组织单位 */
    private String organizer;

    /** 学时/时长 */
    private String hours;

    /** 排序 */
    private Integer sortOrder;

    /** 状态（0正常 1停用） */
    private String status;

    public void setDevId(Long devId)
    {
        this.devId = devId;
    }

    public Long getDevId()
    {
        return devId;
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

    public void setActivity(String activity)
    {
        this.activity = activity;
    }

    public String getActivity()
    {
        return activity;
    }

    public void setOrganizer(String organizer)
    {
        this.organizer = organizer;
    }

    public String getOrganizer()
    {
        return organizer;
    }

    public void setHours(String hours)
    {
        this.hours = hours;
    }

    public String getHours()
    {
        return hours;
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
            .append("devId", getDevId())
            .append("professorId", getProfessorId())
            .append("period", getPeriod())
            .append("activity", getActivity())
            .append("organizer", getOrganizer())
            .append("hours", getHours())
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
