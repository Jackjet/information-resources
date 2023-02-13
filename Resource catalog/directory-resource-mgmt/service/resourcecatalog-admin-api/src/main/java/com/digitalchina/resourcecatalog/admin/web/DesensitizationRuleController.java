package com.digitalchina.resourcecatalog.admin.web;

import java.util.Date;

import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.digitalchina.resourcecatalog.db.domain.ArchBusiUviewEx;
import com.digitalchina.resourcecatalog.db.domain.ArchBusiUviewStrEx;
import com.digitalchina.resourcecatalog.db.domain.DesensitizationRule;
import com.digitalchina.resourcecatalog.db.service.ArchBusiUviewExService;
import com.digitalchina.resourcecatalog.db.service.ArchBusiUviewStrExService;
import com.digitalchina.resourcecatalog.db.service.DesensitizationRuleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author 作者 zhaoyan:
 * @version 创建时间：2020年6月17日
 * 
 */
@RestController
@RequestMapping("/admin/desensitizationRule")
@Api(value = "信息项（正式表）脱敏规则保存", tags = "信息项（正式表）脱敏规则保存")
public class DesensitizationRuleController {

	@Autowired
	private LogHelper logHelper;

	@Autowired
	private DesensitizationRuleService desensitizationRuleService;
	
	@Autowired
	private ArchBusiUviewStrExService archBusiUviewStrExService;
	
	@Autowired
	private ArchBusiUviewExService archBusiUviewExService;

	/**
	 * 信息项（正式表）脱敏规则增加或更新
	 * 
	 * 
	 * */

	@PostMapping("/addOrUpdate")
	@RequiresPermissions("admin:desensitizationRule:addOrUpdate")
	@RequiresPermissionsDesc(menu = { "目录编制", "信息资源目录" }, button = "脱敏规则保存")
	@ApiOperation("添加或更新")
	public Object addOrUpdate(
			@Valid @RequestBody DesensitizationRule desensitizationRule) {

		ArchBusiUviewStrEx archBusiUviewStrEx = archBusiUviewStrExService.getById(desensitizationRule.getItemId());
		if(null == archBusiUviewStrEx) {
			return ResponseUtil.fail("信息项关联有误！");
		}
		ArchBusiUviewEx archBusiUviewEx = archBusiUviewExService.getById(archBusiUviewStrEx.getUviewId());
		if(archBusiUviewEx == null) {
			return ResponseUtil.fail("信息资源目录关联有误！");
		}
		if(archBusiUviewEx.getAuditStatus().equals("1")) {
			 return ResponseUtil.fail("信息资源目录为待审核，不能修改！");
		}
		DesensitizationRule rule = desensitizationRuleService
				.getInfo(desensitizationRule.getItemId());
		if (null != rule) {
			desensitizationRule.setUpdateBy(UserInfo.getUsername());
			desensitizationRule.setUpdateTime(new Date());
			desensitizationRule.setDeleted(0);

			desensitizationRuleService.update(desensitizationRule);
			logHelper.logOrgSucceed("编辑信息项（正式表）脱敏规则", desensitizationRule.getId()
					.toString(), desensitizationRule.getItemId().toString());
		} else {

			desensitizationRule.setCreateBy(UserInfo.getUsername());
			desensitizationRule.setCreateTime(new Date());
			desensitizationRule.setDeleted(0);
			desensitizationRuleService.add(desensitizationRule);
			logHelper.logOrgSucceed("新增信息项（正式表）脱敏规则", desensitizationRule.getId()
					.toString(), desensitizationRule.getItemId().toString());

		}
		if(archBusiUviewEx.getAuditStatus().equals("2") || archBusiUviewEx.getAuditStatus().equals("3")) {
			archBusiUviewEx.setAuditStatus("0");
			archBusiUviewExService.updateById(archBusiUviewEx);
		}
		return ResponseUtil.ok();
	}

	/**
	 * 信息项（正式表）脱敏规则详情
	 * 
	 * 
	 * */
	@GetMapping("/info")
	@RequiresPermissions("admin:desensitizationRule:info")
	@RequiresPermissionsDesc(menu = { "目录编制", "信息资源目录" }, button = "脱敏规则详情")
	@ApiOperation("获取详情")
	public Object info(@RequestParam Integer itemId) {
		DesensitizationRule desensitizationRule = desensitizationRuleService
				.getInfo(itemId);
		return ResponseUtil.ok(desensitizationRule);
	}

	/**
	 * 信息项（正式表）脱敏规则移除
	 * 
	 * 
	 * */
	@GetMapping("/delete")
	@RequiresPermissions("admin:desensitizationRule:delete")
	@RequiresPermissionsDesc(menu = { "目录编制", "信息资源目录" }, button = "脱敏规则移除")
	@ApiOperation("删除脱敏")
	public Object delete(@RequestParam Integer id) {
		DesensitizationRule desensitizationRule = desensitizationRuleService.getById(id);
		ArchBusiUviewStrEx archBusiUviewStrEx = archBusiUviewStrExService.getById(desensitizationRule.getItemId());
		if(null == archBusiUviewStrEx) {
			return ResponseUtil.fail("信息项关联有误！");
		}
		ArchBusiUviewEx archBusiUviewEx = archBusiUviewExService.getById(archBusiUviewStrEx.getUviewId());
		if(archBusiUviewEx == null) {
			return ResponseUtil.fail("信息资源目录关联有误！");
		}
		if(archBusiUviewEx.getAuditStatus().equals("1")) {
			 return ResponseUtil.fail("信息资源目录为待审核，不能修改！");
		}
		desensitizationRule.setDeleted(1);
		desensitizationRuleService.update(desensitizationRule);
		logHelper.logOrgSucceed("移除信息项（正式表）脱敏规则", desensitizationRule.getId()
				.toString(), "0");
		if(archBusiUviewEx.getAuditStatus().equals("2")|| archBusiUviewEx.getAuditStatus().equals("3")) {
			archBusiUviewEx.setAuditStatus("0");
			archBusiUviewExService.updateById(archBusiUviewEx);
		}
		return ResponseUtil.ok();
	}

}
