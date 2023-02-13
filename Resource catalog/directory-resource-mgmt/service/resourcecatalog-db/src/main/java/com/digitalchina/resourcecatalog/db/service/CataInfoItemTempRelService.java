package com.digitalchina.resourcecatalog.db.service;

import com.digitalchina.resourcecatalog.db.domain.CataInfoItemTempRel;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 信息项(暂存表) 服务类
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
public interface CataInfoItemTempRelService extends IService<CataInfoItemTempRel> {

	void saveInfo(CataInfoItemTempRel cata);

	void updateInfo(CataInfoItemTempRel cata);

}
