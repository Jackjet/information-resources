package d1.project.api.integration.apimanage.business;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.project.api.integration.apimanage.entity.*;
import d1.project.api.integration.apimanage.model.ApiAuthInsertVm;
import d1.project.api.integration.apimanage.model.GroupAuthInsertVm;
import d1.project.api.integration.apimanage.service.*;
import d1.project.api.integration.apimanage.view.entity.*;
import d1.project.api.integration.apimanage.view.service.*;
import d1.project.api.integration.application.entity.Application;
import d1.project.api.integration.application.entity.NormalConsumer;
import d1.project.api.integration.application.service.ApplicationService;
import d1.project.api.integration.application.service.NormalConsumerService;
import d1.project.api.integration.common.Constants;
import d1.project.api.integration.common.model.OperationLog;
import d1.project.api.integration.common.service.IOperationLogService;
import d1.project.api.integration.common.utils.BaseUtils;
import d1.project.api.integration.kong.business.KongBusiness;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author baozh
 */
@Service
public class ApiAuthManageBusiness {

    private final ApiWithSourceApiService apiWithSourceApiService;
    private final GroupApiListService groupApiListService;
    private final ApiGroupListService apiGroupListService;
    private final AuthAppListService authAppListService;
    private final ApiAuthManageService apiAuthManageService;
    private final ApiAuthAppService apiAuthAppService;
    private final ApplicationService applicationService;
    private final KongBusiness kongBusiness;
    private final ApiBaseInfoService apiBaseInfoService;
    private final IOperationLogService iOperationLogService;
    private final KongApiService kongApiService;
    private final MetaService metaService;
    private final ApiGroupInfoService apiGroupInfoService;
    private final NormalConsumerService normalConsumerService;
    private final KongApiViewService kongApiViewService;

    public ApiAuthManageBusiness(ApiWithSourceApiService apiWithSourceApiService, GroupApiListService groupApiListService, ApiGroupListService apiGroupListService, AuthAppListService authAppListService, ApiAuthManageService apiAuthManageService, ApiAuthAppService apiAuthAppService, ApplicationService applicationService, KongBusiness kongBusiness, ApiBaseInfoService apiBaseInfoService, IOperationLogService iOperationLogService, KongApiService kongApiService, MetaService metaService, ApiGroupInfoService apiGroupInfoService, NormalConsumerService normalConsumerService, KongApiViewService kongApiViewService) {
        this.apiWithSourceApiService = apiWithSourceApiService;
        this.groupApiListService = groupApiListService;
        this.apiGroupListService = apiGroupListService;
        this.authAppListService = authAppListService;
        this.apiAuthManageService = apiAuthManageService;
        this.apiAuthAppService = apiAuthAppService;
        this.applicationService = applicationService;
        this.kongBusiness = kongBusiness;
        this.apiBaseInfoService = apiBaseInfoService;
        this.iOperationLogService = iOperationLogService;
        this.kongApiService = kongApiService;
        this.metaService = metaService;
        this.apiGroupInfoService = apiGroupInfoService;
        this.normalConsumerService = normalConsumerService;
        this.kongApiViewService = kongApiViewService;
    }

    /**
     * API列表
     *
     * @param params 查询条件
     * @return 查询结果
     * @throws Exception 异常处理
     */
    public JSONObject findApiList(JSONObject params) throws Exception {
        //查询已发布API
        params.put("status", Constants.API_STATUS_RELEASE);

        Page<ApiWithSourceApi> data = apiWithSourceApiService.findAll(params);
        JSONObject object = (JSONObject) JSON.toJSON(data);
        return object;
    }

