package com.ruoyi.system.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.MsPatent;
import com.ruoyi.system.service.IMsPatentService;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/research/patent")
public class MsPatentController extends BaseController
{
    @Autowired
    private IMsPatentService msPatentService;

    @PreAuthorize("@ss.hasPermi('research:patent:list')")
    @GetMapping("/list")
    public TableDataInfo list(MsPatent msPatent) {
        startPage();
        return getDataTable(msPatentService.selectMsPatentList(msPatent));
    }

    @PreAuthorize("@ss.hasPermi('research:patent:export')")
    @Log(title = "专利", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MsPatent msPatent) {
        List<MsPatent> list = msPatentService.selectMsPatentList(msPatent);
        new ExcelUtil<MsPatent>(MsPatent.class).exportExcel(response, list, "专利数据");
    }

    @PreAuthorize("@ss.hasPermi('research:patent:query')")
    @GetMapping("/{patentId}")
    public AjaxResult getInfo(@PathVariable Long patentId) {
        return success(msPatentService.selectMsPatentByPatentId(patentId));
    }

    @PreAuthorize("@ss.hasPermi('research:patent:add')")
    @Log(title = "专利", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MsPatent msPatent) {
        return toAjax(msPatentService.insertMsPatent(msPatent));
    }

    @PreAuthorize("@ss.hasPermi('research:patent:edit')")
    @Log(title = "专利", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MsPatent msPatent) {
        return toAjax(msPatentService.updateMsPatent(msPatent));
    }

    @PreAuthorize("@ss.hasPermi('research:patent:remove')")
    @Log(title = "专利", businessType = BusinessType.DELETE)
    @DeleteMapping("/{patentIds}")
    public AjaxResult remove(@PathVariable Long[] patentIds) {
        return toAjax(msPatentService.deleteMsPatentByPatentIds(patentIds));
    }

    @PreAuthorize("@ss.hasPermi('research:patent:import')")
    @Log(title = "专利", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<MsPatent> util = new ExcelUtil<>(MsPatent.class);
        List<MsPatent> list = util.importExcel(file.getInputStream());
        return success(msPatentService.importPatent(list, updateSupport, getUsername()));
    }

    @PreAuthorize("@ss.hasPermi('research:patent:import')")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        new ExcelUtil<>(MsPatent.class).importTemplateExcel(response, "专利数据");
    }
}
