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
import com.ruoyi.system.domain.MsProjectGrade;
import com.ruoyi.system.domain.MsResearchProject;
import com.ruoyi.system.service.IMsProjectGradeService;
import com.ruoyi.system.service.IMsResearchProjectService;
import org.springframework.web.multipart.MultipartFile;

/**
 * 科研项目Controller
 *
 * @author 王祎哲
 * @date 2026-06-12
 */
@RestController
@RequestMapping("/research/project")
public class MsResearchProjectController extends BaseController
{
    @Autowired
    private IMsResearchProjectService msResearchProjectService;

    @Autowired
    private IMsProjectGradeService msProjectGradeService;

    // ==================== 项目 CRUD ====================

    @PreAuthorize("@ss.hasPermi('research:project:list')")
    @GetMapping("/list")
    public TableDataInfo list(MsResearchProject msResearchProject)
    {
        startPage();
        List<MsResearchProject> list = msResearchProjectService.selectMsResearchProjectList(msResearchProject);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('research:project:export')")
    @Log(title = "科研项目", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MsResearchProject msResearchProject)
    {
        List<MsResearchProject> list = msResearchProjectService.selectMsResearchProjectList(msResearchProject);
        ExcelUtil<MsResearchProject> util = new ExcelUtil<MsResearchProject>(MsResearchProject.class);
        util.exportExcel(response, list, "科研项目数据");
    }

    @PreAuthorize("@ss.hasPermi('research:project:query')")
    @GetMapping(value = "/{projectId}")
    public AjaxResult getInfo(@PathVariable("projectId") Long projectId)
    {
        return success(msResearchProjectService.selectMsResearchProjectByProjectId(projectId));
    }

    @PreAuthorize("@ss.hasPermi('research:project:add')")
    @Log(title = "科研项目", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MsResearchProject msResearchProject)
    {
        return toAjax(msResearchProjectService.insertMsResearchProject(msResearchProject));
    }

    @PreAuthorize("@ss.hasPermi('research:project:edit')")
    @Log(title = "科研项目", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MsResearchProject msResearchProject)
    {
        return toAjax(msResearchProjectService.updateMsResearchProject(msResearchProject));
    }

    @PreAuthorize("@ss.hasPermi('research:project:remove')")
    @Log(title = "科研项目", businessType = BusinessType.DELETE)
    @DeleteMapping("/{projectIds}")
    public AjaxResult remove(@PathVariable Long[] projectIds)
    {
        return toAjax(msResearchProjectService.deleteMsResearchProjectByProjectIds(projectIds));
    }

    @PreAuthorize("@ss.hasPermi('research:project:import')")
    @Log(title = "科研项目", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<MsResearchProject> util = new ExcelUtil<>(MsResearchProject.class);
        List<MsResearchProject> list = util.importExcel(file.getInputStream());
        String message = msResearchProjectService.importProject(list, updateSupport, getUsername());
        return success(message);
    }

    @PreAuthorize("@ss.hasPermi('research:project:import')")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<MsResearchProject> util = new ExcelUtil<>(MsResearchProject.class);
        util.importTemplateExcel(response, "科研项目数据");
    }

    // ==================== 项目等级 ====================

    @PreAuthorize("@ss.hasPermi('research:projectGrade:query')")
    @GetMapping("/grade/all")
    public AjaxResult allGrades()
    {
        List<MsProjectGrade> list = msProjectGradeService.selectMsProjectGradeAll();
        return success(list);
    }

    @PreAuthorize("@ss.hasPermi('research:projectGrade:list')")
    @GetMapping("/grade/list")
    public TableDataInfo gradeList(MsProjectGrade msProjectGrade)
    {
        startPage();
        List<MsProjectGrade> list = msProjectGradeService.selectMsProjectGradeList(msProjectGrade);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('research:projectGrade:query')")
    @GetMapping(value = "/grade/{gradeId}")
    public AjaxResult getGradeInfo(@PathVariable("gradeId") Long gradeId)
    {
        return success(msProjectGradeService.selectMsProjectGradeByGradeId(gradeId));
    }

    @PreAuthorize("@ss.hasPermi('research:projectGrade:add')")
    @Log(title = "项目等级", businessType = BusinessType.INSERT)
    @PostMapping("/grade")
    public AjaxResult addGrade(@RequestBody MsProjectGrade msProjectGrade)
    {
        return toAjax(msProjectGradeService.insertMsProjectGrade(msProjectGrade));
    }

    @PreAuthorize("@ss.hasPermi('research:projectGrade:edit')")
    @Log(title = "项目等级", businessType = BusinessType.UPDATE)
    @PutMapping("/grade")
    public AjaxResult editGrade(@RequestBody MsProjectGrade msProjectGrade)
    {
        return toAjax(msProjectGradeService.updateMsProjectGrade(msProjectGrade));
    }

    @PreAuthorize("@ss.hasPermi('research:projectGrade:remove')")
    @Log(title = "项目等级", businessType = BusinessType.DELETE)
    @DeleteMapping("/grade/{gradeIds}")
    public AjaxResult removeGrade(@PathVariable Long[] gradeIds)
    {
        return toAjax(msProjectGradeService.deleteMsProjectGradeByGradeIds(gradeIds));
    }

    @PreAuthorize("@ss.hasPermi('research:projectGrade:export')")
    @Log(title = "项目等级", businessType = BusinessType.EXPORT)
    @PostMapping("/grade/export")
    public void exportGrade(HttpServletResponse response, MsProjectGrade msProjectGrade)
    {
        List<MsProjectGrade> list = msProjectGradeService.selectMsProjectGradeList(msProjectGrade);
        ExcelUtil<MsProjectGrade> util = new ExcelUtil<MsProjectGrade>(MsProjectGrade.class);
        util.exportExcel(response, list, "项目等级数据");
    }
}
