package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Validator;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.MsProjectGrade;
import com.ruoyi.system.domain.MsResearchProject;
import com.ruoyi.system.mapper.MsProjectGradeMapper;
import com.ruoyi.system.mapper.MsProfessorMapper;
import com.ruoyi.system.mapper.MsResearchProjectMapper;
import com.ruoyi.system.service.IMsResearchProjectService;

/**
 * 科研项目Service业务层处理
 *
 * @author 王祎哲
 * @date 2026-06-12
 */
@Service
public class MsResearchProjectServiceImpl implements IMsResearchProjectService
{
    @Autowired
    private MsResearchProjectMapper msResearchProjectMapper;

    @Autowired
    private MsProjectGradeMapper msProjectGradeMapper;

    @Autowired
    private MsProfessorMapper msProfessorMapper;

    @Autowired
    protected Validator validator;

    @Override
    public MsResearchProject selectMsResearchProjectByProjectId(Long projectId)
    {
        return msResearchProjectMapper.selectMsResearchProjectByProjectId(projectId);
    }

    @Override
    public List<MsResearchProject> selectMsResearchProjectList(MsResearchProject msResearchProject)
    {
        return msResearchProjectMapper.selectMsResearchProjectList(msResearchProject);
    }

    @Override
    @Transactional
    public int insertMsResearchProject(MsResearchProject project)
    {
        project.setCreateTime(DateUtils.getNowDate());
        calculateProjectScore(project);
        int rows = msResearchProjectMapper.insertMsResearchProject(project);
        if (project.getProfessorId() != null)
        {
            msProfessorMapper.updateMsProfessorTotalScore(project.getProfessorId());
        }
        return rows;
    }

    @Override
    @Transactional
    public int updateMsResearchProject(MsResearchProject project)
    {
        project.setUpdateTime(DateUtils.getNowDate());
        calculateProjectScore(project);
        int rows = msResearchProjectMapper.updateMsResearchProject(project);
        if (project.getProfessorId() != null)
        {
            msProfessorMapper.updateMsProfessorTotalScore(project.getProfessorId());
        }
        return rows;
    }

    @Override
    @Transactional
    public int deleteMsResearchProjectByProjectIds(Long[] projectIds)
    {
        for (Long projectId : projectIds)
        {
            MsResearchProject project = msResearchProjectMapper.selectMsResearchProjectByProjectId(projectId);
            if (project != null && project.getProfessorId() != null)
            {
                msResearchProjectMapper.deleteMsResearchProjectByProjectId(projectId);
                msProfessorMapper.updateMsProfessorTotalScore(project.getProfessorId());
            }
            else
            {
                msResearchProjectMapper.deleteMsResearchProjectByProjectId(projectId);
            }
        }
        return projectIds.length;
    }

    @Override
    @Transactional
    public String importProject(List<MsResearchProject> list, boolean updateSupport, String operName)
    {
        if (list == null || list.isEmpty()) throw new ServiceException("导入项目数据不能为空！");
        int successNum = 0, failureNum = 0;
        StringBuilder successMsg = new StringBuilder(), failureMsg = new StringBuilder();
        for (MsResearchProject p : list)
        {
            try
            {
                BeanValidators.validateWithException(validator, p);
                // 根据项目等级名称查找等级
                if (p.getGradeName() != null && !p.getGradeName().isEmpty())
                {
                    MsProjectGrade grade = msProjectGradeMapper.selectMsProjectGradeByGradeName(p.getGradeName().trim());
                    if (grade != null) p.setGradeId(grade.getGradeId());
                    else { failureNum++; failureMsg.append("<br/>").append(failureNum).append("、项目等级 \"").append(p.getGradeName()).append("\" 不存在"); continue; }
                }
                if (p.getProfessorId() == null) { failureNum++; failureMsg.append("<br/>").append(failureNum).append("、项目 \"").append(p.getProjectName()).append("\" 未指定教授"); continue; }
                calculateProjectScore(p);
                p.setCreateBy(operName); p.setCreateTime(DateUtils.getNowDate());
                MsResearchProject exist = null;
                if (p.getProjectId() != null) exist = msResearchProjectMapper.selectMsResearchProjectByProjectId(p.getProjectId());
                if (exist != null && updateSupport) { p.setUpdateBy(operName); p.setUpdateTime(DateUtils.getNowDate()); msResearchProjectMapper.updateMsResearchProject(p); successNum++; successMsg.append("<br/>").append(successNum).append("、项目 \"").append(p.getProjectName()).append("\" 更新成功"); }
                else { msResearchProjectMapper.insertMsResearchProject(p); successNum++; successMsg.append("<br/>").append(successNum).append("、项目 \"").append(p.getProjectName()).append("\" 导入成功"); }
                if (p.getProfessorId() != null) msProfessorMapper.updateMsProfessorTotalScore(p.getProfessorId());
            }
            catch (Exception e) { failureNum++; failureMsg.append("<br/>").append(failureNum).append("、项目导入失败：").append(e.getMessage()); }
        }
        StringBuilder result = new StringBuilder();
        if (successNum > 0) result.append("成功 ").append(successNum).append(" 条").append(successMsg);
        if (failureNum > 0) result.append("<br/>失败 ").append(failureNum).append(" 条").append(failureMsg);
        return result.toString();
    }

    /**
     * 自动计算项目积分
     * 纵向项目：base_score × 角色系数（主持=1.0，参与=0.5）
     * 横向项目：funding_amount × 单位分值
     */
    private void calculateProjectScore(MsResearchProject project)
    {
        if (project.getGradeId() != null)
        {
            MsProjectGrade grade = msProjectGradeMapper.selectMsProjectGradeByGradeId(project.getGradeId());
            if (grade != null)
            {
                int baseScore = grade.getBaseScore();
                String code = grade.getGradeCode();

                // 横向项目按经费计算
                if ("HORIZONTAL".equals(code) && project.getFundingAmount() != null)
                {
                    // 自然科学横向：每万元0.75分
                    BigDecimal amount = project.getFundingAmount();
                    int score = amount.multiply(new BigDecimal("0.75")).intValue();
                    project.setActualScore(score);
                    return;
                }
                if ("SOCIAL_HORIZONTAL".equals(code) && project.getFundingAmount() != null)
                {
                    // 人文社科横向：每万元1分
                    BigDecimal amount = project.getFundingAmount();
                    int score = amount.intValue();
                    project.setActualScore(score);
                    return;
                }

                // 纵向项目按角色系数
                BigDecimal coeff = BigDecimal.ONE;
                if ("参与".equals(project.getRole()))
                {
                    coeff = new BigDecimal("0.5");
                }
                int score = new BigDecimal(baseScore).multiply(coeff).intValue();
                project.setActualScore(score);
            }
        }
    }
}
