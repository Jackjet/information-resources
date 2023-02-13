package com.digitalchina.resourcecatalog.admin.web;


import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalchina.resourcecatalog.admin.annotation.RequiresPermissionsDesc;
import com.digitalchina.resourcecatalog.core.util.ResponseUtil;
import com.digitalchina.resourcecatalog.db.domain.CataInfoItemHistoryRel;
import com.digitalchina.resourcecatalog.db.service.CataInfoItemHistoryRelService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 信息项(历史记录表) 前端控制器
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
@RestController
@RequestMapping("/admin/cataInfoItemHistoryRel")
@Validated
@Api(value = "信息项(历史版本)", tags = "信息项(历史版本)")
public class CataInfoItemHistoryRelController {
	@Autowired
	private CataInfoItemHistoryRelService cataInfoItemHistoryRelService;
	
	@RequiresPermissions("cata:infoItemHistoryRel:list")
	@RequiresPermissionsDesc(menu = { "目录编制", "信息资源目录" }, button = "历史版本信息项列表查询")
	@GetMapping("/list")
	@ApiOperation("历史版本信息项列表查询")
	public Object list(String srcField, String engCd,@RequestParam(required=true) Integer uviewId,String srcFieldAndEngCd,
			@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
		Page page1 = new Page<>(page, limit);
		QueryWrapper<CataInfoItemHistoryRel> qw=new QueryWrapper<CataInfoItemHistoryRel>();
		if(!StringUtils.isEmpty(srcFieldAndEngCd)){
			qw.and(w -> w.like(CataInfoItemHistoryRel.ENG_CD, srcFieldAndEngCd).or().like(CataInfoItemHistoryRel.SRC_FIELD, srcFieldAndEngCd));
		}else{
			if(!StringUtils.isEmpty(srcField)){
				qw.like(CataInfoItemHistoryRel.SRC_FIELD, srcField);
			}
			if(!StringUtils.isEmpty(engCd)){
				qw.like(CataInfoItemHistoryRel.ENG_CD, engCd);
			}
		}
		qw.eq(CataInfoItemHistoryRel.UVIEW_ID, uviewId);
		qw.orderByAsc(CataInfoItemHistoryRel.SNO);
		return ResponseUtil.ok(cataInfoItemHistoryRelService.page(page1, qw));
	}
}
