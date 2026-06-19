package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.MsResource;

/**
 * 资料共享Service接口
 * 
 * @author 王祎哲
 * @date 2026-03-19
 */
public interface IMsResourceService 
{
    /**
     * 查询资料共享
     * 
     * @param resourceId 资料共享主键
     * @return 资料共享
     */
    public MsResource selectMsResourceByResourceId(Long resourceId);

    /**
     * 查询资料共享列表
     * 
     * @param msResource 资料共享
     * @return 资料共享集合
     */
    public List<MsResource> selectMsResourceList(MsResource msResource);

    /**
     * 新增资料共享
     * 
     * @param msResource 资料共享
     * @return 结果
     */
    public int insertMsResource(MsResource msResource);

    /**
     * 修改资料共享
     * 
     * @param msResource 资料共享
     * @return 结果
     */
    public int updateMsResource(MsResource msResource);

    /**
     * 批量删除资料共享
     * 
     * @param resourceIds 需要删除的资料共享主键集合
     * @return 结果
     */
    public int deleteMsResourceByResourceIds(Long[] resourceIds);

    /**
     * 删除资料共享信息
     * 
     * @param resourceId 资料共享主键
     * @return 结果
     */
    public int deleteMsResourceByResourceId(Long resourceId);
}
