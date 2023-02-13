package d1.project.resourcesharingmgmt.resource.business;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.TokenManager;
import d1.project.resourcesharingmgmt.resource.dao.*;
import d1.project.resourcesharingmgmt.resource.entity.*;
import d1.project.resourcesharingmgmt.resource.model.ArchBusiUviewEx.ArchBusiUviewExFindAllVm;
import d1.project.resourcesharingmgmt.resource.model.AssetApiEx.AssetExFindAllVm;
import d1.project.resourcesharingmgmt.resource.model.DemandedInfo.DemandedInfoFindAllVm;
import d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo.ResourceUseInfoFindAllVm;
import d1.project.resourcesharingmgmt.resource.model.way.WayCatalogResVm;
import d1.project.resourcesharingmgmt.resource.model.way.WayChargeWebVm;
import d1.project.resourcesharingmgmt.resource.service.AssetExService;
import d1.project.resourcesharingmgmt.system.entity.OrganizationEntity;
import d1.project.resourcesharingmgmt.system.service.OrganizationService;
import net.dcrun.component.http.HttpService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author dy
 */
@Service
public class ChargeBusiness {
    private final HttpServletRequest request;
    private final WayRoleDao wayRoleDao;
    private final AssetExService assetExService;
    private final AssetExDao assetExDao;
    private final ResourceUseInfoBusiness resourceUseInfoBusiness;
    private final ResourceUseInfoDao resourceUseInfoDao;
    private final DemandedInfoBusiness demandedInfoBusiness;
    private final DemandedInfoDao demandedInfoDao;
    private final ArchBusiUviewExBusiness archBusiUviewExBusiness;
    private final OrganizationService organizationService;
    private final WayDao wayDao;

    @Value("${dass.url}")
    private String catalogRootPath;
    @Value("${daas.apiKey}")
    private String catalogApiKey;

    public ChargeBusiness(HttpServletRequest request,
                          WayRoleDao wayRoleDao,
                          AssetExService assetExService,
                          AssetExDao assetExDao,
                          ResourceUseInfoBusiness resourceUseInfoBusiness,
                          ResourceUseInfoDao resourceUseInfoDao,
                          DemandedInfoBusiness demandedInfoBusiness,
                          DemandedInfoDao demandedInfoDao,
                          ArchBusiUviewExBusiness archBusiUviewExBusiness,
                          OrganizationService organizationService, WayDao wayDao) {
        this.wayRoleDao = wayRoleDao;
        this.assetExService = assetExService;
        this.assetExDao = assetExDao;
        this.resourceUseInfoBusiness = resourceUseInfoBusiness;
        this.resourceUseInfoDao = resourceUseInfoDao;
        this.demandedInfoBusiness = demandedInfoBusiness;
        this.demandedInfoDao = demandedInfoDao;
        this.archBusiUviewExBusiness = archBusiUviewExBusiness;
        this.request = request;
        this.organizationService = organizationService;
        this.wayDao = wayDao;
    }

