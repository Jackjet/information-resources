package com.digitalchina.resourcecatalog.admin.web;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalchina.resourcecatalog.admin.annotation.RequiresPermissionsDesc;
import com.digitalchina.resourcecatalog.admin.service.LogHelper;
import com.digitalchina.resourcecatalog.admin.util.UserInfo;
import com.digitalchina.resourcecatalog.core.util.PageUtil;
import com.digitalchina.resourcecatalog.core.util.ResponseUtil;
import com.digitalchina.resourcecatalog.db.domain.ArchOrg;
import com.digitalchina.resourcecatalog.db.domain.CataInfoApprove;
import com.digitalchina.resourcecatalog.db.domain.CataInfoTemp;
import com.digitalchina.resourcecatalog.db.domain.DictAssetCate;
import com.digitalchina.resourcecatalog.db.service.CataInfoApproveService;
import com.digitalchina.resourcecatalog.db.service.CataInfoHistoryTypeRelService;
import com.digitalchina.resourcecatalog.db.service.CataInfoTempService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

import static com.digitalchina.resourcecatalog.admin.util.AdminResponseCode.INFO_NO_DELETE;

/**
 * <p>
 * 信息资源目录审核记录表 前端控制器
 * </p>
 *
 * @author baokd
 * @since 2020-05-15
 */
@RestController
@RequestMapping("/admin/cataInfoApprove")
@Api(value = "目录初审", tags = "目录初审")
public class CataInfoApproveController {
    @Autowired
    private CataInfoApproveService cataInfoApproveService;
    @Autowired
    private CataInfoTempService cataInfoTempService;

    @Autowired
    private LogHelper logHelper;

    @RequiresPermissions("cata:infoApprove:cataInfoList")
    @RequiresPermissionsDesc(menu = {"目录管理"}, button = "目录列表分页")
    @GetMapping("/cataInfoList")
    @ApiOperation("目录列表分页")
    public Object cataInfoList(String name,Integer pid,String auditStatus,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit) {
        Page page1 = new Page<>(page, limit);
        return ResponseUtil.ok(cataInfoApproveService.getCataInfoList(page1, name, pid, auditStatus));
    }
    @RequiresPermissions("cata:infoApprove:cataInfoDeleteList")
    @RequiresPermissionsDesc(menu = {"目录管理", "删除审核"}, button = "列表")
    @GetMapping("/cataInfoDeleteList")
    @ApiOperation("删除审核列表分页")
    public Object cataInfoDeleteList(String name,Integer pid,String auditStatus,String deleteStatus,
                               @RequestParam(defaultValue = "1") Integer page,
                               @RequestParam(defaultValue = "10") Integer limit) {
        Page page1 = new Page<>(page, limit);
        return ResponseUtil.ok(cataInfoApproveService.getDeleteCheckCataInfoList(page1, name, pid, auditStatus, deleteStatus));
    }
    @RequiresPermissions("cata:infoApprove:approveList")
    @RequiresPermissionsDesc(menu = {"目录管理"}, button = "审核列表分页")
    @GetMapping("/approveList")
    @ApiOperation("审核列表分页")
    public Object approveList(Integer infoId,
                               @RequestParam(defaultValue = "1") Integer page,
                               @RequestParam(defaultValue = "5") Integer limit) {
        if(StringUtils.isEmpty(infoId)){
            return ResponseUtil.fail(9001,"资源目录ID不能为空！");
        }
        QueryWrapper<CataInfoApprove> qw=new QueryWrapper<CataInfoApprove>();
        if(!StringUtils.isEmpty(infoId)){
            qw.eq(CataInfoApprove.INFO_ID,infoId);
        }
        qw.orderByDesc(CataInfoApprove.CHECK_TIME);
        Page page1 = new Page<>(page, limit);
        return ResponseUtil.ok(cataInfoApproveService.page(page1,qw));
    }

