package com.ruoyi.system.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.MsResourceCategory;
import com.ruoyi.system.service.IMsResourceCategoryService;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 资料分类Controller
 * 
 * @author 王祎哲
 * @date 2026-03-19
 */
@RestController
@RequestMapping("/resource/category")
public class MsResourceCategoryController extends BaseController
{
    @Autowired
    private IMsResourceCategoryService msResourceCategoryService;

    /**
     * 查询资料分类列表
     */
    @PreAuthorize("@ss.hasPermi('resource:category:list')")
    @GetMapping("/list")
    public AjaxResult list(MsResourceCategory msResourceCategory)
    {
        List<MsResourceCategory> list = msResourceCategoryService.selectMsResourceCategoryList(msResourceCategory);
        return success(list);
    }

    /**
     * 导出资料分类列表
     */
    @PreAuthorize("@ss.hasPermi('resource:category:export')")
    @Log(title = "资料分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MsResourceCategory msResourceCategory)
    {
        List<MsResourceCategory> list = msResourceCategoryService.selectMsResourceCategoryList(msResourceCategory);
        ExcelUtil<MsResourceCategory> util = new ExcelUtil<MsResourceCategory>(MsResourceCategory.class);
        util.exportExcel(response, list, "资料分类数据");
    }

    /**
     * 获取资料分类详细信息
     */
    @PreAuthorize("@ss.hasPermi('resource:category:query')")
    @GetMapping(value = "/{categoryId}")
    public AjaxResult getInfo(@PathVariable("categoryId") Long categoryId)
    {
        return success(msResourceCategoryService.selectMsResourceCategoryByCategoryId(categoryId));
    }

    /**
     * 新增资料分类
     */
    @PreAuthorize("@ss.hasPermi('resource:category:add')")
    @Log(title = "资料分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MsResourceCategory msResourceCategory)
    {
        return toAjax(msResourceCategoryService.insertMsResourceCategory(msResourceCategory));
    }

    /**
     * 修改资料分类
     */
    @PreAuthorize("@ss.hasPermi('resource:category:edit')")
    @Log(title = "资料分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MsResourceCategory msResourceCategory)
    {
        return toAjax(msResourceCategoryService.updateMsResourceCategory(msResourceCategory));
    }

    /**
     * 删除资料分类
     */
    @PreAuthorize("@ss.hasPermi('resource:category:remove')")
    @Log(title = "资料分类", businessType = BusinessType.DELETE)
	@DeleteMapping("/{categoryIds}")
    public AjaxResult remove(@PathVariable Long[] categoryIds)
    {
        return toAjax(msResourceCategoryService.deleteMsResourceCategoryByCategoryIds(categoryIds));
    }
}
