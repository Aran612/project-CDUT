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
import com.ruoyi.system.domain.MsResource;
import com.ruoyi.system.service.IMsResourceService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 资料共享Controller
 * 
 * @author 王祎哲
 * @date 2026-03-19
 */
@RestController
@RequestMapping("/resource/resource")
public class MsResourceController extends BaseController
{
    @Autowired
    private IMsResourceService msResourceService;

    /**
     * 查询资料共享列表
     */
    @PreAuthorize("@ss.hasPermi('resource:resource:list')")
    @GetMapping("/list")
    public TableDataInfo list(MsResource msResource)
    {
        startPage();
        List<MsResource> list = msResourceService.selectMsResourceList(msResource);
        return getDataTable(list);
    }

    /**
     * 导出资料共享列表
     */
    @PreAuthorize("@ss.hasPermi('resource:resource:export')")
    @Log(title = "资料共享", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MsResource msResource)
    {
        List<MsResource> list = msResourceService.selectMsResourceList(msResource);
        ExcelUtil<MsResource> util = new ExcelUtil<MsResource>(MsResource.class);
        util.exportExcel(response, list, "资料共享数据");
    }

    /**
     * 获取资料共享详细信息
     */
    @PreAuthorize("@ss.hasPermi('resource:resource:query')")
    @GetMapping(value = "/{resourceId}")
    public AjaxResult getInfo(@PathVariable("resourceId") Long resourceId)
    {
        return success(msResourceService.selectMsResourceByResourceId(resourceId));
    }

    /**
     * 新增资料共享
     */
    @PreAuthorize("@ss.hasPermi('resource:resource:add')")
    @Log(title = "资料共享", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MsResource msResource)
    {
        return toAjax(msResourceService.insertMsResource(msResource));
    }

    /**
     * 修改资料共享
     */
    @PreAuthorize("@ss.hasPermi('resource:resource:edit')")
    @Log(title = "资料共享", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MsResource msResource)
    {
        return toAjax(msResourceService.updateMsResource(msResource));
    }

    /**
     * 删除资料共享
     */
    @PreAuthorize("@ss.hasPermi('resource:resource:remove')")
    @Log(title = "资料共享", businessType = BusinessType.DELETE)
	@DeleteMapping("/{resourceIds}")
    public AjaxResult remove(@PathVariable Long[] resourceIds)
    {
        return toAjax(msResourceService.deleteMsResourceByResourceIds(resourceIds));
    }
}
