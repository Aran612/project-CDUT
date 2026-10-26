package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.MsCvService;

/**
 * CV社会服务Mapper接口
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
public interface MsCvServiceMapper
{
    /**
     * 查询社会服务
     */
    public MsCvService selectMsCvServiceByServiceId(Long serviceId);

    /**
     * 查询社会服务列表
     */
    public List<MsCvService> selectMsCvServiceList(MsCvService msCvService);

    /**
     * 新增社会服务
     */
    public int insertMsCvService(MsCvService msCvService);

    /**
     * 修改社会服务
     */
    public int updateMsCvService(MsCvService msCvService);

    /**
     * 删除社会服务
     */
    public int deleteMsCvServiceByServiceId(Long serviceId);

    /**
     * 批量删除社会服务
     */
    public int deleteMsCvServiceByServiceIds(Long[] serviceIds);
}
