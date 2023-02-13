package com.digitalchina.resourcecatalog.admin.web;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.cell.CellUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalchina.resourcecatalog.admin.annotation.RequiresPermissionsDesc;
import com.digitalchina.resourcecatalog.admin.dto.ArchBusiDto;
import com.digitalchina.resourcecatalog.admin.service.LogHelper;
import com.digitalchina.resourcecatalog.admin.util.UserInfo;
import com.digitalchina.resourcecatalog.core.storage.config.StorageProperties;
import com.digitalchina.resourcecatalog.core.util.BeanUtils;
import com.digitalchina.resourcecatalog.core.util.CharUtil;
import com.digitalchina.resourcecatalog.core.util.PageUtil;
import com.digitalchina.resourcecatalog.core.util.ResponseUtil;
import com.digitalchina.resourcecatalog.db.domain.*;
import com.digitalchina.resourcecatalog.db.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 权责清单 前端控制器
 * </p>
 *
 * @author wangmh
 * @since 2020-05-09
 */
@RestController
@RequestMapping("/admin/archBusi")
@Api(value = "权责清单管理", tags = "权责清单管理")
public class ArchBusiController {

    @Autowired
    ArchBusiService archBusiService;

    @Autowired
    private LogHelper logHelper;

    @Autowired
    CataBusInfoRelService cataBusInfoRelService;

    @Autowired
    CataInfoTempService cataInfoTempService;

    @Autowired
    CataAppBusRelService cataAppBusRelService;

    @Autowired
    CataRequireService cataRequireService;

    @Autowired
    ArchAppSysService archAppSysService;

    @Autowired
    ArchOrgService archOrgService;
    @Autowired
    private StorageProperties storageProperties;

    /***
     * 部门的权责清单树
     * @return
     */
    @GetMapping("/tree")
    @RequiresAuthentication
//    @RequiresPermissions("admin:archBusi:tree")
//    @RequiresPermissionsDesc(menu = {"权责清单管理"}, order = 3, button = "左侧树")
    @ApiOperation("部门的权责清单树")
    public Object tree() {
        return ResponseUtil.ok(archBusiService.tree(UserInfo.isSuperAdmin(), UserInfo.getDeptIds()));
    }
    /***
     * 部门的权责清单树
     * @return
     */
    @GetMapping("/treeByDeptId")
    @RequiresAuthentication
    @ApiOperation("根据部门Id获取不包含组织机构的权责树")
    public Object treeByDeptId(@RequestParam(required = true )Integer deptId) {
        return ResponseUtil.ok(archBusiService.treeByDeptId(deptId));
    }
    /***
     *
     * @param name 权责清单名称
     * @param current
     * @param size
     * @param isDept 是否是部门级 0部门级 1权责清单
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("admin:archBusi:list")
    @RequiresPermissionsDesc(menu = {"权责清单管理"}, button = "查询")
    @ApiOperation("分页")
    public Object list(@RequestParam(required = false) String name,
                       @RequestParam(defaultValue = "1") Integer current,
                       @RequestParam(defaultValue = "10") Integer size,
                       @RequestParam(defaultValue = "0") Integer isDept,
                       @RequestParam Integer id,
                           @RequestParam(defaultValue = "0") Integer filterCata) {
        return ResponseUtil.ok(PageUtil.startPage(archBusiService.selectByPage(name, isDept, id, filterCata), current, size));
    }

    /***
     * 详情
     * @return
     */
    @GetMapping("/detail")
    @RequiresPermissions("admin:archBusi:detail")
    @RequiresPermissionsDesc(menu = {"权责清单管理"}, button = "详情")
    @ApiOperation("详情")
    public Object detail(@RequestParam Integer busiId) {
        ArchBusi detail = archBusiService.detail(busiId);
        detail.setAppList(archBusiService.getAppListByBusiId(busiId));
        return ResponseUtil.ok(detail);
    }

