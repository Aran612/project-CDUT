package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.MsResourceCategoryMapper;
import com.ruoyi.system.domain.MsResourceCategory;
import com.ruoyi.system.service.IMsResourceCategoryService;

/**
 * 资料分类Service业务层处理
 * 
 * @author 王祎哲
 * @date 2026-03-19
 */
@Service
public class MsResourceCategoryServiceImpl implements IMsResourceCategoryService 
{
    @Autowired
    private MsResourceCategoryMapper msResourceCategoryMapper;

    /**
     * 查询资料分类
     * 
     * @param categoryId 资料分类主键
     * @return 资料分类
     */
    @Override
    public MsResourceCategory selectMsResourceCategoryByCategoryId(Long categoryId)
    {
        return msResourceCategoryMapper.selectMsResourceCategoryByCategoryId(categoryId);
    }

    /**
     * 查询资料分类列表
     * 
     * @param msResourceCategory 资料分类
     * @return 资料分类
     */
    @Override
    public List<MsResourceCategory> selectMsResourceCategoryList(MsResourceCategory msResourceCategory)
    {
        return msResourceCategoryMapper.selectMsResourceCategoryList(msResourceCategory);
    }

    /**
     * 新增资料分类
     * 
     * @param msResourceCategory 资料分类
     * @return 结果
     */
    @Override
    public int insertMsResourceCategory(MsResourceCategory msResourceCategory)
    {
        msResourceCategory.setCreateTime(DateUtils.getNowDate());
        return msResourceCategoryMapper.insertMsResourceCategory(msResourceCategory);
    }

    /**
     * 修改资料分类
     * 
     * @param msResourceCategory 资料分类
     * @return 结果
     */
    @Override
    public int updateMsResourceCategory(MsResourceCategory msResourceCategory)
    {
        msResourceCategory.setUpdateTime(DateUtils.getNowDate());
        return msResourceCategoryMapper.updateMsResourceCategory(msResourceCategory);
    }

    /**
     * 批量删除资料分类
     * 
     * @param categoryIds 需要删除的资料分类主键
     * @return 结果
     */
    @Override
    public int deleteMsResourceCategoryByCategoryIds(Long[] categoryIds)
    {
        return msResourceCategoryMapper.deleteMsResourceCategoryByCategoryIds(categoryIds);
    }

    /**
     * 删除资料分类信息
     * 
     * @param categoryId 资料分类主键
     * @return 结果
     */
    @Override
    public int deleteMsResourceCategoryByCategoryId(Long categoryId)
    {
        return msResourceCategoryMapper.deleteMsResourceCategoryByCategoryId(categoryId);
    }
}
