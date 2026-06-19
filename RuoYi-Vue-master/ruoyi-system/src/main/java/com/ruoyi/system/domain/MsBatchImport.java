package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 一键导入统一对象 — 映射 Excel 行到各子模块
 */
public class MsBatchImport extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Excel(name = "成果类型") private String achievementType;  // 论文/项目/专利/著作/获奖

    // 论文字段
    @Excel(name = "论文标题") private String title;
    @Excel(name = "作者身份") private String authorPosition;
    @Excel(name = "期刊名称") private String journalName;
    @Excel(name = "DOI") private String doi;
    @Excel(name = "论文链接") private String paperUrl;
    @Excel(name = "摘要") private String abstractText;

    // 项目字段
    @Excel(name = "项目名称") private String projectName;
    @Excel(name = "承担角色") private String role;
    @Excel(name = "经费(万元)") private String fundingAmountStr;
    @Excel(name = "批准号") private String approvalNumber;
    @Excel(name = "项目来源") private String projectSource;

    // 专利字段
    @Excel(name = "专利名称") private String patentName;
    @Excel(name = "发明人排名") private String inventorRank;
    @Excel(name = "专利号") private String patentNumber;
    @Excel(name = "授权国家") private String patentCountry;

    // 著作字段
    @Excel(name = "著作名称") private String bookName;
    @Excel(name = "作者角色") private String authorRole;
    @Excel(name = "出版社") private String publisher;
    @Excel(name = "字数(万)") private Integer wordCount;
    @Excel(name = "ISBN") private String isbn;

    // 获奖字段
    @Excel(name = "获奖名称") private String awardName;
    @Excel(name = "完成人排名") private String awardRank;
    @Excel(name = "颁发单位") private String awardOrg;
    @Excel(name = "证书编号") private String certNumber;

    // 通用字段
    @Excel(name = "等级名称") private String gradeName;
    @Excel(name = "发表/开始日期") private String dateStr;
    @Excel(name = "结束日期") private String endDateStr;
    @Excel(name = "实际积分") private Integer actualScore;

    // Getters & Setters
    public String getAchievementType() { return achievementType; }
    public void setAchievementType(String v) { achievementType = v; }
    public String getTitle() { return title; }
    public void setTitle(String v) { title = v; }
    public String getAuthorPosition() { return authorPosition; }
    public void setAuthorPosition(String v) { authorPosition = v; }
    public String getJournalName() { return journalName; }
    public void setJournalName(String v) { journalName = v; }
    public String getDoi() { return doi; }
    public void setDoi(String v) { doi = v; }
    public String getPaperUrl() { return paperUrl; }
    public void setPaperUrl(String v) { paperUrl = v; }
    public String getAbstractText() { return abstractText; }
    public void setAbstractText(String v) { abstractText = v; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String v) { projectName = v; }
    public String getRole() { return role; }
    public void setRole(String v) { role = v; }
    public String getFundingAmountStr() { return fundingAmountStr; }
    public void setFundingAmountStr(String v) { fundingAmountStr = v; }
    public String getApprovalNumber() { return approvalNumber; }
    public void setApprovalNumber(String v) { approvalNumber = v; }
    public String getProjectSource() { return projectSource; }
    public void setProjectSource(String v) { projectSource = v; }
    public String getPatentName() { return patentName; }
    public void setPatentName(String v) { patentName = v; }
    public String getInventorRank() { return inventorRank; }
    public void setInventorRank(String v) { inventorRank = v; }
    public String getPatentNumber() { return patentNumber; }
    public void setPatentNumber(String v) { patentNumber = v; }
    public String getPatentCountry() { return patentCountry; }
    public void setPatentCountry(String v) { patentCountry = v; }
    public String getBookName() { return bookName; }
    public void setBookName(String v) { bookName = v; }
    public String getAuthorRole() { return authorRole; }
    public void setAuthorRole(String v) { authorRole = v; }
    public String getPublisher() { return publisher; }
    public void setPublisher(String v) { publisher = v; }
    public Integer getWordCount() { return wordCount; }
    public void setWordCount(Integer v) { wordCount = v; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String v) { isbn = v; }
    public String getAwardName() { return awardName; }
    public void setAwardName(String v) { awardName = v; }
    public String getAwardRank() { return awardRank; }
    public void setAwardRank(String v) { awardRank = v; }
    public String getAwardOrg() { return awardOrg; }
    public void setAwardOrg(String v) { awardOrg = v; }
    public String getCertNumber() { return certNumber; }
    public void setCertNumber(String v) { certNumber = v; }
    public String getGradeName() { return gradeName; }
    public void setGradeName(String v) { gradeName = v; }
    public String getDateStr() { return dateStr; }
    public void setDateStr(String v) { dateStr = v; }
    public String getEndDateStr() { return endDateStr; }
    public void setEndDateStr(String v) { endDateStr = v; }
    public Integer getActualScore() { return actualScore; }
    public void setActualScore(Integer v) { actualScore = v; }
}