    /***
     * 添加或者编辑
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @RequiresPermissions("admin:archBusi:saveOrUpdate")
    @RequiresPermissionsDesc(menu = {"权责清单管理"}, button = "添加或者编辑")
    @ApiOperation("添加和编辑")
    public Object saveOrUpdate(@Valid @RequestBody ArchBusiDto archBusiDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseUtil.fail(400, result.getFieldError().getDefaultMessage());
        }
//        if (CharUtil.isSpecialChar(archBusiDto.getBusiNo())) {
//            return ResponseUtil.fail("机构编码不能包含特殊字符！");
//        }
//        if (!CharUtil.isCodeTrue(archBusiDto.getBusiNo())) {
//            return ResponseUtil.fail("机构编码必须以字母开头，数字结尾！");
//        }
        ArchBusi busi = BeanUtils.dtoToDo(archBusiDto, ArchBusi.class);
        LambdaQueryWrapper<ArchBusi> qwC = new QueryWrapper<ArchBusi>().lambda().eq(ArchBusi::getDeptId, busi.getDeptId()).eq(ArchBusi::getBusiNm, busi.getBusiNm());
        if(busi.getBusiId() != null){
        	qwC.ne(ArchBusi::getBusiId, busi.getBusiId());
        }
        if (archBusiService.count(qwC) > 0) {
            return ResponseUtil.fail(401, "权责清单名称同一级部门下不能重复！");
        }
        QueryWrapper<ArchBusi> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ArchBusi::getBusiNo, busi.getBusiNo());
        if (busi.getBusiId() == null) {
            busi.setUpdatedBy(UserInfo.getUsername());
            busi.setUpdatedTime(new Date());
        } else {
            wrapper.lambda().ne(ArchBusi::getBusiId, busi.getBusiId());
            busi.setCreatedBy(UserInfo.getUsername());
            busi.setCreatedTime(new Date());
        }
        if (archBusiService.count(wrapper) > 0) {
            return ResponseUtil.fail(401, "该权责清单编码已存在！");
        }
        List<Integer> appIds = archBusiDto.getAppIds();
        //验证所支撑的应用系统不能重复
        if (appIds != null && appIds.size() > 1) {
            HashSet<Integer> hashSet = new HashSet<>(appIds);
            if (appIds.size() != hashSet.size()) {
                return ResponseUtil.fail(401, "所支撑的应用系统不能重复！");
            }
        }
        logHelper.logOrgSucceed(archBusiService.saveOrUpdateInfo(busi, archBusiDto.getAppIds()) ? "保存权责清单" : "编辑权责清单", busi.getBusiNm(), busi.getBusiNo());
        return ResponseUtil.ok();
    }

    /***
     * 获取权责清单编码
     * @return
     */
    @GetMapping("/getBusiNo")
    @RequiresPermissions("admin:archBusi:getBusiNo")
    @RequiresPermissionsDesc(menu = {"权责清单管理"}, button = "获取权责清单编码")
    @ApiOperation("获取权责清单编码")
    public Object getBusiNo(@RequestParam Integer deptId, @RequestParam(required = false) Integer pId) {
        /*Map map = new HashMap();
        StringBuffer buffer = new StringBuffer();
        Long busiNoMax = archBusiService.getBusiNoMaxByPId(pId, deptId);
        if (pId == null) {//可能是第一级第一个 或属于第一级
        	ArchOrg byId = archOrgService.getById(deptId);
        	buffer.append("B");
        	buffer.append(byId.getOrgCd().length() % 2 == 0 ? byId.getOrgCd() : "0" + byId.getOrgCd());
            if (busiNoMax == null) {//说明是第一级第一个
                map.put("busiNo", buffer.append("0001"));
                return ResponseUtil.ok(map);
            }else{
                 map.put("busiNo", buffer.append(String.format("%04d",busiNoMax + 1)));
                 return ResponseUtil.ok(map);
            }
        } else {
        	QueryWrapper<ArchBusi> wrapper = new QueryWrapper<>();
        	wrapper.lambda().eq(ArchBusi::getBusiId, pId);
        	ArchBusi one = archBusiService.getOne(wrapper);
        	buffer.append(one.getBusiNo());
            if (busiNoMax == null) {//可能是子集第一个
                map.put("busiNo", buffer.append("0001"));
                return ResponseUtil.ok(map);
            }else{
                 map.put("busiNo", buffer.append(String.format("%04d",busiNoMax + 1)));
                 return ResponseUtil.ok(map);
            }
        }*/
        return ResponseUtil.ok(archBusiService.getBusiNo(deptId, pId));
    }

