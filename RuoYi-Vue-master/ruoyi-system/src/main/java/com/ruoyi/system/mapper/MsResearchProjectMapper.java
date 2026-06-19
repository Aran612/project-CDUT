package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;
import com.ruoyi.system.domain.MsResearchProject;

/**
 * 科研项目Mapper接口
 *
 * @author 王祎哲
 * @date 2026-06-12
 */
public interface MsResearchProjectMapper
{
    public MsResearchProject selectMsResearchProjectByProjectId(Long projectId);

    public List<MsResearchProject> selectMsResearchProjectList(MsResearchProject msResearchProject);

    public int insertMsResearchProject(MsResearchProject msResearchProject);

    public int updateMsResearchProject(MsResearchProject msResearchProject);

    public int deleteMsResearchProjectByProjectId(Long projectId);

    public int deleteMsResearchProjectByProjectIds(Long[] projectIds);
    public int deleteMsResearchProjectByProfessorId(Long professorId);

    /**
     * 统计项目数量和总积分（支持年份筛选）
     */
    public Map<String, Object> countProjectStats(String year);
}
