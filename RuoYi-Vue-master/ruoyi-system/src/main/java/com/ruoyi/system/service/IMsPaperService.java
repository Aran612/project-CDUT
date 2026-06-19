package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.MsPaper;

/**
 * 论文Service接口
 *
 * @author 王祎哲
 * @date 2026-06-12
 */
public interface IMsPaperService
{
    /**
     * 查询论文
     */
    public MsPaper selectMsPaperByPaperId(Long paperId);

    /**
     * 查询论文列表
     */
    public List<MsPaper> selectMsPaperList(MsPaper msPaper);

    /**
     * 新增论文
     */
    public int insertMsPaper(MsPaper msPaper);

    /**
     * 修改论文
     */
    public int updateMsPaper(MsPaper msPaper);

    /**
     * 删除论文
     */
    public int deleteMsPaperByPaperIds(Long[] paperIds);

    /**
     * 导入论文数据
     *
     * @param paperList 论文数据列表
     * @param updateSupport 是否更新已存在数据
     * @param operName 操作人
     * @return 结果消息
     */
    public String importPaper(List<MsPaper> paperList, boolean updateSupport, String operName);
}
