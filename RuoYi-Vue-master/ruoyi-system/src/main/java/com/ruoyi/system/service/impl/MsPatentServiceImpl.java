package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Validator;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.MsPatent;
import com.ruoyi.system.domain.MsPatentGrade;
import com.ruoyi.system.mapper.MsPatentMapper;
import com.ruoyi.system.mapper.MsPatentGradeMapper;
import com.ruoyi.system.mapper.MsProfessorMapper;
import com.ruoyi.system.service.IMsPatentService;

@Service
public class MsPatentServiceImpl implements IMsPatentService
{
    @Autowired
    private MsPatentMapper msPatentMapper;
    @Autowired
    private MsProfessorMapper msProfessorMapper;
    @Autowired
    private MsPatentGradeMapper msPatentGradeMapper;
    @Autowired
    protected Validator validator;

    @Override public MsPatent selectMsPatentByPatentId(Long id) { return msPatentMapper.selectMsPatentByPatentId(id); }

    @Override public List<MsPatent> selectMsPatentList(MsPatent p) { return msPatentMapper.selectMsPatentList(p); }

    @Override @Transactional
    public int insertMsPatent(MsPatent p) {
        p.setCreateTime(DateUtils.getNowDate());
        int rows = msPatentMapper.insertMsPatent(p);
        if (p.getProfessorId() != null) msProfessorMapper.updateMsProfessorTotalScore(p.getProfessorId());
        return rows;
    }

    @Override @Transactional
    public int updateMsPatent(MsPatent p) {
        p.setUpdateTime(DateUtils.getNowDate());
        int rows = msPatentMapper.updateMsPatent(p);
        if (p.getProfessorId() != null) msProfessorMapper.updateMsProfessorTotalScore(p.getProfessorId());
        return rows;
    }

    @Override @Transactional
    public String importPatent(List<MsPatent> list, boolean updateSupport, String operName) {
        if (list == null || list.isEmpty()) throw new ServiceException("导入专利数据不能为空！");
        int successNum = 0, failureNum = 0;
        StringBuilder successMsg = new StringBuilder(), failureMsg = new StringBuilder();
        for (MsPatent p : list) {
            try {
                BeanValidators.validateWithException(validator, p);
                if (p.getGradeName() != null && !p.getGradeName().isEmpty()) {
                    MsPatentGrade g = msPatentGradeMapper.selectMsPatentGradeByGradeName(p.getGradeName().trim());
                    if (g != null) p.setGradeId(g.getGradeId());
                    else { failureNum++; failureMsg.append("<br/>").append(failureNum).append("、专利等级 \"").append(p.getGradeName()).append("\" 不存在"); continue; }
                }
                if (p.getProfessorId() == null) { failureNum++; failureMsg.append("<br/>").append(failureNum).append("、专利 \"").append(p.getPatentName()).append("\" 未指定教授"); continue; }
                p.setCreateBy(operName); p.setCreateTime(DateUtils.getNowDate());
                MsPatent exist = null;
                if (p.getPatentId() != null) exist = msPatentMapper.selectMsPatentByPatentId(p.getPatentId());
                if (exist != null && updateSupport) { p.setUpdateBy(operName); p.setUpdateTime(DateUtils.getNowDate()); msPatentMapper.updateMsPatent(p); successNum++; successMsg.append("<br/>").append(successNum).append("、专利 \"").append(p.getPatentName()).append("\" 更新成功"); }
                else { msPatentMapper.insertMsPatent(p); successNum++; successMsg.append("<br/>").append(successNum).append("、专利 \"").append(p.getPatentName()).append("\" 导入成功"); }
                if (p.getProfessorId() != null) msProfessorMapper.updateMsProfessorTotalScore(p.getProfessorId());
            } catch (Exception e) { failureNum++; failureMsg.append("<br/>").append(failureNum).append("、专利导入失败：").append(e.getMessage()); }
        }
        StringBuilder result = new StringBuilder();
        if (successNum > 0) result.append("成功 ").append(successNum).append(" 条").append(successMsg);
        if (failureNum > 0) result.append("<br/>失败 ").append(failureNum).append(" 条").append(failureMsg);
        return result.toString();
    }

    @Override @Transactional
    public int deleteMsPatentByPatentIds(Long[] ids) {
        for (Long id : ids) {
            MsPatent p = msPatentMapper.selectMsPatentByPatentId(id);
            if (p != null && p.getProfessorId() != null) {
                msPatentMapper.deleteMsPatentByPatentId(id);
                msProfessorMapper.updateMsProfessorTotalScore(p.getProfessorId());
            } else msPatentMapper.deleteMsPatentByPatentId(id);
        }
        return ids.length;
    }
}
