package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.MsProjectGrade;

/**
 * 科研项目等级Service接口
 *
 * @author 王祎哲
 * @date 2026-06-12
 */
public interface IMsProjectGradeService
{
    public MsProjectGrade selectMsProjectGradeByGradeId(Long gradeId);

    public MsProjectGrade selectMsProjectGradeByGradeName(String gradeName);

    public List<MsProjectGrade> selectMsProjectGradeList(MsProjectGrade msProjectGrade);

    public List<MsProjectGrade> selectMsProjectGradeAll();

    public int insertMsProjectGrade(MsProjectGrade msProjectGrade);

    public int updateMsProjectGrade(MsProjectGrade msProjectGrade);

    public int deleteMsProjectGradeByGradeIds(Long[] gradeIds);
}
