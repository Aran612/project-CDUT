package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.AolLg;

/**
 * AoL学习目标Mapper接口
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
public interface AolLgMapper
{
    /**
     * 查询学习目标
     */
    public AolLg selectAolLgByLgId(Long lgId);

    /**
     * 查询学习目标列表（按能力目标查询，排序号排序）
     */
    public List<AolLg> selectAolLgList(AolLg aolLg);

    /**
     * 新增学习目标
     */
    public int insertAolLg(AolLg aolLg);

    /**
     * 修改学习目标
     */
    public int updateAolLg(AolLg aolLg);

    /**
     * 删除学习目标
     */
    public int deleteAolLgByLgId(Long lgId);

    /**
     * 批量删除学习目标
     */
    public int deleteAolLgByLgIds(Long[] lgIds);
}
