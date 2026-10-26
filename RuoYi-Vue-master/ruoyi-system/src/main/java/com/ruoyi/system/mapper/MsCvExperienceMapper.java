package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.MsCvExperience;

/**
 * CV工作经历Mapper接口
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
public interface MsCvExperienceMapper
{
    /**
     * 查询工作经历
     */
    public MsCvExperience selectMsCvExperienceByExperienceId(Long experienceId);

    /**
     * 查询工作经历列表
     */
    public List<MsCvExperience> selectMsCvExperienceList(MsCvExperience msCvExperience);

    /**
     * 新增工作经历
     */
    public int insertMsCvExperience(MsCvExperience msCvExperience);

    /**
     * 修改工作经历
     */
    public int updateMsCvExperience(MsCvExperience msCvExperience);

    /**
     * 删除工作经历
     */
    public int deleteMsCvExperienceByExperienceId(Long experienceId);

    /**
     * 批量删除工作经历
     */
    public int deleteMsCvExperienceByExperienceIds(Long[] experienceIds);
}
