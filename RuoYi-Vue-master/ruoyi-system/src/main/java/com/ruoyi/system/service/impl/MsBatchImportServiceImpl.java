package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.IMsBatchImportService;

@Service
public class MsBatchImportServiceImpl implements IMsBatchImportService {

    @Autowired private MsProfessorMapper professorMapper;
    @Autowired private MsPaperMapper paperMapper;
    @Autowired private MsResearchProjectMapper projectMapper;
    @Autowired private MsPatentMapper patentMapper;
    @Autowired private MsBookMapper bookMapper;
    @Autowired private MsAwardMapper awardMapper;
    @Autowired private MsPaperGradeMapper paperGradeMapper;
    @Autowired private MsProjectGradeMapper projectGradeMapper;
    @Autowired private MsPatentGradeMapper patentGradeMapper;
    @Autowired private MsBookGradeMapper bookGradeMapper;
    @Autowired private MsAwardGradeMapper awardGradeMapper;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public String batchImport(List<MsBatchImport> list, Long professorId, String operName) {
        if (list == null || list.isEmpty()) throw new ServiceException("导入数据不能为空！");
        if (professorId == null) throw new ServiceException("请先选择一位教授！");
        MsProfessor professor = professorMapper.selectMsProfessorByProfessorId(professorId);
        if (professor == null) throw new ServiceException("指定的教授不存在！");

        int successNum = 0, failureNum = 0;
        int[] typeCount = new int[5]; // 论文/项目/专利/著作/获奖 各成功数
        StringBuilder successMsg = new StringBuilder(), failureMsg = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            MsBatchImport row = list.get(i);
            if (row == null) continue; // 跳过空行（模板标题、说明、分组标题、分隔行等）
            String type = row.getAchievementType();
            if (type == null || type.trim().isEmpty()) {
                failureNum++; failureMsg.append("<br/>").append(failureNum).append("、第").append(i+1).append("行缺少成果类型"); continue;
            }
            type = type.trim();
            try {
                switch (type) {
                    case "论文": importPaper(row, professorId, operName); typeCount[0]++; break;
                    case "项目": importProject(row, professorId, operName); typeCount[1]++; break;
                    case "专利": importPatent(row, professorId, operName); typeCount[2]++; break;
                    case "著作": importBook(row, professorId, operName); typeCount[3]++; break;
                    case "获奖": importAward(row, professorId, operName); typeCount[4]++; break;
                    default: failureNum++; failureMsg.append("<br/>").append(failureNum).append("、第").append(i+1).append("行成果类型 \"").append(type).append("\" 无效，请填写：论文/项目/专利/著作/获奖"); continue;
                }
                successNum++;
                successMsg.append("<br/>").append(successNum).append("、").append(type).append(" \"").append(getName(row, type)).append("\" 导入成功");
                professorMapper.updateMsProfessorTotalScore(professorId);
            } catch (Exception e) {
                failureNum++;
                failureMsg.append("<br/>").append(failureNum).append("、第").append(i+1).append("行").append(type).append("导入失败：").append(e.getMessage());
            }
        }

