package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class MsAward extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long awardId;
    @Excel(name="获奖名称") private String awardName;
    private Long gradeId;
    @Excel(name="获奖等级") private String gradeName;
    private Long professorId;
    @Excel(name="获奖人") private String professorName;
    @Excel(name="完成人排名") private String awardRank;
    @Excel(name="颁发单位") private String awardOrg;
    @JsonFormat(pattern="yyyy-MM-dd") @Excel(name="获奖日期",width=30,dateFormat="yyyy-MM-dd") private Date awardDate;
    @Excel(name="证书编号") private String certNumber;
    @Excel(name="积分") private Integer actualScore;
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用") private String status;

    public Long getAwardId(){return awardId;} public void setAwardId(Long v){awardId=v;}
    public String getAwardName(){return awardName;} public void setAwardName(String v){awardName=v;}
    public Long getGradeId(){return gradeId;} public void setGradeId(Long v){gradeId=v;}
    public String getGradeName(){return gradeName;} public void setGradeName(String v){gradeName=v;}
    public Long getProfessorId(){return professorId;} public void setProfessorId(Long v){professorId=v;}
    public String getProfessorName(){return professorName;} public void setProfessorName(String v){professorName=v;}
    public String getAwardRank(){return awardRank;} public void setAwardRank(String v){awardRank=v;}
    public String getAwardOrg(){return awardOrg;} public void setAwardOrg(String v){awardOrg=v;}
    public Date getAwardDate(){return awardDate;} public void setAwardDate(Date v){awardDate=v;}
    public String getCertNumber(){return certNumber;} public void setCertNumber(String v){certNumber=v;}
    public Integer getActualScore(){return actualScore;} public void setActualScore(Integer v){actualScore=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
}
