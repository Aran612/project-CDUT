package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.AolAssessment;
import com.ruoyi.system.domain.AolCg;
import com.ruoyi.system.domain.AolCourseCommittee;
import com.ruoyi.system.domain.AolLg;
import com.ruoyi.system.domain.AolMapCell;
import com.ruoyi.system.domain.AolMapCourse;
import com.ruoyi.system.domain.AolProgram;
import com.ruoyi.system.domain.MsCourse;
import com.ruoyi.system.mapper.AolAssessmentMapper;
import com.ruoyi.system.mapper.AolCgMapper;
import com.ruoyi.system.mapper.AolCourseCommitteeMapper;
import com.ruoyi.system.mapper.AolLgMapper;
import com.ruoyi.system.mapper.AolMapCellMapper;
import com.ruoyi.system.mapper.AolMapCourseMapper;
import com.ruoyi.system.mapper.AolProgramMapper;
import com.ruoyi.system.mapper.MsCourseMapper;
import com.ruoyi.system.service.IMsAolService;

/**
 * AoL课程图谱Service业务层处理
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
@Service
public class MsAolServiceImpl implements IMsAolService
{
    @Autowired
    private AolProgramMapper aolProgramMapper;

    @Autowired
    private AolCgMapper aolCgMapper;

    @Autowired
    private AolLgMapper aolLgMapper;

    @Autowired
    private AolMapCourseMapper aolMapCourseMapper;

    @Autowired
    private AolMapCellMapper aolMapCellMapper;

    @Autowired
    private AolAssessmentMapper aolAssessmentMapper;

    @Autowired
    private AolCourseCommitteeMapper aolCourseCommitteeMapper;

    @Autowired
    private MsCourseMapper msCourseMapper;

    // ------------------------------------------------------------------
    // 学位项目
    // ------------------------------------------------------------------

    @Override
    public AolProgram selectAolProgramByProgramId(Long programId)
    {
        return aolProgramMapper.selectAolProgramByProgramId(programId);
    }

    @Override
    public List<AolProgram> selectAolProgramList(AolProgram aolProgram)
    {
        return aolProgramMapper.selectAolProgramList(aolProgram);
    }

    @Override
    @Transactional
    public int insertAolProgram(AolProgram aolProgram)
    {
        aolProgram.setCreateTime(DateUtils.getNowDate());
        return aolProgramMapper.insertAolProgram(aolProgram);
    }

    @Override
    @Transactional
    public int updateAolProgram(AolProgram aolProgram)
    {
        aolProgram.setUpdateTime(DateUtils.getNowDate());
        return aolProgramMapper.updateAolProgram(aolProgram);
    }

    @Override
    @Transactional
    public int deleteAolProgramByProgramIds(Long[] programIds)
    {
        return aolProgramMapper.deleteAolProgramByProgramIds(programIds);
    }

    // ------------------------------------------------------------------
    // 能力目标（CG）
    // ------------------------------------------------------------------

    @Override
    public AolCg selectAolCgByCgId(Long cgId)
    {
        return aolCgMapper.selectAolCgByCgId(cgId);
    }

    @Override
    public List<AolCg> selectAolCgList(AolCg aolCg)
    {
        return aolCgMapper.selectAolCgList(aolCg);
    }

    @Override
    @Transactional
    public int insertAolCg(AolCg aolCg)
    {
        aolCg.setCreateTime(DateUtils.getNowDate());
        return aolCgMapper.insertAolCg(aolCg);
    }

    @Override
    @Transactional
    public int updateAolCg(AolCg aolCg)
    {
        aolCg.setUpdateTime(DateUtils.getNowDate());
        return aolCgMapper.updateAolCg(aolCg);
    }

    @Override
    @Transactional
    public int deleteAolCgByCgIds(Long[] cgIds)
    {
        return aolCgMapper.deleteAolCgByCgIds(cgIds);
    }

    // ------------------------------------------------------------------
    // 学习目标（LG）
    // ------------------------------------------------------------------

    @Override
    public AolLg selectAolLgByLgId(Long lgId)
    {
        return aolLgMapper.selectAolLgByLgId(lgId);
    }

    @Override
    public List<AolLg> selectAolLgList(AolLg aolLg)
    {
        return aolLgMapper.selectAolLgList(aolLg);
    }

    @Override
    @Transactional
    public int insertAolLg(AolLg aolLg)
    {
        aolLg.setCreateTime(DateUtils.getNowDate());
        return aolLgMapper.insertAolLg(aolLg);
    }

    @Override
    @Transactional
    public int updateAolLg(AolLg aolLg)
    {
        aolLg.setUpdateTime(DateUtils.getNowDate());
        return aolLgMapper.updateAolLg(aolLg);
    }

    @Override
    @Transactional
    public int deleteAolLgByLgIds(Long[] lgIds)
    {
        return aolLgMapper.deleteAolLgByLgIds(lgIds);
    }

    // ------------------------------------------------------------------
    // 课程图谱
    // ------------------------------------------------------------------

    @Override
    public List<AolMapCourse> selectMapCourses(Long programId)
    {
        AolMapCourse query = new AolMapCourse();
        query.setProgramId(programId);
        List<AolMapCourse> courses = aolMapCourseMapper.selectAolMapCourseList(query);
        for (AolMapCourse course : courses)
        {
            course.setCells(aolMapCellMapper.selectAolMapCellsByMapCourseId(course.getMapCourseId()));
        }
        return courses;
    }

    @Override
    public AolMapCourse selectAolMapCourseByMapCourseId(Long mapCourseId)
    {
        AolMapCourse course = aolMapCourseMapper.selectAolMapCourseByMapCourseId(mapCourseId);
        if (course != null)
        {
            course.setCells(aolMapCellMapper.selectAolMapCellsByMapCourseId(mapCourseId));
        }
        return course;
    }

    @Override
    @Transactional
    public int insertAolMapCourse(AolMapCourse aolMapCourse)
    {
        aolMapCourse.setCreateTime(DateUtils.getNowDate());
        return aolMapCourseMapper.insertAolMapCourse(aolMapCourse);
    }

    @Override
    @Transactional
    public int updateAolMapCourse(AolMapCourse aolMapCourse)
    {
        aolMapCourse.setUpdateTime(DateUtils.getNowDate());
        return aolMapCourseMapper.updateAolMapCourse(aolMapCourse);
    }

    @Override
    @Transactional
    public int deleteAolMapCourseByMapCourseIds(Long[] mapCourseIds)
    {
        // 级联清理覆盖格明细与委员会成员
        aolMapCellMapper.deleteAolMapCellsByMapCourseIds(mapCourseIds);
        aolCourseCommitteeMapper.deleteAolCourseCommitteeByMapCourseIds(mapCourseIds);
        return aolMapCourseMapper.deleteAolMapCourseByMapCourseIds(mapCourseIds);
    }

    @Override
    @Transactional
    public int saveCells(Long mapCourseId, List<AolMapCell> cells)
    {
        // 先删除该课程的旧覆盖格
        aolMapCellMapper.deleteAolMapCellsByMapCourseId(mapCourseId);
        if (cells == null || cells.isEmpty())
        {
            return 0;
        }
        List<AolMapCell> validCells = new ArrayList<>();
        for (AolMapCell cell : cells)
        {
            if (cell == null || cell.getLgId() == null)
            {
                continue;
            }
            // 覆盖层级为空白的跳过
            if (StringUtils.isEmpty(cell.getCoverageLevel()))
            {
                continue;
            }
            cell.setMapCourseId(mapCourseId);
            validCells.add(cell);
        }
        if (validCells.isEmpty())
        {
            return 0;
        }
        return aolMapCellMapper.batchInsertAolMapCells(validCells);
    }

    // ------------------------------------------------------------------
    // 测评方式
    // ------------------------------------------------------------------

    @Override
    public AolAssessment selectAolAssessmentByAssessId(Long assessId)
    {
        return aolAssessmentMapper.selectAolAssessmentByAssessId(assessId);
    }

    @Override
    public List<AolAssessment> selectAolAssessmentList(AolAssessment aolAssessment)
    {
        return aolAssessmentMapper.selectAolAssessmentList(aolAssessment);
    }

    @Override
    @Transactional
    public int insertAolAssessment(AolAssessment aolAssessment)
    {
        aolAssessment.setCreateTime(DateUtils.getNowDate());
        return aolAssessmentMapper.insertAolAssessment(aolAssessment);
    }

    @Override
    @Transactional
    public int updateAolAssessment(AolAssessment aolAssessment)
    {
        aolAssessment.setUpdateTime(DateUtils.getNowDate());
        return aolAssessmentMapper.updateAolAssessment(aolAssessment);
    }

    @Override
    @Transactional
    public int deleteAolAssessmentByAssessIds(Long[] assessIds)
    {
        return aolAssessmentMapper.deleteAolAssessmentByAssessIds(assessIds);
    }

    // ------------------------------------------------------------------
    // 课程委员会
    // ------------------------------------------------------------------

    @Override
    public AolCourseCommittee selectAolCourseCommitteeByCommitteeId(Long committeeId)
    {
        return aolCourseCommitteeMapper.selectAolCourseCommitteeByCommitteeId(committeeId);
    }

    @Override
    public List<AolCourseCommittee> selectAolCourseCommitteeList(AolCourseCommittee aolCourseCommittee)
    {
        return aolCourseCommitteeMapper.selectAolCourseCommitteeList(aolCourseCommittee);
    }

    @Override
    @Transactional
    public int insertAolCourseCommittee(AolCourseCommittee aolCourseCommittee)
    {
        return aolCourseCommitteeMapper.insertAolCourseCommittee(aolCourseCommittee);
    }

    @Override
    @Transactional
    public int updateAolCourseCommittee(AolCourseCommittee aolCourseCommittee)
    {
        return aolCourseCommitteeMapper.updateAolCourseCommittee(aolCourseCommittee);
    }

    @Override
    @Transactional
    public int deleteAolCourseCommitteeByCommitteeIds(Long[] committeeIds)
    {
        return aolCourseCommitteeMapper.deleteAolCourseCommitteeByCommitteeIds(committeeIds);
    }

    @Override
    public List<Map<String, Object>> selectTeacherOptions()
    {
        return aolCourseCommitteeMapper.selectTeacherOptions();
    }

    // ------------------------------------------------------------------
    // 导出
    // ------------------------------------------------------------------

    @Override
    public XSSFWorkbook exportMapExcel(Long programId)
    {
        ProgramExportData data = loadExportData(programId);
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFCellStyle titleStyle = buildBoldStyle(wb, 14);
        XSSFCellStyle headerStyle = buildBoldStyle(wb, 11);
        XSSFCellStyle wrapStyle = buildWrapStyle(wb);
        buildMapSheet(wb, data, titleStyle, headerStyle, wrapStyle);
        buildCoverageSheet(wb, data, headerStyle);
        buildBloomSheet(wb, data, headerStyle, wrapStyle);
        buildAssessmentSheet(wb, data, headerStyle, wrapStyle);
        buildCourseAolSheet(wb, data, headerStyle);
        return wb;
    }

    @Override
    public XWPFDocument exportMapWord(Long programId)
    {
        ProgramExportData data = loadExportData(programId);
        AolProgram program = data.program;
        XWPFDocument doc = new XWPFDocument();
        // 标题
        XWPFParagraph title = doc.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = title.createRun();
        titleRun.setText("AACSB 高级课程地图——" + StringUtils.defaultString(program.getProgramName()) + "（AoL 框架）");
        titleRun.setBold(true);
        titleRun.setFontSize(16);
        // 第1节：CG/LG列表
        addHeading(doc, "一、能力目标（CG）与学习目标（LG）");
        for (AolCg cg : data.cgs)
        {
            addBoldParagraph(doc, cg.getCgCode() + "  " + StringUtils.defaultString(cg.getCgName())
                    + (StringUtils.isNotEmpty(cg.getCgNameEn()) ? "  " + cg.getCgNameEn() : ""));
            addParagraph(doc, StringUtils.defaultString(cg.getDescription()));
            List<AolLg> cgLgs = data.lgMapByCg.get(cg.getCgId());
            if (cgLgs != null)
            {
                for (AolLg lg : cgLgs)
                {
                    addParagraph(doc, "LG " + lg.getLgCode() + "  " + StringUtils.defaultString(lg.getDescription()));
                }
            }
        }
        // 第2节：课程覆盖矩阵
        addHeading(doc, "二、课程覆盖矩阵");
        XWPFTable coverTable = doc.createTable(data.courses.size() + 1, 2 + data.lgs.size());
        fillCell(coverTable.getRow(0).getCell(0), "课程", true);
        for (int i = 0; i < data.lgs.size(); i++)
        {
            fillCell(coverTable.getRow(0).getCell(i + 1), data.lgs.get(i).getLgCode(), true);
        }
        fillCell(coverTable.getRow(0).getCell(data.lgs.size() + 1), "主要支撑LG", true);
        for (int r = 0; r < data.courses.size(); r++)
        {
            AolMapCourse course = data.courses.get(r);
            XWPFTableRow row = coverTable.getRow(r + 1);
            fillCell(row.getCell(0), displayCourseName(course), false);
            Map<Long, String> cellMap = courseCellMap(course);
            for (int i = 0; i < data.lgs.size(); i++)
            {
                AolLg lg = data.lgs.get(i);
                fillCell(row.getCell(i + 1), StringUtils.defaultString(cellMap.get(lg.getLgId())), false);
            }
            fillCell(row.getCell(data.lgs.size() + 1), StringUtils.defaultString(course.getPrimaryLgs()), false);
        }
        // 第3节：布鲁姆层级表
        addHeading(doc, "三、布鲁姆层级表");
        XWPFTable bloomTable = doc.createTable(data.lgs.size() + 1, 5);
        fillCell(bloomTable.getRow(0).getCell(0), "LG", true);
        fillCell(bloomTable.getRow(0).getCell(1), "Description", true);
        fillCell(bloomTable.getRow(0).getCell(2), "Bloom's Level", true);
        fillCell(bloomTable.getRow(0).getCell(3), "Active Verbs", true);
        fillCell(bloomTable.getRow(0).getCell(4), "Course(s)", true);
        for (int r = 0; r < data.lgs.size(); r++)
        {
            AolLg lg = data.lgs.get(r);
            XWPFTableRow row = bloomTable.getRow(r + 1);
            fillCell(row.getCell(0), lg.getLgCode(), false);
            fillCell(row.getCell(1), StringUtils.isNotEmpty(lg.getDescriptionEn()) ? lg.getDescriptionEn() : lg.getDescription(), false);
            fillCell(row.getCell(2), StringUtils.defaultString(lg.getBloomLevel()), false);
            fillCell(row.getCell(3), StringUtils.defaultString(lg.getBloomVerbs()), false);
            fillCell(row.getCell(4), StringUtils.defaultString(data.lgCourses.get(lg.getLgId())), false);
        }
        // 第4节：直接与间接评估措施表
        addHeading(doc, "四、直接与间接评估措施");
        XWPFTable assessTable = doc.createTable(data.assessments.size() + 1, 7);
        fillCell(assessTable.getRow(0).getCell(0), "PLG", true);
        fillCell(assessTable.getRow(0).getCell(1), "CG名称", true);
        fillCell(assessTable.getRow(0).getCell(2), "LGs Assessed", true);
        fillCell(assessTable.getRow(0).getCell(3), "Direct Measures", true);
        fillCell(assessTable.getRow(0).getCell(4), "Rubric / Threshold", true);
        fillCell(assessTable.getRow(0).getCell(5), "Indirect Measures", true);
        fillCell(assessTable.getRow(0).getCell(6), "Frequency", true);
        for (int r = 0; r < data.assessments.size(); r++)
        {
            AolAssessment as = data.assessments.get(r);
            AolCg cg = data.cgMap.get(as.getCgId());
            XWPFTableRow row = assessTable.getRow(r + 1);
            fillCell(row.getCell(0), cg != null ? cg.getCgCode() : "", false);
            fillCell(row.getCell(1), cg != null ? cg.getCgName() : "", false);
            fillCell(row.getCell(2), StringUtils.defaultString(as.getLgsAssessed()), false);
            fillCell(row.getCell(3), toOneLine(as.getDirectMeasures()), false);
            fillCell(row.getCell(4), StringUtils.defaultString(as.getRubricThreshold()), false);
            fillCell(row.getCell(5), toOneLine(as.getIndirectMeasures()), false);
            fillCell(row.getCell(6), StringUtils.defaultString(as.getFrequency()), false);
        }
        // 第5节：AoL循环说明（固定文字）
        addHeading(doc, "五、AoL 循环说明");
        addParagraph(doc, "AoL（Assurance of Learning，学习成果保障）循环：定义 PLG 与 CLO → 对齐课程体系 → 收集 AoL 数据 → 分析结果 → 闭环改进 → 重新评估。");
        addParagraph(doc, "1. 定义 PLG 与 CLO：确定项目学习目标（PLG）与课程学习目标（CLO）；");
        addParagraph(doc, "2. 对齐课程体系：将课程映射到学习目标，明确 I/D/M 覆盖层级；");
        addParagraph(doc, "3. 收集 AoL 数据：按测评方式采集学生表现数据；");
        addParagraph(doc, "4. 分析结果：对照评分标准与阈值分析目标达成度；");
        addParagraph(doc, "5. 闭环改进：针对未达标目标制定并落实改进措施；");
        addParagraph(doc, "6. 重新评估：在后续认证周期验证改进效果。");
        return doc;
    }

    @Override
    public XWPFDocument exportSyllabus(Long mapCourseId)
    {
        AolMapCourse course = aolMapCourseMapper.selectAolMapCourseByMapCourseId(mapCourseId);
        if (course == null)
        {
            throw new ServiceException("图谱课程不存在");
        }
        course.setCells(aolMapCellMapper.selectAolMapCellsByMapCourseId(mapCourseId));
        AolProgram program = aolProgramMapper.selectAolProgramByProgramId(course.getProgramId());
        MsCourse msCourse = course.getCourseId() != null ? msCourseMapper.selectMsCourseByCourseId(course.getCourseId()) : null;
        // 项目的全部CG与LG
        AolCg cgQuery = new AolCg();
        cgQuery.setProgramId(course.getProgramId());
        List<AolCg> cgs = aolCgMapper.selectAolCgList(cgQuery);
        List<AolLg> allLgs = new ArrayList<>();
        for (AolCg cg : cgs)
        {
            AolLg lgQuery = new AolLg();
            lgQuery.setCgId(cg.getCgId());
            allLgs.addAll(aolLgMapper.selectAolLgList(lgQuery));
        }
        // 本课程覆盖的LG与CG
        Set<Long> coveredLgIds = new HashSet<>();
        if (course.getCells() != null)
        {
            for (AolMapCell cell : course.getCells())
            {
                if (StringUtils.isNotEmpty(cell.getCoverageLevel()) && cell.getLgId() != null)
                {
                    coveredLgIds.add(cell.getLgId());
                }
            }
        }
        Set<Long> coveredCgIds = new HashSet<>();
        for (AolLg lg : allLgs)
        {
            if (coveredLgIds.contains(lg.getLgId()))
            {
                coveredCgIds.add(lg.getCgId());
            }
        }
        // 覆盖CG对应的测评方式
        AolAssessment asQuery = new AolAssessment();
        asQuery.setProgramId(course.getProgramId());
        List<AolAssessment> allAssessments = aolAssessmentMapper.selectAolAssessmentList(asQuery);
        List<AolAssessment> coveredAssessments = new ArrayList<>();
        for (AolAssessment as : allAssessments)
        {
            if (coveredCgIds.contains(as.getCgId()))
            {
                coveredAssessments.add(as);
            }
        }
        // 组装文档
        XWPFDocument doc = new XWPFDocument();
        String courseTitle = msCourse != null && StringUtils.isNotEmpty(msCourse.getCourseName())
                ? msCourse.getCourseName() : course.getCourseName();
        XWPFParagraph title = doc.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = title.createRun();
        titleRun.setText("课程大纲 Syllabus——" + StringUtils.defaultString(courseTitle));
        titleRun.setBold(true);
        titleRun.setFontSize(16);
        // 一、课程基本信息
        addHeading(doc, "一、课程基本信息");
        XWPFTable infoTable = doc.createTable(8, 2);
        String creditText = msCourse != null && msCourse.getCreditValue() != null ? String.valueOf(msCourse.getCreditValue()) : "";
        String hoursText = msCourse != null && msCourse.getTotalHours() != null ? String.valueOf(msCourse.getTotalHours()) : "";
        fillCell(infoTable.getRow(0).getCell(0), "课程名称（中文）", true);
        fillCell(infoTable.getRow(0).getCell(1), courseTitle, false);
        fillCell(infoTable.getRow(1).getCell(0), "课程名称（英文）", true);
        fillCell(infoTable.getRow(1).getCell(1), StringUtils.defaultString(course.getCourseNameEn()), false);
        fillCell(infoTable.getRow(2).getCell(0), "课程代码", true);
        fillCell(infoTable.getRow(2).getCell(1), msCourse != null ? StringUtils.defaultString(msCourse.getCourseCode()) : "", false);
        fillCell(infoTable.getRow(3).getCell(0), "学分", true);
        fillCell(infoTable.getRow(3).getCell(1), creditText, false);
        fillCell(infoTable.getRow(4).getCell(0), "学时", true);
        fillCell(infoTable.getRow(4).getCell(1), hoursText, false);
        fillCell(infoTable.getRow(5).getCell(0), "开课院系", true);
        fillCell(infoTable.getRow(5).getCell(1), msCourse != null ? StringUtils.defaultString(msCourse.getDepartmentName()) : "", false);
        fillCell(infoTable.getRow(6).getCell(0), "年级", true);
        fillCell(infoTable.getRow(6).getCell(1), StringUtils.defaultString(course.getYearLevel()), false);
        fillCell(infoTable.getRow(7).getCell(0), "课程性质", true);
        fillCell(infoTable.getRow(7).getCell(1), StringUtils.defaultString(course.getCourseKind()), false);
        // 二、对能力目标的贡献
        addHeading(doc, "二、对能力目标的贡献");
        for (AolCg cg : cgs)
        {
            String mark = coveredCgIds.contains(cg.getCgId()) ? "☑ " : "□ ";
            addParagraph(doc, mark + cg.getCgCode() + "  " + StringUtils.defaultString(cg.getCgName()));
        }
        // 三、对学习目标的贡献
        addHeading(doc, "三、对学习目标的贡献");
        for (AolLg lg : allLgs)
        {
            String mark = coveredLgIds.contains(lg.getLgId()) ? "☑ " : "□ ";
            addParagraph(doc, mark + lg.getLgCode() + "  " + StringUtils.defaultString(lg.getDescription()));
        }
        // 四、测评方式
        addHeading(doc, "四、测评方式");
        XWPFTable asTable = doc.createTable(coveredAssessments.size() + 1, 5);
        fillCell(asTable.getRow(0).getCell(0), "CG", true);
        fillCell(asTable.getRow(0).getCell(1), "所评估的LG", true);
        fillCell(asTable.getRow(0).getCell(2), "直接测评措施", true);
        fillCell(asTable.getRow(0).getCell(3), "评分标准/阈值", true);
        fillCell(asTable.getRow(0).getCell(4), "权重", true);
        Map<Long, AolCg> cgMap = new HashMap<>();
        for (AolCg cg : cgs)
        {
            cgMap.put(cg.getCgId(), cg);
        }
        for (int r = 0; r < coveredAssessments.size(); r++)
        {
            AolAssessment as = coveredAssessments.get(r);
            AolCg cg = cgMap.get(as.getCgId());
            XWPFTableRow row = asTable.getRow(r + 1);
            fillCell(row.getCell(0), cg != null ? cg.getCgCode() + " " + StringUtils.defaultString(cg.getCgName()) : "", false);
            fillCell(row.getCell(1), StringUtils.defaultString(as.getLgsAssessed()), false);
            fillCell(row.getCell(2), toOneLine(as.getDirectMeasures()), false);
            fillCell(row.getCell(3), StringUtils.defaultString(as.getRubricThreshold()), false);
            fillCell(row.getCell(4), "", false);
        }
        return doc;
    }

    // ------------------------------------------------------------------
    // 导出辅助（内部使用）
    // ------------------------------------------------------------------

    /**
     * 导出数据上下文
     */
    private static class ProgramExportData
    {
        /** 学位项目 */
        AolProgram program;
        /** 全部CG（按排序号） */
        List<AolCg> cgs;
        /** 全部LG（按CG顺序展开） */
        List<AolLg> lgs;
        /** cgId -> LG列表 */
        Map<Long, List<AolLg>> lgMapByCg;
        /** cgId -> CG */
        Map<Long, AolCg> cgMap;
        /** 图谱课程（含覆盖格） */
        List<AolMapCourse> courses;
        /** 测评方式 */
        List<AolAssessment> assessments;
        /** 委员会成员（含教师姓名/工号） */
        List<AolCourseCommittee> committees;
        /** lgId -> 覆盖该LG的课程名（顿号连接） */
        Map<Long, String> lgCourses;
    }

    /**
     * 装配导出所需数据
     */
    private ProgramExportData loadExportData(Long programId)
    {
        AolProgram program = aolProgramMapper.selectAolProgramByProgramId(programId);
        if (program == null)
        {
            throw new ServiceException("学位项目不存在");
        }
        ProgramExportData data = new ProgramExportData();
        data.program = program;
        // CG（排序号排序）
        AolCg cgQuery = new AolCg();
        cgQuery.setProgramId(programId);
        data.cgs = aolCgMapper.selectAolCgList(cgQuery);
        // LG（按CG顺序展开）
        data.lgs = new ArrayList<>();
        data.lgMapByCg = new LinkedHashMap<>();
        data.cgMap = new HashMap<>();
        for (AolCg cg : data.cgs)
        {
            data.cgMap.put(cg.getCgId(), cg);
            AolLg lgQuery = new AolLg();
            lgQuery.setCgId(cg.getCgId());
            List<AolLg> cgLgs = aolLgMapper.selectAolLgList(lgQuery);
            data.lgMapByCg.put(cg.getCgId(), cgLgs);
            data.lgs.addAll(cgLgs);
        }
        // 图谱课程（含覆盖格）
        data.courses = selectMapCourses(programId);
        // 反查每个LG被哪些课程覆盖
        data.lgCourses = new HashMap<>();
        for (AolMapCourse course : data.courses)
        {
            if (course.getCells() == null)
            {
                continue;
            }
            String courseName = displayCourseName(course);
            for (AolMapCell cell : course.getCells())
            {
                if (StringUtils.isEmpty(cell.getCoverageLevel()) || cell.getLgId() == null)
                {
                    continue;
                }
                String joined = data.lgCourses.get(cell.getLgId());
                data.lgCourses.put(cell.getLgId(), joined == null ? courseName : joined + "、" + courseName);
            }
        }
        // 测评方式
        AolAssessment asQuery = new AolAssessment();
        asQuery.setProgramId(programId);
        data.assessments = aolAssessmentMapper.selectAolAssessmentList(asQuery);
        // 委员会成员
        AolCourseCommittee cmtQuery = new AolCourseCommittee();
        cmtQuery.setProgramId(programId);
        data.committees = aolCourseCommitteeMapper.selectAolCourseCommitteeList(cmtQuery);
        return data;
    }

    /**
     * 课程展示名（英文优先，空则中文）
     */
    private String displayCourseName(AolMapCourse course)
    {
        return StringUtils.isNotEmpty(course.getCourseNameEn()) ? course.getCourseNameEn()
                : StringUtils.defaultString(course.getCourseName());
    }

    /**
     * 课程覆盖格映射：lgId -> coverageLevel
     */
    private Map<Long, String> courseCellMap(AolMapCourse course)
    {
        Map<Long, String> map = new HashMap<>();
        if (course.getCells() != null)
        {
            for (AolMapCell cell : course.getCells())
            {
                map.put(cell.getLgId(), cell.getCoverageLevel());
            }
        }
        return map;
    }

    /**
     * 多行文本合并为一行（换行替换为分号）
     */
    private String toOneLine(String text)
    {
        if (text == null)
        {
            return "";
        }
        return text.replace("\r\n", "；").replace("\n", "；").replace("\r", "；");
    }

    /**
     * 构建加粗单元格样式
     */
    private XSSFCellStyle buildBoldStyle(XSSFWorkbook wb, int fontSize)
    {
        XSSFCellStyle style = wb.createCellStyle();
        XSSFFont font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) fontSize);
        style.setFont(font);
        style.setWrapText(true);
        return style;
    }

    /**
     * 构建自动换行单元格样式
     */
    private XSSFCellStyle buildWrapStyle(XSSFWorkbook wb)
    {
        XSSFCellStyle style = wb.createCellStyle();
        style.setWrapText(true);
        return style;
    }

    /**
     * Sheet1：AACSB Advanced Curriculum Map（CG块+LG行）
     */
    private void buildMapSheet(XSSFWorkbook wb, ProgramExportData data, XSSFCellStyle titleStyle,
            XSSFCellStyle headerStyle, XSSFCellStyle wrapStyle)
    {
        XSSFSheet sheet = wb.createSheet("1.AACSB Advanced Curriculum Map");
        int rowIdx = 0;
        XSSFRow titleRow = sheet.createRow(rowIdx++);
        XSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("AACSB Advanced Curriculum Map——" + StringUtils.defaultString(data.program.getProgramName())
                + (StringUtils.isNotEmpty(data.program.getProgramNameEn()) ? "（" + data.program.getProgramNameEn() + "）" : ""));
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
        for (AolCg cg : data.cgs)
        {
            // CG块标题行：CG1 | 名称
            XSSFRow cgRow = sheet.createRow(rowIdx++);
            XSSFCell codeCell = cgRow.createCell(0);
            codeCell.setCellValue(StringUtils.defaultString(cg.getCgCode()));
            codeCell.setCellStyle(headerStyle);
            XSSFCell nameCell = cgRow.createCell(1);
            nameCell.setCellValue(StringUtils.defaultString(cg.getCgName())
                    + (StringUtils.isNotEmpty(cg.getCgNameEn()) ? " " + cg.getCgNameEn() : ""));
            nameCell.setCellStyle(headerStyle);
            // CG描述行
            XSSFRow descRow = sheet.createRow(rowIdx++);
            XSSFCell descCell = descRow.createCell(0);
            descCell.setCellValue(StringUtils.defaultString(cg.getDescription()));
            descCell.setCellStyle(wrapStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowIdx - 1, rowIdx - 1, 0, 3));
            // LG行：| LG | 1.1 | 描述
            List<AolLg> cgLgs = data.lgMapByCg.get(cg.getCgId());
            if (cgLgs != null)
            {
                for (AolLg lg : cgLgs)
                {
                    XSSFRow lgRow = sheet.createRow(rowIdx++);
                    lgRow.createCell(1).setCellValue("LG");
                    lgRow.createCell(2).setCellValue(StringUtils.defaultString(lg.getLgCode()));
                    XSSFCell lgDescCell = lgRow.createCell(3);
                    lgDescCell.setCellValue(StringUtils.defaultString(lg.getDescription()));
                    lgDescCell.setCellStyle(wrapStyle);
                }
            }
        }
        sheet.setColumnWidth(0, 8 * 256);
        sheet.setColumnWidth(1, 34 * 256);
        sheet.setColumnWidth(2, 8 * 256);
        sheet.setColumnWidth(3, 90 * 256);
    }

    /**
     * Sheet2：Course Coverage Matrix（课程×LG覆盖矩阵）
     */
    private void buildCoverageSheet(XSSFWorkbook wb, ProgramExportData data, XSSFCellStyle headerStyle)
    {
        XSSFSheet sheet = wb.createSheet("2. Course Coverage Matrix");
        int lgCount = data.lgs.size();
        int totalCols = 2 + lgCount + 1;
        // 首行：CourseMap_Project：<项目名>
        XSSFRow projectRow = sheet.createRow(0);
        XSSFCell projectCell = projectRow.createCell(0);
        projectCell.setCellValue("CourseMap_Project：" + StringUtils.defaultString(data.program.getProgramName()));
        projectCell.setCellStyle(headerStyle);
        if (totalCols > 1)
        {
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, totalCols - 1));
        }
        // 表头两行：按CG分组显示CG编号+名称，子行lg_code
        XSSFRow cgHeaderRow = sheet.createRow(1);
        XSSFRow lgHeaderRow = sheet.createRow(2);
        cgHeaderRow.createCell(0).setCellValue("NO.");
        cgHeaderRow.getCell(0).setCellStyle(headerStyle);
        cgHeaderRow.createCell(1).setCellValue("Course（课程）");
        cgHeaderRow.getCell(1).setCellStyle(headerStyle);
        int col = 2;
        for (AolCg cg : data.cgs)
        {
            List<AolLg> cgLgs = data.lgMapByCg.get(cg.getCgId());
            int span = cgLgs == null ? 0 : cgLgs.size();
            cgHeaderRow.createCell(col).setCellValue(cg.getCgCode() + " " + StringUtils.defaultString(cg.getCgName()));
            cgHeaderRow.getCell(col).setCellStyle(headerStyle);
            if (span > 1)
            {
                sheet.addMergedRegion(new CellRangeAddress(1, 1, col, col + span - 1));
            }
            for (int i = 0; i < span; i++)
            {
                lgHeaderRow.createCell(col + i).setCellValue(cgLgs.get(i).getLgCode());
                lgHeaderRow.getCell(col + i).setCellStyle(headerStyle);
            }
            col += span;
        }
        int primaryCol = col;
        cgHeaderRow.createCell(primaryCol).setCellValue("Primary LGs 主要支撑学习目标");
        cgHeaderRow.getCell(primaryCol).setCellStyle(headerStyle);
        // NO./Course/Primary LGs 纵向合并两行表头
        lgHeaderRow.createCell(0).setCellStyle(headerStyle);
        lgHeaderRow.createCell(1).setCellStyle(headerStyle);
        lgHeaderRow.createCell(primaryCol).setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));
        sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 1));
        sheet.addMergedRegion(new CellRangeAddress(1, 2, primaryCol, primaryCol));
        // 数据行：序号、课程名、各LG覆盖层级、主要支撑学习目标
        int rowIdx = 3;
        int seq = 1;
        for (AolMapCourse course : data.courses)
        {
            XSSFRow row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(seq++);
            row.createCell(1).setCellValue(displayCourseName(course));
            Map<Long, String> cellMap = courseCellMap(course);
            int lgCol = 2;
            for (AolLg lg : data.lgs)
            {
                row.createCell(lgCol++).setCellValue(StringUtils.defaultString(cellMap.get(lg.getLgId())));
            }
            row.createCell(primaryCol).setCellValue(StringUtils.defaultString(course.getPrimaryLgs()));
        }
        sheet.setColumnWidth(0, 6 * 256);
        sheet.setColumnWidth(1, 42 * 256);
        for (int c = 2; c < 2 + lgCount; c++)
        {
            sheet.setColumnWidth(c, 7 * 256);
        }
        sheet.setColumnWidth(primaryCol, 30 * 256);
    }

    /**
     * Sheet3：Bloom's Taxonomy Levels
     */
    private void buildBloomSheet(XSSFWorkbook wb, ProgramExportData data, XSSFCellStyle headerStyle, XSSFCellStyle wrapStyle)
    {
        XSSFSheet sheet = wb.createSheet("3. Bloom's Taxonomy Levels");
        XSSFRow headerRow = sheet.createRow(0);
        String[] headers = {"LG", "Description", "Bloom's Level", "Active Verbs", "Course(s)"};
        for (int i = 0; i < headers.length; i++)
        {
            headerRow.createCell(i).setCellValue(headers[i]);
            headerRow.getCell(i).setCellStyle(headerStyle);
        }
        int rowIdx = 1;
        for (AolLg lg : data.lgs)
        {
            XSSFRow row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(StringUtils.defaultString(lg.getLgCode()));
            XSSFCell descCell = row.createCell(1);
            descCell.setCellValue(StringUtils.isNotEmpty(lg.getDescriptionEn()) ? lg.getDescriptionEn() : StringUtils.defaultString(lg.getDescription()));
            descCell.setCellStyle(wrapStyle);
            row.createCell(2).setCellValue(StringUtils.defaultString(lg.getBloomLevel()));
            XSSFCell verbsCell = row.createCell(3);
            verbsCell.setCellValue(StringUtils.defaultString(lg.getBloomVerbs()));
            verbsCell.setCellStyle(wrapStyle);
            XSSFCell courseCell = row.createCell(4);
            courseCell.setCellValue(StringUtils.defaultString(data.lgCourses.get(lg.getLgId())));
            courseCell.setCellStyle(wrapStyle);
        }
        sheet.setColumnWidth(0, 8 * 256);
        sheet.setColumnWidth(1, 60 * 256);
        sheet.setColumnWidth(2, 14 * 256);
        sheet.setColumnWidth(3, 30 * 256);
        sheet.setColumnWidth(4, 46 * 256);
    }

    /**
     * Sheet4：Direct and Indirect Assessment
     */
    private void buildAssessmentSheet(XSSFWorkbook wb, ProgramExportData data, XSSFCellStyle headerStyle, XSSFCellStyle wrapStyle)
    {
        XSSFSheet sheet = wb.createSheet("4. Direct and Indirect Assessme");
        XSSFRow headerRow = sheet.createRow(0);
        String[] headers = {"PLG", "CG名称", "LGs Assessed", "Direct Measures", "Rubric / Threshold", "Indirect Measures", "Frequency"};
        for (int i = 0; i < headers.length; i++)
        {
            headerRow.createCell(i).setCellValue(headers[i]);
            headerRow.getCell(i).setCellStyle(headerStyle);
        }
        int rowIdx = 1;
        for (AolAssessment as : data.assessments)
        {
            AolCg cg = data.cgMap.get(as.getCgId());
            XSSFRow row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(cg != null ? StringUtils.defaultString(cg.getCgCode()) : "");
            XSSFCell cgNameCell = row.createCell(1);
            cgNameCell.setCellValue(cg != null ? StringUtils.defaultString(cg.getCgName()) : "");
            cgNameCell.setCellStyle(wrapStyle);
            row.createCell(2).setCellValue(StringUtils.defaultString(as.getLgsAssessed()));
            XSSFCell directCell = row.createCell(3);
            directCell.setCellValue(toOneLine(as.getDirectMeasures()));
            directCell.setCellStyle(wrapStyle);
            XSSFCell rubricCell = row.createCell(4);
            rubricCell.setCellValue(StringUtils.defaultString(as.getRubricThreshold()));
            rubricCell.setCellStyle(wrapStyle);
            XSSFCell indirectCell = row.createCell(5);
            indirectCell.setCellValue(toOneLine(as.getIndirectMeasures()));
            indirectCell.setCellStyle(wrapStyle);
            row.createCell(6).setCellValue(StringUtils.defaultString(as.getFrequency()));
        }
        sheet.setColumnWidth(0, 8 * 256);
        sheet.setColumnWidth(1, 26 * 256);
        sheet.setColumnWidth(2, 18 * 256);
        sheet.setColumnWidth(3, 40 * 256);
        sheet.setColumnWidth(4, 46 * 256);
        sheet.setColumnWidth(5, 34 * 256);
        sheet.setColumnWidth(6, 10 * 256);
    }

    /**
     * Sheet5：Course_AOL（课程委员会成员）
     */
    private void buildCourseAolSheet(XSSFWorkbook wb, ProgramExportData data, XSSFCellStyle headerStyle)
    {
        XSSFSheet sheet = wb.createSheet("5.Course_AOL");
        XSSFRow headerRow = sheet.createRow(0);
        String[] headers = {"NO.", "Course", "Teacher1 CDUT ID", "Teacher1 Name", "Teacher2 CDUT ID", "Teacher2 Name", "Teacher3 CDUT ID", "Teacher3 Name"};
        for (int i = 0; i < headers.length; i++)
        {
            headerRow.createCell(i).setCellValue(headers[i]);
            headerRow.getCell(i).setCellStyle(headerStyle);
        }
        // 按图谱课程分组委员会成员（成员序号1-3）
        Map<Long, List<AolCourseCommittee>> committeeMap = new HashMap<>();
        for (AolCourseCommittee cmt : data.committees)
        {
            List<AolCourseCommittee> list = committeeMap.get(cmt.getMapCourseId());
            if (list == null)
            {
                list = new ArrayList<>();
                committeeMap.put(cmt.getMapCourseId(), list);
            }
            list.add(cmt);
        }
        int rowIdx = 1;
        int seq = 1;
        for (AolMapCourse course : data.courses)
        {
            XSSFRow row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(seq++);
            row.createCell(1).setCellValue(displayCourseName(course));
            List<AolCourseCommittee> members = committeeMap.get(course.getMapCourseId());
            for (int m = 0; m < 3; m++)
            {
                if (members == null || m >= members.size())
                {
                    continue;
                }
                AolCourseCommittee member = members.get(m);
                row.createCell(2 + m * 2).setCellValue(StringUtils.defaultString(member.getCdutId()));
                row.createCell(3 + m * 2).setCellValue(StringUtils.defaultString(member.getProfessorName()));
            }
        }
        sheet.setColumnWidth(0, 6 * 256);
        sheet.setColumnWidth(1, 42 * 256);
        for (int c = 2; c < 8; c++)
        {
            sheet.setColumnWidth(c, 18 * 256);
        }
    }

    /**
     * 新增标题段落（加粗）
     */
    private void addHeading(XWPFDocument doc, String text)
    {
        XWPFParagraph p = doc.createParagraph();
        XWPFRun run = p.createRun();
        run.setText(StringUtils.defaultString(text));
        run.setBold(true);
        run.setFontSize(13);
    }

    /**
     * 新增普通段落
     */
    private void addParagraph(XWPFDocument doc, String text)
    {
        XWPFParagraph p = doc.createParagraph();
        p.createRun().setText(StringUtils.defaultString(text));
    }

    /**
     * 新增加粗段落
     */
    private void addBoldParagraph(XWPFDocument doc, String text)
    {
        XWPFParagraph p = doc.createParagraph();
        XWPFRun run = p.createRun();
        run.setText(StringUtils.defaultString(text));
        run.setBold(true);
    }

    /**
     * 填充表格单元格文本
     */
    private void fillCell(XWPFTableCell cell, String text, boolean bold)
    {
        cell.setText(StringUtils.defaultString(text));
        if (bold && !cell.getParagraphs().isEmpty())
        {
            for (XWPFRun run : cell.getParagraphs().get(0).getRuns())
            {
                run.setBold(true);
            }
        }
    }
}
