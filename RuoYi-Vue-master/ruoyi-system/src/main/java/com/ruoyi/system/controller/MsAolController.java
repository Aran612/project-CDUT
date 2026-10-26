package com.ruoyi.system.controller;

import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
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
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.system.domain.AolAssessment;
import com.ruoyi.system.domain.AolCg;
import com.ruoyi.system.domain.AolCourseCommittee;
import com.ruoyi.system.domain.AolLg;
import com.ruoyi.system.domain.AolMapCell;
import com.ruoyi.system.domain.AolMapCourse;
import com.ruoyi.system.domain.AolProgram;
import com.ruoyi.system.service.IMsAolService;

/**
 * AoL课程图谱Controller
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
@RestController
@RequestMapping("/aol")
public class MsAolController extends BaseController
{
    @Autowired
    private IMsAolService msAolService;

    // ------------------------------------------------------------------
    // 学位项目
    // ------------------------------------------------------------------

    /**
     * 查询学位项目列表
     */
    @PreAuthorize("@ss.hasPermi('aol:program:list')")
    @GetMapping("/program/list")
    public TableDataInfo programList(AolProgram aolProgram)
    {
        startPage();
        List<AolProgram> list = msAolService.selectAolProgramList(aolProgram);
        return getDataTable(list);
    }

    /**
     * 新增学位项目
     */
    @PreAuthorize("@ss.hasPermi('aol:program:add')")
    @Log(title = "AoL学位项目", businessType = BusinessType.INSERT)
    @PostMapping("/program")
    public AjaxResult programAdd(@RequestBody AolProgram aolProgram)
    {
        return toAjax(msAolService.insertAolProgram(aolProgram));
    }

    /**
     * 修改学位项目
     */
    @PreAuthorize("@ss.hasPermi('aol:program:edit')")
    @Log(title = "AoL学位项目", businessType = BusinessType.UPDATE)
    @PutMapping("/program")
    public AjaxResult programEdit(@RequestBody AolProgram aolProgram)
    {
        return toAjax(msAolService.updateAolProgram(aolProgram));
    }

    /**
     * 删除学位项目
     */
    @PreAuthorize("@ss.hasPermi('aol:program:remove')")
    @Log(title = "AoL学位项目", businessType = BusinessType.DELETE)
    @DeleteMapping("/program/{programIds}")
    public AjaxResult programRemove(@PathVariable Long[] programIds)
    {
        return toAjax(msAolService.deleteAolProgramByProgramIds(programIds));
    }

    // ------------------------------------------------------------------
    // 能力目标（CG）
    // ------------------------------------------------------------------

    /**
     * 查询能力目标列表（按项目查询）
     */
    @PreAuthorize("@ss.hasPermi('aol:program:list')")
    @GetMapping("/cg/list")
    public TableDataInfo cgList(AolCg aolCg)
    {
        startPage();
        List<AolCg> list = msAolService.selectAolCgList(aolCg);
        return getDataTable(list);
    }

    /**
     * 新增能力目标
     */
    @PreAuthorize("@ss.hasPermi('aol:program:add')")
    @Log(title = "AoL能力目标", businessType = BusinessType.INSERT)
    @PostMapping("/cg")
    public AjaxResult cgAdd(@RequestBody AolCg aolCg)
    {
        return toAjax(msAolService.insertAolCg(aolCg));
    }

    /**
     * 修改能力目标
     */
    @PreAuthorize("@ss.hasPermi('aol:program:edit')")
    @Log(title = "AoL能力目标", businessType = BusinessType.UPDATE)
    @PutMapping("/cg")
    public AjaxResult cgEdit(@RequestBody AolCg aolCg)
    {
        return toAjax(msAolService.updateAolCg(aolCg));
    }

    /**
     * 删除能力目标
     */
    @PreAuthorize("@ss.hasPermi('aol:program:remove')")
    @Log(title = "AoL能力目标", businessType = BusinessType.DELETE)
    @DeleteMapping("/cg/{cgIds}")
    public AjaxResult cgRemove(@PathVariable Long[] cgIds)
    {
        return toAjax(msAolService.deleteAolCgByCgIds(cgIds));
    }

    // ------------------------------------------------------------------
    // 学习目标（LG）
    // ------------------------------------------------------------------

    /**
     * 查询学习目标列表（按能力目标查询）
     */
    @PreAuthorize("@ss.hasPermi('aol:program:list')")
    @GetMapping("/lg/list")
    public TableDataInfo lgList(AolLg aolLg)
    {
        startPage();
        List<AolLg> list = msAolService.selectAolLgList(aolLg);
        return getDataTable(list);
    }

    /**
     * 新增学习目标
     */
    @PreAuthorize("@ss.hasPermi('aol:program:add')")
    @Log(title = "AoL学习目标", businessType = BusinessType.INSERT)
    @PostMapping("/lg")
    public AjaxResult lgAdd(@RequestBody AolLg aolLg)
    {
        return toAjax(msAolService.insertAolLg(aolLg));
    }

    /**
     * 修改学习目标
     */
    @PreAuthorize("@ss.hasPermi('aol:program:edit')")
    @Log(title = "AoL学习目标", businessType = BusinessType.UPDATE)
    @PutMapping("/lg")
    public AjaxResult lgEdit(@RequestBody AolLg aolLg)
    {
        return toAjax(msAolService.updateAolLg(aolLg));
    }

    /**
     * 删除学习目标
     */
    @PreAuthorize("@ss.hasPermi('aol:program:remove')")
    @Log(title = "AoL学习目标", businessType = BusinessType.DELETE)
    @DeleteMapping("/lg/{lgIds}")
    public AjaxResult lgRemove(@PathVariable Long[] lgIds)
    {
        return toAjax(msAolService.deleteAolLgByLgIds(lgIds));
    }

    // ------------------------------------------------------------------
    // 课程图谱
    // ------------------------------------------------------------------

    /**
     * 查询图谱课程列表（按项目查询，带出覆盖格明细）
     */
    @PreAuthorize("@ss.hasPermi('aol:map:list')")
    @GetMapping("/map/list")
    public TableDataInfo mapList(AolMapCourse aolMapCourse)
    {
        startPage();
        List<AolMapCourse> list = msAolService.selectMapCourses(aolMapCourse.getProgramId());
        return getDataTable(list);
    }

    /**
     * 新增图谱课程
     */
    @PreAuthorize("@ss.hasPermi('aol:map:add')")
    @Log(title = "AoL图谱课程", businessType = BusinessType.INSERT)
    @PostMapping("/map")
    public AjaxResult mapAdd(@RequestBody AolMapCourse aolMapCourse)
    {
        return toAjax(msAolService.insertAolMapCourse(aolMapCourse));
    }

    /**
     * 修改图谱课程
     */
    @PreAuthorize("@ss.hasPermi('aol:map:edit')")
    @Log(title = "AoL图谱课程", businessType = BusinessType.UPDATE)
    @PutMapping("/map")
    public AjaxResult mapEdit(@RequestBody AolMapCourse aolMapCourse)
    {
        return toAjax(msAolService.updateAolMapCourse(aolMapCourse));
    }

    /**
     * 删除图谱课程
     */
    @PreAuthorize("@ss.hasPermi('aol:map:remove')")
    @Log(title = "AoL图谱课程", businessType = BusinessType.DELETE)
    @DeleteMapping("/map/{mapCourseIds}")
    public AjaxResult mapRemove(@PathVariable Long[] mapCourseIds)
    {
        return toAjax(msAolService.deleteAolMapCourseByMapCourseIds(mapCourseIds));
    }

    /**
     * 保存课程覆盖格（请求体：{"mapCourseId":1,"cells":[{"lgId":11,"coverageLevel":"I"},...]}）
     */
    @PreAuthorize("@ss.hasPermi('aol:map:edit')")
    @Log(title = "AoL图谱覆盖格", businessType = BusinessType.UPDATE)
    @PutMapping("/map/cells")
    public AjaxResult saveCells(@RequestBody CellSaveBody body)
    {
        msAolService.saveCells(body.getMapCourseId(), body.getCells());
        return success();
    }

    /**
     * 覆盖格保存请求体
     */
    public static class CellSaveBody
    {
        /** 图谱课程ID */
        private Long mapCourseId;

        /** 覆盖格明细 */
        private List<AolMapCell> cells;

        public void setMapCourseId(Long mapCourseId)
        {
            this.mapCourseId = mapCourseId;
        }

        public Long getMapCourseId()
        {
            return mapCourseId;
        }

        public void setCells(List<AolMapCell> cells)
        {
            this.cells = cells;
        }

        public List<AolMapCell> getCells()
        {
            return cells;
        }
    }

    // ------------------------------------------------------------------
    // 测评方式
    // ------------------------------------------------------------------

    /**
     * 查询测评方式列表（按项目查询）
     */
    @PreAuthorize("@ss.hasPermi('aol:assessment:list')")
    @GetMapping("/assessment/list")
    public TableDataInfo assessmentList(AolAssessment aolAssessment)
    {
        startPage();
        List<AolAssessment> list = msAolService.selectAolAssessmentList(aolAssessment);
        return getDataTable(list);
    }

    /**
     * 新增测评方式
     */
    @PreAuthorize("@ss.hasPermi('aol:assessment:add')")
    @Log(title = "AoL测评方式", businessType = BusinessType.INSERT)
    @PostMapping("/assessment")
    public AjaxResult assessmentAdd(@RequestBody AolAssessment aolAssessment)
    {
        return toAjax(msAolService.insertAolAssessment(aolAssessment));
    }

    /**
     * 修改测评方式
     */
    @PreAuthorize("@ss.hasPermi('aol:assessment:edit')")
    @Log(title = "AoL测评方式", businessType = BusinessType.UPDATE)
    @PutMapping("/assessment")
    public AjaxResult assessmentEdit(@RequestBody AolAssessment aolAssessment)
    {
        return toAjax(msAolService.updateAolAssessment(aolAssessment));
    }

    /**
     * 删除测评方式
     */
    @PreAuthorize("@ss.hasPermi('aol:assessment:remove')")
    @Log(title = "AoL测评方式", businessType = BusinessType.DELETE)
    @DeleteMapping("/assessment/{assessIds}")
    public AjaxResult assessmentRemove(@PathVariable Long[] assessIds)
    {
        return toAjax(msAolService.deleteAolAssessmentByAssessIds(assessIds));
    }

    // ------------------------------------------------------------------
    // 课程委员会
    // ------------------------------------------------------------------

    /**
     * 查询委员会成员列表（按项目/图谱课程查询）
     */
    @PreAuthorize("@ss.hasPermi('aol:committee:list')")
    @GetMapping("/committee/list")
    public TableDataInfo committeeList(AolCourseCommittee aolCourseCommittee)
    {
        startPage();
        List<AolCourseCommittee> list = msAolService.selectAolCourseCommitteeList(aolCourseCommittee);
        return getDataTable(list);
    }

    /**
     * 新增委员会成员
     */
    @PreAuthorize("@ss.hasPermi('aol:committee:add')")
    @Log(title = "AoL课程委员会", businessType = BusinessType.INSERT)
    @PostMapping("/committee")
    public AjaxResult committeeAdd(@RequestBody AolCourseCommittee aolCourseCommittee)
    {
        return toAjax(msAolService.insertAolCourseCommittee(aolCourseCommittee));
    }

    /**
     * 修改委员会成员
     */
    @PreAuthorize("@ss.hasPermi('aol:committee:edit')")
    @Log(title = "AoL课程委员会", businessType = BusinessType.UPDATE)
    @PutMapping("/committee")
    public AjaxResult committeeEdit(@RequestBody AolCourseCommittee aolCourseCommittee)
    {
        return toAjax(msAolService.updateAolCourseCommittee(aolCourseCommittee));
    }

    /**
     * 删除委员会成员
     */
    @PreAuthorize("@ss.hasPermi('aol:committee:remove')")
    @Log(title = "AoL课程委员会", businessType = BusinessType.DELETE)
    @DeleteMapping("/committee/{committeeIds}")
    public AjaxResult committeeRemove(@PathVariable Long[] committeeIds)
    {
        return toAjax(msAolService.deleteAolCourseCommitteeByCommitteeIds(committeeIds));
    }

    // ------------------------------------------------------------------
    // 教师下拉选项
    // ------------------------------------------------------------------

    /**
     * 查询教师下拉选项（教师ID/姓名/工号）
     */
    @GetMapping("/teachers")
    public AjaxResult teacherOptions()
    {
        List<Map<String, Object>> options = msAolService.selectTeacherOptions();
        return success(options);
    }

    // ------------------------------------------------------------------
    // 导出
    // ------------------------------------------------------------------

    /**
     * 导出课程图谱Excel（5个Sheet，官方模板格式）
     */
    @PreAuthorize("@ss.hasPermi('aol:export')")
    @Log(title = "AoL课程图谱导出", businessType = BusinessType.EXPORT)
    @PostMapping("/export/mapExcel")
    public void exportMapExcel(HttpServletResponse response, @RequestParam("programId") Long programId) throws Exception
    {
        AolProgram program = msAolService.selectAolProgramByProgramId(programId);
        String programName = program != null ? program.getProgramName() : "课程图谱";
        XSSFWorkbook workbook = msAolService.exportMapExcel(programId);
        try
        {
            FileUtils.setAttachmentResponseHeader(response, "AACSB课程图谱_" + programName + ".xlsx");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            ServletOutputStream out = response.getOutputStream();
            workbook.write(out);
        }
        finally
        {
            workbook.close();
        }
    }

    /**
     * 导出课程图谱Word
     */
    @PreAuthorize("@ss.hasPermi('aol:export')")
    @Log(title = "AoL课程图谱导出", businessType = BusinessType.EXPORT)
    @PostMapping("/export/mapWord")
    public void exportMapWord(HttpServletResponse response, @RequestParam("programId") Long programId) throws Exception
    {
        AolProgram program = msAolService.selectAolProgramByProgramId(programId);
        String programName = program != null ? program.getProgramName() : "课程图谱";
        XWPFDocument document = msAolService.exportMapWord(programId);
        try
        {
            FileUtils.setAttachmentResponseHeader(response, "AACSB高级课程地图_" + programName + ".docx");
            response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            ServletOutputStream out = response.getOutputStream();
            document.write(out);
        }
        finally
        {
            document.close();
        }
    }

    /**
     * 导出课程大纲Syllabus Word
     */
    @PreAuthorize("@ss.hasPermi('aol:export')")
    @Log(title = "AoL课程大纲导出", businessType = BusinessType.EXPORT)
    @PostMapping("/export/syllabus")
    public void exportSyllabus(HttpServletResponse response, @RequestParam("mapCourseId") Long mapCourseId) throws Exception
    {
        AolMapCourse course = msAolService.selectAolMapCourseByMapCourseId(mapCourseId);
        if (course == null)
        {
            throw new ServiceException("图谱课程不存在");
        }
        String courseName = StringUtils.isNotEmpty(course.getCourseNameEn()) ? course.getCourseNameEn() : course.getCourseName();
        XWPFDocument document = msAolService.exportSyllabus(mapCourseId);
        try
        {
            FileUtils.setAttachmentResponseHeader(response, "课程大纲Syllabus_" + StringUtils.defaultString(courseName) + ".docx");
            response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            ServletOutputStream out = response.getOutputStream();
            document.write(out);
        }
        finally
        {
            document.close();
        }
    }
}
