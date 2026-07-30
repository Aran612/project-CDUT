package com.ruoyi.system.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.MsCreditStats;
import com.ruoyi.system.service.IMsCreditManagementService;

/**
 * 学分管理Controller
 *
 * @author CDUT-ALPHA
 * @date 2026-07-26
 */
@RestController
@RequestMapping("/credit")
public class MsCreditManagementController extends BaseController
{
    @Autowired
    private IMsCreditManagementService msCreditManagementService;

    /**
     * 学分总览统计
     */
    @PreAuthorize("@ss.hasPermi('credit:stats:view')")
    @GetMapping("/overview")
    public AjaxResult overview(@RequestParam(required = false) String semester,
                               @RequestParam(required = false) String department)
    {
        MsCreditStats stats = new MsCreditStats();
        stats.setSemester(semester);
        stats.setDepartment(department);
        return success(msCreditManagementService.selectCreditOverview(stats));
    }

    /**
     * 按师资类型统计学分
     */
    @PreAuthorize("@ss.hasPermi('credit:stats:view')")
    @GetMapping("/byTeacherType")
    public AjaxResult byTeacherType(@RequestParam(required = false) String semester,
                                    @RequestParam(required = false) String department)
    {
        MsCreditStats stats = new MsCreditStats();
        stats.setSemester(semester);
        stats.setDepartment(department);
        return success(msCreditManagementService.selectCreditByTeacherType(stats));
    }

    /**
     * 按专业汇总学分
     */
    @PreAuthorize("@ss.hasPermi('credit:major:view')")
    @GetMapping("/byDepartment")
    public AjaxResult byDepartment(@RequestParam(required = false) String semester,
                                    @RequestParam(required = false) String teacherType)
    {
        MsCreditStats stats = new MsCreditStats();
        stats.setSemester(semester);
        stats.setTeacherType(teacherType);
        return success(msCreditManagementService.selectCreditByDepartment(stats));
    }

    /**
     * 研究型占比分析（按专业）
     */
    @PreAuthorize("@ss.hasPermi('credit:research:view')")
    @GetMapping("/researchRatioByDepartment")
    public AjaxResult researchRatioByDepartment(@RequestParam(required = false) String semester)
    {
        MsCreditStats stats = new MsCreditStats();
        stats.setSemester(semester);
        return success(msCreditManagementService.selectResearchRatioByDepartment(stats));
    }

    /**
     * 研究型占比分析（按师资类型）
     */
    @PreAuthorize("@ss.hasPermi('credit:research:view')")
    @GetMapping("/researchRatioByTeacherType")
    public AjaxResult researchRatioByTeacherType(@RequestParam(required = false) String semester)
    {
        MsCreditStats stats = new MsCreditStats();
        stats.setSemester(semester);
        return success(msCreditManagementService.selectResearchRatioByTeacherType(stats));
    }

    /**
     * 学生学分明细
     */
    @PreAuthorize("@ss.hasPermi('credit:stats:view')")
    @GetMapping("/studentDetail")
    public AjaxResult studentDetail(MsCreditStats stats)
    {
        return success(msCreditManagementService.selectStudentCreditDetail(stats));
    }

    /**
     * 按教授统计教学学分
     */
    @PreAuthorize("@ss.hasPermi('credit:stats:view')")
    @GetMapping("/byProfessor")
    public AjaxResult byProfessor(@RequestParam(required = false) String semester,
                                  @RequestParam(required = false) String teacherType,
                                  @RequestParam(required = false) String department)
    {
        MsCreditStats stats = new MsCreditStats();
        stats.setSemester(semester);
        stats.setTeacherType(teacherType);
        stats.setDepartment(department);
        return success(msCreditManagementService.selectCreditByProfessor(stats));
    }

    /**
     * 获取学期列表
     */
    @PreAuthorize("@ss.hasPermi('credit:stats:view')")
    @GetMapping("/semesterList")
    public AjaxResult semesterList()
    {
        return success(msCreditManagementService.selectSemesterList());
    }

    /**
     * 获取院系列表
     */
    @PreAuthorize("@ss.hasPermi('credit:stats:view')")
    @GetMapping("/departmentList")
    public AjaxResult departmentList()
    {
        return success(msCreditManagementService.selectDepartmentList());
    }

    /**
     * 获取师资类型字典
     */
    @PreAuthorize("@ss.hasPermi('credit:stats:view')")
    @GetMapping("/teacherTypeDict")
    public AjaxResult teacherTypeDict()
    {
        return success(msCreditManagementService.getTeacherTypeDict());
    }
}
