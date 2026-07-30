package com.ruoyi.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 学生学分记录对象 ms_student_credit
 *
 * @author CDUT-ALPHA
 * @date 2026-07-26
 */
public class MsStudentCredit extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 学分记录ID */
    private Long creditId;

    /** 学生姓名 */
    @Excel(name = "学生姓名")
    private String studentName;

    /** 学生学号 */
    @Excel(name = "学号")
    private String studentId;

    /** 所属院系 */
    @Excel(name = "所属院系")
    private String departmentName;

    /** 累计总学分 */
    @Excel(name = "累计学分")
    private BigDecimal totalCredits;

    /** 学期 */
    @Excel(name = "学期")
    private String semester;

    /** 状态（0=正常 1=停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setCreditId(Long creditId)
    {
        this.creditId = creditId;
    }

    public Long getCreditId()
    {
        return creditId;
    }

    public void setStudentName(String studentName)
    {
        this.studentName = studentName;
    }

    public String getStudentName()
    {
        return studentName;
    }

    public void setStudentId(String studentId)
    {
        this.studentId = studentId;
    }

    public String getStudentId()
    {
        return studentId;
    }

    public void setDepartmentName(String departmentName)
    {
        this.departmentName = departmentName;
    }

    public String getDepartmentName()
    {
        return departmentName;
    }

    public void setTotalCredits(BigDecimal totalCredits)
    {
        this.totalCredits = totalCredits;
    }

    public BigDecimal getTotalCredits()
    {
        return totalCredits;
    }

    public void setSemester(String semester)
    {
        this.semester = semester;
    }

    public String getSemester()
    {
        return semester;
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
            .append("creditId", getCreditId())
            .append("studentName", getStudentName())
            .append("studentId", getStudentId())
            .append("departmentName", getDepartmentName())
            .append("totalCredits", getTotalCredits())
            .append("semester", getSemester())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
