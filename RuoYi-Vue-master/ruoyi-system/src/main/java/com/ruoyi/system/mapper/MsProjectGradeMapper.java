package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.MsProjectGrade;

/**
 * 科研项目等级Mapper接口
 *
 * @author 王祎哲
 * @date 2026-06-12
 */
public interface MsProjectGradeMapper
{
    public List<MsProjectGrade> selectMsProjectGradeList(MsProjectGrade msProjectGrade);

    public List<MsProjectGrade> selectMsProjectGradeAll();

    public MsProjectGrade selectMsProjectGradeByGradeId(Long gradeId);

    public MsProjectGrade selectMsProjectGradeByGradeCode(String gradeCode);

    public MsProjectGrade selectMsProjectGradeByGradeName(String gradeName);

    public int insertMsProjectGrade(MsProjectGrade msProjectGrade);

    public int updateMsProjectGrade(MsProjectGrade msProjectGrade);

    public int deleteMsProjectGradeByGradeId(Long gradeId);

    public int deleteMsProjectGradeByGradeIds(Long[] gradeIds);
}
