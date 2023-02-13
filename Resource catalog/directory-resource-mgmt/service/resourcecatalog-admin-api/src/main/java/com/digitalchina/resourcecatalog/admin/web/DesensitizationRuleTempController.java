package com.digitalchina.resourcecatalog.admin.web;


import java.util.Date;

import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.digitalchina.resourcecatalog.admin.annotation.RequiresPermissionsDesc;
import com.digitalchina.resourcecatalog.admin.service.LogHelper;
import com.digitalchina.resourcecatalog.admin.util.UserInfo;
import com.digitalchina.resourcecatalog.core.util.ResponseUtil;
import com.digitalchina.resourcecatalog.db.domain.CataInfoItemTempRel;
import com.digitalchina.resourcecatalog.db.domain.CataInfoTemp;
import com.digitalchina.resourcecatalog.db.domain.DesensitizationRuleTemp;
import com.digitalchina.resourcecatalog.db.service.CataInfoItemTempRelService;
import com.digitalchina.resourcecatalog.db.service.CataInfoTempService;
import com.digitalchina.resourcecatalog.db.service.DesensitizationRuleTempService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 信息项（暂存表）脱敏规则表 前端控制器
 * </p>
 *
 * @author zhaoyan
 * @since 2020-06-18
 */
@RestController
@RequestMapping("/admin/desensitizationRuleTemp")
@Api(value = "信息项（暂存表）脱敏规则保存", tags = "信息项（暂存表）脱敏规则保存")
public class DesensitizationRuleTempController {

	@Autowired
	private LogHelper logHelper;
	
	@Autowired
	private DesensitizationRuleTempService desensitizationRuleTempService;
	
	@Autowired
	private CataInfoTempService cataInfoTempService;
	
	@Autowired
	private CataInfoItemTempRelService cataInfoItemTempRelService;
	/**
	 * 信息项（暂存表）脱敏规则增加或更新
	 * 
	 * 
	 * */
	
	@PostMapping("/addOrUpdate")
	@RequiresPermissions("admin:desensitizationRuleTemp:addOrUpdate")
	@RequiresPermissionsDesc(menu = { "目录编制", "信息资源目录" }, button = "信息项（暂存表）脱敏规则保存")
	@ApiOperation("添加或更新")
	public Object addOrUpdate(
			@Valid @RequestBody DesensitizationRuleTemp desensitizationRuleTemp) {
		CataInfoItemTempRel cataInfoItemTempRel = cataInfoItemTempRelService.getById(desensitizationRuleTemp.getItemId());
		if(null == cataInfoItemTempRel) {
			return ResponseUtil.fail("信息项关联有误！");
		}
		CataInfoTemp cataInfoTemp = cataInfoTempService.getById(cataInfoItemTempRel.getUviewId());
		if(null == cataInfoTemp) {
			return ResponseUtil.fail("信息资源目录关联有误！");
		}
		if(cataInfoTemp.getAuditStatus() == "1") {
			return ResponseUtil.fail("信息资源目录为待审核，不能修改！");
		}
		DesensitizationRuleTemp ruleTemp = desensitizationRuleTempService
				.getInfo(desensitizationRuleTemp.getItemId());
		if (null != ruleTemp) {
			desensitizationRuleTemp.setUpdateBy(UserInfo.getUsername());
			desensitizationRuleTemp.setUpdateTime(new Date());
			desensitizationRuleTemp.setDeleted(0);
	
			desensitizationRuleTempService.update(desensitizationRuleTemp);
			logHelper.logOrgSucceed("编辑信息项（暂存表）脱敏规则", desensitizationRuleTemp.getId()
					.toString(), desensitizationRuleTemp.getItemId().toString());
		} else {
	
			desensitizationRuleTemp.setCreateBy(UserInfo.getUsername());
			desensitizationRuleTemp.setCreateTime(new Date());
			desensitizationRuleTemp.setDeleted(0);
			desensitizationRuleTempService.add(desensitizationRuleTemp);
			logHelper.logOrgSucceed("新增信息项（暂存表）脱敏规则", desensitizationRuleTemp.getId()
					.toString(), desensitizationRuleTemp.getItemId().toString());
	
		}
		if(cataInfoTemp.getAuditStatus() == "2" || cataInfoTemp.getAuditStatus() == "3" ) {
			cataInfoTemp.setAuditStatus("0");
			cataInfoTempService.updateById(cataInfoTemp);
		}
		return ResponseUtil.ok();
	}
	
	/**
	 * 信息项（暂存表）脱敏规则详情
	 * 
	 * 
	 * */
	@GetMapping("/info")
	@RequiresPermissions("admin:desensitizationRuleTemp:info")
	@RequiresPermissionsDesc(menu = { "目录编制", "信息资源目录" }, button = "信息项（暂存表）脱敏规则详情")
	@ApiOperation("获取详情")
	public Object info(@RequestParam Integer itemId) {
		DesensitizationRuleTemp desensitizationRuleTemp = desensitizationRuleTempService
				.getInfo(itemId);
		return ResponseUtil.ok(desensitizationRuleTemp);
	}
	
	/**
	 * 信息项（暂存表）脱敏规则移除
	 * 
	 * 
	 * */
	@GetMapping("/delete")
	@RequiresPermissions("admin:desensitizationRuleTemp:delete")
	@RequiresPermissionsDesc(menu = { "目录编制", "信息资源目录" }, button = "信息项（暂存表）脱敏规则移除")
	@ApiOperation("删除脱敏")
	public Object delete(@RequestParam Integer id) {
		DesensitizationRuleTemp desensitizationRuleTemp = desensitizationRuleTempService.getById(id);
		CataInfoItemTempRel cataInfoItemTempRel = cataInfoItemTempRelService.getById(desensitizationRuleTemp.getItemId());
		if(null == cataInfoItemTempRel) {
			return ResponseUtil.fail("信息项关联有误！");
		}
		CataInfoTemp cataInfoTemp = cataInfoTempService.getById(cataInfoItemTempRel.getUviewId());
		if(null == cataInfoTemp) {
			return ResponseUtil.fail("信息资源目录关联有误！");
		}
		if(cataInfoTemp.getAuditStatus() == "1") {
			return ResponseUtil.fail("信息资源目录为待审核，不能修改！");
		}
		desensitizationRuleTemp.setId(id);
		desensitizationRuleTemp.setDeleted(1);
		desensitizationRuleTempService.update(desensitizationRuleTemp);
		logHelper.logOrgSucceed("移除信息项（暂存表）脱敏规则", desensitizationRuleTemp.getId()
				.toString(), "0");
		
		if(cataInfoTemp.getAuditStatus() == "2" || cataInfoTemp.getAuditStatus() == "3" ) {
			cataInfoTemp.setAuditStatus("0");
			cataInfoTempService.updateById(cataInfoTemp);
		}
		return ResponseUtil.ok();
	}
	

}

