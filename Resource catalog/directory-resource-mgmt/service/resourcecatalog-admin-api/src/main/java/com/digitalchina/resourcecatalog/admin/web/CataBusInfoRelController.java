package com.digitalchina.resourcecatalog.admin.web;


import static com.digitalchina.resourcecatalog.admin.util.AdminResponseCode.BUS_ERROR;

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
import com.digitalchina.resourcecatalog.db.domain.ArchBusi;
import com.digitalchina.resourcecatalog.db.domain.CataBusInfoRel;
import com.digitalchina.resourcecatalog.db.domain.CataRequire;
import com.digitalchina.resourcecatalog.db.service.ArchBusiService;
import com.digitalchina.resourcecatalog.db.service.CataBusInfoRelService;
import com.digitalchina.resourcecatalog.db.service.CataRequireService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 权责清单与信息资源关联表 前端控制器
 * </p>
 *
 * @author wangmh
 * @since 2020-05-09
 */
@RestController
@RequestMapping("/admin/cataBusInfoRel")
@Validated
@Api(value = "权责清单与信息资源关联管理", tags = "权责清单与信息资源关联管理")
public class CataBusInfoRelController {
//	@Autowired
//	private CataBusInfoRelService cataBusInfoRelService;
//    @Autowired
//    private LogHelper logHelper;
//    @Autowired
//    private CataRequireService cataRequireService;
//    @Autowired
//    private ArchBusiService archBusiService;
	
//	@RequiresPermissions("cata:busInfoRel:list")
//	@RequiresPermissionsDesc(menu = { "目录编制", "信息需求目录" }, button = "关联权责清单列表查询")
//	@GetMapping("/list")
//	@ApiOperation("关联权责清单列表查询")
//	public Object list(String busiNo, String busiNm, @RequestParam(required = true) Integer infoId,
//			String busiNoAndBusiNm, @RequestParam(defaultValue = "1") Integer type,
//			@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
//		Page page1 = new Page<>(page, limit);
//		if(!StringUtils.isEmpty(busiNoAndBusiNm)){
//			busiNo="";
//			busiNm="";
//		}
//		return ResponseUtil.ok(cataBusInfoRelService.selectPages(page1, busiNoAndBusiNm,busiNo,busiNm,infoId,type));
//	}
	
//	@RequiresPermissions("cata:busInfoRel:busList")
//	@RequiresPermissionsDesc(menu = { "目录编制", "信息需求目录" }, button = "未关联权责清单列表查询")
//	@GetMapping("/busList")
//	@ApiOperation("未关联权责清单列表查询")
//	public Object busList(String busiNo, String busiNm, @RequestParam(required = true) Integer infoId,
//			String busiNoAndBusiNm, @RequestParam(defaultValue = "1") Integer type,
//			@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
//		Page page1 = new Page<>(page, limit);
//		if(!StringUtils.isEmpty(busiNoAndBusiNm)){
//			busiNo="";
//			busiNm="";
//		}
//		CataRequire cataRequire=cataRequireService.getById(infoId);
//		if(cataRequire!=null&&cataRequire.getDeptId()!=null){
//			Integer deptId=cataRequire.getDeptId();
//			return ResponseUtil.ok(cataBusInfoRelService.selectBusPage(page1, busiNoAndBusiNm,busiNo,busiNm,infoId,type,deptId));
//		}
//		return ResponseUtil.fail(BUS_ERROR,"请确认所选信息需求目录是否有所属部门");
//	}
	
//	@RequiresPermissions("cata:busInfoRel:save")
//    @RequiresPermissionsDesc(menu = {"目录编制", "信息需求目录"}, button = "关联权责清单新增")
//    @PostMapping("/save")
//    @ApiOperation("关联权责清单新增")
//    public Object save(@RequestBody Integer[] ids,@RequestParam(required=true) Integer infoId,@RequestParam(defaultValue="1") Integer type) {
//		if (ids == null) {
//            return ResponseUtil.badArgument();
//        }
//		String log=cataBusInfoRelService.saveInfo(ids,infoId,type);
//		if(log==null){
//			return ResponseUtil.fail(BUS_ERROR, "增加失败");
//		}
//		logHelper.logGeneralSucceed("需求目录关联权责清单", "需求信息资源关联权责清单" + log);
//        return ResponseUtil.ok();
//    }
	
//    @RequiresPermissions("cata:busInfoRel:delete")
//    @RequiresPermissionsDesc(menu = {"目录编制", "信息需求目录"}, button = "关联权责清单删除")
//    @PostMapping("/delete")
//    @ApiOperation("关联权责清单删除")
//    public Object delete(@RequestBody Integer[] ids,@RequestParam(required=true) Integer infoId) {
//        if (ids == null) {
//            return ResponseUtil.badArgument();
//        }
//		if (!cataBusInfoRelService.remove(new QueryWrapper<CataBusInfoRel>().in(CataBusInfoRel.BUS_ID, ids)
//				.eq(CataBusInfoRel.INFO_ID, infoId).eq(CataBusInfoRel.TYPE, 1))) {
//			return ResponseUtil.fail(BUS_ERROR,"删除失败");
//		}
//		CataRequire cataRequire = cataRequireService.getById(infoId);
//		String log="【"+cataRequire.getName()+"/"+cataRequire.getCode()+"】【";
//		List<ArchBusi> list=archBusiService.list(new QueryWrapper<ArchBusi>().in(ArchBusi.BUSI_ID, ids));
//		if(list!=null&&list.size()>0){
//			for (ArchBusi archBusi : list) {
//				log+=archBusi.getBusiNo()+"、";
//			}
//			log=log.substring(0,log.length()-1)+"】";
//			logHelper.logGeneralSucceed("需求目录关联权责清单", "移除需求信息资源关联权责清单" + log);
//		}
//		return ResponseUtil.ok();
//    }
}
