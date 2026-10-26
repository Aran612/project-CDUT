package com.ruoyi.system.domain;

import java.io.Serializable;

/**
 * AoL课程委员会对象 aol_course_committee
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
public class AolCourseCommittee implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 委员会成员ID */
    private Long committeeId;

    /** 学位项目ID */
    private Long programId;

    /** 图谱课程ID */
    private Long mapCourseId;

    /** 教师ID（ms_professor） */
    private Long professorId;

    /** 成员序号（1-3） */
    private Integer memberOrder;

    /** 教师姓名（查询字段，来自ms_professor） */
    private String professorName;

    /** CDUT工号（查询字段，来自ms_cv_profile） */
    private String cdutId;

    /** 课程名称（查询字段，来自aol_map_course） */
    private String courseName;

    public void setCommitteeId(Long committeeId)
    {
        this.committeeId = committeeId;
    }

    public Long getCommitteeId()
    {
        return committeeId;
    }

    public void setProgramId(Long programId)
    {
        this.programId = programId;
    }

    public Long getProgramId()
    {
        return programId;
    }

    public void setMapCourseId(Long mapCourseId)
    {
        this.mapCourseId = mapCourseId;
    }

    public Long getMapCourseId()
    {
        return mapCourseId;
    }

    public void setProfessorId(Long professorId)
    {
        this.professorId = professorId;
    }

    public Long getProfessorId()
    {
        return professorId;
    }

    public void setMemberOrder(Integer memberOrder)
    {
        this.memberOrder = memberOrder;
    }

    public Integer getMemberOrder()
    {
        return memberOrder;
    }

    public void setProfessorName(String professorName)
    {
        this.professorName = professorName;
    }

    public String getProfessorName()
    {
        return professorName;
    }

    public void setCdutId(String cdutId)
    {
        this.cdutId = cdutId;
    }

    public String getCdutId()
    {
        return cdutId;
    }

    public void setCourseName(String courseName)
    {
        this.courseName = courseName;
    }

    public String getCourseName()
    {
        return courseName;
    }
}
