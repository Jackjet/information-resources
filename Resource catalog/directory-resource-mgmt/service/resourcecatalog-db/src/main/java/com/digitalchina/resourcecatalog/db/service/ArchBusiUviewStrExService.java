package com.digitalchina.resourcecatalog.db.service;

import com.digitalchina.resourcecatalog.db.domain.ArchBusiUviewStrEx;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 信息项(正式表) 服务类
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
public interface ArchBusiUviewStrExService extends IService<ArchBusiUviewStrEx> {

	IPage getList(Page page1, String pubSts, Integer uviewId);
	List<ArchBusiUviewStrEx> getListByDeptId(Integer deptId);

}
