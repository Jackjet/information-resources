package com.digitalchina.resourcecatalog.admin.web;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalchina.resourcecatalog.admin.annotation.RequiresPermissionsDesc;
import com.digitalchina.resourcecatalog.admin.dto.ArchOrgDto;
import com.digitalchina.resourcecatalog.admin.dto.ArchOrgEditDto;
import com.digitalchina.resourcecatalog.admin.service.LogHelper;
import com.digitalchina.resourcecatalog.admin.util.UserInfo;
import com.digitalchina.resourcecatalog.core.util.BeanUtils;
import com.digitalchina.resourcecatalog.core.util.CharUtil;
import com.digitalchina.resourcecatalog.core.util.PageUtil;
import com.digitalchina.resourcecatalog.core.util.ResponseUtil;
import com.digitalchina.resourcecatalog.db.domain.ArchBusi;
import com.digitalchina.resourcecatalog.db.domain.ArchOrg;
import com.digitalchina.resourcecatalog.db.domain.CataOrgItemRel;
//import com.digitalchina.resourcecatalog.db.rabbitmq.RabbitConfig;
import com.digitalchina.resourcecatalog.db.service.ArchBusiService;
import com.digitalchina.resourcecatalog.db.service.ArchOrgService;
import com.digitalchina.resourcecatalog.db.service.CataOrgItemRelService;
//import com.digitalchina.resourcecatalog.db.service.MqResourceService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jodd.util.StringUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 组织机构 前端控制器
 * </p>
 *
 * @author wangmh
 * @since 2020-05-09
 */
@RestController
@RequestMapping("/admin/org")
@Api(value = "组织机构管理", tags = "组织机构管理")
public class ArchOrgController {

    @Autowired
    ArchOrgService archOrgService;

    @Autowired
    LogHelper logHelper;

    @Autowired
    ArchBusiService archBusiService;

    @Autowired
    CataOrgItemRelService cataOrgItemRelService;
    
//    @Autowired
//    MqResourceService mqResourceService;

    /***
     * 分页
     * @param orgId
     * @param current
     * @param size
     * @return
     */
    @RequiresPermissions("admin:org:list")
    @RequiresPermissionsDesc(menu = {"系统管理", "组织机构管理"}, button = "分页")
    @GetMapping("/list")
    @ApiOperation("组织机构管理-分页")
    public Object list(@RequestParam(required = false) Integer orgId,
                       @RequestParam(defaultValue = "1") Integer current,
                       @RequestParam(defaultValue = "10") Integer size) {
        List<ArchOrg> orgList = archOrgService.myPage(orgId, UserInfo.getDeptIds(), UserInfo.isDeptAdmin());
        return ResponseUtil.ok(PageUtil.startPage(orgList, current, size));
    }

    /**
     * 树（isTop 一级是否部门 0是 1否）
     *
     * @param isTop 0 是部门 第一级 1 是组织 第二级
     * @return
     */
//    @RequiresPermissions("admin:org:deptTree")
//    @RequiresPermissionsDesc(menu = {"部门职能职责"}, order = 2, button = "左侧树")
    @RequiresAuthentication
    @GetMapping("/deptTree")
    @ApiOperation("树（isTop 一级是否部门 0是 1否）")
    public Object tree(@RequestParam(defaultValue = "0") Integer isTop) {
        return archOrgService.tree(isTop, UserInfo.isDeptAdmin(), UserInfo.getDeptIds());
    }

    @RequiresAuthentication
    @GetMapping("/deptTree2")
    @ApiOperation("部门职能职责的部门树")
    public Object tree2() {
        return archOrgService.tree2(UserInfo.isDeptAdmin(), UserInfo.getDeptIds());
    }

    /**
     * 组织结构查询定位
     *
     * @param orgNm
     * @return
     */
    @RequiresPermissions("admin:org:getPosition")
    @RequiresPermissionsDesc(menu = {"系统管理", "组织机构管理"}, button = "查询定位")
    @GetMapping("/getPosition")
    @ApiOperation("组织机构管理-组织结构查询定位")
    public Object getPosition(@RequestParam String orgNm) {
        QueryWrapper<ArchOrg> wrapper = new QueryWrapper<>();
        wrapper.lambda().select(ArchOrg::getOrgId).like(ArchOrg::getOrgNm, orgNm).isNotNull(ArchOrg::getParOrgId);
        return ResponseUtil.ok(archOrgService.list(wrapper));
    }

