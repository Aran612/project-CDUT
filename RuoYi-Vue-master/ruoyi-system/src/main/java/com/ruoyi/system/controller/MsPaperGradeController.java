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
import com.ruoyi.system.domain.MsPaperGrade;
import com.ruoyi.system.service.IMsPaperGradeService;

/**
 * 论文等级Controller
 *
 * @author 王祎哲
 * @date 2026-06-12
 */
@RestController
@RequestMapping("/research/grade")
public class MsPaperGradeController extends BaseController
{
    @Autowired
    private IMsPaperGradeService msPaperGradeService;

    /**
     * 查询论文等级列表
     */
    @PreAuthorize("@ss.hasPermi('research:grade:list')")
    @GetMapping("/list")
    public TableDataInfo list(MsPaperGrade msPaperGrade)
    {
        startPage();
        List<MsPaperGrade> list = msPaperGradeService.selectMsPaperGradeList(msPaperGrade);
        return getDataTable(list);
    }

    /**
     * 查询所有正常状态的论文等级（不分页，供下拉选择等使用）
     */
    @PreAuthorize("@ss.hasPermi('research:grade:query')")
    @GetMapping("/all")
    public AjaxResult all()
    {
        List<MsPaperGrade> list = msPaperGradeService.selectMsPaperGradeAll();
        return success(list);
    }

    /**
     * 导出论文等级
     */
    @PreAuthorize("@ss.hasPermi('research:grade:export')")
    @Log(title = "论文等级", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MsPaperGrade msPaperGrade)
    {
        List<MsPaperGrade> list = msPaperGradeService.selectMsPaperGradeList(msPaperGrade);
        ExcelUtil<MsPaperGrade> util = new ExcelUtil<MsPaperGrade>(MsPaperGrade.class);
        util.exportExcel(response, list, "论文等级数据");
    }

    /**
     * 获取论文等级详细信息
     */
    @PreAuthorize("@ss.hasPermi('research:grade:query')")
    @GetMapping(value = "/{gradeId}")
    public AjaxResult getInfo(@PathVariable("gradeId") Long gradeId)
    {
        return success(msPaperGradeService.selectMsPaperGradeByGradeId(gradeId));
    }

    /**
     * 新增论文等级
     */
    @PreAuthorize("@ss.hasPermi('research:grade:add')")
    @Log(title = "论文等级", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MsPaperGrade msPaperGrade)
    {
        return toAjax(msPaperGradeService.insertMsPaperGrade(msPaperGrade));
    }

    /**
     * 修改论文等级
     */
    @PreAuthorize("@ss.hasPermi('research:grade:edit')")
    @Log(title = "论文等级", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MsPaperGrade msPaperGrade)
    {
        return toAjax(msPaperGradeService.updateMsPaperGrade(msPaperGrade));
    }

    /**
     * 删除论文等级
     */
    @PreAuthorize("@ss.hasPermi('research:grade:remove')")
    @Log(title = "论文等级", businessType = BusinessType.DELETE)
    @DeleteMapping("/{gradeIds}")
    public AjaxResult remove(@PathVariable Long[] gradeIds)
    {
        return toAjax(msPaperGradeService.deleteMsPaperGradeByGradeIds(gradeIds));
    }
}
