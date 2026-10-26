package com.ruoyi.system.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.MsAward;
import com.ruoyi.system.domain.MsBook;
import com.ruoyi.system.domain.MsCvCertification;
import com.ruoyi.system.domain.MsCvCourse;
import com.ruoyi.system.domain.MsCvDevelopment;
import com.ruoyi.system.domain.MsCvEducation;
import com.ruoyi.system.domain.MsCvExperience;
import com.ruoyi.system.domain.MsCvProfile;
import com.ruoyi.system.domain.MsCvService;
import com.ruoyi.system.domain.MsPaper;
import com.ruoyi.system.domain.MsPatent;
import com.ruoyi.system.domain.MsResearchProject;
import com.ruoyi.system.service.IMsCvService;

/**
 * 教师CV简历Controller
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
@RestController
@RequestMapping("/cv")
public class MsCvController extends BaseController
{
    @Autowired
    private IMsCvService cvService;

    // ==================== 档案 ====================

    /**
     * 我的简历（聚合数据，档案不存在自动创建）
     */
    @PreAuthorize("@ss.hasPermi('cv:profile:view')")
    @GetMapping("/my")
    public AjaxResult my()
    {
        return success(cvService.getCvData(cvService.resolveSelfProfessorId(), true));
    }

    /**
     * 查看指定教师简历（管理端）
     */
    @PreAuthorize("@ss.hasPermi('cv:manage:list')")
    @GetMapping("/detail/{professorId}")
    public AjaxResult detail(@PathVariable("professorId") Long professorId)
    {
        return success(cvService.getCvData(professorId, true));
    }

    /**
     * 教师本人修改基本信息（professorId强制为本人）
     */
    @PreAuthorize("@ss.hasPermi('cv:profile:edit')")
    @Log(title = "CV基本信息", businessType = BusinessType.UPDATE)
    @PutMapping("/my/profile")
    public AjaxResult editMyProfile(@RequestBody MsCvProfile profile)
    {
        profile.setProfessorId(cvService.resolveSelfProfessorId());
        return toAjax(cvService.updateProfile(profile));
    }

    /**
     * 管理员代录基本信息（使用body中的professorId）
     */
    @PreAuthorize("@ss.hasPermi('cv:manage:edit')")
    @Log(title = "CV基本信息", businessType = BusinessType.UPDATE)
    @PutMapping("/manage/profile")
    public AjaxResult editManageProfile(@RequestBody MsCvProfile profile)
    {
        return toAjax(cvService.updateProfile(profile));
    }

    // ==================== 教育背景 ====================

    /**
     * 新增教育背景（教师本人）
     */
    @PreAuthorize("@ss.hasPermi('cv:profile:edit')")
    @Log(title = "CV教育背景", businessType = BusinessType.INSERT)
    @PostMapping("/my/education")
    public AjaxResult addMyEducation(@RequestBody MsCvEducation education)
    {
        education.setProfessorId(cvService.resolveSelfProfessorId());
        return toAjax(cvService.insertEducation(education));
    }

    /**
     * 修改教育背景（教师本人）
     */
    @PreAuthorize("@ss.hasPermi('cv:profile:edit')")
    @Log(title = "CV教育背景", businessType = BusinessType.UPDATE)
    @PutMapping("/my/education")
    public AjaxResult editMyEducation(@RequestBody MsCvEducation education)
    {
        education.setProfessorId(cvService.resolveSelfProfessorId());
        return toAjax(cvService.updateEducation(education));
    }

    /**
     * 删除教育背景（教师本人）
     */
    @PreAuthorize("@ss.hasPermi('cv:profile:edit')")
    @Log(title = "CV教育背景", businessType = BusinessType.DELETE)
    @DeleteMapping("/my/education/{id}")
    public AjaxResult removeMyEducation(@PathVariable("id") Long id)
    {
        return toAjax(cvService.deleteEducation(id));
    }

    /**
     * 新增教育背景（管理员代录）
     */
    @PreAuthorize("@ss.hasPermi('cv:manage:edit')")
    @Log(title = "CV教育背景", businessType = BusinessType.INSERT)
    @PostMapping("/manage/education")
    public AjaxResult addManageEducation(@RequestBody MsCvEducation education)
    {
        return toAjax(cvService.insertEducation(education));
    }

    /**
     * 修改教育背景（管理员代录）
     */
    @PreAuthorize("@ss.hasPermi('cv:manage:edit')")
    @Log(title = "CV教育背景", businessType = BusinessType.UPDATE)
    @PutMapping("/manage/education")
    public AjaxResult editManageEducation(@RequestBody MsCvEducation education)
    {
        return toAjax(cvService.updateEducation(education));
    }

    /**
     * 删除教育背景（管理员）
     */
    @PreAuthorize("@ss.hasPermi('cv:manage:edit')")
    @Log(title = "CV教育背景", businessType = BusinessType.DELETE)
    @DeleteMapping("/manage/education/{id}")
    public AjaxResult removeManageEducation(@PathVariable("id") Long id)
    {
        return toAjax(cvService.deleteEducation(id));
    }

    // ==================== 工作经历 ====================

    /**
     * 新增工作经历（教师本人）
     */
    @PreAuthorize("@ss.hasPermi('cv:profile:edit')")
    @Log(title = "CV工作经历", businessType = BusinessType.INSERT)
    @PostMapping("/my/experience")
    public AjaxResult addMyExperience(@RequestBody MsCvExperience experience)
    {
        experience.setProfessorId(cvService.resolveSelfProfessorId());
        return toAjax(cvService.insertExperience(experience));
    }

    /**
     * 修改工作经历（教师本人）
     */
    @PreAuthorize("@ss.hasPermi('cv:profile:edit')")
    @Log(title = "CV工作经历", businessType = BusinessType.UPDATE)
    @PutMapping("/my/experience")
    public AjaxResult editMyExperience(@RequestBody MsCvExperience experience)
    {
        experience.setProfessorId(cvService.resolveSelfProfessorId());
        return toAjax(cvService.updateExperience(experience));
    }

    /**
     * 删除工作经历（教师本人）
     */
    @PreAuthorize("@ss.hasPermi('cv:profile:edit')")
    @Log(title = "CV工作经历", businessType = BusinessType.DELETE)
    @DeleteMapping("/my/experience/{id}")
    public AjaxResult removeMyExperience(@PathVariable("id") Long id)
    {
        return toAjax(cvService.deleteExperience(id));
    }

    /**
     * 新增工作经历（管理员代录）
     */
    @PreAuthorize("@ss.hasPermi('cv:manage:edit')")
    @Log(title = "CV工作经历", businessType = BusinessType.INSERT)
    @PostMapping("/manage/experience")
    public AjaxResult addManageExperience(@RequestBody MsCvExperience experience)
    {
        return toAjax(cvService.insertExperience(experience));
    }

    /**
     * 修改工作经历（管理员代录）
     */
    @PreAuthorize("@ss.hasPermi('cv:manage:edit')")
    @Log(title = "CV工作经历", businessType = BusinessType.UPDATE)
    @PutMapping("/manage/experience")
    public AjaxResult editManageExperience(@RequestBody MsCvExperience experience)
    {
        return toAjax(cvService.updateExperience(experience));
    }

    /**
     * 删除工作经历（管理员）
     */
    @PreAuthorize("@ss.hasPermi('cv:manage:edit')")
    @Log(title = "CV工作经历", businessType = BusinessType.DELETE)
    @DeleteMapping("/manage/experience/{id}")
    public AjaxResult removeManageExperience(@PathVariable("id") Long id)
    {
        return toAjax(cvService.deleteExperience(id));
    }

    // ==================== 职业资格 ====================

    /**
     * 新增职业资格（教师本人）
     */
    @PreAuthorize("@ss.hasPermi('cv:profile:edit')")
    @Log(title = "CV职业资格", businessType = BusinessType.INSERT)
    @PostMapping("/my/certification")
    public AjaxResult addMyCertification(@RequestBody MsCvCertification certification)
    {
        certification.setProfessorId(cvService.resolveSelfProfessorId());
        return toAjax(cvService.insertCertification(certification));
    }

    /**
     * 修改职业资格（教师本人）
     */
    @PreAuthorize("@ss.hasPermi('cv:profile:edit')")
    @Log(title = "CV职业资格", businessType = BusinessType.UPDATE)
    @PutMapping("/my/certification")
    public AjaxResult editMyCertification(@RequestBody MsCvCertification certification)
    {
        certification.setProfessorId(cvService.resolveSelfProfessorId());
        return toAjax(cvService.updateCertification(certification));
    }

    /**
     * 删除职业资格（教师本人）
     */
    @PreAuthorize("@ss.hasPermi('cv:profile:edit')")
    @Log(title = "CV职业资格", businessType = BusinessType.DELETE)
    @DeleteMapping("/my/certification/{id}")
    public AjaxResult removeMyCertification(@PathVariable("id") Long id)
    {
        return toAjax(cvService.deleteCertification(id));
    }

    /**
     * 新增职业资格（管理员代录）
     */
    @PreAuthorize("@ss.hasPermi('cv:manage:edit')")
    @Log(title = "CV职业资格", businessType = BusinessType.INSERT)
    @PostMapping("/manage/certification")
    public AjaxResult addManageCertification(@RequestBody MsCvCertification certification)
    {
        return toAjax(cvService.insertCertification(certification));
    }

    /**
     * 修改职业资格（管理员代录）
     */
    @PreAuthorize("@ss.hasPermi('cv:manage:edit')")
    @Log(title = "CV职业资格", businessType = BusinessType.UPDATE)
    @PutMapping("/manage/certification")
    public AjaxResult editManageCertification(@RequestBody MsCvCertification certification)
    {
        return toAjax(cvService.updateCertification(certification));
    }

    /**
     * 删除职业资格（管理员）
     */
    @PreAuthorize("@ss.hasPermi('cv:manage:edit')")
    @Log(title = "CV职业资格", businessType = BusinessType.DELETE)
    @DeleteMapping("/manage/certification/{id}")
    public AjaxResult removeManageCertification(@PathVariable("id") Long id)
    {
        return toAjax(cvService.deleteCertification(id));
    }

    // ==================== 授课课程 ====================

    /**
     * 新增授课课程（教师本人）
     */
    @PreAuthorize("@ss.hasPermi('cv:profile:edit')")
    @Log(title = "CV授课课程", businessType = BusinessType.INSERT)
    @PostMapping("/my/course")
    public AjaxResult addMyCourse(@RequestBody MsCvCourse course)
    {
        course.setProfessorId(cvService.resolveSelfProfessorId());
        return toAjax(cvService.insertCourse(course));
    }

    /**
     * 修改授课课程（教师本人）
     */
    @PreAuthorize("@ss.hasPermi('cv:profile:edit')")
    @Log(title = "CV授课课程", businessType = BusinessType.UPDATE)
    @PutMapping("/my/course")
    public AjaxResult editMyCourse(@RequestBody MsCvCourse course)
    {
        course.setProfessorId(cvService.resolveSelfProfessorId());
        return toAjax(cvService.updateCourse(course));
    }

    /**
     * 删除授课课程（教师本人）
     */
    @PreAuthorize("@ss.hasPermi('cv:profile:edit')")
    @Log(title = "CV授课课程", businessType = BusinessType.DELETE)
    @DeleteMapping("/my/course/{id}")
    public AjaxResult removeMyCourse(@PathVariable("id") Long id)
    {
        return toAjax(cvService.deleteCourse(id));
    }

    /**
     * 从授课记录同步CV课程（教师本人）
     */
    @PreAuthorize("@ss.hasPermi('cv:course:sync')")
    @Log(title = "CV授课同步", businessType = BusinessType.UPDATE)
    @PostMapping("/my/course/sync")
    public AjaxResult syncMyCourses()
    {
        int count = cvService.syncCoursesFromTeachingRecord(cvService.resolveSelfProfessorId());
        return AjaxResult.success("同步完成，新增" + count + "条");
    }

    /**
     * 新增授课课程（管理员代录）
     */
    @PreAuthorize("@ss.hasPermi('cv:manage:edit')")
    @Log(title = "CV授课课程", businessType = BusinessType.INSERT)
    @PostMapping("/manage/course")
    public AjaxResult addManageCourse(@RequestBody MsCvCourse course)
    {
        return toAjax(cvService.insertCourse(course));
    }

    /**
     * 修改授课课程（管理员代录）
     */
    @PreAuthorize("@ss.hasPermi('cv:manage:edit')")
    @Log(title = "CV授课课程", businessType = BusinessType.UPDATE)
    @PutMapping("/manage/course")
    public AjaxResult editManageCourse(@RequestBody MsCvCourse course)
    {
        return toAjax(cvService.updateCourse(course));
    }

    /**
     * 删除授课课程（管理员）
     */
    @PreAuthorize("@ss.hasPermi('cv:manage:edit')")
    @Log(title = "CV授课课程", businessType = BusinessType.DELETE)
    @DeleteMapping("/manage/course/{id}")
    public AjaxResult removeManageCourse(@PathVariable("id") Long id)
    {
        return toAjax(cvService.deleteCourse(id));
    }

    // ==================== 职业发展 ====================

    /**
     * 新增职业发展（教师本人）
     */
    @PreAuthorize("@ss.hasPermi('cv:profile:edit')")
    @Log(title = "CV职业发展", businessType = BusinessType.INSERT)
    @PostMapping("/my/development")
    public AjaxResult addMyDevelopment(@RequestBody MsCvDevelopment development)
    {
        development.setProfessorId(cvService.resolveSelfProfessorId());
        return toAjax(cvService.insertDevelopment(development));
    }

    /**
     * 修改职业发展（教师本人）
     */
    @PreAuthorize("@ss.hasPermi('cv:profile:edit')")
    @Log(title = "CV职业发展", businessType = BusinessType.UPDATE)
    @PutMapping("/my/development")
    public AjaxResult editMyDevelopment(@RequestBody MsCvDevelopment development)
    {
        development.setProfessorId(cvService.resolveSelfProfessorId());
        return toAjax(cvService.updateDevelopment(development));
    }

    /**
     * 删除职业发展（教师本人）
     */
    @PreAuthorize("@ss.hasPermi('cv:profile:edit')")
    @Log(title = "CV职业发展", businessType = BusinessType.DELETE)
    @DeleteMapping("/my/development/{id}")
    public AjaxResult removeMyDevelopment(@PathVariable("id") Long id)
    {
        return toAjax(cvService.deleteDevelopment(id));
    }

    /**
     * 新增职业发展（管理员代录）
     */
    @PreAuthorize("@ss.hasPermi('cv:manage:edit')")
    @Log(title = "CV职业发展", businessType = BusinessType.INSERT)
    @PostMapping("/manage/development")
    public AjaxResult addManageDevelopment(@RequestBody MsCvDevelopment development)
    {
        return toAjax(cvService.insertDevelopment(development));
    }

    /**
     * 修改职业发展（管理员代录）
     */
    @PreAuthorize("@ss.hasPermi('cv:manage:edit')")
    @Log(title = "CV职业发展", businessType = BusinessType.UPDATE)
    @PutMapping("/manage/development")
    public AjaxResult editManageDevelopment(@RequestBody MsCvDevelopment development)
    {
        return toAjax(cvService.updateDevelopment(development));
    }

    /**
     * 删除职业发展（管理员）
     */
    @PreAuthorize("@ss.hasPermi('cv:manage:edit')")
    @Log(title = "CV职业发展", businessType = BusinessType.DELETE)
    @DeleteMapping("/manage/development/{id}")
    public AjaxResult removeManageDevelopment(@PathVariable("id") Long id)
    {
        return toAjax(cvService.deleteDevelopment(id));
    }

    // ==================== 社会服务 ====================

    /**
     * 新增社会服务（教师本人）
     */
    @PreAuthorize("@ss.hasPermi('cv:profile:edit')")
    @Log(title = "CV社会服务", businessType = BusinessType.INSERT)
    @PostMapping("/my/service")
    public AjaxResult addMyService(@RequestBody MsCvService service)
    {
        service.setProfessorId(cvService.resolveSelfProfessorId());
        return toAjax(cvService.insertService(service));
    }

    /**
     * 修改社会服务（教师本人）
     */
    @PreAuthorize("@ss.hasPermi('cv:profile:edit')")
    @Log(title = "CV社会服务", businessType = BusinessType.UPDATE)
    @PutMapping("/my/service")
    public AjaxResult editMyService(@RequestBody MsCvService service)
    {
        service.setProfessorId(cvService.resolveSelfProfessorId());
        return toAjax(cvService.updateService(service));
    }

    /**
     * 删除社会服务（教师本人）
     */
    @PreAuthorize("@ss.hasPermi('cv:profile:edit')")
    @Log(title = "CV社会服务", businessType = BusinessType.DELETE)
    @DeleteMapping("/my/service/{id}")
    public AjaxResult removeMyService(@PathVariable("id") Long id)
    {
        return toAjax(cvService.deleteService(id));
    }

    /**
     * 新增社会服务（管理员代录）
     */
    @PreAuthorize("@ss.hasPermi('cv:manage:edit')")
    @Log(title = "CV社会服务", businessType = BusinessType.INSERT)
    @PostMapping("/manage/service")
    public AjaxResult addManageService(@RequestBody MsCvService service)
    {
        return toAjax(cvService.insertService(service));
    }

    /**
     * 修改社会服务（管理员代录）
     */
    @PreAuthorize("@ss.hasPermi('cv:manage:edit')")
    @Log(title = "CV社会服务", businessType = BusinessType.UPDATE)
    @PutMapping("/manage/service")
    public AjaxResult editManageService(@RequestBody MsCvService service)
    {
        return toAjax(cvService.updateService(service));
    }

    /**
     * 删除社会服务（管理员）
     */
    @PreAuthorize("@ss.hasPermi('cv:manage:edit')")
    @Log(title = "CV社会服务", businessType = BusinessType.DELETE)
    @DeleteMapping("/manage/service/{id}")
    public AjaxResult removeManageService(@PathVariable("id") Long id)
    {
        return toAjax(cvService.deleteService(id));
    }

    // ==================== 科研成果（CV写入科研库） ====================

    /**
     * 新增论文（教师本人，写入科研库并自动算分）
     */
    @PreAuthorize("@ss.hasPermi('cv:research:add')")
    @Log(title = "CV新增论文", businessType = BusinessType.INSERT)
    @PostMapping("/my/research/paper")
    public AjaxResult addMyPaper(@RequestBody MsPaper paper)
    {
        return toAjax(cvService.insertMyPaper(paper));
    }

    /**
     * 新增专著（教师本人，写入科研库并自动算分）
     */
    @PreAuthorize("@ss.hasPermi('cv:research:add')")
    @Log(title = "CV新增专著", businessType = BusinessType.INSERT)
    @PostMapping("/my/research/book")
    public AjaxResult addMyBook(@RequestBody MsBook book)
    {
        return toAjax(cvService.insertMyBook(book));
    }

    /**
     * 新增专利（教师本人，写入科研库并自动算分）
     */
    @PreAuthorize("@ss.hasPermi('cv:research:add')")
    @Log(title = "CV新增专利", businessType = BusinessType.INSERT)
    @PostMapping("/my/research/patent")
    public AjaxResult addMyPatent(@RequestBody MsPatent patent)
    {
        return toAjax(cvService.insertMyPatent(patent));
    }

    /**
     * 新增获奖（教师本人，写入科研库并自动算分）
     */
    @PreAuthorize("@ss.hasPermi('cv:research:add')")
    @Log(title = "CV新增获奖", businessType = BusinessType.INSERT)
    @PostMapping("/my/research/award")
    public AjaxResult addMyAward(@RequestBody MsAward award)
    {
        return toAjax(cvService.insertMyAward(award));
    }

    /**
     * 新增科研项目（教师本人，写入科研库并自动算分）
     */
    @PreAuthorize("@ss.hasPermi('cv:research:add')")
    @Log(title = "CV新增科研项目", businessType = BusinessType.INSERT)
    @PostMapping("/my/research/project")
    public AjaxResult addMyProject(@RequestBody MsResearchProject project)
    {
        return toAjax(cvService.insertMyProject(project));
    }

    // ==================== 导出 ====================

    /**
     * 导出教师CV Word文档（本人或拥有cv:manage:edit权限的管理员）
     */
    @PreAuthorize("@ss.hasPermi('cv:export')")
    @Log(title = "CV导出", businessType = BusinessType.EXPORT)
    @PostMapping("/export/{professorId}")
    public void export(@PathVariable("professorId") Long professorId, HttpServletResponse response)
    {
        // 本人或管理员才允许导出（管理员账号未关联教师档案时忽略）
        Long selfId = null;
        try
        {
            selfId = cvService.resolveSelfProfessorId();
        }
        catch (ServiceException e)
        {
            selfId = null;
        }
        boolean isSelf = professorId.equals(selfId);
        boolean canManage = SecurityUtils.getLoginUser().getPermissions().contains("cv:manage:edit");
        if (!isSelf && !canManage)
        {
            throw new ServiceException("无权导出他人简历");
        }
        cvService.exportCvWord(professorId, response);
    }
}
