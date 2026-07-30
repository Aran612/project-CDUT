package com.ruoyi.system.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.system.domain.MsTeachingRecord;

/**
 * 授课记录Service接口
 *
 * @author CDUT-ALPHA
 * @date 2026-07-26
 */
public interface IMsTeachingRecordService
{
    /**
     * 查询授课记录
     */
    public MsTeachingRecord selectMsTeachingRecordByRecordId(Long recordId);

    /**
     * 查询授课记录列表
     */
    public List<MsTeachingRecord> selectMsTeachingRecordList(MsTeachingRecord msTeachingRecord);

    /**
     * 新增授课记录（自动计算积分）
     */
    public int insertMsTeachingRecord(MsTeachingRecord msTeachingRecord);

    /**
     * 修改授课记录（重新计算积分）
     */
    public int updateMsTeachingRecord(MsTeachingRecord msTeachingRecord);

    /**
     * 删除授课记录
     */
    public int deleteMsTeachingRecordByRecordIds(Long[] recordIds);

    /**
     * 导入授课记录数据
     */
    public String importTeachingRecord(List<MsTeachingRecord> recordList, boolean updateSupport, String operName);

    /**
     * 按教授统计授课积分
     */
    public List<Map<String, Object>> selectProfessorTeachingStats(String semester);

    /**
     * 按学期统计
     */
    public List<Map<String, Object>> countTeachingScoreBySemester();

    /**
     * 按教授统计研究型课程积分（用于评估）
     */
    public List<Map<String, Object>> selectProfessorResearchStats(String semester);

    /**
     * 获取全体学生总学分
     */
    public Map<String, Object> selectTotalStudentCredits(String semester);
}
