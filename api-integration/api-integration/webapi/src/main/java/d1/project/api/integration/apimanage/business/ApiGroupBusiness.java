package d1.project.api.integration.apimanage.business;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.project.api.integration.apimanage.entity.ApiGroupInfo;
import d1.project.api.integration.apimanage.entity.GroupApi;
import d1.project.api.integration.apimanage.model.ApiGroupVm;
import d1.project.api.integration.apimanage.service.ApiGroupInfoService;
import d1.project.api.integration.apimanage.service.GroupApiService;
import d1.project.api.integration.apimanage.view.entity.ApiGroupList;
import d1.project.api.integration.apimanage.view.entity.ApiWithSourceApi;
import d1.project.api.integration.apimanage.view.entity.GroupApiList;
import d1.project.api.integration.apimanage.view.service.ApiGroupListService;
import d1.project.api.integration.apimanage.view.service.ApiWithSourceApiService;
import d1.project.api.integration.apimanage.view.service.GroupApiListService;
import d1.project.api.integration.common.Constants;
import d1.project.api.integration.common.model.OperationLog;
import d1.project.api.integration.common.service.IOperationLogService;
import d1.project.api.integration.common.utils.BaseUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author baozh
 */
@Service
public class ApiGroupBusiness {
    private final ApiGroupListService apiGroupListService;
    private final ApiGroupInfoService apiGroupInfoService;
    private final GroupApiService groupApiService;
    private final GroupApiListService groupApiListService;
    private final ApiWithSourceApiService apiWithSourceApiService;
    private final IOperationLogService iOperationLogService;

    public ApiGroupBusiness(ApiGroupListService apiGroupListService, ApiGroupInfoService apiGroupInfoService, GroupApiService groupApiService, GroupApiListService groupApiListService, ApiWithSourceApiService apiWithSourceApiService, IOperationLogService iOperationLogService) {
        this.apiGroupListService = apiGroupListService;
        this.apiGroupInfoService = apiGroupInfoService;
        this.groupApiService = groupApiService;
        this.groupApiListService = groupApiListService;
        this.apiWithSourceApiService = apiWithSourceApiService;
        this.iOperationLogService = iOperationLogService;
    }

    public JSONArray tree() {
        Sort sort = Sort.by(Sort.Direction.ASC, "createTime");
        List<ApiGroupInfo> apiGroupInfos = apiGroupInfoService.findAll(sort);

        JSONArray data = new JSONArray();
        if (apiGroupInfos.size() > 0) {
            Map<String, List<ApiGroupInfo>> map = apiGroupInfos.stream().collect(Collectors.groupingBy(ApiGroupInfo::getParentId));

            data = (JSONArray) JSON.toJSON(map.get("0"));

            //构建森林
            getChildren(data, map);
        }

        return data;
    }

    /**
     * 查询列表
     *
     * @param params 查询条件
     * @return 查询结果
     * @throws Exception 向上抛出异常
     */
    public Page<ApiGroupList> findGroupList(JSONObject params) throws Exception {
        return apiGroupListService.findAll(params);
    }

    /**
     * api分组详情
     *
     * @param id 数据ID
     * @return 查询结果
     * @throws Exception 向上抛出异常
     */
    public ApiGroupInfo apiGroupFindById(String id) throws Exception {
        return apiGroupInfoService.findById(id);
    }

    /**
     * 添加分组
     *
     * @param insertVm 添加信息
     * @param request  请求信息
     * @throws Exception 向上抛出异常
     */
    public void insertApiGroupInfo(ApiGroupInfo insertVm, HttpServletRequest request) throws Exception {
        String name = insertVm.getName();
        if (apiGroupInfoService.existsAllByName(name)) {
            throw new Exception(Constants.DATA_REPEAT);
        }
        String userId = BaseUtils.getUserId(request);
        insertVm.setCreateById(userId);
        insertVm.setCreateTime(Calendar.getInstance());
        insertVm.setUpdateById(userId);
        insertVm.setUpdateTime(Calendar.getInstance());
        insertVm.setId(BaseUtils.generate32Id());
        apiGroupInfoService.saveInfo(insertVm);
        iOperationLogService.insert(new OperationLog("API组", "API组添加", "添加组", "添加" + insertVm.getName() + "组", 1), request);
    }

