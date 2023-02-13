package com.digitalchina.resourcecatalog.db.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digitalchina.resourcecatalog.db.dao.ArchBusiUviewExExportTmpMapper;
import com.digitalchina.resourcecatalog.db.domain.ArchBusiUviewExExportTmp;
import com.digitalchina.resourcecatalog.db.service.ArchBusiUviewExExportTmpService;
import org.springframework.stereotype.Service;


@Service
public class ArchBusiUviewExExportTmpServiceImpl extends ServiceImpl<ArchBusiUviewExExportTmpMapper, ArchBusiUviewExExportTmp>
		implements ArchBusiUviewExExportTmpService {

	@Override
	public void updateFormal() {
		this.baseMapper.updateFormal();
	}

	@Override
	public void updateTmp() {
		this.baseMapper.updateTmp();
	}

	@Override
	public void updateHistory() {
		this.baseMapper.updateHistory();
	}
}
