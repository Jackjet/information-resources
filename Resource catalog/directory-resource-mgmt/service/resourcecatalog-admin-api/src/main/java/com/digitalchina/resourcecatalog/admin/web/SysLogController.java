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
import com.digitalchina.resourcecatalog.db.domain.SysLog;
import com.digitalchina.resourcecatalog.db.domain.SysStorage;
import com.digitalchina.resourcecatalog.db.service.ArchOrgService;
import com.digitalchina.resourcecatalog.db.service.SysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * <p>
 * ??????????????? ???????????????
 * </p>
 *
 * @author wangmh
 * @since 2020-04-30
 */
@RestController
@RequestMapping("/admin/log")
@Validated
@Api(value = "????????????", tags = "????????????")
public class SysLogController {
    private final Log logger = LogFactory.getLog(SysLogController.class);
    @Autowired
    private SysLogService logService;
    
    @Autowired
    private StorageService storageService;
    @Autowired
    private LogHelper logHelper;

    @RequiresPermissions("admin:log:list")
    @RequiresPermissionsDesc(menu = {"????????????", "????????????"}, order= 100, button = "??????")
    @GetMapping("/list")
    @ApiOperation("??????")
    public Object list(String name,String startDate,String endDate,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit) {
        Page page1 = new Page<>(page, limit);
        QueryWrapper<SysLog> qw = new QueryWrapper<SysLog>();
        qw.eq(SysLog.TYPE,0);
        qw.eq(SysLog.DELETED,0);
        if(!StringUtils.isEmpty(name)){
            qw.and(w -> w.like(SysLog.ADMIN,name).or().like(SysLog.ACCOUNT,name));
        }
        //TODO ??????
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if(!StringUtils.isEmpty(startDate)){
            startDate = startDate + " 00:00:00";
            qw.ge(SysLog.ADD_TIME, LocalDateTime.parse(startDate,dtf));
        }
        if(!StringUtils.isEmpty(endDate)){
            endDate = endDate + " 23:59:59";
            qw.le(SysLog.ADD_TIME,LocalDateTime.parse(endDate,dtf));
        }
        qw.orderByDesc(SysLog.ADD_TIME);
        return ResponseUtil.ok(logService.page(page1, qw));
    }

    /**
     * ??????
     *
     * @return
     */
    @GetMapping("/export")
    @RequiresPermissions("admin:log:export")
    @RequiresPermissionsDesc(menu = {"????????????", "????????????"}, button = "??????")
    @ApiOperation("??????")
    @Transactional
    public ResponseEntity<Resource> export(String name,String startDate,String endDate,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit) throws IOException {
        Workbook workbook = logService.export(name,startDate,endDate);
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        workbook.write(bos);
        byte[] barray=bos.toByteArray();
        int length = bos.size();
        //is?????????
        InputStream is=new ByteArrayInputStream(barray);
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//??????????????????
        String time=df.format(new Date());
        String fileName = "??????????????????-"+time+".xlsx";
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        SysStorage storage = storageService.store(is, length, contentType, fileName);
        if(storage == null){
            return ResponseEntity.notFound().build();
        }

        String type = storage.getType();
        MediaType mediaType = MediaType.parseMediaType(type);

        Resource file = storageService.loadAsResource(storage.getKey());
        if (file == null) {
            return ResponseEntity.notFound().build();
        }
        logHelper.logGeneralSucceed("??????????????????","????????????????????????"+time+"??????");
        try {
            return ResponseEntity.ok().contentType(mediaType).header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + URLEncoder.encode(storage.getName(), "UTF-8") + "\"").body(file);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
    
}
