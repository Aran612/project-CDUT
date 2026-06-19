package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.MsPatent;

/**
 * 专利Mapper接口
 */
public interface MsPatentMapper
{
    public MsPatent selectMsPatentByPatentId(Long patentId);
    public List<MsPatent> selectMsPatentList(MsPatent msPatent);
    public int insertMsPatent(MsPatent msPatent);
    public int updateMsPatent(MsPatent msPatent);
    public int deleteMsPatentByPatentId(Long patentId);
    public int deleteMsPatentByPatentIds(Long[] patentIds);
    public int deleteMsPatentByProfessorId(Long professorId);
}
