package com.ruoyi.system.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.MsAward;
import com.ruoyi.system.domain.MsBook;
import com.ruoyi.system.domain.MsCvCertification;
import com.ruoyi.system.domain.MsCvCourse;
import com.ruoyi.system.domain.MsCvData;
import com.ruoyi.system.domain.MsCvDevelopment;
import com.ruoyi.system.domain.MsCvEducation;
import com.ruoyi.system.domain.MsCvExperience;
import com.ruoyi.system.domain.MsCvProfile;
import com.ruoyi.system.domain.MsCvService;
import com.ruoyi.system.domain.MsPaper;
import com.ruoyi.system.domain.MsPatent;
import com.ruoyi.system.domain.MsProfessor;
import com.ruoyi.system.domain.MsResearchProject;
import com.ruoyi.system.domain.MsTeachingRecord;
import com.ruoyi.system.mapper.MsAwardGradeMapper;
import com.ruoyi.system.mapper.MsAwardMapper;
import com.ruoyi.system.mapper.MsBookGradeMapper;
import com.ruoyi.system.mapper.MsBookMapper;
import com.ruoyi.system.mapper.MsCvCertificationMapper;
import com.ruoyi.system.mapper.MsCvCourseMapper;
import com.ruoyi.system.mapper.MsCvDevelopmentMapper;
import com.ruoyi.system.mapper.MsCvEducationMapper;
import com.ruoyi.system.mapper.MsCvExperienceMapper;
import com.ruoyi.system.mapper.MsCvProfileMapper;
import com.ruoyi.system.mapper.MsCvServiceMapper;
import com.ruoyi.system.mapper.MsPaperGradeMapper;
import com.ruoyi.system.mapper.MsPaperMapper;
import com.ruoyi.system.mapper.MsPatentGradeMapper;
import com.ruoyi.system.mapper.MsPatentMapper;
import com.ruoyi.system.mapper.MsProjectGradeMapper;
import com.ruoyi.system.mapper.MsResearchProjectMapper;
import com.ruoyi.system.service.IMsAwardService;
import com.ruoyi.system.service.IMsBookService;
import com.ruoyi.system.service.IMsCvService;
import com.ruoyi.system.service.IMsPaperService;
import com.ruoyi.system.service.IMsPatentService;
import com.ruoyi.system.service.IMsProfessorService;
import com.ruoyi.system.service.IMsResearchProjectService;
import com.ruoyi.system.service.IMsTeachingRecordService;
import com.ruoyi.system.service.ISysConfigService;

