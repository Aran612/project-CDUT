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
import com.ruoyi.system.domain.MsCourse;
import com.ruoyi.system.service.IMsCourseService;

/**
 * 课程Controller
 *
 * @author CDUT-ALPHA
 * @date 2026-07-26
 */
@RestController
@RequestMapping("/research/course")
public class MsCourseController extends BaseController
{
    @Autowired
    private IMsCourseService msCourseService;

    /**
     * 查询课程列表
     */
    @PreAuthorize("@ss.hasPermi('research:course:list')")
    @GetMapping("/list")
    public TableDataInfo list(MsCourse msCourse)
    {
        startPage();
        List<MsCourse> list = msCourseService.selectMsCourseList(msCourse);
        return getDataTable(list);
    }

    /**
     * 获取所有课程（下拉选择用）
     */
    @PreAuthorize("@ss.hasPermi('research:course:list')")
    @GetMapping("/all")
    public AjaxResult all()
    {
        return success(msCourseService.selectAllMsCourse());
    }

    /**
     * 导出课程数据
     */
    @PreAuthorize("@ss.hasPermi('research:course:export')")
    @Log(title = "课程管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MsCourse msCourse)
    {
        List<MsCourse> list = msCourseService.selectMsCourseList(msCourse);
        ExcelUtil<MsCourse> util = new ExcelUtil<MsCourse>(MsCourse.class);
        util.exportExcel(response, list, "课程数据");
    }

    /**
     * 获取课程详细信息
     */
    @PreAuthorize("@ss.hasPermi('research:course:query')")
    @GetMapping(value = "/{courseId}")
    public AjaxResult getInfo(@PathVariable("courseId") Long courseId)
    {
        return success(msCourseService.selectMsCourseByCourseId(courseId));
    }

    /**
     * 新增课程
     */
    @PreAuthorize("@ss.hasPermi('research:course:add')")
    @Log(title = "课程管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MsCourse msCourse)
    {
        return toAjax(msCourseService.insertMsCourse(msCourse));
    }

    /**
     * 修改课程
     */
    @PreAuthorize("@ss.hasPermi('research:course:edit')")
    @Log(title = "课程管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MsCourse msCourse)
    {
        return toAjax(msCourseService.updateMsCourse(msCourse));
    }

    /**
     * 删除课程
     */
    @PreAuthorize("@ss.hasPermi('research:course:remove')")
    @Log(title = "课程管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{courseIds}")
    public AjaxResult remove(@PathVariable Long[] courseIds)
    {
        return toAjax(msCourseService.deleteMsCourseByCourseIds(courseIds));
    }

    /**
     * 导入课程数据
     */
    @PreAuthorize("@ss.hasPermi('research:course:import')")
    @Log(title = "课程管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<MsCourse> util = new ExcelUtil<MsCourse>(MsCourse.class);
        List<MsCourse> courseList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = msCourseService.importCourse(courseList, updateSupport, operName);
        return success(message);
    }

    /**
     * 下载课程导入模板
     */
    @PreAuthorize("@ss.hasPermi('research:course:import')")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<MsCourse> util = new ExcelUtil<MsCourse>(MsCourse.class);
        util.importTemplateExcel(response, "课程数据");
    }
}
