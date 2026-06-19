package com.ruoyi.system.service.impl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Validator;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.MsAward;
import com.ruoyi.system.domain.MsAwardGrade;
import com.ruoyi.system.mapper.MsAwardMapper;
import com.ruoyi.system.mapper.MsAwardGradeMapper;
import com.ruoyi.system.mapper.MsProfessorMapper;
import com.ruoyi.system.service.IMsAwardService;

@Service
public class MsAwardServiceImpl implements IMsAwardService {
    @Autowired private MsAwardMapper mapper;
    @Autowired private MsProfessorMapper profMapper;
    @Autowired private MsAwardGradeMapper gradeMapper;
    @Autowired protected Validator validator;

    @Override public MsAward selectMsAwardByAwardId(Long id) { return mapper.selectMsAwardByAwardId(id); }
    @Override public List<MsAward> selectMsAwardList(MsAward a) { return mapper.selectMsAwardList(a); }

    @Override @Transactional
    public int insertMsAward(MsAward a) {
        a.setCreateTime(DateUtils.getNowDate());
        int rows = mapper.insertMsAward(a);
        if (a.getProfessorId() != null) profMapper.updateMsProfessorTotalScore(a.getProfessorId());
        return rows;
    }

    @Override @Transactional
    public int updateMsAward(MsAward a) {
        a.setUpdateTime(DateUtils.getNowDate());
        int rows = mapper.updateMsAward(a);
        if (a.getProfessorId() != null) profMapper.updateMsProfessorTotalScore(a.getProfessorId());
        return rows;
    }

    @Override @Transactional
    public String importAward(List<MsAward> list, boolean updateSupport, String operName) {
        if (list == null || list.isEmpty()) throw new ServiceException("导入获奖数据不能为空！");
        int successNum = 0, failureNum = 0;
        StringBuilder successMsg = new StringBuilder(), failureMsg = new StringBuilder();
        for (MsAward a : list) {
            try {
                BeanValidators.validateWithException(validator, a);
                if (a.getGradeName() != null && !a.getGradeName().isEmpty()) {
                    MsAwardGrade g = gradeMapper.selectMsAwardGradeByGradeName(a.getGradeName().trim());
                    if (g != null) a.setGradeId(g.getGradeId());
                    else { failureNum++; failureMsg.append("<br/>").append(failureNum).append("、获奖等级 \"").append(a.getGradeName()).append("\" 不存在"); continue; }
                }
                if (a.getProfessorId() == null) { failureNum++; failureMsg.append("<br/>").append(failureNum).append("、获奖 \"").append(a.getAwardName()).append("\" 未指定教授"); continue; }
                a.setCreateBy(operName); a.setCreateTime(DateUtils.getNowDate());
                MsAward exist = null;
                if (a.getAwardId() != null) exist = mapper.selectMsAwardByAwardId(a.getAwardId());
                if (exist != null && updateSupport) { a.setUpdateBy(operName); a.setUpdateTime(DateUtils.getNowDate()); mapper.updateMsAward(a); successNum++; successMsg.append("<br/>").append(successNum).append("、获奖 \"").append(a.getAwardName()).append("\" 更新成功"); }
                else { mapper.insertMsAward(a); successNum++; successMsg.append("<br/>").append(successNum).append("、获奖 \"").append(a.getAwardName()).append("\" 导入成功"); }
                if (a.getProfessorId() != null) profMapper.updateMsProfessorTotalScore(a.getProfessorId());
            } catch (Exception e) { failureNum++; failureMsg.append("<br/>").append(failureNum).append("、获奖导入失败：").append(e.getMessage()); }
        }
        StringBuilder result = new StringBuilder();
        if (successNum > 0) result.append("成功 ").append(successNum).append(" 条").append(successMsg);
        if (failureNum > 0) result.append("<br/>失败 ").append(failureNum).append(" 条").append(failureMsg);
        return result.toString();
    }

    @Override @Transactional
    public int deleteMsAwardByAwardIds(Long[] ids) {
        for (Long id : ids) {
            MsAward a = mapper.selectMsAwardByAwardId(id);
            if (a != null && a.getProfessorId() != null) {
                mapper.deleteMsAwardByAwardId(id);
                profMapper.updateMsProfessorTotalScore(a.getProfessorId());
            } else mapper.deleteMsAwardByAwardId(id);
        }
        return ids.length;
    }
}
