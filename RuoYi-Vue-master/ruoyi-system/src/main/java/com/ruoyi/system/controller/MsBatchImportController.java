package com.ruoyi.system.controller;

import java.util.List;
import java.awt.Color;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.MsBatchImport;
import com.ruoyi.system.service.IMsBatchImportService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@RestController
@RequestMapping("/research/batchImport")
public class MsBatchImportController extends BaseController {
    @Autowired
    private IMsBatchImportService batchImportService;

    @PreAuthorize("@ss.hasPermi('research:batchImport:import')")
    @Log(title = "一键导入", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, Long professorId) throws Exception {
        ExcelUtil<MsBatchImport> util = new ExcelUtil<>(MsBatchImport.class);
        List<MsBatchImport> list = util.importExcel(file.getInputStream(), 2);
        String message = batchImportService.batchImport(list, professorId, getUsername());
        return success(message);
    }

    @PreAuthorize("@ss.hasPermi('research:batchImport:import')")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws Exception {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("一键导入模板");

        // ========== 列索引（必须与 MsBatchImport @Excel 顺序一致） ==========
        final int C_TYPE = 0, C_TITLE = 1, C_AUTHOR_POS = 2, C_JOURNAL = 3, C_DOI = 4, C_URL = 5, C_ABSTRACT = 6;
        final int C_PROJ_NAME = 7, C_ROLE = 8, C_FUNDING = 9, C_APPROVAL = 10, C_PROJ_SRC = 11;
        final int C_PATENT_NAME = 12, C_INV_RANK = 13, C_PATENT_NO = 14, C_PATENT_CTRY = 15;
        final int C_BOOK_NAME = 16, C_AUTHOR_ROLE = 17, C_PUBLISHER = 18, C_WORDS = 19, C_ISBN = 20;
        final int C_AWARD_NAME = 21, C_AWARD_RANK = 22, C_AWARD_ORG = 23, C_CERT_NO = 24;
        final int C_GRADE = 25, C_DATE = 26, C_END_DATE = 27, C_SCORE = 28;
        final int TOTAL_COLS = 29;

        String[] headers = {
            "成果类型",
            "论文标题", "作者身份", "期刊名称", "DOI", "论文链接", "摘要",
            "项目名称", "承担角色", "经费(万元)", "批准号", "项目来源",
            "专利名称", "发明人排名", "专利号", "授权国家",
            "著作名称", "作者角色", "出版社", "字数(万)", "ISBN",
            "获奖名称", "完成人排名", "颁发单位", "证书编号",
            "等级名称", "发表/开始日期", "结束日期", "实际积分"
        };

        // ========== 预定义样式 ==========
        // 大标题
        CellStyle titleStyle = wb.createCellStyle();
        Font titleFont = wb.createFont();
        titleFont.setFontName("微软雅黑"); titleFont.setFontHeightInPoints((short) 18); titleFont.setBold(true);
        titleFont.setColor(IndexedColors.WHITE.getIndex());
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER); titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 副标题
        CellStyle subtitleStyle = wb.createCellStyle();
        Font subtitleFont = wb.createFont();
        subtitleFont.setFontName("微软雅黑"); subtitleFont.setFontHeightInPoints((short) 10);
        subtitleFont.setColor(IndexedColors.GREY_50_PERCENT.getIndex());
        subtitleStyle.setFont(subtitleFont);
        subtitleStyle.setAlignment(HorizontalAlignment.LEFT); subtitleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // 表头
        CellStyle headerStyle = wb.createCellStyle();
        Font headerFont = wb.createFont();
        headerFont.setFontName("微软雅黑"); headerFont.setFontHeightInPoints((short) 10); headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER); headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderTop(BorderStyle.THIN); headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN); headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setTopBorderColor(IndexedColors.WHITE.getIndex());
        headerStyle.setBottomBorderColor(IndexedColors.WHITE.getIndex());
        headerStyle.setLeftBorderColor(IndexedColors.ROYAL_BLUE.getIndex());
        headerStyle.setRightBorderColor(IndexedColors.ROYAL_BLUE.getIndex());

        // 分组标题栏
        CellStyle groupStyle = wb.createCellStyle();
        Font groupFont = wb.createFont();
        groupFont.setFontName("微软雅黑"); groupFont.setFontHeightInPoints((short) 11); groupFont.setBold(true);
        groupFont.setColor(IndexedColors.WHITE.getIndex());
        groupStyle.setFont(groupFont);
        groupStyle.setAlignment(HorizontalAlignment.LEFT); groupStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        groupStyle.setBorderTop(BorderStyle.THIN); groupStyle.setBorderBottom(BorderStyle.THIN);
        groupStyle.setBorderLeft(BorderStyle.THIN); groupStyle.setBorderRight(BorderStyle.THIN);

        // 普通数据单元格（带边框、自动换行）
        CellStyle baseDataStyle = wb.createCellStyle();
        Font dataFont = wb.createFont();
        dataFont.setFontName("微软雅黑"); dataFont.setFontHeightInPoints((short) 11);
        baseDataStyle.setFont(dataFont);
        baseDataStyle.setAlignment(HorizontalAlignment.LEFT); baseDataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        baseDataStyle.setBorderTop(BorderStyle.THIN); baseDataStyle.setBorderBottom(BorderStyle.THIN);
        baseDataStyle.setBorderLeft(BorderStyle.THIN); baseDataStyle.setBorderRight(BorderStyle.THIN);
        baseDataStyle.setWrapText(true);

        // ========== 按类型预建数据样式 ==========
        // 自定义柔和色：论文淡蓝 / 项目天蓝 / 专利浅绿 / 著作深蓝 / 获奖淡紫
        Color[] rowColors = {
            new Color(220, 233, 244), // 淡蓝
            new Color(212, 234, 247), // 天蓝
            new Color(222, 240, 227), // 浅绿
            new Color(197, 217, 240), // 深蓝
            new Color(232, 220, 240), // 淡紫
        };
        Color[] groupColors = {
            new Color(90, 155, 213),  // 中蓝
            new Color(80, 165, 215),  // 天蓝深
            new Color(100, 180, 130), // 绿
            new Color(60, 110, 180),  // 深蓝
            new Color(140, 110, 180), // 紫
        };

        CellStyle[] rowStyles = new CellStyle[5];
        for (int t = 0; t < 5; t++) {
            XSSFCellStyle xs = (XSSFCellStyle) wb.createCellStyle();
            xs.cloneStyleFrom(baseDataStyle);
            xs.setFillForegroundColor(new XSSFColor(rowColors[t], null));
            xs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            rowStyles[t] = xs;
        }
        // 分组标题样式
        CellStyle[] groupStyles = new CellStyle[5];
        for (int t = 0; t < 5; t++) {
            XSSFCellStyle xs = (XSSFCellStyle) wb.createCellStyle();
            xs.cloneStyleFrom(groupStyle);
            xs.setFillForegroundColor(new XSSFColor(groupColors[t], null));
            xs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            xs.setBorderLeft(BorderStyle.THIN); xs.setBorderRight(BorderStyle.THIN);
            xs.setBorderTop(BorderStyle.THIN); xs.setBorderBottom(BorderStyle.THIN);
            groupStyles[t] = xs;
        }

        // 空白分隔行
        CellStyle blankStyle = wb.createCellStyle(); blankStyle.cloneStyleFrom(baseDataStyle);
        blankStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        blankStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // ========== 构建模板 ==========
        int r = 0;

        // --- 大标题 ---
        Row titleRow = sheet.createRow(r++);
        titleRow.setHeightInPoints(44);
        for (int c = 0; c < TOTAL_COLS; c++) {
            Cell cell = titleRow.createCell(c);
            cell.setCellStyle(titleStyle);
            if (c == 0) cell.setCellValue("  科研信息一键导入模板");
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, TOTAL_COLS - 1));

        // --- 说明行 ---
        Row guideRow = sheet.createRow(r++);
        guideRow.setHeightInPoints(28);
        for (int c = 0; c < TOTAL_COLS; c++) {
            Cell cell = guideRow.createCell(c);
            cell.setCellStyle(subtitleStyle);
            if (c == 0) cell.setCellValue("使用说明：按成果类型填写对应列，每行一种成果，类型可选：论文 / 项目 / 专利 / 著作 / 获奖。下方彩色区域为示例，请替换为实际数据。");
        }
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, TOTAL_COLS - 1));

        // --- 表头行 ---
        Row headerRow = sheet.createRow(r++);
        headerRow.setHeightInPoints(34);
        for (int c = 0; c < TOTAL_COLS; c++) {
            Cell cell = headerRow.createCell(c);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(headers[c]);
        }

        // --- 示例数据（每条精确 29 列：类型(1) + 论文(6) + 项目(5) + 专利(4) + 著作(5) + 获奖(4) + 通用(4) = 29） ---
        // 空段模板：论文段(6空) / 项目段(5空) / 专利段(4空) / 著作段(5空) / 获奖段(4空)
        String[] PE = {"","","","","",""};            // 论文字段占位 x6
        String[] PJ = {"","","","",""};                 // 项目字段占位 x5
        String[] PT = {"","","",""};                    // 专利字段占位 x4
        String[] BK = {"","","","",""};                 // 著作字段占位 x5
        String[] AW = {"","","",""};                    // 获奖字段占位 x4
        // 列结构：[0]类型 [1-6]论文 [7-11]项目 [12-15]专利 [16-20]著作 [21-24]获奖 [25-28]通用

        String[][][] examples = {
            // 论文：类型 + 论文6值 + 项目空5 + 专利空4 + 著作空5 + 获奖空4 + 通用4
            {
                {"论文","基于深度学习的岩石微观结构智能识别研究","第一作者","Nature","10.1038/s41586-025-00001-X","https://www.nature.com/articles/s41586-025-00001-X","本文提出了一种基于深度卷积神经网络的岩石微观结构自动识别方法...", "","","","","", "","","","", "","","","","", "","","","", "N/S/C正刊","2025-06-01","","5000"},
                {"论文","成渝双城经济圈碳排放时空演变及驱动因素分析","通讯作者","Science","10.1126/science.2025-abc123","https://www.science.org/doi/10.1126/science.2025-abc123","基于夜间灯光数据反演成渝地区碳排放格局...", "","","","","", "","","","", "","","","","", "","","","", "N/S/C正刊","2025-03-15","","2500"},
            },
            // 项目：类型 + 论文空6 + 项目5值 + 专利空4 + 著作空5 + 获奖空4 + 通用4
            {
                {"项目","","","","","","", "川西锂矿带深部资源探测关键技术研究","主持","300","2025YFC2900000","国家重点研发计划", "","","","", "","","","","", "","","","", "国自然面上/青年/地区/外国青年学者","2025-01-01","2028-12-31","300"},
                {"项目","","","","","","", "青藏高原东缘地热资源评价与开发潜力分析","参与","150","U20A20100","国家自然科学基金联合基金", "","","","", "","","","","", "","","","", "国自然重点/国际合作/重大研究计划/重大仪器","2024-09-01","2027-08-31","400"},
            },
            // 专利：类型 + 论文空6 + 项目空5 + 专利4值 + 著作空5 + 获奖空4 + 通用4
            {
                {"专利","","","","","","", "","","","","", "一种地质雷达天线自适应耦合装置","第一发明人","ZL202510123456.7","中国", "","","","","", "","","","", "美日韩欧授权发明专利","2025-03-15","2025-08-20","200"},
                {"专利","","","","","","", "","","","","", "基于无人机遥感的地质灾害智能识别系统V1.0","第一发明人","2025SR0123456","中国", "","","","","", "","","","", "计算机软件著作权","2025-04-10","","10"},
            },
            // 著作：类型 + 论文空6 + 项目空5 + 专利空4 + 著作5值 + 获奖空4 + 通用4
            {
                {"著作","","","","","","", "","","","","", "","","","", "地质灾害风险评估理论与方法","著","科学出版社","35","978-7-03-075123-4", "","","","", "中央级/优秀出版社出版著作","2025-05-01","","100"},
                {"著作","","","","","","", "","","","","", "","","","", "普通地质学（第三版）","主编","地质出版社","50","978-7-116-12345-6", "","","","", "国家级规划教材","2024-12-01","","200"},
            },
            // 获奖：类型 + 论文空6 + 项目空5 + 专利空4 + 著作空5 + 获奖4值 + 通用4
            {
                {"获奖","","","","","","", "","","","","", "","","","", "","","","","", "青藏高原重大工程建设地质环境效应与灾害防控","第一完成人","四川省人民政府","2025-J-1-001", "省级自然科学/技术发明/科技进步奖 一等奖","2025-04-01","","1000"},
                {"获奖","","","","","","", "","","","","", "","","","", "","","","","", "深层海相碳酸盐岩油气成藏理论技术创新与重大发现","第三完成人","中华人民共和国国务院","2025-F-2-003", "国家自然科学/技术发明/科技进步奖 二等奖","2025-02-15","","1000"},
            },
        };

        String[] typeNames = {"论文", "项目", "专利", "著作", "获奖"};

        for (int t = 0; t < 5; t++) {
            // 分组标题行
            Row groupRow = sheet.createRow(r++);
            groupRow.setHeightInPoints(28);
            for (int c = 0; c < TOTAL_COLS; c++) {
                Cell cell = groupRow.createCell(c);
                cell.setCellStyle(groupStyles[t]);
            }
            groupRow.getCell(0).setCellValue("  ▎" + typeNames[t] + "类成果（以下为示例，请替换为实际数据）");
            sheet.addMergedRegion(new CellRangeAddress(r - 1, r - 1, 0, TOTAL_COLS - 1));

            // 示例数据行
            for (String[] rowData : examples[t]) {
                Row dataRow = sheet.createRow(r++);
                dataRow.setHeightInPoints(30);
                for (int c = 0; c < TOTAL_COLS; c++) {
                    Cell cell = dataRow.createCell(c);
                    cell.setCellStyle(rowStyles[t]);
                    if (c < rowData.length && rowData[c] != null && !rowData[c].isEmpty()) {
                        cell.setCellValue(rowData[c]);
                    }
                }
            }

            // 空白分隔行
            Row blankRow = sheet.createRow(r++);
            blankRow.setHeightInPoints(10);
            for (int c = 0; c < TOTAL_COLS; c++) {
                Cell cell = blankRow.createCell(c);
                cell.setCellStyle(blankStyle);
            }
            sheet.addMergedRegion(new CellRangeAddress(r - 1, r - 1, 0, TOTAL_COLS - 1));
        }

        // ========== 设置列宽 ==========
        int[] colWidths = {
            14,  // 成果类型
            56,  // 论文标题 / 项目名称 / 专利名称 / 著作名称 / 获奖名称
            16,  // 作者身份 / 承担角色 / 发明人排名 / 作者角色 / 完成人排名
            26,  // 期刊名称 / 项目来源 / 授权国家 / 出版社 / 颁发单位
            26,  // DOI / 批准号
            44,  // 论文链接
            38,  // 摘要
            56,  // 项目名称
            16,  // 承担角色
            14,  // 经费(万元)
            22,  // 批准号
            26,  // 项目来源
            56,  // 专利名称
            16,  // 发明人排名
            28,  // 专利号
            14,  // 授权国家
            56,  // 著作名称
            16,  // 作者角色
            26,  // 出版社
            12,  // 字数(万)
            26,  // ISBN
            56,  // 获奖名称
            16,  // 完成人排名
            26,  // 颁发单位
            22,  // 证书编号
            34,  // 等级名称
            16,  // 发表/开始日期
            16,  // 结束日期
            14,  // 实际积分
        };
        for (int c = 0; c < TOTAL_COLS; c++) {
            sheet.setColumnWidth(c, colWidths[c] * 256);
        }

        // ========== 冻结表头 ==========
        sheet.createFreezePane(0, 3);

        // ========== 添加自动筛选 ==========
        sheet.setAutoFilter(new CellRangeAddress(2, r - 1, 0, TOTAL_COLS - 1));

        // ========== 写入响应 ==========
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=样板 ALPHA.xlsx");
        wb.write(response.getOutputStream());
        wb.close();
    }

}
