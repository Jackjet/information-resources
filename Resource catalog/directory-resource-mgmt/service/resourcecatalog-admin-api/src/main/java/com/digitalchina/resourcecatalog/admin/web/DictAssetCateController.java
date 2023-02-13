package com.digitalchina.resourcecatalog.admin.web;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalchina.resourcecatalog.admin.annotation.RequiresPermissionsDesc;
import com.digitalchina.resourcecatalog.admin.service.LogHelper;
import com.digitalchina.resourcecatalog.admin.util.UserInfo;
import com.digitalchina.resourcecatalog.core.util.PageUtil;
import com.digitalchina.resourcecatalog.core.util.ResponseUtil;
import com.digitalchina.resourcecatalog.db.domain.*;
//import com.digitalchina.resourcecatalog.db.rabbitmq.RabbitConfig;
import com.digitalchina.resourcecatalog.db.service.CataInfoHistoryTypeRelService;
import com.digitalchina.resourcecatalog.db.service.CataInfoTempTypeRelService;
import com.digitalchina.resourcecatalog.db.service.CataUserTypeRelService;
import com.digitalchina.resourcecatalog.db.service.DictAssetCateService;
//import com.digitalchina.resourcecatalog.db.service.MqResourceService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 目录分类管理 前端控制器
 * </p>
 *
 * @author baokd
 * @since 2020-05-12
 */
@RestController
@RequestMapping("/admin/dictAssetCate")
@Api(value = "目录分类管理", tags = "目录分类管理")
public class DictAssetCateController {
    @Autowired
    private DictAssetCateService dictAssetCateService;

    @Autowired
    private CataInfoTempTypeRelService cataInfoTempTypeRelService;

    @Autowired
    private CataInfoHistoryTypeRelService cataInfoHistoryTypeRelService;

    @Autowired
    private CataUserTypeRelService cataUserTypeRelService;

//    @Autowired
//    private MqResourceService mqResourceService;

    @Autowired
    private LogHelper logHelper;

    @RequiresPermissions("dict:assetcate:list")
    @RequiresPermissionsDesc(menu = {"系统管理", "目录分类管理"}, button = "分页")
    @GetMapping("/list")
    @ApiOperation("分页")
    public Object list(String name, Integer pid,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit) {
        QueryWrapper<DictAssetCate> qw = new QueryWrapper<DictAssetCate>();
        if (!StringUtils.isEmpty(pid)) {
            qw.eq(DictAssetCate.PAR_TYP_ID, pid);
        } else {
            qw.isNull(DictAssetCate.PAR_TYP_ID);
        }
        if (!StringUtils.isEmpty(name)) {
            qw.like(DictAssetCate.TYP_NM, name);
        }
        if (UserInfo.isDeptAdmin()) {
            //如果是部门管理员，那么需要按照权限展示
            List<Integer> typeIdList = new ArrayList<Integer>();
            List<CataUserTypeRel> cataUserTypeRelList = cataUserTypeRelService.list(
                    new QueryWrapper<CataUserTypeRel>().lambda().eq(CataUserTypeRel::getUserId, UserInfo.getId()));
            if (StringUtils.isEmpty(cataUserTypeRelList) || cataUserTypeRelList.size() == 0) {
                return ResponseUtil.ok(PageUtil.startPage(new ArrayList(), 1, 10));
            } else {
                for (CataUserTypeRel cataUserTypeRel : cataUserTypeRelList) {
                    typeIdList.add(cataUserTypeRel.getTypeId());
                }
                qw.in(DictAssetCate.TYP_ID, typeIdList);
            }
        }
        qw.orderByAsc(DictAssetCate.DISPLAY_SN);
        qw.orderByAsc(DictAssetCate.TYP_CD);
        Page page1 = new Page<>(page, limit);
        return ResponseUtil.ok(dictAssetCateService.page(page1, qw));
    }

