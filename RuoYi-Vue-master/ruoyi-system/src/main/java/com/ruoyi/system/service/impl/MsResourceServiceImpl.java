package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.MsResourceMapper;
import com.ruoyi.system.domain.MsResource;
import com.ruoyi.system.service.IMsResourceService;

/**
 * 资料共享Service业务层处理
 * 
 * @author 王祎哲
 * @date 2026-03-19
 */
@Service
public class MsResourceServiceImpl implements IMsResourceService 
{
    @Autowired
    private MsResourceMapper msResourceMapper;

    /**
     * 查询资料共享
     * 
     * @param resourceId 资料共享主键
     * @return 资料共享
     */
    @Override
    public MsResource selectMsResourceByResourceId(Long resourceId)
    {
        return msResourceMapper.selectMsResourceByResourceId(resourceId);
    }

    /**
     * 查询资料共享列表
     * 
     * @param msResource 资料共享
     * @return 资料共享
     */
    @Override
    public List<MsResource> selectMsResourceList(MsResource msResource)
    {
        return msResourceMapper.selectMsResourceList(msResource);
    }

    /**
     * 新增资料共享
     * 
     * @param msResource 资料共享
     * @return 结果
     */
    @Override
    public int insertMsResource(MsResource msResource)
    {
        msResource.setCreateTime(DateUtils.getNowDate());
        return msResourceMapper.insertMsResource(msResource);
    }

    /**
     * 修改资料共享
     * 
     * @param msResource 资料共享
     * @return 结果
     */
    @Override
    public int updateMsResource(MsResource msResource)
    {
        msResource.setUpdateTime(DateUtils.getNowDate());
        return msResourceMapper.updateMsResource(msResource);
    }

    /**
     * 批量删除资料共享
     * 
     * @param resourceIds 需要删除的资料共享主键
     * @return 结果
     */
    @Override
    public int deleteMsResourceByResourceIds(Long[] resourceIds)
    {
        return msResourceMapper.deleteMsResourceByResourceIds(resourceIds);
    }

    /**
     * 删除资料共享信息
     * 
     * @param resourceId 资料共享主键
     * @return 结果
     */
    @Override
    public int deleteMsResourceByResourceId(Long resourceId)
    {
        return msResourceMapper.deleteMsResourceByResourceId(resourceId);
    }
}
