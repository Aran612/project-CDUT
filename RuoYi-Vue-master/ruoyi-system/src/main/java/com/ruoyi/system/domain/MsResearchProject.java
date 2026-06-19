package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 科研项目对象 ms_research_project
 *
 * @author 王祎哲
 * @date 2026-06-12
 */
public class MsResearchProject extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 项目ID */
    private Long projectId;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String projectName;

    /** 项目等级ID */
    @Excel(name = "项目等级ID", type = Excel.Type.IMPORT)
    private Long gradeId;

    /** 项目等级名称（用于显示） */
    @Excel(name = "项目等级")
    private String gradeName;

    /** 负责人教授ID */
    @Excel(name = "教授ID", type = Excel.Type.IMPORT)
    private Long professorId;

    /** 教授姓名（用于显示） */
    @Excel(name = "教授姓名")
    private String professorName;

    /** 承担角色（主持/参与） */
    @Excel(name = "承担角色", readConverterExp = "主持=主持,参与=参与")
    private String role;

    /** 经费(万元) */
    @Excel(name = "经费(万元)")
    private BigDecimal fundingAmount;

    /** 批准号 */
    @Excel(name = "批准号")
    private String approvalNumber;

    /** 项目来源 */
    @Excel(name = "项目来源")
    private String projectSource;

    /** 开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startDate;

    /** 结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endDate;

    /** 实际积分 */
    @Excel(name = "实际积分")
    private Integer actualScore;

    /** 状态（0在研 1结题 2终止） */
    @Excel(name = "状态", readConverterExp = "0=在研,1=结题,2=终止")
    private String status;

    // ---- 用于查询的临时字段 ----
    private String gradeCode;

    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public Long getProjectId() { return projectId; }

    public void setProjectName(String projectName) { this.projectName = projectName; }
    public String getProjectName() { return projectName; }

    public void setGradeId(Long gradeId) { this.gradeId = gradeId; }
    public Long getGradeId() { return gradeId; }

    public void setGradeName(String gradeName) { this.gradeName = gradeName; }
    public String getGradeName() { return gradeName; }

    public void setProfessorId(Long professorId) { this.professorId = professorId; }
    public Long getProfessorId() { return professorId; }

    public void setProfessorName(String professorName) { this.professorName = professorName; }
    public String getProfessorName() { return professorName; }

    public void setRole(String role) { this.role = role; }
    public String getRole() { return role; }

    public void setFundingAmount(BigDecimal fundingAmount) { this.fundingAmount = fundingAmount; }
    public BigDecimal getFundingAmount() { return fundingAmount; }

    public void setApprovalNumber(String approvalNumber) { this.approvalNumber = approvalNumber; }
    public String getApprovalNumber() { return approvalNumber; }

    public void setProjectSource(String projectSource) { this.projectSource = projectSource; }
    public String getProjectSource() { return projectSource; }

    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getStartDate() { return startDate; }

    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public Date getEndDate() { return endDate; }

    public void setActualScore(Integer actualScore) { this.actualScore = actualScore; }
    public Integer getActualScore() { return actualScore; }

    public void setStatus(String status) { this.status = status; }
    public String getStatus() { return status; }

    public void setGradeCode(String gradeCode) { this.gradeCode = gradeCode; }
    public String getGradeCode() { return gradeCode; }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("projectId", getProjectId())
            .append("projectName", getProjectName())
            .append("gradeId", getGradeId())
            .append("gradeName", getGradeName())
            .append("professorId", getProfessorId())
            .append("professorName", getProfessorName())
            .append("role", getRole())
            .append("fundingAmount", getFundingAmount())
            .append("approvalNumber", getApprovalNumber())
            .append("projectSource", getProjectSource())
            .append("startDate", getStartDate())
            .append("endDate", getEndDate())
            .append("actualScore", getActualScore())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
