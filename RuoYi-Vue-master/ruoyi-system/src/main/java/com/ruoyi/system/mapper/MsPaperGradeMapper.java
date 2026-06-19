package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.MsPaperGrade;

/**
 * 论文等级Mapper接口
 *
 * @author 王祎哲
 * @date 2026-06-12
 */
public interface MsPaperGradeMapper
{
    /**
     * 查询论文等级
     */
    public MsPaperGrade selectMsPaperGradeByGradeId(Long gradeId);

    /**
     * 根据编码查询论文等级
     */
    public MsPaperGrade selectMsPaperGradeByGradeCode(String gradeCode);

    /**
     * 根据名称查询论文等级（用于导入匹配）
     */
    public MsPaperGrade selectMsPaperGradeByGradeName(String gradeName);

    /**
     * 查询论文等级列表
     */
    public List<MsPaperGrade> selectMsPaperGradeList(MsPaperGrade msPaperGrade);

    /**
     * 查询所有正常状态的论文等级（按排序）
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
    public int deleteMsPaperGradeByGradeId(Long gradeId);

    /**
     * 批量删除论文等级
     */
    public int deleteMsPaperGradeByGradeIds(Long[] gradeIds);
}
