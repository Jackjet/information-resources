package d1.project.api.integration.external.business;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.project.api.integration.apimanage.entity.*;
import d1.project.api.integration.apimanage.model.SourceApiVm;
import d1.project.api.integration.application.entity.Application;
import d1.project.api.integration.application.entity.NormalConsumer;
import d1.project.api.integration.common.Constants;
import d1.project.api.integration.common.service.*;
import d1.project.api.integration.common.utils.BaseUtils;
import d1.project.api.integration.external.entity.ApplicationUser;
import d1.project.api.integration.external.model.ApiExtInsertPostVm;
import d1.project.api.integration.external.service.ApplicationUserService;
import d1.project.api.integration.kong.business.KongBusiness;
import d1.project.api.integration.kong.entity.KongLogDir;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author libin
 */
@Service
public class ApiExtBusiness {
    private final IApiService iApiService;
    private final IApiLogRecordService iApiLogRecordService;
    private final IKongLogDirService iKongLogDirService;
    private final ISourceApiService iSourceApiService;
    private final KongBusiness kongBusiness;
    private final IKongSourceApiService iKongSourceApiService;
    private final IKongApiService iKongApiService;
    private final IApiAuthManageService iApiAuthManageService;
    private final IApplicationService iApplicationService;
    private final INormalConsumerService iNormalConsumerService;
    private final ApplicationUserService applicationUserService;

    public ApiExtBusiness(IApiService iApiService, IApiLogRecordService iApiLogRecordService, IKongLogDirService iKongLogDirService, ISourceApiService iSourceApiService, KongBusiness kongBusiness, IKongSourceApiService iKongSourceApiService, IKongApiService iKongApiService, IApiAuthManageService iApiAuthManageService, IApplicationService iApplicationService, INormalConsumerService iNormalConsumerService, ApplicationUserService applicationUserService) {
        this.iApiService = iApiService;
        this.iApiLogRecordService = iApiLogRecordService;
        this.iKongLogDirService = iKongLogDirService;
        this.iSourceApiService = iSourceApiService;
        this.kongBusiness = kongBusiness;
        this.iKongSourceApiService = iKongSourceApiService;
        this.iKongApiService = iKongApiService;
        this.iApiAuthManageService = iApiAuthManageService;
        this.iApplicationService = iApplicationService;
        this.iNormalConsumerService = iNormalConsumerService;
        this.applicationUserService = applicationUserService;
    }

    public JSONObject findAll(JSONObject param) throws Exception {
        Page<ApiBaseInfo> data = iApiService.findAllByContainer(param);
        JSONObject result = (JSONObject) JSON.toJSON(data);
        JSONArray content = result.getJSONArray("content");

        for (Object obj : content) {
            JSONObject item = (JSONObject) obj;
            item.put("total", iApiLogRecordService.countAll());
            item.put("success", iApiLogRecordService.countByStatus(1));
            item.put("fail", iApiLogRecordService.countByStatus(0));
        }

        return result;
    }

    public JSONObject statistical(String container) {
        List<ApiBaseInfo> apiBaseInfos = iApiService.findAllByContainer(container);
        Map<String, List<ApiBaseInfo>> map = apiBaseInfos.stream().collect(Collectors.groupingBy(ApiBaseInfo::getMethods));

        JSONObject result = new JSONObject();
        result.put("total", apiBaseInfos.size());

        int getSize = 0;
        List<ApiBaseInfo> gets = map.get("GET");
        if (gets != null) {
            getSize = gets.size();
        }

        int postSize = 0;
        List<ApiBaseInfo> posts = map.get("POST");
        if (posts != null) {
            postSize = posts.size();
        }


        int putSize = 0;
        List<ApiBaseInfo> puts = map.get("PUT");
        if (puts != null) {
            putSize = puts.size();
        }

        int deleteSize = 0;
        List<ApiBaseInfo> deletes = map.get("DELETE");
        if (deletes != null) {
            deleteSize = deletes.size();
        }

        result.put("get", getSize);
        result.put("post", postSize);
        result.put("put", putSize);
        result.put("delete", deleteSize);

        double rate = 0.0;
        int total = iApiLogRecordService.countAll();
        if (total > 0) {
            int success = iApiLogRecordService.countByStatus(1);
            rate = success * 1.0 / total;
        }
        result.put("rate", rate);
        return result;
    }

    public List<KongLogDir> log(JSONObject param) throws Exception {
        return iKongLogDirService.findAll(param);
    }


    @Transactional(rollbackOn = Exception.class)
    public SourceApiVm insert(ApiExtInsertPostVm param, String container, String verificationMode) throws Exception {
        ApiBaseInfo apiBaseInfo = insertApi(param, container, verificationMode);

        SourceApi sourceApi = insertSourceApi(param.getSourceApi(), apiBaseInfo.getId());
        KongApi kongApi = publish(apiBaseInfo, sourceApi, container);

        NormalConsumer consumer = insertConsumer(param.getUserId(), param.getUsername(), container);
        insertAuth(kongApi, consumer, container);

        SourceApiVm vm = new SourceApiVm();
        BeanUtils.copyProperties(sourceApi, vm);
        vm.setContainer(apiBaseInfo.getContainer());
        vm.setRouteInfo(apiBaseInfo.getRouteInfo());
        vm.setKongApiId(kongApi.getApiId());
        return vm;
    }

    //=======================================================================
    private void checkApiLimitNum(ApiExtInsertPostVm info) throws Exception {
        long minute = info.getPerMinute();
        long hour = info.getEveryHour();
        long day = info.getEveryDay();
        if (minute > hour) {
            throw new Exception(Constants.HOURS_ERROR);
        }
        if (hour > day) {
            throw new Exception(Constants.DAY_ERROR);
        }
    }

