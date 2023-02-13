package com.digitalchina.resourcecatalog.admin.web.api;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalchina.resourcecatalog.admin.service.LogHelper;
import com.digitalchina.resourcecatalog.core.util.ResponseUtil;
import com.digitalchina.resourcecatalog.db.domain.ArchBusiUviewEx;
import com.digitalchina.resourcecatalog.db.domain.ArchOrg;
import com.digitalchina.resourcecatalog.db.domain.DictAssetCate;
//import com.digitalchina.resourcecatalog.db.domain.MqResource;
import com.digitalchina.resourcecatalog.db.domain.SysDict;
import com.digitalchina.resourcecatalog.db.service.*;
//import com.digitalchina.resourcecatalog.db.service.MqResourceService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 对外暴漏接口
 * </p>
 *
 * @author baokd
 * @since 2020-06-12
 */
@RestController
@RequestMapping("/api")
@Api(value = "对外暴漏接口", tags = "对外暴漏接口")
public class ApiController {

    @Autowired
    ArchOrgService archOrgService;

    @Autowired
    LogHelper logHelper;

    @Autowired
    ArchBusiUviewExService archBusiUviewExService;

    @Autowired
    ArchBusiUviewStrExService archBusiUviewStrExService;
    
    @Autowired
    DictAssetCateService dictAssetCateService;
    
    @Autowired
    AssetCateExService assetCateExService;

    @Autowired
    SysDictService sysDictService;
    
//    @Autowired
//    MqResourceService mqResourceService;
    
    @Value("${apiKey}")
    String apiKeyConfig;

