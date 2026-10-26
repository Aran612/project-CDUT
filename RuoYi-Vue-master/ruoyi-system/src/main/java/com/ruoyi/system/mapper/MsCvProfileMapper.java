package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.MsCvProfile;

/**
 * 教师CV档案Mapper接口
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
public interface MsCvProfileMapper
{
    /**
     * 查询CV档案
     */
    public MsCvProfile selectMsCvProfileByCvId(Long cvId);

    /**
     * 按教师ID查询CV档案
     */
    public MsCvProfile selectMsCvProfileByProfessorId(Long professorId);

    /**
     * 查询CV档案列表
     */
    public List<MsCvProfile> selectMsCvProfileList(MsCvProfile msCvProfile);

    /**
     * 新增CV档案
     */
    public int insertMsCvProfile(MsCvProfile msCvProfile);

    /**
     * 修改CV档案
     */
    public int updateMsCvProfile(MsCvProfile msCvProfile);

    /**
     * 删除CV档案
     */
    public int deleteMsCvProfileByCvId(Long cvId);

    /**
     * 批量删除CV档案
     */
    public int deleteMsCvProfileByCvIds(Long[] cvIds);

    /**
     * 按登录用户ID查找教师ID（ms_professor.user_id）
     */
    public Long selectProfessorIdByUserId(Long userId);
}
