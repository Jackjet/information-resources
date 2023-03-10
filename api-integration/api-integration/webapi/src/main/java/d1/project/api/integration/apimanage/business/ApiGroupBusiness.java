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

            //????????????
            getChildren(data, map);
        }

        return data;
    }

    /**
     * ????????????
     *
     * @param params ????????????
     * @return ????????????
     * @throws Exception ??????????????????
     */
    public Page<ApiGroupList> findGroupList(JSONObject params) throws Exception {
        return apiGroupListService.findAll(params);
    }

    /**
     * api????????????
     *
     * @param id ??????ID
     * @return ????????????
     * @throws Exception ??????????????????
     */
    public ApiGroupInfo apiGroupFindById(String id) throws Exception {
        return apiGroupInfoService.findById(id);
    }

    /**
     * ????????????
     *
     * @param insertVm ????????????
     * @param request  ????????????
     * @throws Exception ??????????????????
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
        iOperationLogService.insert(new OperationLog("API???", "API?????????", "?????????", "??????" + insertVm.getName() + "???", 1), request);
    }

    /**
     * ????????????
     *
     * @param id
     * @param name    ??????
     * @param request ????????????
     * @throws Exception ??????????????????
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
        iOperationLogService.insert(new OperationLog("API???", "API?????????", "?????????", "??????" + name + "???", 1), request);
    }

    /**
     * ????????????
     *
     * @param id ??????ID
     * @throws Exception ??????????????????
     */
    public void deleteGroup(String id, HttpServletRequest request) throws Exception {
        if (groupApiService.existsAllByGroupId(id)) {
            throw new Exception(Constants.API_GROUP_NOT_NULL);
        }

        if(apiGroupInfoService.existApiGroupByParentId(id)){
            throw new Exception(Constants.API_GROUP_HAS_CHILDREN);
        }

        apiGroupInfoService.deleteById(id);
        iOperationLogService.insert(new OperationLog("API???", "API?????????", "?????????", "?????????", 1), request);
    }

    /**
     * ???????????????API
     *
     * @param insertVm ????????????
     * @param request  ????????????
     * @throws Exception ??????????????????
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
        iOperationLogService.insert(new OperationLog("API???", "API?????????API", "????????????API", "???" + groupId + "????????????API", 1), request);
    }

    /**
     * ??????????????????API??????
     *
     * @param params ????????????
     * @return ????????????
     * @throws Exception ??????????????????
     */
    public Page<GroupApiList> groupApiList(JSONObject params) throws Exception {
        return groupApiListService.findAll(params);
    }

    /**
     * ????????????API????????????API
     *
     * @param ids ID??????
     */
    public void groupApiDelete(List<String> ids, HttpServletRequest request) throws Exception {
        groupApiService.deleteAll(ids);
        iOperationLogService.insert(new OperationLog("API???", "API???????????????API", "API???????????????API", "API???????????????API", 1), request);

    }


    /**
     * ??????API???????????????????????????API??????
     *
     * @param params ????????????
     * @return ????????????
     * @throws Exception ??????????????????
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
