package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.system.domain.MsCourse;
import com.ruoyi.system.domain.MsProfessor;
import com.ruoyi.system.domain.MsTeachingRecord;
import com.ruoyi.system.mapper.MsCourseMapper;
import com.ruoyi.system.mapper.MsProfessorMapper;
import com.ruoyi.system.mapper.MsTeachingRecordMapper;
import com.ruoyi.system.service.IMsTeachingRecordService;

/**
 * 授课记录Service业务层处理
 * 核心逻辑：计算积分 = 课程学分 × 选课学生数量
 *
 * @author CDUT-ALPHA
 * @date 2026-07-26
 */
@Service
public class MsTeachingRecordServiceImpl implements IMsTeachingRecordService
{
    private static final Logger log = LoggerFactory.getLogger(MsTeachingRecordServiceImpl.class);

    @Autowired
    private MsTeachingRecordMapper msTeachingRecordMapper;

    @Autowired
    private MsCourseMapper msCourseMapper;

    @Autowired
    private MsProfessorMapper msProfessorMapper;

    @Autowired
    protected Validator validator;

    @Override
    public MsTeachingRecord selectMsTeachingRecordByRecordId(Long recordId)
    {
        return msTeachingRecordMapper.selectMsTeachingRecordByRecordId(recordId);
    }

    @Override
    public List<MsTeachingRecord> selectMsTeachingRecordList(MsTeachingRecord msTeachingRecord)
    {
        return msTeachingRecordMapper.selectMsTeachingRecordList(msTeachingRecord);
    }

    @Override
    @Transactional
    public int insertMsTeachingRecord(MsTeachingRecord msTeachingRecord)
    {
        msTeachingRecord.setCreateTime(DateUtils.getNowDate());
        // 自动计算积分
        calculateScore(msTeachingRecord);
        // 填充冗余字段
        fillRedundantFields(msTeachingRecord);
        return msTeachingRecordMapper.insertMsTeachingRecord(msTeachingRecord);
    }

    @Override
    @Transactional
    public int updateMsTeachingRecord(MsTeachingRecord msTeachingRecord)
    {
        msTeachingRecord.setUpdateTime(DateUtils.getNowDate());
        // 重新计算积分
        calculateScore(msTeachingRecord);
        fillRedundantFields(msTeachingRecord);
        return msTeachingRecordMapper.updateMsTeachingRecord(msTeachingRecord);
    }

    @Override
    @Transactional
    public int deleteMsTeachingRecordByRecordIds(Long[] recordIds)
    {
        return msTeachingRecordMapper.deleteMsTeachingRecordByRecordIds(recordIds);
    }

    @Override
    @Transactional
    public String importTeachingRecord(List<MsTeachingRecord> recordList, boolean updateSupport, String operName)
    {
        if (recordList == null || recordList.isEmpty())
        {
            throw new ServiceException("导入授课记录数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (MsTeachingRecord record : recordList)
        {
            try
            {
                BeanValidators.validateWithException(validator, record);

                // 填充冗余字段并计算积分
                fillRedundantFields(record);
                calculateScore(record);

                record.setCreateBy(operName);
                record.setCreateTime(DateUtils.getNowDate());

                MsTeachingRecord existRecord = null;
                if (record.getRecordId() != null)
                {
                    existRecord = msTeachingRecordMapper.selectMsTeachingRecordByRecordId(record.getRecordId());
                }

                if (existRecord != null && updateSupport)
                {
                    record.setUpdateBy(operName);
                    record.setUpdateTime(DateUtils.getNowDate());
                    msTeachingRecordMapper.updateMsTeachingRecord(record);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、授课记录 \"").append(record.getCourseName())
                            .append(" - ").append(record.getProfessorName()).append("\" 更新成功");
                }
                else
                {
                    msTeachingRecordMapper.insertMsTeachingRecord(record);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、授课记录 \"").append(record.getCourseName())
                            .append(" - ").append(record.getProfessorName()).append("\" 导入成功");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String courseName = record.getCourseName() != null ? record.getCourseName() : "未知";
                failureMsg.append("<br/>").append(failureNum).append("、授课记录 \"").append(courseName).append("\" 导入失败：");
                failureMsg.append(e.getMessage());
                log.error("导入授课记录失败：{}", e.getMessage());
            }
        }

        if (failureNum > 0)
        {
            failureMsg.insert(0, "导入失败共 " + failureNum + " 条，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "导入成功共 " + successNum + " 条，数据已处理。");
        }

        return successMsg.toString();
    }

    @Override
    public List<Map<String, Object>> selectProfessorTeachingStats(String semester)
    {
        return msTeachingRecordMapper.selectProfessorTeachingStats(semester);
    }

    @Override
    public List<Map<String, Object>> countTeachingScoreBySemester()
    {
        return msTeachingRecordMapper.countTeachingScoreBySemester();
    }

    @Override
    public List<Map<String, Object>> selectProfessorResearchStats(String semester)
    {
        return msTeachingRecordMapper.selectProfessorResearchStats(semester);
    }

    @Override
    public Map<String, Object> selectTotalStudentCredits(String semester)
    {
        return msTeachingRecordMapper.selectTotalStudentCredits(semester);
    }

    /**
     * 计算积分：课程学分 × 选课学生数量
     */
    private void calculateScore(MsTeachingRecord record)
    {
        if (record.getCreditPerCourse() != null && record.getStudentCount() != null)
        {
            BigDecimal score = record.getCreditPerCourse().multiply(new BigDecimal(record.getStudentCount()));
            record.setCalculatedScore(score);
        }
        // 如果有课程ID但没有显式设置学分，则从课程表获取
        if (record.getCourseId() != null && record.getCreditPerCourse() == null)
        {
            MsCourse course = msCourseMapper.selectMsCourseByCourseId(record.getCourseId());
            if (course != null && course.getCreditValue() != null && record.getStudentCount() != null)
            {
                record.setCreditPerCourse(course.getCreditValue());
                BigDecimal score = course.getCreditValue().multiply(new BigDecimal(record.getStudentCount()));
                record.setCalculatedScore(score);
            }
        }
    }

    /**
     * 填充冗余字段（课程名称、教授姓名等）
     */
    private void fillRedundantFields(MsTeachingRecord record)
    {
        // 填充课程名称
        if (record.getCourseId() != null && (record.getCourseName() == null || record.getCourseName().isEmpty()))
        {
            MsCourse course = msCourseMapper.selectMsCourseByCourseId(record.getCourseId());
            if (course != null)
            {
                record.setCourseName(course.getCourseName());
                if (record.getCreditPerCourse() == null)
                {
                    record.setCreditPerCourse(course.getCreditValue());
                }
            }
        }

        // 填充教授姓名
        if (record.getProfessorId() != null && (record.getProfessorName() == null || record.getProfessorName().isEmpty()))
        {
            MsProfessor professor = msProfessorMapper.selectMsProfessorByProfessorId(record.getProfessorId());
            if (professor != null)
            {
                record.setProfessorName(professor.getName());
            }
        }
    }
}
