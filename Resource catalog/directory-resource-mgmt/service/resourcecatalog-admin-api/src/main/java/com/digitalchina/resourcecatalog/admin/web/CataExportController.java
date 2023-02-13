package com.digitalchina.resourcecatalog.admin.web;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalchina.resourcecatalog.admin.annotation.RequiresPermissionsDesc;
import com.digitalchina.resourcecatalog.admin.service.LogHelper;
import com.digitalchina.resourcecatalog.admin.util.UserInfo;
import com.digitalchina.resourcecatalog.core.storage.StorageService;
import com.digitalchina.resourcecatalog.core.util.ResponseUtil;
import com.digitalchina.resourcecatalog.db.domain.ArchOrg;
import com.digitalchina.resourcecatalog.db.domain.CataExport;
import com.digitalchina.resourcecatalog.db.domain.SysStorage;
import com.digitalchina.resourcecatalog.db.service.ArchOrgService;
import com.digitalchina.resourcecatalog.db.service.CataExportService;
import com.digitalchina.resourcecatalog.db.service.SysStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.URLEncoder;
import java.util.Date;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhangys
 * @since 2020-05-19
 */
@RestController
@RequestMapping("/admin/cataExport")
@Api(value = "编制表导出", tags = "编制表导出")
public class CataExportController {
    @Autowired
    CataExportService cataExportService;
    @Autowired
    SysStorageService sysStorageService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private ArchOrgService archOrgService;
    @Autowired
    private LogHelper logHelper;

    @RequiresPermissions("cata:export:list")
    @RequiresPermissionsDesc(menu = {"目录编制", "编制表导出"}, button = "分页查询")
    @GetMapping("/list")
    @ApiOperation("分页查询")
    public Object list(@RequestParam Integer depId,
                       @RequestParam(required = false) @ApiParam(value = "开始日期，格式yyyy-mm-dd") String startDate,
                       @RequestParam(required = false) @ApiParam(value = "结束日期，格式yyyy-mm-dd") String endDate,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit) {
        Page page1 = new Page<>(page, limit);
        return ResponseUtil.ok(cataExportService.selectPages(page1,depId,startDate,endDate));
    }

    /***
     * 删除
     * @return
     */
    @GetMapping("/delete")
    @RequiresPermissions("cata:export:delete")
    @RequiresPermissionsDesc(menu = {"目录编制", "编制表导出"}, button = "删除")
    @ApiOperation("删除")
    public Object delete(@RequestParam Integer cataExportId) {
        cataExportService.delete(cataExportId);
        //逻辑删除能查到记录
        CataExport cataExport = cataExportService.getById(cataExportId);
        if(null != cataExport){
            SysStorage sysStorage = sysStorageService.getOne(new QueryWrapper<SysStorage>().eq(SysStorage.KEY, cataExport.getFileKey()));
            if(null != sysStorage){
                logHelper.logGeneralSucceed("编制表导出","删除编制表导出记录【"+sysStorage.getName()+"】。");
            }
        }
        return ResponseUtil.ok();
    }

    /**
     * 导出
     *
     * @param depId 部门ID
     * @return
     */
    @GetMapping("/export")
    @RequiresPermissions("cata:export:export")
    @RequiresPermissionsDesc(menu = {"目录编制", "编制表导出"}, button = "导出")
    @ApiOperation("导出")
    @Transactional
    public ResponseEntity<Resource> export(@RequestParam("depId") Integer depId) throws IOException {
        Workbook workbook = cataExportService.export(depId);
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        workbook.write(bos);
        byte[] barray=bos.toByteArray();
        int length = bos.size();
        //is第一转
        InputStream is=new ByteArrayInputStream(barray);
        ArchOrg archOrg = archOrgService.getById(depId);
        String fileName = "信息资源梳理编制表-"+archOrg.getOrgNm()+".xlsx";
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        SysStorage storage = storageService.store(is, length, contentType, fileName);
        if(storage == null){
            return ResponseEntity.notFound().build();
        }
        CataExport cataExport = new CataExport();
        cataExport.setDepId(depId);
        cataExport.setCreateBy(UserInfo.getUsername());
        cataExport.setCreateTime(new Date());
        cataExport.setDeleted(0);
        cataExport.setFileKey(storage.getKey());
        cataExportService.save(cataExport);

        String type = storage.getType();
        MediaType mediaType = MediaType.parseMediaType(type);

        Resource file = storageService.loadAsResource(storage.getKey());
        if (file == null) {
            return ResponseEntity.notFound().build();
        }
        logHelper.logGeneralSucceed("编制表导出","导出编制表【"+fileName+"】。");
        try {
            return ResponseEntity.ok().contentType(mediaType).header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + URLEncoder.encode(storage.getName(), "UTF-8") + "\"").body(file);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
    /**
     * 市表导出
     *
     * @param depId 部门ID
     * @return
     */
    @GetMapping("/exportCity")
    @RequiresPermissions("cata:export:exportCity")
    @RequiresPermissionsDesc(menu = {"目录编制", "编制表导出"}, button = "市表导出")
    @ApiOperation("市表导出")
    @Transactional
    public ResponseEntity<Resource> exportCity(@RequestParam("depId") Integer depId) throws IOException {
        Workbook workbook = cataExportService.exportCity(depId);
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        workbook.write(bos);
        byte[] barray=bos.toByteArray();
        int length = bos.size();
        //is第一转
        InputStream is=new ByteArrayInputStream(barray);

        String fileName = "未关联市表编码信息资源市表导出.xlsx";
        if(depId==null || depId==0){
            ArchOrg archOrg = archOrgService.getById(depId);
            fileName = "未关联市表编码信息资源市表导出-"+archOrg.getOrgNm()+".xlsx";
        }
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        SysStorage storage = storageService.store(is, length, contentType, fileName);
        if(storage == null){
            return ResponseEntity.notFound().build();
        }
        CataExport cataExport = new CataExport();
        cataExport.setDepId(depId);
        cataExport.setCreateBy(UserInfo.getUsername());
        cataExport.setCreateTime(new Date());
        cataExport.setDeleted(0);
        cataExport.setFileKey(storage.getKey());
        cataExportService.save(cataExport);

        String type = storage.getType();
        MediaType mediaType = MediaType.parseMediaType(type);

        Resource file = storageService.loadAsResource(storage.getKey());
        if (file == null) {
            return ResponseEntity.notFound().build();
        }
        logHelper.logGeneralSucceed("编制表导出","未关联市表编码信息资源市表导出【"+fileName+"】。");
        try {
            return ResponseEntity.ok().contentType(mediaType).header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + URLEncoder.encode(storage.getName(), "UTF-8") + "\"").body(file);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}
