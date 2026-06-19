package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.MsPatent;

public interface IMsPatentService
{
    public MsPatent selectMsPatentByPatentId(Long patentId);
    public List<MsPatent> selectMsPatentList(MsPatent msPatent);
    public int insertMsPatent(MsPatent msPatent);
    public int updateMsPatent(MsPatent msPatent);
    public int deleteMsPatentByPatentIds(Long[] patentIds);

    public String importPatent(List<MsPatent> list, boolean updateSupport, String operName);
}
