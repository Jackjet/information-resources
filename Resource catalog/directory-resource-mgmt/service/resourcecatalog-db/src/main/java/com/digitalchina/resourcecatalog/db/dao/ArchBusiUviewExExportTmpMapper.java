package com.digitalchina.resourcecatalog.db.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalchina.resourcecatalog.db.domain.ArchBusiUviewExExportTmp;
import com.digitalchina.resourcecatalog.db.dto.CataInfoDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface ArchBusiUviewExExportTmpMapper extends BaseMapper<ArchBusiUviewExExportTmp> {

	void updateFormal();

	void updateTmp();

	void updateHistory();

}
