package com.digitalchina.resourcecatalog.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.digitalchina.resourcecatalog.db.domain.*;
import com.digitalchina.resourcecatalog.db.dao.CataInfoApproveMapper;
import com.digitalchina.resourcecatalog.db.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 信息资源目录审核记录表 服务实现类
 * </p>
 *
 * @author baokd
 * @since 2020-05-15
 */
@Service
public class CataInfoApproveServiceImpl extends ServiceImpl<CataInfoApproveMapper, CataInfoApprove> implements CataInfoApproveService {
    @Autowired
    private CataInfoTempService cataInfoTempService;
    @Autowired
    private CataInfoHistoryService cataInfoHistoryService;
    @Autowired
    private CataInfoItemTempRelService cataInfoItemTempRelService;
    @Autowired
    private CataInfoTempTypeRelService cataInfoTempTypeRelService;
    @Autowired
    private ArchBusiUviewExService archBusiUviewExService;



    public IPage<List<Map<String,Object>>> getCataInfoList(IPage page, String name, Integer pid, String auditStatus){
        return this.baseMapper.getCataInfoList(page, name, pid, auditStatus);
    }
    public IPage<List<Map<String,Object>>> getDeleteCheckCataInfoList(IPage page, String name, Integer pid, String auditStatus, String deleteStatus){
        return this.baseMapper.getDeleteCheckCataInfoList(page, name, pid, auditStatus, deleteStatus);
    }
    @Transactional
    public boolean approve(String optType, List<Integer> infoIds,String cataInfoStatusNew, String approveStatus,Integer userId,String userName,String comment){
        //审核不通过
        List<CataInfoTemp> cataInfoTempList = (List<CataInfoTemp>) cataInfoTempService.listByIds(infoIds);
        List<CataInfoApprove> cataInfoApproveList = new ArrayList<>();
        for (CataInfoTemp cataInfoTemp:cataInfoTempList) {
            cataInfoTemp.setAuditStatus(cataInfoStatusNew);
            CataInfoApprove.addCataInfoApprove(optType, cataInfoApproveList, cataInfoTemp.getUviewId(), approveStatus, userId, userName, comment);
        }
        if(cataInfoTempList.size()>0){
            cataInfoTempService.updateBatchById(cataInfoTempList, 100);
        }
        if(cataInfoApproveList.size()>0){
            this.saveBatch(cataInfoApproveList, 100);
        }
        return true;
    }
    @Transactional
    public boolean approveSecond(String optType, List<Integer> infoIds,String approveStatus,Integer userId,String userName,String comment){
        LocalDateTime localDateTime = LocalDateTime.now();
        //审核通过
        boolean isOK = true;
        for(Integer infoId : infoIds){
            //0、存入审核表
            CataInfoApprove cataInfoApprove = new CataInfoApprove();
            cataInfoApprove.setCheckById(userId);
            cataInfoApprove.setCheckByName(userName);
            cataInfoApprove.setCheckTime(localDateTime);
            cataInfoApprove.setComment(comment);
            cataInfoApprove.setInfoId(infoId);
            cataInfoApprove.setOptType(optType);
            cataInfoApprove.setStatus("1");
            this.save(cataInfoApprove);
            //1、先获取暂存表版本
            CataInfoTemp cataInfoTemp = cataInfoTempService.getById(infoId);
            String version = cataInfoTemp.getVersion();
            if(StringUtils.isEmpty(version)){
                //为空，第一次审核通过
                //设置暂存表，版本1.0
                cataInfoTemp.setVersion("1.0");
            }else {
                //之前有审核记录，版本号+0.1
                version = String.format("%.1f", Double.valueOf(version) + Double.valueOf("0.1"));
                cataInfoTemp.setVersion(version);
            }
            cataInfoTemp.setAuditStatus("5");
            boolean flag1 = cataInfoTempService.updateById(cataInfoTemp);
            //copy表格，插入正式表
            List<CataInfoItemTempRel> cataInfoItemTempRelList = cataInfoItemTempRelService.list(
                    new QueryWrapper<CataInfoItemTempRel>().lambda().eq(CataInfoItemTempRel::getUviewId,infoId));
            List<CataInfoTempTypeRel> cataInfoTempTypeRelList = cataInfoTempTypeRelService.list(
                    new QueryWrapper<CataInfoTempTypeRel>().lambda().eq(CataInfoTempTypeRel::getInfoId,infoId));
            ArchBusiUviewEx archBusiUviewEx = new ArchBusiUviewEx();
            List<ArchBusiUviewStrEx> archBusiUviewStrExList = new ArrayList<ArchBusiUviewStrEx>();
            List<AssetCateEx> assetCateExList = new ArrayList<AssetCateEx>();
            //copy表格，插入历史表，注意关联ID为历史表ID，不是资源目录ID。
            CataInfoHistory cataInfoHistory = new CataInfoHistory();
            List<CataInfoHistoryTypeRel> cataInfoHistoryTypeRelList = new ArrayList<CataInfoHistoryTypeRel>();
            List<CataInfoItemHistoryRel> cataInfoItemHistoryRelList = new ArrayList<CataInfoItemHistoryRel>();
            BeanUtils.copyProperties(cataInfoTemp, archBusiUviewEx);
            BeanUtils.copyProperties(cataInfoTemp, cataInfoHistory);

//                BeanUtils.copyProperties(cataInfoItemTempRelList, cataInfoItemHistoryRelList);
//                BeanUtils.copyProperties(cataInfoTempTypeRelList, cataInfoHistoryTypeRelList);

            if (!StringUtils.isEmpty(cataInfoItemTempRelList)){
                for(CataInfoItemTempRel cataInfoItemTempRel:cataInfoItemTempRelList){
                    //正式表信息项
                    ArchBusiUviewStrEx archBusiUviewStrEx = new ArchBusiUviewStrEx();
                    BeanUtils.copyProperties(cataInfoItemTempRel, archBusiUviewStrEx);
                    archBusiUviewStrExList.add(archBusiUviewStrEx);
                    //历史表信息项
                    CataInfoItemHistoryRel cataInfoItemHistoryRel = new CataInfoItemHistoryRel();
                    BeanUtils.copyProperties(cataInfoItemTempRel, cataInfoItemHistoryRel);
                    cataInfoItemHistoryRelList.add(cataInfoItemHistoryRel);
                }
            }
            if (!StringUtils.isEmpty(cataInfoTempTypeRelList)){
                for(CataInfoTempTypeRel cataInfoTempTypeRel:cataInfoTempTypeRelList){
                    //正式表关联表
                    AssetCateEx assetCateEx = new AssetCateEx();
                    BeanUtils.copyProperties(cataInfoTempTypeRel, assetCateEx);
                    assetCateExList.add(assetCateEx);
                    //历史表关联表
                    CataInfoHistoryTypeRel cataInfoHistoryTypeRel = new CataInfoHistoryTypeRel();
                    BeanUtils.copyProperties(cataInfoTempTypeRel, cataInfoHistoryTypeRel);
                    cataInfoHistoryTypeRelList.add(cataInfoHistoryTypeRel);
                }
            }
            boolean flag2 = archBusiUviewExService.saveArchBuisUviewEx(archBusiUviewEx,archBusiUviewStrExList,assetCateExList,userName,cataInfoItemTempRelList);
            cataInfoHistoryService.updateAllStatus(infoId);
            boolean flag3 = cataInfoHistoryService.saveCataInfoHistory(cataInfoHistory,cataInfoHistoryTypeRelList,cataInfoItemHistoryRelList,userName);
            if(!(flag1 && flag2 && flag3 )){
                isOK = false;
                continue;
            }
        }
        return isOK;

    }

}
