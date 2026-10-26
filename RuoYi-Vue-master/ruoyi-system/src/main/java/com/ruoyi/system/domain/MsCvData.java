package com.ruoyi.system.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 教师CV聚合数据对象（前端一次拉取全部CV数据）
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
public class MsCvData implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** CV档案 */
    private MsCvProfile profile;

    /** 教授信息 */
    private MsProfessor professor;

    /** 教育背景列表 */
    private List<MsCvEducation> educations;

    /** 工作经历列表（含学术与行业两类） */
    private List<MsCvExperience> experiences;

    /** 职业资格列表 */
    private List<MsCvCertification> certifications;

    /** 授课课程列表 */
    private List<MsCvCourse> courses;

    /** 职业发展列表 */
    private List<MsCvDevelopment> developments;

    /** 社会服务列表 */
    private List<MsCvService> services;

    /** 期刊论文列表（按时间窗口过滤） */
    private List<MsPaper> papers;

    /** 专著列表（按时间窗口过滤） */
    private List<MsBook> books;

    /** 专利列表（按时间窗口过滤） */
    private List<MsPatent> patents;

    /** 荣誉获奖列表（按时间窗口过滤） */
    private List<MsAward> awards;

    /** 科研项目列表（按时间窗口过滤） */
    private List<MsResearchProject> projects;

    /** 论文等级字典（全量） */
    private List<MsPaperGrade> paperGrades;

    /** 项目等级字典（全量） */
    private List<MsProjectGrade> projectGrades;

    /** 专利等级字典（全量） */
    private List<MsPatentGrade> patentGrades;

    /** 著作等级字典（全量） */
    private List<MsBookGrade> bookGrades;

    /** 获奖等级字典（全量） */
    private List<MsAwardGrade> awardGrades;

    /** 科研成果统计年限（年） */
    private int yearWindow;

    public void setProfile(MsCvProfile profile)
    {
        this.profile = profile;
    }

    public MsCvProfile getProfile()
    {
        return profile;
    }

    public void setProfessor(MsProfessor professor)
    {
        this.professor = professor;
    }

    public MsProfessor getProfessor()
    {
        return professor;
    }

    public void setEducations(List<MsCvEducation> educations)
    {
        this.educations = educations;
    }

    public List<MsCvEducation> getEducations()
    {
        return educations;
    }

    public void setExperiences(List<MsCvExperience> experiences)
    {
        this.experiences = experiences;
    }

    public List<MsCvExperience> getExperiences()
    {
        return experiences;
    }

    public void setCertifications(List<MsCvCertification> certifications)
    {
        this.certifications = certifications;
    }

    public List<MsCvCertification> getCertifications()
    {
        return certifications;
    }

    public void setCourses(List<MsCvCourse> courses)
    {
        this.courses = courses;
    }

    public List<MsCvCourse> getCourses()
    {
        return courses;
    }

    public void setDevelopments(List<MsCvDevelopment> developments)
    {
        this.developments = developments;
    }

    public List<MsCvDevelopment> getDevelopments()
    {
        return developments;
    }

    public void setServices(List<MsCvService> services)
    {
        this.services = services;
    }

    public List<MsCvService> getServices()
    {
        return services;
    }

    public void setPapers(List<MsPaper> papers)
    {
        this.papers = papers;
    }

    public List<MsPaper> getPapers()
    {
        return papers;
    }

    public void setBooks(List<MsBook> books)
    {
        this.books = books;
    }

    public List<MsBook> getBooks()
    {
        return books;
    }

    public void setPatents(List<MsPatent> patents)
    {
        this.patents = patents;
    }

    public List<MsPatent> getPatents()
    {
        return patents;
    }

    public void setAwards(List<MsAward> awards)
    {
        this.awards = awards;
    }

    public List<MsAward> getAwards()
    {
        return awards;
    }

    public void setProjects(List<MsResearchProject> projects)
    {
        this.projects = projects;
    }

    public List<MsResearchProject> getProjects()
    {
        return projects;
    }

    public void setPaperGrades(List<MsPaperGrade> paperGrades)
    {
        this.paperGrades = paperGrades;
    }

    public List<MsPaperGrade> getPaperGrades()
    {
        return paperGrades;
    }

    public void setProjectGrades(List<MsProjectGrade> projectGrades)
    {
        this.projectGrades = projectGrades;
    }

    public List<MsProjectGrade> getProjectGrades()
    {
        return projectGrades;
    }

    public void setPatentGrades(List<MsPatentGrade> patentGrades)
    {
        this.patentGrades = patentGrades;
    }

    public List<MsPatentGrade> getPatentGrades()
    {
        return patentGrades;
    }

    public void setBookGrades(List<MsBookGrade> bookGrades)
    {
        this.bookGrades = bookGrades;
    }

    public List<MsBookGrade> getBookGrades()
    {
        return bookGrades;
    }

    public void setAwardGrades(List<MsAwardGrade> awardGrades)
    {
        this.awardGrades = awardGrades;
    }

    public List<MsAwardGrade> getAwardGrades()
    {
        return awardGrades;
    }

    public void setYearWindow(int yearWindow)
    {
        this.yearWindow = yearWindow;
    }

    public int getYearWindow()
    {
        return yearWindow;
    }
}