    /***
     * 详情
     * @param orgId
     * @return
     */
    @RequiresAuthentication
    @GetMapping("/detail")
    @ApiOperation("组织机构管理-详情")
    public Object detail(@RequestParam Integer orgId) {
        return ResponseUtil.ok(archOrgService.detail(orgId));
    }

    /***
     * 删除
     * @param orgId
     * @return
     */
    @RequiresPermissions("admin:org:delete")
    @RequiresPermissionsDesc(menu = {"系统管理", "组织机构管理"}, button = "删除机构")
    @GetMapping("/delete")
    @ApiOperation("组织机构管理-删除")
    public Object delete(@RequestParam Integer orgId) {
        ArchOrg byId = archOrgService.getById(orgId);
        archOrgService.deleteTree(orgId);
        logHelper.logOrgSucceed("删除组织机构", byId.getOrgNm(), byId.getOrgCd());
        return ResponseUtil.ok();
    }

    /***
     * 组织机构管理-新增
     * @param archOrgDto
     * @return
     */
    @RequiresPermissions("admin:org:save")
    @RequiresPermissionsDesc(menu = {"系统管理", "组织机构管理"}, button = "新增机构")
    @PostMapping("/save")
    @ApiOperation("组织机构管理-新增")
    public Object save(@Valid @RequestBody ArchOrgDto archOrgDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseUtil.fail(400, result.getFieldError().getDefaultMessage());
        }
        if (CharUtil.isSpecialChar(archOrgDto.getOrgCd())) {
            return ResponseUtil.fail("组织机构编码不能包含特殊字符！");
        }
        ArchOrg archOrg = BeanUtils.dtoToDo(archOrgDto, ArchOrg.class);
        archOrg.setOrgId(null);
        //验证编码在同级下是否存在
        QueryWrapper<ArchOrg> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ArchOrg::getOrgCd, archOrg.getOrgCd());
        if (archOrg.getParOrgId() == null) {
            wrapper.lambda().isNull(ArchOrg::getParOrgId);
        } else {
            wrapper.lambda().eq(ArchOrg::getParOrgId, archOrg.getParOrgId());
        }
        if (archOrgService.count(wrapper) > 0) {
            return ResponseUtil.fail(400, "部门编码已存在！");
        }
        if(archOrgDto.getParOrgId()!=null){
        	if (archOrgService.count(new QueryWrapper<ArchOrg>().eq(ArchOrg.PAR_ORG_ID, archOrg.getParOrgId()).eq(ArchOrg.ORG_NM, archOrg.getOrgNm())) > 0) {
                return ResponseUtil.fail(400, "部门名称不能重复！");
            }
        }else{
        	if (archOrgService.count(new QueryWrapper<ArchOrg>().isNull(ArchOrg.PAR_ORG_ID).eq(ArchOrg.ORG_NM, archOrg.getOrgNm())) > 0) {
        		return ResponseUtil.fail(400, "部门名称不能重复！");
        	}
        }
        archOrg.setCreator(UserInfo.getUsername());
        archOrg.setCrtDt(new Date());
        if (archOrgService.save(archOrg)) {
            logHelper.logOrgSucceed("新增组织机构", archOrg.getOrgNm(), archOrg.getOrgCd());
//            mqResourceService.sendMessage(RabbitConfig.ORG_UPDATE, archOrg);
            return ResponseUtil.ok();
        }
        return ResponseUtil.fail();
    }

    /***
     * 组织机构管理-编辑
     * @param archOrgDto
     * @return
     */
    @RequiresPermissions("admin:org:updateOrg")
    @RequiresPermissionsDesc(menu = {"系统管理", "组织机构管理"}, button = "编辑机构")
    @PostMapping("/updateOrg")
    @ApiOperation("组织机构管理-编辑")
    public Object updateOrg(@Valid @RequestBody ArchOrgDto archOrgDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseUtil.fail(400, result.getFieldError().getDefaultMessage());
        }
        if (archOrgDto.getOrgId() == null) {
            return ResponseUtil.fail(400, "组织机构id不能为空");
        }
        if (CharUtil.isSpecialChar(archOrgDto.getOrgCd())) {
            return ResponseUtil.fail("权责清单编码不能包含特殊字符！");
        }
        ArchOrg archOrg = BeanUtils.dtoToDo(archOrgDto, ArchOrg.class);
        //验证编码在同级下是否存在
        QueryWrapper<ArchOrg> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ArchOrg::getOrgCd, archOrg.getOrgCd())
                .ne(ArchOrg::getOrgId, archOrg.getOrgId());
        if (archOrg.getParOrgId() == null) {
            wrapper.lambda().isNull(ArchOrg::getParOrgId);
        } else {
            wrapper.lambda().eq(ArchOrg::getParOrgId, archOrg.getParOrgId());
        }
        if (archOrgService.count(wrapper) > 0) {
            return ResponseUtil.fail(400, "部门编码已存在！");
        }
        if(archOrgDto.getParOrgId()!=null){
        	if (archOrgService.count(new QueryWrapper<ArchOrg>().eq(ArchOrg.PAR_ORG_ID, archOrg.getParOrgId()).eq(ArchOrg.ORG_NM, archOrg.getOrgNm()).ne(ArchOrg.ORG_ID, archOrg.getOrgId())) > 0) {
        		return ResponseUtil.fail(400, "部门名称不能重复！");
        	}
        }else{
        	if (archOrgService.count(new QueryWrapper<ArchOrg>().isNull(ArchOrg.PAR_ORG_ID).eq(ArchOrg.ORG_NM, archOrg.getOrgNm()).ne(ArchOrg.ORG_ID, archOrg.getOrgId())) > 0) {
        		return ResponseUtil.fail(400, "部门名称不能重复！");
        	}
        }
        archOrg.setUpdater(UserInfo.getUsername());
        archOrg.setUpdateDt(new Date());
        if (archOrgService.updateById(archOrg)) {
            logHelper.logOrgSucceed("编辑组织机构", archOrg.getOrgNm(), archOrg.getOrgCd());
//            mqResourceService.sendMessage(RabbitConfig.ORG_UPDATE, archOrg);
            return ResponseUtil.ok();
        }
        return ResponseUtil.fail();
    }

    /***
     * 部门职能职责-保存
     * @param archOrgEditDto
     * @return
     */
    @RequiresPermissions("admin:org:updateDept")
    @RequiresPermissionsDesc(menu = {"部门职能职责"}, button = "编辑部门")
    @PostMapping("/updateDept")
    @ApiOperation("部门职能职责-保存")
    public Object updateDept(@Valid @RequestBody ArchOrgEditDto archOrgEditDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseUtil.fail(400, result.getFieldError().getDefaultMessage());
        }
        ArchOrg archOrg = BeanUtils.dtoToDo(archOrgEditDto, ArchOrg.class);
        archOrg.setUpdater(UserInfo.getUsername());
        archOrg.setUpdateDt(new Date());
        if (archOrgService.updateById(archOrg)) {
            ArchOrg byId = archOrgService.getById(archOrg.getOrgId());
            logHelper.logOrgSucceed("保存部门职能职责", byId.getOrgNm(), byId.getOrgCd());
            return ResponseUtil.ok();
        }
        return ResponseUtil.fail();
    }

    /***
     * 生成机构编码
     * @param parOrgId
     * @return
     */
    @RequiresPermissions("admin:org:generateCd")
    @RequiresPermissionsDesc(menu = {"系统管理", "组织机构管理"}, button = "生成编码")
    @GetMapping("/generateCd")
    @ApiOperation("组织机构管理-生成机构编码")
    public Object generateCd(@RequestParam(required = false) Integer parOrgId) {
        Map resultMap = new HashMap();
        //获取显示序号
        Integer dispalySn = archOrgService.getDispalySn(parOrgId);
        if (dispalySn == null) {
            resultMap.put("dispalySn", 1);
        } else {
            resultMap.put("dispalySn", dispalySn + 1);
        }
        //如果等于null 属于第一级部门
        if (parOrgId == null || parOrgId == 0) {
            //获取最大的部门编码
            Integer orgCode = archOrgService.getMaxOrgCdByPidIsNull();
            if (orgCode == null) {
                resultMap.put("orgCode", "01");
                return ResponseUtil.ok(resultMap);
            }
            resultMap.put("orgCode", (++orgCode).toString().length() % 2 == 0 ? orgCode : "0" + orgCode);
            return ResponseUtil.ok(resultMap);
        }
        //如果不等于null 属于一级以下机构
        if (parOrgId != null) {
            //获取同级组织机构编码
            Integer orgCode = archOrgService.getMaxOrgCdByPid(parOrgId);
            //若等于null,说明添加的属于该组织机构下的第一个
            if (orgCode == null) {
                //获取上级父类的编码
                ArchOrg byId = archOrgService.getById(parOrgId);
                resultMap.put("orgCode", byId.getOrgCd() + "01");
                return ResponseUtil.ok(resultMap);
            }
            //若不为null，直接+1，判断字符串长度是否为偶数
            String newCode = Integer.toString(orgCode + 1);
            resultMap.put("orgCode", newCode.length() % 2 == 0 ? newCode : "0" + newCode);
            return ResponseUtil.ok(resultMap);
        }
        return ResponseUtil.fail();
    }

    /***
     * 关联权责清单-是否可点击
     * @param orgId
     * @return
     */
    @RequiresPermissions("admin:org:isCilckBusi")
    @RequiresPermissionsDesc(menu = {"部门职能职责"}, button = "关联权责清单")
    @GetMapping("/isCilckBusi")
    @ApiOperation("关联权责清单-是否可点击")
    public Object isCilckBusi(@RequestParam Integer orgId) {
        ArchOrg archOrg = archOrgService.getById(orgId);
        if (archOrg != null && !StringUtil.isEmpty(archOrg.getOrgDuty())) {
            return ResponseUtil.ok("可点击！");
        }
        return ResponseUtil.fail("不可点击！");
    }

    /***
     * 关联权责清单分页
     * @param orgId
     * @param current
     * @param size
     * @return
     */
    @RequiresPermissions("admin:org:busiList")
    @RequiresPermissionsDesc(menu = {"部门职能职责"}, button = "查询部门")
    @GetMapping("/busiList")
    @ApiOperation("部门职能职责-关联权责清单分页")
    public Object busiList(@RequestParam(required = false) Integer orgId,
                           @RequestParam(defaultValue = "1") Integer current,
                           @RequestParam(defaultValue = "10") Integer size) {
        return ResponseUtil.ok(PageUtil.startPage(archBusiService.getBusInfoByOrgId(orgId, "1"), current, size));
    }

    /***
     * 批量移除权责清单
     * @param busiIds
     * @param deptId
     * @return
     */
    @RequiresPermissions("admin:org:removeBusiList")
    @RequiresPermissionsDesc(menu = {"部门职能职责"}, button = "移除")
    @PostMapping("/removeBusiList/{deptId}")
    @ApiOperation("部门职能职责-批量移除权责清单")
    public Object removeBusiList(@RequestBody List<Integer> busiIds, @PathVariable Integer deptId) {
        UpdateWrapper<CataOrgItemRel> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().in(CataOrgItemRel::getItemId, busiIds).and(Wrapper -> Wrapper.eq(CataOrgItemRel::getOrgId, deptId).
                or(wrapper2 -> wrapper2.eq(CataOrgItemRel::getDeptId, deptId).isNull(CataOrgItemRel::getOrgId)));
        if (cataOrgItemRelService.remove(updateWrapper)) {
            ArchOrg archOrg = archOrgService.getById(deptId);
            List<ArchBusi> archBusis = archBusiService.list(new QueryWrapper<ArchBusi>().lambda().in(ArchBusi::getBusiId, busiIds));
            //记录日志
            logHelper.logBusiRelSucceed("部门职能关联权责清单", archOrg.getOrgNm(), null,
                    archBusis.stream().map(ArchBusi::getBusiNm).collect(Collectors.joining("、")));
            return ResponseUtil.ok();
        }
        return ResponseUtil.fail();
    }


    /***
     * 关联权责清单新增
     * @return
     */
    @RequiresPermissions("admin:org:saveBusiRef")
    @RequiresPermissionsDesc(menu = {"部门职能职责"}, button = "新增")
    @PostMapping("/saveBusiRef")
    @ApiOperation("关联权责清单-新增")
    public Object saveBusiRef(@ApiParam(name = "map", value = "{\"orgId\":1,\"deptId\":10,\"busiIds\":[1,2]}", required = true) @RequestBody Map<String, Object> map) {
        List<Integer> busiIds = (List<Integer>) map.get("busiIds");
        Integer deptId = (Integer) map.get("deptId");
        if (busiIds == null || busiIds.size() == 0 || deptId == null) {
            return ResponseUtil.ok("部门id或权责清单id不能为空！");
        }
        QueryWrapper<CataOrgItemRel> cataOrgItemRelQueryWrapper = new QueryWrapper<>();
        cataOrgItemRelQueryWrapper.lambda().in(CataOrgItemRel::getItemId, busiIds)
                .eq(CataOrgItemRel::getDeptId, deptId);
        Integer orgId = null;
        if (StringUtil.isEmpty(map.get("orgId").toString())) {
            cataOrgItemRelQueryWrapper.lambda().isNull(CataOrgItemRel::getOrgId);
        } else {
            orgId = (Integer) map.get("orgId");
            cataOrgItemRelQueryWrapper.lambda().eq(CataOrgItemRel::getOrgId, orgId);
        }
        if (cataOrgItemRelService.count(cataOrgItemRelQueryWrapper) > 0) {
            return ResponseUtil.fail("该部门中已存在所选一个或多个权责清单！");
        }
        List<CataOrgItemRel> cataOrgItemRels = new ArrayList<>();
        for (Integer item : busiIds) {
            CataOrgItemRel cataOrgItemRel = new CataOrgItemRel();
            cataOrgItemRel.setOrgId(orgId);
            cataOrgItemRel.setDeptId(deptId);
            cataOrgItemRel.setItemId(item);
            cataOrgItemRels.add(cataOrgItemRel);
        }
        if (cataOrgItemRelService.saveBatch(cataOrgItemRels)) {
            QueryWrapper<ArchOrg> orgQueryWrapper = new QueryWrapper<>();
            orgQueryWrapper.lambda().eq(ArchOrg::getOrgId, deptId);
            ArchOrg archorg = archOrgService.getOne(orgQueryWrapper);
            QueryWrapper<ArchBusi> archBusiQueryWrapper = new QueryWrapper<>();
            archBusiQueryWrapper.lambda().select(ArchBusi::getBusiNm).in(ArchBusi::getBusiId, busiIds);
            List<ArchBusi> archBusis = archBusiService.list(archBusiQueryWrapper);
            logHelper.logBusiRelSucceed("部门职能关联权责清单", archorg.getOrgNm()==null?"":archorg.getOrgNm(), null,
                    archBusis.stream().map(ArchBusi::getBusiNm).collect(Collectors.joining("、")));
            return ResponseUtil.ok();
        }
        return ResponseUtil.fail();
    }

    /***
     * 分页
     * @param orgId
     * @param current
     * @param size
     * @return
     */
    @RequiresPermissions("admin:org:addBusiPage")
    @RequiresPermissionsDesc(menu = {"部门职能职责"}, button = "权责清单分页")
    @GetMapping("/addBusiPage")
    @ApiOperation("关联权责清单-新增-分页")
    public Object addBusiPage(@RequestParam(required = false) String busiNm,
                              @RequestParam Integer orgId,
                              @RequestParam(defaultValue = "1") Integer current,
                              @RequestParam(defaultValue = "10") Integer size) {
        List<CataOrgItemRel> list = cataOrgItemRelService.list(new QueryWrapper<CataOrgItemRel>().lambda().eq(CataOrgItemRel::getDeptId, orgId));
        IPage page = new Page<>(current, size);
        QueryWrapper<ArchBusi> wrapper = new QueryWrapper<>();
        wrapper.lambda().notIn(ArchBusi::getBusiId, list.stream().map(CataOrgItemRel::getItemId).collect(Collectors.toList())).isNull(ArchBusi::getpId)
                .eq(ArchBusi::getDeptId, orgId).orderByAsc(ArchBusi::getBusiNo).and(wrapper1 -> wrapper1.like(ArchBusi::getBusiNo, busiNm).or().like(ArchBusi::getBusiNm, busiNm));
        return ResponseUtil.ok(archBusiService.page(page, wrapper));
    }
}
