package com.ruoyi.system.service.impl;

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
import com.ruoyi.system.mapper.MsCourseMapper;
import com.ruoyi.system.service.IMsCourseService;

/**
 * 课程Service业务层处理
 *
 * @author CDUT-ALPHA
 * @date 2026-07-26
 */
@Service
public class MsCourseServiceImpl implements IMsCourseService
{
    private static final Logger log = LoggerFactory.getLogger(MsCourseServiceImpl.class);

    @Autowired
    private MsCourseMapper msCourseMapper;

    @Autowired
    protected Validator validator;

    @Override
    public MsCourse selectMsCourseByCourseId(Long courseId)
    {
        return msCourseMapper.selectMsCourseByCourseId(courseId);
    }

    @Override
    public List<MsCourse> selectMsCourseList(MsCourse msCourse)
    {
        return msCourseMapper.selectMsCourseList(msCourse);
    }

    @Override
    public List<MsCourse> selectAllMsCourse()
    {
        return msCourseMapper.selectAllMsCourse();
    }

    @Override
    @Transactional
    public int insertMsCourse(MsCourse msCourse)
    {
        msCourse.setCreateTime(DateUtils.getNowDate());
        return msCourseMapper.insertMsCourse(msCourse);
    }

    @Override
    @Transactional
    public int updateMsCourse(MsCourse msCourse)
    {
        msCourse.setUpdateTime(DateUtils.getNowDate());
        return msCourseMapper.updateMsCourse(msCourse);
    }

    @Override
    @Transactional
    public int deleteMsCourseByCourseIds(Long[] courseIds)
    {
        return msCourseMapper.deleteMsCourseByCourseIds(courseIds);
    }

    @Override
    @Transactional
    public String importCourse(List<MsCourse> courseList, boolean updateSupport, String operName)
    {
        if (courseList == null || courseList.isEmpty())
        {
            throw new ServiceException("导入课程数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (MsCourse course : courseList)
        {
            try
            {
                BeanValidators.validateWithException(validator, course);

                course.setCreateBy(operName);
                course.setCreateTime(DateUtils.getNowDate());

                MsCourse existCourse = null;
                if (course.getCourseId() != null)
                {
                    existCourse = msCourseMapper.selectMsCourseByCourseId(course.getCourseId());
                }

                if (existCourse != null && updateSupport)
                {
                    course.setUpdateBy(operName);
                    course.setUpdateTime(DateUtils.getNowDate());
                    msCourseMapper.updateMsCourse(course);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、课程 \"").append(course.getCourseName()).append("\" 更新成功");
                }
                else
                {
                    msCourseMapper.insertMsCourse(course);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、课程 \"").append(course.getCourseName()).append("\" 导入成功");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String name = course.getCourseName() != null ? course.getCourseName() : "未知";
                failureMsg.append("<br/>").append(failureNum).append("、课程 \"").append(name).append("\" 导入失败：");
                failureMsg.append(e.getMessage());
                log.error("导入课程失败：{}", e.getMessage());
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
    public List<Map<String, Object>> countCourseByType()
    {
        return msCourseMapper.countCourseByType();
    }
}
