package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.MsPatentGrade;

public interface MsPatentGradeMapper {
    MsPatentGrade selectMsPatentGradeByGradeId(Long gradeId);
    MsPatentGrade selectMsPatentGradeByGradeName(String gradeName);
    List<MsPatentGrade> selectMsPatentGradeAll();
}
