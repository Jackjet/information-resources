package com.digitalchina.resourcecatalog.admin.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.digitalchina.resourcecatalog.admin.annotation.RequiresPermissionsDesc;
import com.digitalchina.resourcecatalog.db.domain.*;
import com.digitalchina.resourcecatalog.admin.util.UserInfo;
import com.digitalchina.resourcecatalog.core.util.ResponseUtil;
import com.digitalchina.resourcecatalog.db.service.*;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/index")
@Api(value = "首页", tags = "首页")
public class SysIndexController {

    @Autowired
    ArchBusiService archBusiService;

    @Autowired
    ArchOrgService archOrgService;

    @Autowired
    ArchBusiUviewExService archBusiUviewExService;

    @Autowired
    ArchBusiUviewStrExService archBusiUviewStrExService;

    @Autowired
    ArchAppSysService archAppSysService;

    @Autowired
    CataInfoTempService cataInfoTempService;

    @Autowired
    FileUploadRelService fileUploadRelService;

    @RequiresPermissions("admin:index:system")
    @RequiresPermissionsDesc(menu = {"首页"}, order = 1, button = "系统管理首页")
    @GetMapping("/system")
    public Object system() {
        Map<String, Object> resultMap = new HashMap<>();
        //部门count
        resultMap.put("deptCount", archOrgService.count(new QueryWrapper<ArchOrg>().lambda().isNull(ArchOrg::getParOrgId)));
        //权责清单count
        resultMap.put("busiCount", archBusiService.count(new QueryWrapper<>()));
        //信息项count
        resultMap.put("infoItemCount", archBusiService.countByDepts(null));
        //信息资源count
        resultMap.put("infoCataCount", cataInfoTempService.count(new QueryWrapper<CataInfoTemp>().lambda().eq(CataInfoTemp::getDeleted, 0)));
//        resultMap.put("infoCataCount", cataInfoTempService.count(new QueryWrapper<CataInfoTemp>().lambda()
//                .and(wrapper -> wrapper.eq(CataInfoTemp::getAuditStatus, "2").or().eq(CataInfoTemp::getAuditStatus, "3"))));
        //待审核资源信息List
        resultMap.put("waitReviewInfoList", cataInfoTempService.waitReviewInfoList());
        //待审核文件List
        resultMap.put("waitReviewFileList", fileUploadRelService.waitReviewFileList("0"));
        //提交信息资源前10部门
        resultMap.put("top10SubmitDept", top10Dept(null));
        //可共享信息资源前10部门
        resultMap.put("top10ShareDept", top10Dept("3"));
        return ResponseUtil.ok(resultMap);
    }


    @RequiresPermissions("admin:index:dept")
    @RequiresPermissionsDesc(menu = {"首页"}, order = 2, button = "部门管理员首页首页")
    @GetMapping("/dept")
    public Object dept() {
        List<Integer> deptIds = UserInfo.getDeptIds();
        Map<String, Object> resultMap = new HashMap<>();
        //权责清单count
        QueryWrapper<ArchBusi> archBusiQueryWrapper = new QueryWrapper<ArchBusi>();
        //应用系统count
        QueryWrapper<ArchAppSys> archAppSysQueryWrapper = new QueryWrapper<ArchAppSys>();
        //信息资源count 总数
        QueryWrapper<CataInfoTemp> cataInfoTempQueryWrapper = new QueryWrapper<CataInfoTemp>();
        cataInfoTempQueryWrapper.lambda().eq(CataInfoTemp::getDeleted, 0);
        //不共享
        QueryWrapper<CataInfoTemp> cataInfoTempQueryWrapperNoShare = new QueryWrapper<CataInfoTemp>();
        cataInfoTempQueryWrapperNoShare.lambda().eq(CataInfoTemp::getDeleted, 0).eq(CataInfoTemp::getShareLv, "03");
        //通过
        QueryWrapper<CataInfoTemp> cataInfoTempQueryWrapperPass = new QueryWrapper<CataInfoTemp>();
        cataInfoTempQueryWrapperPass.lambda().eq(CataInfoTemp::getDeleted, 0).eq(CataInfoTemp::getAuditStatus, "5");
        if (deptIds != null && deptIds.size() > 0) {
            archBusiQueryWrapper.lambda().in(ArchBusi::getDeptId, deptIds);
            archAppSysQueryWrapper.lambda().in(ArchAppSys::getProvOrgId, deptIds);
            cataInfoTempQueryWrapper.lambda().in(CataInfoTemp::getProvOrgId, deptIds);
            cataInfoTempQueryWrapperNoShare.lambda().in(CataInfoTemp::getProvOrgId, deptIds);
            cataInfoTempQueryWrapperPass.lambda().in(CataInfoTemp::getProvOrgId, deptIds);
        }
//        if (deptIds != null && deptIds.size() > 0) {
            //权责清单count
            resultMap.put("busiCount", archBusiService.count(archBusiQueryWrapper));
            //应用系统count
            resultMap.put("appCount", archAppSysService.count(archAppSysQueryWrapper));
            //信息项count
            resultMap.put("infoItemCount", archBusiService.countByDepts(deptIds));
            //信息资源count
            int cataTotal = cataInfoTempService.count(cataInfoTempQueryWrapper);
            resultMap.put("infoCataCount", cataTotal);
            //最新审核的信息资源前5
            resultMap.put("NewReviewInfoList", archBusiUviewExService.getNewTop5Info(deptIds));

            List pieDataList = new ArrayList();
            Map map1 = new HashMap();
            map1.put("name", "不可共享");
            int noshareCount = cataInfoTempService.count(cataInfoTempQueryWrapperNoShare);
            map1.put("value", noshareCount);
            pieDataList.add(map1);
            Map map2 = new HashMap();
            map2.put("name", "可共享");
            map2.put("value", cataTotal - noshareCount);
            pieDataList.add(map2);
            //饼图
            resultMap.put("pieData", pieDataList);
            //通过率
            List gaugeDataList = new ArrayList();
            Map map = new HashMap();
            map.put("name", "审核通过率");
            if (cataTotal != 0) {
                NumberFormat nt = NumberFormat.getPercentInstance();
                nt.setMaximumFractionDigits(2);
//                LambdaQueryWrapper<ArchBusiUviewEx> in2 = new QueryWrapper<ArchBusiUviewEx>().lambda().in(ArchBusiUviewEx::getProvOrgId, deptIds).eq(ArchBusiUviewEx::getDeleted, 0);
//                in2.eq(ArchBusiUviewEx::getAuditStatus, "5").eq(ArchBusiUviewEx::getDeleted, 0);
                map.put("value", nt.format((float) (cataInfoTempService.count(cataInfoTempQueryWrapperPass)) / (float) cataTotal));
            } else {
                map.put("value", 0);
            }
            gaugeDataList.add(map);
            resultMap.put("gaugeData", gaugeDataList);
//        }
        //最新审核文件前5
        resultMap.put("NewReviewFileList", fileUploadRelService.waitReviewFileList(null));
        //提交信息资源前10部门
        resultMap.put("top10SubmitDept", top10Dept(null));
        return ResponseUtil.ok(resultMap);
    }

    /***
     * 获取信息资源前10部门信息
     * @return
     */
    private List top10Dept(String shareType) {
        List<Result> results = archOrgService.top10Dept(shareType);
        List list = new ArrayList();
        Map map = new HashMap();
        map.put("name", results.stream().map(Result::getName).collect(Collectors.toList()));
        map.put("total", results.stream().map(Result::getTotal).collect(Collectors.toList()));
        list.add(map);
        return list;
    }
}
