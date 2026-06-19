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
import com.ruoyi.system.domain.MsBook;
import com.ruoyi.system.service.IMsBookService;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/research/book")
public class MsBookController extends BaseController {
    @Autowired private IMsBookService svc;

    @PreAuthorize("@ss.hasPermi('research:book:list')") @GetMapping("/list") public TableDataInfo list(MsBook b) { startPage(); return getDataTable(svc.selectMsBookList(b)); }
    @PreAuthorize("@ss.hasPermi('research:book:export')") @Log(title="著作",businessType=BusinessType.EXPORT) @PostMapping("/export") public void export(HttpServletResponse r, MsBook b) { new ExcelUtil<>(MsBook.class).exportExcel(r, svc.selectMsBookList(b), "著作数据"); }
    @PreAuthorize("@ss.hasPermi('research:book:query')") @GetMapping("/{id}") public AjaxResult getInfo(@PathVariable Long id) { return success(svc.selectMsBookByBookId(id)); }
    @PreAuthorize("@ss.hasPermi('research:book:add')") @Log(title="著作",businessType=BusinessType.INSERT) @PostMapping public AjaxResult add(@RequestBody MsBook b) { return toAjax(svc.insertMsBook(b)); }
    @PreAuthorize("@ss.hasPermi('research:book:edit')") @Log(title="著作",businessType=BusinessType.UPDATE) @PutMapping public AjaxResult edit(@RequestBody MsBook b) { return toAjax(svc.updateMsBook(b)); }
    @PreAuthorize("@ss.hasPermi('research:book:remove')") @Log(title="著作",businessType=BusinessType.DELETE) @DeleteMapping("/{ids}") public AjaxResult remove(@PathVariable Long[] ids) { return toAjax(svc.deleteMsBookByBookIds(ids)); }
    @PreAuthorize("@ss.hasPermi('research:book:import')") @Log(title="著作",businessType=BusinessType.IMPORT) @PostMapping("/importData") public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception { ExcelUtil<MsBook> util = new ExcelUtil<>(MsBook.class); return success(svc.importBook(util.importExcel(file.getInputStream()), updateSupport, getUsername())); }
    @PreAuthorize("@ss.hasPermi('research:book:import')") @PostMapping("/importTemplate") public void importTemplate(HttpServletResponse response) { new ExcelUtil<>(MsBook.class).importTemplateExcel(response, "著作数据"); }
}
