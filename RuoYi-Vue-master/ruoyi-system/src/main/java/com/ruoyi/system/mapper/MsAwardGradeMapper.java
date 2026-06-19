package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.MsAwardGrade;

public interface MsAwardGradeMapper {
    MsAwardGrade selectMsAwardGradeByGradeId(Long gradeId);
    MsAwardGrade selectMsAwardGradeByGradeName(String gradeName);
    List<MsAwardGrade> selectMsAwardGradeAll();
}