    @RequiresPermissions("dict:assetcate:get")
    @RequiresPermissionsDesc(menu = {"系统管理", "目录分类管理"}, button = "详情")
    @GetMapping("/get")
    @ApiOperation("详情")
    public Object get(@RequestParam Integer id) {
        DictAssetCate dictAssetCate = dictAssetCateService.getById(id);
        return ResponseUtil.ok(dictAssetCate);
    }

    @RequiresPermissions("dict:assetcate:add")
    @RequiresPermissionsDesc(menu = {"系统管理", "目录分类管理"}, button = "保存")
    @PostMapping("/add")
    @ApiOperation("保存")
    public Object add(@RequestBody DictAssetCate dictAssetCate) {
        //检查必填字段
        if (StringUtils.isEmpty(dictAssetCate.getTypNm())) {
            return ResponseUtil.fail(9001, "分类名称不能为空！");
        }
        if (StringUtils.isEmpty(dictAssetCate.getTypCd())) {
            return ResponseUtil.fail(9001, "分类编码不能为空！");
        }
        if (StringUtils.isEmpty(dictAssetCate.getDisplaySn())) {
            return ResponseUtil.fail(9001, "显示序号不能为空！");
        }
//        if (!dictAssetCate.getFullTypCd().equals("3") && dictAssetCate.getFullTypCd().startsWith("3")
//                && dictAssetCate.getFullTypCd().length() >= 6) {
//            return ResponseUtil.fail(9001, "部门类目录分类部门ID不能为空！");
//        }
        QueryWrapper<DictAssetCate> qw = new QueryWrapper<DictAssetCate>();
        if (StringUtils.isEmpty(dictAssetCate.getParTypId())) {
            qw.isNull(DictAssetCate.PAR_TYP_ID);
        } else {
            qw.eq(DictAssetCate.PAR_TYP_ID, dictAssetCate.getParTypId());
        }
        qw.eq(DictAssetCate.TYP_CD, dictAssetCate.getTypCd());
        if (dictAssetCateService.count(qw) > 0) {
            return ResponseUtil.fail(9001, "编码代码已存在，请重新生成！");
        }
        QueryWrapper<DictAssetCate> qw1 = new QueryWrapper<DictAssetCate>();
        if (StringUtils.isEmpty(dictAssetCate.getParTypId())) {
            qw1.isNull(DictAssetCate.PAR_TYP_ID);
        } else {
            qw1.eq(DictAssetCate.PAR_TYP_ID, dictAssetCate.getParTypId());
        }
        qw1.eq(DictAssetCate.TYP_NM, dictAssetCate.getTypNm());
        if (dictAssetCateService.count(qw1) > 0) {
            return ResponseUtil.fail(9001, "分类名称已存在！");
        }
        dictAssetCate.setCreator(UserInfo.getUsername());
        dictAssetCate.setCrtDt(LocalDateTime.now());
        dictAssetCate.setStatus("1");
        if (!UserInfo.isDeptAdmin() || dictAssetCate.getFullTypCd().length() == 1) {
            //如果不是部门管理员，直接保存
            if (!dictAssetCateService.save(dictAssetCate)) {
                return ResponseUtil.fail();
            }
        } else {
            //如果是部门管理员，需要更新cata_user_type_rel表
            if (!dictAssetCateService.saveAndUpdateUserType(dictAssetCate, UserInfo.getId())) {
                return ResponseUtil.fail();
            }
        }
        logHelper.logGeneralSucceed("目录分类管理", "新增信息资源分类：" + dictAssetCate.getTypNm() + "/" + dictAssetCate.getTypCd() + "/" + dictAssetCate.getFullTypCd());
//        mqResourceService.sendMessage(RabbitConfig.DICT_UPDATE, dictAssetCate);
        return ResponseUtil.ok(dictAssetCate);
    }

