package com.ruoyi.system.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.MsTeachingRecord;
import com.ruoyi.system.service.IMsTeachingRecordService;

/**
 * 授课记录Controller
 *
 * @author CDUT-ALPHA
 * @date 2026-07-26
 */
@RestController
@RequestMapping("/research/record")
public class MsTeachingRecordController extends BaseController
{
    @Autowired
    private IMsTeachingRecordService msTeachingRecordService;

    /**
     * 查询授课记录列表
     */
    @PreAuthorize("@ss.hasPermi('research:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(MsTeachingRecord msTeachingRecord)
    {
        startPage();
        List<MsTeachingRecord> list = msTeachingRecordService.selectMsTeachingRecordList(msTeachingRecord);
        return getDataTable(list);
    }

    /**
     * 导出授课记录数据
     */
    @PreAuthorize("@ss.hasPermi('research:record:export')")
    @Log(title = "授课记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MsTeachingRecord msTeachingRecord)
    {
        List<MsTeachingRecord> list = msTeachingRecordService.selectMsTeachingRecordList(msTeachingRecord);
        ExcelUtil<MsTeachingRecord> util = new ExcelUtil<MsTeachingRecord>(MsTeachingRecord.class);
        util.exportExcel(response, list, "授课记录数据");
    }

    /**
     * 获取授课记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('research:record:query')")
    @GetMapping(value = "/{recordId}")
    public AjaxResult getInfo(@PathVariable("recordId") Long recordId)
    {
        return success(msTeachingRecordService.selectMsTeachingRecordByRecordId(recordId));
    }

    /**
     * 新增授课记录
     */
    @PreAuthorize("@ss.hasPermi('research:record:add')")
    @Log(title = "授课记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MsTeachingRecord msTeachingRecord)
    {
        return toAjax(msTeachingRecordService.insertMsTeachingRecord(msTeachingRecord));
    }

    /**
     * 修改授课记录
     */
    @PreAuthorize("@ss.hasPermi('research:record:edit')")
    @Log(title = "授课记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MsTeachingRecord msTeachingRecord)
    {
        return toAjax(msTeachingRecordService.updateMsTeachingRecord(msTeachingRecord));
    }

    /**
     * 删除授课记录
     */
    @PreAuthorize("@ss.hasPermi('research:record:remove')")
    @Log(title = "授课记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{recordIds}")
    public AjaxResult remove(@PathVariable Long[] recordIds)
    {
        return toAjax(msTeachingRecordService.deleteMsTeachingRecordByRecordIds(recordIds));
    }

    /**
     * 导入授课记录数据
     */
    @PreAuthorize("@ss.hasPermi('research:record:import')")
    @Log(title = "授课记录", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<MsTeachingRecord> util = new ExcelUtil<MsTeachingRecord>(MsTeachingRecord.class);
        List<MsTeachingRecord> recordList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = msTeachingRecordService.importTeachingRecord(recordList, updateSupport, operName);
        return success(message);
    }

    /**
     * 下载授课记录导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<MsTeachingRecord> util = new ExcelUtil<MsTeachingRecord>(MsTeachingRecord.class);
        util.importTemplateExcel(response, "授课记录数据");
    }
}
