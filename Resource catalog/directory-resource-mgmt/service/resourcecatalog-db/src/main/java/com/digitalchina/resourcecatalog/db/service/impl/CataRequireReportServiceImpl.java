package com.digitalchina.resourcecatalog.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digitalchina.resourcecatalog.db.dao.CataRequireReportMapper;
import com.digitalchina.resourcecatalog.db.domain.*;
import com.digitalchina.resourcecatalog.db.service.*;
import com.digitalchina.resourcecatalog.db.util.CreateWord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 信息需求目录报告 服务实现类
 * </p>
 *
 * @author baokd
 * @since 2020-05-25
 */
@Service
public class CataRequireReportServiceImpl extends ServiceImpl<CataRequireReportMapper, CataRequireReport> implements CataRequireReportService {
    @Autowired
    SysStorageService sysStorageService;
    @Autowired
    ArchOrgService archOrgService;
    @Autowired
    CataRequireService cataRequireService;
    @Autowired
    SysDictService sysDictService;
    @Autowired
    AssetCateExService assetCateExService;

    @Value("${templatepath}")
    String templatepath;

    String[] types = new String[]{"share_way", "update_cycle"};

    @Override
    @Transactional
    public void delete(CataRequireReport bean) {
        bean.setDeleted(1);
        if (this.updateById(bean)) {
            SysStorage sysFileStore = sysStorageService.getOne(new QueryWrapper<SysStorage>().eq(SysStorage.KEY, bean.getFileKey()));
            if (null != sysFileStore) {
                sysFileStore.setDeleted(1);
                sysStorageService.updateById(sysFileStore);
            }
        }
    }

    @Override
    public IPage<List<CataInfoReport>> selectPages(Page page, Integer depId, String startDate, String endDate) {
        return this.baseMapper.selectPages(page, depId, startDate, endDate);
    }

    @Override
    public String generateReport(Integer depId) {
        try {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            ArchOrg archOrg = archOrgService.getById(depId);
            dataMap.put("deptName", "" + archOrg.getOrgNm());
            SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");
            dataMap.put("downloadTime", datef.format(new Date()));
            List<CataRequire> detailList = cataRequireService.list(new QueryWrapper<CataRequire>()
                    .eq(CataRequire.DEPT_ID, depId).eq(CataRequire.DELETED, 0).orderByDesc(CataRequire.CREATE_TIME));
            if (detailList != null && detailList.size() > 0) {
                dataMap.put("count", detailList.size());
                List<SysDict> dictList = sysDictService
                        .list(new QueryWrapper<SysDict>().in(SysDict.TYPE, types).orderByAsc(SysDict.TYPE));
//                List<ArchOrg> depList = archOrgService.list(new QueryWrapper<ArchOrg>());
                for (int i = 1; i < detailList.size() + 1; i++) {
                    CataRequire cataRequire = detailList.get(i - 1);
                    cataRequire.setNumber(i);
//                    for (ArchOrg org : depList) {
//                        if (org.getOrgId().equals(cataRequire.getBelongTo())) {
//                            orgName.setBelongToName(org.getOrgNm());
//                        } else if (org.getOrgId().equals(cataRequire.getProvOrgId())) {
//                            cataRequire.setProvOrgName(org.getOrgNm());
//                        }
//                    }
                    ArchOrg provOrgNm = archOrgService.getById(cataRequire.getProvOrgId());
                    if (provOrgNm != null) {
                        ArchOrg belongToNm = archOrgService.getById(cataRequire.getBelongTo());
                        if (belongToNm != null) {
                            cataRequire.setProvOrgName(provOrgNm.getOrgNm() + " / " + belongToNm.getOrgNm());
                        } else {
                            cataRequire.setProvOrgName(provOrgNm.getOrgNm());
                        }
                    }
                    ArchOrg deptNm = archOrgService.getById(cataRequire.getDeptId());
                    if (deptNm != null) {
                        ArchOrg orgNm = archOrgService.getById(cataRequire.getOrgId());
                        if (orgNm != null) {
                            cataRequire.setBelongToName(deptNm.getOrgNm() + " / " + orgNm.getOrgNm());
                        } else {
                            cataRequire.setBelongToName(deptNm.getOrgNm());
                        }
                    }
                    for (SysDict sysDict : dictList) {
                        if ("share_way".equals(sysDict.getType())
                                && sysDict.getValue().equals(cataRequire.getAccessWay())) {
                            cataRequire.setAccessWay(sysDict.getName());
                        } else if ("update_cycle".equals(sysDict.getType())
                                && sysDict.getValue().equals(cataRequire.getUseFrequency())) {
                            cataRequire.setUseFrequency(sysDict.getName());
                        }
                    }
                    if (cataRequire.getIsAccess() != null && "0".equals(cataRequire.getIsAccess())) {
                        cataRequire.setIsAccess("是");
                    } else {
                        cataRequire.setIsAccess("否");
                    }
                }
            } else {
                dataMap.put("count", 0);
            }
            dataMap.put("detailList", detailList);
            CreateWord createWord = new CreateWord();
            String outPath = "需求资源报告-" + archOrg.getOrgNm() + ".docx";
            createWord.createWord(dataMap, "require", templatepath, outPath);
            return outPath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
