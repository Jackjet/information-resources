package com.digitalchina.resourcecatalog.admin.web;


import static com.digitalchina.resourcecatalog.admin.util.AdminResponseCode.INFO_CODE_EXIST;
import static com.digitalchina.resourcecatalog.admin.util.AdminResponseCode.BUS_ERROR;

import java.util.Date;
import java.util.List;

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
import com.digitalchina.resourcecatalog.core.util.ResponseUtil;
import com.digitalchina.resourcecatalog.db.domain.CataInfoItemTempRel;
import com.digitalchina.resourcecatalog.db.domain.CataInfoTemp;
import com.digitalchina.resourcecatalog.db.service.CataInfoItemTempRelService;
import com.digitalchina.resourcecatalog.db.service.CataInfoTempService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 信息项(暂存表) 前端控制器
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
@RestController
@RequestMapping("/admin/cataInfoItemTempRel")
@Validated
@Api(value = "信息项(暂存)", tags = "信息项(暂存)")
public class CataInfoItemTempRelController {
	@Autowired
	private CataInfoItemTempRelService cataInfoItemTempRelService;
	
    @Autowired
    private CataInfoTempService cataInfoTempService;
    
    @Autowired
    private LogHelper logHelper;

	@RequiresPermissions("cata:infoItemTemp:list")
	@RequiresPermissionsDesc(menu = { "目录编制", "信息资源目录" }, button = "暂存信息项列表查询")
	@GetMapping("/list")
	@ApiOperation("暂存信息项列表查询")
	public Object list(String srcField, String engCd,@RequestParam(required=true) Integer uviewId,String srcFieldAndEngCd,
			@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
		Page page1 = new Page<>(page, limit);
		QueryWrapper<CataInfoItemTempRel> qw=new QueryWrapper<CataInfoItemTempRel>();
		if(!StringUtils.isEmpty(srcFieldAndEngCd)){
			qw.and(w -> w.like(CataInfoItemTempRel.ENG_CD, srcFieldAndEngCd).or().like(CataInfoItemTempRel.SRC_FIELD, srcFieldAndEngCd));
		}else{
			if(!StringUtils.isEmpty(srcField)){
				qw.like(CataInfoItemTempRel.SRC_FIELD, srcField);
			}
			if(!StringUtils.isEmpty(engCd)){
				qw.like(CataInfoItemTempRel.ENG_CD, engCd);
			}
		}
		qw.eq(CataInfoItemTempRel.UVIEW_ID, uviewId);
		qw.orderByAsc(CataInfoItemTempRel.SNO);
		return ResponseUtil.ok(cataInfoItemTempRelService.page(page1, qw));
	}
	
	@RequiresPermissions("cata:infoItemTemp:getOrderNum")
	@RequiresPermissionsDesc(menu = { "目录编制", "信息资源目录" }, button = "查询信息项显示序号")
	@GetMapping("/getOrderNum")
	@ApiOperation("查询信息项显示序号")
	public Object getOrderNum(@RequestParam(required=true) Integer uviewId) {
		QueryWrapper<CataInfoItemTempRel> qw=new QueryWrapper<CataInfoItemTempRel>();
		qw.eq(CataInfoItemTempRel.UVIEW_ID, uviewId);
		qw.orderByDesc(CataInfoItemTempRel.SNO);
		CataInfoItemTempRel cataInfoItemTempRel=cataInfoItemTempRelService.getOne(qw);
		if(cataInfoItemTempRel!=null){
			if(cataInfoItemTempRel.getSno()!=null){
				return ResponseUtil.ok(cataInfoItemTempRel.getSno()+1);
			}
		}
		return ResponseUtil.ok(1);
	}
	
	@RequiresPermissions("cata:infoItemTemp:save")
    @RequiresPermissionsDesc(menu = {"目录编制", "信息资源目录"}, button = "暂存信息项新增")
    @PostMapping("/save")
    @ApiOperation("暂存信息项新增")
    public Object save(@RequestBody CataInfoItemTempRel cata) {
        QueryWrapper<CataInfoItemTempRel> qw=new QueryWrapper<CataInfoItemTempRel>();
        if(cata!=null&&cata.getUviewId()!=null){
        	qw.eq(CataInfoItemTempRel.UVIEW_ID, cata.getUviewId());
        	qw.eq(CataInfoItemTempRel.ENG_CD, cata.getEngCd());
//        	qw.eq(CataInfoItemTempRel.SRC_FIELD, cata.getSrcField());
        	cata.setCreateTime(new Date());
        }
        if (cataInfoItemTempRelService.count(qw) > 0) {
            return ResponseUtil.fail(INFO_CODE_EXIST, "英文标识已经存在");
        }
        CataInfoTemp cataInfoTemp=cataInfoTempService.getOne(new QueryWrapper<CataInfoTemp>().eq(CataInfoTemp.UVIEW_ID, cata.getUviewId()).eq(CataInfoTemp.DELETED, 0));
        if(cataInfoTemp!=null&&"1".equals(cataInfoTemp.getAuditStatus())){
        	return ResponseUtil.fail(BUS_ERROR, "不能对‘待审核’状态的数据进行新增");
        }
        cataInfoItemTempRelService.saveInfo(cata);
        cataInfoTemp.setAuditStatus("0");
        cataInfoTempService.updateById(cataInfoTemp);
        logHelper.logGeneralSucceed("信息资源信息项维护", "新增信息项【" +cataInfoTemp.getUviewNm()+"/"+ cata.getSrcField() + "/"+ cata.getEngCd()+ "】");
        return ResponseUtil.ok();
    }
	
