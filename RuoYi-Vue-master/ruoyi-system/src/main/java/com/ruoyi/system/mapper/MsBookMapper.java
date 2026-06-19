package com.ruoyi.system.mapper;
import java.util.List;
import com.ruoyi.system.domain.MsBook;
public interface MsBookMapper {
    MsBook selectMsBookByBookId(Long id);
    List<MsBook> selectMsBookList(MsBook b);
    int insertMsBook(MsBook b);
    int updateMsBook(MsBook b);
    int deleteMsBookByBookId(Long id);
    int deleteMsBookByBookIds(Long[] ids);
    int deleteMsBookByProfessorId(Long professorId);
}
