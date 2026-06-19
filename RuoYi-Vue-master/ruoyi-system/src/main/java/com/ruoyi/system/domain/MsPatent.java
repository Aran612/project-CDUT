package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 专利对象 ms_patent
 *
 * @author 王祎哲
 * @date 2026-06-12
 */
public class MsPatent extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "专利名称")
    private String patentName;

    private Long gradeId;

    @Excel(name = "专利等级")
    private String gradeName;

    /** 专利等级编码（用于前端显示判别） */
    private String gradeCode;

    private Long professorId;

    @Excel(name = "发明人姓名")
    private String professorName;

    @Excel(name = "发明人排名")
    private String inventorRank;

    @Excel(name = "专利号")
    private String patentNumber;

    @Excel(name = "授权国家")
    private String patentCountry;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "申请日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date applicationDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "授权日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date grantDate;

    @Excel(name = "实际积分")
    private Integer actualScore;

    /** 状态（0正常 1停用） */
    private String status;

    private Long patentId;

    public void setPatentId(Long patentId) { this.patentId = patentId; }
    public Long getPatentId() { return patentId; }

    public void setPatentName(String patentName) { this.patentName = patentName; }
    public String getPatentName() { return patentName; }

    public void setGradeId(Long gradeId) { this.gradeId = gradeId; }
    public Long getGradeId() { return gradeId; }

    public void setGradeName(String gradeName) { this.gradeName = gradeName; }
    public String getGradeName() { return gradeName; }

    public void setGradeCode(String gradeCode) { this.gradeCode = gradeCode; }
    public String getGradeCode() { return gradeCode; }

    public void setProfessorId(Long professorId) { this.professorId = professorId; }
    public Long getProfessorId() { return professorId; }

    public void setProfessorName(String professorName) { this.professorName = professorName; }
    public String getProfessorName() { return professorName; }

    public void setInventorRank(String inventorRank) { this.inventorRank = inventorRank; }
    public String getInventorRank() { return inventorRank; }

    public void setPatentNumber(String patentNumber) { this.patentNumber = patentNumber; }
    public String getPatentNumber() { return patentNumber; }

    public void setPatentCountry(String patentCountry) { this.patentCountry = patentCountry; }
    public String getPatentCountry() { return patentCountry; }

    public void setApplicationDate(Date applicationDate) { this.applicationDate = applicationDate; }
    public Date getApplicationDate() { return applicationDate; }

    public void setGrantDate(Date grantDate) { this.grantDate = grantDate; }
    public Date getGrantDate() { return grantDate; }

    public void setActualScore(Integer actualScore) { this.actualScore = actualScore; }
    public Integer getActualScore() { return actualScore; }

    public void setStatus(String status) { this.status = status; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("patentId", getPatentId())
            .append("patentName", getPatentName())
            .append("gradeId", getGradeId())
            .append("gradeName", getGradeName())
            .append("professorId", getProfessorId())
            .append("professorName", getProfessorName())
            .append("inventorRank", getInventorRank())
            .append("patentNumber", getPatentNumber())
            .append("patentCountry", getPatentCountry())
            .append("applicationDate", getApplicationDate())
            .append("grantDate", getGrantDate())
            .append("actualScore", getActualScore())
            .append("status", getStatus())
            .toString();
    }
}
