package com.digitalchina.resourcecatalog.admin.web;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalchina.resourcecatalog.admin.annotation.RequiresPermissionsDesc;
import com.digitalchina.resourcecatalog.admin.dto.FileCenterRelDto;
import com.digitalchina.resourcecatalog.admin.service.LogHelper;
import com.digitalchina.resourcecatalog.admin.util.UserInfo;
import com.digitalchina.resourcecatalog.core.util.BeanUtils;
import com.digitalchina.resourcecatalog.core.util.ResponseUtil;
import com.digitalchina.resourcecatalog.db.domain.FileCenterRel;
import com.digitalchina.resourcecatalog.db.service.FileCenterRelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 资料中心文件关联表 前端控制器
 * </p>
 *
 * @author wangmh
 * @since 2020-05-21
 */
@RestController
@Api(value = "资料中心", tags = "资料中心")
@RequestMapping("/admin/fileCenterRel")
public class FileCenterRelController {

    @Autowired
    LogHelper logHelper;

    @Autowired
    FileCenterRelService fileCenterRelService;

    @GetMapping("/list1")
    @RequiresPermissions("admin:fileCenterRel:list1")
    @RequiresPermissionsDesc(menu = {"资料中心", "资料发布"}, button = "查询")
    @ApiOperation("资料发布-分页")
    public Object list1(@RequestParam(required = false) String fileName,
                        @RequestParam(defaultValue = "1") Integer current,
                        @RequestParam(defaultValue = "10") Integer size) {
        IPage page = new Page<>(current, size);
        return ResponseUtil.ok(fileCenterRelService.myPage(page, fileName));
    }

    @GetMapping("/list2")
    @RequiresPermissions("admin:fileCenterRel:list2")
    @RequiresPermissionsDesc(menu = {"资料中心", "资料下载"}, button = "查询")
    @ApiOperation("资料下载-分页")
    public Object list2(@RequestParam(required = false) String fileName,
                        @RequestParam(defaultValue = "1") Integer current,
                        @RequestParam(defaultValue = "10") Integer size) {
        IPage page = new Page<>(current, size);
        return ResponseUtil.ok(fileCenterRelService.myPage(page, fileName));
    }

    @PostMapping("/save")
    @RequiresPermissions("admin:fileCenterRel:save")
    @RequiresPermissionsDesc(menu = {"资料中心", "资料发布"}, button = "上传文件")
    @ApiOperation("资料发布-上传文件")
    public Object save(@Valid @RequestBody FileCenterRelDto fileCenterRelDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseUtil.fail(400, result.getFieldError().getDefaultMessage());
        }
        fileCenterRelDto.setId(null);
        FileCenterRel fileCenterRel = BeanUtils.dtoToDo(fileCenterRelDto, FileCenterRel.class);
        fileCenterRel.setCreateBy(UserInfo.getUsername());
        fileCenterRel.setCreateTime(new Date());
        fileCenterRel.setDwCount(0);
        if (fileCenterRelService.save(fileCenterRel)) {
            logHelper.logUpLoadFileSucceed("新增文件", fileCenterRel.getFileName());
            return ResponseUtil.ok();
        }
        return ResponseUtil.fail();
    }

    @PostMapping("/update")
    @RequiresPermissions("admin:fileCenterRel:update")
    @RequiresPermissionsDesc(menu = {"资料中心", "资料发布"}, button = "编辑")
    @ApiOperation("资料发布-编辑")
    public Object update(@Valid @RequestBody FileCenterRelDto fileCenterRelDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseUtil.fail(400, result.getFieldError().getDefaultMessage());
        }
        if (fileCenterRelDto.getId() == null) {
            return ResponseUtil.fail(400, "id不能为空");
        }
        FileCenterRel fileCenterRel = BeanUtils.dtoToDo(fileCenterRelDto, FileCenterRel.class);
        fileCenterRel.setCreateBy(UserInfo.getUsername());
        if (fileCenterRelService.updateInfo(fileCenterRel)) {
            logHelper.logUpLoadFileSucceed("编辑文件", fileCenterRel.getFileName());
            return ResponseUtil.ok();
        }
        return ResponseUtil.fail();
    }

    @PostMapping("/removeBatch")
    @RequiresPermissions("admin:fileCenterRel:removeBatch")
    @RequiresPermissionsDesc(menu = {"资料中心", "资料发布"}, button = "删除")
    @ApiOperation("资料发布-删除")
    public Object removeBatch(@RequestBody List<Integer> ids) {
        Collection<FileCenterRel> fileCenterRels = fileCenterRelService.listByIds(ids);
        String collect = fileCenterRels.stream().map(FileCenterRel::getFileName).collect(Collectors.joining("、"));
        if (ids == null || ids.size() < 1) {
            return ResponseUtil.fail(400, "ids不能为空");
        }
        if (fileCenterRelService.removeInfo(ids)) {
            logHelper.logUpLoadFileSucceed("删除文件", collect);
            return ResponseUtil.ok();
        }
        return ResponseUtil.fail();
    }

    @GetMapping("/addCount")
//    @RequiresPermissions("admin:fileCenterRel:addCount")
//    @RequiresPermissionsDesc(menu = {"资料中心", "资料发布"}, button = "下载")
    @RequiresAuthentication
    @ApiOperation("资料发布-增加下载次数")
    public Object addCount(@RequestParam Integer id) {
        FileCenterRel fileCenterRel = new FileCenterRel();
        fileCenterRel.setId(id);
        fileCenterRel.setDwCount(fileCenterRelService.getById(id).getDwCount() + 1);
        if (fileCenterRelService.updateById(fileCenterRel)) {
            return ResponseUtil.ok();
        }
        return ResponseUtil.fail();
    }


    @GetMapping("/detail")
    @RequiresPermissions("admin:fileCenterRel:detail")
    @RequiresPermissionsDesc(menu = {"资料中心", "资料发布"}, button = "详情")
    @ApiOperation("资料发布-详情")
    public Object detail(@RequestParam Integer id) {
        return ResponseUtil.ok(fileCenterRelService.detail(id));
    }
}
