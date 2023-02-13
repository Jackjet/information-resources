package com.digitalchina.resourcecatalog.admin.web;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalchina.resourcecatalog.admin.annotation.RequiresPermissionsDesc;
import com.digitalchina.resourcecatalog.admin.service.LogHelper;
import com.digitalchina.resourcecatalog.admin.util.UserInfo;
import com.digitalchina.resourcecatalog.core.util.ResponseUtil;
import com.digitalchina.resourcecatalog.db.domain.*;
import com.digitalchina.resourcecatalog.db.dto.ArchOrgDto;
import com.digitalchina.resourcecatalog.db.dto.CataInfoTempDto;
import com.digitalchina.resourcecatalog.db.dto.CataInfoTempTypeRelDto;
import com.digitalchina.resourcecatalog.db.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.digitalchina.resourcecatalog.admin.util.AdminResponseCode.*;

/**
 * <p>
 * 信息资源目录(暂存表) 前端控制器
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
@RestController
@RequestMapping("/admin/cataInfoTemp")
@Validated
@Api(value = "信息资源目录", tags = "信息资源目录")
public class CataInfoTempController {
    @Autowired
    private CataInfoTempService cataInfoTempService;
    @Autowired
    private ArchBusiUviewExService archBusiUviewExService;
    @Autowired
    private CataInfoTempTypeRelService cataInfoTempTypeRelService;
    @Autowired
    private SysDictService sysDictService;
    @Autowired
    private DictAssetCateService dictAssetCateService;
    @Autowired
    private ArchOrgService archOrgService;
	@Autowired
	private CataBusInfoRelService cataBusInfoRelService;
	@Autowired
	private CataInfoApproveService cataInfoApproveService;
    @Autowired
    private LogHelper logHelper;
    
    @RequiresPermissions("cata:infoTemp:list")
    @RequiresPermissionsDesc(menu = {"目录编制", "信息资源目录"}, button = "目录查询")
    @GetMapping("/list")
    @ApiOperation("目录查询")
    public Object list(String uviewNo,String uviewNm,String provOrgId,String auditStatus,Integer typeId, String shareLv, String pubSts,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit) {
        Page page1 = new Page<>(page, limit);
        if(UserInfo.isDeptAdmin()){
        	List<Integer> list=UserInfo.getDeptIds();
        	for (Integer integer : list) {
        		provOrgId=integer+",";
			}
        	if(provOrgId!=null && provOrgId!=""){
        		provOrgId=provOrgId.substring(0,provOrgId.length()-1);
        	}
        }
        return ResponseUtil.ok(cataInfoTempService.selectPages(page1,uviewNo,uviewNm,provOrgId,auditStatus,typeId, shareLv, pubSts, null));
    }
	@RequiresPermissions("cata:infoTemp:list2")
	@RequiresPermissionsDesc(menu = {"目录编制", "更新预警"}, button = "目录查询")
	@GetMapping("/list2")
	@ApiOperation("目录查询")
	public Object list2(String uviewNo,String uviewNm,Integer orgId,String updateCyc,
					   @RequestParam(defaultValue = "1") Integer page,
					   @RequestParam(defaultValue = "10") Integer limit) {
		Page page1 = new Page<>(page, limit);
		return ResponseUtil.ok(cataInfoTempService.queryUpdateLimitPages(page1,uviewNo,uviewNm,orgId, updateCyc));
	}
    @RequiresPermissions("cata:infoTemp:read")
    @RequiresPermissionsDesc(menu = {"目录编制", "信息资源目录"}, button = "目录详情")
    @GetMapping("/read")
    @ApiOperation("详情")
    public Object read(@RequestParam(required=true) Integer id) {
        CataInfoTemp cata = cataInfoTempService.getOne(new QueryWrapper<CataInfoTemp>().eq(CataInfoTemp.UVIEW_ID, id).eq(CataInfoTemp.DELETED, 0));
        CataInfoTempDto cataDto=new CataInfoTempDto();
        if(cata!=null){
	        BeanUtils.copyProperties(cata, cataDto);
	        String[] types=new String[]{"format_type","format_type_"+cataDto.getMediaFmt(),"update_cycle","share_type"};
	        List<SysDict> dictList=sysDictService.list(new QueryWrapper<SysDict>().in(SysDict.TYPE, types).orderByAsc(SysDict.TYPE));
	        for (SysDict sysDict : dictList) {
				if("format_type".equals(sysDict.getType())&&sysDict.getValue().equals(cataDto.getMediaFmt())){
					cataDto.setMediaFmtName(sysDict.getName());
				}else if(("format_type_"+cataDto.getMediaFmt()).equals(sysDict.getType())&&sysDict.getValue().equals(cataDto.getMediaFmtType())){
					cataDto.setMediaFmtTypeName(sysDict.getName());
				}else if("update_cycle".equals(sysDict.getType())&&sysDict.getValue().equals(cataDto.getUpdateCyc())){
					cataDto.setUpdateCycName(sysDict.getName());
				}else if("share_type".equals(sysDict.getType())&&sysDict.getValue().equals(cataDto.getShareLv())){
					cataDto.setShareLvName(sysDict.getName());
				}
			}
			QueryWrapper<ArchOrg> queryWrapper = new QueryWrapper<ArchOrg>();
			queryWrapper.select(ArchOrg.ORG_ID, ArchOrg.PAR_ORG_ID, ArchOrg.ORG_CD, ArchOrg.ORG_NM, ArchOrg.SOCIAL_CREDIT_CD);
	        if(!StringUtils.isEmpty(cataDto.getBelongTo())){
	        	String[] belongStrArr = cataDto.getBelongTo().split(",");
				List<Integer> orgIds = Arrays.asList((Integer[]) ConvertUtils.convert(belongStrArr,Integer.class));
				queryWrapper.and(w -> w.eq(ArchOrg.ORG_ID, cataDto.getProvOrgId()).or().in(ArchOrg.ORG_ID, orgIds));
				List<ArchOrg> deptListAll = archOrgService.list(queryWrapper);
				List<String> deptListFirstLevel = deptListAll.stream().filter(item -> item.getOrgId().equals(cataDto.getProvOrgId())).collect(Collectors.toList())
						.stream().map(x -> x.getOrgNm()).collect(Collectors.toList());
				cataDto.setProvOrgName(String.join( ",", deptListFirstLevel));

				List<ArchOrg> deptListSecondLevelOrg = deptListAll.stream().filter(item -> orgIds.contains(item.getOrgId())).collect(Collectors.toList());
				List<String> deptNmListSecondLevel = new ArrayList<>();
				List<Integer> deptIdListSecondLevel = new ArrayList<>();
				deptListSecondLevelOrg.forEach(item ->{
					deptNmListSecondLevel.add(item.getOrgNm());
					deptIdListSecondLevel.add(item.getOrgId());
				});
				cataDto.setBelongToName(String.join( ",", deptNmListSecondLevel));
				cataDto.setBelongToList(deptIdListSecondLevel);
			}else{
				queryWrapper.eq(ArchOrg.ORG_ID, cataDto.getProvOrgId());
				List<ArchOrg> deptListAll = archOrgService.list(queryWrapper);
				List<String> deptListFirstLevel = deptListAll.stream().filter(item -> item.getOrgId().equals(cataDto.getProvOrgId())).collect(Collectors.toList())
						.stream().map(x -> x.getOrgNm()).collect(Collectors.toList());
				cataDto.setProvOrgName(String.join( ",", deptListFirstLevel));
				cataDto.setBelongToList(new ArrayList<>());
			}

	        List<CataInfoTempTypeRel> typeList=cataInfoTempTypeRelService.list(new QueryWrapper<CataInfoTempTypeRel>().eq(CataInfoTempTypeRel.INFO_ID, id));
	        List<CataInfoTempTypeRelDto> dtoTypeList=new ArrayList<CataInfoTempTypeRelDto>();
	        List<DictAssetCate> dictAssetCateList=dictAssetCateService.list(new QueryWrapper<DictAssetCate>().eq(DictAssetCate.STATUS, "1"));
	        for (DictAssetCate dictAssetCate : dictAssetCateList) {
	        	for (CataInfoTempTypeRel cataInfoTempTypeRel : typeList) {
	        		if(dictAssetCate.getTypId().equals(cataInfoTempTypeRel.getTypeId())){
	        			CataInfoTempTypeRelDto dto=new CataInfoTempTypeRelDto();
	        			BeanUtils.copyProperties(cataInfoTempTypeRel, dto);
	        			dto.setTypeName(dictAssetCate.getTypNm());
	        			dtoTypeList.add(dto);
	        		}
				}
			}
	        cataDto.setCataInfoTempTypeRelDtoList(dtoTypeList);
	        //权责清单
			List<CataBusInfoRel> cataBusInfoRelList =cataBusInfoRelService.list(new QueryWrapper<CataBusInfoRel>().eq(CataBusInfoRel.INFO_ID, id));
			List<Integer> relBusList = new ArrayList<>();
			cataBusInfoRelList.forEach(item -> relBusList.add(item.getBusId()));
			cataDto.setRelBusList(relBusList);
        }
        return ResponseUtil.ok(cataDto);
    }
    
    @RequiresPermissions("cata:infoTemp:getCode")
    @RequiresPermissionsDesc(menu = {"目录编制", "信息资源目录"}, button = "获取信息资源代码")
    @GetMapping("/getCode")
    @ApiOperation("获取信息资源代码")
    public Object getCode(@RequestParam(required=true) String fullTypCd) {
        return ResponseUtil.ok(cataInfoTempService.getCode(fullTypCd));
    }

    @RequiresPermissions("cata:infoTemp:update")
    @RequiresPermissionsDesc(menu = {"目录编制", "信息资源目录"}, button = "目录保存")
    @PostMapping("/update")
    @ApiOperation("保存")
    public Object update(@RequestBody CataInfoTempDto cata) {
        QueryWrapper<CataInfoTemp> qw=new QueryWrapper<CataInfoTemp>();
        if(cata!=null&&cata.getUviewId()!=null){
        	qw.ne(CataInfoTemp.UVIEW_ID, cata.getUviewId());
        }
        qw.and(w -> w.eq(CataInfoTemp.UVIEW_NO, cata.getUviewNo()).or().eq(CataInfoTemp.UVIEW_NM, cata.getUviewNm()).eq(CataInfoTemp.PROV_ORG_ID, cata.getProvOrgId()));
        qw.eq(CataInfoTemp.DELETED, 0);
        if (cataInfoTempService.count(qw) > 0) {
            return ResponseUtil.fail(INFO_CODE_EXIST, "信息资源名称或者代码已经存在");
        }
        Map<String,Object> map=cataInfoTempService.saveInfoTemp(cata,UserInfo.getUsername());
        if(map==null){
        	return ResponseUtil.fail(INFO_EXIST,"不能修改‘待审核’状态信息资源");
        }else{
        	logHelper.logGeneralSucceed("信息资源基本信息维护", map.get("log").toString());
        }
        return ResponseUtil.ok(map.get("uviewId"));
    }

    @RequiresPermissions("cata:infoTemp:delete")
    @RequiresPermissionsDesc(menu = {"目录编制", "信息资源目录"}, button = "目录删除")
    @PostMapping("/delete")
    @ApiOperation("目录删除")
    public Object delete(@RequestBody Integer[] ids) {
        if (ids == null) {
            return ResponseUtil.badArgument();
        }
		List<String> logList = new ArrayList<>();
		List<CataInfoTemp> tmpList= cataInfoTempService.list(new QueryWrapper<CataInfoTemp>().in(CataInfoTemp.UVIEW_ID, ids).eq(CataInfoTemp.DELETED, 0));
		if(tmpList!=null&&tmpList.size()>0){
			String error="";
			for (CataInfoTemp info : tmpList) {
				if(CataInfoTemp.AUDIT_STATUS_NO_DELETE.contains(info.getAuditStatus())){
					error+=info.getUviewNm()+",";
				}
			}
			if (error!="") {
				error=error.substring(0,error.length()-1);
				return ResponseUtil.fail(INFO_NO_DELETE, "所选信息资源中【"+error+"】状态有待审核或初审通过，不能删除");
			}
		}
		//正式表中的记录
		List<ArchBusiUviewEx> exList = archBusiUviewExService.list(new QueryWrapper<ArchBusiUviewEx>().in(ArchBusiUviewEx.UVIEW_ID, ids).eq(ArchBusiUviewEx.DELETED, 0));
		List<Integer> exIds = new ArrayList<>();
		for (int i=0;i<exList.size();i++){
			exIds.add(exList.get(i).getUviewId());
		}
		//审核记录表中的记录
		List<CataInfoApprove> exitAppList = cataInfoApproveService.list(new QueryWrapper<CataInfoApprove>().in(CataInfoApprove.INFO_ID, ids).eq(CataInfoApprove.OPT_TYPE, "删除审核").eq(CataInfoApprove.STATUS, ""));
		Map<Integer, CataInfoApprove> appMap = new HashMap<>();
		for (int i=0;i<exitAppList.size();i++){
			appMap.put(exitAppList.get(i).getInfoId(), exitAppList.get(i));
		}
		//可以删除的
		List<CataInfoTemp> deleteList = new ArrayList<>();
		if (tmpList != null && tmpList.size() > 0) {
			List<CataInfoApprove> deleteApprove = new ArrayList<>();
			for (CataInfoTemp cataInfoTemp : tmpList) {
				//曾经审核通过过
				if(exIds.contains(cataInfoTemp.getUviewId())){
					//未操作过进入到-删除记录
					if(!appMap.containsKey(cataInfoTemp.getUviewId())){
						CataInfoApprove cataInfoApprove = new CataInfoApprove();
						cataInfoApprove.setOptType("删除审核");
						cataInfoApprove.setInfoId(cataInfoTemp.getUviewId());
						cataInfoApprove.setStatus("");
						LocalDateTime localDateTime = LocalDateTime.now();
						cataInfoApprove.setCheckTime(localDateTime);
						deleteApprove.add(cataInfoApprove);
					}
					logList.add(cataInfoTemp.getUviewNo() + "/" + cataInfoTemp.getUviewNm() + "待删除审核");
				}else{
					cataInfoTemp.setDeleted(1);
					cataInfoTemp.setUpdateDt(new Date());
					deleteList.add(cataInfoTemp);
					logList.add(cataInfoTemp.getUviewNo() + "/" + cataInfoTemp.getUviewNm() + "已删除");
				}
			}
			if(deleteList.size()>0){
				cataInfoTempService.updateBatchById(deleteList);
			}
			if(deleteApprove.size()>0){
				cataInfoApproveService.saveBatch(deleteApprove);
			}
		}
//		List<ArchBusiUviewEx> infoList = archBusiUviewExService.list(new QueryWrapper<ArchBusiUviewEx>().in(ArchBusiUviewEx.UVIEW_ID, ids));
//        if(UserInfo.isDeptAdmin()){
//	        List<ArchBusiUviewEx> infoList = archBusiUviewExService.list(new QueryWrapper<ArchBusiUviewEx>().in(ArchBusiUviewEx.UVIEW_ID, ids).eq(ArchBusiUviewEx.DELETED, 0));
//	        if(infoList!=null&&infoList.size()>0){
//	        	String error="";
//		        for (ArchBusiUviewEx info : infoList) {
//		        	if(ArrayUtils.contains(ids, info.getUviewId())){
//		        		error+=info.getUviewNm()+",";
//		        	}
//		        }
//		        if (error!="") {
//		        	error=error.substring(0,error.length()-1);
//	                return ResponseUtil.fail(INFO_EXIST, "所选信息资源中【"+error+"】已有版本发布，不能删除");
//	            }
//	        }
//	        List<CataInfoTemp> list=cataInfoTempService.list(new QueryWrapper<CataInfoTemp>().in(CataInfoTemp.UVIEW_ID, ids).eq(CataInfoTemp.DELETED, 0));
//	        if(list!=null&&list.size()>0){
//		        for (CataInfoTemp cataInfoTemp : list) {
//		        	cataInfoTemp.setDeleted(1);
//					cataInfoTemp.setUpdateDt(new Date());
//		        	log+=cataInfoTemp.getUviewNo()+"/"+cataInfoTemp.getUviewNm()+"、";
//				}
//		        cataInfoTempService.updateBatchById(list);
//		        log=log.substring(0,log.length()-1);
//	        }
//        }else{
//        	log=cataInfoTempService.deleteByIds(ids);
//        }
		String log = String.join("、", logList);
        if(log!=""){
        	logHelper.logGeneralSucceed("信息资源基本信息维护", "删除信息资源【" + log + "】");
        }
        return ResponseUtil.ok(log);
    }
    
    @RequiresPermissions("cata:infoTemp:allCheck")
    @RequiresPermissionsDesc(menu = {"目录编制", "信息资源目录"}, button = "全部提交审核")
    @PostMapping("/allCheck")
    @ApiOperation("全部提交审核")
    public Object allCheck() {
        QueryWrapper<CataInfoTemp> qw=new QueryWrapper<CataInfoTemp>();
        qw.eq(CataInfoTemp.AUDIT_STATUS, "0").eq(CataInfoTemp.DELETED, 0);
        if(UserInfo.isDeptAdmin()){
        	qw.in(CataInfoTemp.PROV_ORG_ID, UserInfo.getDeptIds());
		}
		List<CataInfoTemp> infoList = cataInfoTempService.list(qw);
        if (infoList != null && infoList.size() > 0) {
        	String log="";
			List<CataInfoApprove> cataInfoApproveList = new ArrayList<>();
			for (CataInfoTemp info : infoList) {
				info.setAuditStatus("1");
				info.setUpdater(UserInfo.getUsername());
				info.setUpdateDt(new Date());
				log+=info.getUviewNm()+"、";
				CataInfoApprove.addCataInfoApprove("提交审核", cataInfoApproveList, info.getUviewId(), "", UserInfo.getId(), UserInfo.getName(), "提交审核");
			}
			cataInfoTempService.updateBatchById(infoList);
			cataInfoApproveService.saveBatch(cataInfoApproveList);
			log=log.substring(0,log.length()-1);
			logHelper.logGeneralSucceed("信息资源基本信息维护", "提交信息资源审核【" + log + "】");
		}else{
			return ResponseUtil.fail(BUS_ERROR,"当前没有状态为‘草稿’的信息资源！");
		}
        return ResponseUtil.ok();
    }
    
    @RequiresPermissions("cata:infoTemp:check")
    @RequiresPermissionsDesc(menu = {"目录编制", "信息资源目录"}, button = "提交审核")
    @PostMapping("/check")
    @ApiOperation("提交审核")
    public Object check(@RequestBody Integer[] ids) {
		if (ids == null) {
			return ResponseUtil.badArgument();
		}
		List<CataInfoTemp> infoList = cataInfoTempService
				.list(new QueryWrapper<CataInfoTemp>().in(CataInfoTemp.UVIEW_ID, ids).eq(CataInfoTemp.DELETED, 0));
		if (infoList != null && infoList.size() > 0) {
			String error = "";
			String log="";
			List<CataInfoApprove> cataInfoApproveList = new ArrayList<>();
			for (CataInfoTemp info : infoList) {
				if (!"0".equals(info.getAuditStatus())) {
					error += info.getUviewNm() + ",";
					continue;
				}
				info.setAuditStatus("1");
				info.setUpdater(UserInfo.getUsername());
				info.setUpdateDt(new Date());
				log+=info.getUviewNm()+"、";
				CataInfoApprove.addCataInfoApprove("提交审核", cataInfoApproveList, info.getUviewId(), "", UserInfo.getId(), UserInfo.getName(), "提交审核");
			}
			if (error != "") {
				error = error.substring(0, error.length() - 1);
				return ResponseUtil.fail(INFO_EXIST, "只能选择状态为‘草稿’的信息资源！ 所选信息资源中【" + error + "】不是草稿状态，不能提交");
			}
			cataInfoTempService.updateBatchById(infoList);
			if(cataInfoApproveList.size()>0){
				cataInfoApproveService.saveBatch(cataInfoApproveList);
			}
			log=log.substring(0,log.length()-1);
			logHelper.logGeneralSucceed("信息资源基本信息维护", "提交信息资源审核【" + log + "】");
		}
        return ResponseUtil.ok();
    }
    
    @RequiresPermissions("cata:infoTemp:back")
    @RequiresPermissionsDesc(menu = {"目录编制", "信息资源目录"}, button = "撤回")
    @PostMapping("/back")
    @ApiOperation("撤回")
    public Object back(@RequestBody Integer[] ids) {
		if (ids == null) {
			return ResponseUtil.badArgument();
		}
		List<CataInfoTemp> infoList = cataInfoTempService
				.list(new QueryWrapper<CataInfoTemp>().in(CataInfoTemp.UVIEW_ID, ids).eq(CataInfoTemp.DELETED, 0));
		if (infoList != null && infoList.size() > 0) {
			String error = "";
			String log="";
			for (CataInfoTemp info : infoList) {
				if (!"1".equals(info.getAuditStatus())) {
					error += info.getUviewNm() + ",";
					continue;
				}
				info.setAuditStatus("0");
				info.setUpdater(UserInfo.getUsername());
				info.setUpdateDt(new Date());
				log+=info.getUviewNm()+"、";
			}
			if (error != "") {
				error = error.substring(0, error.length() - 1);
				return ResponseUtil.fail(INFO_EXIST, "只能撤回状态为‘待初审’的信息资源！ 所选信息资源中【" + error + "】不是待初审状态，不能撤回");
			}
			cataInfoTempService.updateBatchById(infoList);
			log=log.substring(0,log.length()-1);
			logHelper.logGeneralSucceed("信息资源基本信息维护", "撤回信息资源审核【" + log + "】");
		}
        return ResponseUtil.ok();
    }

    @RequiresPermissions("admin:infoTemp:getTreeForCata")
    @RequiresPermissionsDesc(menu = {"目录编制", "信息资源目录"}, button = "左侧树")
    @GetMapping("/getTreeForCata")
    @ApiOperation("左侧树")
    public Object getTreeForCata() {
        QueryWrapper<ArchOrg> orgWrapper = new QueryWrapper<ArchOrg>();
		orgWrapper.select(ArchOrg.ORG_ID, ArchOrg.PAR_ORG_ID, ArchOrg.ORG_CD, ArchOrg.ORG_NM, ArchOrg.SOCIAL_CREDIT_CD);
		orgWrapper.isNull(ArchOrg.PAR_ORG_ID);
        if(UserInfo.getDeptIds()!=null && UserInfo.getDeptIds().size()>0){
			orgWrapper.in(ArchOrg.ORG_ID, UserInfo.getDeptIds());
        }
		orgWrapper.orderByAsc(ArchOrg.DISPALY_SN);
        List<ArchOrg> list = archOrgService.list(orgWrapper);

        QueryWrapper<DictAssetCate> dictWrapper = new QueryWrapper<DictAssetCate>();
		dictWrapper.select(DictAssetCate.TYP_ID, DictAssetCate.PAR_TYP_ID, DictAssetCate.ORG_ID, DictAssetCate.TYP_NM, DictAssetCate.TYP_CD);
		if(UserInfo.isDeptAdmin()){
			dictWrapper.and(wrapper -> wrapper.isNull(DictAssetCate.ORG_ID).or().in(DictAssetCate.ORG_ID, UserInfo.getDeptIds()));
		}
		dictWrapper.orderByAsc(DictAssetCate.TYP_ID);
		List<DictAssetCate> dictAssetCateList = dictAssetCateService.list(dictWrapper);

        DictAssetCate dictAssetCateBase = dictAssetCateList.stream().filter(item -> item.getTypNm().equals("基础类")).collect(Collectors.toList()).get(0);
		List<DictAssetCate> dictAssetCateBaseList = dictAssetCateList.stream().filter(item -> item.getParTypId()!=null && item.getParTypId().equals(dictAssetCateBase.getTypId())).collect(Collectors.toList());
		dictAssetCateBase.setChildren(dictAssetCateBaseList);

		DictAssetCate dictAssetCateTopic = dictAssetCateList.stream().filter(item -> item.getTypNm().equals("主题类")).collect(Collectors.toList()).get(0);
		List<DictAssetCate> dictAssetCateTopicList = dictAssetCateList.stream().filter(item -> item.getParTypId()!=null && item.getParTypId().equals(dictAssetCateTopic.getTypId())).collect(Collectors.toList());
		dictAssetCateTopic.setChildren(dictAssetCateTopicList);

		DictAssetCate dictAssetCateDept = dictAssetCateList.stream().filter(item -> item.getTypNm().equals("部门类")).collect(Collectors.toList()).get(0);
		List<DictAssetCate> dictAssetCateDeptList = dictAssetCateList.stream().filter(item -> item.getParTypId()!=null && item.getParTypId().equals(dictAssetCateDept.getTypId())).collect(Collectors.toList());
		dictAssetCateDept.setChildren(dictAssetCateDeptList);
        List<ArchOrgDto> dtoList=new ArrayList<ArchOrgDto>();
        for (ArchOrg archOrg : list) {
        	ArchOrgDto archOrgDto=new ArchOrgDto();
        	BeanUtils.copyProperties(archOrg, archOrgDto);

        	List<DictAssetCate> dictList=new LinkedList<DictAssetCate>();
        	dictList.add(dictAssetCateBase);
        	dictList.add(dictAssetCateTopic);

			DictAssetCate dictAssetCateDeptSelf = new DictAssetCate();
			BeanUtils.copyProperties(dictAssetCateDept, dictAssetCateDeptSelf);

        	List<DictAssetCate> dictAssetCateDeptSelfList = dictAssetCateList.stream().filter(
				item -> item.getTypType()!=null && item.getTypType().equals("目")
						&& item.getOrgId()!=null &&item.getOrgId().equals(archOrg.getOrgId())
			).collect(Collectors.toList());

			dictAssetCateDeptSelfList.forEach(
				item ->{
					List<DictAssetCate> dictAssetCateDeptSelfList2 = dictAssetCateList.stream().filter(
							item2 -> item2.getTypType()!=null && item2.getTypType().equals("细目")
									&& item2.getOrgId()!=null &&item2.getOrgId().equals(archOrg.getOrgId())
									&& item2.getParTypId()!=null && item2.getParTypId().equals(item.getTypId())
					).collect(Collectors.toList());
					item.setChildren(dictAssetCateDeptSelfList2);
				}
			);
			dictAssetCateDeptSelf.setChildren(dictAssetCateDeptSelfList);
			dictList.add(dictAssetCateDeptSelf);
        	archOrgDto.setChildren(dictList);
        	archOrgDto.setTypId(archOrgDto.getOrgId());
        	archOrgDto.setTypNm(archOrgDto.getOrgNm());
        	dtoList.add(archOrgDto);
		}
        return ResponseUtil.ok(dtoList);
    }
//	@RequiresPermissions("admin:infoTemp:getTreeForCata")
//	@RequiresPermissionsDesc(menu = {"目录编制", "信息资源目录"}, button = "左侧树")
//	@GetMapping("/getTreeForCata")
//	@ApiOperation("左侧树")
//	public Object getTreeForCata() {
//		QueryWrapper<ArchOrg> wrapper = new QueryWrapper<ArchOrg>();
//		wrapper.isNull(ArchOrg.PAR_ORG_ID);
//		if(UserInfo.isDeptAdmin()){
//			wrapper.in(ArchOrg.ORG_ID, UserInfo.getDeptIds());
//		}
//		wrapper.orderByAsc(ArchOrg.DISPALY_SN);
//		List<ArchOrg> list=archOrgService.list(wrapper);
//		QueryWrapper<DictAssetCate> qw = new QueryWrapper<DictAssetCate>();
//		qw.isNull(DictAssetCate.PAR_TYP_ID);
//		qw.eq(DictAssetCate.STATUS,"1");
//		qw.orderByAsc(DictAssetCate.DISPLAY_SN);
//		qw.orderByAsc(DictAssetCate.TYP_CD);
//		List<DictAssetCate> dictAssetCateList = dictAssetCateService.list(qw);
//		DictAssetCate dictAssetCate1=new DictAssetCate();
//		DictAssetCate dictAssetCate2=new DictAssetCate();
//		DictAssetCate dictAssetCate3=new DictAssetCate();
//		for (DictAssetCate dictAssetCate : dictAssetCateList) {
//			if("1".equals(dictAssetCate.getTypCd())){
//				dictAssetCate.setChildren(dictAssetCateService.getTreeByPidAndOrgId(dictAssetCate,null));
//				dictAssetCate1=dictAssetCate;
//			}else if("2".equals(dictAssetCate.getTypCd())){
//				dictAssetCate.setChildren(dictAssetCateService.getTreeByPidAndOrgId(dictAssetCate,null));
//				dictAssetCate2=dictAssetCate;
//			}else if("3".equals(dictAssetCate.getTypCd())){
//				dictAssetCate3=dictAssetCate;
//			}
//		}
//		List<ArchOrgDto> dtoList=new ArrayList<ArchOrgDto>();
//		for (ArchOrg archOrg : list) {
//			ArchOrgDto archOrgDto=new ArchOrgDto();
//			BeanUtils.copyProperties(archOrg, archOrgDto);
//			List<DictAssetCate> dictList=new LinkedList<DictAssetCate>();
//			dictList.add(dictAssetCate1);
//			dictList.add(dictAssetCate2);
//			List<Integer> orgIds = new ArrayList<Integer>();
//			orgIds.add(archOrg.getOrgId());
//			DictAssetCate dictAssetCate=new DictAssetCate();
//			BeanUtils.copyProperties(dictAssetCate3, dictAssetCate);
//			dictAssetCate.setChildren(dictAssetCateService.getTreeByPidAndOrgId(dictAssetCate,orgIds));
//			dictList.add(dictAssetCate);
//			archOrgDto.setChildren(dictList);
//			archOrgDto.setTypId(archOrgDto.getOrgId());
//			archOrgDto.setTypNm(archOrgDto.getOrgNm());
//			dtoList.add(archOrgDto);
//		}
//		return ResponseUtil.ok(dtoList);
//	}
	@RequiresPermissions("cata:infoTemp:updateLimit")
	@RequiresPermissionsDesc(menu = {"目录编制", "更新预警"}, button = "已读")
	@GetMapping("/updateLimit")
	@ApiOperation("已读")
	public Object updateLimit(@RequestParam(required=true) Integer id) {
		return ResponseUtil.ok(cataInfoTempService.updateLimitReadedStatus(id));
	}
	@RequiresPermissions("cata:infoTemp:updateLimitAll")
	@RequiresPermissionsDesc(menu = {"目录编制", "更新预警"}, button = "全部已读")
	@GetMapping("/updateLimitAll")
	@ApiOperation("全部已读")
	public Object updateLimitAll(String uviewNo,String uviewNm,Integer orgId, String updateCyc) {
		return ResponseUtil.ok(cataInfoTempService.updateLimitReadedStatusAll(uviewNo, uviewNm, orgId, updateCyc));
	}
}
