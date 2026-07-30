package com.ruoyi.system.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import com.ruoyi.system.service.IMsTeachingRecordService;

/**
 * 研究型学者评估Controller
 * 核心逻辑：研究型学者提供的学分 >= 全体学生总学分的 40%
 *
 * @author CDUT-ALPHA
 * @date 2026-07-26
 */
@RestController
@RequestMapping("/research/evaluation")
public class MsResearchEvaluationController extends BaseController
{
    private static final BigDecimal THRESHOLD = new BigDecimal("0.40"); // 40% 阈值

    @Autowired
    private IMsTeachingRecordService msTeachingRecordService;

    /**
     * 获取研究型学者评估结果
     * 每位教授的研究型课程积分占比 = 研究型课程积分 / 全体学生总学分
     * >= 40% 即为合格的研究型学者
     */
    @PreAuthorize("@ss.hasPermi('research:evaluation:view')")
    @GetMapping("/result")
    public AjaxResult evaluationResult(@RequestParam(required = false) String semester)
    {
        Map<String, Object> result = new HashMap<>();

        // 获取全体学生总学分
        Map<String, Object> totalCreditsMap = msTeachingRecordService.selectTotalStudentCredits(semester);
        BigDecimal totalStudentCredits = new BigDecimal(totalCreditsMap.get("total_credits").toString());
        result.put("totalStudentCredits", totalStudentCredits);
        result.put("threshold", "40%");

        // 获取各教授的研究型课程统计
        List<Map<String, Object>> professorResearchStats = msTeachingRecordService.selectProfessorResearchStats(semester);

        // 计算每位教授的研究型占比和是否达标
        for (Map<String, Object> stat : professorResearchStats)
        {
            BigDecimal researchScore = new BigDecimal(stat.get("research_score").toString());
            BigDecimal professorTotalScore = new BigDecimal(stat.get("total_score").toString());

            // 计算研究型积分占全体学生总学分的比例
            BigDecimal ratio = BigDecimal.ZERO;
            if (totalStudentCredits.compareTo(BigDecimal.ZERO) > 0)
            {
                ratio = researchScore.divide(totalStudentCredits, 4, RoundingMode.HALF_UP);
            }
            stat.put("researchRatio", ratio);
            stat.put("researchRatioPercent", ratio.multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP));

            // 是否为研究型学者
            boolean isResearch = ratio.compareTo(THRESHOLD) >= 0;
            stat.put("isResearch", isResearch);
            stat.put("evaluationStatus", isResearch ? "合格" : "未达标");
        }

        result.put("professorEvaluations", professorResearchStats);

        // 统计合格人数
        long qualifiedCount = professorResearchStats.stream()
                .filter(s -> "合格".equals(s.get("evaluationStatus")))
                .count();
        result.put("qualifiedCount", qualifiedCount);
        result.put("totalCount", professorResearchStats.size());

        return success(result);
    }

    /**
     * 获取全体学生总学分
     */
    @PreAuthorize("@ss.hasPermi('research:evaluation:view')")
    @GetMapping("/totalCredits")
    public AjaxResult totalCredits(@RequestParam(required = false) String semester)
    {
        Map<String, Object> totalCredits = msTeachingRecordService.selectTotalStudentCredits(semester);
        return success(totalCredits);
    }

    /**
     * 单项评估 - 计算某位教授是否为研究型学者
     */
    @PreAuthorize("@ss.hasPermi('research:evaluation:view')")
    @GetMapping("/check")
    public AjaxResult checkProfessor(@RequestParam Long professorId,
                                     @RequestParam(required = false) String semester)
    {
        Map<String, Object> result = new HashMap<>();

        // 获取全体学生总学分
        Map<String, Object> totalCreditsMap = msTeachingRecordService.selectTotalStudentCredits(semester);
        BigDecimal totalStudentCredits = new BigDecimal(totalCreditsMap.get("total_credits").toString());

        // 获取该教授的研究型积分
        List<Map<String, Object>> allResearchStats = msTeachingRecordService.selectProfessorResearchStats(semester);
        Map<String, Object> professorStat = allResearchStats.stream()
                .filter(s -> professorId.longValue() == ((Number) s.get("professor_id")).longValue())
                .findFirst()
                .orElse(null);

        if (professorStat == null)
        {
            result.put("message", "未找到该教授的授课记录");
            return success(result);
        }

        BigDecimal researchScore = new BigDecimal(professorStat.get("research_score").toString());
        BigDecimal ratio = BigDecimal.ZERO;
        if (totalStudentCredits.compareTo(BigDecimal.ZERO) > 0)
        {
            ratio = researchScore.divide(totalStudentCredits, 4, RoundingMode.HALF_UP);
        }

        boolean isResearch = ratio.compareTo(THRESHOLD) >= 0;

        result.put("professorId", professorStat.get("professor_id"));
        result.put("professorName", professorStat.get("professor_name"));
        result.put("researchScore", researchScore);
        result.put("totalStudentCredits", totalStudentCredits);
        result.put("researchRatioPercent", ratio.multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP));
        result.put("isResearch", isResearch);
        result.put("evaluationStatus", isResearch ? "合格 - 研究型学者" : "未达标 - 需增加研究型课程");
        result.put("thresholdPercent", 40);

        return success(result);
    }
}
