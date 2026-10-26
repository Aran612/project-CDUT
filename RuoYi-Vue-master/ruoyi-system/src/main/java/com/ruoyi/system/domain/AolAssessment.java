package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * AoL测评方式对象 aol_assessment
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
public class AolAssessment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 测评ID */
    private Long assessId;

    /** 学位项目ID */
    private Long programId;

    /** 能力目标ID */
    private Long cgId;

    /** 所评估的LG（如1.1,1.2,1.3） */
    @Excel(name = "所评估的LG")
    private String lgsAssessed;

    /** 直接测评措施（每行一条） */
    @Excel(name = "直接测评措施")
    private String directMeasures;

    /** 评分标准与阈值 */
    @Excel(name = "评分标准与阈值")
    private String rubricThreshold;

    /** 间接测评措施（每行一条） */
    @Excel(name = "间接测评措施")
    private String indirectMeasures;

    /** 频率（每年/每2年） */
    @Excel(name = "频率")
    private String frequency;

    /** 排序 */
    private Integer sortOrder;

    /** 状态（0正常 1停用） */
    private String status;

    public void setAssessId(Long assessId)
    {
        this.assessId = assessId;
    }

    public Long getAssessId()
    {
        return assessId;
    }

    public void setProgramId(Long programId)
    {
        this.programId = programId;
    }

    public Long getProgramId()
    {
        return programId;
    }

    public void setCgId(Long cgId)
    {
        this.cgId = cgId;
    }

    public Long getCgId()
    {
        return cgId;
    }

    public void setLgsAssessed(String lgsAssessed)
    {
        this.lgsAssessed = lgsAssessed;
    }

    public String getLgsAssessed()
    {
        return lgsAssessed;
    }

    public void setDirectMeasures(String directMeasures)
    {
        this.directMeasures = directMeasures;
    }

    public String getDirectMeasures()
    {
        return directMeasures;
    }

    public void setRubricThreshold(String rubricThreshold)
    {
        this.rubricThreshold = rubricThreshold;
    }

    public String getRubricThreshold()
    {
        return rubricThreshold;
    }

    public void setIndirectMeasures(String indirectMeasures)
    {
        this.indirectMeasures = indirectMeasures;
    }

    public String getIndirectMeasures()
    {
        return indirectMeasures;
    }

    public void setFrequency(String frequency)
    {
        this.frequency = frequency;
    }

    public String getFrequency()
    {
        return frequency;
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
            .append("assessId", getAssessId())
            .append("programId", getProgramId())
            .append("cgId", getCgId())
            .append("lgsAssessed", getLgsAssessed())
            .append("directMeasures", getDirectMeasures())
            .append("rubricThreshold", getRubricThreshold())
            .append("indirectMeasures", getIndirectMeasures())
            .append("frequency", getFrequency())
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