    /**
     * 查询分组内的API列表
     *
     * @param params 查询条件
     * @return 查询结果
     * @throws Exception 向上抛出异常
     */
    public JSONArray findGroupList(JSONObject params) throws Exception {
        String name = params.getString("name");
        Sort sort = Sort.by(Sort.Direction.ASC, "createTime");
        List<ApiGroupInfo> apiGroupInfos = apiGroupInfoService.findAll(sort);

        JSONArray data = new JSONArray();
        if (apiGroupInfos.size() > 0) {
            Map<String, List<ApiGroupInfo>> map = apiGroupInfos.stream().collect(Collectors.groupingBy(ApiGroupInfo::getParentId));

            data = (JSONArray) JSON.toJSON(map.get("0"));

            //构建森林
            getChildren(data, map);

            //移除不符合条件的节点
            if (!StringUtils.isEmpty(name)) {
                removeChildByNameNotContains(data, name);
            }
        }

        return data;
    }

    /**
     * 查询应用列表
     *
     * @param params 查询条件
     * @return 查询结果
     * @throws Exception 向上抛出异常
     */
    public Page<AuthAppList> findAppList(JSONObject params) throws Exception {
        return authAppListService.findAll(params);
    }

    /**
     * API授权给应用
     *
     * @param insertVm 授权信息
     * @param request  请求信息
     * @throws Exception 向上抛出异常
     */
    public void insertAuth(ApiAuthInsertVm insertVm, HttpServletRequest request) throws Exception {
        List<KongApi> kongApis = this.kongApiService.findAllByApiIds(insertVm.getApiIdList());
        Map<String, KongApi> kongApiMap = kongApis.stream().collect(Collectors.toMap(KongApi::getApiId, Function.identity()));

        List<NormalConsumer> consumers = this.normalConsumerService.findAllByAppid(insertVm.getAppIdList());
        Map<String, NormalConsumer> consumerMap = consumers.stream().collect(Collectors.toMap(NormalConsumer::getAppid, Function.identity()));

        List<ApiAuthManage> apiAuthManages = new Vector<>();

        StringBuilder errorBuilder = new StringBuilder();
        for (String apiId : insertVm.getApiIdList()) {
            KongApi kongApi = kongApiMap.get(apiId);

            String groups = "";
            for (String appId : insertVm.getAppIdList()) {
                //设置ip限制
                NormalConsumer consumer = consumerMap.get(appId);
                kongBusiness.setIpWhiteListOrBlackList(insertVm.getContainer(), consumer, insertVm.getListType(), insertVm.getListContent());

                groups += consumer.getGroups();
                groups += ",";
            }

            //route允许通过的组
            if(!StringUtils.isEmpty(groups)) {
                groups = groups.substring(0,groups.lastIndexOf(","));
            }

            kongApi.setAclGroup(groups);

            for (String appId : insertVm.getAppIdList()) {
                try {
                    kongBusiness.setPermission(insertVm.getContainer(), kongApi);

                    ApiAuthManage apiAuthManage = this.apiAuthManageService.findFirstByApiIdAndAppId(apiId, appId);

                    if (apiAuthManage == null) {
                        apiAuthManage = new ApiAuthManage();
                        apiAuthManage.setId(BaseUtils.generate32Id());
                    }

                    apiAuthManage.setApiId(apiId);
                    apiAuthManage.setAppId(appId);
                    apiAuthManage.setListType(insertVm.getListType());
                    apiAuthManage.setListContent(insertVm.getListContent());

                    BaseUtils.setCreateTimeAndCreateById(request,apiAuthManage);
                    apiAuthManages.add(apiAuthManage);
                } catch (Exception e) {
                    ApiBaseInfo apiBaseInfo = apiBaseInfoService.findById(apiId);

                    errorBuilder.append(apiBaseInfo.getName());
                    errorBuilder.append(",");
                }
            }
        }

        apiAuthManageService.batchSave(apiAuthManages);

        String errorStr = errorBuilder.toString();
        if (!StringUtils.isEmpty(errorStr)) {
            errorStr = "下列API授权失败:" + errorStr.substring(0, errorStr.lastIndexOf(","));
            throw new Exception(errorStr);
        }
        iOperationLogService.insert(new OperationLog("API授权", "添加授权", "新增授权", "添加API授权", 1), request);
    }

