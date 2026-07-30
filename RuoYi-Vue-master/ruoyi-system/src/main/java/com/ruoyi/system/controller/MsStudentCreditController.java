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
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.MsStudentCredit;
import com.ruoyi.system.service.IMsStudentCreditService;

/**
 * 学生学分记录Controller
 *
 * @author CDUT-ALPHA
 * @date 2026-07-26
 */
@RestController
@RequestMapping("/research/studentCredit")
public class MsStudentCreditController extends BaseController
{
    @Autowired
    private IMsStudentCreditService msStudentCreditService;

    /**
     * 查询学生学分记录列表
     */
    @PreAuthorize("@ss.hasPermi('research:studentCredit:list')")
    @GetMapping("/list")
    public TableDataInfo list(MsStudentCredit msStudentCredit)
    {
        startPage();
        List<MsStudentCredit> list = msStudentCreditService.selectMsStudentCreditList(msStudentCredit);
        return getDataTable(list);
    }

    /**
     * 导出学生学分记录数据
     */
    @PreAuthorize("@ss.hasPermi('research:studentCredit:export')")
    @Log(title = "学生学分", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MsStudentCredit msStudentCredit)
    {
        List<MsStudentCredit> list = msStudentCreditService.selectMsStudentCreditList(msStudentCredit);
        ExcelUtil<MsStudentCredit> util = new ExcelUtil<MsStudentCredit>(MsStudentCredit.class);
        util.exportExcel(response, list, "学生学分数据");
    }

    /**
     * 获取学生学分记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('research:studentCredit:query')")
    @GetMapping(value = "/{creditId}")
    public AjaxResult getInfo(@PathVariable("creditId") Long creditId)
    {
        return success(msStudentCreditService.selectMsStudentCreditByCreditId(creditId));
    }

    /**
     * 新增学生学分记录
     */
    @PreAuthorize("@ss.hasPermi('research:studentCredit:add')")
    @Log(title = "学生学分", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MsStudentCredit msStudentCredit)
    {
        return toAjax(msStudentCreditService.insertMsStudentCredit(msStudentCredit));
    }

    /**
     * 修改学生学分记录
     */
    @PreAuthorize("@ss.hasPermi('research:studentCredit:edit')")
    @Log(title = "学生学分", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MsStudentCredit msStudentCredit)
    {
        return toAjax(msStudentCreditService.updateMsStudentCredit(msStudentCredit));
    }

    /**
     * 删除学生学分记录
     */
    @PreAuthorize("@ss.hasPermi('research:studentCredit:remove')")
    @Log(title = "学生学分", businessType = BusinessType.DELETE)
    @DeleteMapping("/{creditIds}")
    public AjaxResult remove(@PathVariable Long[] creditIds)
    {
        return toAjax(msStudentCreditService.deleteMsStudentCreditByCreditIds(creditIds));
    }

    /**
     * 导入学生学分数据
     */
    @PreAuthorize("@ss.hasPermi('research:studentCredit:import')")
    @Log(title = "学生学分", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<MsStudentCredit> util = new ExcelUtil<MsStudentCredit>(MsStudentCredit.class);
        List<MsStudentCredit> creditList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = msStudentCreditService.importStudentCredit(creditList, updateSupport, operName);
        return success(message);
    }

    /**
     * 下载学生学分导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<MsStudentCredit> util = new ExcelUtil<MsStudentCredit>(MsStudentCredit.class);
        util.importTemplateExcel(response, "学生学分数据");
    }
}