    /**
     * 编辑分组
     *
     * @param id
     * @param name    组名
     * @param request 请求信息
     * @throws Exception 向上抛出异常
     */
    public void updateApiGroupInfo(String id, String name, HttpServletRequest request) throws Exception {
        if (apiGroupInfoService.existsAllByNameAndIdNot(name, id)) {
            throw new Exception(Constants.DATA_REPEAT);
        }
        String userId = BaseUtils.getUserId(request);
        ApiGroupInfo apiGroupInfo = apiGroupInfoService.findById(id);
        apiGroupInfo.setName(name);
        apiGroupInfo.setUpdateTime(Calendar.getInstance());
        apiGroupInfo.setUpdateById(userId);
        apiGroupInfoService.saveInfo(apiGroupInfo);
        iOperationLogService.insert(new OperationLog("API组", "API组编辑", "编辑组", "编辑" + name + "组", 1), request);
    }

    /**
     * 删除分组
     *
     * @param id 分组ID
     * @throws Exception 向上抛出异常
     */
    public void deleteGroup(String id, HttpServletRequest request) throws Exception {
        if (groupApiService.existsAllByGroupId(id)) {
            throw new Exception(Constants.API_GROUP_NOT_NULL);
        }

        if(apiGroupInfoService.existApiGroupByParentId(id)){
            throw new Exception(Constants.API_GROUP_HAS_CHILDREN);
        }

        apiGroupInfoService.deleteById(id);
        iOperationLogService.insert(new OperationLog("API组", "API组删除", "删除组", "删除组", 1), request);
    }

    /**
     * 给分组添加API
     *
     * @param insertVm 添加信息
     * @param request  请求信息
     * @throws Exception 向上抛出异常
     */
    public void insertGroupApi(ApiGroupVm insertVm, HttpServletRequest request) throws Exception {
        String userId = BaseUtils.getUserId(request);
        String groupId = insertVm.getGroupId();
        List<String> apiIds = insertVm.getApiIds();
        for (String apiId : apiIds) {
            GroupApi groupApi = new GroupApi();
            groupApi.setGroupId(groupId);
            groupApi.setApiId(apiId);
            groupApi.setCreateById(userId);
            groupApi.setCreateTime(Calendar.getInstance());
            groupApi.setId(BaseUtils.generate32Id());
            groupApiService.saveInfo(groupApi);
        }
        iOperationLogService.insert(new OperationLog("API组", "API组添加API", "组内添加API", "为" + groupId + "组内添加API", 1), request);
    }

    /**
     * 查询分组内的API列表
     *
     * @param params 查询条件
     * @return 查询结果
     * @throws Exception 向上抛出异常
     */
    public Page<GroupApiList> groupApiList(JSONObject params) throws Exception {
        return groupApiListService.findAll(params);
    }

    /**
     * 批量删除API分组下的API
     *
     * @param ids ID集合
     */
    public void groupApiDelete(List<String> ids, HttpServletRequest request) throws Exception {
        groupApiService.deleteAll(ids);
        iOperationLogService.insert(new OperationLog("API组", "API组批量删除API", "API组批量删除API", "API组批量删除API", 1), request);

    }


    /**
     * 获取API分组中不在该分组的API列表
     *
     * @param params 查询条件
     * @return 查询结果
     * @throws Exception 向上抛出异常
     */
    public Page<ApiWithSourceApi> apiList(JSONObject params) throws Exception {
        String groupId = params.getString("groupId");
        params.remove("groupId");
        List<GroupApi> list = groupApiService.findAll(groupId);
        if (list.size() > 0) {
            List<String> ids = new ArrayList<>(list.size());
            for (GroupApi groupApi : list) {
                ids.add(groupApi.getApiId());
            }
            params.put("id", ids);
        }
        return apiWithSourceApiService.findByIdIn(params);
    }

    private void getChildren(JSONArray array, Map<String, List<ApiGroupInfo>> map) {
        for (Object obj : array) {
            JSONObject item = (JSONObject) obj;

            List<ApiGroupInfo> children = map.get(item.get("id"));

            if (children != null) {
                item.put("children", JSON.toJSON(children));
                getChildren((JSONArray) item.get("children"), map);
            }
        }
    }
}
