package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.MsBookGrade;

public interface MsBookGradeMapper {
    MsBookGrade selectMsBookGradeByGradeId(Long gradeId);
    MsBookGrade selectMsBookGradeByGradeName(String gradeName);
    List<MsBookGrade> selectMsBookGradeAll();
}
