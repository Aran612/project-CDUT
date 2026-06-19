package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.MsBatchImport;

public interface IMsBatchImportService {
    String batchImport(List<MsBatchImport> list, Long professorId, String operName);
}