	@RequiresPermissions("cata:infoItemTemp:update")
    @RequiresPermissionsDesc(menu = {"目录编制", "信息资源目录"}, button = "暂存信息项修改")
    @PostMapping("/update")
    @ApiOperation("暂存信息项修改")
    public Object update(@RequestBody CataInfoItemTempRel cata) {
		QueryWrapper<CataInfoItemTempRel> qw=new QueryWrapper<CataInfoItemTempRel>();
        if(cata!=null&&cata.getUviewId()!=null){
        	qw.ne(CataInfoItemTempRel.UVIEWSTR_ID, cata.getUviewstrId());
        	qw.eq(CataInfoItemTempRel.UVIEW_ID, cata.getUviewId());
        	qw.eq(CataInfoItemTempRel.ENG_CD, cata.getEngCd());
//        	qw.eq(CataInfoItemTempRel.SRC_FIELD, cata.getSrcField());
        }
        if (cataInfoItemTempRelService.count(qw) > 0) {
            return ResponseUtil.fail(INFO_CODE_EXIST, "英文标识已经存在");
        }
        CataInfoTemp cataInfoTemp=cataInfoTempService.getOne(new QueryWrapper<CataInfoTemp>().eq(CataInfoTemp.UVIEW_ID, cata.getUviewId()).eq(CataInfoTemp.DELETED, 0));
        if(cataInfoTemp!=null&&"1".equals(cataInfoTemp.getAuditStatus())){
        	return ResponseUtil.fail(BUS_ERROR, "不能对‘待审核’状态的数据进行修改");
        }
        cataInfoItemTempRelService.updateInfo(cata);
        cataInfoTemp.setAuditStatus("0");
        cataInfoTempService.updateById(cataInfoTemp);
        logHelper.logGeneralSucceed("信息资源信息项维护", "编辑信息项【" +cataInfoTemp.getUviewNm()+ "/"+cata.getSrcField() + "/"+ cata.getEngCd()+ "】");
        return ResponseUtil.ok();
    }

    @RequiresPermissions("cata:infoItemTemp:delete")
    @RequiresPermissionsDesc(menu = {"目录编制", "信息资源目录"}, button = "暂存信息项删除")
    @PostMapping("/delete")
    @ApiOperation("暂存信息项删除")
    public Object delete(@RequestBody Integer[] ids,@RequestParam(required=true)Integer uviewId) {
        if (ids == null) {
            return ResponseUtil.badArgument();
        }
        CataInfoTemp cataInfoTemp=cataInfoTempService.getOne(new QueryWrapper<CataInfoTemp>().eq(CataInfoTemp.UVIEW_ID, uviewId).eq(CataInfoTemp.DELETED, 0));
        if("1".equals(cataInfoTemp.getAuditStatus())){
        	return ResponseUtil.fail(BUS_ERROR, "不能对‘待审核’状态的数据进行删除");
        }
        List<CataInfoItemTempRel> list=cataInfoItemTempRelService.list(new QueryWrapper<CataInfoItemTempRel>()
				.eq(CataInfoItemTempRel.UVIEW_ID, uviewId).in(CataInfoItemTempRel.UVIEWSTR_ID, ids));
		if (cataInfoItemTempRelService.remove(new QueryWrapper<CataInfoItemTempRel>()
				.eq(CataInfoItemTempRel.UVIEW_ID, uviewId).in(CataInfoItemTempRel.UVIEWSTR_ID, ids))) {
			if(list!=null&&list.size()>0){
				String log = "";
				for (CataInfoItemTempRel cataInfoItemTempRel : list) {
					log+=cataInfoTemp.getUviewNm()+"/"+cataInfoItemTempRel.getSrcField()+"/"+cataInfoItemTempRel.getEngCd()+"、";
				}
				log=log.substring(0,log.length()-1);
				logHelper.logGeneralSucceed("信息资源信息项维护", "删除信息项【" + log + "】");
			}
	        cataInfoTemp.setAuditStatus("0");
	        cataInfoTempService.updateById(cataInfoTemp);
			return ResponseUtil.ok();
		}
		return ResponseUtil.fail(BUS_ERROR,"删除失败");
    }
}
