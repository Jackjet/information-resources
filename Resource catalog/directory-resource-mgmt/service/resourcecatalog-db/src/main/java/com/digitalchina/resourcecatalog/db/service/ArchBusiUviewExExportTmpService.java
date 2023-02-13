package com.digitalchina.resourcecatalog.db.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.digitalchina.resourcecatalog.db.domain.*;
import com.digitalchina.resourcecatalog.db.dto.CataInfoDto;

import java.util.List;
import java.util.Map;

public interface ArchBusiUviewExExportTmpService extends IService<ArchBusiUviewExExportTmp> {

    void updateFormal();

    void updateTmp();

    void updateHistory();
}
