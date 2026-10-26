package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * AoL学习目标对象 aol_lg
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
public class AolLg extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 学习目标ID */
    private Long lgId;

    /** 能力目标ID */
    private Long cgId;

    /** 编号（1.1/1.2...） */
    @Excel(name = "编号")
    private String lgCode;

    /** 描述（中文） */
    @Excel(name = "描述")
    private String description;

    /** 描述（英文） */
    @Excel(name = "英文描述")
    private String descriptionEn;

    /** 布鲁姆层级（L1记忆 L2理解 L3应用 L4分析 L5评价 L6创造） */
    @Excel(name = "布鲁姆层级")
    private String bloomLevel;

    /** 布鲁姆行为动词（顿号分隔） */
    @Excel(name = "行为动词")
    private String bloomVerbs;

    /** 排序 */
    private Integer sortOrder;

    /** 状态（0正常 1停用） */
    private String status;

    public void setLgId(Long lgId)
    {
        this.lgId = lgId;
    }

    public Long getLgId()
    {
        return lgId;
    }

    public void setCgId(Long cgId)
    {
        this.cgId = cgId;
    }

    public Long getCgId()
    {
        return cgId;
    }

    public void setLgCode(String lgCode)
    {
        this.lgCode = lgCode;
    }

    public String getLgCode()
    {
        return lgCode;
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

    public void setBloomLevel(String bloomLevel)
    {
        this.bloomLevel = bloomLevel;
    }

    public String getBloomLevel()
    {
        return bloomLevel;
    }

    public void setBloomVerbs(String bloomVerbs)
    {
        this.bloomVerbs = bloomVerbs;
    }

    public String getBloomVerbs()
    {
        return bloomVerbs;
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
            .append("lgId", getLgId())
            .append("cgId", getCgId())
            .append("lgCode", getLgCode())
            .append("description", getDescription())
            .append("descriptionEn", getDescriptionEn())
            .append("bloomLevel", getBloomLevel())
            .append("bloomVerbs", getBloomVerbs())
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
