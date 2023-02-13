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
import com.digitalchina.resourcecatalog.db.domain.DesensitizationRuleHistory;
import com.digitalchina.resourcecatalog.db.service.DesensitizationRuleHistoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 信息项（历史表）脱敏规则表 前端控制器
 * </p>
 *
 * @author zhaoyan
 * @since 2020-06-18
 */
@RestController
@RequestMapping("/desensitizationRuleHistory")
@Api(value = "信息项（历史表）脱敏规则保存", tags = "信息项（历史表）脱敏规则保存")
public class DesensitizationRuleHistoryController {

//	@Autowired
//	private LogHelper logHelper;
//
//	@Autowired
//	private DesensitizationRuleHistoryService desensitizationRuleHistoryService;
//
//	/**
//	 * 信息项（历史表）脱敏规则增加或更新
//	 * 
//	 * 
//	 * */
//
//	@PostMapping("/addOrUpdate")
//	@RequiresPermissions("admin:desensitizationRule:addOrUpdate")
//	@RequiresPermissionsDesc(menu = { "目录编制", "信息资源目录" }, button = "脱敏规则保存")
//	@ApiOperation("添加或更新")
//	public Object addOrUpdate(
//			@Valid @RequestBody DesensitizationRuleHistory desensitizationRuleHistory) {
//
//		DesensitizationRuleHistory ruleHistory = desensitizationRuleHistoryService
//				.getInfo(desensitizationRuleHistory.getItemId());
//		if (null != ruleHistory) {
//			desensitizationRuleHistory.setUpdateBy(UserInfo.getUsername());
//			desensitizationRuleHistory.setUpdateTime(new Date());
//			desensitizationRuleHistory.setDeleted(0);
//
//			desensitizationRuleHistoryService.update(desensitizationRuleHistory);
//			logHelper.logOrgSucceed("编辑信息项（历史表）脱敏规则", desensitizationRuleHistory.getId()
//					.toString(), desensitizationRuleHistory.getItemId().toString());
//		} else {
//
//			desensitizationRuleHistory.setCreateBy(UserInfo.getUsername());
//			desensitizationRuleHistory.setCreateTime(new Date());
//			desensitizationRuleHistory.setDeleted(0);
//			desensitizationRuleHistoryService.add(desensitizationRuleHistory);
//			logHelper.logOrgSucceed("新增信息项（历史表）脱敏规则", desensitizationRuleHistory.getId()
//					.toString(), desensitizationRuleHistory.getItemId().toString());
//
//		}
//
//		return ResponseUtil.ok();
//	}
//
//	/**
//	 * 脱敏规则详情
//	 * 
//	 * 
//	 * */
//	@GetMapping("/info")
//	@RequiresPermissions("admin:desensitizationRule:info")
//	@RequiresPermissionsDesc(menu = { "目录编制", "信息资源目录" }, button = "脱敏规则详情")
//	@ApiOperation("获取详情")
//	public Object info(@RequestParam Integer itemId) {
//		DesensitizationRuleHistory desensitizationRuleHistory = desensitizationRuleHistoryService
//				.getInfo(itemId);
//		return ResponseUtil.ok(desensitizationRuleHistory);
//	}
//
//	/**
//	 * 脱敏规则移除
//	 * 
//	 * 
//	 * */
//	@GetMapping("/delete")
//	@RequiresPermissions("admin:desensitizationRule:delete")
//	@RequiresPermissionsDesc(menu = { "目录编制", "信息资源目录" }, button = "脱敏规则移除")
//	@ApiOperation("删除脱敏")
//	public Object delete(@RequestParam Integer id) {
//		DesensitizationRuleHistory desensitizationRuleHistory = new DesensitizationRuleHistory();
//		desensitizationRuleHistory.setId(id);
//		desensitizationRuleHistory.setDeleted(1);
//		desensitizationRuleHistoryService.update(desensitizationRuleHistory);
//		logHelper.logOrgSucceed("移除信息项（历史表）脱敏规则", desensitizationRuleHistory.getId()
//				.toString(), "0");
//		return ResponseUtil.ok();
//	}

}