    @RequiresPermissions("dict:assetcate:update")
    @RequiresPermissionsDesc(menu = {"系统管理", "目录分类管理"}, button = "更新")
    @PostMapping("/update")
    @ApiOperation("更新")
    public Object update(@RequestBody DictAssetCate dictAssetCate) {
        if (StringUtils.isEmpty(dictAssetCate.getTypNm())) {
            return ResponseUtil.fail(9001, "分类名称不能为空");
        }
        if (StringUtils.isEmpty(dictAssetCate.getTypCd())) {
            return ResponseUtil.fail(9001, "分类编码不能为空");
        }
        if (StringUtils.isEmpty(dictAssetCate.getDisplaySn())) {
            return ResponseUtil.fail(9001, "显示序号不能为空");
        }
        if (!dictAssetCate.getTypCd().equals("3") && dictAssetCate.getTypCd().startsWith("3")
                && dictAssetCate.getFullTypCd().length() >= 6 && StringUtils.isEmpty(dictAssetCate.getOrgId())) {
            return ResponseUtil.fail(9001, "部门类目录分类部门ID不能为空！");
        }
        QueryWrapper<DictAssetCate> qw = new QueryWrapper<DictAssetCate>();
        if (StringUtils.isEmpty(dictAssetCate.getParTypId())) {
            qw.isNull(DictAssetCate.PAR_TYP_ID);
        } else {
            qw.eq(DictAssetCate.PAR_TYP_ID, dictAssetCate.getParTypId());
        }
        qw.eq(DictAssetCate.TYP_NM, dictAssetCate.getTypNm());
        qw.ne(DictAssetCate.TYP_ID, dictAssetCate.getTypId());
        if (dictAssetCateService.count(qw) > 0) {
            return ResponseUtil.fail(9001, "分类名称已存在！");
        }
        dictAssetCate.setUpdater(UserInfo.getUsername());
        dictAssetCate.setUpdateDt(LocalDateTime.now());
        if (!dictAssetCateService.updateById(dictAssetCate)) {
            return ResponseUtil.fail();
        }
        logHelper.logGeneralSucceed("目录分类管理", "编辑信息资源分类：" + dictAssetCate.getTypNm() + "/" + dictAssetCate.getTypCd() + "/" + dictAssetCate.getFullTypCd());
//        mqResourceService.sendMessage(RabbitConfig.DICT_UPDATE, dictAssetCate);
        return ResponseUtil.ok(dictAssetCate);
    }

    @RequiresPermissions("dict:assetcate:delete")
    @RequiresPermissionsDesc(menu = {"系统管理", "目录分类管理"}, button = "删除")
    @GetMapping("/delete")
    @ApiOperation("删除")
    public Object delete(@RequestParam Integer id) {
        DictAssetCate dictAssetCate = dictAssetCateService.getById(id);
        if (!StringUtils.isEmpty(dictAssetCate)) {
            QueryWrapper<DictAssetCate> qw = new QueryWrapper<DictAssetCate>();
            //首先判断是否叶子节点
            qw.eq(DictAssetCate.PAR_TYP_ID, id);
            if (dictAssetCateService.count(qw) > 0) {
                return ResponseUtil.fail(9001, "不是叶子节点，无法删除！");
            }
            //判断是否与暂存表或历史表有关联关系
//        QueryWrapper<CataInfoTempTypeRel> qw1 = new QueryWrapper<CataInfoTempTypeRel>();
//        qw1.eq(CataInfoTempTypeRel.TYPE_ID,id);
//        boolean tempTypeRel = (cataInfoTempTypeRelService.count(qw1) > 0) ? true : false;
//        QueryWrapper<CataInfoHistoryTypeRel> qw2 = new QueryWrapper<CataInfoHistoryTypeRel>();
//        qw1.eq(CataInfoHistoryTypeRel.TYPE_ID,id);
//        boolean HistoryTypeRel = (cataInfoHistoryTypeRelService.count(qw2) > 0) ? true : false;
//        if(tempTypeRel || HistoryTypeRel){
//            return ResponseUtil.fail(9001,"该分类已关联信息资源，请先移除关系后再删除！");
//        }
            if (cataInfoTempTypeRelService.count(new QueryWrapper<CataInfoTempTypeRel>().lambda().eq(CataInfoTempTypeRel::getTypeId, id)) > 0
                    || cataInfoHistoryTypeRelService.count(new QueryWrapper<CataInfoHistoryTypeRel>().lambda().eq(CataInfoHistoryTypeRel::getTypeId, id)) > 0) {
                return ResponseUtil.fail(9001, "该分类已关联信息资源，请先移除关系后再删除！");
            }
            //没有关联并且是叶子节点，可删除
            if (dictAssetCateService.deleteAndUpdateUserType(dictAssetCate)) {
                logHelper.logGeneralSucceed("目录分类管理", "删除信息资源分类：" + dictAssetCate.getTypNm() + "/" + dictAssetCate.getTypCd() + "/" + dictAssetCate.getFullTypCd());
//                mqResourceService.sendMessage(RabbitConfig.DICT_DELETE, dictAssetCate);
                return ResponseUtil.ok();
            } else {
                return ResponseUtil.fail();
            }
        } else {
            return ResponseUtil.fail(9001, "ID不正确，未找到相关目录分类！");
        }

    }

