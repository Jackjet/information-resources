package com.digitalchina.resourcecatalog.admin.web;

import static com.digitalchina.resourcecatalog.admin.util.AdminResponseCode.BUS_ERROR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.digitalchina.resourcecatalog.db.domain.*;
import com.digitalchina.resourcecatalog.db.service.*;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalchina.resourcecatalog.admin.annotation.RequiresPermissionsDesc;
import com.digitalchina.resourcecatalog.admin.service.LogHelper;
import com.digitalchina.resourcecatalog.admin.util.UserInfo;
import com.digitalchina.resourcecatalog.core.util.ResponseUtil;
import com.digitalchina.resourcecatalog.db.dto.CataInfoHistoryDto;
import com.digitalchina.resourcecatalog.db.dto.CataInfoTempTypeRelDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 信息资源目录历史信息记录 前端控制器
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
@RestController
@RequestMapping("/admin/cataInfoHistory")
@Validated
@Api(value = "信息资源目录历史信息记录", tags = "信息资源目录历史信息记录")
public class CataInfoHistoryController {
	@Autowired
	private CataInfoHistoryService cataInfoHistoryService;
	@Autowired
	private CataInfoHistoryTypeRelService cataInfoHistoryTypeRelService;
	@Autowired
	private SysDictService sysDictService;
	@Autowired
	private ArchOrgService archOrgService;
	@Autowired
	private DictAssetCateService dictAssetCateService;
	@Autowired
	private CataBusInfoRelService cataBusInfoRelService;
    @Autowired
    private LogHelper logHelper;

	@RequiresPermissions("cata:infoHistory:list")
	@RequiresPermissionsDesc(menu = { "目录编制", "信息资源目录" }, button = "版本列表查询")
	@GetMapping("/list")
	@ApiOperation("版本列表查询")
	public Object list(@RequestParam(required=true)Integer uviewId,
			@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
		Page page1 = new Page<>(page, limit);
		return ResponseUtil.ok(cataInfoHistoryService.page(page1,new QueryWrapper<CataInfoHistory>().eq(CataInfoHistory.UVIEW_ID, uviewId).eq(CataInfoHistory.DELETED, 0).orderByDesc(CataInfoHistory.VERSION)));
	}

	@RequiresPermissions("cata:infoHistory:read")
	@RequiresPermissionsDesc(menu = { "目录编制", "信息资源目录" }, button = "版本详情")
	@GetMapping("/read")
	@ApiOperation("版本详情")
	public Object read(@RequestParam(required=true) Integer id) {
		CataInfoHistory cata = cataInfoHistoryService
				.getOne(new QueryWrapper<CataInfoHistory>().eq(CataInfoHistory.ID, id).eq(CataInfoHistory.DELETED, 0));
		CataInfoHistoryDto cataDto = new CataInfoHistoryDto();
		if (cata != null) {
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
	        List<CataInfoHistoryTypeRel> typeList=cataInfoHistoryTypeRelService.list(new QueryWrapper<CataInfoHistoryTypeRel>().eq(CataInfoHistoryTypeRel.INFO_ID, id));
	        List<CataInfoTempTypeRelDto> dtoTypeList=new ArrayList<CataInfoTempTypeRelDto>();
	        List<DictAssetCate> dictAssetCateList=dictAssetCateService.list(new QueryWrapper<DictAssetCate>().eq(DictAssetCate.STATUS, "1"));
	        for (DictAssetCate dictAssetCate : dictAssetCateList) {
	        	for (CataInfoHistoryTypeRel cataInfoTempTypeRel : typeList) {
	        		if(dictAssetCate.getTypId().equals(cataInfoTempTypeRel.getTypeId())){
	        			CataInfoTempTypeRelDto dto=new CataInfoTempTypeRelDto();
	        			BeanUtils.copyProperties(cataInfoTempTypeRel, dto);
	        			dto.setTypeName(dictAssetCate.getTypNm());
	        			dtoTypeList.add(dto);
	        		}
				}
			}
	        cataDto.setCataInfoHistoryTypeRelDtoList(dtoTypeList);
			//权责清单
//			List<CataBusInfoRel> cataBusInfoRelList =cataBusInfoRelService.list(new QueryWrapper<CataBusInfoRel>().eq(CataBusInfoRel.INFO_ID, id));
//			List<Integer> relBusList = new ArrayList<>();
//			cataBusInfoRelList.forEach(item -> relBusList.add(item.getBusId()));
//			cataDto.setRelBusList(relBusList);
        }
        return ResponseUtil.ok(cataDto);
	}
	
	@RequiresPermissions("cata:infoHistory:delete")
	@RequiresPermissionsDesc(menu = { "目录编制", "信息资源目录" }, button = "版本删除")
	@PostMapping("/delete")
	@ApiOperation("版本删除")
	public Object delete(@RequestBody Integer[] ids) {
		if (ids == null) {
			return ResponseUtil.badArgument();
		}
		List<CataInfoHistory> infoList = cataInfoHistoryService.list(new QueryWrapper<CataInfoHistory>()
				.in(CataInfoHistory.ID, ids).eq(CataInfoHistory.DELETED, 0));
		if (infoList != null && infoList.size() > 0) {
			String log="";
			for (CataInfoHistory info : infoList) {
				if ("1".equals(info.getIsCurrent())) {
					return ResponseUtil.fail(BUS_ERROR, "当前版本不能删除");
				}
				info.setDeleted(1);
				log+=info.getUviewNm()+"/"+info.getVersion()+"、";
			}
			cataInfoHistoryService.updateBatchById(infoList);
			logHelper.logGeneralSucceed("版本管理", "删除信息资源版本【" + log + "】");
		}
		return ResponseUtil.ok();
	}
	
	@RequiresPermissions("cata:infoHistory:rollback")
	@RequiresPermissionsDesc(menu = { "目录编制", "信息资源目录" }, button = "版本恢复")
	@GetMapping("/rollback")
	@ApiOperation("版本恢复")
	public Object rollback(@RequestParam(required=true) Integer id) {
		CataInfoHistory cata=cataInfoHistoryService.rollback(id,UserInfo.getUsername());
		if(cata!=null){
			logHelper.logGeneralSucceed("版本管理", "信息资源版本替换【" + cata.getUviewNm() + "/"+ cata.getVersion()+ "】");
			return ResponseUtil.ok();
		}
		return ResponseUtil.fail(BUS_ERROR, "恢复失败");
	}
	
	@RequiresPermissions("cata:infoHistory:compare")
	@RequiresPermissionsDesc(menu = { "目录编制", "信息资源目录" }, button = "版本比较")
	@GetMapping("/compare")
	@ApiOperation("版本比较")
	public Object compare(@RequestParam(required=true) Integer id) {
		return ResponseUtil.ok(cataInfoHistoryService.compare(id));
	}
	
}
