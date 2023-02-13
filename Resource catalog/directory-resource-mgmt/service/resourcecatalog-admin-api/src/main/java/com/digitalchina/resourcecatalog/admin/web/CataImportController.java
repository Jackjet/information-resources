package com.digitalchina.resourcecatalog.admin.web;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalchina.resourcecatalog.admin.annotation.RequiresPermissionsDesc;
import com.digitalchina.resourcecatalog.admin.service.LogHelper;
import com.digitalchina.resourcecatalog.admin.util.UserInfo;
import com.digitalchina.resourcecatalog.core.storage.StorageService;
import com.digitalchina.resourcecatalog.core.util.ResponseUtil;
import com.digitalchina.resourcecatalog.db.domain.*;
import com.digitalchina.resourcecatalog.db.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhangys
 * @since 2020-05-12
 */
@RestController
@RequestMapping("/admin/cataImport")
@Api(value = "编制表导入", tags = "编制表导入")
public class CataImportController {
    @Autowired
    CataImportService cataImportService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private SysStorageService sysStorageService;
    @Autowired
    private CataImportErrorService cataImportErrorService;
    @Autowired
    ArchOrgService archOrgService;
    @Autowired
    private LogHelper logHelper;

    @Autowired
    ArchBusiUviewExExportTmpService archBusiUviewExExportTmpService;
    @RequiresPermissions("cata:import:list")
    @RequiresPermissionsDesc(menu = {"目录编制", "编制表导入"}, button = "分页查询")
    @GetMapping("/list")
    @ApiOperation("分页查询")
    public Object list(@RequestParam Integer depId,
                       @RequestParam(required = false) @ApiParam(value = "开始日期，格式yyyy-mm-dd") String startDate,
                       @RequestParam(required = false) @ApiParam(value = "结束日期，格式yyyy-mm-dd") String endDate,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit) {
        Page page1 = new Page<>(page, limit);
        return ResponseUtil.ok(cataImportService.selectPages(page1,depId,startDate,endDate));
    }

    /***
     * 删除
     * @return
     */
    @GetMapping("/delete")
    @RequiresPermissions("cata:import:delete")
    @RequiresPermissionsDesc(menu = {"目录编制", "编制表导入"}, button = "删除")
    @ApiOperation("删除")
    public Object delete(@RequestParam Integer cataImportId) {
        cataImportService.delete(cataImportId);
        //逻辑删除能查到记录
        CataImport cataImport = cataImportService.getById(cataImportId);
        if(null != cataImport){
            SysStorage sysStorage = sysStorageService.getOne(new QueryWrapper<SysStorage>().eq(SysStorage.KEY, cataImport.getFileKey()));
            if(null != sysStorage){
                logHelper.logGeneralSucceed("编制表导入","删除编制表导入记录【"+sysStorage.getName()+"】。");
            }
        }
        return ResponseUtil.ok();
    }

    /***
     * 异常信息查询
     * @return
     */
    @GetMapping("/getErrorList")
    @RequiresPermissions("cata:import:getErrorList")
    @RequiresPermissionsDesc(menu = {"目录编制", "编制表导入"}, button = "异常信息查询")
    @ApiOperation("异常信息查询")
    public Object getErrorList(@RequestParam Integer cataImportId) {
        return ResponseUtil.ok(cataImportService.getErrorList(cataImportId));
    }

