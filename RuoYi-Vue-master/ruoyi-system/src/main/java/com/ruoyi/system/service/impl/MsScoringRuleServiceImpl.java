package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.MsScoringRule;
import com.ruoyi.system.mapper.MsScoringRuleMapper;
import com.ruoyi.system.service.IMsScoringRuleService;

/**
 * 积分规则Service业务层处理
 *
 * @author 王祎哲
 * @date 2026-06-12
 */
@Service
public class MsScoringRuleServiceImpl implements IMsScoringRuleService
{
    @Autowired
    private MsScoringRuleMapper msScoringRuleMapper;

    @Override
    public MsScoringRule selectMsScoringRuleByRuleId(Long ruleId)
    {
        return msScoringRuleMapper.selectMsScoringRuleByRuleId(ruleId);
    }

    @Override
    public MsScoringRule selectMsScoringRuleByGradeIdAndPosition(Long gradeId, String positionType)
    {
        return msScoringRuleMapper.selectMsScoringRuleByGradeIdAndPosition(gradeId, positionType);
    }

    @Override
    public List<MsScoringRule> selectMsScoringRuleList(MsScoringRule msScoringRule)
    {
        return msScoringRuleMapper.selectMsScoringRuleList(msScoringRule);
    }

    @Override
    public int insertMsScoringRule(MsScoringRule msScoringRule)
    {
        msScoringRule.setCreateTime(DateUtils.getNowDate());
        return msScoringRuleMapper.insertMsScoringRule(msScoringRule);
    }

    @Override
    public int updateMsScoringRule(MsScoringRule msScoringRule)
    {
        msScoringRule.setUpdateTime(DateUtils.getNowDate());
        return msScoringRuleMapper.updateMsScoringRule(msScoringRule);
    }

    @Override
    public int deleteMsScoringRuleByRuleIds(Long[] ruleIds)
    {
        return msScoringRuleMapper.deleteMsScoringRuleByRuleIds(ruleIds);
    }
}
