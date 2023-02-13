package com.digitalchina.resourcecatalog.admin.web;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalchina.resourcecatalog.admin.annotation.RequiresPermissionsDesc;
import com.digitalchina.resourcecatalog.admin.service.LogHelper;
import com.digitalchina.resourcecatalog.admin.util.UserInfo;
import com.digitalchina.resourcecatalog.core.util.ResponseUtil;
import com.digitalchina.resourcecatalog.db.domain.*;
import com.digitalchina.resourcecatalog.db.service.ArchBusiUviewExService;
import com.digitalchina.resourcecatalog.db.service.SysDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 * @author LiangChuanQi
 * @since 2020-05-09
 */
@RestController
@RequestMapping("/admin/sysDict")
@Api(value = "数据编码管理", tags = "数据编码管理")
public class SysDictController {
    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private ArchBusiUviewExService archBusiUviewExService;

    @Autowired
    private LogHelper logHelper;

    @RequiresPermissions("sys:dict:list")
    @RequiresPermissionsDesc(menu = {"系统管理", "数据编码管理"}, button = "分页")
    @GetMapping("/list")
    @ApiOperation("分页")
    public Object list(String name, String value, Integer pid,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit) {
        QueryWrapper<SysDict> qw = new QueryWrapper<SysDict>();
        if (!StringUtils.isEmpty(pid)) {
            qw.eq(SysDict.PID, pid);
        } else {
            qw.isNull(SysDict.PID);
        }
        if (!StringUtils.isEmpty(name)) {
            qw.like(SysDict.NAME, name);
        }
        if (!StringUtils.isEmpty(value)) {
            qw.like(SysDict.VALUE, value);
        }
        Page page1 = new Page<>(page, limit);
        return ResponseUtil.ok(sysDictService.page(page1, qw));
    }

    @RequiresPermissions("sys:dict:get")
    @RequiresPermissionsDesc(menu = {"系统管理", "数据编码管理"}, button = "详情")
    @GetMapping("/get")
    @ApiOperation("详情")
    public Object get(@RequestParam Integer id) {
        SysDict sysDict = sysDictService.getById(id);
        return ResponseUtil.ok(sysDict);
    }

    @RequiresPermissions("sys:dict:add")
    @RequiresPermissionsDesc(menu = {"系统管理", "数据编码管理"}, button = "保存")
    @PostMapping("/add")
    @ApiOperation("保存")
    public Object add(@RequestBody SysDict sysDict) {
        //检查必填字段
        if (StringUtils.isEmpty(sysDict.getName())) {
            return ResponseUtil.fail(9001, "编码名称不能为空");
        }
        if (StringUtils.isEmpty(sysDict.getValue())) {
            return ResponseUtil.fail(9001, "编码代码不能为空");
        }
        if (StringUtils.isEmpty(sysDict.getType())) {
            return ResponseUtil.fail(9001, "编码类型不能为空");
        }
        if (StringUtils.isEmpty(sysDict.getPid())) {
            return ResponseUtil.fail(9001, "上级编码不能为空");
        }
        QueryWrapper<SysDict> qw = new QueryWrapper<SysDict>();
        qw.eq(SysDict.PID, sysDict.getPid());
//        qw.eq(SysDict.TYPE,sysDict.getType());
        qw.eq(SysDict.IS_INTER, 0);
        List<SysDict> sysDictList = sysDictService.list(qw);
        if (sysDictList != null && sysDictList.size() > 0) {
            //判断本层级有没有，如果有，则TYPE必须跟本层级相同
            if (!sysDict.getType().equals(sysDictList.get(0).getType())) {
                return ResponseUtil.fail(9001, "本层级已有字典，请保证同级字典TYPE相同");
            }
        }
        qw.eq(SysDict.VALUE, sysDict.getValue());
        if (sysDictService.count(qw) > 0) {
            return ResponseUtil.fail(9001, "本层级编码代码已存在");
        }
        sysDict.setCreateBy(UserInfo.getId());
        sysDict.setCreateDate(LocalDateTime.now());
        if (!sysDictService.save(sysDict)) {
            return ResponseUtil.fail();
        }
        logHelper.logGeneralSucceed("数据编码管理", "新增数据编码：" + sysDict.getName() + "/" + sysDict.getValue());
        return ResponseUtil.ok(sysDict);
    }

