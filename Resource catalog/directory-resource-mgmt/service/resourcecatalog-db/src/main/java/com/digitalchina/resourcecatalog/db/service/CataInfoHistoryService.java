package com.digitalchina.resourcecatalog.db.service;

import com.digitalchina.resourcecatalog.db.domain.CataInfoHistory;
import com.digitalchina.resourcecatalog.db.domain.CataInfoHistoryTypeRel;
import com.digitalchina.resourcecatalog.db.domain.CataInfoItemHistoryRel;
import com.digitalchina.resourcecatalog.db.dto.CataInfoCompareDto;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 信息资源目录历史信息记录 服务类
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
public interface CataInfoHistoryService extends IService<CataInfoHistory> {

	CataInfoHistory rollback(Integer id,String userName);

	CataInfoCompareDto compare(Integer id);

	public boolean saveCataInfoHistory(CataInfoHistory cataInfoHistory, List<CataInfoHistoryTypeRel> cataInfoHistoryTypeRelList,
								List<CataInfoItemHistoryRel> cataInfoItemHistoryRelList,String userName);

	public boolean updateAllStatus(Integer infoId);
}
