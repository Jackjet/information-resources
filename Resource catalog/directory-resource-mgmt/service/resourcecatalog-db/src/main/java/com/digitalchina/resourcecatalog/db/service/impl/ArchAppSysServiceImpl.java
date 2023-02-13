package com.digitalchina.resourcecatalog.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.digitalchina.resourcecatalog.db.domain.ArchAppSys;
import com.digitalchina.resourcecatalog.db.dao.ArchAppSysMapper;
import com.digitalchina.resourcecatalog.db.domain.ArchBusi;
import com.digitalchina.resourcecatalog.db.domain.CataAppBusRel;
import com.digitalchina.resourcecatalog.db.service.ArchAppSysService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digitalchina.resourcecatalog.db.service.CataAppBusRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 应用系统信息 服务实现类
 * </p>
 *
 * @author wangmh
 * @since 2020-05-09
 */
@Service
public class ArchAppSysServiceImpl extends ServiceImpl<ArchAppSysMapper, ArchAppSys> implements ArchAppSysService {

    @Autowired
    CataAppBusRelService cataAppBusRelService;

    @Override
    @Transactional
    public boolean saveOrUpdateInfo(ArchAppSys archAppSys, List<Integer> busiIds, String username) {
        boolean type = false;
        if (archAppSys.getAppsysId() == null) {//保存
            archAppSys.setCreatedBy(username);
            archAppSys.setCreatedTime(new Date());
            this.baseMapper.insert(archAppSys);
            type = true;
        } else { //编辑
            archAppSys.setUpdatedBy(username);
            archAppSys.setUpdatedTime(new Date());
            this.baseMapper.updateById(archAppSys);
            //删除关联表
            cataAppBusRelService.remove(new QueryWrapper<CataAppBusRel>().lambda().eq(CataAppBusRel::getAppId, archAppSys.getAppsysId()));
        }
        if (busiIds != null && busiIds.size() > 0) {
            List<CataAppBusRel> list = new ArrayList<>(busiIds.size());
            CataAppBusRel cataAppBusRel;
            for (Integer busiId : busiIds) {
                cataAppBusRel = new CataAppBusRel();
                cataAppBusRel.setAppId(archAppSys.getAppsysId());
                cataAppBusRel.setItemId(busiId);
                list.add(cataAppBusRel);
            }
            cataAppBusRelService.saveBatch(list);
        }
        return type;
    }

    @Override
    @Transactional
    public void delete(Integer appsysId) {
        //删除应用系统
        this.baseMapper.deleteById(appsysId);
        //删除应用系统与权责清单关联项
        cataAppBusRelService.remove(new QueryWrapper<CataAppBusRel>().lambda().eq(CataAppBusRel::getAppId, appsysId));
    }

    @Override
    public List<ArchBusi> getBusiRelList(Integer appsysId) {
        return this.baseMapper.getBusiRelList(appsysId);
    }

    @Override
    public List<Map<String, Object>> getNodes(Integer appId) {
        return this.baseMapper.getNodes(appId);
    }

    @Override
    public List<Map<String, Object>> getLinks(Integer appId) {
        return this.baseMapper.getLinks(appId);
    }
}
