package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.MsCreditStats;
import com.ruoyi.system.mapper.MsCreditManagementMapper;
import com.ruoyi.system.service.IMsCreditManagementService;

/**
 * 学分管理Service实现
 *
 * @author CDUT-ALPHA
 * @date 2026-07-26
 */
@Service
public class MsCreditManagementServiceImpl implements IMsCreditManagementService
{
    @Autowired
    private MsCreditManagementMapper msCreditManagementMapper;

    /** 师资类型字典 */
    private static final Map<String, String> TEACHER_TYPE_DICT = new HashMap<>();
    static
    {
        TEACHER_TYPE_DICT.put("1", "研究型学者");
        TEACHER_TYPE_DICT.put("2", "教学型教师");
        TEACHER_TYPE_DICT.put("3", "双师型教师");
        TEACHER_TYPE_DICT.put("4", "行政兼职");
    }

    @Override
    public List<Map<String, Object>> selectCreditByTeacherType(MsCreditStats stats)
    {
        List<Map<String, Object>> result = msCreditManagementMapper.selectCreditByTeacherType(stats);
        for (Map<String, Object> row : result)
        {
            String teacherType = (String) row.get("teacher_type");
            row.put("teacher_type_name", TEACHER_TYPE_DICT.getOrDefault(teacherType, "未知"));
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> selectCreditByDepartment(MsCreditStats stats)
    {
        return msCreditManagementMapper.selectCreditByDepartment(stats);
    }

    @Override
    public List<Map<String, Object>> selectResearchRatioByDepartment(MsCreditStats stats)
    {
        List<Map<String, Object>> result = msCreditManagementMapper.selectResearchRatioByDepartment(stats);
        BigDecimal targetRatio = new BigDecimal("0.40");
        for (Map<String, Object> row : result)
        {
            Object totalObj = row.get("total_credits");
            Object researchObj = row.get("research_credits");
            if (totalObj != null && researchObj != null)
            {
                BigDecimal total = new BigDecimal(totalObj.toString());
                BigDecimal research = new BigDecimal(researchObj.toString());
                BigDecimal ratio = BigDecimal.ZERO;
                if (total.compareTo(BigDecimal.ZERO) > 0)
                {
                    ratio = research.divide(total, 4, java.math.RoundingMode.HALF_UP);
                }
                row.put("research_ratio", ratio);
                row.put("is_target_met", ratio.compareTo(targetRatio) >= 0);
            }
            else
            {
                row.put("research_ratio", BigDecimal.ZERO);
                row.put("is_target_met", false);
            }
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> selectResearchRatioByTeacherType(MsCreditStats stats)
    {
        List<Map<String, Object>> result = msCreditManagementMapper.selectResearchRatioByTeacherType(stats);
        BigDecimal targetRatio = new BigDecimal("0.40");
        for (Map<String, Object> row : result)
        {
            String teacherType = (String) row.get("teacher_type");
            row.put("teacher_type_name", TEACHER_TYPE_DICT.getOrDefault(teacherType, "未知"));

            Object totalObj = row.get("total_credits");
            Object researchObj = row.get("research_credits");
            if (totalObj != null && researchObj != null)
            {
                BigDecimal total = new BigDecimal(totalObj.toString());
                BigDecimal research = new BigDecimal(researchObj.toString());
                BigDecimal ratio = BigDecimal.ZERO;
                if (total.compareTo(BigDecimal.ZERO) > 0)
                {
                    ratio = research.divide(total, 4, java.math.RoundingMode.HALF_UP);
                }
                row.put("research_ratio", ratio);
                row.put("is_target_met", ratio.compareTo(targetRatio) >= 0);
            }
            else
            {
                row.put("research_ratio", BigDecimal.ZERO);
                row.put("is_target_met", false);
            }
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> selectStudentCreditDetail(MsCreditStats stats)
    {
        return msCreditManagementMapper.selectStudentCreditDetail(stats);
    }

    @Override
    public List<Map<String, Object>> selectCreditByProfessor(MsCreditStats stats)
    {
        List<Map<String, Object>> result = msCreditManagementMapper.selectCreditByProfessor(stats);
        for (Map<String, Object> row : result)
        {
            String teacherType = (String) row.get("teacher_type");
            row.put("teacher_type_name", TEACHER_TYPE_DICT.getOrDefault(teacherType, "未知"));
        }
        return result;
    }

    @Override
    public Map<String, Object> selectCreditOverview(MsCreditStats stats)
    {
        Map<String, Object> overview = msCreditManagementMapper.selectCreditOverview(stats);
        if (overview == null)
        {
            overview = new HashMap<>();
        }

        // 计算研究型占比
        Object totalObj = overview.get("total_credits");
        Object researchObj = overview.get("research_credits");
        if (totalObj != null && researchObj != null)
        {
            BigDecimal total = new BigDecimal(totalObj.toString());
            BigDecimal research = new BigDecimal(researchObj.toString());
            BigDecimal ratio = BigDecimal.ZERO;
            if (total.compareTo(BigDecimal.ZERO) > 0)
            {
                ratio = research.divide(total, 4, java.math.RoundingMode.HALF_UP);
            }
            overview.put("research_ratio", ratio);
            overview.put("is_target_met", ratio.compareTo(new BigDecimal("0.40")) >= 0);
        }
        else
        {
            overview.put("research_ratio", BigDecimal.ZERO);
            overview.put("is_target_met", false);
        }

        // 设置目标值
        overview.put("target_ratio", new BigDecimal("0.40"));
        return overview;
    }

    @Override
    public List<String> selectSemesterList()
    {
        return msCreditManagementMapper.selectSemesterList();
    }

    @Override
    public List<String> selectDepartmentList()
    {
        return msCreditManagementMapper.selectDepartmentList();
    }

    @Override
    public Map<String, String> getTeacherTypeDict()
    {
        return TEACHER_TYPE_DICT;
    }
}
