package d1.project.resourcesharingmgmt.resource.business;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.TokenManager;
import d1.project.resourcesharingmgmt.common.Constants;
import d1.project.resourcesharingmgmt.common.model.OperationLog;
import d1.project.resourcesharingmgmt.common.service.IOperationLogService;
import d1.project.resourcesharingmgmt.common.utils.BaseUtils;
import d1.project.resourcesharingmgmt.resource.entity.*;
import d1.project.resourcesharingmgmt.resource.model.DemandedInfo.DemandedAnalysisVm;
import d1.project.resourcesharingmgmt.resource.model.DemandedInfo.DemandedByReasonVm;
import d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo.*;
import d1.project.resourcesharingmgmt.resource.model.ResourceUseInfoAnalysisVm;
import d1.project.resourcesharingmgmt.resource.service.*;
import d1.project.resourcesharingmgmt.system.entity.OrganizationEntity;
import d1.project.resourcesharingmgmt.system.entity.WebAdminUserEntity;
import d1.project.resourcesharingmgmt.system.service.IWebAdminUserService;
import d1.project.resourcesharingmgmt.system.service.OrganizationService;
import net.dcrun.component.http.HttpService;
import net.dcrun.component.security.HmacSignService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 资源使用
 *
 * @author JungYoung
 */
@Service
public class ResourceUseInfoBusiness {
    private final ResourceUseInfoService resourceUseInfoService;
    private final ResourceUseLogService resourceUseLogService;
    private final ArchBusiUviewExService archBusiUviewExService;
    private final AssetApiExService assetApiExService;
    private final AssetFileExService assetFileExService;
    private final AssetDataExService assetDataExService;
    private final IOperationLogService iOperationLogService;
    private final IWebAdminUserService iWebAdminUserService;
    private final OrganizationService organizationService;

    @Value("${apiIntegrate.url}")
    private String apiIntegrate_url;

    @Value("${apiIntegrate.appid}")
    private String apiIntegrate_appid;

    @Value("${apiIntegrate.appkey}")
    private String apiIntegrate_appkey;

    public ResourceUseInfoBusiness(ResourceUseInfoService resourceUseInfoService, ResourceUseLogService resourceUseLogService, ArchBusiUviewExService archBusiUviewExService, AssetApiExService assetApiExService, AssetFileExService assetFileExService, AssetDataExService assetDataExService, IOperationLogService iOperationLogService, IWebAdminUserService iWebAdminUserService, OrganizationService organizationService) {
        this.resourceUseInfoService = resourceUseInfoService;
        this.resourceUseLogService = resourceUseLogService;
        this.archBusiUviewExService = archBusiUviewExService;
        this.assetApiExService = assetApiExService;
        this.assetFileExService = assetFileExService;
        this.assetDataExService = assetDataExService;
        this.iOperationLogService = iOperationLogService;
        this.iWebAdminUserService = iWebAdminUserService;
        this.organizationService = organizationService;
    }

