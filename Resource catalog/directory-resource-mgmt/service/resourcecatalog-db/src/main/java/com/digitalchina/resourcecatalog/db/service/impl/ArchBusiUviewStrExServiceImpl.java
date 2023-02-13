package com.digitalchina.resourcecatalog.db.service.impl;

import com.digitalchina.resourcecatalog.db.domain.ArchBusiUviewStrEx;
import com.digitalchina.resourcecatalog.db.dao.ArchBusiUviewStrExMapper;
import com.digitalchina.resourcecatalog.db.service.ArchBusiUviewStrExService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 信息项(正式表) 服务实现类
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
@Service
public class ArchBusiUviewStrExServiceImpl extends ServiceImpl<ArchBusiUviewStrExMapper, ArchBusiUviewStrEx> implements ArchBusiUviewStrExService {

	@Override
	public IPage getList(Page page,String pubSts, Integer uviewId) {
		return this.baseMapper.getList(page,pubSts,uviewId);
	}
	@Override
	public List<ArchBusiUviewStrEx> getListByDeptId(Integer deptId) { return this.baseMapper.getListByDeptId(deptId);}
}
