package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.MsCvDevelopment;

/**
 * CV职业发展Mapper接口
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
public interface MsCvDevelopmentMapper
{
    /**
     * 查询职业发展
     */
    public MsCvDevelopment selectMsCvDevelopmentByDevId(Long devId);

    /**
     * 查询职业发展列表
     */
    public List<MsCvDevelopment> selectMsCvDevelopmentList(MsCvDevelopment msCvDevelopment);

    /**
     * 新增职业发展
     */
    public int insertMsCvDevelopment(MsCvDevelopment msCvDevelopment);

    /**
     * 修改职业发展
     */
    public int updateMsCvDevelopment(MsCvDevelopment msCvDevelopment);

    /**
     * 删除职业发展
     */
    public int deleteMsCvDevelopmentByDevId(Long devId);

    /**
     * 批量删除职业发展
     */
    public int deleteMsCvDevelopmentByDevIds(Long[] devIds);
}
