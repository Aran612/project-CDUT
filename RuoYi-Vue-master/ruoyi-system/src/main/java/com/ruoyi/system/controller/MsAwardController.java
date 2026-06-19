package com.ruoyi.system.controller;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.MsAward;
import com.ruoyi.system.service.IMsAwardService;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/research/award")
public class MsAwardController extends BaseController {
    @Autowired private IMsAwardService svc;

    @PreAuthorize("@ss.hasPermi('research:award:list')") @GetMapping("/list") public TableDataInfo list(MsAward a) { startPage(); return getDataTable(svc.selectMsAwardList(a)); }
    @PreAuthorize("@ss.hasPermi('research:award:export')") @Log(title="获奖",businessType=BusinessType.EXPORT) @PostMapping("/export") public void export(HttpServletResponse r, MsAward a) { new ExcelUtil<>(MsAward.class).exportExcel(r, svc.selectMsAwardList(a), "获奖数据"); }
    @PreAuthorize("@ss.hasPermi('research:award:query')") @GetMapping("/{id}") public AjaxResult getInfo(@PathVariable Long id) { return success(svc.selectMsAwardByAwardId(id)); }
    @PreAuthorize("@ss.hasPermi('research:award:add')") @Log(title="获奖",businessType=BusinessType.INSERT) @PostMapping public AjaxResult add(@RequestBody MsAward a) { return toAjax(svc.insertMsAward(a)); }
    @PreAuthorize("@ss.hasPermi('research:award:edit')") @Log(title="获奖",businessType=BusinessType.UPDATE) @PutMapping public AjaxResult edit(@RequestBody MsAward a) { return toAjax(svc.updateMsAward(a)); }
    @PreAuthorize("@ss.hasPermi('research:award:remove')") @Log(title="获奖",businessType=BusinessType.DELETE) @DeleteMapping("/{ids}") public AjaxResult remove(@PathVariable Long[] ids) { return toAjax(svc.deleteMsAwardByAwardIds(ids)); }
    @PreAuthorize("@ss.hasPermi('research:award:import')") @Log(title="获奖",businessType=BusinessType.IMPORT) @PostMapping("/importData") public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception { ExcelUtil<MsAward> util = new ExcelUtil<>(MsAward.class); return success(svc.importAward(util.importExcel(file.getInputStream()), updateSupport, getUsername())); }
    @PreAuthorize("@ss.hasPermi('research:award:import')") @PostMapping("/importTemplate") public void importTemplate(HttpServletResponse response) { new ExcelUtil<>(MsAward.class).importTemplateExcel(response, "获奖数据"); }
}
