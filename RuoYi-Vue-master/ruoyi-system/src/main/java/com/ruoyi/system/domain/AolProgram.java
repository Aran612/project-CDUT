package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * AoL学位项目对象 aol_program
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
public class AolProgram extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 学位项目ID */
    private Long programId;

    /** 项目名称（中文） */
    @Excel(name = "项目名称")
    private String programName;

    /** 项目名称（英文） */
    @Excel(name = "英文名称")
    private String programNameEn;

    /** 学位层次（1=本科 2=硕士 3=博士） */
    @Excel(name = "学位层次", readConverterExp = "1=本科,2=硕士,3=博士")
    private String degreeLevel;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setProgramId(Long programId)
    {
        this.programId = programId;
    }

    public Long getProgramId()
    {
        return programId;
    }

    public void setProgramName(String programName)
    {
        this.programName = programName;
    }

    public String getProgramName()
    {
        return programName;
    }

    public void setProgramNameEn(String programNameEn)
    {
        this.programNameEn = programNameEn;
    }

    public String getProgramNameEn()
    {
        return programNameEn;
    }

    public void setDegreeLevel(String degreeLevel)
    {
        this.degreeLevel = degreeLevel;
    }

    public String getDegreeLevel()
    {
        return degreeLevel;
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
            .append("programId", getProgramId())
            .append("programName", getProgramName())
            .append("programNameEn", getProgramNameEn())
            .append("degreeLevel", getDegreeLevel())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
