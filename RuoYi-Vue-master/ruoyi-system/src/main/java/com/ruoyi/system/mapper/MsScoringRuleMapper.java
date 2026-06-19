package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.MsScoringRule;

/**
 * 积分规则Mapper接口
 *
 * @author 王祎哲
 * @date 2026-06-12
 */
public interface MsScoringRuleMapper
{
    /**
     * 查询积分规则
     */
    public MsScoringRule selectMsScoringRuleByRuleId(Long ruleId);

    /**
     * 根据等级ID和作者身份查询积分系数
     */
    public MsScoringRule selectMsScoringRuleByGradeIdAndPosition(Long gradeId, String positionType);

    /**
     * 查询积分规则列表
     */
    public List<MsScoringRule> selectMsScoringRuleList(MsScoringRule msScoringRule);

    /**
     * 新增积分规则
     */
    public int insertMsScoringRule(MsScoringRule msScoringRule);

    /**
     * 修改积分规则
     */
    public int updateMsScoringRule(MsScoringRule msScoringRule);

    /**
     * 删除积分规则
     */
    public int deleteMsScoringRuleByRuleId(Long ruleId);

    /**
     * 批量删除积分规则
     */
    public int deleteMsScoringRuleByRuleIds(Long[] ruleIds);
}