    /***
     * 产生的信息资源和需要的信息资源
     * @return
     */
    @PostMapping("/relCataInfo")
    @RequiresPermissions("admin:archBusi:relCataInfo")
    @RequiresPermissionsDesc(menu = {"权责清单管理"}, button = "产生的信息资源|需要的信息资源")
    @ApiOperation("产生的信息资源|需要的信息资源")
    public Object relCataInfo(@Valid @RequestBody CataBusInfoRel cataBusInfoRel, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseUtil.fail(400, result.getFieldError().getDefaultMessage());
        }
        QueryWrapper<CataBusInfoRel> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CataBusInfoRel::getType, cataBusInfoRel.getType())
                .eq(CataBusInfoRel::getBusId, cataBusInfoRel.getBusId())
                .in(CataBusInfoRel::getInfoId, cataBusInfoRel.getInfoIds());
        if (cataBusInfoRelService.count(queryWrapper) > 0) {
            return ResponseUtil.fail(401, "该信息资源关联项已存在！");
        }
        if (cataBusInfoRelService.relCataInfo(cataBusInfoRel)) {
            ArchBusi byId = archBusiService.getById(cataBusInfoRel.getBusId());
            if (cataBusInfoRel.getType() == 0) {
                cataInfoTempService.updateRelBusi(1, "", cataBusInfoRel.getInfoIds());
                List<CataInfoTemp> list = cataInfoTempService.list(new QueryWrapper<CataInfoTemp>().lambda()
                        .in(CataInfoTemp::getUviewId, cataBusInfoRel.getInfoIds()));
                //记录日志
                logHelper.logBusiRelSucceed("权责清单关联信息资源", byId.getBusiNm(), byId.getBusiNo()
                        , list.stream().map(CataInfoTemp::getUviewNm).collect(Collectors.joining("、")));
            }
            if (cataBusInfoRel.getType() == 1) {
                List<CataRequire> list = cataRequireService.list(new QueryWrapper<CataRequire>().lambda()
                        .in(CataRequire::getId, cataBusInfoRel.getInfoIds()));

                //记录日志
                logHelper.logBusiRelSucceed("权责清单关联信息需求", byId.getBusiNm(), byId.getBusiNo()
                        , list.stream().map(CataRequire::getName).collect(Collectors.joining("、")));
            }
            return ResponseUtil.ok();
        }
        return ResponseUtil.fail();
    }

    /***
     * 产生|需要的信息资源列表
     * @return
     */
    @GetMapping("/cataInfoPage")
    @RequiresPermissions("admin:archBusi:cataInfoPage")
    @RequiresPermissionsDesc(menu = {"权责清单管理"}, button = "产生|需要的信息资源列表")
    @ApiOperation("产生|需要的信息资源列表(分页)")
    public Object cataInfoPage(@RequestParam(defaultValue = "1") Integer current,
                               @RequestParam(defaultValue = "10") Integer size,
                               @RequestParam Integer busiId,
                               @RequestParam Integer type) {
        Page page = new Page<>(current, size);
        return ResponseUtil.ok(cataBusInfoRelService.cataInfoPage(page, busiId, type));
    }

    /***
     * 移除信息资源目录
     * @return
     */
    @GetMapping("/deleteCataInfoRel")
    @RequiresPermissions("admin:archBusi:deleteCataInfoRel")
    @RequiresPermissionsDesc(menu = {"权责清单管理"}, button = "移除")
    @ApiOperation("移除信息资源目录")
    public Object deleteCataInfoRel(@RequestParam Integer id, @RequestParam Integer type) {
        CataBusInfoRel cataBusInfoRel = cataBusInfoRelService.getById(id);
        if (cataBusInfoRelService.removeById(id)) {
            ArchBusi archBusi = archBusiService.getById(cataBusInfoRel.getBusId());
            if (type == 0) {
                CataInfoTemp cataInfoTemp = cataInfoTempService.getById(cataBusInfoRel.getInfoId());
                QueryWrapper<CataBusInfoRel> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq(CataBusInfoRel.INFO_ID, cataInfoTemp.getUviewId());
                queryWrapper.eq(CataBusInfoRel.TYPE, 0);
                int leftCount = cataBusInfoRelService.count(queryWrapper);
                if(leftCount==0){
                    cataInfoTempService.updateRelBusi(0, "无", Arrays.asList(cataBusInfoRel.getInfoId()));
                }
                //记录日志
                logHelper.logBusiRelSucceed("移除权责清单关联的信息资源", archBusi.getBusiNm(), archBusi.getBusiNo(), cataInfoTemp.getUviewNm());
            }
            if (type == 1) {
                CataRequire cataRequire = cataRequireService.getById(cataBusInfoRel.getInfoId());
                //记录日志
                logHelper.logBusiRelSucceed("移除权责清单关联的信息需求", archBusi.getBusiNm(), archBusi.getBusiNo(), cataRequire.getName());
            }
            return ResponseUtil.ok();
        }
        return ResponseUtil.fail();
    }


    /***
     * 删除
     * @return
     */
    @GetMapping("/delete")
    @RequiresPermissions("admin:archBusi:delete")
    @RequiresPermissionsDesc(menu = {"权责清单管理"}, button = "删除")
    @ApiOperation("删除")
    public Object delete(@RequestParam Integer busiId) {
        ArchBusi archBusi = archBusiService.getById(busiId);
        if (archBusiService.delete(busiId)) {
            //记录日志
            logHelper.logOrgSucceed("删除权责清单", archBusi.getBusiNm(), archBusi.getBusiNo());
            return ResponseUtil.ok();
        }
        return ResponseUtil.fail();
    }

    /***
     * 应用系统分页
     * @return
     */
    @GetMapping("/appPage")
    @RequiresPermissions("admin:archBusi:appPage")
    @RequiresPermissionsDesc(menu = {"权责清单管理"}, button = "应用系统分页")
    @ApiOperation("应用系统分页")
    public Object appPage(@RequestParam(required = false) Integer busiId,
                          @RequestParam(required = false) String appNoOrAppNm,
                          @RequestParam Integer orgId,
                          @RequestParam(defaultValue = "1") Integer current,
                          @RequestParam(defaultValue = "10") Integer size) {
        Page page = new Page<>(current, size);
        QueryWrapper<ArchAppSys> sysQueryWrapper = new QueryWrapper<>();
        sysQueryWrapper.lambda()
                .select(ArchAppSys::getAppsysId, ArchAppSys::getAppsysNm, ArchAppSys::getAppsysNo)
                .orderByAsc(ArchAppSys::getAppsysNo)
                .eq(ArchAppSys::getProvOrgId, orgId)
                .and(wrapper -> wrapper.like(ArchAppSys::getAppsysNm, appNoOrAppNm).or().like(ArchAppSys::getAppsysNo, appNoOrAppNm));
//        if (busiId != null) {
//            List<CataAppBusRel> list = cataAppBusRelService.list(new QueryWrapper<CataAppBusRel>().lambda().eq(CataAppBusRel::getItemId, busiId));
//            sysQueryWrapper.lambda().notIn(ArchAppSys::getAppsysId, list.stream().map(CataAppBusRel::getAppId).collect(Collectors.toList()));
//        }
        return ResponseUtil.ok(archAppSysService.page(page, sysQueryWrapper));
    }

    /***
     * 增加-0产生| 1需要的信息资源分页
     * @return
     */
    @GetMapping("/getCataInfoPage")
    @RequiresPermissions("admin:archBusi:getCataInfoPage")
    @RequiresPermissionsDesc(menu = {"权责清单管理"}, button = "增加")
    @ApiOperation("增加-0产生| 1需要的信息资源分页")
    public Object getCataInfoPage(@RequestParam(required = false) Integer busiId,
                                  @RequestParam(required = false) String cataName,
                                  @RequestParam Integer orgId,
                                  @RequestParam Integer type,
                                  @RequestParam(defaultValue = "1") Integer current,
                                  @RequestParam(defaultValue = "10") Integer size) {
        Page page = new Page<>(current, size);  //
        List<Integer> infoIds = null;
        if (busiId != null) {//若为空则是新增,只需要查询资源目录为当前部门;若不为空则是编辑,需要从关联表中查询出已关联的目录,并且排除这些目录
            List<CataBusInfoRel> cataBusInfoRels = cataBusInfoRelService.list(new QueryWrapper<CataBusInfoRel>().lambda().eq(CataBusInfoRel::getType, type).eq(CataBusInfoRel::getBusId, busiId));
            infoIds = cataBusInfoRels.stream().map(CataBusInfoRel::getInfoId).collect(Collectors.toList());
        }
        return ResponseUtil.ok(archBusiService.cataInfoPage(page, orgId, cataName, infoIds, type));
    }

    @RequiresPermissions("admin:archBusi:upload")
    @RequiresPermissionsDesc(menu = {"权责清单管理"}, button = "导入")
    @PostMapping(value = "/upload")
    @ApiOperation("导入")
    @Transactional
    public Object create(@RequestParam("file") MultipartFile multipartFile) throws IOException
    {
        String importRs = archBusiService.importBusisExcel(multipartFile.getInputStream());
        logHelper.logGeneralSucceed("权责清单导入","权责清单导入【"+importRs+"】。");
        return ResponseUtil.ok();
    }
    /**
     * 导出
     * @return
     */
    @GetMapping("/export")
    @RequiresPermissions("admin:archBusi:export")
    @RequiresPermissionsDesc(menu = {"权责清单管理"}, button = "导出")
    @ApiOperation("导出")
    @Transactional
    public ResponseEntity<Resource> export() {
        //2.定义基础数据
        List<String> rowHead = CollUtil.newArrayList("职权信息");
        List<String> rowHead1 = CollUtil.newArrayList("实施主体","职责", "职权信息");
        List<String> rowHead2 = CollUtil.newArrayList("","","职权名称","职权类别","职权编码");
        //3.通过ExcelUtil.getBigWriter()创建Writer对象，BigExcelWriter用于大数据量的导出，不会引起溢出；
        ExcelWriter writer = ExcelUtil.getWriter(false);
        //4.写入标题
        writer.writeRow(rowHead);
        writer.writeRow(rowHead1);
        writer.writeRow(rowHead2);

        List<CellRangeAddress> craList = new ArrayList<CellRangeAddress>();
        craList.add(new CellRangeAddress(0,0,0,4));
        craList.add(new CellRangeAddress(1,2,0,0));
        craList.add(new CellRangeAddress(1,2,1,1));
        craList.add(new CellRangeAddress(1,1,2,4));
        //最后合并单元格
        for (CellRangeAddress cellRangeAddress : craList) {
            writer.getSheet().addMergedRegion(cellRangeAddress);
            setBorder(1, cellRangeAddress, writer.getSheet());
        }
        //5.实现核心逻辑
        try {
            //6.定义容器保存人物数据
            List<List<Object>> rows = new LinkedList<>();
            //判断用户的权限
            List<ArchBusi> busiList = new ArrayList<>();
            if (UserInfo.isDeptAdmin()) {//只显示该部门
                if (UserInfo.getDeptIds() != null && UserInfo.getDeptIds().size() > 0) {
                    busiList = archBusiService.selectListContainsOrgNm(UserInfo.getDeptIds());
                }
            } else {//全部
                busiList = archBusiService.selectListContainsOrgNm(null);
            }
            //第一级 key机构名称 value一级权责集合
            Map<String, List<ArchBusi>> orgFirstMap = new HashMap<>();
            Map<Integer, ArchBusi> pIdMap = new HashMap<>();
            //解析数据
            int currentRowIndex = 3;
            String currentOrgName = "";
            String currentFirstName = "";
            int lastOrgRow = currentRowIndex, lastFirstRow = currentRowIndex;
            int orgStep = 0, firstStep = 0;
            for(int i=0;i< busiList.size();i++){
                ArchBusi archBusi = busiList.get(i);
                if(archBusi.getpId()==null || archBusi.getpId()==0){//一级数据
                    if(!pIdMap.containsKey(archBusi.getBusiId())){
                        pIdMap.put(archBusi.getBusiId(), archBusi);
                    }
                }else{//二级数据
                    List<Object> perList = new ArrayList<>();
                    perList.add(archBusi.getOrgNm());
                    perList.add(pIdMap.get(archBusi.getpId()).getBusiNm());
                    perList.add(archBusi.getBusiNm());
                    perList.add(archBusi.getBusiType());
                    perList.add(archBusi.getBusiNo());
                    rows.add(perList);
                    //单元格合并
                    if(currentRowIndex==3){
                        currentOrgName = archBusi.getOrgNm();
                        currentFirstName = pIdMap.get(archBusi.getpId()).getBusiNm();
                    }
                    if(currentOrgName.equals(archBusi.getOrgNm())){
                        orgStep++;
                    }else{
                        currentOrgName = archBusi.getOrgNm();
                        if(orgStep>1){
                            CellUtil.mergingCells(writer.getSheet(), lastOrgRow,lastOrgRow  + orgStep - 1,0,0, null);

                        }
                        lastOrgRow = lastOrgRow  + orgStep;
                        orgStep = 1;
                    }
                    if(currentFirstName.equals(pIdMap.get(archBusi.getpId()).getBusiNm())){
                        firstStep++;
                    }else{
                        currentFirstName = pIdMap.get(archBusi.getpId()).getBusiNm();
                        if(firstStep>1){
                            CellUtil.mergingCells(writer.getSheet(), lastFirstRow,lastFirstRow  + firstStep - 1,1,1, null);

                        }
                        lastFirstRow = lastFirstRow  + firstStep;
                        firstStep = 1;
                    }
                    //最后一行处理
                    if(i==busiList.size()-1){
                        if(orgStep>1){
                            CellUtil.mergingCells(writer.getSheet(), lastOrgRow,lastOrgRow  + orgStep - 1,0,0, null);

                        }
                        if(firstStep>1){
                            CellUtil.mergingCells(writer.getSheet(), lastFirstRow,lastFirstRow  + firstStep - 1,1,1, null);
                        }
                    }
                    currentRowIndex++;
                }
            }
            String fileName = "权责信息导出.xls";
            File file = FileUtil.file(storageProperties.getLocal().getStoragePath() + fileName);
            //19.导出数据
            // 一次性写出内容，使用默认样式，强制输出标题
            writer.write(rows);
            writer.flush(file);
            Resource resource = new FileSystemResource(file);
            String type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            MediaType mediaType = MediaType.parseMediaType(type);
            logHelper.logGeneralSucceed("权责信息导出","权责信息导出【"+fileName+"】。");
                return ResponseEntity.ok().contentType(mediaType).header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"").body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }finally {
            //关闭writer，释放内存
            writer.close();
        }
    }
    private void setBorder(int border, CellRangeAddress cra, Sheet sheet){
        // 使用RegionUtil类为合并后的单元格添加边框
        RegionUtil.setBorderBottom(border, cra, sheet); // 下边框
        RegionUtil.setBorderLeft(border, cra, sheet); // 左边框
        RegionUtil.setBorderRight(border, cra, sheet); // 有边框
        RegionUtil.setBorderTop(border, cra, sheet); // 上边框
    }
}