    @RequiresPermissions("sys:dict:update")
    @RequiresPermissionsDesc(menu = {"系统管理", "数据编码管理"}, button = "更新")
    @PostMapping("/update")
    @ApiOperation("更新")
    public Object update(@RequestBody SysDict sysDict) {
        //检查必填字段
        if (StringUtils.isEmpty(sysDict.getName())) {
            return ResponseUtil.fail(9001, "编码名称不能为空");
        }
        if (StringUtils.isEmpty(sysDict.getValue())) {
            return ResponseUtil.fail(9001, "编码代码不能为空");
        }
        if (StringUtils.isEmpty(sysDict.getType())) {
            return ResponseUtil.fail(9001, "编码类型不能为空");
        }
        if (StringUtils.isEmpty(sysDict.getPid())) {
            return ResponseUtil.fail(9001, "上级编码不能为空");
        }
        QueryWrapper<SysDict> qw = new QueryWrapper<SysDict>();
        qw.eq(SysDict.PID, sysDict.getPid());
//        qw.eq(SysDict.TYPE,sysDict.getType());
        qw.eq(SysDict.IS_INTER, 0);
        List<SysDict> sysDictList = sysDictService.list(qw);
        if (sysDictList != null && sysDictList.size() > 0) {
            //判断本层级有没有，如果有，则TYPE必须跟本层级相同
            if (!sysDict.getType().equals(sysDictList.get(0).getType())) {
                return ResponseUtil.fail(9001, "本层级已有字典，请保证同级字典TYPE相同");
            }
        }
        qw.eq(SysDict.VALUE, sysDict.getValue());
        qw.ne(SysDict.ID, sysDict.getId());
        if (sysDictService.count(qw) > 0) {
            return ResponseUtil.fail(9001, "本层级编码代码已存在");
        }
        sysDict.setUpdateBy(UserInfo.getId());
        sysDict.setUpdateDate(LocalDateTime.now());
        if (!sysDictService.updateById(sysDict)) {
            return ResponseUtil.updatedDataFailed();
        }

        logHelper.logGeneralSucceed("数据编码管理", "编辑数据编码：" + sysDict.getName() + "/" + sysDict.getValue());
        return ResponseUtil.ok(sysDict);
    }

    @RequiresPermissions("sys:dict:delete")
    @RequiresPermissionsDesc(menu = {"系统管理", "数据编码管理"}, button = "删除")
    @GetMapping("/delete")
    @ApiOperation("删除")
    public Object delete(@RequestParam Integer id) {
        SysDict sysDict = sysDictService.getById(id);
        if (!StringUtils.isEmpty(sysDict)) {
            //判断如果不是叶子节点，则提示不能删除
            if (sysDictService.count(new QueryWrapper<SysDict>().lambda().eq(SysDict::getPid, id)) > 0) {
                return ResponseUtil.fail(9001, "不是叶子节点，无法删除！");
            }
            if (sysDictService.removeById(id)) {
                logHelper.logGeneralSucceed("数据编码管理", "删除数据编码：" + sysDict.getName() + "/" + sysDict.getValue());
                return ResponseUtil.ok();
            } else {
                return ResponseUtil.fail();
            }
        } else {
            return ResponseUtil.fail(9001, "ID不正确，未找到对应数据编码！");
        }

        // 批量删除节点及其叶子节点，暂时先注释掉。sysDictService.deleteTree(id);

    }

    //    @RequiresPermissions("sys:dict:getTree")
//    @RequiresPermissionsDesc(menu = {"系统管理", "数据编码管理"}, button = "获取树")
    @RequiresAuthentication
    @GetMapping("/getTree")
    @ApiOperation("获取树")
    public Object getTree() {
        return ResponseUtil.ok(sysDictService.getTree());
    }

    //    @RequiresPermissions("sys:dict:getByType")
//    @RequiresPermissionsDesc(menu = {"系统管理", "数据编码管理"}, button = "通过类型获取列表")
    @RequiresAuthentication
    @GetMapping("/getByType")
    @ApiOperation("通过类型获取列表")
    public Object getByType(@RequestParam String type) {
        if (StringUtils.isEmpty(type)) {
            return ResponseUtil.fail(9001, "编码类型不能为空");
        }
        return ResponseUtil.ok(sysDictService.list(new QueryWrapper<SysDict>().lambda().eq(SysDict::getType, type)));
    }

    @RequiresAuthentication
    @GetMapping("/getNotUseList")
    @ApiOperation("获取未使用过的列表")
    public Object getNotUseList(@RequestParam Integer pid,
                                @RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "10") Integer limit) {
        Page page1 = new Page<>(page, limit);
        return ResponseUtil.ok(sysDictService.getNotUseList(page1, pid));
    }

    @RequiresAuthentication
    @GetMapping("/getAnalysisList")
    @ApiOperation("获取与资源目录关联分析")
    public Object getNotUseList(@RequestParam Integer id) {
        Map<String, Object> resultMap = new HashMap<>();

        SysDict sysDict = sysDictService.getById(id);

        QueryWrapper<ArchBusiUviewEx> qw = new QueryWrapper<ArchBusiUviewEx>();
        qw.eq(ArchBusiUviewEx.MEDIA_FMT_TYPE, sysDict.getValue());
        qw.select().orderByAsc("crt_dt");
        List<ArchBusiUviewEx> archBusiUviewExList = archBusiUviewExService.list(qw);
        if (archBusiUviewExList.size() > 0) {
            resultMap.put("from", archBusiUviewExList.get(0));
            if (archBusiUviewExList.size() > 1) {
                resultMap.put("to", archBusiUviewExList.subList(1, archBusiUviewExList.size()));
            }
        } else {
            resultMap.put("from", "");
            resultMap.put("to", "");
        }
        resultMap.put("sysDict", sysDict);

        return ResponseUtil.ok(resultMap);
    }
}
