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
import com.ruoyi.system.domain.MsPaper;
import com.ruoyi.system.service.IMsPaperService;

/**
 * 论文Controller
 *
 * @author 王祎哲
 * @date 2026-06-12
 */
@RestController
@RequestMapping("/research/paper")
public class MsPaperController extends BaseController
{
    @Autowired
    private IMsPaperService msPaperService;

    /**
     * 查询论文列表
     */
    @PreAuthorize("@ss.hasPermi('research:paper:list')")
    @GetMapping("/list")
    public TableDataInfo list(MsPaper msPaper)
    {
        startPage();
        List<MsPaper> list = msPaperService.selectMsPaperList(msPaper);
        return getDataTable(list);
    }

    /**
     * 导出论文数据
     */
    @PreAuthorize("@ss.hasPermi('research:paper:export')")
    @Log(title = "论文管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MsPaper msPaper)
    {
        List<MsPaper> list = msPaperService.selectMsPaperList(msPaper);
        ExcelUtil<MsPaper> util = new ExcelUtil<MsPaper>(MsPaper.class);
        util.exportExcel(response, list, "论文数据");
    }

    /**
     * 获取论文详细信息
     */
    @PreAuthorize("@ss.hasPermi('research:paper:query')")
    @GetMapping(value = "/{paperId}")
    public AjaxResult getInfo(@PathVariable("paperId") Long paperId)
    {
        return success(msPaperService.selectMsPaperByPaperId(paperId));
    }

    /**
     * 新增论文
     */
    @PreAuthorize("@ss.hasPermi('research:paper:add')")
    @Log(title = "论文管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MsPaper msPaper)
    {
        return toAjax(msPaperService.insertMsPaper(msPaper));
    }

    /**
     * 修改论文
     */
    @PreAuthorize("@ss.hasPermi('research:paper:edit')")
    @Log(title = "论文管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MsPaper msPaper)
    {
        return toAjax(msPaperService.updateMsPaper(msPaper));
    }

    /**
     * 删除论文
     */
    @PreAuthorize("@ss.hasPermi('research:paper:remove')")
    @Log(title = "论文管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{paperIds}")
    public AjaxResult remove(@PathVariable Long[] paperIds)
    {
        return toAjax(msPaperService.deleteMsPaperByPaperIds(paperIds));
    }

    /**
     * 导入论文数据
     */
    @PreAuthorize("@ss.hasPermi('research:paper:import')")
    @Log(title = "论文管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<MsPaper> util = new ExcelUtil<MsPaper>(MsPaper.class);
        List<MsPaper> paperList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = msPaperService.importPaper(paperList, updateSupport, operName);
        return success(message);
    }

    /**
     * 下载论文导入模板
     */
    @PreAuthorize("@ss.hasPermi('research:paper:import')")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<MsPaper> util = new ExcelUtil<MsPaper>(MsPaper.class);
        util.importTemplateExcel(response, "论文数据");
    }
}
