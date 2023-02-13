package com.digitalchina.resourcecatalog.admin.web;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalchina.resourcecatalog.admin.annotation.RequiresPermissionsDesc;
import com.digitalchina.resourcecatalog.admin.dto.ArchAppSysDto;
import com.digitalchina.resourcecatalog.admin.service.LogHelper;
import com.digitalchina.resourcecatalog.admin.util.UserInfo;
import com.digitalchina.resourcecatalog.core.util.BeanUtils;
import com.digitalchina.resourcecatalog.core.util.CharUtil;
import com.digitalchina.resourcecatalog.core.util.ResponseUtil;
import com.digitalchina.resourcecatalog.db.domain.ArchAppSys;
import com.digitalchina.resourcecatalog.db.domain.ArchBusi;
import com.digitalchina.resourcecatalog.db.domain.CataAppBusRel;
import com.digitalchina.resourcecatalog.db.service.ArchAppSysService;
import com.digitalchina.resourcecatalog.db.service.ArchBusiService;
import com.digitalchina.resourcecatalog.db.service.ArchOrgService;
import com.digitalchina.resourcecatalog.db.service.CataAppBusRelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 应用系统管理 前端控制器
 * </p>
 *
 * @author wangmh
 * @since 2020-05-09
 */
@RestController
@RequestMapping("/admin/archAppSys")
@Api(value = "应用系统管理", tags = "应用系统管理")
public class ArchAppSysController {

    @Autowired
    LogHelper logHelper;

    @Autowired
    ArchAppSysService archAppSysService;

    @Autowired
    ArchOrgService archOrgService;

    @Autowired
    ArchBusiService archBusiService;

    @Autowired
    CataAppBusRelService cataAppBusRelService;

    /**
     * 树（isTop 一级是否部门 0是 1否）
     *
     * @return
     */
//    @RequiresPermissions("admin:archAppSys:deptTree")
//    @RequiresPermissionsDesc(menu = {"应用系统管理"}, order = 4, button = "左侧树")
    @RequiresAuthentication
    @GetMapping("/deptTree")
    @ApiOperation("左侧树")
    public Object deptTree() {
        return archOrgService.tree(0, UserInfo.isDeptAdmin(), UserInfo.getDeptIds());
    }

    /***
     * 分页
     * @param orgId
     * @param current
     * @param size
     * @return
     */
    @RequiresPermissions("admin:archAppSys:list")
    @RequiresPermissionsDesc(menu = {"应用系统管理"}, button = "分页")
    @GetMapping("/list")
    @ApiOperation("分页")
    public Object list(@RequestParam(required = false) String appsysNm,
                       @RequestParam(required = false) Integer orgId,
                       @RequestParam(defaultValue = "1") Integer current,
                       @RequestParam(defaultValue = "10") Integer size) {
        IPage page = new Page<>(current, size);
        QueryWrapper<ArchAppSys> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .select(ArchAppSys::getAppsysId, ArchAppSys::getAppsysNo, ArchAppSys::getAppsysNm, ArchAppSys::getAppsysDesc)
                .like(ArchAppSys::getAppsysNm, appsysNm)
                .orderByDesc(ArchAppSys::getCreatedTime);
        if (orgId != null && orgId != 0) {
            queryWrapper.lambda().eq(ArchAppSys::getProvOrgId, orgId);
            return ResponseUtil.ok(archAppSysService.page(page, queryWrapper));
        }
        if (UserInfo.isDeptAdmin()) {// 部门
            queryWrapper.lambda().in(ArchAppSys::getProvOrgId, UserInfo.getDeptIds());
        }
        return ResponseUtil.ok(archAppSysService.page(page, queryWrapper));
    }

