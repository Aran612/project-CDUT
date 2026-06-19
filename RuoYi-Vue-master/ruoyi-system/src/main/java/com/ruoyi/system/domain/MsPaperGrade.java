package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 论文等级对象 ms_paper_grade
 *
 * @author 王祎哲
 * @date 2026-06-12
 */
public class MsPaperGrade extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 等级ID */
    private Long gradeId;

    /** 等级编码 */
    @Excel(name = "等级编码")
    private String gradeCode;

    /** 等级名称 */
    @Excel(name = "等级名称")
    private String gradeName;

    /** 基础积分 */
    @Excel(name = "基础积分")
    private Integer baseScore;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sortOrder;

    /** 仅用于岗位聘期考核（0否 1是） */
    @Excel(name = "仅用于考核", readConverterExp = "0=否,1=是")
    private String forEvaluationOnly;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setGradeId(Long gradeId)
    {
        this.gradeId = gradeId;
    }

    public Long getGradeId()
    {
        return gradeId;
    }

    public void setGradeCode(String gradeCode)
    {
        this.gradeCode = gradeCode;
    }

    public String getGradeCode()
    {
        return gradeCode;
    }

    public void setGradeName(String gradeName)
    {
        this.gradeName = gradeName;
    }

    public String getGradeName()
    {
        return gradeName;
    }

    public void setBaseScore(Integer baseScore)
    {
        this.baseScore = baseScore;
    }

    public Integer getBaseScore()
    {
        return baseScore;
    }

    public void setSortOrder(Integer sortOrder)
    {
        this.sortOrder = sortOrder;
    }

    public Integer getSortOrder()
    {
        return sortOrder;
    }

    public void setForEvaluationOnly(String forEvaluationOnly)
    {
        this.forEvaluationOnly = forEvaluationOnly;
    }

    public String getForEvaluationOnly()
    {
        return forEvaluationOnly;
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
            .append("gradeId", getGradeId())
            .append("gradeCode", getGradeCode())
            .append("gradeName", getGradeName())
            .append("baseScore", getBaseScore())
            .append("sortOrder", getSortOrder())
            .append("forEvaluationOnly", getForEvaluationOnly())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
