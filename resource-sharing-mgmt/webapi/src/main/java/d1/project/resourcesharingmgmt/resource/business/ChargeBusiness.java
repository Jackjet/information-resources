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
     * ????????????--????????????
     *
     * @return
     * @throws DoValidException
     */
    public List<WayChargeWebVm> findAll() throws DoValidException {
        JSONObject userObj = TokenManager.getInstance().getUserByToken(request);

        String userId = userObj.getString("id");
        String roleId = userObj.getString("roleId");
        String orgId = userObj.getString("organizationId");

        //0?????????1????????????
        String weyType = "0";
        //??????????????????????????????
        List<WayRoleEntity> wayRoles = wayRoleDao.findAllByRoleIdAndType(roleId, weyType);

        List<WayEntity> ways = wayDao.findAllByIdIn(wayRoles.stream().map(WayRoleEntity::getWayId).distinct().collect(Collectors.toList()));

        //??????
        List<WayChargeWebVm> results = new ArrayList<>();

        wayRoles.forEach(t -> {
            //??????????????????(catalogingManagementAuditFirst)?????????????????????(catalogingManagementAuditSecond)???
            //??????????????????(toExamineList)?????????????????????(resourceUseInfo)???
            //??????????????????(demandedInfoAudit)
            //??????????????????(infoCatalogList)????????????????????????submitList???
            switch (t.getWayId()) {
                //??????????????????
                case "catalogingManagementAuditFirst":
                    WayChargeWebVm res1 = this.catalogingManagementAuditFirst(ways, orgId);
                    results.add(res1);
                    break;
                //??????????????????
                case "catalogingManagementAuditSecond":
                    WayChargeWebVm res2 = this.catalogingManagementAuditSecond(ways);
                    results.add(res2);
                    break;
                //??????????????????
                case "catalogingManagementAuditDelete":
                    WayChargeWebVm res3 = this.catalogingManagementAuditDelete(ways);
                    results.add(res3);
                    break;
                //??????????????????
                case "toExamineList":
                    WayChargeWebVm res4 = this.toExamineList(ways, userId, orgId);
                    results.add(res4);
                    break;
                //??????????????????
                case "resourceUseInfoPrimary":
                    WayChargeWebVm res5 = this.resourceUseInfoPrimary(ways, roleId, orgId);
                    results.add(res5);
                    break;
                //??????????????????
                case "resourceUseInfoUltimate":
                    WayChargeWebVm res6 = this.resourceUseInfoUltimate(ways, roleId, orgId);
                    results.add(res6);
                    break;
                //??????????????????
                case "demandedInfoAudit":
                    WayChargeWebVm res7 = this.demandedInfoAudit(ways, roleId, orgId);
                    results.add(res7);
                    break;
                //??????????????????
                case "infoCatalogList":
                    WayChargeWebVm res8 = this.infoCatalogList(ways, orgId);
                    results.add(res8);
                    break;
                //??????????????????
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
     * ??????????????????(???????????????)
     *
     * @return
     */
    private WayChargeWebVm catalogingManagementAuditFirst(List<WayEntity> ways, String orgId) {
        //remark??????????????????way??????id??????
        String remark = "catalogingManagementAuditFirst";
        WayChargeWebVm res = new WayChargeWebVm();
        res.setRemark(remark);
        res.setRemarkName("??????????????????");

        //path
        WayEntity way = this.getWayEntity(ways, remark);
        String path = way == null ? "" : way.getPath();
        res.setSeq(way == null ? 100 : way.getSeq());
        res.setPath(path);

        try {
            //num ??? data
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
     * ??????????????????(??????????????????)
     *
     * @return
     */
    private WayChargeWebVm catalogingManagementAuditSecond(List<WayEntity> ways) {
        //remark??????????????????way??????id??????
        String remark = "catalogingManagementAuditSecond";

        WayChargeWebVm res = new WayChargeWebVm();
        res.setRemark(remark);
        res.setRemarkName("??????????????????");

        //path
        WayEntity way = this.getWayEntity(ways, remark);
        String path = way == null ? "" : way.getPath();
        res.setSeq(way == null ? 100 : way.getSeq());
        res.setPath(path);

        try {
            //num ??? data
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
     * ??????????????????(??????????????????)
     *
     * @return
     */
    private WayChargeWebVm catalogingManagementAuditDelete(List<WayEntity> ways) {
        //remark??????????????????way??????id??????
        String remark = "catalogingManagementAuditDelete";

        WayChargeWebVm res = new WayChargeWebVm();
        res.setRemark(remark);
        res.setRemarkName("??????????????????");

        //path
        WayEntity way = this.getWayEntity(ways, remark);
        String path = way == null ? "" : way.getPath();
        res.setSeq(way == null ? 100 : way.getSeq());
        res.setPath(path);

        try {
            //num ??? data
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
     * ??????????????????(??????????????????)
     */
    private WayChargeWebVm toExamineList(List<WayEntity> ways, String userId, String orgId) {
        //remark??????????????????way??????id??????
        String remark = "toExamineList";
        Integer status = 1;

        WayChargeWebVm res = new WayChargeWebVm();
        res.setRemark(remark);
        res.setRemarkName("??????????????????");

        //path
        WayEntity way = this.getWayEntity(ways, remark);
        String path = way == null ? "" : way.getPath();
        res.setSeq(way == null ? 100 : way.getSeq());
        res.setPath(path);

        try {
            //?????????????????????
            List<String> orgs = new ArrayList<>();
            List<Integer> orgsInt = new ArrayList<>();

            Pageable pageable = PageRequest.of( 0 , 5 , Sort.Direction.DESC, "createTime" );

            if (!"admin".equals(userId)) {
                List<OrganizationEntity> orgList = organizationService.findAllByOrganizationId(orgId);
                orgs = orgList.stream().map(OrganizationEntity::getId).collect(Collectors.toList());

                //????????????
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
     * ??????????????????(??????????????????)
     */
    private WayChargeWebVm resourceUseInfoPrimary(List<WayEntity> ways, String roleId, String orgId) {
        //remark??????????????????way??????id??????
        String remark = "resourceUseInfoPrimary";
        String status = "0";

        WayChargeWebVm res = new WayChargeWebVm();
        res.setRemark(remark);
        res.setRemarkName("??????????????????");

        //path
        WayEntity way = this.getWayEntity(ways, remark);
        String path = way == null ? "" : way.getPath();
        res.setSeq(way == null ? 100 : way.getSeq());
        res.setPath(path);

        try {
            Integer num = 0;
            //??????
            if (!roleId.contains("admin")) {
                num = resourceUseInfoDao.countByStatusAndProvOrgId(status, orgId);
            } else {
                num = resourceUseInfoDao.countByStatus(status);
            }

            //??????
            ResourceUseInfoFindAllVm model = new ResourceUseInfoFindAllVm();
            //0?????????1?????????2?????????
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
     * ??????????????????(??????????????????)
     */
    private WayChargeWebVm resourceUseInfoUltimate(List<WayEntity> ways, String roleId, String orgId) {
        //remark??????????????????way??????id??????
        String remark = "resourceUseInfoUltimate";
        String status = "1";

        WayChargeWebVm res = new WayChargeWebVm();
        res.setRemark(remark);
        res.setRemarkName("??????????????????");

        //path
        WayEntity way = this.getWayEntity(ways, remark);
        String path = way == null ? "" : way.getPath();
        res.setSeq(way == null ? 100 : way.getSeq());
        res.setPath(path);

        try {
            Integer num = 0;
            //??????
            if (!roleId.contains("admin")) {
                num = resourceUseInfoDao.countByStatusAndProvOrgId(status, orgId);
            } else {
                num = resourceUseInfoDao.countByStatus(status);
            }

            //??????
            ResourceUseInfoFindAllVm model = new ResourceUseInfoFindAllVm();
            //0?????????1?????????2?????????
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
     * ??????????????????(??????????????????)
     */
    private WayChargeWebVm demandedInfoAudit(List<WayEntity> ways, String roleId, String orgId) {
        //remark??????????????????way??????id??????
        String remark = "demandedInfoAudit";
        String status = "0";

        WayChargeWebVm res = new WayChargeWebVm();
        res.setRemark(remark);
        res.setRemarkName("??????????????????");

        //path
        WayEntity way = this.getWayEntity(ways, remark);
        String path = way == null ? "" : way.getPath();
        res.setSeq(way == null ? 100 : way.getSeq());
        res.setPath(path);

        try {
            Integer num = 0;
            //??????
            if (!roleId.contains("admin")) {
                num = demandedInfoDao.countByStatusAndAcceptDeptId(status, orgId);
            } else {
                num = demandedInfoDao.countByStatus(status);
            }

            //??????
            DemandedInfoFindAllVm model = new DemandedInfoFindAllVm();
            //0?????????1?????????2?????????
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
     * ??????????????????(?????????????????????)
     */
    private WayChargeWebVm infoCatalogList(List<WayEntity> ways, String orgId) {
        //remark??????????????????way??????id??????
        String remark = "infoCatalogList";

        WayChargeWebVm res = new WayChargeWebVm();
        res.setRemark(remark);
        res.setRemarkName("??????????????????");

        //path
        WayEntity way = this.getWayEntity(ways, remark);
        String path = way == null ? "" : way.getPath();
        res.setSeq(way == null ? 100 : way.getSeq());
        res.setPath(path);

        try {
            //?????????0?????? 1????????? 2??????????????? 3???????????? 4???????????? 5????????????
            WayCatalogResVm resCatalog1 = this.sendDataCatalog(1, 5, 2, orgId);
            WayCatalogResVm resCatalog2 = this.sendDataCatalog(1, 5, 4, orgId);

            //????????????--????????????
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
     * ??????????????????(?????????????????????)
     */
    private WayChargeWebVm submitList(List<WayEntity> ways, String userId, String orgId) {
        //remark??????????????????way??????id??????
        String remark = "submitList";
        List<Integer> status = new ArrayList<>();
        status.add(1);
        status.add(3);

        WayChargeWebVm res = new WayChargeWebVm();
        res.setRemark(remark);
        res.setRemarkName("??????????????????");

        //path
        WayEntity way = this.getWayEntity(ways, remark);
        String path = way == null ? "" : way.getPath();
        res.setSeq(way == null ? 100 : way.getSeq());
        res.setPath(path);

        try {
            //?????????????????????
            List<String> orgs = new ArrayList<>();
            List<Integer> orgsInt = new ArrayList<>();

            Pageable pageable = PageRequest.of( 0 , 5  , Sort.Direction.DESC, "createTime");

            if (!"admin".equals(userId)) {
                List<OrganizationEntity> orgList = organizationService.findAllByOrganizationId(orgId);
                orgs = orgList.stream().map(OrganizationEntity::getId).collect(Collectors.toList());

                //????????????
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
     * ??????????????????????????????
     *
     * @param page        ???????????????
     * @param limit       ????????????
     * @param auditStatus ?????????0?????? 1????????? 2??????????????? 3???????????? 4???????????? 5????????????
     * @param provOrgId   ??????Id
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
        //?????????0?????? 1????????? 2??????????????? 3???????????? 4???????????? 5????????????
        params.put("auditStatus", auditStatus);
        if (!"headquarters".equals(provOrgId)) {
            params.put("provOrgId", provOrgId);
        }

        //??????
        String result = httpService.get(catalogRootPath + "/resourceCatalogTmp", headers, params);
        JSONObject jsonObject = JSONObject.parseObject(result);

        if ("-1".equals(jsonObject.getString("errno"))) {
            throw new Exception(jsonObject.getString("errmsg"));
        }

        JSONArray data = jsonObject.getJSONObject("data").getJSONArray("records");
        String numString = jsonObject.getJSONObject("data").getString("total");
        Integer num = StringUtils.isEmpty(numString) ? 0 : Integer.parseInt(numString);

        //??????
        WayCatalogResVm resVm = new WayCatalogResVm();
        resVm.setData(data);
        resVm.setNum(num);

        return resVm;
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param page        ???????????????
     * @param limit       ????????????
     * @param auditStatus ?????????0?????? 1????????? 2??????????????? 3???????????? 4???????????? 5????????????
     * @param provOrgId   ??????Id
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
        //?????????0?????? 1????????? 2??????????????? 3???????????? 4???????????? 5????????????
//        params.put("auditStatus", auditStatus);
        if (!"headquarters".equals(provOrgId)) {
            params.put("provOrgId", provOrgId);
        }

        //??????
        String result = httpService.get(catalogRootPath + "/resourceCatalogDeleting", headers, params);
        JSONObject jsonObject = JSONObject.parseObject(result);

        if ("-1".equals(jsonObject.getString("errno"))) {
            throw new Exception(jsonObject.getString("errmsg"));
        }

        JSONArray data = jsonObject.getJSONObject("data").getJSONArray("records");
        String numString = jsonObject.getJSONObject("data").getString("total");
        Integer num = StringUtils.isEmpty(numString) ? 0 : Integer.parseInt(numString);

        //??????
        WayCatalogResVm resVm = new WayCatalogResVm();
        resVm.setData(data);
        resVm.setNum(num);

        return resVm;
    }

    /**
     * ??????ways??????way
     *
     * @param ways wayEntity??????
     * @param id   ??????????????? way id??????
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