    /***
     * 新增或保存
     * @param archAppSysDto
     * @param result
     * @return
     */
    @RequiresPermissions("admin:archAppSys:saveOrUpdate")
    @RequiresPermissionsDesc(menu = {"应用系统管理"}, button = "新增或保存")
    @PostMapping("/saveOrUpdate")
    @ApiOperation("新增或编辑")
    public Object saveOrUpdate(@Valid @RequestBody ArchAppSysDto archAppSysDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseUtil.fail(400, result.getFieldError().getDefaultMessage());
        }
        if (CharUtil.isSpecialChar(archAppSysDto.getAppsysNo())) {
            return ResponseUtil.fail("应用系统代码不能包含特殊字符！");
        }
        if (!CharUtil.isCodeTrue(archAppSysDto.getAppsysNo())) {
            return ResponseUtil.fail("应用系统代码必须以字母开头，数字结尾，并不能输入中文！");
        }
        LambdaQueryWrapper<ArchAppSys> qwC = new QueryWrapper<ArchAppSys>().lambda().eq(ArchAppSys::getAppsysNm, archAppSysDto.getAppsysNm()).eq(ArchAppSys::getProvOrgId, archAppSysDto.getProvOrgId());
        if (archAppSysDto.getAppsysId() != null) {
        	qwC.ne(ArchAppSys::getAppsysId, archAppSysDto.getAppsysId());
        }
        if (archAppSysService.count(qwC) > 0) {
            return ResponseUtil.fail("应用系统名称同一部门下不能重复！");
        }
        QueryWrapper<ArchAppSys> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ArchAppSys::getAppsysNo, archAppSysDto.getAppsysNo());
        if (archAppSysDto.getAppsysId() != null) {
            wrapper.lambda().ne(ArchAppSys::getAppsysId, archAppSysDto.getAppsysId());
        }
        if (archAppSysService.count(wrapper) > 0) {
            return ResponseUtil.fail("应用系统代码不能重复！");
        }
        //所支撑的权责清单是否重复
        List<Integer> busiIds = archAppSysDto.getBusiIds();
        if (busiIds != null && busiIds.size() > 1) {
            HashSet<Integer> hashSet = new HashSet<>(busiIds);
            if (busiIds.size() != hashSet.size()) {
                return ResponseUtil.fail("所支撑的权责清单不能重复！");
            }
        }
        ArchAppSys archAppSys = BeanUtils.dtoToDo(archAppSysDto, ArchAppSys.class);
        boolean flag = archAppSysService.saveOrUpdateInfo(archAppSys, busiIds, UserInfo.getUsername());
        //记录日志
        logHelper.logOrgSucceed(flag ? "新增应用系统" : "编辑应用系统", archAppSys.getAppsysNm(), archAppSys.getAppsysNo());
        return ResponseUtil.ok();
    }

    /***
     * 删除
     * @param appsysId
     * @return
     */
    @RequiresPermissions("admin:archAppSys:delete")
    @RequiresPermissionsDesc(menu = {"应用系统管理"}, button = "删除")
    @PostMapping("/delete/{appsysId}")
    @ApiOperation("删除")
    public Object delete(@PathVariable Integer appsysId) {
        ArchAppSys byId = archAppSysService.getById(appsysId);
        archAppSysService.delete(appsysId);
        //记录日志
        logHelper.logOrgSucceed("删除应用系统", byId.getAppsysNm(), byId.getAppsysNo());
        return ResponseUtil.ok();
    }

    /***
     * 详情
     * @param appId
     * @return
     */
    @RequiresPermissions("admin:archAppSys:detail")
    @RequiresPermissionsDesc(menu = {"应用系统管理"}, button = "详情")
    @GetMapping("/detail")
    @ApiOperation("详情")
    public Object detail(@RequestParam Integer appId) {
        ArchAppSys byId = archAppSysService.getById(appId);
        byId.setBusis(archAppSysService.getBusiRelList(appId));
        return ResponseUtil.ok(byId);
    }

    /***
     * 内（下）设机构树
     * @param orgId
     * @return
     */
//    @RequiresPermissions("admin:archAppSys:orgTree")
//    @RequiresPermissionsDesc(menu = {"应用系统管理"}, button = "内（下）设机构树")
    @RequiresAuthentication
    @GetMapping("/orgTree")
    @ApiOperation("内（下）设机构树")
    public Object orgTree(@NotNull @RequestParam Integer orgId) {
        return ResponseUtil.ok(archOrgService.treeAppOrg(orgId));
    }

    /***
     * 分页
     * @param appId
     * @param current
     * @param size
     * @return
     */
    @RequiresPermissions("admin:archAppSys:busiPage")
    @RequiresPermissionsDesc(menu = {"应用系统管理"}, button = "权责清单分页")
    @GetMapping("/busiPage")
    @ApiOperation("权责清单分页")
    public Object busiPage(@RequestParam(required = false) String busiNmOrBusiNo,
                           @RequestParam(required = false) Integer appId,
                           @RequestParam Integer orgId,
                           @RequestParam(defaultValue = "1") Integer current,
                           @RequestParam(defaultValue = "10") Integer size) {
        IPage page = new Page<>(current, size);
        QueryWrapper<ArchBusi> busiQueryWrapper = new QueryWrapper<>();
        busiQueryWrapper.lambda()
                .select(ArchBusi::getBusiId, ArchBusi::getBusiNo, ArchBusi::getBusiNm)
                .orderByAsc(ArchBusi::getBusiNo)
                .eq(ArchBusi::getDeptId, orgId)
                .and(wrapper -> wrapper.like(ArchBusi::getBusiNm, busiNmOrBusiNo).or().like(ArchBusi::getBusiNo, busiNmOrBusiNo));
        busiQueryWrapper.orderByAsc("p_id");
//        if (appId != null) {
//            List<CataAppBusRel> cataAppRelList = cataAppBusRelService.list(new QueryWrapper<CataAppBusRel>().lambda().eq(CataAppBusRel::getAppId, appId));
//            busiQueryWrapper.lambda()
//                    .notIn(ArchBusi::getBusiId, cataAppRelList.stream().map(CataAppBusRel::getItemId).collect(Collectors.toList()));
//        }
        return ResponseUtil.ok(archBusiService.page(page, busiQueryWrapper));
    }

    /***
     * 关系图
     * @return
     */
    @GetMapping("/getDiagram")
    @RequiresPermissions("admin:archBusi:getDiagram")
    @RequiresPermissionsDesc(menu = {"应用系统管理"}, button = "资源关系图")
    @ApiOperation("资源关系图")
    public Object getDiagram(@RequestParam Integer appId) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("nodes", archAppSysService.getNodes(appId));
        resultMap.put("links", archAppSysService.getLinks(appId));
        return ResponseUtil.ok(resultMap);
    }
    @GetMapping("/getCode")
    @ApiOperation("获取应用系统代码")
    public Object getCode() {
        QueryWrapper<ArchAppSys> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("appsys_id");
        wrapper.last("limit 1");
        ArchAppSys archAppSys = archAppSysService.getOne(wrapper);
        int index = 1;
        if(archAppSys!=null){
            index = archAppSys.getAppsysId() + 1;
        }
        return ResponseUtil.ok("TJHB" + index);
    }
}
