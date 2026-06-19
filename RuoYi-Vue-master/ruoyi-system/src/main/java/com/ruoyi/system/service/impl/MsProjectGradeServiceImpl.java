package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.MsProjectGrade;
import com.ruoyi.system.mapper.MsProjectGradeMapper;
import com.ruoyi.system.service.IMsProjectGradeService;

/**
 * 科研项目等级Service业务层处理
 *
 * @author 王祎哲
 * @date 2026-06-12
 */
@Service
public class MsProjectGradeServiceImpl implements IMsProjectGradeService
{
    @Autowired
    private MsProjectGradeMapper msProjectGradeMapper;

    @Override
    public MsProjectGrade selectMsProjectGradeByGradeId(Long gradeId)
    {
        return msProjectGradeMapper.selectMsProjectGradeByGradeId(gradeId);
    }

    @Override
    public MsProjectGrade selectMsProjectGradeByGradeName(String gradeName)
    {
        return msProjectGradeMapper.selectMsProjectGradeByGradeName(gradeName);
    }

    @Override
    public List<MsProjectGrade> selectMsProjectGradeList(MsProjectGrade msProjectGrade)
    {
        return msProjectGradeMapper.selectMsProjectGradeList(msProjectGrade);
    }

    @Override
    public List<MsProjectGrade> selectMsProjectGradeAll()
    {
        return msProjectGradeMapper.selectMsProjectGradeAll();
    }

    @Override
    public int insertMsProjectGrade(MsProjectGrade msProjectGrade)
    {
        msProjectGrade.setCreateTime(DateUtils.getNowDate());
        return msProjectGradeMapper.insertMsProjectGrade(msProjectGrade);
    }

    @Override
    public int updateMsProjectGrade(MsProjectGrade msProjectGrade)
    {
        msProjectGrade.setUpdateTime(DateUtils.getNowDate());
        return msProjectGradeMapper.updateMsProjectGrade(msProjectGrade);
    }

    @Override
    public int deleteMsProjectGradeByGradeIds(Long[] gradeIds)
    {
        return msProjectGradeMapper.deleteMsProjectGradeByGradeIds(gradeIds);
    }
}
