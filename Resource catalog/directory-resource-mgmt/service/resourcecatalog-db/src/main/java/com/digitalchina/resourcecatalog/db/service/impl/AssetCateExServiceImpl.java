package com.digitalchina.resourcecatalog.db.service.impl;

import com.digitalchina.resourcecatalog.db.domain.AssetCateEx;
import com.digitalchina.resourcecatalog.db.dao.AssetCateExMapper;
import com.digitalchina.resourcecatalog.db.service.AssetCateExService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 信息资源目录与信息类型关联表(正式表) 服务实现类
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
@Service
public class AssetCateExServiceImpl extends ServiceImpl<AssetCateExMapper, AssetCateEx> implements AssetCateExService {

	@Override
	public IPage getList(Page page1, String type, Integer infoId, Integer typeId) {
		return this.baseMapper.getList(page1,type,infoId,typeId);
	}

}
