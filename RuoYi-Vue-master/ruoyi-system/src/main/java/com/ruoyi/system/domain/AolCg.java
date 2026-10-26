package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * AoL能力目标对象 aol_cg
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
public class AolCg extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 能力目标ID */
    private Long cgId;

    /** 学位项目ID */
    private Long programId;

    /** 编号（CG1/CG2...） */
    @Excel(name = "编号")
    private String cgCode;

    /** 名称（中文） */
    @Excel(name = "名称")
    private String cgName;

    /** 名称（英文） */
    @Excel(name = "英文名称")
    private String cgNameEn;

    /** 描述（中文） */
    @Excel(name = "描述")
    private String description;

    /** 描述（英文） */
    @Excel(name = "英文描述")
    private String descriptionEn;

    /** 排序 */
    private Integer sortOrder;

    /** 状态（0正常 1停用） */
    private String status;

    public void setCgId(Long cgId)
    {
        this.cgId = cgId;
    }

    public Long getCgId()
    {
        return cgId;
    }

    public void setProgramId(Long programId)
    {
        this.programId = programId;
    }

    public Long getProgramId()
    {
        return programId;
    }

    public void setCgCode(String cgCode)
    {
        this.cgCode = cgCode;
    }

    public String getCgCode()
    {
        return cgCode;
    }

    public void setCgName(String cgName)
    {
        this.cgName = cgName;
    }

    public String getCgName()
    {
        return cgName;
    }

    public void setCgNameEn(String cgNameEn)
    {
        this.cgNameEn = cgNameEn;
    }

    public String getCgNameEn()
    {
        return cgNameEn;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescriptionEn(String descriptionEn)
    {
        this.descriptionEn = descriptionEn;
    }

    public String getDescriptionEn()
    {
        return descriptionEn;
    }

    public void setSortOrder(Integer sortOrder)
    {
        this.sortOrder = sortOrder;
    }

    public Integer getSortOrder()
    {
        return sortOrder;
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
            .append("cgId", getCgId())
            .append("programId", getProgramId())
            .append("cgCode", getCgCode())
            .append("cgName", getCgName())
            .append("cgNameEn", getCgNameEn())
            .append("description", getDescription())
            .append("descriptionEn", getDescriptionEn())
            .append("sortOrder", getSortOrder())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
