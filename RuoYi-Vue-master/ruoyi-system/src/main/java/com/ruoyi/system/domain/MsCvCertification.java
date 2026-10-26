package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * CV职业资格对象 ms_cv_certification
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
public class MsCvCertification extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 职业资格ID */
    private Long certId;

    /** 教师ID */
    private Long professorId;

    /** 证书名称（如CPA/CFA/ACCA/FRM） */
    private String certName;

    /** 颁发机构 */
    private String issuingBody;

    /** 取得年份 */
    private String certYear;

    /** 现状（有效/已更新） */
    private String statusText;

    /** 排序 */
    private Integer sortOrder;

    /** 状态（0正常 1停用） */
    private String status;

    public void setCertId(Long certId)
    {
        this.certId = certId;
    }

    public Long getCertId()
    {
        return certId;
    }

    public void setProfessorId(Long professorId)
    {
        this.professorId = professorId;
    }

    public Long getProfessorId()
    {
        return professorId;
    }

    public void setCertName(String certName)
    {
        this.certName = certName;
    }

    public String getCertName()
    {
        return certName;
    }

    public void setIssuingBody(String issuingBody)
    {
        this.issuingBody = issuingBody;
    }

    public String getIssuingBody()
    {
        return issuingBody;
    }

    public void setCertYear(String certYear)
    {
        this.certYear = certYear;
    }

    public String getCertYear()
    {
        return certYear;
    }

    public void setStatusText(String statusText)
    {
        this.statusText = statusText;
    }

    public String getStatusText()
    {
        return statusText;
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
            .append("certId", getCertId())
            .append("professorId", getProfessorId())
            .append("certName", getCertName())
            .append("issuingBody", getIssuingBody())
            .append("certYear", getCertYear())
            .append("statusText", getStatusText())
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
