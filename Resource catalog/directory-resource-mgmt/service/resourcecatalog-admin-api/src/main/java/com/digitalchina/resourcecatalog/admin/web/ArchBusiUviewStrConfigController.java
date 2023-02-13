package com.digitalchina.resourcecatalog.admin.web;


import static com.digitalchina.resourcecatalog.admin.util.AdminResponseCode.BUS_ERROR;
import static com.digitalchina.resourcecatalog.admin.util.AdminResponseCode.INFO_CODE_EXIST;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.digitalchina.resourcecatalog.db.domain.ArchBusiUviewStrConfig;
import com.digitalchina.resourcecatalog.db.domain.CataInfoItemTempRel;
import com.digitalchina.resourcecatalog.db.domain.SysDict;
import com.digitalchina.resourcecatalog.db.service.ArchBusiUviewStrConfigService;
import com.digitalchina.resourcecatalog.db.service.SysDictService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 信息项属性表 前端控制器
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
@RestController
@RequestMapping("/admin/archBusiUviewStrConfig")
@Validated
@Api(value = "信息项管理", tags = "信息项管理")
public class ArchBusiUviewStrConfigController {
	@Autowired
	private ArchBusiUviewStrConfigService archBusiUviewStrConfigService;
    @Autowired
    private SysDictService sysDictService;
    @Autowired
    private LogHelper logHelper;
	
//	@RequiresPermissions("cata:archBusiUviewStrConfig:list")
//	@RequiresPermissionsDesc(menu = { "系统管理", "信息项管理" }, button = "信息项属性列表查询")
    @RequiresAuthentication
	@GetMapping("/list")
	@ApiOperation("信息项属性列表查询")
	public Object list(String itemName, String itemMark,String itemNameAndItemMark,
			@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
		Page page1 = new Page<>(page, limit);
		QueryWrapper<ArchBusiUviewStrConfig> qw=new QueryWrapper<ArchBusiUviewStrConfig>();
		if(!StringUtils.isEmpty(itemNameAndItemMark)){
			qw.and(w -> w.like(ArchBusiUviewStrConfig.ITEM_NAME, itemNameAndItemMark).or().like(ArchBusiUviewStrConfig.ITEM_MARK, itemNameAndItemMark));
		}else{
			if(!StringUtils.isEmpty(itemName)){
				qw.like(ArchBusiUviewStrConfig.ITEM_NAME, itemName);
			}
			if(!StringUtils.isEmpty(itemMark)){
				qw.like(ArchBusiUviewStrConfig.ITEM_MARK, itemMark);
			}
		}
		qw.orderByAsc(ArchBusiUviewStrConfig.ORDER_NUM);
		return ResponseUtil.ok(archBusiUviewStrConfigService.page(page1, qw));
	}
	
	@RequiresPermissions("cata:archBusiUviewStrConfig:getOrderNum")
	@RequiresPermissionsDesc(menu = { "系统管理", "信息项管理" }, button = "查询信息项显示序号")
	@GetMapping("/getOrderNum")
	@ApiOperation("查询信息项显示序号")
	public Object getOrderNum() {
		QueryWrapper<ArchBusiUviewStrConfig> qw=new QueryWrapper<ArchBusiUviewStrConfig>();
		qw.orderByDesc(ArchBusiUviewStrConfig.ORDER_NUM);
		ArchBusiUviewStrConfig archBusiUviewStrConfig=archBusiUviewStrConfigService.getOne(qw);
		if(archBusiUviewStrConfig!=null){
			if(archBusiUviewStrConfig.getOrderNum()!=null){
				return ResponseUtil.ok(archBusiUviewStrConfig.getOrderNum()+1);
			}
		}
		return ResponseUtil.ok(1);
	}
	
