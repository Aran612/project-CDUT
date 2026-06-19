package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;
import com.ruoyi.system.domain.MsProfessor;

/**
 * 教授信息Mapper接口
 *
 * @author 王祎哲
 * @date 2026-06-12
 */
public interface MsProfessorMapper
{
    /**
     * 查询教授信息
     */
    public MsProfessor selectMsProfessorByProfessorId(Long professorId);

    /**
     * 查询教授信息列表
     */
    public List<MsProfessor> selectMsProfessorList(MsProfessor msProfessor);

    /**
     * 新增教授信息
     */
    public int insertMsProfessor(MsProfessor msProfessor);

    /**
     * 修改教授信息
     */
    public int updateMsProfessor(MsProfessor msProfessor);

    /**
     * 删除教授信息
     */
    public int deleteMsProfessorByProfessorId(Long professorId);

    /**
     * 批量删除教授信息
     */
    public int deleteMsProfessorByProfessorIds(Long[] professorIds);

    /**
     * 更新教授累计积分
     */
    public int updateMsProfessorTotalScore(Long professorId);

    /**
     * 查询全部正常状态教授（下拉选择用）
     */
    public List<MsProfessor> selectMsProfessorAll();

    /**
     * 按姓名精确查找教授
     */
    public MsProfessor selectMsProfessorByProfessorName(String name);

    /**
     * 教授综合成果统计（跨五表联合，支持年份筛选）
     */
    public List<Map<String, Object>> selectComprehensiveProfessorStats(String year);
}