    @Transactional(rollbackFor = Exception.class)
    public void insert(ResourceUseInfoInsertVm model, HttpServletRequest request) throws DoValidException {
        String uviewId = model.getUviewId();

        JSONObject user = TokenManager.getInstance().getUserByToken(request);
        ResourceUseInfoEntity resourceUseInfoEntity = new ResourceUseInfoEntity();
        ArchBusiUviewExEntity archBusiUviewExEntity = archBusiUviewExService.findByUviewId(uviewId).orElseThrow(() -> new DoValidException("信息资源目录不存在"));

        ResourceUseInfoEntity entity = new ResourceUseInfoEntity();
        if ("1".equals(model.getType())) {
            if (StringUtils.isEmpty(model.getUviewApiId())) {
                throw new DoValidException("云接口ID不可为空");
            }
            if (StringUtils.isEmpty(model.getSourceApiName())) {
                throw new DoValidException("云接口名称不可为空");
            }
            if (resourceUseInfoService.existsByUviewApiIdAndCreateById(model.getUviewApiId(), user.getString("id"))) {
                entity = resourceUseInfoService.findFirstByUviewApiIdAndCreateByIdOrderByCreateTimeDesc(model.getUviewApiId(), user.getString("id"));
            }
            AssetApiExEntity assetApiExEntity = assetApiExService.find(model.getUviewApiId()).orElseThrow(() -> new DoValidException("云接口不存在"));
            resourceUseInfoEntity.setResourceName(assetApiExEntity.getName());

            resourceUseInfoEntity.setName(assetApiExEntity.getName());
            String host = "";
            JSONArray jsonArray = JSONObject.parseArray(assetApiExEntity.getHost());
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject job = jsonArray.getJSONObject(i);
                host += job.getString("target") + ",";
            }
            if (host.length() > 0) {
                host = host.substring(0, host.length() - 1);
            }

            resourceUseInfoEntity.setHost(host);
            resourceUseInfoEntity.setPath(assetApiExEntity.getPath());
            resourceUseInfoEntity.setProtocol(assetApiExEntity.getProtocol());
            resourceUseInfoEntity.setMethod(assetApiExEntity.getMethod());
            resourceUseInfoEntity.setFormatType(assetApiExEntity.getFormatType());
            resourceUseInfoEntity.setBody(assetApiExEntity.getBody());
            resourceUseInfoEntity.setParams(assetApiExEntity.getParams());
            resourceUseInfoEntity.setResponse(assetApiExEntity.getResponse());
            resourceUseInfoEntity.setConstants(assetApiExEntity.getConstants());
            resourceUseInfoEntity.setApiId(assetApiExEntity.getSourceApiId());
        } else if ("2".equals(model.getType())) {
            if (StringUtils.isEmpty(model.getFileId())) {
                throw new DoValidException("云文件ID不可为空");
            }
            if (StringUtils.isEmpty(model.getFileName())) {
                throw new DoValidException("云文件名称不可为空");
            }
            if (resourceUseInfoService.existsByFileIdAndCreateById(model.getFileId(), user.getString("id"))) {
                entity = resourceUseInfoService.findFirstByFileIdAndCreateByIdOrderByCreateTimeDesc(model.getFileId(), user.getString("id"));
            }
            AssetFileExEntity assetFileExEntity = assetFileExService.find(model.getFileId()).orElseThrow(() -> new DoValidException("云文件不存在"));
            resourceUseInfoEntity.setResourceName(assetFileExEntity.getName());
            resourceUseInfoEntity.setFileName(assetFileExEntity.getName());
            resourceUseInfoEntity.setFileDownloadUri(assetFileExEntity.getFileDownloadUri());
            resourceUseInfoEntity.setFileType(assetFileExEntity.getFileType());
            resourceUseInfoEntity.setFileDetail(assetFileExEntity.getDetail());
        } else if ("3".equals(model.getType())) {
            if (StringUtils.isEmpty(model.getDataId())) {
                throw new DoValidException("云数据ID不可为空");
            }
            if (StringUtils.isEmpty(model.getTableName())) {
                throw new DoValidException("云数据表名不可为空");
            }
            if (resourceUseInfoService.existsByDataIdAndCreateById(model.getDataId(), user.getString("id"))) {
                entity = resourceUseInfoService.findFirstByDataIdAndCreateByIdOrderByCreateTimeDesc(model.getDataId(), user.getString("id"));
            }
            AssetDataExEntity assetDataExEntity = assetDataExService.find(model.getDataId()).orElseThrow(() -> new DoValidException("云数据不存在"));
            resourceUseInfoEntity.setResourceName(assetDataExEntity.getTableName());
            resourceUseInfoEntity.setIp(assetDataExEntity.getIp());
            resourceUseInfoEntity.setPort(assetDataExEntity.getPort());
            resourceUseInfoEntity.setUsername(assetDataExEntity.getUsername());
            resourceUseInfoEntity.setPassword(assetDataExEntity.getPassword());
            resourceUseInfoEntity.setDatabaseName(assetDataExEntity.getDatabaseName());
            resourceUseInfoEntity.setTableName(assetDataExEntity.getTableName());
            resourceUseInfoEntity.setDataDetail(assetDataExEntity.getDetail());
        }
        if (entity.getStatus() != null) {
            String msg = "";
            switch (entity.getStatus()) {
                case "0":
                case "1":
                case "2":
                case "4":
                    msg = "您已申请过该资源，请勿重复申请，可到用户中心-我的云数据查看";
                    break;
                case "3":
                    //资源申请驳回的可以继续申请
                    break;
                default:
                    msg = Constants.RESOURCE_IS_EXIST;
                    break;
            }
            if (!StringUtils.isEmpty(msg)) {
                throw new DoValidException(msg);
            }
        }