    /**
     * 获取代办--门户使用
     *
     * @return
     * @throws DoValidException
     */
    public List<WayChargeWebVm> findAll() throws DoValidException {
        JSONObject userObj = TokenManager.getInstance().getUserByToken(request);

        String userId = userObj.getString("id");
        String roleId = userObj.getString("roleId");
        String orgId = userObj.getString("organizationId");

        //0代办、1服务指引
        String weyType = "0";
        //当前角色拥有代办模块
        List<WayRoleEntity> wayRoles = wayRoleDao.findAllByRoleIdAndType(roleId, weyType);

        List<WayEntity> ways = wayDao.findAllByIdIn(wayRoles.stream().map(WayRoleEntity::getWayId).distinct().collect(Collectors.toList()));

        //结果
        List<WayChargeWebVm> results = new ArrayList<>();

        wayRoles.forEach(t -> {
            //目录管理初审(catalogingManagementAuditFirst)、目录管理终审(catalogingManagementAuditSecond)、
            //资源挂接审核(toExamineList)、资源使用审核(resourceUseInfo)、
            //部门需求审核(demandedInfoAudit)
            //目录管理办理(infoCatalogList)、资源挂接办理（submitList）
            switch (t.getWayId()) {
                //目录管理初审
                case "catalogingManagementAuditFirst":
                    WayChargeWebVm res1 = this.catalogingManagementAuditFirst(ways, orgId);
                    results.add(res1);
                    break;
                //目录管理终审
                case "catalogingManagementAuditSecond":
                    WayChargeWebVm res2 = this.catalogingManagementAuditSecond(ways);
                    results.add(res2);
                    break;
                //目录删除审核
                case "catalogingManagementAuditDelete":
                    WayChargeWebVm res3 = this.catalogingManagementAuditDelete(ways);
                    results.add(res3);
                    break;
                //资源挂接审核
                case "toExamineList":
                    WayChargeWebVm res4 = this.toExamineList(ways, userId, orgId);
                    results.add(res4);
                    break;
                //资源使用初审
                case "resourceUseInfoPrimary":
                    WayChargeWebVm res5 = this.resourceUseInfoPrimary(ways, roleId, orgId);
                    results.add(res5);
                    break;
                //资源使用审核
                case "resourceUseInfoUltimate":
                    WayChargeWebVm res6 = this.resourceUseInfoUltimate(ways, roleId, orgId);
                    results.add(res6);
                    break;
                //部门需求审核
                case "demandedInfoAudit":
                    WayChargeWebVm res7 = this.demandedInfoAudit(ways, roleId, orgId);
                    results.add(res7);
                    break;
                //目录管理办理
                case "infoCatalogList":
                    WayChargeWebVm res8 = this.infoCatalogList(ways, orgId);
                    results.add(res8);
                    break;
                //资源挂接办理
                case "submitList":
                    WayChargeWebVm res9 = this.submitList(ways, userId, orgId);
                    results.add(res9);
                    break;
                default:

                    break;
            }
        });

        return results.stream().sorted(Comparator.comparing(WayChargeWebVm::getSeq)).collect(Collectors.toList());
    }

    /**
     * 目录管理初审(部门审核员)
     *
     * @return
     */
    private WayChargeWebVm catalogingManagementAuditFirst(List<WayEntity> ways, String orgId) {
        //remark模块标识，与way对应id匹配
        String remark = "catalogingManagementAuditFirst";
        WayChargeWebVm res = new WayChargeWebVm();
        res.setRemark(remark);
        res.setRemarkName("目录管理初审");

        //path
        WayEntity way = this.getWayEntity(ways, remark);
        String path = way == null ? "" : way.getPath();
        res.setSeq(way == null ? 100 : way.getSeq());
        res.setPath(path);

        try {
            //num 和 data
            WayCatalogResVm resCatalog = this.sendDataCatalog(1, 5, 1, orgId);
            HashMap<String, Object> content = new HashMap<>();
            content.put("content", resCatalog.getData());
            res.setNum(resCatalog.getNum());
            res.setData(content);
            return res;
        } catch (Exception e) {
            return res;
        }
    }

    /**
     * 目录管理终审(主管部门权限)
     *
     * @return
     */
    private WayChargeWebVm catalogingManagementAuditSecond(List<WayEntity> ways) {
        //remark模块标识，与way对应id匹配
        String remark = "catalogingManagementAuditSecond";

        WayChargeWebVm res = new WayChargeWebVm();
        res.setRemark(remark);
        res.setRemarkName("目录管理终审");

        //path
        WayEntity way = this.getWayEntity(ways, remark);
        String path = way == null ? "" : way.getPath();
        res.setSeq(way == null ? 100 : way.getSeq());
        res.setPath(path);

        try {
            //num 和 data
            WayCatalogResVm resCatalog = this.sendDataCatalog(1, 5, 3, "");
            HashMap<String, Object> content = new HashMap<>();
            content.put("content", resCatalog.getData());

            res.setNum(resCatalog.getNum());
            res.setData(content);
            return res;
        } catch (Exception e) {
            return res;
        }
    }

    /**
     * 目录删除审核(主管部门权限)
     *
     * @return
     */
    private WayChargeWebVm catalogingManagementAuditDelete(List<WayEntity> ways) {
        //remark模块标识，与way对应id匹配
        String remark = "catalogingManagementAuditDelete";

        WayChargeWebVm res = new WayChargeWebVm();
        res.setRemark(remark);
        res.setRemarkName("目录删除审核");

        //path
        WayEntity way = this.getWayEntity(ways, remark);
        String path = way == null ? "" : way.getPath();
        res.setSeq(way == null ? 100 : way.getSeq());
        res.setPath(path);

        try {
            //num 和 data
            WayCatalogResVm resCatalog = this.sendDataCatalogDeleting(1, 5, null, "");
            HashMap<String, Object> content = new HashMap<>();
            content.put("content", resCatalog.getData());

            res.setNum(resCatalog.getNum());
            res.setData(content);
            return res;
        } catch (Exception e) {
            return res;
        }
    }

