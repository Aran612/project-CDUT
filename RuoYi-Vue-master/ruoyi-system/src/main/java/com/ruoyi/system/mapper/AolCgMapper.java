package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.AolCg;

/**
 * AoL能力目标Mapper接口
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
public interface AolCgMapper
{
    /**
     * 查询能力目标
     */
    public AolCg selectAolCgByCgId(Long cgId);

    /**
     * 查询能力目标列表（按项目查询，排序号排序）
     */
    public List<AolCg> selectAolCgList(AolCg aolCg);

    /**
     * 新增能力目标
     */
    public int insertAolCg(AolCg aolCg);

    /**
     * 修改能力目标
     */
    public int updateAolCg(AolCg aolCg);

    /**
     * 删除能力目标
     */
    public int deleteAolCgByCgId(Long cgId);

    /**
     * 批量删除能力目标
     */
    public int deleteAolCgByCgIds(Long[] cgIds);
}