        //无条件共享无需进行初审直接进行终审，有条件共享需要进行初审再进行终审
        if ("01".equals(archBusiUviewExEntity.getShareLv())) {
            resourceUseInfoEntity.setStatus("1");
        } else {
            resourceUseInfoEntity.setStatus("0");
        }

        //拷贝资源目录数据
        resourceUseInfoEntity.setProvOrgId(archBusiUviewExEntity.getProvOrgId());
        resourceUseInfoEntity.setProvOrgCode(archBusiUviewExEntity.getProvOrgCode());
        OrganizationEntity organizationEntity = organizationService.find(archBusiUviewExEntity.getProvOrgId()).orElseThrow(() -> new DoValidException("提供部门不存在"));
        resourceUseInfoEntity.setProvOrgName(organizationEntity.getName());
        resourceUseInfoEntity.setUviewNm(archBusiUviewExEntity.getUviewNm());
        resourceUseInfoEntity.setUviewNo(archBusiUviewExEntity.getUviewNo());
        resourceUseInfoEntity.setUpdateCyc(archBusiUviewExEntity.getUpdateCyc());
        resourceUseInfoEntity.setUviewDesc(archBusiUviewExEntity.getUviewDesc());
        resourceUseInfoEntity.setShareLv(archBusiUviewExEntity.getShareLv());
        resourceUseInfoEntity.setCreateDeptId(user.getString("organizationId"));
        resourceUseInfoEntity.setCreateDeptName(user.getString("organizationName"));

