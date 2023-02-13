package com.digitalchina.resourcecatalog.db.service;

import com.digitalchina.resourcecatalog.db.domain.ArchAppSys;
import com.baomidou.mybatisplus.extension.service.IService;
import com.digitalchina.resourcecatalog.db.domain.ArchBusi;
import com.digitalchina.resourcecatalog.db.domain.ArchOrg;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 应用系统信息 服务类
 * </p>
 *
 * @author wangmh
 * @since 2020-05-09
 */
public interface ArchAppSysService extends IService<ArchAppSys> {
    boolean saveOrUpdateInfo(ArchAppSys archAppSys, List<Integer> busiIds, String username);

    void delete(Integer appsysId);

    List<ArchBusi> getBusiRelList(Integer appsysId);

    List<Map<String, Object>> getNodes(Integer appId);

    List<Map<String, Object>> getLinks(Integer appId);
}
