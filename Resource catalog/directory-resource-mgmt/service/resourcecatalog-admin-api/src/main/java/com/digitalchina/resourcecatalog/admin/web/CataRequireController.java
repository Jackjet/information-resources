//package com.digitalchina.resourcecatalog.admin.web;
//
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.digitalchina.resourcecatalog.admin.annotation.RequiresPermissionsDesc;
//import com.digitalchina.resourcecatalog.admin.service.LogHelper;
//import com.digitalchina.resourcecatalog.admin.util.UserInfo;
//import com.digitalchina.resourcecatalog.core.util.ResponseUtil;
//import com.digitalchina.resourcecatalog.db.domain.ArchOrg;
//import com.digitalchina.resourcecatalog.db.domain.CataInfoTemp;
//import com.digitalchina.resourcecatalog.db.domain.CataRequire;
//import com.digitalchina.resourcecatalog.db.service.ArchOrgService;
//import com.digitalchina.resourcecatalog.db.service.CataRequireService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.util.StringUtils;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Date;
//import java.util.List;
//
//import static com.digitalchina.resourcecatalog.admin.util.AdminResponseCode.BUS_ERROR;
//import static com.digitalchina.resourcecatalog.admin.util.AdminResponseCode.INFO_CODE_EXIST;
//
///**
// * <p>
// * 信息需求目录 前端控制器
// * </p>
// *
// * @author baokd
// * @since 2020-05-09
// */
//@RestController
//@RequestMapping("/admin/cataRequire")
//@Validated
//@Api(value = "信息需求目录", tags = "信息需求目录")
//public class CataRequireController {
//    @Autowired
//    private CataRequireService cataRequireService;
//    @Autowired
//    private ArchOrgService archOrgService;
//    @Autowired
//    private LogHelper logHelper;
//
//    @RequiresPermissions("cata:require:list")
//    @RequiresPermissionsDesc(menu = {"目录编制", "信息需求目录"}, button = "信息需求目录列表查询")
//    @GetMapping("/list")
//    @ApiOperation("信息需求目录列表查询")
//    public Object list(String name, String code, String nameAndCode, String deptId,
//                       @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
//        Page page1 = new Page<>(page, limit);
//        if (UserInfo.isDeptAdmin()) {
//            List<Integer> list = UserInfo.getDeptIds();
//            for (Integer integer : list) {
//                deptId = integer + ",";
//            }
//            if (deptId != null && deptId != "") {
//                deptId = deptId.substring(0, deptId.length() - 1);
//            }
//        }
//        if (!StringUtils.isEmpty(nameAndCode)) {
//            code = null;
//            name = null;
//        }
//        return ResponseUtil.ok(cataRequireService.selectPages(page1, name, code, deptId, nameAndCode));
//    }
//
//    @RequiresPermissions("cata:require:save")
//    @RequiresPermissionsDesc(menu = {"目录编制", "信息需求目录"}, button = "信息需求目录新增")
//    @PostMapping("/save")
//    @ApiOperation("信息需求目录新增")
//    public Object save(@RequestBody CataRequire cata) {
//        QueryWrapper<CataRequire> qw = new QueryWrapper<CataRequire>();
//        if (cata != null) {
//            qw.eq(CataRequire.NAME, cata.getName()).eq(CataRequire.DELETED, 0).eq(CataRequire.DEPT_ID, cata.getDeptId());
//            cata.setCreateTime(new Date());
//            cata.setCreateBy(UserInfo.getUsername());
//        } else {
//            return ResponseUtil.fail(BUS_ERROR, "请传入参数");
//        }
//        if (cataRequireService.count(qw) > 0) {
//            return ResponseUtil.fail(INFO_CODE_EXIST, "信息需求目录名称已经存在");
//        }
//        int seq = cataRequireService.selectSequence("cata_require_code_seq");
//        if (cata.getProvOrgId() != null) {
//            ArchOrg archOrg = archOrgService.getById(cata.getProvOrgId());
//            if (archOrg != null) {
//                cata.setCode("R" + archOrg.getOrgCd() + String.format("%04d", seq));
//            } else {
//                return ResponseUtil.fail(BUS_ERROR, "所选部门不存在");
//            }
//        } else {
//            cata.setCode("R00" + String.format("%04d", seq));
//        }
//        cataRequireService.save(cata);
//        logHelper.logGeneralSucceed("需求目录管理", "新增需求信息资源【" + cata.getName() + "/" + cata.getCode() + "】");
//        return ResponseUtil.ok(cata.getCode());
//    }
//
//    @RequiresPermissions("cata:require:update")
//    @RequiresPermissionsDesc(menu = {"目录编制", "信息需求目录"}, button = "信息需求目录修改")
//    @PostMapping("/update")
//    @ApiOperation("信息需求目录修改")
//    public Object update(@RequestBody CataRequire cata) {
//        QueryWrapper<CataRequire> qw = new QueryWrapper<CataRequire>();
//        if (cata != null) {
//            qw.ne(CataRequire.ID, cata.getId());
//            qw.eq(CataRequire.NAME, cata.getName()).eq(CataRequire.DELETED, 0).eq(CataRequire.DEPT_ID, cata.getDeptId());
//            cata.setUpdateId(UserInfo.getId());
//            cata.setUpdateTime(new Date());
//            cata.setUpdateName(UserInfo.getUsername());
//        }
//        if (cataRequireService.count(qw) > 0) {
//            return ResponseUtil.fail(INFO_CODE_EXIST, "信息需求目录名称已经存在");
//        }
//        cataRequireService.updateById(cata);
//        logHelper.logGeneralSucceed("需求目录管理", "编辑需求信息资源【" + cata.getName() + "/" + cata.getCode() + "】");
//        return ResponseUtil.ok();
//    }
//
//    @RequiresPermissions("cata:require:delete")
//    @RequiresPermissionsDesc(menu = {"目录编制", "信息需求目录"}, button = "信息需求目录删除")
//    @PostMapping("/delete")
//    @ApiOperation("信息需求目录删除")
//    public Object delete(@RequestBody Integer[] ids) {
//        if (ids == null) {
//            return ResponseUtil.badArgument();
//        }
//        String log = cataRequireService.deleteByIds(ids);
//        if (log == null) {
//            return ResponseUtil.fail(BUS_ERROR, "删除失败");
//        }
//        logHelper.logGeneralSucceed("需求目录管理", "删除需求信息资源【" + log + "】");
//        return ResponseUtil.ok();
//    }
//
//    @RequiresPermissions("cata:require:read")
//    @RequiresPermissionsDesc(menu = {"目录编制", "信息需求目录"}, button = "信息需求目录详情")
//    @GetMapping("/read")
//    @ApiOperation("信息需求目录详情")
//    public Object read(@RequestParam(required = true) Integer id) {
//        CataRequire cata = cataRequireService.getOne(new QueryWrapper<CataRequire>().eq(CataRequire.ID, id).eq(CataInfoTemp.DELETED, 0));
//        if (cata != null) {
//            List<ArchOrg> depList = archOrgService.list(new QueryWrapper<ArchOrg>().and(w -> w.eq(ArchOrg.ORG_ID, cata.getProvOrgId()).or().eq(ArchOrg.ORG_ID, cata.getBelongTo()).or().eq(ArchOrg.ORG_ID, cata.getDeptId()).or().eq(ArchOrg.ORG_ID, cata.getOrgId())));
//            for (ArchOrg archOrg : depList) {
//                if (archOrg.getOrgId().equals(cata.getBelongTo())) {
//                    cata.setBelongToName(archOrg.getOrgNm());
//                }
//                if (archOrg.getOrgId().equals(cata.getProvOrgId())) {
//                    cata.setProvOrgName(archOrg.getOrgNm());
//                }
//                if (archOrg.getOrgId().equals(cata.getDeptId())) {
//                    cata.setDeptName(archOrg.getOrgNm());
//                }
//                if (archOrg.getOrgId().equals(cata.getOrgId())) {
//                    cata.setOrgName(archOrg.getOrgNm());
//                }
//            }
//            return ResponseUtil.ok(cata);
//        }
//        return ResponseUtil.fail(BUS_ERROR, "该目录不存在");
//    }
//
//
//    /**
//     * 树
//     *
//     * @return
//     */
//    @RequiresPermissions("cata:require:deptTree")
//    @RequiresPermissionsDesc(menu = {"目录编制", "信息需求目录"}, button = "部门树")
//    @GetMapping("/deptTree")
//    @ApiOperation("树")
//    public Object tree() {
//        return archOrgService.tree(0, false, null);
//    }
//}
