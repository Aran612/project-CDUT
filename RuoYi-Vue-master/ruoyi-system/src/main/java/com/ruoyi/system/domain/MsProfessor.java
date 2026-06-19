package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 教授信息对象 ms_professor
 *
 * @author 王祎哲
 * @date 2026-06-12
 */
public class MsProfessor extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 教授ID */
    private Long professorId;

    /** 关联系统用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 姓名 */
    @Excel(name = "姓名")
    private String name;

    /** 所属系/部门 */
    @Excel(name = "所属部门")
    private String department;

    /** 职称 */
    @Excel(name = "职称", readConverterExp = "教授=教授,副教授=副教授,讲师=讲师,助教=助教")
    private String title;

    /** 研究方向 */
    @Excel(name = "研究方向")
    private String researchField;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 累计科研积分 */
    @Excel(name = "累计积分")
    private Integer totalScore;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setProfessorId(Long professorId)
    {
        this.professorId = professorId;
    }

    public Long getProfessorId()
    {
        return professorId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setDepartment(String department)
    {
        this.department = department;
    }

    public String getDepartment()
    {
        return department;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }

    public void setResearchField(String researchField)
    {
        this.researchField = researchField;
    }

    public String getResearchField()
    {
        return researchField;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }

    public void setTotalScore(Integer totalScore)
    {
        this.totalScore = totalScore;
    }

    public Integer getTotalScore()
    {
        return totalScore;
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
            .append("professorId", getProfessorId())
            .append("userId", getUserId())
            .append("name", getName())
            .append("department", getDepartment())
            .append("title", getTitle())
            .append("researchField", getResearchField())
            .append("email", getEmail())
            .append("totalScore", getTotalScore())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