    @RequiresAuthentication
    @GetMapping("/getTreeByPid")
    @ApiOperation("获取子节点")
    public Object getTreeByPid(@RequestParam(required = false) Integer pId) {
        QueryWrapper<DictAssetCate> qw = new QueryWrapper<DictAssetCate>();
        if (StringUtils.isEmpty(pId)) {
            qw.isNull(DictAssetCate.PAR_TYP_ID);
        } else {
            qw.eq(DictAssetCate.PAR_TYP_ID, pId);
        }
        qw.eq(DictAssetCate.STATUS, "1");
        qw.orderByAsc(DictAssetCate.DISPLAY_SN);
        qw.orderByAsc(DictAssetCate.TYP_CD);
        return ResponseUtil.ok(dictAssetCateService.list(qw));
    }

    //    @RequiresPermissions("dict:assetcate:generateCode")
//    @RequiresPermissionsDesc(menu = {"系统管理", "目录分类管理"}, button = "生成编码")
    @RequiresAuthentication
    @GetMapping("/generateCode")
    @ApiOperation("生成编码")
    public Object generateCode(@RequestParam(required = false) Integer pId) {
        Integer oldCode = dictAssetCateService.getMaxCodeByPid(pId);
        oldCode = (oldCode == null ? 0 : oldCode);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (StringUtils.isEmpty(pId)) {
            //如果为NULL，则是类目录,类目录不能大于9
            if (oldCode >= 9) {
                return ResponseUtil.fail(9001, "类编码最大不超过9！");
            } else {
                String newCode = Integer.toString(oldCode + 1);
                resultMap.put("code", newCode);
                resultMap.put("fullCode", newCode);
                resultMap.put("typType", "类");
                return ResponseUtil.ok(resultMap);
            }
        } else {
            DictAssetCate dictAssetCate = dictAssetCateService.getById(pId);
            //如果不为NULL,则通过full_typ_cd（详细编码）来判断是哪一层
            String currentFullTypeCode = dictAssetCate.getFullTypCd();
            if (currentFullTypeCode.length() == 1) {
                //如果当前详细编码长度为1，则是项目录，二位编码，不能大于99
                if (oldCode >= 99) {
                    return ResponseUtil.fail(9001, "项编码最大不超过99！");
                } else {
                    String newCode = Integer.toString(oldCode + 1);
                    newCode = newCode.length() == 2 ? newCode : "0" + newCode;
                    resultMap.put("code", newCode);
                    resultMap.put("fullCode", currentFullTypeCode + newCode);
                    resultMap.put("typType", "项");
                    return ResponseUtil.ok(resultMap);
                }
            } else if (currentFullTypeCode.length() == 3) {
                //如果当前详细编码长度为3，则是目目录，三位编码，不能大于999
                if (oldCode >= 999) {
                    return ResponseUtil.fail(9001, "目编码最大不超过999！");
                } else {
                    String newCode = Integer.toString(oldCode + 1);
                    newCode = newCode.length() == 3 ? newCode : (newCode.length() == 2 ? "0" + newCode : "00" + newCode);
                    resultMap.put("code", newCode);
                    resultMap.put("fullCode", currentFullTypeCode + newCode);
                    resultMap.put("typType", "目");
                    return ResponseUtil.ok(resultMap);
                }
            } else {
                //如果不是1或者3，则是细目，二位编码，不能大于99
                if (oldCode >= 99) {
                    return ResponseUtil.fail(9001, "细目编码最大不超过99！");
                } else {
                    String newCode = Integer.toString(oldCode + 1);
                    newCode = newCode.length() == 2 ? newCode : "0" + newCode;
                    resultMap.put("code", newCode);
                    resultMap.put("fullCode", currentFullTypeCode + newCode);
                    resultMap.put("typType", "细目");
                    resultMap.put("orgId", dictAssetCate.getOrgId());
                    return ResponseUtil.ok(resultMap);
                }
            }
        }
    }