	@Autowired
    CataInfoTempService cataInfoTempService;
	/**
	 * 信息资源目分页-分页
	 * @param apiKey
	 * @param page
	 * @param limit
	 * @param auditStatus 状态，0草稿 1待审核 2初审未通过 3初审通过 4终审驳回 5终审通过
	 * @param provOrgId
	 * @return
	 */
	@GetMapping("/resourceCatalog")
	@ApiOperation("信息资源目分页")
	public Object resourceCatalog(@RequestParam(required = true) String apiKey,
						 @RequestParam(defaultValue = "1") Integer page,
						 @RequestParam(defaultValue = "5") Integer limit,
					     @RequestParam(defaultValue = "5") String auditStatus,
						 @RequestParam(required = false) String provOrgId,
					     @RequestParam(required = false) String cityCataCode
								  ) {
		if(!apiKeyConfig.equals(apiKey)){
			return ResponseUtil.fail("请输入正确的apiKey!");
		}
//		if(StringUtils.isEmpty(provOrgId)){
//			return ResponseUtil.fail("部门ID不能为空!");
//		}
		Page<ArchOrg> page1 = new Page<ArchOrg>(page, limit);
		return ResponseUtil.ok(cataInfoTempService.selectPages(page1,null,null, provOrgId, auditStatus,null, null, null, cityCataCode));
	}
    /***
     * 组织机构列表-分页
     * @param apiKey
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/allOrg")
    @ApiOperation("组织机构列表")
    public Object allOrg(@RequestParam(required = true) String apiKey,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit) {
		if(!apiKeyConfig.equals(apiKey)){
			return ResponseUtil.fail("请输入正确的apiKey!");
		}
		Page<ArchOrg> page1 = new Page<ArchOrg>(page, limit);
		return ResponseUtil.ok(archOrgService.page(page1,new QueryWrapper<ArchOrg>()));
    }

    /**
     * 所有的资源目录
     *
     * @param apiKey
     * @param pubSts
     * @param page
     * @param limit
     * @return
     * @throws ParseException 
     */
    @GetMapping("/allResdoc")
    @ApiOperation("所有的资源目录")
    public Object allResdoc(@RequestParam(required = true) String apiKey,@RequestParam(required = false) String pubSts,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false)String startTime,@RequestParam(required = false)String endTime) throws ParseException {
		if(!apiKeyConfig.equals(apiKey)){
			return ResponseUtil.fail("请输入正确的apiKey!");
		}
		QueryWrapper<ArchBusiUviewEx> qw = new QueryWrapper<ArchBusiUviewEx>();
		// qw.eq(ArchBusiUviewEx.DELETED, 0);
		if(!StringUtils.isEmpty(pubSts)){
			qw.eq(ArchBusiUviewEx.PUB_STS, pubSts);
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(!StringUtils.isEmpty(startTime)){
			if(startTime.length()<11){
				simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			}else{
				simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			}
			Date date = simpleDateFormat.parse(startTime);
			qw.ge(ArchBusiUviewEx.UPDATE_DT, date);
		}
		if(!StringUtils.isEmpty(endTime)){
			if(endTime.length()<11){
				simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			}else{
				simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			}
			Date date = simpleDateFormat.parse(endTime);
			qw.lt(ArchBusiUviewEx.UPDATE_DT, date);
		}
		Page<ArchBusiUviewEx> page1 = new Page<ArchBusiUviewEx>(page, limit);
		return ResponseUtil.ok(archBusiUviewExService.page(page1,qw));
    }
    
    /**
     * 所有的资源目录信息项
     *
     * @param apiKey
     * @param pubSts
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/allResdocStr")
    @ApiOperation("所有的资源目录信息项")
    public Object allResdocStr(@RequestParam(required = true) String apiKey,@RequestParam(required = false) String pubSts,
            @RequestParam(required = false) Integer uviewId,@RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit) {
		if(!apiKeyConfig.equals(apiKey)){
			return ResponseUtil.fail("请输入正确的apiKey!");
		}
		Page page1 = new Page<>(page, limit);
		return ResponseUtil.ok(archBusiUviewStrExService.getList(page1,pubSts,uviewId));
    }

    /**
     * 所有的目录分类
     *
     * @param apiKey
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/allType")
    @ApiOperation("所有的目录分类")
    public Object allType(@RequestParam(required = true) String apiKey,@RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit) {
		if(!apiKeyConfig.equals(apiKey)){
			return ResponseUtil.fail("请输入正确的apiKey!");
		}
		Page<DictAssetCate> page1 = new Page<DictAssetCate>(page, limit);
		return ResponseUtil.ok(dictAssetCateService.page(page1,new QueryWrapper<DictAssetCate>().eq(DictAssetCate.STATUS, "1")));
    }
    
    /**
     * 资源目录与分类关联
     * @param apiKey
     * @param type
     * @param infoId
     * @param typeId
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/allTypeRel")
    @ApiOperation("资源目录与分类关联")
    public Object allTypeRel(@RequestParam(required = true) String apiKey,@RequestParam(required = false) String type,
    		@RequestParam(required = false) Integer infoId,@RequestParam(required = false) Integer typeId,
    		@RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit) {
		if(!apiKeyConfig.equals(apiKey)){
			return ResponseUtil.fail("请输入正确的apiKey!");
		}
		Page page1 = new Page<>(page, limit);
		return ResponseUtil.ok(assetCateExService.getList(page1,type,infoId,typeId));
    }
    
    /**
     * 创建MQ序列
     * @param apiKey
     * @param mqResource
     * @return
     */
//    @PostMapping("/createMQ")
//    @ApiOperation("创建MQ序列")
//    public Object createMQ(@RequestParam(required = true) String apiKey,@RequestBody MqResource mqResource) {
//		if(!apiKeyConfig.equals(apiKey)){
//			return ResponseUtil.fail("请输入正确的apiKey!");
//		}
//		if(mqResource!=null){
//    		mqResourceService.insert(mqResource);
//			return ResponseUtil.ok();
//		}else{
//			return ResponseUtil.fail("请传入参数！");
//		}
//    }
    
    /**
     * 删除MQ序列
     * @param apiKey
     * @param type
     * @param clientId
     * @return
     */
//    @GetMapping("/deleteMQ")
//    @ApiOperation("删除MQ序列")
//    public Object deleteMQ(@RequestParam(required = true) String apiKey,@RequestParam(required = true) Integer type,
//    		@RequestParam(required = true) String clientId) {
//		if(!apiKeyConfig.equals(apiKey)){
//			return ResponseUtil.fail("请输入正确的apiKey!");
//		}
//		mqResourceService.delete(type,clientId);
//		return ResponseUtil.ok();
//    }
    
    /**
     * 查询机构
     * @param apiKey
     * @return
     */
    @GetMapping("/selectOrg")
    @ApiOperation("查询机构")
    public Object selectOrg(@RequestParam(required = true) String apiKey) {
		if(!apiKeyConfig.equals(apiKey)){
			return ResponseUtil.fail("请输入正确的apiKey!");
		}
		List<ArchOrg> list=archOrgService.list(new QueryWrapper<ArchOrg>());
//		mqResourceService.sendMessage(1, list);
		return ResponseUtil.ok(list);
    }
	/**
	 * 查询字典
	 * @param apiKey
	 * @return
	 */
	@GetMapping("/queryDict")
	@ApiOperation("查询字典")
	public Object queryDict(@RequestParam(required = true) String apiKey, @RequestParam(required = false) String name,
							@RequestParam(required = false) String type, @RequestParam(required = false) Integer pId) {
		if(!apiKeyConfig.equals(apiKey)){
			return ResponseUtil.fail("请输入正确的apiKey!");
		}
		QueryWrapper queryWrapper = new QueryWrapper<SysDict>();
		if(!org.springframework.util.StringUtils.isEmpty(name)){
			queryWrapper.like(SysDict.NAME, name);
		}
		if(!org.springframework.util.StringUtils.isEmpty(type)){
			queryWrapper.eq(SysDict.TYPE, type.trim());
		}
		if(pId!=null){
			queryWrapper.eq(SysDict.PID, pId);
		}
		queryWrapper.orderByAsc(SysDict.SORT);
		List<SysDict> list= sysDictService.list(queryWrapper);
		return ResponseUtil.ok(list);
	}
}