    private ApiBaseInfo insertApi(ApiExtInsertPostVm param, String container, String verificationMode) throws Exception {
        String apiName = param.getUsername() + "_" + param.getSourceApi().getName();
        if (iApiService.existsAllByName(apiName)) {
            return iApiService.findFirstByName(apiName);
        }

        checkApiLimitNum(param);

        ApiBaseInfo apiBaseInfo = new ApiBaseInfo();
        BeanUtils.copyProperties(param, apiBaseInfo);
        apiBaseInfo.setName(apiName);
        //新增资源目录ID，防止多个目录挂同一个接口出现router相同
        apiBaseInfo.setRouteInfo("/" + param.getUsername() + "/" + param.getUviewId() + param.getSourceApi().getPath());
        apiBaseInfo.setVerificationMode(verificationMode);
        apiBaseInfo.setCreateTime(Calendar.getInstance());
        apiBaseInfo.setUpdateTime(Calendar.getInstance());
        apiBaseInfo.setId(BaseUtils.generate32Id());
        apiBaseInfo.setMethods(param.getSourceApi().getMethod());

        apiBaseInfo.setStatus(Constants.API_STATUS_RELEASE);
        apiBaseInfo.setContainer(container);
        iApiService.saveInfo(apiBaseInfo);

        return apiBaseInfo;
    }

    private KongApi publish(ApiBaseInfo apiBaseInfo, SourceApi sourceApi, String container) throws Exception {
        KongSourceApi kongSourceApi = iKongSourceApiService.findByApiId(sourceApi.getId());
        if (kongSourceApi == null) {
            kongSourceApi = new KongSourceApi();
            kongSourceApi.setId(BaseUtils.generate32Id());
            kongSourceApi.setSourceId(sourceApi.getId());
        }

        BeanUtils.copyProperties(sourceApi, kongSourceApi);

        String[] host = sourceApi.getHost().split(",");
        JSONArray array = new JSONArray();
        for (String item : host) {
            JSONObject obj = new JSONObject();
            obj.put("target", item);

            array.add(obj);
        }

        kongSourceApi.setHost(array.toJSONString());

        kongBusiness.saveSourceApi(container, kongSourceApi);

        iKongSourceApiService.save(kongSourceApi);

        //kong添加route和插件
        KongApi kongApi = iKongApiService.findByApiId(apiBaseInfo.getId());
        if (kongApi == null) {
            kongApi = new KongApi();
            kongApi.setId(BaseUtils.generate32Id());
        }

        BeanUtils.copyProperties(apiBaseInfo, kongApi);

        kongApi.setServiceId(kongSourceApi.getServiceId());

        String method = sourceApi.getMethod();
        if (StringUtils.isEmpty(method)) {
            method = "OPTIONS";
        } else if (!method.contains("OPTIONS")) {
            method = method + ",OPTIONS";
        }
        kongApi.setMethods(method);
        kongApi.setApiId(apiBaseInfo.getId());
        //保存kong配置
        kongBusiness.saveApi(apiBaseInfo.getContainer(), kongApi);

        iKongApiService.save(kongApi);

        return kongApi;
    }

    private SourceApi insertSourceApi(SourceApi param, String apiId) {
        SourceApi sourceApi = new SourceApi();
        SourceApi entity = iSourceApiService.findFirstByApiId(apiId);
        if (entity != null) {
            return entity;
        }
        BeanUtils.copyProperties(param, sourceApi);

        sourceApi.setId(BaseUtils.generate32Id());
        sourceApi.setApiId(apiId);
        iSourceApiService.save(sourceApi);

        return sourceApi;
    }

    private NormalConsumer insertConsumer(String userId, String userName, String container) throws Exception {
        ApplicationUser applicationUser = applicationUserService.findFirstByUserId(userId);
        NormalConsumer consumer;
        if (applicationUser == null) {
            Application application = new Application();
            application.setId(BaseUtils.generate32Id());
            application.setName(userName);
            application.setKey(BaseUtils.generate32Id());
            application.setCreateTime(Calendar.getInstance());
            application.setContainer(container);

            iApplicationService.save(application);

            applicationUser = new ApplicationUser();
            applicationUser.setAppid(application.getId());
            applicationUser.setUserId(userId);
            applicationUser.setId(BaseUtils.generate32Id());

            applicationUserService.save(applicationUser);

            consumer = new NormalConsumer();
            consumer.setId(BaseUtils.generate32Id());
            consumer.setUsername(application.getId());
            consumer.setKey(application.getKey());
            consumer.setContainer(container);
            consumer.setGroups(application.getId());
            consumer.setAppid(application.getId());

            iNormalConsumerService.save(consumer);

            kongBusiness.addConsumer(container, consumer);
        } else {
            consumer = iNormalConsumerService.findByAppid(applicationUser.getAppid());
        }

        return consumer;
    }

    private void insertAuth(KongApi kongApi, NormalConsumer kongConsumer, String container) throws Exception {
        kongApi.setAclGroup(kongConsumer.getGroups());

        kongBusiness.setPermission(container, kongApi);

        ApiAuthManage apiAuthManage = this.iApiAuthManageService.findFirstByApiIdAndAppId(kongApi.getApiId(), kongConsumer.getAppid());

        if (apiAuthManage == null) {
            apiAuthManage = new ApiAuthManage();
            apiAuthManage.setId(BaseUtils.generate32Id());
        }

        apiAuthManage.setApiId(kongApi.getApiId());
        apiAuthManage.setAppId(kongConsumer.getAppid());
        apiAuthManage.setCreateTime(Calendar.getInstance());
        iApiAuthManageService.saveInfo(apiAuthManage);
    }
}
