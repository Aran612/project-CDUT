package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.validation.Validator;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.MsProfessor;
import com.ruoyi.system.mapper.MsProfessorMapper;
import com.ruoyi.system.service.IMsProfessorService;

/**
 * 教授信息Service业务层处理
 *
 * @author 王祎哲
 * @date 2026-06-12
 */
@Service
public class MsProfessorServiceImpl implements IMsProfessorService
{
    @Autowired
    private MsProfessorMapper msProfessorMapper;

    @Autowired
    protected Validator validator;

    @Override
    public MsProfessor selectMsProfessorByProfessorId(Long professorId)
    {
        return msProfessorMapper.selectMsProfessorByProfessorId(professorId);
    }

    @Override
    public List<MsProfessor> selectMsProfessorList(MsProfessor msProfessor)
    {
        return msProfessorMapper.selectMsProfessorList(msProfessor);
    }

    @Override
    public int insertMsProfessor(MsProfessor msProfessor)
    {
        msProfessor.setCreateTime(DateUtils.getNowDate());
        return msProfessorMapper.insertMsProfessor(msProfessor);
    }

    @Override
    public int updateMsProfessor(MsProfessor msProfessor)
    {
        msProfessor.setUpdateTime(DateUtils.getNowDate());
        return msProfessorMapper.updateMsProfessor(msProfessor);
    }

    @Override
    public int deleteMsProfessorByProfessorIds(Long[] professorIds)
    {
        return msProfessorMapper.deleteMsProfessorByProfessorIds(professorIds);
    }

    @Override
    public List<MsProfessor> selectMsProfessorAll()
    {
        return msProfessorMapper.selectMsProfessorAll();
    }

    @Override
    public MsProfessor selectMsProfessorByProfessorName(String name)
    {
        return msProfessorMapper.selectMsProfessorByProfessorName(name);
    }

    @Override
    @Transactional
    public String importProfessor(List<MsProfessor> list, boolean updateSupport, String operName)
    {
        if (list == null || list.isEmpty()) throw new ServiceException("导入教授数据不能为空！");
        int successNum = 0, failureNum = 0;
        StringBuilder successMsg = new StringBuilder(), failureMsg = new StringBuilder();
        for (MsProfessor p : list)
        {
            try
            {
                BeanValidators.validateWithException(validator, p);
                p.setCreateBy(operName); p.setCreateTime(DateUtils.getNowDate());
                MsProfessor exist = null;
                if (p.getProfessorId() != null) exist = msProfessorMapper.selectMsProfessorByProfessorId(p.getProfessorId());
                if (exist != null && updateSupport)
                {
                    p.setUpdateBy(operName); p.setUpdateTime(DateUtils.getNowDate());
                    msProfessorMapper.updateMsProfessor(p); successNum++;
                    successMsg.append("<br/>").append(successNum).append("、教授 \"").append(p.getName()).append("\" 更新成功");
                }
                else
                {
                    msProfessorMapper.insertMsProfessor(p); successNum++;
                    successMsg.append("<br/>").append(successNum).append("、教授 \"").append(p.getName()).append("\" 导入成功");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                failureMsg.append("<br/>").append(failureNum).append("、教授导入失败：").append(e.getMessage());
            }
        }
        StringBuilder result = new StringBuilder();
        if (successNum > 0) result.append("成功 ").append(successNum).append(" 条").append(successMsg);
        if (failureNum > 0) result.append("<br/>失败 ").append(failureNum).append(" 条").append(failureMsg);
        return result.toString();
    }
}
