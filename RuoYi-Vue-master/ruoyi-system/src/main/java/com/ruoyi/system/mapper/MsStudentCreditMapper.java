package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.MsStudentCredit;

/**
 * 学生学分记录Mapper接口
 *
 * @author CDUT-ALPHA
 * @date 2026-07-26
 */
public interface MsStudentCreditMapper
{
    /**
     * 查询学生学分记录
     */
    public MsStudentCredit selectMsStudentCreditByCreditId(Long creditId);

    /**
     * 查询学生学分记录列表
     */
    public List<MsStudentCredit> selectMsStudentCreditList(MsStudentCredit msStudentCredit);

    /**
     * 新增学生学分记录
     */
    public int insertMsStudentCredit(MsStudentCredit msStudentCredit);

    /**
     * 修改学生学分记录
     */
    public int updateMsStudentCredit(MsStudentCredit msStudentCredit);

    /**
     * 删除学生学分记录
     */
    public int deleteMsStudentCreditByCreditId(Long creditId);

    /**
     * 批量删除学生学分记录
     */
    public int deleteMsStudentCreditByCreditIds(Long[] creditIds);

    /**
     * 根据学生姓名和学期更新学分
     */
    public int updateTotalCreditsByStudent(MsStudentCredit msStudentCredit);
}