    /**
     * 资源挂接审核(主管部门权限)
     */
    private WayChargeWebVm toExamineList(List<WayEntity> ways, String userId, String orgId) {
        //remark模块标识，与way对应id匹配
        String remark = "toExamineList";
        Integer status = 1;

        WayChargeWebVm res = new WayChargeWebVm();
        res.setRemark(remark);
        res.setRemarkName("资源挂接审核");

        //path
        WayEntity way = this.getWayEntity(ways, remark);
        String path = way == null ? "" : way.getPath();
        res.setSeq(way == null ? 100 : way.getSeq());
        res.setPath(path);

        try {
            //筛选子部门数据
            List<String> orgs = new ArrayList<>();
            List<Integer> orgsInt = new ArrayList<>();

            Pageable pageable = PageRequest.of( 0 , 5 , Sort.Direction.DESC, "createTime" );

            if (!"admin".equals(userId)) {
                List<OrganizationEntity> orgList = organizationService.findAllByOrganizationId(orgId);
                orgs = orgList.stream().map(OrganizationEntity::getId).collect(Collectors.toList());

                //类型转换
                orgs.forEach(t -> {
                    try {
                        orgsInt.add(Integer.parseInt(t));
                    } catch (Exception e) {

                    }
                });

                Page<AssetExEntity> results = assetExDao.findAllByStatusAndOrgIdIn(status, orgsInt, pageable);
                res.setNum((int)results.getTotalElements());
                res.setData(results);
                return res;
            } else {
                Page<AssetExEntity> results = assetExDao.findAllByStatus(status, pageable);
                res.setNum((int)results.getTotalElements());
                res.setData(results);
                return res;
            }
        } catch (Exception e) {
            return res;
        }
    }

    /**
     * 资源使用初审(主管部门权限)
     */
    private WayChargeWebVm resourceUseInfoPrimary(List<WayEntity> ways, String roleId, String orgId) {
        //remark模块标识，与way对应id匹配
        String remark = "resourceUseInfoPrimary";
        String status = "0";

        WayChargeWebVm res = new WayChargeWebVm();
        res.setRemark(remark);
        res.setRemarkName("资源申请初审");

        //path
        WayEntity way = this.getWayEntity(ways, remark);
        String path = way == null ? "" : way.getPath();
        res.setSeq(way == null ? 100 : way.getSeq());
        res.setPath(path);

        try {
            Integer num = 0;
            //数量
            if (!roleId.contains("admin")) {
                num = resourceUseInfoDao.countByStatusAndProvOrgId(status, orgId);
            } else {
                num = resourceUseInfoDao.countByStatus(status);
            }

            //数据
            ResourceUseInfoFindAllVm model = new ResourceUseInfoFindAllVm();
            //0未审核1已审核2已驳回
            model.setStatus(status);
            model.setSize(5);
            Page<ResourceUseInfoEntity> results = resourceUseInfoBusiness.findAllByProvOrgId(model, request);

            res.setNum(num);
            res.setData(results);
            return res;
        } catch (Exception e) {
            return res;
        }
    }

    /**
     * 资源使用终审(主管部门权限)
     */
    private WayChargeWebVm resourceUseInfoUltimate(List<WayEntity> ways, String roleId, String orgId) {
        //remark模块标识，与way对应id匹配
        String remark = "resourceUseInfoUltimate";
        String status = "1";

        WayChargeWebVm res = new WayChargeWebVm();
        res.setRemark(remark);
        res.setRemarkName("资源申请终审");

        //path
        WayEntity way = this.getWayEntity(ways, remark);
        String path = way == null ? "" : way.getPath();
        res.setSeq(way == null ? 100 : way.getSeq());
        res.setPath(path);

        try {
            Integer num = 0;
            //数量
            if (!roleId.contains("admin")) {
                num = resourceUseInfoDao.countByStatusAndProvOrgId(status, orgId);
            } else {
                num = resourceUseInfoDao.countByStatus(status);
            }

            //数据
            ResourceUseInfoFindAllVm model = new ResourceUseInfoFindAllVm();
            //0未审核1已审核2已驳回
            model.setStatus(status);
            model.setSize(5);
            Page<ResourceUseInfoEntity> results = resourceUseInfoBusiness.findAllByProvOrgId(model, request);

            res.setNum(num);
            res.setData(results);
            return res;
        } catch (Exception e) {
            return res;
        }
    }

