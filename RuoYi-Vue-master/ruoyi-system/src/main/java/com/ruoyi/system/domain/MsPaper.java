package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 论文对象 ms_paper
 *
 * @author 王祎哲
 * @date 2026-06-12
 */
public class MsPaper extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 论文ID */
    private Long paperId;

    /** 论文名称 */
    @Excel(name = "论文名称")
    private String title;

    /** 论文等级ID */
    @Excel(name = "论文等级ID", type = Excel.Type.IMPORT)
    private Long gradeId;

    /** 论文等级名称（用于导入导出显示） */
    @Excel(name = "论文等级", type = Excel.Type.IMPORT)
    private String gradeName;

    /** 是否仅用于岗位聘期考核（来自ms_paper_grade关联，仅用于显示） */
    private String forEvaluationOnly;

    /** 教授ID */
    @Excel(name = "教授ID", type = Excel.Type.IMPORT)
    private Long professorId;

    /** 教授姓名（用于导入导出显示） */
    @Excel(name = "教授姓名")
    private String professorName;

    /** 作者身份 */
    @Excel(name = "作者身份", readConverterExp = "第一作者=第一作者,通讯作者=通讯作者,其他=其他")
    private String authorPosition;

    /** 期刊/会议名称 */
    @Excel(name = "期刊/会议名称")
    private String journalName;

    /** 发表时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发表时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date publishDate;

    /** DOI号 */
    @Excel(name = "DOI号")
    private String doi;

    /** 论文链接 */
    @Excel(name = "论文链接")
    private String paperUrl;

    /** 摘要 */
    private String abstractText;

    /** 实际积分 */
    @Excel(name = "实际积分")
    private Integer actualScore;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setPaperId(Long paperId)
    {
        this.paperId = paperId;
    }

    public Long getPaperId()
    {
        return paperId;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }

    public void setGradeId(Long gradeId)
    {
        this.gradeId = gradeId;
    }

    public Long getGradeId()
    {
        return gradeId;
    }

    public void setGradeName(String gradeName)
    {
        this.gradeName = gradeName;
    }

    public String getGradeName()
    {
        return gradeName;
    }

    public void setForEvaluationOnly(String forEvaluationOnly)
    {
        this.forEvaluationOnly = forEvaluationOnly;
    }

    public String getForEvaluationOnly()
    {
        return forEvaluationOnly;
    }

    public void setProfessorId(Long professorId)
    {
        this.professorId = professorId;
    }

    public Long getProfessorId()
    {
        return professorId;
    }

    public void setProfessorName(String professorName)
    {
        this.professorName = professorName;
    }

    public String getProfessorName()
    {
        return professorName;
    }

    public void setAuthorPosition(String authorPosition)
    {
        this.authorPosition = authorPosition;
    }

    public String getAuthorPosition()
    {
        return authorPosition;
    }

    public void setJournalName(String journalName)
    {
        this.journalName = journalName;
    }

    public String getJournalName()
    {
        return journalName;
    }

    public void setPublishDate(Date publishDate)
    {
        this.publishDate = publishDate;
    }

    public Date getPublishDate()
    {
        return publishDate;
    }

    public void setDoi(String doi)
    {
        this.doi = doi;
    }

    public String getDoi()
    {
        return doi;
    }

    public void setPaperUrl(String paperUrl)
    {
        this.paperUrl = paperUrl;
    }

    public String getPaperUrl()
    {
        return paperUrl;
    }

    public void setAbstractText(String abstractText)
    {
        this.abstractText = abstractText;
    }

    public String getAbstractText()
    {
        return abstractText;
    }

    public void setActualScore(Integer actualScore)
    {
        this.actualScore = actualScore;
    }

    public Integer getActualScore()
    {
        return actualScore;
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
            .append("paperId", getPaperId())
            .append("title", getTitle())
            .append("gradeId", getGradeId())
            .append("professorId", getProfessorId())
            .append("authorPosition", getAuthorPosition())
            .append("journalName", getJournalName())
            .append("publishDate", getPublishDate())
            .append("doi", getDoi())
            .append("paperUrl", getPaperUrl())
            .append("abstractText", getAbstractText())
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
