package com.ruoyi.system.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.MsProfessor;
import com.ruoyi.system.mapper.MsPaperMapper;
import com.ruoyi.system.mapper.MsProfessorMapper;
import com.ruoyi.system.mapper.MsResearchProjectMapper;
import com.ruoyi.system.service.IMsProfessorService;

@RestController
@RequestMapping("/research/stats")
public class MsResearchStatsController extends BaseController
{
    @Autowired
    private MsPaperMapper msPaperMapper;

    @Autowired
    private MsResearchProjectMapper msResearchProjectMapper;

    @Autowired
    private MsProfessorMapper msProfessorMapper;

    @Autowired
    private IMsProfessorService msProfessorService;

    /**
     * 全院科研总览（支持年份筛选）
     */
    @PreAuthorize("@ss.hasPermi('research:stats:view')")
    @GetMapping("/summary")
    public AjaxResult summary(@RequestParam(required = false) String year)
    {
        Map<String, Object> result = new HashMap<>();

        // 论文等级分布统计（支持年份筛选）
        List<Map<String, Object>> gradeStats = msPaperMapper.countPaperByGrade(year);
        result.put("gradeStats", gradeStats);

        // 论文汇总
        long totalPapers = 0, totalScore = 0, sciPapers = 0;
        for (Map<String, Object> stat : gradeStats) {
            Object co = stat.get("count"), so = stat.get("total_score"), cd = stat.get("grade_code");
            if (co != null) totalPapers += ((Number) co).longValue();
            if (so != null) totalScore += ((Number) so).longValue();
            if (cd != null && (cd.toString().startsWith("SCIE") || cd.toString().startsWith("SCI_")
                    || cd.toString().startsWith("NSC") || cd.toString().equals("NI")))
                if (co != null) sciPapers += ((Number) co).longValue();
        }
        result.put("totalPapers", totalPapers);
        result.put("totalScore", totalScore);
        result.put("sciPapers", sciPapers);

        // SCI/SCIE细分
        Map<String, Map<String, Long>> sciBreakdown = buildSciBreakdown(gradeStats);
        result.put("sciBreakdown", sciBreakdown);

        // 考核/奖励分离（支持年份筛选）
        List<Map<String, Object>> evalStats = msPaperMapper.countPaperByEvaluationType(year);
        long rewardScore = 0, rewardPapers = 0, evalScore = 0, evalPapers = 0;
        for (Map<String, Object> es : evalStats) {
            Object t = es.get("for_evaluation_only"), c = es.get("paper_count"), s = es.get("total_score");
            long cnt = c != null ? ((Number) c).longValue() : 0;
            long sc = s != null ? ((Number) s).longValue() : 0;
            if ("1".equals(String.valueOf(t))) { evalPapers = cnt; evalScore = sc; }
            else { rewardPapers = cnt; rewardScore = sc; }
        }
        result.put("rewardScore", rewardScore);
        result.put("rewardPapers", rewardPapers);
        result.put("evalScore", evalScore);
        result.put("evalPapers", evalPapers);

        // 项目统计（支持年份筛选）
        Map<String, Object> ps = msResearchProjectMapper.countProjectStats(year);
        long projectCount = 0, projectScore = 0, projectActive = 0, projectCompleted = 0;
        if (ps != null) {
            projectCount = num(ps, "project_count");
            projectScore = num(ps, "total_score");
            projectActive = num(ps, "active_count");
            projectCompleted = num(ps, "completed_count");
        }

        // 五类成果综合统计（从教授综合排名聚合）
        List<Map<String, Object>> allStats = msProfessorMapper.selectComprehensiveProfessorStats(year);
        long allPatentCount = 0, allPatentScore = 0, allBookCount = 0, allBookScore = 0,
             allAwardCount = 0, allAwardScore = 0, allProjectTotalCount = 0, allProjectTotalScore = 0,
             allPaperTotalCount = 0, allPaperTotalScore = 0;
        for (Map<String, Object> s : allStats) {
            allPaperTotalCount += num(s, "paper_count");
            allPaperTotalScore += num(s, "paper_score");
            allProjectTotalCount += num(s, "project_count");
            allProjectTotalScore += num(s, "project_score");
            allPatentCount += num(s, "patent_count");
            allPatentScore += num(s, "patent_score");
            allBookCount += num(s, "book_count");
            allBookScore += num(s, "book_score");
            allAwardCount += num(s, "award_count");
            allAwardScore += num(s, "award_score");
        }

        result.put("projectCount", projectCount > 0 ? projectCount : allProjectTotalCount);
        result.put("projectScore", projectScore > 0 ? projectScore : allProjectTotalScore);
        result.put("projectActive", projectActive);
        result.put("projectCompleted", projectCompleted);
        result.put("patentCount", allPatentCount);
        result.put("patentScore", allPatentScore);
        result.put("bookCount", allBookCount);
        result.put("bookScore", allBookScore);
        result.put("awardCount", allAwardCount);
        result.put("awardScore", allAwardScore);

        // 综合总积分 = 五类成果之和
        long totalAllScore = totalScore + allProjectTotalScore + allPatentScore + allBookScore + allAwardScore;
        result.put("totalAllScore", totalAllScore);

        // 成果类型分布（供前端饼图）
        Map<String, Long> typeDist = new LinkedHashMap<>();
        typeDist.put("论文", totalScore);
        typeDist.put("项目", allProjectTotalScore);
        typeDist.put("专利", allPatentScore);
        typeDist.put("著作", allBookScore);
        typeDist.put("获奖", allAwardScore);
        result.put("typeDistribution", typeDist);

        // 学院产出等级
        result.put("collegeLevel", evaluateLevel(rewardScore + allProjectTotalScore));

        return success(result);
    }

