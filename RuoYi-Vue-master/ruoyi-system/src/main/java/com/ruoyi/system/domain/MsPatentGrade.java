package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 专利等级对象 ms_patent_grade
 */
public class MsPatentGrade extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long gradeId;
    @Excel(name = "等级编码") private String gradeCode;
    @Excel(name = "等级名称") private String gradeName;
    @Excel(name = "基础积分") private Integer baseScore;
    @Excel(name = "仅用于考核", readConverterExp = "0=否,1=是") private String forEvaluationOnly;
    @Excel(name = "排序") private Integer sortOrder;
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用") private String status;

    public Long getGradeId() { return gradeId; }
    public void setGradeId(Long v) { gradeId = v; }
    public String getGradeCode() { return gradeCode; }
    public void setGradeCode(String v) { gradeCode = v; }
    public String getGradeName() { return gradeName; }
    public void setGradeName(String v) { gradeName = v; }
    public Integer getBaseScore() { return baseScore; }
    public void setBaseScore(Integer v) { baseScore = v; }
    public String getForEvaluationOnly() { return forEvaluationOnly; }
    public void setForEvaluationOnly(String v) { forEvaluationOnly = v; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer v) { sortOrder = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { status = v; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("gradeId", getGradeId()).append("gradeCode", getGradeCode()).append("gradeName", getGradeName())
            .append("baseScore", getBaseScore()).append("forEvaluationOnly", getForEvaluationOnly())
            .append("sortOrder", getSortOrder()).append("status", getStatus())
            .append("createBy", getCreateBy()).append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy()).append("updateTime", getUpdateTime()).append("remark", getRemark())
            .toString();
    }
}
