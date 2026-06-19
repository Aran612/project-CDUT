package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.MsResourceCategory;

/**
 * 资料分类Mapper接口
 * 
 * @author 王祎哲
 * @date 2026-03-19
 */
public interface MsResourceCategoryMapper 
{
    /**
     * 查询资料分类
     * 
     * @param categoryId 资料分类主键
     * @return 资料分类
     */
    public MsResourceCategory selectMsResourceCategoryByCategoryId(Long categoryId);

    /**
     * 查询资料分类列表
     * 
     * @param msResourceCategory 资料分类
     * @return 资料分类集合
     */
    public List<MsResourceCategory> selectMsResourceCategoryList(MsResourceCategory msResourceCategory);

    /**
     * 新增资料分类
     * 
     * @param msResourceCategory 资料分类
     * @return 结果
     */
    public int insertMsResourceCategory(MsResourceCategory msResourceCategory);

    /**
     * 修改资料分类
     * 
     * @param msResourceCategory 资料分类
     * @return 结果
     */
    public int updateMsResourceCategory(MsResourceCategory msResourceCategory);

    /**
     * 删除资料分类
     * 
     * @param categoryId 资料分类主键
     * @return 结果
     */
    public int deleteMsResourceCategoryByCategoryId(Long categoryId);

    /**
     * 批量删除资料分类
     * 
     * @param categoryIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMsResourceCategoryByCategoryIds(Long[] categoryIds);
}
