package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 获奖等级对象 ms_award_grade
 */
public class MsAwardGrade extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long gradeId;
    @Excel(name = "等级编码") private String gradeCode;
    @Excel(name = "等级名称") private String gradeName;
    @Excel(name = "基础积分") private Integer baseScore;
    @Excel(name = "获奖大类") private String awardCategory;
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
    public String getAwardCategory() { return awardCategory; }
    public void setAwardCategory(String v) { awardCategory = v; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer v) { sortOrder = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { status = v; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("gradeId", getGradeId()).append("gradeCode", getGradeCode()).append("gradeName", getGradeName())
            .append("baseScore", getBaseScore()).append("awardCategory", getAwardCategory())
            .append("sortOrder", getSortOrder()).append("status", getStatus())
            .append("createBy", getCreateBy()).append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy()).append("updateTime", getUpdateTime()).append("remark", getRemark())
            .toString();
    }
}