    @RequiresPermissions("dict:assetcate:isDisabled")
    @RequiresPermissionsDesc(menu = {"系统管理", "目录分类管理"}, button = "禁用/启用")
    @GetMapping("/isDisabled")
    @ApiOperation("禁用/启用")
    public Object isDisabled(@RequestParam Integer id, String status) {
        DictAssetCate dictAssetCate = dictAssetCateService.getById(id);
        dictAssetCate.setStatus(status);
        dictAssetCateService.updateById(dictAssetCate);
        logHelper.logGeneralSucceed("目录分类管理", ("1".equals(status) ? "启用" : "禁用") + "信息资源分类：" + dictAssetCate.getTypNm() + "/" + dictAssetCate.getTypCd() + "/" + dictAssetCate.getFullTypCd());
//        mqResourceService.sendMessage("1".equals(status) ? RabbitConfig.DICT_UPDATE : RabbitConfig.DICT_DELETE, dictAssetCate);
        return ResponseUtil.ok();
    }

    @RequiresAuthentication
    @GetMapping("/getTreeByCodeAndOrgId")
    @ApiOperation("按Code获取分类树")
    public Object getTreeByCodeAndOrgId(@RequestParam String code,
                                        @RequestParam(required = false) Integer orgId) {
        //通过CODE获取这条记录ID，然后查询树返回
        QueryWrapper<DictAssetCate> qw = new QueryWrapper<DictAssetCate>();
        qw.eq(DictAssetCate.FULL_TYP_CD, code);
        DictAssetCate dictAssetCate = dictAssetCateService.getOne(qw);
        List<Integer> orgIds = new ArrayList<Integer>();
        if (StringUtils.isEmpty(orgId)) {
            orgIds = null;
        } else {
            orgIds.add(orgId);
        }
        return ResponseUtil.ok(dictAssetCateService.getTreeByPidAndOrgId(dictAssetCate, orgIds));
    }

    //    @RequiresPermissions("dict:assetcate:getTree")
//    @RequiresPermissionsDesc(menu = {"系统管理", "目录分类管理"}, button = "获取全部树")
    @RequiresAuthentication
    @GetMapping("/getTree")
    @ApiOperation("获取全部树")
    public Object getTree(@RequestParam(required = false) Integer pId) {
        return ResponseUtil.ok(dictAssetCateService.getTreeByPid(pId));
    }

    @RequiresAuthentication
    @GetMapping("/getTreeByPower")
    @ApiOperation("根据权限获取全部树")
    public Object getTreeByPower() {
        return ResponseUtil.ok(dictAssetCateService.getTreeByPower(UserInfo.getId()));
    }

    @RequiresAuthentication
    @GetMapping("/getOrderNum")
    @ApiOperation("查询目录分类显示序号")
    public Object getOrderNum(@RequestParam(required = true) Integer pId) {
        int display = 1;
        LambdaQueryWrapper<DictAssetCate> queryWrapper = new QueryWrapper<DictAssetCate>().lambda()
                .eq(DictAssetCate::getParTypId, pId).orderByDesc(DictAssetCate::getDisplaySn).last(" limit 1");
        List<DictAssetCate> list = dictAssetCateService.list(queryWrapper);
        if (list != null && list.size() != 0) {
            display += list.get(0).getDisplaySn();
        }
        return ResponseUtil.ok(display);
    }
}
