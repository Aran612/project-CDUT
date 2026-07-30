package com.ruoyi.system.controller;

import java.math.BigDecimal;
import java.util.HashMap;
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
import com.ruoyi.system.service.IMsCourseService;
import com.ruoyi.system.service.IMsTeachingRecordService;

/**
 * 教学学分统计Controller
 *
 * @author CDUT-ALPHA
 * @date 2026-07-26
 */
@RestController
@RequestMapping("/research/teachingStats")
public class MsTeachingStatsController extends BaseController
{
    @Autowired
    private IMsTeachingRecordService msTeachingRecordService;

    @Autowired
    private IMsCourseService msCourseService;

    /**
     * 获取教学统计总览数据
     */
    @PreAuthorize("@ss.hasPermi('research:teachingStats:view')")
    @GetMapping("/overview")
    public AjaxResult overview(@RequestParam(required = false) String semester)
    {
        Map<String, Object> result = new HashMap<>();

        // 按学期统计
        List<Map<String, Object>> semesterStats = msTeachingRecordService.countTeachingScoreBySemester();
        result.put("semesterStats", semesterStats);

        // 按课程类型统计
        List<Map<String, Object>> courseTypeStats = msCourseService.countCourseByType();
        result.put("courseTypeStats", courseTypeStats);

        // 按教授统计授课积分
        List<Map<String, Object>> professorStats = msTeachingRecordService.selectProfessorTeachingStats(semester);
        result.put("professorStats", professorStats);

        return success(result);
    }

    /**
     * 按教授统计详情
     */
    @PreAuthorize("@ss.hasPermi('research:teachingStats:view')")
    @GetMapping("/professor")
    public AjaxResult professorStats(@RequestParam(required = false) String semester)
    {
        List<Map<String, Object>> professorStats = msTeachingRecordService.selectProfessorTeachingStats(semester);
        return success(professorStats);
    }

    /**
     * 学期统计
     */
    @PreAuthorize("@ss.hasPermi('research:teachingStats:view')")
    @GetMapping("/semester")
    public AjaxResult semesterStats()
    {
        List<Map<String, Object>> semesterStats = msTeachingRecordService.countTeachingScoreBySemester();
        return success(semesterStats);
    }
}
