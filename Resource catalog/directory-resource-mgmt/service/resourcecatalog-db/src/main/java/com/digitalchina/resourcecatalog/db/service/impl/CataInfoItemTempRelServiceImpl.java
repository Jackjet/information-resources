package com.digitalchina.resourcecatalog.db.service.impl;

import com.digitalchina.resourcecatalog.db.domain.CataInfoItemTempRel;
import com.digitalchina.resourcecatalog.db.dao.CataInfoItemTempRelMapper;
import com.digitalchina.resourcecatalog.db.service.CataInfoItemTempRelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 信息项(暂存表) 服务实现类
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
@Service
public class CataInfoItemTempRelServiceImpl extends ServiceImpl<CataInfoItemTempRelMapper, CataInfoItemTempRel> implements CataInfoItemTempRelService {

	@Override
	public void saveInfo(CataInfoItemTempRel cata) {
		this.baseMapper.insert(cata);
	}

	@Override
	public void updateInfo(CataInfoItemTempRel cata) {
		this.baseMapper.updateById(cata);
	}

}
