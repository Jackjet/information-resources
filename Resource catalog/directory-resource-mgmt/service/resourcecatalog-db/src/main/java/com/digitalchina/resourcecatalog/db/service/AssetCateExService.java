package com.digitalchina.resourcecatalog.db.service;

import com.digitalchina.resourcecatalog.db.domain.AssetCateEx;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 信息资源目录与信息类型关联表(正式表) 服务类
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
public interface AssetCateExService extends IService<AssetCateEx> {

	IPage getList(Page page1, String type, Integer infoId, Integer typeId);

}
