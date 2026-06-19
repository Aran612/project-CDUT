package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.MsProfessor;

/**
 * 教授信息Service接口
 *
 * @author 王祎哲
 * @date 2026-06-12
 */
public interface IMsProfessorService
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
    public int deleteMsProfessorByProfessorIds(Long[] professorIds);

    /**
     * 查询全部正常状态教授（下拉选择用）
     */
    public List<MsProfessor> selectMsProfessorAll();

    /**
     * 按姓名精确查找教授
     */
    public MsProfessor selectMsProfessorByProfessorName(String name);

    /**
     * 导入教授数据
     */
    public String importProfessor(List<MsProfessor> list, boolean updateSupport, String operName);
}
