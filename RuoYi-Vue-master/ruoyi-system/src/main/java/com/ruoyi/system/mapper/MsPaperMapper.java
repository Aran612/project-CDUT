package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;
import com.ruoyi.system.domain.MsPaper;

/**
 * 论文Mapper接口
 *
 * @author 王祎哲
 * @date 2026-06-12
 */
public interface MsPaperMapper
{
    /**
     * 查询论文
     */
    public MsPaper selectMsPaperByPaperId(Long paperId);

    /**
     * 查询论文列表
     */
    public List<MsPaper> selectMsPaperList(MsPaper msPaper);

    /**
     * 新增论文
     */
    public int insertMsPaper(MsPaper msPaper);

    /**
     * 修改论文
     */
    public int updateMsPaper(MsPaper msPaper);

    /**
     * 删除论文
     */
    public int deleteMsPaperByPaperId(Long paperId);

    /**
     * 批量删除论文
     */
    public int deleteMsPaperByPaperIds(Long[] paperIds);

    /**
     * 按教授ID清除所有论文（测试用）
     */
    public int deleteMsPaperByProfessorId(Long professorId);

    /**
     * 获取论文等级分布统计（支持年份筛选）
     */
    public List<Map<String, Object>> countPaperByGrade(String year);

    /**
     * 获取教授论文统计（按年份筛选）
     */
    public List<Map<String, Object>> selectProfessorPaperStats(String year);

    /**
     * 按考核/奖励类型分离统计（支持年份筛选）
     */
    public List<Map<String, Object>> countPaperByEvaluationType(String year);
}
