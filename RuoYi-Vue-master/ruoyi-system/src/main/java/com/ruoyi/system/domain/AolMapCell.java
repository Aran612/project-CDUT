package com.ruoyi.system.domain;

import java.io.Serializable;

/**
 * AoL覆盖矩阵明细对象 aol_map_cell
 *
 * @author CDUT-ALPHA
 * @date 2026-10-26
 */
public class AolMapCell implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 覆盖格ID */
    private Long cellId;

    /** 图谱课程ID */
    private Long mapCourseId;

    /** 学习目标ID */
    private Long lgId;

    /** 覆盖层级（I=引入 D=发展 M=精通） */
    private String coverageLevel;

    public void setCellId(Long cellId)
    {
        this.cellId = cellId;
    }

    public Long getCellId()
    {
        return cellId;
    }

    public void setMapCourseId(Long mapCourseId)
    {
        this.mapCourseId = mapCourseId;
    }

    public Long getMapCourseId()
    {
        return mapCourseId;
    }

    public void setLgId(Long lgId)
    {
        this.lgId = lgId;
    }

    public Long getLgId()
    {
        return lgId;
    }

    public void setCoverageLevel(String coverageLevel)
    {
        this.coverageLevel = coverageLevel;
    }

    public String getCoverageLevel()
    {
        return coverageLevel;
    }
}
