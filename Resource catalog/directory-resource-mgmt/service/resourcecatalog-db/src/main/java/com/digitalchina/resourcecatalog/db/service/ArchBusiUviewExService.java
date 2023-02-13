package com.digitalchina.resourcecatalog.db.service;

import com.digitalchina.resourcecatalog.db.domain.ArchBusiUviewEx;
import com.baomidou.mybatisplus.extension.service.IService;
import com.digitalchina.resourcecatalog.db.domain.ArchBusiUviewStrEx;
import com.digitalchina.resourcecatalog.db.domain.AssetCateEx;
import com.digitalchina.resourcecatalog.db.domain.CataInfoItemTempRel;
import com.digitalchina.resourcecatalog.db.dto.CataInfoDto;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 信息资源目录(正式表) 服务类
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
public interface ArchBusiUviewExService extends IService<ArchBusiUviewEx> {
    public boolean saveArchBuisUviewEx(ArchBusiUviewEx archBusiUviewEx,
                                       List<ArchBusiUviewStrEx> archBusiUviewStrExList,
                                       List<AssetCateEx> assetCateExList,
                                       String userName, 
                                       List<CataInfoItemTempRel> cataInfoItemTempRelList);

    List<Map<String, Object>> getNewTop5Info(List<Integer> deptIds);

	public List<CataInfoDto> selectByTypeId(Integer depId, Integer typId);

	public List<Map<String, Object>> selectForType(Integer depId);
	

}
