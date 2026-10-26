package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.AolAssessment;

/**
 * AoL测评方式Mapper接口
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
public interface AolAssessmentMapper
{
    /**
     * 查询测评方式
     */
    public AolAssessment selectAolAssessmentByAssessId(Long assessId);

    /**
     * 查询测评方式列表（按项目查询，排序号排序）
     */
    public List<AolAssessment> selectAolAssessmentList(AolAssessment aolAssessment);

    /**
     * 新增测评方式
     */
    public int insertAolAssessment(AolAssessment aolAssessment);

    /**
     * 修改测评方式
     */
    public int updateAolAssessment(AolAssessment aolAssessment);

    /**
     * 删除测评方式
     */
    public int deleteAolAssessmentByAssessId(Long assessId);

    /**
     * 批量删除测评方式
     */
    public int deleteAolAssessmentByAssessIds(Long[] assessIds);
}
