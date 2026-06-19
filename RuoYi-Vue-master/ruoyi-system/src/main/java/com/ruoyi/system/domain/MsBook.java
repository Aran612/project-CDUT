package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class MsBook extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long bookId;
    @Excel(name="著作名称") private String bookName;
    private Long gradeId;
    @Excel(name="著作等级") private String gradeName;
    private Long professorId;
    @Excel(name="作者姓名") private String professorName;
    @Excel(name="作者角色") private String authorRole;
    @Excel(name="出版社") private String publisher;
    @JsonFormat(pattern="yyyy-MM-dd") @Excel(name="出版日期",width=30,dateFormat="yyyy-MM-dd") private Date publishDate;
    @Excel(name="字数(万)") private Integer wordCount;
    @Excel(name="ISBN") private String isbn;
    @Excel(name="积分") private Integer actualScore;
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用") private String status;

    public Long getBookId(){return bookId;} public void setBookId(Long v){bookId=v;}
    public String getBookName(){return bookName;} public void setBookName(String v){bookName=v;}
    public Long getGradeId(){return gradeId;} public void setGradeId(Long v){gradeId=v;}
    public String getGradeName(){return gradeName;} public void setGradeName(String v){gradeName=v;}
    public Long getProfessorId(){return professorId;} public void setProfessorId(Long v){professorId=v;}
    public String getProfessorName(){return professorName;} public void setProfessorName(String v){professorName=v;}
    public String getAuthorRole(){return authorRole;} public void setAuthorRole(String v){authorRole=v;}
    public String getPublisher(){return publisher;} public void setPublisher(String v){publisher=v;}
    public Date getPublishDate(){return publishDate;} public void setPublishDate(Date v){publishDate=v;}
    public Integer getWordCount(){return wordCount;} public void setWordCount(Integer v){wordCount=v;}
    public String getIsbn(){return isbn;} public void setIsbn(String v){isbn=v;}
    public Integer getActualScore(){return actualScore;} public void setActualScore(Integer v){actualScore=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
}
