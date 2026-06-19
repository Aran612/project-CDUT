package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.MsPaperGrade;
import com.ruoyi.system.mapper.MsPaperGradeMapper;
import com.ruoyi.system.service.IMsPaperGradeService;

/**
 * 论文等级Service业务层处理
 *
 * @author 王祎哲
 * @date 2026-06-12
 */
@Service
public class MsPaperGradeServiceImpl implements IMsPaperGradeService
{
    @Autowired
    private MsPaperGradeMapper msPaperGradeMapper;

    @Override
    public MsPaperGrade selectMsPaperGradeByGradeId(Long gradeId)
    {
        return msPaperGradeMapper.selectMsPaperGradeByGradeId(gradeId);
    }

    @Override
    public MsPaperGrade selectMsPaperGradeByGradeName(String gradeName)
    {
        return msPaperGradeMapper.selectMsPaperGradeByGradeName(gradeName);
    }

    @Override
    public List<MsPaperGrade> selectMsPaperGradeList(MsPaperGrade msPaperGrade)
    {
        return msPaperGradeMapper.selectMsPaperGradeList(msPaperGrade);
    }

    @Override
    public List<MsPaperGrade> selectMsPaperGradeAll()
    {
        return msPaperGradeMapper.selectMsPaperGradeAll();
    }

    @Override
    public int insertMsPaperGrade(MsPaperGrade msPaperGrade)
    {
        msPaperGrade.setCreateTime(DateUtils.getNowDate());
        return msPaperGradeMapper.insertMsPaperGrade(msPaperGrade);
    }

    @Override
    public int updateMsPaperGrade(MsPaperGrade msPaperGrade)
    {
        msPaperGrade.setUpdateTime(DateUtils.getNowDate());
        return msPaperGradeMapper.updateMsPaperGrade(msPaperGrade);
    }

    @Override
    public int deleteMsPaperGradeByGradeIds(Long[] gradeIds)
    {
        return msPaperGradeMapper.deleteMsPaperGradeByGradeIds(gradeIds);
    }
}
