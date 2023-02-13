package com.digitalchina.resourcecatalog.admin.web;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalchina.resourcecatalog.admin.annotation.RequiresPermissionsDesc;
import com.digitalchina.resourcecatalog.core.storage.StorageService;
import com.digitalchina.resourcecatalog.core.util.ResponseUtil;
import com.digitalchina.resourcecatalog.db.domain.SysStorage;
import com.digitalchina.resourcecatalog.db.service.SysStorageService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 文件存储表 前端控制器
 * </p>
 *
 * @author wangmh
 * @since 2020-04-30
 */
@RestController
@RequestMapping("/admin/storage")
@Api(value = "文件存储", tags = "文件存储")
public class SysStorageController {

    private final Log logger = LogFactory.getLog(SysStorageController.class);

    @Autowired
    private SysStorageService sysStorageService;

    @Autowired
    private StorageService storageService;

    @RequiresPermissions("admin:storage:list")
    @RequiresPermissionsDesc(menu = {"系统管理", "对象存储"}, button = "查询")
    @GetMapping("/list")
    @ApiOperation("分页")
    public Object list(String key, String name,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit) {
        Page page1 = new Page(page, limit);
        return ResponseUtil.ok(sysStorageService.page(page1, new QueryWrapper<SysStorage>().lambda().like(SysStorage::getKey, key)
                .like(SysStorage::getName, name).eq(SysStorage::getDeleted, 0).orderByDesc(SysStorage::getAddTime)));
    }

    //    @RequiresPermissions("admin:storage:create")
//    @RequiresPermissionsDesc(menu = {"系统管理", "对象存储"}, button = "上传")
    @PostMapping("/create")
    @ApiOperation("保存")
    @RequiresAuthentication
    public Object create(@RequestParam("file") MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        SysStorage sysStorage = storageService.store(file.getInputStream(), file.getSize(),
                file.getContentType(), originalFilename);
        return ResponseUtil.ok(sysStorage);
    }

    //    @RequiresPermissions("admin:storage:read")
//    @RequiresPermissionsDesc(menu = {"系统管理", "对象存储"}, button = "详情")
    @RequiresAuthentication
    @GetMapping("/read")
    public Object read(Integer id) {
        SysStorage storageInfo = sysStorageService.getById(id);
        if (storageInfo == null) {
            return ResponseUtil.badArgumentValue();
        }
        return ResponseUtil.ok(storageInfo);
    }

    @RequiresPermissions("admin:storage:update")
    @RequiresPermissionsDesc(menu = {"系统管理", "对象存储"}, button = "编辑")
    @PostMapping("/update")
    @ApiOperation("更新")
    public Object update(@RequestBody SysStorage sysStorage) {
        sysStorage.setUpdateTime(LocalDateTime.now());
        if (!sysStorageService.updateById(sysStorage)) {
            return ResponseUtil.updatedDataFailed();
        }
        return ResponseUtil.ok(sysStorage);
    }

    @RequiresPermissions("admin:storage:delete")
    @RequiresPermissionsDesc(menu = {"系统管理", "对象存储"}, button = "删除")
    @PostMapping("/delete")
    @ApiOperation("删除")
    public Object delete(@RequestBody SysStorage sysStorage) {
        String key = sysStorage.getKey();
        if (StringUtils.isEmpty(key)) {
            return ResponseUtil.badArgument();
        }
        SysStorage storage = new SysStorage();
        storage.setDeleted(1);
        sysStorageService.update(storage, new UpdateWrapper<SysStorage>().lambda().eq(SysStorage::getKey, key));
        return ResponseUtil.ok();
    }

    /**
     * 访问存储对象
     *
     * @param key 存储对象key
     * @return
     */
    @GetMapping("/fetch/{key:.+}")
//    @RequiresPermissions("admin:storage:fetch")
//    @RequiresPermissionsDesc(menu = {"系统管理", "对象存储"}, button = "下载")
//    @RequiresAuthentication
    @ApiOperation("下载")
    public ResponseEntity<Resource> fetch(@PathVariable String key) {
        if (key == null) {
            return ResponseEntity.notFound().build();
        }
        if (key.contains("../")) {
            return ResponseEntity.badRequest().build();
        }
        SysStorage storage = sysStorageService.getOne(new QueryWrapper<SysStorage>().eq(SysStorage.KEY, key).eq(SysStorage.DELETED, 0));
        if (storage == null) {
            return ResponseEntity.notFound().build();
        }
        String type = storage.getType();
        MediaType mediaType = MediaType.parseMediaType(type);

        Resource file = storageService.loadAsResource(key);
        if (file == null) {
            return ResponseEntity.notFound().build();
        }
        String filename = file.getFilename().trim();
        if (filename.endsWith(".rar") || filename.endsWith(".zip")) {
            mediaType = MediaType.parseMediaType("application/octet-stream");
        }
        if (filename.endsWith(".doc") || filename.endsWith(".docx") || filename.endsWith(".xls")
                || filename.endsWith(".xlsx") || filename.endsWith(".ppt") || filename.endsWith(".pptx")
                || filename.endsWith(".rar") || filename.endsWith(".zip") || filename.endsWith(".pdf") || filename.endsWith(".txt")) {
            try {
                return ResponseEntity.ok().contentType(mediaType).header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + URLEncoder.encode(storage.getName(), "UTF-8") + "\"").body(file);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return ResponseEntity.ok().contentType(mediaType).body(file);
    }

}
