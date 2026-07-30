package com.ruoyi.system.service.impl;

import java.util.List;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.system.domain.MsStudentCredit;
import com.ruoyi.system.mapper.MsStudentCreditMapper;
import com.ruoyi.system.service.IMsStudentCreditService;

/**
 * 学生学分记录Service业务层处理
 *
 * @author CDUT-ALPHA
 * @date 2026-07-26
 */
@Service
public class MsStudentCreditServiceImpl implements IMsStudentCreditService
{
    private static final Logger log = LoggerFactory.getLogger(MsStudentCreditServiceImpl.class);

    @Autowired
    private MsStudentCreditMapper msStudentCreditMapper;

    @Autowired
    protected Validator validator;

    @Override
    public MsStudentCredit selectMsStudentCreditByCreditId(Long creditId)
    {
        return msStudentCreditMapper.selectMsStudentCreditByCreditId(creditId);
    }

    @Override
    public List<MsStudentCredit> selectMsStudentCreditList(MsStudentCredit msStudentCredit)
    {
        return msStudentCreditMapper.selectMsStudentCreditList(msStudentCredit);
    }

    @Override
    @Transactional
    public int insertMsStudentCredit(MsStudentCredit msStudentCredit)
    {
        msStudentCredit.setCreateTime(DateUtils.getNowDate());
        return msStudentCreditMapper.insertMsStudentCredit(msStudentCredit);
    }

    @Override
    @Transactional
    public int updateMsStudentCredit(MsStudentCredit msStudentCredit)
    {
        msStudentCredit.setUpdateTime(DateUtils.getNowDate());
        return msStudentCreditMapper.updateMsStudentCredit(msStudentCredit);
    }

    @Override
    @Transactional
    public int deleteMsStudentCreditByCreditIds(Long[] creditIds)
    {
        return msStudentCreditMapper.deleteMsStudentCreditByCreditIds(creditIds);
    }

    @Override
    @Transactional
    public String importStudentCredit(List<MsStudentCredit> creditList, boolean updateSupport, String operName)
    {
        if (creditList == null || creditList.isEmpty())
        {
            throw new ServiceException("导入学生学分数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (MsStudentCredit credit : creditList)
        {
            try
            {
                BeanValidators.validateWithException(validator, credit);

                credit.setCreateBy(operName);
                credit.setCreateTime(DateUtils.getNowDate());

                MsStudentCredit existCredit = null;
                if (credit.getCreditId() != null)
                {
                    existCredit = msStudentCreditMapper.selectMsStudentCreditByCreditId(credit.getCreditId());
                }

                if (existCredit != null && updateSupport)
                {
                    credit.setUpdateBy(operName);
                    credit.setUpdateTime(DateUtils.getNowDate());
                    msStudentCreditMapper.updateMsStudentCredit(credit);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、学生 \"").append(credit.getStudentName())
                            .append("\" 学分记录更新成功");
                }
                else
                {
                    msStudentCreditMapper.insertMsStudentCredit(credit);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、学生 \"").append(credit.getStudentName())
                            .append("\" 学分记录导入成功");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String name = credit.getStudentName() != null ? credit.getStudentName() : "未知";
                failureMsg.append("<br/>").append(failureNum).append("、学生 \"").append(name).append("\" 导入失败：");
                failureMsg.append(e.getMessage());
                log.error("导入学生学分数据失败：{}", e.getMessage());
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
}
