package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.AolMapCell;

/**
 * AoL覆盖矩阵明细Mapper接口
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
public interface AolMapCellMapper
{
    /**
     * 查询图谱课程的覆盖格明细
     */
    public List<AolMapCell> selectAolMapCellsByMapCourseId(Long mapCourseId);

    /**
     * 新增覆盖格
     */
    public int insertAolMapCell(AolMapCell aolMapCell);

    /**
     * 批量新增覆盖格
     */
    public int batchInsertAolMapCells(List<AolMapCell> cells);

    /**
     * 删除图谱课程的全部覆盖格
     */
    public int deleteAolMapCellsByMapCourseId(Long mapCourseId);

    /**
     * 批量删除图谱课程的全部覆盖格
     */
    public int deleteAolMapCellsByMapCourseIds(Long[] mapCourseIds);
}
