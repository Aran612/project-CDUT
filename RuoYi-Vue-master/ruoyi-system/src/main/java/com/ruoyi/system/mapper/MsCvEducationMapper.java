package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.MsCvEducation;

/**
 * CV教育背景Mapper接口
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
public interface MsCvEducationMapper
{
    /**
     * 查询教育背景
     */
    public MsCvEducation selectMsCvEducationByEducationId(Long educationId);

    /**
     * 查询教育背景列表
     */
    public List<MsCvEducation> selectMsCvEducationList(MsCvEducation msCvEducation);

    /**
     * 新增教育背景
     */
    public int insertMsCvEducation(MsCvEducation msCvEducation);

    /**
     * 修改教育背景
     */
    public int updateMsCvEducation(MsCvEducation msCvEducation);

    /**
     * 删除教育背景
     */
    public int deleteMsCvEducationByEducationId(Long educationId);

    /**
     * 批量删除教育背景
     */
    public int deleteMsCvEducationByEducationIds(Long[] educationIds);
}