/**
 * 教师CV简历Service业务层处理
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
@Service
public class MsCvServiceImpl implements IMsCvService
{
    /** CV科研成果统计年限参数键 */
    private static final String CONFIG_KEY_YEAR_WINDOW = "cv.year.window";

    /** 科研成果统计年限默认值 */
    private static final int DEFAULT_YEAR_WINDOW = 6;

    @Autowired
    private MsCvProfileMapper cvProfileMapper;

    @Autowired
    private MsCvEducationMapper cvEducationMapper;

    @Autowired
    private MsCvExperienceMapper cvExperienceMapper;

    @Autowired
    private MsCvCertificationMapper cvCertificationMapper;

    @Autowired
    private MsCvCourseMapper cvCourseMapper;

    @Autowired
    private MsCvDevelopmentMapper cvDevelopmentMapper;

    @Autowired
    private MsCvServiceMapper cvServiceMapper;

    @Autowired
    private IMsProfessorService professorService;

    @Autowired
    private IMsTeachingRecordService teachingRecordService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private MsPaperMapper paperMapper;

    @Autowired
    private MsBookMapper bookMapper;

    @Autowired
    private MsPatentMapper patentMapper;

    @Autowired
    private MsAwardMapper awardMapper;

    @Autowired
    private MsResearchProjectMapper researchProjectMapper;

    @Autowired
    private MsPaperGradeMapper paperGradeMapper;

    @Autowired
    private MsBookGradeMapper bookGradeMapper;

    @Autowired
    private MsPatentGradeMapper patentGradeMapper;

    @Autowired
    private MsAwardGradeMapper awardGradeMapper;

    @Autowired
    private MsProjectGradeMapper projectGradeMapper;

    @Autowired
    private IMsPaperService paperService;

    @Autowired
    private IMsBookService bookService;

    @Autowired
    private IMsPatentService patentService;

    @Autowired
    private IMsAwardService awardService;

    @Autowired
    private IMsResearchProjectService researchProjectService;

    @Autowired
    private MsCvWordExporter cvWordExporter;

    /**
     * 获取教师CV聚合数据（档案不存在时自动创建）
     */
    @Override
    public MsCvData getCvData(Long professorId, boolean createProfileIfMissing)
    {
        MsProfessor professor = professorService.selectMsProfessorByProfessorId(professorId);
        if (professor == null)
        {
            throw new ServiceException("教师档案不存在");
        }
        MsCvProfile profile = cvProfileMapper.selectMsCvProfileByProfessorId(professorId);
        if (profile == null)
        {
            if (!createProfileIfMissing)
            {
                throw new ServiceException("CV档案不存在");
            }
            // 自动创建档案，employer/address 使用表默认值
            profile = new MsCvProfile();
            profile.setProfessorId(professorId);
            profile.setEmploymentType("1");
            profile.setEmployer("College of Management Science, Chengdu University of Technology");
            profile.setAddress("Erxianqiao Subdistrict, Chenghua District, Chengdu, Sichuan, China");
            profile.setCreateBy(SecurityUtils.getUsername());
            profile.setCreateTime(DateUtils.getNowDate());
            cvProfileMapper.insertMsCvProfile(profile);
            profile = cvProfileMapper.selectMsCvProfileByProfessorId(professorId);
        }

        MsCvData data = new MsCvData();
        data.setProfile(profile);
        data.setProfessor(professor);
        data.setEducations(listEducations(professorId));
        data.setExperiences(listExperiences(professorId));
        data.setCertifications(listCertifications(professorId));
        data.setCourses(listCourses(professorId));
        data.setDevelopments(listDevelopments(professorId));
        data.setServices(listServices(professorId));

        // 科研成果按时间窗口过滤并按年份倒序
        int yearWindow = resolveYearWindow();
        LocalDate cutoff = LocalDate.now().minusYears(yearWindow);
        data.setPapers(filterPapers(professorId, cutoff));
        data.setBooks(filterBooks(professorId, cutoff));
        data.setPatents(filterPatents(professorId, cutoff));
        data.setAwards(filterAwards(professorId, cutoff));
        data.setProjects(filterProjects(professorId, cutoff));

        // 五类等级字典全量
        data.setPaperGrades(paperGradeMapper.selectMsPaperGradeAll());
        data.setBookGrades(bookGradeMapper.selectMsBookGradeAll());
        data.setPatentGrades(patentGradeMapper.selectMsPatentGradeAll());
        data.setAwardGrades(awardGradeMapper.selectMsAwardGradeAll());
        data.setProjectGrades(projectGradeMapper.selectMsProjectGradeAll());
        data.setYearWindow(yearWindow);
        return data;
    }

    /**
     * 解析当前登录用户关联的教师ID
     */
    @Override
    public Long resolveSelfProfessorId()
    {
        Long professorId = cvProfileMapper.selectProfessorIdByUserId(SecurityUtils.getUserId());
        if (professorId == null)
        {
            throw new ServiceException("当前账号未关联教师档案，请联系管理员");
        }
        return professorId;
    }

    /**
     * 更新CV档案基本信息（按professorId定位档案）
     */
    @Override
    public int updateProfile(MsCvProfile profile)
    {
        if (profile.getProfessorId() == null)
        {
            throw new ServiceException("缺少教师ID");
        }
        MsCvProfile exists = cvProfileMapper.selectMsCvProfileByProfessorId(profile.getProfessorId());
        if (exists == null)
        {
            // 档案缺失时先自动创建再更新
            exists = new MsCvProfile();
            exists.setProfessorId(profile.getProfessorId());
            exists.setEmployer("College of Management Science, Chengdu University of Technology");
            exists.setAddress("Erxianqiao Subdistrict, Chenghua District, Chengdu, Sichuan, China");
            exists.setCreateBy(SecurityUtils.getUsername());
            exists.setCreateTime(DateUtils.getNowDate());
            cvProfileMapper.insertMsCvProfile(exists);
            exists = cvProfileMapper.selectMsCvProfileByProfessorId(profile.getProfessorId());
        }
        profile.setCvId(exists.getCvId());
        profile.setUpdateBy(SecurityUtils.getUsername());
        profile.setUpdateTime(DateUtils.getNowDate());
        return cvProfileMapper.updateMsCvProfile(profile);
    }

    /**
     * 查询教育背景列表
     */
    @Override
    public List<MsCvEducation> listEducations(Long professorId)
    {
        MsCvEducation query = new MsCvEducation();
        query.setProfessorId(professorId);
        return cvEducationMapper.selectMsCvEducationList(query);
    }

    /**
     * 新增教育背景
     */
    @Override
    public int insertEducation(MsCvEducation education)
    {
        education.setCreateBy(SecurityUtils.getUsername());
        education.setCreateTime(DateUtils.getNowDate());
        return cvEducationMapper.insertMsCvEducation(education);
    }

    /**
     * 修改教育背景
     */
    @Override
    public int updateEducation(MsCvEducation education)
    {
        education.setUpdateBy(SecurityUtils.getUsername());
        education.setUpdateTime(DateUtils.getNowDate());
        return cvEducationMapper.updateMsCvEducation(education);
    }

    /**
     * 删除教育背景
     */
    @Override
    public int deleteEducation(Long educationId)
    {
        return cvEducationMapper.deleteMsCvEducationByEducationId(educationId);
    }

    /**
     * 查询工作经历列表
     */
    @Override
    public List<MsCvExperience> listExperiences(Long professorId)
    {
        MsCvExperience query = new MsCvExperience();
        query.setProfessorId(professorId);
        return cvExperienceMapper.selectMsCvExperienceList(query);
    }

    /**
     * 新增工作经历
     */
    @Override
    public int insertExperience(MsCvExperience experience)
    {
        experience.setCreateBy(SecurityUtils.getUsername());
        experience.setCreateTime(DateUtils.getNowDate());
        return cvExperienceMapper.insertMsCvExperience(experience);
    }

    /**
     * 修改工作经历
     */
    @Override
    public int updateExperience(MsCvExperience experience)
    {
        experience.setUpdateBy(SecurityUtils.getUsername());
        experience.setUpdateTime(DateUtils.getNowDate());
        return cvExperienceMapper.updateMsCvExperience(experience);
    }

    /**
     * 删除工作经历
     */
    @Override
    public int deleteExperience(Long experienceId)
    {
        return cvExperienceMapper.deleteMsCvExperienceByExperienceId(experienceId);
    }

    /**
     * 查询职业资格列表
     */
    @Override
    public List<MsCvCertification> listCertifications(Long professorId)
    {
        MsCvCertification query = new MsCvCertification();
        query.setProfessorId(professorId);
        return cvCertificationMapper.selectMsCvCertificationList(query);
    }

    /**
     * 新增职业资格
     */
    @Override
    public int insertCertification(MsCvCertification certification)
    {
        certification.setCreateBy(SecurityUtils.getUsername());
        certification.setCreateTime(DateUtils.getNowDate());
        return cvCertificationMapper.insertMsCvCertification(certification);
    }

    /**
     * 修改职业资格
     */
    @Override
    public int updateCertification(MsCvCertification certification)
    {
        certification.setUpdateBy(SecurityUtils.getUsername());
        certification.setUpdateTime(DateUtils.getNowDate());
        return cvCertificationMapper.updateMsCvCertification(certification);
    }

    /**
     * 删除职业资格
     */
    @Override
    public int deleteCertification(Long certId)
    {
        return cvCertificationMapper.deleteMsCvCertificationByCertId(certId);
    }

    /**
     * 查询授课课程列表
     */
    @Override
    public List<MsCvCourse> listCourses(Long professorId)
    {
        MsCvCourse query = new MsCvCourse();
        query.setProfessorId(professorId);
        return cvCourseMapper.selectMsCvCourseList(query);
    }

    /**
     * 新增授课课程
     */
    @Override
    public int insertCourse(MsCvCourse course)
    {
        course.setCreateBy(SecurityUtils.getUsername());
        course.setCreateTime(DateUtils.getNowDate());
        return cvCourseMapper.insertMsCvCourse(course);
    }

    /**
     * 修改授课课程
     */
    @Override
    public int updateCourse(MsCvCourse course)
    {
        course.setUpdateBy(SecurityUtils.getUsername());
        course.setUpdateTime(DateUtils.getNowDate());
        return cvCourseMapper.updateMsCvCourse(course);
    }

    /**
     * 删除授课课程
     */
    @Override
    public int deleteCourse(Long cvCourseId)
    {
        return cvCourseMapper.deleteMsCvCourseByCvCourseId(cvCourseId);
    }

    /**
     * 从授课记录同步CV授课课程（semester如"2025-2026-1"转为学年"2025-2026"，按课程名称+学年去重）
     */
    @Override
    @Transactional
    public int syncCoursesFromTeachingRecord(Long professorId)
    {
        // 查询该教师正常状态的授课记录
        MsTeachingRecord recordQuery = new MsTeachingRecord();
        recordQuery.setProfessorId(professorId);
        recordQuery.setStatus("0");
        List<MsTeachingRecord> records = teachingRecordService.selectMsTeachingRecordList(recordQuery);

        // 已有CV课程（课程名称+学年）去重集合
        Set<String> exists = new HashSet<>();
        MsCvCourse courseQuery = new MsCvCourse();
        courseQuery.setProfessorId(professorId);
        for (MsCvCourse course : cvCourseMapper.selectMsCvCourseList(courseQuery))
        {
            exists.add(courseKey(course.getCourseName(), course.getAcademicYear()));
        }

        String operName = SecurityUtils.getUsername();
        int count = 0;
        for (MsTeachingRecord record : records)
        {
            if (record.getCourseName() == null || record.getCourseName().trim().isEmpty())
            {
                continue;
            }
            String academicYear = toAcademicYear(record.getSemester());
            String key = courseKey(record.getCourseName().trim(), academicYear);
            if (exists.contains(key))
            {
                continue;
            }
            MsCvCourse course = new MsCvCourse();
            course.setProfessorId(professorId);
            course.setCourseName(record.getCourseName().trim());
            course.setAcademicYear(academicYear);
            course.setSource("1");
            course.setCreateBy(operName);
            course.setCreateTime(DateUtils.getNowDate());
            cvCourseMapper.insertMsCvCourse(course);
            exists.add(key);
            count++;
        }
        return count;
    }

    /**
     * 查询职业发展列表
     */
    @Override
    public List<MsCvDevelopment> listDevelopments(Long professorId)
    {
        MsCvDevelopment query = new MsCvDevelopment();
        query.setProfessorId(professorId);
        return cvDevelopmentMapper.selectMsCvDevelopmentList(query);
    }

    /**
     * 新增职业发展
     */
    @Override
    public int insertDevelopment(MsCvDevelopment development)
    {
        development.setCreateBy(SecurityUtils.getUsername());
        development.setCreateTime(DateUtils.getNowDate());
        return cvDevelopmentMapper.insertMsCvDevelopment(development);
    }

    /**
     * 修改职业发展
     */
    @Override
    public int updateDevelopment(MsCvDevelopment development)
    {
        development.setUpdateBy(SecurityUtils.getUsername());
        development.setUpdateTime(DateUtils.getNowDate());
        return cvDevelopmentMapper.updateMsCvDevelopment(development);
    }

    /**
     * 删除职业发展
     */
    @Override
    public int deleteDevelopment(Long devId)
    {
        return cvDevelopmentMapper.deleteMsCvDevelopmentByDevId(devId);
    }

    /**
     * 查询社会服务列表
     */
    @Override
    public List<MsCvService> listServices(Long professorId)
    {
        MsCvService query = new MsCvService();
        query.setProfessorId(professorId);
        return cvServiceMapper.selectMsCvServiceList(query);
    }

    /**
     * 新增社会服务
     */
    @Override
    public int insertService(MsCvService service)
    {
        service.setCreateBy(SecurityUtils.getUsername());
        service.setCreateTime(DateUtils.getNowDate());
        return cvServiceMapper.insertMsCvService(service);
    }

    /**
     * 修改社会服务
     */
    @Override
    public int updateService(MsCvService service)
    {
        service.setUpdateBy(SecurityUtils.getUsername());
        service.setUpdateTime(DateUtils.getNowDate());
        return cvServiceMapper.updateMsCvService(service);
    }

    /**
     * 删除社会服务
     */
    @Override
    public int deleteService(Long serviceId)
    {
        return cvServiceMapper.deleteMsCvServiceByServiceId(serviceId);
    }

    /**
     * 教师在CV中新增论文（写入科研库并复用既有算分逻辑，强制当前登录教师）
     */
    @Override
    public int insertMyPaper(MsPaper paper)
    {
        paper.setProfessorId(resolveSelfProfessorId());
        paper.setCreateBy(SecurityUtils.getUsername());
        return paperService.insertMsPaper(paper);
    }

    /**
     * 教师在CV中新增专著（写入科研库并复用既有算分逻辑，强制当前登录教师）
     */
    @Override
    public int insertMyBook(MsBook book)
    {
        book.setProfessorId(resolveSelfProfessorId());
        book.setCreateBy(SecurityUtils.getUsername());
        return bookService.insertMsBook(book);
    }

    /**
     * 教师在CV中新增专利（写入科研库并复用既有算分逻辑，强制当前登录教师）
     */
    @Override
    public int insertMyPatent(MsPatent patent)
    {
        patent.setProfessorId(resolveSelfProfessorId());
        patent.setCreateBy(SecurityUtils.getUsername());
        return patentService.insertMsPatent(patent);
    }

    /**
     * 教师在CV中新增获奖（写入科研库并复用既有算分逻辑，强制当前登录教师）
     */
    @Override
    public int insertMyAward(MsAward award)
    {
        award.setProfessorId(resolveSelfProfessorId());
        award.setCreateBy(SecurityUtils.getUsername());
        return awardService.insertMsAward(award);
    }

    /**
     * 教师在CV中新增科研项目（写入科研库并复用既有算分逻辑，强制当前登录教师）
     */
    @Override
    public int insertMyProject(MsResearchProject project)
    {
        project.setProfessorId(resolveSelfProfessorId());
        project.setCreateBy(SecurityUtils.getUsername());
        return researchProjectService.insertMsResearchProject(project);
    }

    /**
     * 导出教师CV Word文档（导出成功后记录真实性声明）
     */
    @Override
    public void exportCvWord(Long professorId, HttpServletResponse response)
    {
        MsCvData data = getCvData(professorId, true);
        cvWordExporter.export(response, data);
        // 导出成功后更新真实性声明标志（事务简单化，不参与导出回滚）
        MsCvProfile update = new MsCvProfile();
        update.setCvId(data.getProfile().getCvId());
        update.setDeclareFlag("1");
        update.setDeclareTime(DateUtils.getNowDate());
        update.setUpdateBy(SecurityUtils.getUsername());
        update.setUpdateTime(DateUtils.getNowDate());
        cvProfileMapper.updateMsCvProfile(update);
    }

    /**
     * 解析科研成果统计年限（取不到参数时默认6年）
     */
    private int resolveYearWindow()
    {
        try
        {
            String value = configService.selectConfigByKey(CONFIG_KEY_YEAR_WINDOW);
            if (value != null && !value.trim().isEmpty())
            {
                return Integer.parseInt(value.trim());
            }
        }
        catch (Exception e)
        {
            // 参数缺失或非法时使用默认值
        }
        return DEFAULT_YEAR_WINDOW;
    }

    /**
     * 查询并过滤论文（publish_date在时间窗口内，按年份倒序）
     */
    private List<MsPaper> filterPapers(Long professorId, LocalDate cutoff)
    {
        MsPaper query = new MsPaper();
        query.setProfessorId(professorId);
        query.setStatus("0");
        List<MsPaper> list = new ArrayList<>();
        for (MsPaper paper : paperMapper.selectMsPaperList(query))
        {
            if (paper.getPublishDate() != null && !toLocalDate(paper.getPublishDate()).isBefore(cutoff))
            {
                list.add(paper);
            }
        }
        list.sort(Comparator.comparing(p -> toLocalDate(p.getPublishDate()), Comparator.reverseOrder()));
        return list;
    }

    /**
     * 查询并过滤专著（publish_date在时间窗口内，按年份倒序）
     */
    private List<MsBook> filterBooks(Long professorId, LocalDate cutoff)
    {
        MsBook query = new MsBook();
        query.setProfessorId(professorId);
        query.setStatus("0");
        List<MsBook> list = new ArrayList<>();
        for (MsBook book : bookMapper.selectMsBookList(query))
        {
            if (book.getPublishDate() != null && !toLocalDate(book.getPublishDate()).isBefore(cutoff))
            {
                list.add(book);
            }
        }
        list.sort(Comparator.comparing(b -> toLocalDate(b.getPublishDate()), Comparator.reverseOrder()));
        return list;
    }

    /**
     * 查询并过滤专利（grant_date为空取application_date，在时间窗口内，按年份倒序）
     */
    private List<MsPatent> filterPatents(Long professorId, LocalDate cutoff)
    {
        MsPatent query = new MsPatent();
        query.setProfessorId(professorId);
        query.setStatus("0");
        List<MsPatent> list = new ArrayList<>();
        for (MsPatent patent : patentMapper.selectMsPatentList(query))
        {
            Date effective = patent.getGrantDate() != null ? patent.getGrantDate() : patent.getApplicationDate();
            if (effective != null && !toLocalDate(effective).isBefore(cutoff))
            {
                list.add(patent);
            }
        }
        list.sort((a, b) -> {
            Date da = a.getGrantDate() != null ? a.getGrantDate() : a.getApplicationDate();
            Date db = b.getGrantDate() != null ? b.getGrantDate() : b.getApplicationDate();
            return toLocalDate(db).compareTo(toLocalDate(da));
        });
        return list;
    }

    /**
     * 查询并过滤获奖（award_date在时间窗口内，按年份倒序）
     */
    private List<MsAward> filterAwards(Long professorId, LocalDate cutoff)
    {
        MsAward query = new MsAward();
        query.setProfessorId(professorId);
        query.setStatus("0");
        List<MsAward> list = new ArrayList<>();
        for (MsAward award : awardMapper.selectMsAwardList(query))
        {
            if (award.getAwardDate() != null && !toLocalDate(award.getAwardDate()).isBefore(cutoff))
            {
                list.add(award);
            }
        }
        list.sort(Comparator.comparing(a -> toLocalDate(a.getAwardDate()), Comparator.reverseOrder()));
        return list;
    }

    /**
     * 查询并过滤科研项目（start_date为空视为保留，在时间窗口内，按年份倒序）
     */
    private List<MsResearchProject> filterProjects(Long professorId, LocalDate cutoff)
    {
        MsResearchProject query = new MsResearchProject();
        query.setProfessorId(professorId);
        query.setStatus("0");
        List<MsResearchProject> list = new ArrayList<>();
        for (MsResearchProject project : researchProjectMapper.selectMsResearchProjectList(query))
        {
            if (project.getStartDate() == null || !toLocalDate(project.getStartDate()).isBefore(cutoff))
            {
                list.add(project);
            }
        }
        list.sort((a, b) -> {
            LocalDate la = a.getStartDate() != null ? toLocalDate(a.getStartDate()) : LocalDate.MAX;
            LocalDate lb = b.getStartDate() != null ? toLocalDate(b.getStartDate()) : LocalDate.MAX;
            return lb.compareTo(la);
        });
        return list;
    }

    /**
     * Date转LocalDate（兼容java.sql.Date）
     */
    private LocalDate toLocalDate(Date date)
    {
        if (date instanceof java.sql.Date)
        {
            return ((java.sql.Date) date).toLocalDate();
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * 学期转学年（"2025-2026-1"→"2025-2026"）
     */
    private String toAcademicYear(String semester)
    {
        if (semester == null || semester.trim().isEmpty())
        {
            return semester;
        }
        String[] parts = semester.trim().split("-");
        if (parts.length >= 3)
        {
            return parts[0] + "-" + parts[1];
        }
        return semester.trim();
    }

    /**
     * 课程去重键（课程名称+学年）
     */
    private String courseKey(String courseName, String academicYear)
    {
        return (courseName == null ? "" : courseName.trim()) + "|" + (academicYear == null ? "" : academicYear.trim());
    }
}