        StringBuilder result = new StringBuilder();
        result.append("共 ").append(list.size()).append(" 条，成功 ").append(successNum).append(" 条");
        if (typeCount[0] > 0) result.append("（论文").append(typeCount[0]).append("）");
        if (typeCount[1] > 0) result.append("（项目").append(typeCount[1]).append("）");
        if (typeCount[2] > 0) result.append("（专利").append(typeCount[2]).append("）");
        if (typeCount[3] > 0) result.append("（著作").append(typeCount[3]).append("）");
        if (typeCount[4] > 0) result.append("（获奖").append(typeCount[4]).append("）");
        if (successNum > 0) result.append(successMsg);
        if (failureNum > 0) result.append("<br/>失败 ").append(failureNum).append(" 条").append(failureMsg);
        return result.toString();
    }

    private void importPaper(MsBatchImport row, Long professorId, String operName) throws Exception {
        MsPaper p = new MsPaper();
        p.setTitle(row.getTitle()); p.setAuthorPosition(row.getAuthorPosition());
        p.setJournalName(row.getJournalName()); p.setDoi(row.getDoi()); p.setPaperUrl(row.getPaperUrl());
        p.setProfessorId(professorId);
        if (row.getDateStr() != null) p.setPublishDate(sdf.parse(row.getDateStr()));
        if (row.getActualScore() != null) p.setActualScore(row.getActualScore());
        // 等级名称查找
        if (row.getGradeName() != null && !row.getGradeName().isEmpty()) {
            MsPaperGrade g = paperGradeMapper.selectMsPaperGradeByGradeName(row.getGradeName().trim());
            if (g != null) p.setGradeId(g.getGradeId());
        }
        // 自动计算积分（如果未手动指定）
        if (p.getActualScore() == null || p.getActualScore() == 0) calculatePaperScore(p);
        p.setCreateBy(operName); p.setCreateTime(DateUtils.getNowDate()); p.setStatus("0");
        paperMapper.insertMsPaper(p);
    }

    private void importProject(MsBatchImport row, Long professorId, String operName) throws Exception {
        MsResearchProject p = new MsResearchProject();
        p.setProjectName(row.getProjectName()); p.setRole(row.getRole() != null ? row.getRole() : "主持");
        p.setApprovalNumber(row.getApprovalNumber()); p.setProjectSource(row.getProjectSource());
        p.setProfessorId(professorId);
        if (row.getFundingAmountStr() != null) p.setFundingAmount(new BigDecimal(row.getFundingAmountStr()));
        if (row.getDateStr() != null) p.setStartDate(sdf.parse(row.getDateStr()));
        if (row.getEndDateStr() != null) p.setEndDate(sdf.parse(row.getEndDateStr()));
        if (row.getActualScore() != null) p.setActualScore(row.getActualScore());
        if (row.getGradeName() != null && !row.getGradeName().isEmpty()) {
            MsProjectGrade g = projectGradeMapper.selectMsProjectGradeByGradeName(row.getGradeName().trim());
            if (g != null) p.setGradeId(g.getGradeId());
        }
        if (p.getActualScore() == null || p.getActualScore() == 0) calculateProjectScore(p);
        p.setCreateBy(operName); p.setCreateTime(DateUtils.getNowDate()); p.setStatus("0");
        projectMapper.insertMsResearchProject(p);
    }

    private void importPatent(MsBatchImport row, Long professorId, String operName) throws Exception {
        MsPatent p = new MsPatent();
        p.setPatentName(row.getPatentName()); p.setInventorRank(row.getInventorRank() != null ? row.getInventorRank() : "第一发明人");
        p.setPatentNumber(row.getPatentNumber()); p.setPatentCountry(row.getPatentCountry() != null ? row.getPatentCountry() : "中国");
        p.setProfessorId(professorId);
        if (row.getDateStr() != null) p.setApplicationDate(sdf.parse(row.getDateStr()));
        if (row.getEndDateStr() != null) p.setGrantDate(sdf.parse(row.getEndDateStr()));
        if (row.getActualScore() != null) p.setActualScore(row.getActualScore());
        if (row.getGradeName() != null && !row.getGradeName().isEmpty()) {
            MsPatentGrade g = patentGradeMapper.selectMsPatentGradeByGradeName(row.getGradeName().trim());
            if (g != null) p.setGradeId(g.getGradeId());
        }
        p.setCreateBy(operName); p.setCreateTime(DateUtils.getNowDate()); p.setStatus("0");
        patentMapper.insertMsPatent(p);
    }

    private void importBook(MsBatchImport row, Long professorId, String operName) throws Exception {
        MsBook b = new MsBook();
        b.setBookName(row.getBookName()); b.setAuthorRole(row.getAuthorRole() != null ? row.getAuthorRole() : "著");
        b.setPublisher(row.getPublisher()); b.setWordCount(row.getWordCount()); b.setIsbn(row.getIsbn());
        b.setProfessorId(professorId);
        if (row.getDateStr() != null) b.setPublishDate(sdf.parse(row.getDateStr()));
        if (row.getActualScore() != null) b.setActualScore(row.getActualScore());
        if (row.getGradeName() != null && !row.getGradeName().isEmpty()) {
            MsBookGrade g = bookGradeMapper.selectMsBookGradeByGradeName(row.getGradeName().trim());
            if (g != null) b.setGradeId(g.getGradeId());
        }
        b.setCreateBy(operName); b.setCreateTime(DateUtils.getNowDate()); b.setStatus("0");
        bookMapper.insertMsBook(b);
    }

    private void importAward(MsBatchImport row, Long professorId, String operName) throws Exception {
        MsAward a = new MsAward();
        a.setAwardName(row.getAwardName()); a.setAwardRank(row.getAwardRank() != null ? row.getAwardRank() : "第一完成人");
        a.setAwardOrg(row.getAwardOrg()); a.setCertNumber(row.getCertNumber());
        a.setProfessorId(professorId);
        if (row.getDateStr() != null) a.setAwardDate(sdf.parse(row.getDateStr()));
        if (row.getActualScore() != null) a.setActualScore(row.getActualScore());
        if (row.getGradeName() != null && !row.getGradeName().isEmpty()) {
            MsAwardGrade g = awardGradeMapper.selectMsAwardGradeByGradeName(row.getGradeName().trim());
            if (g != null) a.setGradeId(g.getGradeId());
        }
        a.setCreateBy(operName); a.setCreateTime(DateUtils.getNowDate()); a.setStatus("0");
        awardMapper.insertMsAward(a);
    }

    private void calculatePaperScore(MsPaper paper) {
        if (paper.getGradeId() != null) {
            MsPaperGrade grade = paperGradeMapper.selectMsPaperGradeByGradeId(paper.getGradeId());
            if (grade != null) {
                int base = grade.getBaseScore();
                double coeff = 1.0;
                if ("通讯作者".equals(paper.getAuthorPosition()) || "第二作者".equals(paper.getAuthorPosition())) coeff = 0.5;
                else if ("第三作者".equals(paper.getAuthorPosition())) coeff = 0.33;
                else if ("其他作者".equals(paper.getAuthorPosition())) coeff = 0.2;
                paper.setActualScore((int)(base * coeff));
            }
        }
    }

    private void calculateProjectScore(MsResearchProject project) {
        if (project.getGradeId() != null) {
            MsProjectGrade grade = projectGradeMapper.selectMsProjectGradeByGradeId(project.getGradeId());
            if (grade != null) {
                String code = grade.getGradeCode();
                int base = grade.getBaseScore();
                if ("HORIZONTAL".equals(code) && project.getFundingAmount() != null) {
                    project.setActualScore(project.getFundingAmount().multiply(new BigDecimal("0.75")).intValue());
                } else if ("SOCIAL_HORIZONTAL".equals(code) && project.getFundingAmount() != null) {
                    project.setActualScore(project.getFundingAmount().intValue());
                } else {
                    BigDecimal coeff = BigDecimal.ONE;
                    if ("参与".equals(project.getRole())) coeff = new BigDecimal("0.5");
                    project.setActualScore(new BigDecimal(base).multiply(coeff).intValue());
                }
            }
        }
    }

    private String getName(MsBatchImport row, String type) {
        switch (type) {
            case "论文": return row.getTitle() != null ? row.getTitle() : "未知";
            case "项目": return row.getProjectName() != null ? row.getProjectName() : "未知";
            case "专利": return row.getPatentName() != null ? row.getPatentName() : "未知";
            case "著作": return row.getBookName() != null ? row.getBookName() : "未知";
            case "获奖": return row.getAwardName() != null ? row.getAwardName() : "未知";
            default: return "未知";
        }
    }
}
