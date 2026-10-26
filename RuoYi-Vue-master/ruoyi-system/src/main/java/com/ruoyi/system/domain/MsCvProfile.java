package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 教师CV档案对象 ms_cv_profile
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
public class MsCvProfile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** CV档案ID */
    private Long cvId;

    /** 教师ID（ms_professor） */
    private Long professorId;

    /** CDUT工号（员工编号） */
    private String cdutId;

    /** 证件照URL */
    private String photoUrl;

    /** 联系电话 */
    private String phone;

    /** 岗位/职务 */
    private String position;

    /** 任教类型（1=本院全职 2=外院兼职） */
    private String employmentType;

    /** AACSB师资分类（SA/PA/SP/IP/A） */
    private String aacsbClass;

    /** 所属单位 */
    private String employer;

    /** 通讯地址 */
    private String address;

    /** 研究与教学领域（每行一条，3-5条） */
    private String areasOfInterest;

    /** 博士后/访学经历补充（CV第3部分） */
    private String otherInfo;

    /** 已确认真实性声明（0否 1是） */
    private String declareFlag;

    /** 声明确认时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date declareTime;

    /** 状态（0正常 1停用） */
    private String status;

    public void setCvId(Long cvId)
    {
        this.cvId = cvId;
    }

    public Long getCvId()
    {
        return cvId;
    }

    public void setProfessorId(Long professorId)
    {
        this.professorId = professorId;
    }

    public Long getProfessorId()
    {
        return professorId;
    }

    public void setCdutId(String cdutId)
    {
        this.cdutId = cdutId;
    }

    public String getCdutId()
    {
        return cdutId;
    }

    public void setPhotoUrl(String photoUrl)
    {
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl()
    {
        return photoUrl;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPosition(String position)
    {
        this.position = position;
    }

    public String getPosition()
    {
        return position;
    }

    public void setEmploymentType(String employmentType)
    {
        this.employmentType = employmentType;
    }

    public String getEmploymentType()
    {
        return employmentType;
    }

    public void setAacsbClass(String aacsbClass)
    {
        this.aacsbClass = aacsbClass;
    }

    public String getAacsbClass()
    {
        return aacsbClass;
    }

    public void setEmployer(String employer)
    {
        this.employer = employer;
    }

    public String getEmployer()
    {
        return employer;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAreasOfInterest(String areasOfInterest)
    {
        this.areasOfInterest = areasOfInterest;
    }

    public String getAreasOfInterest()
    {
        return areasOfInterest;
    }

    public void setOtherInfo(String otherInfo)
    {
        this.otherInfo = otherInfo;
    }

    public String getOtherInfo()
    {
        return otherInfo;
    }

    public void setDeclareFlag(String declareFlag)
    {
        this.declareFlag = declareFlag;
    }

    public String getDeclareFlag()
    {
        return declareFlag;
    }

    public void setDeclareTime(Date declareTime)
    {
        this.declareTime = declareTime;
    }

    public Date getDeclareTime()
    {
        return declareTime;
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
            .append("cvId", getCvId())
            .append("professorId", getProfessorId())
            .append("cdutId", getCdutId())
            .append("photoUrl", getPhotoUrl())
            .append("phone", getPhone())
            .append("position", getPosition())
            .append("employmentType", getEmploymentType())
            .append("aacsbClass", getAacsbClass())
            .append("employer", getEmployer())
            .append("address", getAddress())
            .append("areasOfInterest", getAreasOfInterest())
            .append("otherInfo", getOtherInfo())
            .append("declareFlag", getDeclareFlag())
            .append("declareTime", getDeclareTime())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