    @RequiresPermissions("cata:import:upload")
    @RequiresPermissionsDesc(menu = {"目录编制", "编制表导入"}, button = "导入")
    @PostMapping(value = "/upload")
    @ApiOperation("导入")
    @Transactional
    public Object create(@RequestParam("file") MultipartFile multipartFile,
                         @RequestParam("depId") Integer depId) throws IOException
    {
        List<String> errorList = new ArrayList<String>();
        List<String> infoList = new ArrayList<String>();
        InputStream is=null;
        String originalFilename = multipartFile.getOriginalFilename();
        SysStorage sysStorage = storageService.store(multipartFile.getInputStream(), multipartFile.getSize(),
                multipartFile.getContentType(), originalFilename);
        try
        {
            is = multipartFile.getInputStream();
            Workbook wb = WorkbookFactory.create(is);
            is.close();
            //如果部门名称都不对，直接返回错误，不用添加导入记录
            if(wb.getNumberOfSheets() < 2){
                wb.close();
                return ResponseUtil.fail("模板错误，sheet页不够。");
            }

            //部门信息
            ArchOrg archOrg = archOrgService.getById(depId);
            if(archOrg == null){
                wb.close();
                return ResponseUtil.fail("部门树所选部门已不存在。");
            }
            SysUser user = new SysUser();
            user.setId(UserInfo.getId());
            user.setUsername(UserInfo.getUsername());
            cataImportService.saveData(archOrg,user,wb,errorList,infoList);
            wb.close();
        }
        catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        CataImport cataImport = new CataImport();
        cataImport.setDepId(depId);
        cataImport.setCreateBy(UserInfo.getUsername());
        cataImport.setCreateTime(new Date());
        cataImport.setDeleted(0);
        if(errorList.size()>0){
            cataImport.setImportStatus("2");
        }else{
            cataImport.setImportStatus("1");
        }
        cataImport.setFileKey(sysStorage.getKey());

        if(errorList.size()>0 || infoList.size()>0){
            cataImport.setHasInfo("1");
        }else{
            cataImport.setHasInfo("0");
        }
        cataImportService.save(cataImport);
        if(errorList.size()>0){
            CataImportError cataImportError = null;
            for (String s : errorList) {
                cataImportError = new CataImportError();
                cataImportError.setImportId(cataImport.getId());
                cataImportError.setErrorInfo(s);
                cataImportErrorService.save(cataImportError);
            }
            cataImport.setHasInfo("1");
        }else{
            if(infoList.size()>0){
                CataImportError cataImportError = null;
                for (String s : infoList) {
                    cataImportError = new CataImportError();
                    cataImportError.setImportId(cataImport.getId());
                    cataImportError.setErrorInfo(s);
                    cataImportErrorService.save(cataImportError);
                }
                cataImport.setHasInfo("1");
            }
        }
        ArchOrg archOrg = archOrgService.getById(depId);
        logHelper.logGeneralSucceed("编制表导入","导入信息资源编制表【"+archOrg.getOrgNm()+"】。");
        return ResponseUtil.ok();
    }
    @RequiresPermissions("cata:import:uploadCity")
    @RequiresPermissionsDesc(menu = {"目录编制", "编制表导入"}, button = "市表导入")
    @PostMapping(value = "/uploadCity")
    @ApiOperation("市表导入")
    @Transactional
    public Object createCity(@RequestParam("file") MultipartFile multipartFile,
                         @RequestParam("depId") Integer depId) throws IOException
    {
        archBusiUviewExExportTmpService.remove(null);
        ArchOrg archOrg = archOrgService.getById(depId);
        ExcelReader reader= ExcelUtil.getReader(multipartFile.getInputStream());
        List<List<Object>> result = reader.read(1);
        Map<String, String> tmpMap = new HashMap<>();
        List<ArchBusiUviewExExportTmp> updateList = new ArrayList<ArchBusiUviewExExportTmp>();
        Integer index = 1;
        for (List<Object> item : result) {
            String city_cata_code = subCellStr(item.get(0));
            String uview_nm = subCellStr(item.get(1));
            String orgName = subCellStr(item.get(22));
            if(orgName.equals(archOrg.getOrgNm())){
                if(!tmpMap.containsKey(city_cata_code)){
                    tmpMap.put(city_cata_code, uview_nm);
                    ArchBusiUviewExExportTmp tmp = new ArchBusiUviewExExportTmp();
                    tmp.setId(index++);
                    tmp.setCityCataCode(city_cata_code);
                    tmp.setUviewNm(uview_nm);
                    tmp.setDeptId(depId);
                    tmp.setDeptName(archOrg.getOrgNm());
                    updateList.add(tmp);
                }
            }
        }
        if(updateList.size()>0){
            archBusiUviewExExportTmpService.saveBatch(updateList);
            archBusiUviewExExportTmpService.updateFormal();
            archBusiUviewExExportTmpService.updateTmp();
            archBusiUviewExExportTmpService.updateHistory();
        }

        logHelper.logGeneralSucceed("市表导入","市表导入【"+archOrg.getOrgNm()+"】。");
        return ResponseUtil.ok();
    }
    private String subCellStr(Object object) {
        return subCellStr(object, 100);
    }

    private static String subCellStr(Object obj, Integer length) {
        if (obj == null) {
            return "";
        } else {
            return StrUtil.subSufByLength(StrUtil.trim(obj.toString()), length);
        }
    }
}
