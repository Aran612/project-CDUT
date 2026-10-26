package com.ruoyi.system.service.impl;

import java.io.InputStream;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.MsAward;
import com.ruoyi.system.domain.MsBook;
import com.ruoyi.system.domain.MsCvCertification;
import com.ruoyi.system.domain.MsCvCourse;
import com.ruoyi.system.domain.MsCvData;
import com.ruoyi.system.domain.MsCvDevelopment;
import com.ruoyi.system.domain.MsCvEducation;
import com.ruoyi.system.domain.MsCvExperience;
import com.ruoyi.system.domain.MsCvProfile;
import com.ruoyi.system.domain.MsCvService;
import com.ruoyi.system.domain.MsPaper;
import com.ruoyi.system.domain.MsPaperGrade;
import com.ruoyi.system.domain.MsPatent;
import com.ruoyi.system.domain.MsProfessor;
import com.ruoyi.system.domain.MsResearchProject;

/**
 * 教师CV Word导出器（基于AACSB教师CV模版填充，按表头特征定位表格）
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
@Component
public class MsCvWordExporter
{
    /** 模板资源路径（ruoyi-admin/src/main/resources/cv/） */
    private static final String TEMPLATE_PATH = "cv/AACSB_Faculty_CV_Template.docx";

    /** 空值兜底文本 */
    private static final String NONE_TEXT = "无 None";

    /** 声明日期格式 */
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * 导出教师CV Word文档
     *
     * @param response 响应对象
     * @param data CV聚合数据
     */
    public void export(HttpServletResponse response, MsCvData data)
    {
        String name = data.getProfessor() != null && data.getProfessor().getName() != null
                ? data.getProfessor().getName() : "";
        try (InputStream is = new ClassPathResource(TEMPLATE_PATH).getInputStream();
             XWPFDocument doc = new XWPFDocument(is))
        {
            fillDocument(doc, data);
            response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("AACSB教师CV-" + name + ".docx", "UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename*=utf-8''" + fileName);
            doc.write(response.getOutputStream());
            response.getOutputStream().flush();
        }
        catch (Exception e)
        {
            throw new ServiceException("CV导出失败：" + e.getMessage());
        }
    }

    /**
     * 按模板结构填充文档
     */
    private void fillDocument(XWPFDocument doc, MsCvData data)
    {
        // 按表头特征匹配定位各表格（避免依赖固定索引）
        XWPFTable basicTable = null;
        XWPFTable eduTable = null;
        XWPFTable academicExpTable = null;
        XWPFTable industryExpTable = null;
        XWPFTable certTable = null;
        XWPFTable honorTable = null;
        XWPFTable grantTable = null;
        XWPFTable courseTable = null;
        XWPFTable paperTable = null;
        XWPFTable bookTable = null;
        XWPFTable otherTable = null;
        XWPFTable devTable = null;
        XWPFTable serviceTable = null;
        for (XWPFTable table : doc.getTables())
        {
            String header = getRowText(table.getRow(0));
            if (header.contains("Item 项目") && header.contains("Content 内容"))
            {
                basicTable = table;
            }
            else if (header.contains("Highest Degree"))
            {
                eduTable = table;
            }
            else if (header.contains("Institution / Rank"))
            {
                academicExpTable = table;
            }
            else if (header.contains("Key Achievements"))
            {
                industryExpTable = table;
            }
            else if (header.contains("Certification 证书名称"))
            {
                certTable = table;
            }
            else if (header.contains("Honor 荣誉"))
            {
                honorTable = table;
            }
            else if (header.contains("Funding Source"))
            {
                grantTable = table;
            }
            else if (header.contains("Academic Year"))
            {
                courseTable = table;
            }
            else if (header.contains("Journal 期刊"))
            {
                paperTable = table;
            }
            else if (header.contains("Publisher 出版社"))
            {
                bookTable = table;
            }
            else if (header.contains("Venue / Number"))
            {
                otherTable = table;
            }
            else if (header.contains("Organizer 组织单位"))
            {
                devTable = table;
            }
            else if (header.contains("Service 服务内容"))
            {
                serviceTable = table;
            }
        }

        MsCvProfile profile = data.getProfile();
        MsProfessor professor = data.getProfessor();
        String name = professor != null && professor.getName() != null ? professor.getName() : "";

        // 1. 基本信息表（按行首列文字匹配，写入第2列并保留原样式）
        if (basicTable != null)
        {
            for (XWPFTableRow row : basicTable.getRows())
            {
                String label = getCellText(row, 0);
                if (label.startsWith("Full Name"))
                {
                    setCellText(row.getCell(1), noneIfBlank(name));
                }
                else if (label.startsWith("Academic Title"))
                {
                    setCellText(row.getCell(1), noneIfBlank(professor != null ? professor.getTitle() : null));
                }
                else if (label.startsWith("Position"))
                {
                    setCellText(row.getCell(1), noneIfBlank(profile.getPosition()));
                }
                else if (label.startsWith("Employer"))
                {
                    setCellText(row.getCell(1), noneIfBlank(profile.getEmployer()));
                }
                else if (label.startsWith("Address"))
                {
                    setCellText(row.getCell(1), noneIfBlank(profile.getAddress()));
                }
                else if (label.startsWith("Tel"))
                {
                    setCellText(row.getCell(1), noneIfBlank(profile.getPhone()));
                }
                else if (label.startsWith("Email"))
                {
                    setCellText(row.getCell(1), noneIfBlank(professor != null ? professor.getEmail() : null));
                }
            }
        }

        // 2. AACSB师资分类勾选
        checkAacsbClass(doc, profile.getAacsbClass());

        // 3. 研究与教学领域
        fillAreasOfInterest(doc, profile.getAreasOfInterest());

        // 4. 教育背景表（预置行含学位标签，按学位关键字匹配）
        if (eduTable != null)
        {
            fillEducationTable(eduTable, data.getEducations());
        }

        // 5. 博士后/访学经历补充
        XWPFParagraph otherInfoPara = findParagraphContaining(doc, "请在此补充");
        if (otherInfoPara != null)
        {
            String text = otherInfoPara.getText();
            int idx = text.indexOf("请在此补充：");
            String prefix = idx >= 0 ? text.substring(0, idx + "请在此补充：".length()) : text;
            setParagraphText(otherInfoPara, prefix + noneIfBlank(profile.getOtherInfo()));
        }

        // 6. 数据表统一填充
        if (academicExpTable != null)
        {
            List<String[]> rows = new ArrayList<>();
            for (MsCvExperience e : data.getExperiences())
            {
                if ("1".equals(e.getExpType()))
                {
                    rows.add(new String[] { e.getPeriod(), e.getOrganization(), e.getDescription() });
                }
            }
            fillTable(academicExpTable, 1, rows);
        }
        if (industryExpTable != null)
        {
            List<String[]> rows = new ArrayList<>();
            for (MsCvExperience e : data.getExperiences())
            {
                if ("2".equals(e.getExpType()))
                {
                    rows.add(new String[] { e.getPeriod(), e.getOrganization(), e.getRole(), e.getDescription() });
                }
            }
            fillTable(industryExpTable, 1, rows);
        }
        if (certTable != null)
        {
            List<String[]> rows = new ArrayList<>();
            for (MsCvCertification c : data.getCertifications())
            {
                rows.add(new String[] { c.getCertName(), c.getIssuingBody(), c.getCertYear(), c.getStatusText() });
            }
            fillTable(certTable, 1, rows);
        }
        if (honorTable != null)
        {
            List<String[]> rows = new ArrayList<>();
            for (MsAward a : data.getAwards())
            {
                rows.add(new String[] { yearText(a.getAwardDate()), a.getAwardName(), a.getAwardOrg() });
            }
            fillTable(honorTable, 1, rows);
        }
        if (grantTable != null)
        {
            List<String[]> rows = new ArrayList<>();
            for (MsResearchProject p : data.getProjects())
            {
                rows.add(new String[] {
                        periodText(p.getStartDate(), p.getEndDate()),
                        p.getProjectName(), p.getProjectSource(), p.getRole(),
                        p.getFundingAmount() == null ? null
                                : p.getFundingAmount().stripTrailingZeros().toPlainString() + "万元" });
            }
            fillTable(grantTable, 1, rows);
        }
        if (courseTable != null)
        {
            List<String[]> rows = new ArrayList<>();
            for (MsCvCourse c : data.getCourses())
            {
                rows.add(new String[] { c.getCourseName(), c.getLevelName(), c.getAcademicYear(),
                        c.getTeachRole(), c.getWeeklyHours() });
            }
            fillTable(courseTable, 1, rows);
        }
        if (paperTable != null)
        {
            List<String[]> rows = new ArrayList<>();
            for (MsPaper p : data.getPapers())
            {
                String author = p.getAuthorPosition();
                if (author != null && !author.endsWith("作者"))
                {
                    author = author + "作者";
                }
                rows.add(new String[] { author, p.getTitle(), p.getJournalName(),
                        resolvePaperGrade(data, p), yearText(p.getPublishDate()) });
            }
            fillTable(paperTable, 1, rows);
        }
        if (bookTable != null)
        {
            List<String[]> rows = new ArrayList<>();
            for (MsBook b : data.getBooks())
            {
                rows.add(new String[] { b.getAuthorRole(), b.getBookName(), b.getPublisher(),
                        yearText(b.getPublishDate()) });
            }
            fillTable(bookTable, 1, rows);
        }
        if (otherTable != null)
        {
            List<String[]> rows = new ArrayList<>();
            for (MsPatent p : data.getPatents())
            {
                // 类型列：专利等级名（无则“专利”），年份取授权日期（空则申请日期）
                String type = p.getGradeName() != null && !p.getGradeName().trim().isEmpty()
                        ? p.getGradeName() : "专利";
                rows.add(new String[] { type, p.getPatentName(), p.getPatentNumber(),
                        yearText(p.getGrantDate() != null ? p.getGrantDate() : p.getApplicationDate()) });
            }
            fillTable(otherTable, 1, rows);
        }
        if (devTable != null)
        {
            List<String[]> rows = new ArrayList<>();
            for (MsCvDevelopment d : data.getDevelopments())
            {
                rows.add(new String[] { d.getPeriod(), d.getActivity(), d.getOrganizer(), d.getHours() });
            }
            fillTable(devTable, 1, rows);
        }
        if (serviceTable != null)
        {
            List<String[]> rows = new ArrayList<>();
            for (MsCvService s : data.getServices())
            {
                rows.add(new String[] { s.getPeriod(), s.getServiceContent(), s.getOrganization() });
            }
            fillTable(serviceTable, 1, rows);
        }

        // 7. 声明段（签名+日期）
        XWPFParagraph signPara = findParagraphContaining(doc, "Signature 签名：");
        if (signPara != null)
        {
            setParagraphText(signPara, "Signature 签名：" + name
                    + "　　Date 日期：" + LocalDate.now().format(DATE_FORMAT));
        }
    }

    /**
     * AACSB师资分类勾选（将选中分类行首的□替换为☑）
     */
    private void checkAacsbClass(XWPFDocument doc, String aacsbClass)
    {
        if (StringUtils.isEmpty(aacsbClass))
        {
            return;
        }
        String tag = "(" + aacsbClass.trim().toUpperCase() + ")";
        for (XWPFParagraph para : doc.getParagraphs())
        {
            String text = para.getText();
            if (text == null || !text.startsWith("□"))
            {
                continue;
            }
            if (text.contains(tag))
            {
                setParagraphText(para, "☑" + text.substring(1));
                break;
            }
        }
    }

    /**
     * 研究与教学领域填充（1．/2．/3．三行，多于3条时克隆第3段补充）
     */
    private void fillAreasOfInterest(XWPFDocument doc, String areasOfInterest)
    {
        List<String> areas = new ArrayList<>();
        if (areasOfInterest != null)
        {
            for (String line : areasOfInterest.split("\\r?\\n"))
            {
                String item = line.trim();
                if (!item.isEmpty())
                {
                    areas.add(item);
                }
            }
        }
        XWPFParagraph p1 = findParagraphStartingWith(doc, "1．");
        XWPFParagraph p2 = findParagraphStartingWith(doc, "2．");
        XWPFParagraph p3 = findParagraphStartingWith(doc, "3．");
        int i = 0;
        if (p1 != null)
        {
            setParagraphText(p1, "1．" + (i < areas.size() ? areas.get(i++) : ""));
        }
        if (p2 != null)
        {
            setParagraphText(p2, "2．" + (i < areas.size() ? areas.get(i++) : ""));
        }
        if (p3 != null)
        {
            setParagraphText(p3, "3．" + (i < areas.size() ? areas.get(i++) : ""));
            // 多于3条：克隆第3段逐条补充
            XWPFParagraph anchor = p3;
            while (i < areas.size())
            {
                anchor = cloneParagraphAfter(anchor, (i + 1) + "．" + areas.get(i));
                if (anchor == null)
                {
                    break;
                }
                i++;
            }
        }
    }

    /**
     * 教育背景表填充（预置行第1列已有学位标签，按学位文字包含匹配；多余条目克隆行并写入第1列学位）
     */
    private void fillEducationTable(XWPFTable table, List<MsCvEducation> educations)
    {
        if (educations.isEmpty())
        {
            // 无数据时首行填写“无 None”（保留学位标签行）
            XWPFTableRow row = table.getRow(1);
            if (row != null)
            {
                setCellText(row.getCell(1), NONE_TEXT);
                setCellText(row.getCell(2), NONE_TEXT);
                setCellText(row.getCell(3), NONE_TEXT);
            }
            return;
        }
        // 模板预置学位行索引：1=博士 2=硕士 3=学士
        String[] keywords = { "博士", "硕士", "学士" };
        List<Integer> usedRows = new ArrayList<>();
        for (MsCvEducation edu : educations)
        {
            String degree = edu.getDegree() == null ? "" : edu.getDegree();
            int rowIdx = -1;
            for (int k = 0; k < keywords.length; k++)
            {
                if (degree.contains(keywords[k]))
                {
                    rowIdx = 1 + k;
                    break;
                }
            }
            if (rowIdx >= 0 && !usedRows.contains(rowIdx) && table.getRow(rowIdx) != null)
            {
                // 使用预置行（保留第1列学位标签）
                fillEducationRow(table.getRow(rowIdx), null, edu);
                usedRows.add(rowIdx);
            }
            else
            {
                // 克隆最后一个数据行，第1列写入学位
                XWPFTableRow last = table.getRow(table.getRows().size() - 1);
                CTRow ctRow = (CTRow) last.getCtRow().copy();
                XWPFTableRow newRow = new XWPFTableRow(ctRow, table);
                table.addRow(newRow);
                fillEducationRow(newRow, noneIfBlank(degree), edu);
            }
        }
    }

    /**
     * 填充教育背景单行（degreeText非空时覆盖第1列学位标签）
     */
    private void fillEducationRow(XWPFTableRow row, String degreeText, MsCvEducation edu)
    {
        if (degreeText != null)
        {
            setCellText(row.getCell(0), degreeText);
        }
        setCellText(row.getCell(1), noneIfBlank(edu.getMajor()));
        setCellText(row.getCell(2), noneIfBlank(edu.getInstitution()));
        setCellText(row.getCell(3), noneIfBlank(edu.getGradYear()));
        setCellText(row.getCell(4), "1".equals(edu.getIsHighest()) ? "是 Yes" : "否 No");
    }

    /**
     * 数据表统一填充辅助方法（预置空行不够时克隆最后一个空行；数据较少时移除多余空行；全空时填一行“无 None”）
     *
     * @param table 表格
     * @param firstDataRowIndex 首个数据行索引
     * @param rows 数据行（各列文本）
     */
    private void fillTable(XWPFTable table, int firstDataRowIndex, List<String[]> rows)
    {
        if (rows.isEmpty())
        {
            XWPFTableRow row = table.getRow(firstDataRowIndex);
            if (row != null)
            {
                for (int c = 0; c < row.getTableCells().size(); c++)
                {
                    setCellText(row.getCell(c), NONE_TEXT);
                }
            }
            return;
        }
        // 模板预置空行数量
        int preset = table.getRows().size() - firstDataRowIndex;
        // 数据多于空行时克隆最后一个空行补充
        for (int i = preset; i < rows.size(); i++)
        {
            XWPFTableRow last = table.getRow(table.getRows().size() - 1);
            CTRow ctRow = (CTRow) last.getCtRow().copy();
            table.addRow(new XWPFTableRow(ctRow, table));
        }
        // 数据少于空行时移除多余空行
        for (int i = table.getRows().size() - 1; i >= firstDataRowIndex + rows.size(); i--)
        {
            table.removeRow(i);
        }
        for (int i = 0; i < rows.size(); i++)
        {
            XWPFTableRow row = table.getRow(firstDataRowIndex + i);
            String[] values = rows.get(i);
            for (int c = 0; c < values.length && c < row.getTableCells().size(); c++)
            {
                setCellText(row.getCell(c), noneIfBlank(values[c]));
            }
        }
    }

    /**
     * 解析论文名称列等级（优先取关联等级名称，空则按gradeId查字典）
     */
    private String resolvePaperGrade(MsCvData data, MsPaper paper)
    {
        if (paper.getGradeName() != null && !paper.getGradeName().trim().isEmpty())
        {
            return paper.getGradeName();
        }
        if (paper.getGradeId() != null && data.getPaperGrades() != null)
        {
            for (MsPaperGrade grade : data.getPaperGrades())
            {
                if (paper.getGradeId().equals(grade.getGradeId()))
                {
                    return grade.getGradeName();
                }
            }
        }
        return null;
    }

    /**
     * 查找包含指定文字的段落
     */
    private XWPFParagraph findParagraphContaining(XWPFDocument doc, String keyword)
    {
        for (XWPFParagraph para : doc.getParagraphs())
        {
            String text = para.getText();
            if (text != null && text.contains(keyword))
            {
                return para;
            }
        }
        return null;
    }

    /**
     * 查找以指定前缀开头的段落
     */
    private XWPFParagraph findParagraphStartingWith(XWPFDocument doc, String prefix)
    {
        for (XWPFParagraph para : doc.getParagraphs())
        {
            String text = para.getText();
            if (text != null && text.startsWith(prefix))
            {
                return para;
            }
        }
        return null;
    }

    /**
     * 克隆段落并插入到锚点段落之后（保留段落与首run字体样式），返回新段落包装对象
     */
    private XWPFParagraph cloneParagraphAfter(XWPFParagraph anchor, String text)
    {
        CTP template = anchor.getCTP();
        CTP newCtp = (CTP) template.copy();
        // 清空复制段的run
        while (newCtp.sizeOfRArray() > 0)
        {
            newCtp.removeR(0);
        }
        CTR run = newCtp.addNewR();
        if (template.getRArray().length > 0 && template.getRArray(0).getRPr() != null)
        {
            run.setRPr((CTRPr) template.getRArray(0).getRPr().copy());
        }
        run.addNewT().setStringValue(text);
        XmlCursor cursor = anchor.getCTP().newCursor();
        try
        {
            if (!cursor.toNextSibling())
            {
                // 锚点后无兄弟元素（不应出现），放弃克隆
                return null;
            }
            newCtp.newCursor().moveXml(cursor);
        }
        finally
        {
            cursor.dispose();
        }
        return new XWPFParagraph(newCtp, anchor.getBody());
    }

    /**
     * 重写段落文本（保留首个run的字符样式；无run时字体跟随段落标记）
     */
    private void setParagraphText(XWPFParagraph para, String text)
    {
        List<XWPFRun> runs = para.getRuns();
        CTRPr rPr = null;
        if (!runs.isEmpty() && runs.get(0).getCTR().getRPr() != null)
        {
            rPr = (CTRPr) runs.get(0).getCTR().getRPr().copy();
        }
        for (int i = runs.size() - 1; i >= 0; i--)
        {
            para.removeRun(i);
        }
        XWPFRun run = para.createRun();
        run.setText(text);
        if (rPr != null)
        {
            run.getCTR().setRPr(rPr);
        }
        else if (para.getCTP().getPPr() != null && para.getCTP().getPPr().getRPr() != null)
        {
            run.getCTR().setRPr((CTRPr) para.getCTP().getPPr().getRPr().copy());
        }
    }

    /**
     * 重写单元格文本（保留首段落首run样式，清除多余段落与run）
     */
    private void setCellText(XWPFTableCell cell, String text)
    {
        if (cell == null)
        {
            return;
        }
        XWPFParagraph para;
        if (cell.getParagraphs().isEmpty())
        {
            para = cell.addParagraph();
        }
        else
        {
            para = cell.getParagraphs().get(0);
            for (int i = cell.getParagraphs().size() - 1; i > 0; i--)
            {
                cell.removeParagraph(i);
            }
        }
        setParagraphText(para, text);
    }

    /**
     * 读取行内所有单元格文本（“|”分隔）
     */
    private String getRowText(XWPFTableRow row)
    {
        if (row == null)
        {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (XWPFTableCell cell : row.getTableCells())
        {
            sb.append(cell.getText()).append("|");
        }
        return sb.toString();
    }

    /**
     * 读取指定单元格文本
     */
    private String getCellText(XWPFTableRow row, int col)
    {
        XWPFTableCell cell = row.getCell(col);
        return cell == null ? "" : cell.getText().trim();
    }

    /**
     * 空值兜底（空串返回“无 None”）
     */
    private String noneIfBlank(String value)
    {
        if (value == null || value.trim().isEmpty())
        {
            return NONE_TEXT;
        }
        return value.trim();
    }

    /**
     * 日期取年份文本（空返回null，由noneIfBlank兜底）
     */
    private String yearText(java.util.Date date)
    {
        if (date == null)
        {
            return null;
        }
        return String.valueOf(toLocalDate(date).getYear());
    }

    /**
     * 起止时间文本（年份，如“2021~2023”）
     */
    private String periodText(java.util.Date start, java.util.Date end)
    {
        String s = start != null ? String.valueOf(toLocalDate(start).getYear()) : "";
        String e = end != null ? String.valueOf(toLocalDate(end).getYear()) : "";
        if (s.isEmpty() && e.isEmpty())
        {
            return null;
        }
        return s + "~" + e;
    }

    /**
     * Date转LocalDate（兼容java.sql.Date）
     */
    private LocalDate toLocalDate(java.util.Date date)
    {
        if (date instanceof java.sql.Date)
        {
            return ((java.sql.Date) date).toLocalDate();
        }
        return date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
    }
}
