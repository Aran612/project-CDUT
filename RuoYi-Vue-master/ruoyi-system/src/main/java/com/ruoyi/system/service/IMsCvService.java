package com.ruoyi.system.service;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
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
import com.ruoyi.system.domain.MsResearchProject;

/**
 * 教师CV简历Service接口
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
public interface IMsCvService
{
    /**
     * 获取教师CV聚合数据（档案不存在时自动创建）
     *
     * @param professorId 教师ID
     * @param createProfileIfMissing 档案缺失时是否自动创建
     * @return CV聚合数据
     */
    public MsCvData getCvData(Long professorId, boolean createProfileIfMissing);

    /**
     * 解析当前登录用户关联的教师ID
     *
     * @return 教师ID
     */
    public Long resolveSelfProfessorId();

    /**
     * 更新CV档案基本信息
     *
     * @param profile CV档案（按professorId定位）
     * @return 结果
     */
    public int updateProfile(MsCvProfile profile);

    /**
     * 查询教育背景列表
     */
    public List<MsCvEducation> listEducations(Long professorId);

    /**
     * 新增教育背景
     */
    public int insertEducation(MsCvEducation education);

    /**
     * 修改教育背景
     */
    public int updateEducation(MsCvEducation education);

    /**
     * 删除教育背景
     */
    public int deleteEducation(Long educationId);

    /**
     * 查询工作经历列表
     */
    public List<MsCvExperience> listExperiences(Long professorId);

    /**
     * 新增工作经历
     */
    public int insertExperience(MsCvExperience experience);

    /**
     * 修改工作经历
     */
    public int updateExperience(MsCvExperience experience);

    /**
     * 删除工作经历
     */
    public int deleteExperience(Long experienceId);

    /**
     * 查询职业资格列表
     */
    public List<MsCvCertification> listCertifications(Long professorId);

    /**
     * 新增职业资格
     */
    public int insertCertification(MsCvCertification certification);

    /**
     * 修改职业资格
     */
    public int updateCertification(MsCvCertification certification);

    /**
     * 删除职业资格
     */
    public int deleteCertification(Long certId);

    /**
     * 查询授课课程列表
     */
    public List<MsCvCourse> listCourses(Long professorId);

    /**
     * 新增授课课程
     */
    public int insertCourse(MsCvCourse course);

    /**
     * 修改授课课程
     */
    public int updateCourse(MsCvCourse course);

    /**
     * 删除授课课程
     */
    public int deleteCourse(Long cvCourseId);

    /**
     * 从授课记录同步CV授课课程（按课程名称+学年去重）
     *
     * @param professorId 教师ID
     * @return 新增条数
     */
    public int syncCoursesFromTeachingRecord(Long professorId);

    /**
     * 查询职业发展列表
     */
    public List<MsCvDevelopment> listDevelopments(Long professorId);

    /**
     * 新增职业发展
     */
    public int insertDevelopment(MsCvDevelopment development);

    /**
     * 修改职业发展
     */
    public int updateDevelopment(MsCvDevelopment development);

    /**
     * 删除职业发展
     */
    public int deleteDevelopment(Long devId);

    /**
     * 查询社会服务列表
     */
    public List<MsCvService> listServices(Long professorId);

    /**
     * 新增社会服务
     */
    public int insertService(MsCvService service);

    /**
     * 修改社会服务
     */
    public int updateService(MsCvService service);

    /**
     * 删除社会服务
     */
    public int deleteService(Long serviceId);

    /**
     * 教师在CV中新增论文（写入科研库并自动算分，强制当前登录教师）
     */
    public int insertMyPaper(MsPaper paper);

    /**
     * 教师在CV中新增专著（写入科研库并自动算分，强制当前登录教师）
     */
    public int insertMyBook(MsBook book);

    /**
     * 教师在CV中新增专利（写入科研库并自动算分，强制当前登录教师）
     */
    public int insertMyPatent(MsPatent patent);

    /**
     * 教师在CV中新增获奖（写入科研库并自动算分，强制当前登录教师）
     */
    public int insertMyAward(MsAward award);

    /**
     * 教师在CV中新增科研项目（写入科研库并自动算分，强制当前登录教师）
     */
    public int insertMyProject(MsResearchProject project);

    /**
     * 导出教师CV Word文档（导出成功后记录真实性声明）
     *
     * @param professorId 教师ID
     * @param response 响应对象
     */
    public void exportCvWord(Long professorId, HttpServletResponse response);
}