    /**
     * 查询API下已授权的APP列表
     *
     * @param params 查询条件
     * @return 查询结果
     * @throws Exception 异常处理
     */
    public Page<ApiAuthApp> findApiAuthAppList(JSONObject params) throws Exception {
        String apiId = params.getString("apiId");
        if (!apiAuthManageService.existsAllByApiId(apiId)) {
            throw new Exception(Constants.API_NO_AUTH);
        }
        return apiAuthAppService.findAll(params);
    }

    /**
     * 批量删除授权
     *
     * @param ids ID集合
     */
    @Transactional(rollbackFor = Exception.class)
    public List<KongApiView> authDelete(List<String> ids, HttpServletRequest request) throws Exception {
        List<ApiAuthManage> auths = apiAuthManageService.findAllByIds(ids);
        List<String> apiIds = auths.stream().map(ApiAuthManage:: getApiId).collect(Collectors.toList());
        List<KongApiView> kongApis = kongApiViewService.findAllByApiIds(apiIds);

        apiAuthManageService.deleteAll(ids);
        iOperationLogService.insert(new OperationLog("API授权", "删除授权", "批量删除授权", "批量删除授权", 1), request);
        return kongApis;
    }

    public boolean getAuthNum(String addIds) {
        String[] array= addIds.split(",");

        List<String> appIdList = new Vector<>();
        for(String item : array){
            appIdList.add(item);
        }
        return apiAuthManageService.existsAllByAppId(appIdList);
    }

    public void insertGroupAuth(GroupAuthInsertVm param, HttpServletRequest request) throws Exception {
        //查询组内api id
        List<ApiBaseInfo> apiBaseInfos = apiBaseInfoService.findAllByGroupId(param.getGroupId());
        List<String> apiIds = apiBaseInfos.stream().map(ApiBaseInfo::getId).collect(Collectors.toList());

        param.setApiIdList(apiIds);

        //授权
        this.insertAuth(param, request);
    }


    /**
     * 保存kongApi
     * @param kongApis
     */
    public void saveKongApis(List<KongApi> kongApis){
        kongApiService.batchSave(kongApis);
    }

    /**
     * 查询分组内的API列表
     *
     * @param params 查询条件
     * @return 查询结果
     * @throws Exception 向上抛出异常
     */
    public Page<GroupApiList> findGroupApiList(JSONObject params) throws Exception {
        return groupApiListService.findAll(params);
    }
    //===========================================
    private void getChildren(JSONArray array, Map<String, List<ApiGroupInfo>> map) {
        for (Object obj : array) {
            JSONObject item = (JSONObject) obj;
            item.put("updateTime",item.getDate("updateTime"));

            int count = apiBaseInfoService.countByGroupId(item.getString("id"));
            item.put("count",count);

            List<ApiGroupInfo> children = map.get(item.get("id"));

            if (children != null) {
                item.put("children", JSON.toJSON(children));
                getChildren((JSONArray) item.get("children"), map);
            }
        }
    }

    /**
     * 删除不符合条件的节点
     *
     * @param array 森林
     * @param name  条件
     */
    private void removeChildByNameNotContains(JSONArray array, String name) {
        int size = array.size();
        for (int i = 0; i < size; i++) {
            JSONObject item = (JSONObject) array.get(i);
            JSONArray children = item.getJSONArray("children");

            //存在子节点，向下遍历
            if (children != null) {
                removeChildByNameNotContains(children, name);
                //检查并移除不符合条件的子节点后，子节点数量为0，置空
                if (children.size() == 0) {
                    children = null;
                }
            }

            //没有子节点，且自身不符合条件，移除
            if (children == null && !item.getString("name").contains(name)) {
                array.remove(item);
                //数组长度减1，由于当前元素移除，后面的元素自动补充占位，所以位标向后移动一位，供下次循环重新检查当前位标数据
                size--;
                i--;
            }
        }
    }
}
