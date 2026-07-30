package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;
import com.ruoyi.system.domain.MsTeachingRecord;

/**
 * 授课记录Mapper接口
 *
 * @author CDUT-ALPHA
 * @date 2026-07-26
 */
public interface MsTeachingRecordMapper
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
     * 新增授课记录
     */
    public int insertMsTeachingRecord(MsTeachingRecord msTeachingRecord);

    /**
     * 修改授课记录
     */
    public int updateMsTeachingRecord(MsTeachingRecord msTeachingRecord);

    /**
     * 删除授课记录
     */
    public int deleteMsTeachingRecordByRecordId(Long recordId);

    /**
     * 批量删除授课记录
     */
    public int deleteMsTeachingRecordByRecordIds(Long[] recordIds);

    /**
     * 按教授统计授课积分
     */
    public List<Map<String, Object>> selectProfessorTeachingStats(String semester);

    /**
     * 按学期统计总授课积分
     */
    public List<Map<String, Object>> countTeachingScoreBySemester();

    /**
     * 按教授统计研究型课程积分
     */
    public List<Map<String, Object>> selectProfessorResearchStats(String semester);

    /**
     * 获取全体学生总学分
     */
    public Map<String, Object> selectTotalStudentCredits(String semester);
}
