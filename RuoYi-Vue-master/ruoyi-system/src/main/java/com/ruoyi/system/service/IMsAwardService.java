package com.ruoyi.system.service;
import java.util.List;
import com.ruoyi.system.domain.MsAward;
public interface IMsAwardService {
    MsAward selectMsAwardByAwardId(Long id);
    List<MsAward> selectMsAwardList(MsAward a);
    int insertMsAward(MsAward a);
    int updateMsAward(MsAward a);
    int deleteMsAwardByAwardIds(Long[] ids);

    String importAward(List<MsAward> list, boolean updateSupport, String operName);
}
