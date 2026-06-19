package com.ruoyi.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 积分规则对象 ms_scoring_rule
 *
 * @author 王祎哲
 * @date 2026-06-12
 */
public class MsScoringRule extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 规则ID */
    private Long ruleId;

    /** 论文等级ID */
    @Excel(name = "论文等级ID")
    private Long gradeId;

    /** 作者身份 */
    @Excel(name = "作者身份")
    private String positionType;

    /** 积分系数 */
    @Excel(name = "积分系数")
    private BigDecimal coefficient;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setRuleId(Long ruleId)
    {
        this.ruleId = ruleId;
    }

    public Long getRuleId()
    {
        return ruleId;
    }

    public void setGradeId(Long gradeId)
    {
        this.gradeId = gradeId;
    }

    public Long getGradeId()
    {
        return gradeId;
    }

    public void setPositionType(String positionType)
    {
        this.positionType = positionType;
    }

    public String getPositionType()
    {
        return positionType;
    }

    public void setCoefficient(BigDecimal coefficient)
    {
        this.coefficient = coefficient;
    }

    public BigDecimal getCoefficient()
    {
        return coefficient;
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
            .append("ruleId", getRuleId())
            .append("gradeId", getGradeId())
            .append("positionType", getPositionType())
            .append("coefficient", getCoefficient())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
