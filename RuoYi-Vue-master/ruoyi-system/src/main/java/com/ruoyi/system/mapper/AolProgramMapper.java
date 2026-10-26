package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.AolProgram;

/**
 * AoL学位项目Mapper接口
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
public interface AolProgramMapper
{
    /**
     * 查询学位项目
     */
    public AolProgram selectAolProgramByProgramId(Long programId);

    /**
     * 查询学位项目列表
     */
    public List<AolProgram> selectAolProgramList(AolProgram aolProgram);

    /**
     * 新增学位项目
     */
    public int insertAolProgram(AolProgram aolProgram);

    /**
     * 修改学位项目
     */
    public int updateAolProgram(AolProgram aolProgram);

    /**
     * 删除学位项目
     */
    public int deleteAolProgramByProgramId(Long programId);

    /**
     * 批量删除学位项目
     */
    public int deleteAolProgramByProgramIds(Long[] programIds);
}