        BeanUtils.copyProperties(model, resourceUseInfoEntity);
        TokenManager.getInstance().updateCreateIdAndTime(request, resourceUseInfoEntity);
        resourceUseInfoEntity.setId(BaseUtils.generate32Id());
        resourceUseInfoService.insert(resourceUseInfoEntity);
        iOperationLogService.insert(new OperationLog("资源使用管理_我的关注", "新增", "新增资源使用" + resourceUseInfoEntity.getUviewNm(), JSON.toJSONString(resourceUseInfoEntity), 1), request);
    }

    @Transactional(rollbackFor = Exception.class)
    public void audit(ResourceUseInfoAuditVm model, HttpServletRequest request) throws Exception {
        JSONObject user = TokenManager.getInstance().getUserByToken(request);
        ResourceUseInfoEntity resourceUseInfoEntity = resourceUseInfoService.find(model.getId()).orElseThrow(() -> new DoValidException("资源使用信息不存在"));
        //资源使用日志
        ResourceUseLogEntity resourceUseLogEntity = new ResourceUseLogEntity();

        if ("1".equals(model.getStatus())) {
            model.setAuditDesc("审核通过");
            resourceUseInfoEntity.setStatus("1");
            resourceUseInfoEntity.setAuditDesc(model.getAuditDesc());
            resourceUseLogEntity.setAuditDesc(model.getAuditDesc());
        } else {
            resourceUseInfoEntity.setStatus("3");
            resourceUseInfoEntity.setRejectDetail(model.getRejectDetail());

            resourceUseLogEntity.setAuditDesc(model.getRejectDetail());
        }
        resourceUseInfoService.save(resourceUseInfoEntity);

        resourceUseLogEntity.setId(BaseUtils.generate32Id());
        resourceUseLogEntity.setResourceId(resourceUseInfoEntity.getId());
        resourceUseLogEntity.setResourceName(resourceUseInfoEntity.getResourceName());
        resourceUseLogEntity.setCreateByName(user.getString("name"));
        resourceUseLogEntity.setProcessName("初审");
        resourceUseLogEntity.setStatus(model.getStatus());
        TokenManager.getInstance().updateCreateIdAndTime(request, resourceUseLogEntity);

        resourceUseLogService.save(resourceUseLogEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(ResourceUseInfoUpdateVm model, HttpServletRequest request) throws Exception {
        JSONObject user = TokenManager.getInstance().getUserByToken(request);
        ResourceUseInfoEntity resourceUseInfoEntity = resourceUseInfoService.find(model.getId()).orElseThrow(() -> new DoValidException("资源使用信息不存在"));
        JSONObject jsonObject = new JSONObject();

        //资源使用日志
        ResourceUseLogEntity resourceUseLogEntity = new ResourceUseLogEntity();

        if ("1".equals(model.getStatus())) {
            model.setAuditDesc("审核通过");
            resourceUseLogEntity.setAuditDesc(model.getAuditDesc());
            if ("1".equals(resourceUseInfoEntity.getType())) {
                //API申请通过时，调用审核接口
                HttpService httpService = new HttpService();
                Map<String, Object> headers = new HashMap<>();
                long timestamp = System.currentTimeMillis();
                headers.put("appid", apiIntegrate_appid);
                headers.put("timestamp", timestamp);
                HmacSignService hmacSignService = new HmacSignService();
                String sign = hmacSignService.sign(apiIntegrate_appid, String.valueOf(timestamp), apiIntegrate_appkey);
                headers.put("Authorization", "sign " + sign);

                Map<String, Object> params = new HashMap<>();
                params.put("userId", resourceUseInfoEntity.getCreateById());
                params.put("uviewId", resourceUseInfoEntity.getUviewId());
                WebAdminUserEntity webAdminUserEntity = iWebAdminUserService.findById(resourceUseInfoEntity.getCreateById()).orElseThrow(() -> new DoValidException("用户信息不存在"));
                params.put("username", webAdminUserEntity.getAccount());
                params.put("perMinute", model.getPerMinute());
                params.put("everyHour", model.getEveryHour());
                params.put("everyDay", model.getEveryDay());
                params.put("singleSize", model.getSingleSize());

                AssetApiExEntity assetApiExEntity = assetApiExService.find(resourceUseInfoEntity.getUviewApiId()).orElseThrow(() -> new DoValidException("资源目录与云数据关系不存在"));
                SourceApiVm sourceApiVm = new SourceApiVm();
                sourceApiVm.setName(assetApiExEntity.getName());
                String host = "";
                JSONArray jsonArray = JSONObject.parseArray(assetApiExEntity.getHost());
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject job = jsonArray.getJSONObject(i);
                    host += job.getString("target") + ",";
                }
                if (host.length() > 0) {
                    host = host.substring(0, host.length() - 1);
                }

                sourceApiVm.setHost(host);
                sourceApiVm.setPath(assetApiExEntity.getPath());
                sourceApiVm.setProtocol(assetApiExEntity.getProtocol());
                sourceApiVm.setMethod(assetApiExEntity.getMethod());
                sourceApiVm.setFormatType(assetApiExEntity.getFormatType());
                sourceApiVm.setBody(assetApiExEntity.getBody());
                sourceApiVm.setParams(assetApiExEntity.getParams());
                sourceApiVm.setResponse(assetApiExEntity.getResponse());
                sourceApiVm.setConstants(assetApiExEntity.getConstants());
                sourceApiVm.setApiId(assetApiExEntity.getSourceApiId());
                params.put("sourceApi", sourceApiVm);

                String result = httpService.post(apiIntegrate_url + "/webadmin/api/ext/insert", headers, params, "application/json");
                JSONObject jResult = JSONObject.parseObject(result);
                String code = jResult.getString("code");
                String msg = jResult.getString("msg");
                String data = jResult.getString("data");
                jsonObject.put("msg", msg);
                if ("1".equals(code)) {
                    model.setStatus("2");
                    SourceApiVm sourceApiVm1 = JSONObject.parseObject(data, SourceApiVm.class);
                    jsonObject.put("container", sourceApiVm1.getContainer());
                    jsonObject.put("routeInfo", sourceApiVm1.getRouteInfo());
                    jsonObject.put("kongApiId", sourceApiVm1.getKongApiId());
                } else {
                    model.setStatus("4");
                    iOperationLogService.insert(new OperationLog("资源使用管理_对接审核", "对接api系统_审核", "对接api系统_审核资源使用：" + resourceUseInfoEntity.getUviewNm() + ";Api系统失败原因:" + msg, JSON.toJSONString(resourceUseInfoEntity), 1), request);
                }
            } else if ("2".equals(resourceUseInfoEntity.getType())) {
                AssetFileExEntity assetFileExEntity = assetFileExService.find(resourceUseInfoEntity.getFileId()).orElseThrow(() -> new DoValidException("资源目录与云文件关系不存在"));
                model.setStatus("2");
            } else if ("3".equals(resourceUseInfoEntity.getType())) {
                AssetDataExEntity assetDataExEntity = assetDataExService.find(resourceUseInfoEntity.getDataId()).orElseThrow(() -> new DoValidException("资源目录与云数据关系不存在"));
                jsonObject.put("ip", model.getIp());
                jsonObject.put("port", model.getPort());
                jsonObject.put("username", model.getUsername());
                jsonObject.put("password", model.getPassword());
                jsonObject.put("databaseName", model.getDatabaseName());
                jsonObject.put("tableName", model.getTableName());
                jsonObject.put("dataDetail", model.getDataDetail());

                model.setStatus("2");
            }
        } else {
            model.setStatus("3");
            resourceUseLogEntity.setAuditDesc(model.getRejectDetail());
        }

        jsonObject.put("id", model.getId());
        jsonObject.put("supportBusiness", model.getSupportBusiness());
        jsonObject.put("perMinute", model.getPerMinute());
        jsonObject.put("everyHour", model.getEveryHour());
        jsonObject.put("everyDay", model.getEveryDay());
        jsonObject.put("singleSize", model.getSingleSize());
        jsonObject.put("status", model.getStatus());
        jsonObject.put("auditDesc", model.getAuditDesc());
        jsonObject.put("rejectDetail", model.getRejectDetail());
        resourceUseInfoService.update(jsonObject, request);

        resourceUseLogEntity.setId(BaseUtils.generate32Id());
        resourceUseLogEntity.setResourceId(resourceUseInfoEntity.getId());
        resourceUseLogEntity.setResourceName(resourceUseInfoEntity.getResourceName());
        resourceUseLogEntity.setCreateByName(user.getString("name"));
        resourceUseLogEntity.setProcessName("终审");
        resourceUseLogEntity.setStatus(model.getStatus());
        TokenManager.getInstance().updateCreateIdAndTime(request, resourceUseLogEntity);
        resourceUseLogService.save(resourceUseLogEntity);
    }

    public ResourceUseInfoEntity find(String id) throws DoValidException {
        ResourceUseInfoEntity archBusiUviewExEntity = resourceUseInfoService.find(id).orElseThrow(() -> new DoValidException("资源使用不存在"));
        return archBusiUviewExEntity;
    }

    public Page<ResourceUseInfoEntity> findAll(ResourceUseInfoFindAllVm model, HttpServletRequest request) throws Exception {
        JSONObject webAdminUserVm = TokenManager.getInstance().getUserByToken(request);
        model.setCreateById(webAdminUserVm.getString("id"));
        return resourceUseInfoService.findAll(model);
    }

    /**
     * 删除
     *
     * @param id
     * @param request
     * @throws DoValidException
     */
    public void delete(String id, HttpServletRequest request) throws DoValidException {
        ResourceUseInfoEntity archBusiUviewExEntity = resourceUseInfoService.find(id).orElseThrow(() -> new DoValidException("资源使用不存在"));
        JSONObject user = TokenManager.getInstance().getUserByToken(request);
//        if (!"0".equals(archBusiUviewExEntity.getStatus())) {
//            throw new DoValidException(Constants.RESOURCE_IS_ACCEP);
//        }

        if (archBusiUviewExEntity.getCreateById().equals(user.getString("id"))) {
            resourceUseInfoService.delete(archBusiUviewExEntity.getId());
        } else {
            throw new DoValidException(Constants.RESOURCE_IS_NOT_USER);
        }

    }

    /**
     * 我的待办-需求使用申请
     *
     * @param request
     * @return
     * @throws DoValidException
     */
    public ResourceUseInfoAnalysisVm findAnalysis(HttpServletRequest request) throws DoValidException {
        JSONObject user = TokenManager.getInstance().getUserByToken(request);
        ResourceUseInfoAnalysisVm resourceUseInfoAnalysisVm = resourceUseInfoService.findAnalysis(user.getString("id"));
        return resourceUseInfoAnalysisVm;
    }

    public Page<ResourceUseInfoEntity> findAllByProvOrgId(ResourceUseInfoFindAllVm model, HttpServletRequest request) throws Exception {
        JSONObject webAdminUserVm = TokenManager.getInstance().getUserByToken(request);

        if (!webAdminUserVm.getString("roleId").contains("admin")) {
            model.setProvOrgId(webAdminUserVm.getString("organizationId"));
        }
        return resourceUseInfoService.findAll(model);
    }

    public Page<ResourceUseInfoEntity> findAllByProvOrgId(ResourceUseInfoFindAllByProvOrgIdVm model, HttpServletRequest request) throws Exception {
        JSONObject pram = (JSONObject) JSON.toJSON(model);
        pram.put("status", "2");
        if("headquarters".equals(model.getCreateDeptId())){
            model.setCreateDeptId("");
        }
        return resourceUseInfoService.findAll(pram);
    }

    public List<ResourceUseExcelExport> findExportByProvOrgId(ResourceUseInfoFindAllByProvOrgIdVm model) throws Exception {
        JSONObject pram = (JSONObject) JSON.toJSON(model);
        pram.put("status", "2");
        if("headquarters".equals(model.getCreateDeptId())){
            model.setCreateDeptId("");
        }
        return resourceUseInfoService.findExportList(pram);
    }

    /**
     * 返回excel
     */
    public void easyExcelWrite(HttpServletResponse response, List<ResourceUseExcelExport> allList) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("申请方统计详情", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), ResourceUseExcelExport.class).sheet("申请方统计详情").doWrite(allList);
    }

    /**
     * 统计数量
     */
    public ResourceUseCountAnalysisVm getResourceUseCountAnalysis(HttpServletRequest request) {
        ResourceUseCountAnalysisVm vm = new ResourceUseCountAnalysisVm();
        vm.setOrg(resourceUseInfoService.countByOrgNumDistinct());
        vm.setNum(resourceUseInfoService.count());
        vm.setProcessed(resourceUseInfoService.countByStatus("2"));
        vm.setReject(resourceUseInfoService.countByStatus("3"));

        return vm;
    }

    /**
     * 资源类型
     */
    public ResourceUserByTypeVm getResourceUseCountByType(HttpServletRequest request) {
        ResourceUserByTypeVm vm = new ResourceUserByTypeVm();
        vm.setApi(resourceUseInfoService.countByType("1"));
        vm.setFile(resourceUseInfoService.countByType("2"));
        vm.setData(resourceUseInfoService.countByType("3"));

        return vm;
    }

    public List<ResourceByMonthVm> getResourceUseByMonth(HttpServletRequest request) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
        Calendar month = Calendar.getInstance();
        List<ResourceByMonthVm> list = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            String monthStr = df.format(month.getTime());
            ResourceByMonthVm vm = new ResourceByMonthVm();
            vm.setMonth(monthStr);
            vm.setNum(resourceUseInfoService.countByMonth(monthStr));
            list.add(vm);

            //每次减一个月
            month.add(Calendar.MONTH, -1);
        }
        return list;
    }

    /**
     * 通过率
     */
    public String getResourceUseByPassRate(HttpServletRequest request) {
        long count = resourceUseInfoService.count();
        long pass = resourceUseInfoService.countByStatus("2");

        if (pass == 0) {
            return "0%";
        }
        return BaseUtils.getPercent(pass, count);
    }
}
