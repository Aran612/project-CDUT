package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.MsStudentCredit;

/**
 * 学生学分记录Service接口
 *
 * @author CDUT-ALPHA
 * @date 2026-07-26
 */
public interface IMsStudentCreditService
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
    public int deleteMsStudentCreditByCreditIds(Long[] creditIds);

    /**
     * 导入学生学分记录数据
     */
    public String importStudentCredit(List<MsStudentCredit> creditList, boolean updateSupport, String operName);
}
