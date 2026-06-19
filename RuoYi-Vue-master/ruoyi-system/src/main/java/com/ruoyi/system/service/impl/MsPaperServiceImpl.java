package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.util.List;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.system.domain.MsPaper;
import com.ruoyi.system.domain.MsPaperGrade;
import com.ruoyi.system.domain.MsScoringRule;
import com.ruoyi.system.mapper.MsPaperGradeMapper;
import com.ruoyi.system.mapper.MsPaperMapper;
import com.ruoyi.system.mapper.MsProfessorMapper;
import com.ruoyi.system.mapper.MsScoringRuleMapper;
import com.ruoyi.system.service.IMsPaperService;

/**
 * 论文Service业务层处理
 *
 * @author 王祎哲
 * @date 2026-06-12
 */
@Service
public class MsPaperServiceImpl implements IMsPaperService
{
    private static final Logger log = LoggerFactory.getLogger(MsPaperServiceImpl.class);

    @Autowired
    private MsPaperMapper msPaperMapper;

    @Autowired
    private MsPaperGradeMapper msPaperGradeMapper;

    @Autowired
    private MsScoringRuleMapper msScoringRuleMapper;

    @Autowired
    private MsProfessorMapper msProfessorMapper;

    @Autowired
    protected Validator validator;

    @Override
    public MsPaper selectMsPaperByPaperId(Long paperId)
    {
        return msPaperMapper.selectMsPaperByPaperId(paperId);
    }

    @Override
    public List<MsPaper> selectMsPaperList(MsPaper msPaper)
    {
        return msPaperMapper.selectMsPaperList(msPaper);
    }

    @Override
    @Transactional
    public int insertMsPaper(MsPaper msPaper)
    {
        msPaper.setCreateTime(DateUtils.getNowDate());
        // 自动计算积分
        calculateScore(msPaper);
        int rows = msPaperMapper.insertMsPaper(msPaper);
        // 更新教授累计积分
        if (msPaper.getProfessorId() != null)
        {
            msProfessorMapper.updateMsProfessorTotalScore(msPaper.getProfessorId());
        }
        return rows;
    }

    @Override
    @Transactional
    public int updateMsPaper(MsPaper msPaper)
    {
        msPaper.setUpdateTime(DateUtils.getNowDate());
        // 重新计算积分
        calculateScore(msPaper);
        int rows = msPaperMapper.updateMsPaper(msPaper);
        // 更新教授累计积分
        if (msPaper.getProfessorId() != null)
        {
            msProfessorMapper.updateMsProfessorTotalScore(msPaper.getProfessorId());
        }
        return rows;
    }

    @Override
    @Transactional
    public int deleteMsPaperByPaperIds(Long[] paperIds)
    {
        // 先查询论文获取教授ID，用于后续积分更新
        for (Long paperId : paperIds)
        {
            MsPaper paper = msPaperMapper.selectMsPaperByPaperId(paperId);
            if (paper != null && paper.getProfessorId() != null)
            {
                msPaperMapper.deleteMsPaperByPaperId(paperId);
                msProfessorMapper.updateMsProfessorTotalScore(paper.getProfessorId());
            }
            else
            {
                msPaperMapper.deleteMsPaperByPaperId(paperId);
            }
        }
        return paperIds.length;
    }

    @Override
    @Transactional
    public String importPaper(List<MsPaper> paperList, boolean updateSupport, String operName)
    {
        if (paperList == null || paperList.isEmpty())
        {
            throw new ServiceException("导入论文数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (MsPaper paper : paperList)
        {
            try
            {
                // 验证实体
                BeanValidators.validateWithException(validator, paper);

                // 根据论文等级名称查找等级
                if (paper.getGradeName() != null && !paper.getGradeName().isEmpty())
                {
                    MsPaperGrade grade = msPaperGradeMapper.selectMsPaperGradeByGradeName(paper.getGradeName().trim());
                    if (grade != null)
                    {
                        paper.setGradeId(grade.getGradeId());
                    }
                    else
                    {
                        failureNum++;
                        failureMsg.append("<br/>").append(failureNum).append("、论文 \"").append(paper.getTitle())
                                .append("\" 的论文等级 \"").append(paper.getGradeName()).append("\" 不存在");
                        continue;
                    }
                }

                // 校验教授存在（通过professorId或professorName匹配）
                if (paper.getProfessorId() == null && paper.getProfessorName() != null)
                {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、论文 \"").append(paper.getTitle())
                            .append("\" 未指定教授，请填写教授ID");
                    continue;
                }

                // 自动计算积分
                calculateScore(paper);

                // 设置创建信息
                paper.setCreateBy(operName);
                paper.setCreateTime(DateUtils.getNowDate());

                // 根据是否为更新模式处理
                MsPaper existPaper = null;
                if (paper.getPaperId() != null)
                {
                    existPaper = msPaperMapper.selectMsPaperByPaperId(paper.getPaperId());
                }

                if (existPaper != null && updateSupport)
                {
                    // 更新现有论文
                    paper.setUpdateBy(operName);
                    paper.setUpdateTime(DateUtils.getNowDate());
                    msPaperMapper.updateMsPaper(paper);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、论文 \"").append(paper.getTitle()).append("\" 更新成功");
                }
                else
                {
                    // 新增论文
                    msPaperMapper.insertMsPaper(paper);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、论文 \"").append(paper.getTitle()).append("\" 导入成功");
                }

                // 更新教授累计积分
                if (paper.getProfessorId() != null)
                {
                    msProfessorMapper.updateMsProfessorTotalScore(paper.getProfessorId());
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String title = paper.getTitle() != null ? paper.getTitle() : "未知";
                failureMsg.append("<br/>").append(failureNum).append("、论文 \"").append(title).append("\" 导入失败：");
                failureMsg.append(e.getMessage());
                log.error("导入论文失败：{}", e.getMessage());
            }
        }

        if (failureNum > 0)
        {
            failureMsg.insert(0, "导入失败共 " + failureNum + " 条，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "导入成功共 " + successNum + " 条，数据已处理。");
        }

        return successMsg.toString();
    }

    /**
     * 根据积分规则自动计算论文积分
     * 公式：actual_score = base_score × coefficient
     */
    private void calculateScore(MsPaper paper)
    {
        if (paper.getGradeId() != null && paper.getAuthorPosition() != null)
        {
            MsPaperGrade grade = msPaperGradeMapper.selectMsPaperGradeByGradeId(paper.getGradeId());
            MsScoringRule rule = msScoringRuleMapper.selectMsScoringRuleByGradeIdAndPosition(
                    paper.getGradeId(), paper.getAuthorPosition());

            if (grade != null && rule != null)
            {
                BigDecimal baseScore = new BigDecimal(grade.getBaseScore());
                BigDecimal coefficient = rule.getCoefficient();
                int calculatedScore = baseScore.multiply(coefficient).intValue();
                paper.setActualScore(calculatedScore);
            }
            else if (grade != null)
            {
                // 如果没有找到匹配的规则，默认使用基础积分（×1.0）
                paper.setActualScore(grade.getBaseScore());
            }
        }
    }
}
