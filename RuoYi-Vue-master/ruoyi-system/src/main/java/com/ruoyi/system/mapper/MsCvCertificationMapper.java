package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.MsCvCertification;

/**
 * CV职业资格Mapper接口
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
public interface MsCvCertificationMapper
{
    /**
     * 查询职业资格
     */
    public MsCvCertification selectMsCvCertificationByCertId(Long certId);

    /**
     * 查询职业资格列表
     */
    public List<MsCvCertification> selectMsCvCertificationList(MsCvCertification msCvCertification);

    /**
     * 新增职业资格
     */
    public int insertMsCvCertification(MsCvCertification msCvCertification);

    /**
     * 修改职业资格
     */
    public int updateMsCvCertification(MsCvCertification msCvCertification);

    /**
     * 删除职业资格
     */
    public int deleteMsCvCertificationByCertId(Long certId);

    /**
     * 批量删除职业资格
     */
    public int deleteMsCvCertificationByCertIds(Long[] certIds);
}
