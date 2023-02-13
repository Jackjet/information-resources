package com.digitalchina.resourcecatalog.db.service.impl;

import com.digitalchina.resourcecatalog.db.domain.CataBusInfoRel;
import com.digitalchina.resourcecatalog.db.domain.CataRequire;
import com.digitalchina.resourcecatalog.db.dao.CataRequireMapper;
import com.digitalchina.resourcecatalog.db.service.CataBusInfoRelService;
import com.digitalchina.resourcecatalog.db.service.CataRequireService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 信息需求目录 服务实现类
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
@Service
public class CataRequireServiceImpl extends ServiceImpl<CataRequireMapper, CataRequire> implements CataRequireService {

	@Autowired
	private CataBusInfoRelService cataBusInfoRelService;
	
	@Override
	@Transactional
	public String deleteByIds(Integer[] ids) {
		List<CataRequire> list = this.list(new QueryWrapper<CataRequire>().in(CataRequire.ID, ids));
		if (list != null && list.size() > 0) {
			String log = "";
			for (CataRequire cataRequire : list) {
				cataRequire.setDeleted(1);
				log+=cataRequire.getName()+"/"+cataRequire.getCode()+"、";
			}
			if (this.updateBatchById(list)) {
				if (cataBusInfoRelService.remove(new QueryWrapper<CataBusInfoRel>().in(CataBusInfoRel.INFO_ID, ids)
						.eq(CataBusInfoRel.TYPE, 1))) {
					log = log.substring(0, log.length() - 1);
					return log;
				}
			}
		}
		return null;
	}

	@Override
	public Integer selectSequence(String seq) {
		return this.baseMapper.selectSequence(seq);
	}

	@Override
	public IPage<CataRequire> selectPages(Page page, String name, String code, String deptId, String nameAndCode) {
		return this.baseMapper.selectPages(page,name,code,deptId,nameAndCode);
	}

}
