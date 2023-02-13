package com.digitalchina.resourcecatalog.db.service.impl;
import com.digitalchina.resourcecatalog.db.domain.ArchBusi;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalchina.resourcecatalog.db.domain.CataBusInfoRel;
import com.digitalchina.resourcecatalog.db.domain.CataRequire;
import com.digitalchina.resourcecatalog.db.dao.CataBusInfoRelMapper;
import com.digitalchina.resourcecatalog.db.service.ArchBusiService;
import com.digitalchina.resourcecatalog.db.service.CataBusInfoRelService;
import com.digitalchina.resourcecatalog.db.service.CataRequireService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 权责清单与信息资源关联表 服务实现类
 * </p>
 *
 * @author wangmh
 * @since 2020-05-09
 */
@Service
public class CataBusInfoRelServiceImpl extends ServiceImpl<CataBusInfoRelMapper, CataBusInfoRel> implements CataBusInfoRelService {

	@Autowired
	private CataRequireService cataRequireService;
	@Autowired
	private ArchBusiService archBusiService;
	
    @Override
    @Transactional
    public boolean relCataInfo(CataBusInfoRel cataBusInfoRel) {
        for (Integer infoId : cataBusInfoRel.getInfoIds()) {
            cataBusInfoRel.setInfoId(infoId);
            this.baseMapper.insert(cataBusInfoRel);
        }
        return true;
    }

    @Override
    public IPage<List<Map<String, Object>>> cataInfoPage(Page page, Integer busiId, Integer type) {
        return this.baseMapper.cataInfoPage(page, busiId, type);
    }
	
	@Override
	@Transactional
	public String saveInfo(Integer[] ids, Integer infoId, Integer type) {
		QueryWrapper<CataBusInfoRel> qw = new QueryWrapper<CataBusInfoRel>();
		if (ids != null && ids.length > 0) {
			qw.in(CataBusInfoRel.BUS_ID, ids);
			qw.eq(CataBusInfoRel.TYPE, type);
			qw.eq(CataBusInfoRel.INFO_ID, infoId);
			if (this.count(qw) > 0) {
				this.remove(qw);
			}
			List<CataBusInfoRel> list=new LinkedList<CataBusInfoRel>();
			CataRequire cataRequire = cataRequireService.getById(infoId);
			String log="【"+cataRequire.getName()+"/"+cataRequire.getCode()+"】【";
			List<ArchBusi> aList=archBusiService.list(new QueryWrapper<ArchBusi>().in(ArchBusi.BUSI_ID, ids));
			if(aList!=null&&aList.size()>0){
				for (ArchBusi archBusi : aList) {
					CataBusInfoRel cataBusInfoRel=new CataBusInfoRel();
					cataBusInfoRel.setBusId(archBusi.getBusiId());
					cataBusInfoRel.setInfoId(infoId);
					cataBusInfoRel.setType(type);
					list.add(cataBusInfoRel);
					log+=archBusi.getBusiNo()+"、";
				}
				this.saveBatch(list);
				log=log.substring(0,log.length()-1)+"】";
				return log;
			}
		}
		return null;
	}

	@Override
	public IPage<List<ArchBusi>> selectPages(Page page, String busiNoAndBusiNm, String busiNo, String busiNm, Integer infoId,
			Integer type) {
		return this.baseMapper.selectPages(page, busiNoAndBusiNm,busiNo,busiNm,infoId,type);
	}

	@Override
	public IPage<List<ArchBusi>> selectBusPage(Page page, String busiNoAndBusiNm, String busiNo, String busiNm,
			Integer infoId, Integer type,Integer deptId) {
		return this.baseMapper.selectBusPage(page, busiNoAndBusiNm,busiNo,busiNm,infoId,type,deptId);
	}
}
