package com.digitalchina.resourcecatalog.admin.web;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalchina.resourcecatalog.admin.annotation.RequiresPermissionsDesc;
import com.digitalchina.resourcecatalog.admin.dto.FileUploadRelDto;
import com.digitalchina.resourcecatalog.admin.service.LogHelper;
import com.digitalchina.resourcecatalog.admin.util.UserInfo;
import com.digitalchina.resourcecatalog.core.util.BeanUtils;
import com.digitalchina.resourcecatalog.core.util.ResponseUtil;
import com.digitalchina.resourcecatalog.db.domain.FileUploadRel;
import com.digitalchina.resourcecatalog.db.service.FileUploadRelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 文件上传关联表 前端控制器
 * </p>
 *
 * @author wangmh
 * @since 2020-05-21
 */
@RestController
@Api(value = "文件上报", tags = "文件上报")
@RequestMapping("/admin/fileUploadRel")
public class FileUploadRelController {
    @Autowired
    LogHelper logHelper;

    @Autowired
    FileUploadRelService fileUploadRelService;

    @GetMapping("/list1")
    @RequiresPermissions("admin:fileUploadRel:list1")
    @RequiresPermissionsDesc(menu = {"文件上报", "文件上报"}, button = "查询")
    @ApiOperation("文件上报-分页")
    public Object list1(@RequestParam(required = false) String name,
                        @RequestParam(defaultValue = "1") Integer current,
                        @RequestParam(defaultValue = "10") Integer size,
                        @RequestParam(required = false) String status,
                        @RequestParam(required = false) String startTime,
                        @RequestParam(required = false) String endTime,
                        @RequestParam(required = false, defaultValue = "") Integer deptId) {
        IPage page = new Page<>(current, size);
        if (UserInfo.isDeptAdmin()) {
            return ResponseUtil.ok(fileUploadRelService.page(page, name, status, startTime, endTime, UserInfo.getId(), deptId));
        }
        return ResponseUtil.ok(fileUploadRelService.page(page, name, status, startTime, endTime, null, deptId));
    }

    @GetMapping("/list2")
    @RequiresPermissions("admin:fileUploadRel:list2")
    @RequiresPermissionsDesc(menu = {"文件上报", "文件审核"}, button = "查询")
    @ApiOperation("文件审核-分页")
    public Object list2(@RequestParam(required = false) String name,
                        @RequestParam(defaultValue = "1") Integer current,
                        @RequestParam(defaultValue = "10") Integer size,
                        @RequestParam(required = false) String status,
                        @RequestParam(required = false) String startTime,
                        @RequestParam(required = false) String endTime,
                        @RequestParam(required = false, defaultValue = "") Integer deptId) {
        IPage page = new Page<>(current, size);
        return ResponseUtil.ok(fileUploadRelService.page(page, name, status, startTime, endTime, null, deptId));
    }

    @GetMapping("/detail")
    @RequiresPermissions("admin:fileUploadRel:detail")
    @RequiresPermissionsDesc(menu = {"文件上报", "详情"}, button = "详情")
    @ApiOperation("文件上报|文件审核-详情")
    public Object detail(@RequestParam Integer id) {
        return ResponseUtil.ok(fileUploadRelService.detail(id));
    }
    
    @GetMapping("/delete")
    @RequiresPermissions("admin:fileUploadRel:delete")
    @RequiresPermissionsDesc(menu = {"文件上报", "删除"}, button = "删除")
    @ApiOperation("文件上报-删除")
    public Object delete(@RequestParam Integer id) {
        return ResponseUtil.ok(fileUploadRelService.delete(id));
    }

    @PostMapping("/save")
    @RequiresPermissions("admin:fileUploadRel:save")
    @RequiresPermissionsDesc(menu = {"文件上报", "文件上报"}, button = "上传文件")
    @ApiOperation("文件上报-上传文件")
    public Object save(@Valid @RequestBody FileUploadRelDto fileUploadRelDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseUtil.fail(400, result.getFieldError().getDefaultMessage());
        }
        fileUploadRelDto.setId(null);
        FileUploadRel fileUploadRel = BeanUtils.dtoToDo(fileUploadRelDto, FileUploadRel.class);
        fileUploadRel.setUserId(UserInfo.getId());
        fileUploadRel.setStatus("0");
        if (fileUploadRelService.save(fileUploadRel)) {
            logHelper.logUpLoadFileSucceed("新增上报文件", fileUploadRel.getFileName());
            return ResponseUtil.ok();
        }
        return ResponseUtil.fail();
    }

    @PostMapping("/update")
    @RequiresPermissions("admin:fileUploadRel:update")
    @RequiresPermissionsDesc(menu = {"文件上报", "文件上报"}, button = "编辑")
    @ApiOperation("文件上报-编辑")
    public Object update(@Valid @RequestBody FileUploadRelDto fileUploadRelDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseUtil.fail(400, result.getFieldError().getDefaultMessage());
        }
        if (fileUploadRelDto.getId() == null) {
            return ResponseUtil.fail(400, "id不能为空！");
        }
        FileUploadRel fileUploadRel = BeanUtils.dtoToDo(fileUploadRelDto, FileUploadRel.class);
        fileUploadRel.setUserId(UserInfo.getId());
        fileUploadRel.setStatus("0");
        fileUploadRelService.updateInfo(fileUploadRel);
        logHelper.logUpLoadFileSucceed("编辑上报文件", fileUploadRel.getFileName());
        return ResponseUtil.ok();
    }


    @PostMapping("/reviewed")
    @RequiresPermissions("admin:fileUploadRel:reviewed")
    @RequiresPermissionsDesc(menu = {"文件上报", "文件审核"}, button = "审核")
    @ApiOperation("文件审核-审核")
    public Object reviewed(@ApiParam(value = "{\"id\":1,\"status\":\"1\",\"comment\":\"\"}") @RequestBody Map<String, Object> map) {
        Integer id = Integer.parseInt(map.get(FileUploadRel.ID).toString());
        if (id == null) {
            return ResponseUtil.fail(400, "id不能为空！");
        }
        String status = map.get(FileUploadRel.STATUS).toString();
        if (StringUtils.isEmpty(status)) {
            return ResponseUtil.fail(400, "审核状态不能为空！");
        }
        FileUploadRel fileUploadRel = new FileUploadRel();
        fileUploadRel.setId(id);
        fileUploadRel.setStatus(status);
        fileUploadRel.setComment(map.get(FileUploadRel.COMMENT).toString());
        fileUploadRel.setUpdateBy(UserInfo.getUsername());
        fileUploadRel.setUpdateTime(new Date());
        if (fileUploadRelService.updateById(fileUploadRel)) {
            FileUploadRel byId = fileUploadRelService.getById(fileUploadRel.getId());
            logHelper.logUpLoadFileSucceed("审核上报文件", byId.getFileName());
            return ResponseUtil.ok();
        }
        return ResponseUtil.fail();
    }

}