    /**
     * 教授全景视图——五类成果明细
     */
    @PreAuthorize("@ss.hasPermi('research:stats:view')")
    @GetMapping("/professor/{professorId}")
    public AjaxResult professorStats(@PathVariable Long professorId, @RequestParam(required = false) String year)
    {
        MsProfessor prof = msProfessorService.selectMsProfessorByProfessorId(professorId);
        if (prof == null) return error("教授不存在");

        Map<String, Object> result = new HashMap<>();
        result.put("professor", prof);

        List<Map<String, Object>> allStats = msProfessorMapper.selectComprehensiveProfessorStats(year);
        for (Map<String, Object> s : allStats) {
            Object pid = s.get("professor_id");
            if (pid != null && ((Number) pid).longValue() == professorId.longValue()) {
                result.put("paperCount", num(s, "paper_count"));
                result.put("paperScore", num(s, "paper_score"));
                result.put("projectCount", num(s, "project_count"));
                result.put("projectScore", num(s, "project_score"));
                result.put("patentCount", num(s, "patent_count"));
                result.put("patentScore", num(s, "patent_score"));
                result.put("bookCount", num(s, "book_count"));
                result.put("bookScore", num(s, "book_score"));
                result.put("awardCount", num(s, "award_count"));
                result.put("awardScore", num(s, "award_score"));
                result.put("totalScore", num(s, "total_score"));
                result.put("level", evaluateLevel(num(s, "total_score")));
                break;
            }
        }
        result.putIfAbsent("totalScore", 0L);
        result.putIfAbsent("level", evaluateLevel(0));
        return success(result);
    }

    /**
     * 教授综合排行榜（五类成果合计，支持年份筛选）
     */
    @PreAuthorize("@ss.hasPermi('research:stats:view')")
    @GetMapping("/ranking")
    public AjaxResult ranking(@RequestParam(required = false) String year)
    {
        List<Map<String, Object>> list = msProfessorMapper.selectComprehensiveProfessorStats(year);
        for (Map<String, Object> item : list) {
            item.put("level", evaluateLevel(num(item, "total_score")));
        }
        return success(list);
    }

    // ---- helpers ----

    private long num(Map<String, Object> m, String key) {
        Object v = m.get(key);
        return v != null ? ((Number) v).longValue() : 0L;
    }

    private String evaluateLevel(long score) {
        if (score >= 5000) return "S";
        else if (score >= 1000) return "A";
        else if (score >= 400) return "B";
        else if (score >= 100) return "C";
        else return "D";
    }

    private Map<String, Long> detailItem(long c, long s) {
        Map<String, Long> m = new HashMap<>(); m.put("count", c); m.put("score", s); return m;
    }

    private String classifySciCategory(String code) {
        if (code == null) return "REGULAR";
        switch (code) {
            case "NSC": case "NSC_SUB": return "NSC";
            case "SCIE_SSCI_Q1": return "SCIE_Q1";
            case "SCIE_SSCI_Q2": return "SCIE_Q2";
            case "NI": return "NI";
            case "AHCI": return "AHCI";
            case "SCIE_SSCI_Q3": return "SCIE_Q3";
            case "SCI_SSCI_OTHER": return "SCI_OTHER";
            case "EI": return "EI";
            case "CSSCI_A1": case "CSSCI_A2": case "CSSCI_B": case "CSSCI_EXT": return "CSSCI";
            default: return "REGULAR";
        }
    }

    private Map<String, Map<String, Long>> buildSciBreakdown(List<Map<String, Object>> stats) {
        Map<String, Map<String, Long>> bd = new LinkedHashMap<>();
        String[] cats = {"NSC","SCIE_Q1","SCIE_Q2","NI","AHCI","SCIE_Q3","SCI_OTHER","EI","CSSCI","REGULAR"};
        for (String c : cats) bd.put(c, detailItem(0, 0));
        for (Map<String, Object> s : stats) {
            Object cd = s.get("grade_code"); if (cd == null) continue;
            String cat = classifySciCategory(cd.toString());
            long cnt = s.get("count") != null ? ((Number) s.get("count")).longValue() : 0;
            long sc = s.get("total_score") != null ? ((Number) s.get("total_score")).longValue() : 0;
            Map<String, Long> item = bd.get(cat);
            if (item != null) { item.put("count", item.get("count") + cnt); item.put("score", item.get("score") + sc); }
        }
        return bd;
    }
}
