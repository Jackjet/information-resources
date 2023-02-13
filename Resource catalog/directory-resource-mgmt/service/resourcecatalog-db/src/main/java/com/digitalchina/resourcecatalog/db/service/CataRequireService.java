package com.digitalchina.resourcecatalog.db.service;

import com.digitalchina.resourcecatalog.db.domain.CataRequire;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 信息需求目录 服务类
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
public interface CataRequireService extends IService<CataRequire> {

	String deleteByIds(Integer[] ids);

	Integer selectSequence(String string);

	IPage<CataRequire> selectPages(Page page1, String name, String code, String deptId, String nameAndCode);

}
