package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.MsPaperGrade;

/**
 * 论文等级Service接口
 *
 * @author 王祎哲
 * @date 2026-06-12
 */
public interface IMsPaperGradeService
{
    /**
     * 查询论文等级
     */
    public MsPaperGrade selectMsPaperGradeByGradeId(Long gradeId);

    /**
     * 根据名称查询论文等级
     */
    public MsPaperGrade selectMsPaperGradeByGradeName(String gradeName);

    /**
     * 查询论文等级列表
     */
    public List<MsPaperGrade> selectMsPaperGradeList(MsPaperGrade msPaperGrade);

    /**
     * 查询所有正常状态的论文等级
     */
    public List<MsPaperGrade> selectMsPaperGradeAll();

    /**
     * 新增论文等级
     */
    public int insertMsPaperGrade(MsPaperGrade msPaperGrade);

    /**
     * 修改论文等级
     */
    public int updateMsPaperGrade(MsPaperGrade msPaperGrade);

    /**
     * 删除论文等级
     */
    public int deleteMsPaperGradeByGradeIds(Long[] gradeIds);
}