    @RequiresPermissions("cata:infoApprove:approve")
    @RequiresPermissionsDesc(menu = {"目录管理", "目录初审"}, button = "审核")
    @PostMapping("/approve")
    @ApiOperation("审核")
    public Object approve(@RequestBody Integer[]  infoIds,
                          @RequestParam(required = true)String approveStatus,
                          @RequestParam(required = true)String comment) {
        if(StringUtils.isEmpty(infoIds) || infoIds.length == 0){
            return ResponseUtil.fail(9001,"资源目录ID列表不能为空！");
        }
        if(StringUtils.isEmpty(approveStatus)){
            return ResponseUtil.fail(9001,"审核状态不能为空！");
        }
        boolean isOK = true;
        String log="";
        for (Integer infoId:infoIds) {
            CataInfoTemp cataInfoTemp = cataInfoTempService.getById(infoId);
            if(!"1".equals(cataInfoTemp.getAuditStatus())){
                isOK = false;
                break;
            }
            log+=cataInfoTemp.getUviewNm()+"、";
        }
        if(!isOK){
            return ResponseUtil.fail(9001,"只能选择状态为‘待初审’的信息资源！");
        }
        if(!StringUtils.isEmpty(log)){
            log=log.substring(0,log.length()-1);
        }
        String userName = UserInfo.getUsername();
        Integer userId = UserInfo.getId();
        List<Integer> idList = new ArrayList<>(infoIds.length);
        Collections.addAll(idList,infoIds);
        String cataInfoStatusNew = "2", cataInfoStatusNewStr = "初审驳回";
        if("1".equals(approveStatus)){
            cataInfoStatusNew = "3";
            cataInfoStatusNewStr = "初审通过";
        }
        if(cataInfoApproveService.approve("初审",idList,cataInfoStatusNew, approveStatus,userId,userName,comment)){
            logHelper.logGeneralSucceed("目录管理-目录初审", cataInfoStatusNewStr + "【" + log + "】");
            return ResponseUtil.ok();
        }else{
            return ResponseUtil.fail();
        }
    }
    @RequiresPermissions("cata:infoApprove:approve2")
    @RequiresPermissionsDesc(menu = {"目录管理", "目录终审"}, button = "审核")
    @PostMapping("/approve2")
    @ApiOperation("审核")
    public Object approve2(@RequestBody Integer[]  infoIds,
                          @RequestParam(required = true)String approveStatus,
                          @RequestParam(required = true)String comment) {
        if(StringUtils.isEmpty(infoIds) || infoIds.length == 0){
            return ResponseUtil.fail(9001,"资源目录ID列表不能为空！");
        }
        if(StringUtils.isEmpty(approveStatus)){
            return ResponseUtil.fail(9001,"审核状态不能为空！");
        }
        boolean isOK = true;
        String log="";
        for (Integer infoId:infoIds) {
            CataInfoTemp cataInfoTemp = cataInfoTempService.getById(infoId);
            if(!"3".equals(cataInfoTemp.getAuditStatus())){
                isOK = false;
                break;
            }
            log+=cataInfoTemp.getUviewNm()+"、";
        }
        if(!isOK){
            return ResponseUtil.fail(9001,"只能选择状态为‘初审通过’的信息资源！");
        }
        if(!StringUtils.isEmpty(log)){
            log=log.substring(0,log.length()-1);
        }
        String userName = UserInfo.getUsername();
        Integer userId = UserInfo.getId();
        List<Integer> idList = new ArrayList<>(infoIds.length);
        Collections.addAll(idList,infoIds);
        if("1".equals(approveStatus)){//终审通过
            if(cataInfoApproveService.approveSecond("终审",idList, approveStatus,userId,userName,comment)){
                logHelper.logGeneralSucceed("目录管理-目录终审", "终审通过【" + log + "】");
                return ResponseUtil.ok();
            }else{
                return ResponseUtil.fail();
            }
        }else{
            if(cataInfoApproveService.approve("终审",idList,"4", approveStatus,userId,userName,comment)){
                logHelper.logGeneralSucceed("目录管理-目录终审", "终审驳回【" + log + "】");
                return ResponseUtil.ok();
            }else{
                return ResponseUtil.fail();
            }
        }
    }
    @RequiresPermissions("cata:infoApprove:approveDelete")
    @RequiresPermissionsDesc(menu = {"目录管理", "删除审核"}, button = "审核")
    @PostMapping("/approveDelete")
    @ApiOperation("删除审核")
    public Object approveDelete(@RequestBody Integer[] appIds,
                          @RequestParam(required = true)String approveStatus,
                          @RequestParam(required = true)String comment) {
        if(StringUtils.isEmpty(appIds) || appIds.length == 0){
            return ResponseUtil.fail(9001,"审核ID列表不能为空！");
        }
        if(StringUtils.isEmpty(approveStatus)){
            return ResponseUtil.fail(9001,"审核状态不能为空！");
        }
        String log="";

        QueryWrapper<CataInfoApprove> appWrapper = new QueryWrapper<CataInfoApprove>();
        appWrapper.in(CataInfoApprove.ID, appIds).eq(CataInfoApprove.OPT_TYPE, "删除审核");
        List<CataInfoApprove> appList = cataInfoApproveService.list(appWrapper);
        List<Integer> infoIds = new ArrayList<>();
        String error="";
        for( int i=0;i< appList.size();i++){
            CataInfoApprove cataInfoApprove = appList.get(i);
            if(!cataInfoApprove.getStatus().equals("")){
                error+=cataInfoApprove.getId()+",";
            }else{
                cataInfoApprove.setCheckById(UserInfo.getId());
                cataInfoApprove.setCheckByName(UserInfo.getUsername());
                cataInfoApprove.setStatus(approveStatus);
                cataInfoApprove.setComment(comment);
                infoIds.add(cataInfoApprove.getInfoId());
            }

        }
        if (error!="") {
            error=error.substring(0,error.length()-1);
            return ResponseUtil.fail(INFO_NO_DELETE, "所选记录中【"+error+"】已处理，不能再次处理");
        }
        if(appList!=null && appList.size()>0){
            cataInfoApproveService.updateBatchById(appList);
        }

        String cataInfoStatusNew = "删除审核拒绝";
        if("1".equals(approveStatus)){
            cataInfoStatusNew = "删除审核通过";
            if(infoIds.size()>0){
                //方法内删除temp后，会删除对应正式表数据，无需额外删除正式表数据。
                log = cataInfoTempService.deleteByIds(infoIds);
            }
        }
        if(!StringUtils.isEmpty(log)){
            log=log.substring(0,log.length()-1);
        }
        logHelper.logGeneralSucceed("目录管理-目录初审",  cataInfoStatusNew + "【" + log + "】");
        return ResponseUtil.ok();

    }
}