    /**
     * 部门需求审核(主管部门权限)
     */
    private WayChargeWebVm demandedInfoAudit(List<WayEntity> ways, String roleId, String orgId) {
        //remark模块标识，与way对应id匹配
        String remark = "demandedInfoAudit";
        String status = "0";

        WayChargeWebVm res = new WayChargeWebVm();
        res.setRemark(remark);
        res.setRemarkName("部门需求审核");

        //path
        WayEntity way = this.getWayEntity(ways, remark);
        String path = way == null ? "" : way.getPath();
        res.setSeq(way == null ? 100 : way.getSeq());
        res.setPath(path);

        try {
            Integer num = 0;
            //数量
            if (!roleId.contains("admin")) {
                num = demandedInfoDao.countByStatusAndAcceptDeptId(status, orgId);
            } else {
                num = demandedInfoDao.countByStatus(status);
            }

            //数据
            DemandedInfoFindAllVm model = new DemandedInfoFindAllVm();
            //0未受理1已受理2已驳回
            model.setStatus(status);
            model.setSize(5);
            Page<DemandedInfoEntity> results = demandedInfoBusiness.findAllByAcceptDeptId(model, request);

            res.setNum(num);
            res.setData(results);
            return res;
        } catch (Exception e) {
            return res;
        }
    }

    /**
     * 目录管理办理(部门维护员权限)
     */
    private WayChargeWebVm infoCatalogList(List<WayEntity> ways, String orgId) {
        //remark模块标识，与way对应id匹配
        String remark = "infoCatalogList";

        WayChargeWebVm res = new WayChargeWebVm();
        res.setRemark(remark);
        res.setRemarkName("目录管理办理");

        //path
        WayEntity way = this.getWayEntity(ways, remark);
        String path = way == null ? "" : way.getPath();
        res.setSeq(way == null ? 100 : way.getSeq());
        res.setPath(path);

        try {
            //状态，0草稿 1待审核 2初审未通过 3初审通过 4终审驳回 5终审通过
            WayCatalogResVm resCatalog1 = this.sendDataCatalog(1, 5, 2, orgId);
            WayCatalogResVm resCatalog2 = this.sendDataCatalog(1, 5, 4, orgId);

            //类型转换--拼接结果
            JSONArray arrayTotal = JSONArray.parseArray(resCatalog1.getData().toString());
            arrayTotal.addAll(JSONArray.parseArray(resCatalog2.getData().toString()));
            List<Object> listTotal = arrayTotal.subList(0, Math.min(arrayTotal.size(), 5));

            HashMap<String, Object> content = new HashMap<>();
            content.put("content", listTotal);

            res.setNum(resCatalog1.getNum() + resCatalog2.getNum());
            res.setData(content);
        } catch (Exception e) {
            return res;
        }
        return res;
    }

    /**
     * 资源挂接办理(部门维护员权限)
     */
    private WayChargeWebVm submitList(List<WayEntity> ways, String userId, String orgId) {
        //remark模块标识，与way对应id匹配
        String remark = "submitList";
        List<Integer> status = new ArrayList<>();
        status.add(1);
        status.add(3);

        WayChargeWebVm res = new WayChargeWebVm();
        res.setRemark(remark);
        res.setRemarkName("资源挂接办理");

        //path
        WayEntity way = this.getWayEntity(ways, remark);
        String path = way == null ? "" : way.getPath();
        res.setSeq(way == null ? 100 : way.getSeq());
        res.setPath(path);

        try {
            //筛选子部门数据
            List<String> orgs = new ArrayList<>();
            List<Integer> orgsInt = new ArrayList<>();

            Pageable pageable = PageRequest.of( 0 , 5  , Sort.Direction.DESC, "createTime");

            if (!"admin".equals(userId)) {
                List<OrganizationEntity> orgList = organizationService.findAllByOrganizationId(orgId);
                orgs = orgList.stream().map(OrganizationEntity::getId).collect(Collectors.toList());

                //类型转换
                orgs.forEach(t -> {
                    try {
                        orgsInt.add(Integer.parseInt(t));
                    } catch (Exception e) {

                    }
                });

                Page<AssetExEntity> results = assetExDao.findAllByStatusInAndOrgIdIn(status, orgsInt, pageable);
                res.setNum((int)results.getTotalElements());
                res.setData(results);
                return res;
            } else {
                Page<AssetExEntity> results = assetExDao.findAllByStatusIn(status, pageable);
                res.setNum((int)results.getTotalElements());
                res.setData(results);
                return res;
            }
        } catch (Exception e) {
            return res;
        }
    }

