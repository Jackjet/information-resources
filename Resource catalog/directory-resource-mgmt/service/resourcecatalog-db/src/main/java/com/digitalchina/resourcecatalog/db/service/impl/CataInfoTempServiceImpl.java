package com.digitalchina.resourcecatalog.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digitalchina.resourcecatalog.db.dao.CataInfoApproveMapper;
import com.digitalchina.resourcecatalog.db.dao.CataInfoTempMapper;
import com.digitalchina.resourcecatalog.db.domain.*;
import com.digitalchina.resourcecatalog.db.dto.CataInfoTempDto;
import com.digitalchina.resourcecatalog.db.service.*;
import org.keycloak.representations.UserInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 信息资源目录(暂存表) 服务实现类
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
@Service
public class CataInfoTempServiceImpl extends ServiceImpl<CataInfoTempMapper, CataInfoTemp> implements CataInfoTempService {

    @Autowired
    private CataInfoTempTypeRelService cataInfoTempTypeRelService;
    @Autowired
    private ArchBusiUviewExService archBusiUviewExService;
    @Autowired
    private CataInfoItemTempRelService cataInfoItemTempRelService;
    @Autowired
    private CataBusInfoRelService cataBusInfoRelService;
//    @Autowired
//    private MqResourceService mqResourceService;
    @Override
    @Transactional
    public Map<String, Object> saveInfoTemp(CataInfoTempDto cata, String userName) {
        String log = "";
        CataInfoTemp cataInfoTemp = new CataInfoTemp();
        BeanUtils.copyProperties(cata, cataInfoTemp);
        cataInfoTemp.setAuditStatus("0");
        cataInfoTemp.setUpdateLimitReaded(0);
        if (cata.getUviewId() != null) {
            if ("1".equals(cata.getAuditStatus())) {
                return null;
            }
            cataInfoTemp.setUpdater(userName);
            cataInfoTemp.setUpdateDt(new Date());
            this.baseMapper.updateById(cataInfoTemp);
            log += "编辑信息资源【" + cataInfoTemp.getUviewNo() + "/" + cataInfoTemp.getUviewNm() + "】";
            //删除分类
            cataInfoTempTypeRelService.remove(new QueryWrapper<CataInfoTempTypeRel>().eq(CataInfoTempTypeRel.INFO_ID, cata.getUviewId()));
            //删除权责
            cataBusInfoRelService.remove(new QueryWrapper<CataBusInfoRel>().eq(CataBusInfoRel.INFO_ID, cata.getUviewId()));
        } else {
            cataInfoTemp.setCreator(userName);
            cataInfoTemp.setCrtDt(new Date());
            this.baseMapper.insert(cataInfoTemp);
            log += "新增信息资源【" + cataInfoTemp.getUviewNo() + "/" + cataInfoTemp.getUviewNm() + "】";
        }
        if (cata.getCataInfoTempTypeRelList() != null) {
            for (CataInfoTempTypeRel cataInfoTempTypeRel : cata.getCataInfoTempTypeRelList()) {
                cataInfoTempTypeRel.setInfoId(cataInfoTemp.getUviewId());
            }
            cataInfoTempTypeRelService.saveBatch(cata.getCataInfoTempTypeRelList());
        }
        if (cata.getRelBusList() != null && cata.getRelBusList().size()>0) {
            List<CataBusInfoRel> cataBuList = new ArrayList<>();
            for (Integer relBusi : cata.getRelBusList()) {
                CataBusInfoRel cataBusInfoRel = new CataBusInfoRel();
                cataBusInfoRel.setBusId(relBusi);
                cataBusInfoRel.setInfoId(cataInfoTemp.getUviewId());
                cataBusInfoRel.setType(0);
                cataBuList.add(cataBusInfoRel);
            }
            cataBusInfoRelService.saveBatch(cataBuList);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("log", log);
        map.put("uviewId", cataInfoTemp.getUviewId());
        return map;
    }

    @Override
    @Transactional
    public String deleteByIds(List<Integer> ids) {
        List<String> logList = new ArrayList<>();
        List<CataInfoTemp> list = this.list(new QueryWrapper<CataInfoTemp>().in(CataInfoTemp.UVIEW_ID, ids).eq(CataInfoTemp.DELETED, 0));
        List<ArchBusiUviewEx> exList = archBusiUviewExService.list(new QueryWrapper<ArchBusiUviewEx>().in(ArchBusiUviewEx.UVIEW_ID, ids).eq(ArchBusiUviewEx.DELETED, 0));
        if (list != null && list.size() > 0) {
            for (CataInfoTemp cataInfoTemp : list) {
                cataInfoTemp.setDeleted(1);
                cataInfoTemp.setUpdateDt(new Date());
                logList.add(cataInfoTemp.getUviewNo() + "/" + cataInfoTemp.getUviewNm() + "已删除");
            }
            if(list.size()>0){
                this.updateBatchById(list);
            }

            if (exList != null && exList.size() > 0) {
            	 for (ArchBusiUviewEx archBusiUviewEx : exList) {
                     archBusiUviewEx.setDeleted(1);
                     archBusiUviewEx.setUpdateDt(new Date());
                 }
            	 archBusiUviewExService.updateBatchById(exList);
            }
//            for (ArchBusiUviewEx archBusiUviewEx : exList) {
//            	if("02".equals(archBusiUviewEx.getPubSts())){
//                	mqResourceService.sendMessage(RabbitConfig.CATA_DELETE_SHARE, archBusiUviewEx.getUviewNo());
//                }else{
//                	mqResourceService.sendMessage(RabbitConfig.CATA_DELETE, archBusiUviewEx.getUviewNo());
//                }
//            }
        }
        return String.join("、", logList);
    }

    @Override
    public IPage<List<CataInfoTemp>> selectPages(Page page, String uviewNo, String uviewNm, String provOrgId, String auditStatus,
                                                 Integer typeId, String shareLv, String pubSts, String cityCataCode) {
        return this.baseMapper.selectPages(page, uviewNo, uviewNm, provOrgId, auditStatus, typeId, shareLv, pubSts, cityCataCode);
    }

    @Override
    public Integer saveInfoTemp_import(CataInfoTempDto cata) {
        CataInfoTemp cataInfoTemp = new CataInfoTemp();
        BeanUtils.copyProperties(cata, cataInfoTemp);
        if (cata.getUviewId() != null) {
            this.baseMapper.updateById(cataInfoTemp);
            cataInfoTempTypeRelService.remove(new QueryWrapper<CataInfoTempTypeRel>().eq(CataInfoTempTypeRel.INFO_ID, cata.getUviewId()));
            cataInfoItemTempRelService.remove(new QueryWrapper<CataInfoItemTempRel>().eq(CataInfoItemTempRel.UVIEW_ID, cata.getUviewId()));
        } else {
            this.baseMapper.insert(cataInfoTemp);
        }
        if (cata.getCataInfoTempTypeRelList() != null) {
            for (CataInfoTempTypeRel cataInfoTempTypeRel : cata.getCataInfoTempTypeRelList()) {
                cataInfoTempTypeRel.setInfoId(cataInfoTemp.getUviewId());
            }
            cataInfoTempTypeRelService.saveBatch(cata.getCataInfoTempTypeRelList());
        }
        if (cata.getCataInfoTempItemRelList() != null) {
            for (CataInfoItemTempRel cataInfoItemTempRel : cata.getCataInfoTempItemRelList()) {
                cataInfoItemTempRel.setUviewId(cataInfoTemp.getUviewId());
            }
            cataInfoItemTempRelService.saveBatch(cata.getCataInfoTempItemRelList());
        }
        return cataInfoTemp.getUviewId();
    }

    @Override
    public List<Map<String, Object>> waitReviewInfoList() {
        return this.baseMapper.waitReviewInfoList();
    }
    @Override
    public String getCode(String fullTypCd){
        CataInfoTemp cata=getOne(new QueryWrapper<CataInfoTemp>().likeRight(CataInfoTemp.UVIEW_NO, fullTypCd+"/").orderByDesc(CataInfoTemp.UVIEW_NO));
        if(cata!=null){
            String code=cata.getUviewNo().split("/")[1];
            int temp=Integer.parseInt(code)+1;
            code=String.format("%06d",temp);
            return fullTypCd+"/"+code;
        }
        return fullTypCd+"/000001";
    }

    @Override
    public int updateRelBusi(Integer newValue, String newMsg, List<Integer> infoIds){
        if(infoIds.size()>0){
            return this.baseMapper.updateRelBusi(newValue, newMsg, infoIds);
        }
        return 0;
    }

    @Override
    public IPage<CataInfoTemp> queryUpdateLimitPages(Page page, String uviewNo, String uviewNm, Integer orgId, String updateCyc) {
        LocalDateTime day = LocalDateTime.now();
        day = day.minusDays(1);

        LocalDateTime week = LocalDateTime.now();
        week = week.minusDays(1 + 7);

        LocalDateTime month = LocalDateTime.now();
        month = month.minusDays(1).minusMonths(1);

        LocalDateTime quarter = LocalDateTime.now();
        quarter = quarter.minusDays(1).minusMonths(3);

        LocalDateTime halfYear = LocalDateTime.now();
        halfYear = halfYear.minusDays(1).minusMonths(6);

        LocalDateTime year = LocalDateTime.now();
        year = year.minusDays(1).minusYears(1);

        IPage<CataInfoTemp> listIPage = this.baseMapper.queryUpdateLimitPages(page, uviewNo, uviewNm, orgId, updateCyc,
                Timestamp.valueOf(day), Timestamp.valueOf(week),Timestamp.valueOf(month),Timestamp.valueOf(quarter), Timestamp.valueOf(halfYear), Timestamp.valueOf(year));
        ZoneId defaultZonId = ZoneId.systemDefault();
        listIPage.getRecords().forEach(cataInfoTemp -> {

            Date updateDt = cataInfoTemp.getUpdateDt();
            cataInfoTemp.setCrtDt(updateDt);
            if(updateDt!=null){
                LocalDateTime localDateTime = LocalDateTime.ofInstant(updateDt.toInstant(), defaultZonId);
                switch (cataInfoTemp.getUpdateCyc()){
                    case "02" :
                        localDateTime = localDateTime.plusDays(1);
                        break;
                    case "03" :
                        localDateTime = localDateTime.plusDays(7);
                        break;
                    case "04" :
                        localDateTime = localDateTime.plusMonths(1);
                        break;
                    case "05" :
                        localDateTime = localDateTime.plusMonths(3);
                        break;
                    case "06" :
                        localDateTime = localDateTime.plusMonths(6);
                        break;
                    case "07" :
                        localDateTime = localDateTime.plusYears(1);
                        break;
                }
                ZonedDateTime zonedDateTime = localDateTime.atZone(defaultZonId);
                cataInfoTemp.setUpdateDt(Date.from(zonedDateTime.toInstant()));
            }
        });
        return listIPage;
    }

    @Override
    public int updateLimitReadedStatus(Integer infoId) {
        return this.baseMapper.updateLimitReadedStatus(infoId);
    }

    @Override
    public int updateLimitReadedStatusAll(String uviewNo, String uviewNm, Integer orgId, String updateCyc) {
        LocalDateTime day = LocalDateTime.now();
        day = day.minusDays(1);

        LocalDateTime week = LocalDateTime.now();
        week = week.minusDays(1 + 7);

        LocalDateTime month = LocalDateTime.now();
        month = month.minusDays(1).minusMonths(1);

        LocalDateTime quarter = LocalDateTime.now();
        quarter = quarter.minusDays(1).minusMonths(3);

        LocalDateTime halfYear = LocalDateTime.now();
        halfYear = halfYear.minusDays(1).minusMonths(6);

        LocalDateTime year = LocalDateTime.now();
        year = year.minusDays(1).minusYears(1);

        return this.baseMapper.updateLimitReadedStatusAll(uviewNo, uviewNm, orgId, updateCyc,
                Timestamp.valueOf(day), Timestamp.valueOf(week),Timestamp.valueOf(month),Timestamp.valueOf(quarter), Timestamp.valueOf(halfYear), Timestamp.valueOf(year));
    }
}
