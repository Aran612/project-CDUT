package com.ruoyi.system.service.impl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Validator;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.MsBook;
import com.ruoyi.system.domain.MsBookGrade;
import com.ruoyi.system.mapper.MsBookMapper;
import com.ruoyi.system.mapper.MsBookGradeMapper;
import com.ruoyi.system.mapper.MsProfessorMapper;
import com.ruoyi.system.service.IMsBookService;

@Service
public class MsBookServiceImpl implements IMsBookService {
    @Autowired private MsBookMapper mapper;
    @Autowired private MsProfessorMapper profMapper;
    @Autowired private MsBookGradeMapper gradeMapper;
    @Autowired protected Validator validator;

    @Override public MsBook selectMsBookByBookId(Long id) { return mapper.selectMsBookByBookId(id); }
    @Override public List<MsBook> selectMsBookList(MsBook b) { return mapper.selectMsBookList(b); }

    @Override @Transactional
    public int insertMsBook(MsBook b) {
        b.setCreateTime(DateUtils.getNowDate());
        int rows = mapper.insertMsBook(b);
        if (b.getProfessorId() != null) profMapper.updateMsProfessorTotalScore(b.getProfessorId());
        return rows;
    }

    @Override @Transactional
    public int updateMsBook(MsBook b) {
        b.setUpdateTime(DateUtils.getNowDate());
        int rows = mapper.updateMsBook(b);
        if (b.getProfessorId() != null) profMapper.updateMsProfessorTotalScore(b.getProfessorId());
        return rows;
    }

    @Override @Transactional
    public String importBook(List<MsBook> list, boolean updateSupport, String operName) {
        if (list == null || list.isEmpty()) throw new ServiceException("导入著作数据不能为空！");
        int successNum = 0, failureNum = 0;
        StringBuilder successMsg = new StringBuilder(), failureMsg = new StringBuilder();
        for (MsBook b : list) {
            try {
                BeanValidators.validateWithException(validator, b);
                if (b.getGradeName() != null && !b.getGradeName().isEmpty()) {
                    MsBookGrade g = gradeMapper.selectMsBookGradeByGradeName(b.getGradeName().trim());
                    if (g != null) b.setGradeId(g.getGradeId());
                    else { failureNum++; failureMsg.append("<br/>").append(failureNum).append("、著作等级 \"").append(b.getGradeName()).append("\" 不存在"); continue; }
                }
                if (b.getProfessorId() == null) { failureNum++; failureMsg.append("<br/>").append(failureNum).append("、著作 \"").append(b.getBookName()).append("\" 未指定教授"); continue; }
                b.setCreateBy(operName); b.setCreateTime(DateUtils.getNowDate());
                MsBook exist = null;
                if (b.getBookId() != null) exist = mapper.selectMsBookByBookId(b.getBookId());
                if (exist != null && updateSupport) { b.setUpdateBy(operName); b.setUpdateTime(DateUtils.getNowDate()); mapper.updateMsBook(b); successNum++; successMsg.append("<br/>").append(successNum).append("、著作 \"").append(b.getBookName()).append("\" 更新成功"); }
                else { mapper.insertMsBook(b); successNum++; successMsg.append("<br/>").append(successNum).append("、著作 \"").append(b.getBookName()).append("\" 导入成功"); }
                if (b.getProfessorId() != null) profMapper.updateMsProfessorTotalScore(b.getProfessorId());
            } catch (Exception e) { failureNum++; failureMsg.append("<br/>").append(failureNum).append("、著作导入失败：").append(e.getMessage()); }
        }
        StringBuilder result = new StringBuilder();
        if (successNum > 0) result.append("成功 ").append(successNum).append(" 条").append(successMsg);
        if (failureNum > 0) result.append("<br/>失败 ").append(failureNum).append(" 条").append(failureMsg);
        return result.toString();
    }

    @Override @Transactional
    public int deleteMsBookByBookIds(Long[] ids) {
        for (Long id : ids) {
            MsBook b = mapper.selectMsBookByBookId(id);
            if (b != null && b.getProfessorId() != null) {
                mapper.deleteMsBookByBookId(id);
                profMapper.updateMsProfessorTotalScore(b.getProfessorId());
            } else mapper.deleteMsBookByBookId(id);
        }
        return ids.length;
    }
}
