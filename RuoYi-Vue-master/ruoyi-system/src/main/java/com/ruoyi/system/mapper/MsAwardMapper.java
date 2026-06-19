package com.ruoyi.system.mapper;
import java.util.List;
import com.ruoyi.system.domain.MsAward;
public interface MsAwardMapper {
    MsAward selectMsAwardByAwardId(Long id);
    List<MsAward> selectMsAwardList(MsAward a);
    int insertMsAward(MsAward a);
    int updateMsAward(MsAward a);
    int deleteMsAwardByAwardId(Long id);
    int deleteMsAwardByAwardIds(Long[] ids);
    int deleteMsAwardByProfessorId(Long professorId);
}
