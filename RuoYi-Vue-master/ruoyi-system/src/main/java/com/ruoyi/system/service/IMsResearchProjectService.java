package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.MsResearchProject;

/**
 * 科研项目Service接口
 *
 * @author 王祎哲
 * @date 2026-06-12
 */
public interface IMsResearchProjectService
{
    public MsResearchProject selectMsResearchProjectByProjectId(Long projectId);

    public List<MsResearchProject> selectMsResearchProjectList(MsResearchProject msResearchProject);

    public int insertMsResearchProject(MsResearchProject msResearchProject);

    public int updateMsResearchProject(MsResearchProject msResearchProject);

    public int deleteMsResearchProjectByProjectIds(Long[] projectIds);

    public String importProject(List<MsResearchProject> list, boolean updateSupport, String operName);
}