	@RequiresPermissions("cata:archBusiUviewStrConfig:save")
    @RequiresPermissionsDesc(menu = {"系统管理", "信息项管理"}, button = "信息项属性新增")
    @PostMapping("/save")
    @ApiOperation("信息项属性新增")
    public Object save(@RequestBody ArchBusiUviewStrConfig cata) {
        QueryWrapper<ArchBusiUviewStrConfig> qw=new QueryWrapper<ArchBusiUviewStrConfig>();
        if(archBusiUviewStrConfigService.count(qw)>25){
        	return ResponseUtil.fail(BUS_ERROR,"最多只能维护26个信息项！");
        }
        if(cata!=null){
        	qw.eq(ArchBusiUviewStrConfig.ITEM_MARK, cata.getItemMark());
        	cata.setCreateTime(new Date());
        	cata.setCreateBy(UserInfo.getUsername());
        }
        if (archBusiUviewStrConfigService.count(qw) > 0) {
            return ResponseUtil.fail(INFO_CODE_EXIST, "信息项编码已存在！");
        }
		if (cata.getItemCode() != null
				&& sysDictService.count(new QueryWrapper<SysDict>().eq(SysDict.TYPE, cata.getItemCode())) == 0) {
			return ResponseUtil.fail(INFO_CODE_EXIST, "数据编码不存在！");
		}
        archBusiUviewStrConfigService.save(cata);
        logHelper.logGeneralSucceed("信息项", "新增信息项元数据【" + cata.getItemName()+"/"+cata.getItemMark() + "】");
        return ResponseUtil.ok();
    }
	
	@RequiresPermissions("cata:archBusiUviewStrConfig:update")
    @RequiresPermissionsDesc(menu = {"系统管理", "信息项管理"}, button = "信息项属性修改")
    @PostMapping("/update")
    @ApiOperation("信息项属性修改")
    public Object update(@RequestBody ArchBusiUviewStrConfig cata) {
		QueryWrapper<ArchBusiUviewStrConfig> qw=new QueryWrapper<ArchBusiUviewStrConfig>();
		if(cata!=null){
			qw.ne(ArchBusiUviewStrConfig.ID, cata.getId());
			qw.eq(ArchBusiUviewStrConfig.ITEM_MARK, cata.getItemMark());
        	cata.setUpdateBy(UserInfo.getUsername());
        	cata.setUpdateTime(new Date());
        }
        if (archBusiUviewStrConfigService.count(qw) > 0) {
            return ResponseUtil.fail(INFO_CODE_EXIST, "信息项编码已存在！");
        }
		if (cata.getItemCode() != null
				&& sysDictService.count(new QueryWrapper<SysDict>().eq(SysDict.TYPE, cata.getItemCode())) == 0) {
			return ResponseUtil.fail(INFO_CODE_EXIST, "数据编码不存在！");
		}
        archBusiUviewStrConfigService.updateById(cata);
        logHelper.logGeneralSucceed("信息项", "编辑信息项元数据【" + cata.getItemName()+"/"+cata.getItemMark() + "】");
        return ResponseUtil.ok();
    }

    @RequiresPermissions("cata:archBusiUviewStrConfig:delete")
    @RequiresPermissionsDesc(menu = {"系统管理", "信息项管理"}, button = "信息项属性删除")
    @PostMapping("/delete")
    @ApiOperation("信息项属性删除")
    public Object delete(@RequestBody Integer[] ids) {
        if (ids == null) {
            return ResponseUtil.badArgument();
        }
        List<ArchBusiUviewStrConfig> list=archBusiUviewStrConfigService.list(new QueryWrapper<ArchBusiUviewStrConfig>().in(ArchBusiUviewStrConfig.ID, ids));
        if(!archBusiUviewStrConfigService.removeByIds(Arrays.asList(ids))){
        	return ResponseUtil.fail(BUS_ERROR,"删除失败");
        }
        String log="";
        for (ArchBusiUviewStrConfig archBusiUviewStrConfig : list) {
        	log+=archBusiUviewStrConfig.getItemName()+"/"+archBusiUviewStrConfig.getItemMark()+"、";
		}
        log=log.substring(0,log.length()-1);
        logHelper.logGeneralSucceed("信息项", "删除信息项元数据【" + log + "】");
        return ResponseUtil.ok();
    }
    
    @RequiresPermissions("cata:archBusiUviewStrConfig:read")
    @RequiresPermissionsDesc(menu = {"系统管理", "信息项管理"}, button = "信息项属性详情")
    @GetMapping("/read")
    @ApiOperation("信息项属性详情")
    public Object read(@RequestParam(required=true) Integer id) {
    	ArchBusiUviewStrConfig cata = archBusiUviewStrConfigService.getById(id);
        return ResponseUtil.ok(cata);
    }
}
