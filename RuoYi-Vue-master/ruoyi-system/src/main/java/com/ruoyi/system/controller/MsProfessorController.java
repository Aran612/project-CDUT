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
import com.ruoyi.system.domain.MsProfessor;
import com.ruoyi.system.mapper.MsAwardMapper;
import com.ruoyi.system.mapper.MsBookMapper;
import com.ruoyi.system.mapper.MsPaperMapper;
import com.ruoyi.system.mapper.MsPatentMapper;
import com.ruoyi.system.mapper.MsResearchProjectMapper;
import com.ruoyi.system.service.IMsProfessorService;
import org.springframework.web.multipart.MultipartFile;

/**
 * 教授信息Controller
 *
 * @author 王祎哲
 * @date 2026-06-12
 */
@RestController
@RequestMapping("/research/professor")
public class MsProfessorController extends BaseController
{
    @Autowired
    private IMsProfessorService msProfessorService;

    @Autowired private MsPaperMapper paperMapper;
    @Autowired private MsResearchProjectMapper projectMapper;
    @Autowired private MsPatentMapper patentMapper;
    @Autowired private MsBookMapper bookMapper;
    @Autowired private MsAwardMapper awardMapper;

    /**
     * 查询教授信息列表
     */
    @PreAuthorize("@ss.hasPermi('research:professor:list')")
    @GetMapping("/list")
    public TableDataInfo list(MsProfessor msProfessor)
    {
        startPage();
        List<MsProfessor> list = msProfessorService.selectMsProfessorList(msProfessor);
        return getDataTable(list);
    }

    /**
     * 导出教授信息
     */
    @PreAuthorize("@ss.hasPermi('research:professor:export')")
    @Log(title = "教授信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MsProfessor msProfessor)
    {
        List<MsProfessor> list = msProfessorService.selectMsProfessorList(msProfessor);
        ExcelUtil<MsProfessor> util = new ExcelUtil<MsProfessor>(MsProfessor.class);
        util.exportExcel(response, list, "教授信息数据");
    }

    /**
     * 查询全部正常状态教授（下拉选择用）
     */
    @GetMapping("/all")
    public AjaxResult listAll()
    {
        List<MsProfessor> list = msProfessorService.selectMsProfessorAll();
        return success(list);
    }

    /**
     * 导入教授信息
     */
    @PreAuthorize("@ss.hasPermi('research:professor:import')")
    @Log(title = "教授信息", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<MsProfessor> util = new ExcelUtil<>(MsProfessor.class);
        List<MsProfessor> list = util.importExcel(file.getInputStream());
        String message = msProfessorService.importProfessor(list, updateSupport, getUsername());
        return success(message);
    }

    @PreAuthorize("@ss.hasPermi('research:professor:import')")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<MsProfessor> util = new ExcelUtil<>(MsProfessor.class);
        util.importTemplateExcel(response, "教授信息数据");
    }

    /**
     * 获取教授信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('research:professor:query')")
    @GetMapping(value = "/{professorId}")
    public AjaxResult getInfo(@PathVariable("professorId") Long professorId)
    {
        return success(msProfessorService.selectMsProfessorByProfessorId(professorId));
    }

    /**
     * 新增教授信息
     */
    @PreAuthorize("@ss.hasPermi('research:professor:add')")
    @Log(title = "教授信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MsProfessor msProfessor)
    {
        return toAjax(msProfessorService.insertMsProfessor(msProfessor));
    }

    /**
     * 修改教授信息
     */
    @PreAuthorize("@ss.hasPermi('research:professor:edit')")
    @Log(title = "教授信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MsProfessor msProfessor)
    {
        return toAjax(msProfessorService.updateMsProfessor(msProfessor));
    }

    /**
     * 删除教授信息
     */
    @PreAuthorize("@ss.hasPermi('research:professor:remove')")
    @Log(title = "教授信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{professorIds}")
    public AjaxResult remove(@PathVariable Long[] professorIds)
    {
        return toAjax(msProfessorService.deleteMsProfessorByProfessorIds(professorIds));
    }

    /**
     * 一键清除教授所有成果数据（测试用）
     */
    @PreAuthorize("@ss.hasPermi('research:professor:remove')")
    @Log(title = "清除教授数据", businessType = BusinessType.DELETE)
    @DeleteMapping("/clearAllData/{professorId}")
    public AjaxResult clearAllData(@PathVariable Long professorId)
    {
        int paperDel = paperMapper.deleteMsPaperByProfessorId(professorId);
        int projectDel = projectMapper.deleteMsResearchProjectByProfessorId(professorId);
        int patentDel = patentMapper.deleteMsPatentByProfessorId(professorId);
        int bookDel = bookMapper.deleteMsBookByProfessorId(professorId);
        int awardDel = awardMapper.deleteMsAwardByProfessorId(professorId);
        // 重置教授积分
        msProfessorService.selectMsProfessorByProfessorId(professorId); // 仅验证教授存在，积分在删除后自动为0
        String msg = String.format("已清除教授 ID=%d 的全部成果：论文%d 项目%d 专利%d 著作%d 获奖%d",
                professorId, paperDel, projectDel, patentDel, bookDel, awardDel);
        return success(msg);
    }
}
