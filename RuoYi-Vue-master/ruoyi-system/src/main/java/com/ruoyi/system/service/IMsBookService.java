package com.ruoyi.system.service;
import java.util.List;
import com.ruoyi.system.domain.MsBook;
public interface IMsBookService {
    MsBook selectMsBookByBookId(Long id);
    List<MsBook> selectMsBookList(MsBook b);
    int insertMsBook(MsBook b);
    int updateMsBook(MsBook b);
    int deleteMsBookByBookIds(Long[] ids);

    String importBook(List<MsBook> list, boolean updateSupport, String operName);
}