    /**
     * 请求资源目录系统数据
     *
     * @param page        当前第几页
     * @param limit       每页条数
     * @param auditStatus 状态，0草稿 1待审核 2初审未通过 3初审通过 4终审驳回 5终审通过
     * @param provOrgId   部门Id
     * @return
     * @throws Exception
     */
    private WayCatalogResVm sendDataCatalog(Integer page, Integer limit, Integer auditStatus, String provOrgId) throws Exception {
        HttpService httpService = new HttpService();
        //header
        Map<String, Object> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        //params
        Map<String, Object> params = new HashMap<>();
        params.put("apiKey", catalogApiKey);
        params.put("page", page);
        params.put("limit", limit);
        //状态，0草稿 1待审核 2初审未通过 3初审通过 4终审驳回 5终审通过
        params.put("auditStatus", auditStatus);
        if (!"headquarters".equals(provOrgId)) {
            params.put("provOrgId", provOrgId);
        }

        //请求
        String result = httpService.get(catalogRootPath + "/resourceCatalogTmp", headers, params);
        JSONObject jsonObject = JSONObject.parseObject(result);

        if ("-1".equals(jsonObject.getString("errno"))) {
            throw new Exception(jsonObject.getString("errmsg"));
        }

        JSONArray data = jsonObject.getJSONObject("data").getJSONArray("records");
        String numString = jsonObject.getJSONObject("data").getString("total");
        Integer num = StringUtils.isEmpty(numString) ? 0 : Integer.parseInt(numString);

        //结果
        WayCatalogResVm resVm = new WayCatalogResVm();
        resVm.setData(data);
        resVm.setNum(num);

        return resVm;
    }

    /**
     * 请求资源目录系统删除审批数据
     *
     * @param page        当前第几页
     * @param limit       每页条数
     * @param auditStatus 状态，0草稿 1待审核 2初审未通过 3初审通过 4终审驳回 5终审通过
     * @param provOrgId   部门Id
     * @return
     * @throws Exception
     */
    private WayCatalogResVm sendDataCatalogDeleting(Integer page, Integer limit, String[] auditStatus, String provOrgId) throws Exception {
        HttpService httpService = new HttpService();
        //header
        Map<String, Object> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        //params
        Map<String, Object> params = new HashMap<>();
        params.put("apiKey", catalogApiKey);
        params.put("page", page);
        params.put("limit", limit);
        //状态，0草稿 1待审核 2初审未通过 3初审通过 4终审驳回 5终审通过
//        params.put("auditStatus", auditStatus);
        if (!"headquarters".equals(provOrgId)) {
            params.put("provOrgId", provOrgId);
        }

        //请求
        String result = httpService.get(catalogRootPath + "/resourceCatalogDeleting", headers, params);
        JSONObject jsonObject = JSONObject.parseObject(result);

        if ("-1".equals(jsonObject.getString("errno"))) {
            throw new Exception(jsonObject.getString("errmsg"));
        }

        JSONArray data = jsonObject.getJSONObject("data").getJSONArray("records");
        String numString = jsonObject.getJSONObject("data").getString("total");
        Integer num = StringUtils.isEmpty(numString) ? 0 : Integer.parseInt(numString);

        //结果
        WayCatalogResVm resVm = new WayCatalogResVm();
        resVm.setData(data);
        resVm.setNum(num);

        return resVm;
    }

    /**
     * 通过ways获取way
     *
     * @param ways wayEntity列表
     * @param id   模块标识与 way id对应
     * @return
     */
    private WayEntity getWayEntity(List<WayEntity> ways, String id) {
        for (WayEntity way : ways) {
            if (way.getId().equals(id)) {
                return way;
            }
        }
        return null;
    }
}
