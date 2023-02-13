package d1.project.d1project.apidesign.business;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.project.d1project.apidesign.entity.ApiDeployment;
import d1.project.d1project.apidesign.entity.ApiDesign;
import d1.project.d1project.apidesign.entity.ApiDevelopment;
import d1.project.d1project.apidesign.entity.ApiTestCase;
import d1.project.d1project.apidesign.model.*;
import d1.project.d1project.apidesign.service.*;
import d1.project.d1project.apidesign.utils.SqlUtils;
import d1.project.d1project.apidesign.utils.VueToContainer;
import d1.project.d1project.common.Constants;
import d1.project.d1project.common.model.OperationLog;
import d1.project.d1project.common.service.IOperationLogService;
import d1.project.d1project.common.utils.BaseUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * API的设计开发部署
 *
 * @author baozh
 */
@Service
public class ApiDesignBusiness {
    private final ApiDesignService apiDesignService;
    private final ApiDevelopmentService apiDevelopmentService;
    private final ApiTestCaseService apiTestCaseService;
    private final IOperationLogService iOperationLogService;
    private final ApiDeploymentService apiDeploymentService;
    private final WebAdminUserService webAdminUserService;

    @Value("${d1.project.container.delete.api}")
    private String containerDeleteApi;
    @Value("${d1.project.container.run.api}")
    private String containerRunApi;

    public ApiDesignBusiness(ApiDesignService apiDesignService, ApiDevelopmentService apiDevelopmentService,
                             ApiTestCaseService apiTestCaseService, IOperationLogService iOperationLogService,
                             ApiDeploymentService apiDeploymentService, WebAdminUserService webAdminUserService) {
        this.apiDesignService = apiDesignService;
        this.apiDevelopmentService = apiDevelopmentService;
        this.apiTestCaseService = apiTestCaseService;
        this.iOperationLogService = iOperationLogService;
        this.apiDeploymentService = apiDeploymentService;
        this.webAdminUserService = webAdminUserService;
    }

    public Page<ApiDesign> findAllApiDesign(JSONObject params) throws Exception {
        return apiDesignService.findAll(params);
    }

    public ApiDesign findApiDesign(String id) throws DoValidException {
        return apiDesignService.findById(id);
    }

    /**
     * 添加API设计信息
     *
     * @param apiDesign api设计信息
     * @param request     请求信息
     * @throws Exception 向上抛出异常
     */
    public String insertApiDesign(ApiDesign apiDesign, HttpServletRequest request) throws Exception {
        String name = apiDesign.getName();
        String requestUrl = apiDesign.getRequestUrl();
        if (apiDesignService.existsAllByName(name)) {
            throw new Exception(Constants.DATA_REPEAT);
        }
        if (apiDesignService.existsAllByRequestUrl(requestUrl)) {
            throw new Exception(Constants.DATA_REQUEST_URL);
        }
        apiDesign.setId(BaseUtils.generate32Id());
        apiDesign.setStatus("0");
        webAdminUserService.updateCreateIdAndTime(request, apiDesign);
        webAdminUserService.updateUpdateIdAndTime(request, apiDesign);
        apiDesignService.insert(apiDesign);
        iOperationLogService.insert(new OperationLog("API设计", "API添加", "API添加", "添加API" + apiDesign.getName(), 1), request);
        return apiDesign.getId();
    }

    /**
     * 编辑API设计信息
     *
     * @param apiDesign API编辑信息
     * @param request     请求信息
     * @throws Exception 向上抛出异常
     */
    public String updateApiDesign(ApiDesign apiDesign, HttpServletRequest request) throws Exception {
        String id = apiDesign.getId();
        String name = apiDesign.getName();
        String requestUrl = apiDesign.getRequestUrl();
        if (apiDesignService.existsAllByNameAndIdNot(name, id)) {
            throw new Exception(Constants.DATA_REPEAT);
        }
        if (apiDesignService.existsAllByRequestUrlAndIdNot(requestUrl, id)) {
            throw new Exception(Constants.DATA_REQUEST_URL);
        }
        webAdminUserService.updateUpdateIdAndTime(request, apiDesign);
        apiDesignService.insert(apiDesign);
        iOperationLogService.insert(new OperationLog("API设计", "API编辑", "API编辑", "编辑API" + apiDesign.getName(), 1), request);
        return apiDesign.getId();
    }

    /**
     * 删除API设计
     *
     * @param id api设计ID
     */
    public void deleteApiDesign(String id, HttpServletRequest request) throws Exception {
        apiDevelopmentService.deleteAll(id);
        ApiDesign apiDesign = apiDesignService.findById(id);
        apiDesignService.deleteById(id);
        iOperationLogService.insert(new OperationLog("API设计", "API删除", "API删除", "删除API" + apiDesign.getName(), 1), request);
    }

    /**
     * 查询API开发详情
     *
     * @param id id
     * @return 查询结果
     */
    public ApiDevelopment findApiDevelopment(String id) {
        return apiDevelopmentService.findById(id);
    }


    public RequestMetaDataVm findRequestMetaData(String id) {
        ApiDesign apiDesign = apiDesignService.findById(id);
        if (apiDesign == null) {
            return null;
        }

        RequestMetaDataVm requestMetaDataVm = new RequestMetaDataVm();
        requestMetaDataVm.setPayload(new ArrayList<>());

        //这个里面包含Header 和 Parameter，通过 type区分
        List<RequestMetaDataItemVm> params = JSON.parseArray(apiDesign.getParams(), RequestMetaDataItemVm.class);
        for (RequestMetaDataItemVm vm : params) {
            //这个type设计不合理，不应该把Header 和 Parameter放到一个字段里面
            vm.setFrom(vm.getType());
            requestMetaDataVm.getPayload().add(vm);
            //修改type属性
            vm.setType("String");
        }


        //设置body，根据 formatType
        String formatType = apiDesign.getFormatType();

        //这种内容格式参数有类型，不能随意输入任何值
        if ("multipart/form-data".equals(formatType)) {
            requestMetaDataVm.setPayload(JSON.parseArray(apiDesign.getBody(), RequestMetaDataItemVm.class));

        } else if ("application/json".equals(formatType)) { //这种内容格式，参数可能为jsonObject 或者 jsonArray 或者String
            Object obj = JSON.parse(apiDesign.getBody());
            Object body = obj;
            if (obj instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) obj;
                if (jsonArray.size() > 0) {
                    body = jsonArray.get(0);
                }
            }

            if (body instanceof JSONObject) {
                Set<String> keySet = ((JSONObject) body).keySet();
                for (String key : keySet) {
                    requestMetaDataVm.getPayload().add(new RequestMetaDataItemVm(key, "String", "Body"));
                }
            }
        }
        return requestMetaDataVm;
    }

    public String generateSql(GenerateSqlVm model) throws DoValidException {
        return SqlUtils.generateSql(model.getDatabaseType(), model.getTableName(), model.getFields(), model.getWheres());
    }


    /**
     * 添加开发信息
     *
     * @param insertVm 开发信息
     * @param request  请求
     * @throws Exception 异常处理
     */
    public void insertApiDevelopment(ApiDevelopment insertVm, HttpServletRequest request) throws Exception {
        String id = insertVm.getApiDesignId();
        if (StringUtils.isEmpty(id)) {
            insertVm.setId(insertVm.getApiDesignId());
        }
        insertVm.setId(id);
        webAdminUserService.updateCreateIdAndTime(request, insertVm);
        webAdminUserService.updateUpdateIdAndTime(request, insertVm);
        apiDevelopmentService.insert(insertVm);
        ApiDesign apiDesign = apiDesignService.findById(id);
        iOperationLogService.insert(new OperationLog("API编排", "API编排", "API编排", "编排API" + apiDesign.getName(), 1), request);
    }

    /**
     * 导出测试用例JSON文件
     *
     * @param id 数据ID
     * @throws Exception 抛出异常
     */
    public void exportCase(String id, HttpServletResponse response) throws Exception {
        ApiTestCase apiTestCase = apiTestCaseService.findById(id);
        String apiId = apiTestCase.getApiId();
        ApiDesign apiDesign = apiDesignService.findById(apiId);
        String apiName = apiDesign.getName();
        BaseUtils.exportText(apiName, apiTestCase.getIp(), apiTestCase.getMethod(), apiTestCase.getContent(), response);
    }

    /**
     * 查询已开发过的API
     *
     * @param params 查询条件
     * @return 查询结果
     * @throws Exception 异常处理
     */
    public Page<ApiDesign> findDesignedList(JSONObject params) throws Exception {
        return apiDesignService.findAll(params);
    }

    /**
     * 查询已部署的容器信息
     *
     * @param deployApiVm api以及容器ID
     * @param request     请求信息
     * @throws Exception 异常处理
     */
    public String deployApi(DeployApiVm deployApiVm, HttpServletRequest request) throws Exception {
        String apiId = deployApiVm.getApiId();
        String containerId = deployApiVm.getContainerId();
        String name = deployApiVm.getName();
        String url = deployApiVm.getUrl();
        ApiDesign apiDesign = apiDesignService.findById(apiId);
        if (apiDesign == null) {
            throw new Exception(Constants.DATA_NULL);
        }
        ApiDevelopment apiDevelopment = apiDevelopmentService.findById(apiId);
        if (apiDevelopment == null) {
            throw new Exception(Constants.API_DEVELOPMENT_NULL);
        }
        ApiDeployment apiDeployment = apiDeploymentService.findFirstByApiDesignIdAndContainerId(apiId, containerId);
        if (apiDeployment == null) {
            apiDeployment = new ApiDeployment();
            apiDeployment.setApiDesignId(apiId);
            apiDeployment.setContainerId(containerId);
            apiDeployment.setName(name);
            apiDeployment.setUrl(url);
            apiDeployment.setStatus("0");
            apiDeployment.setId(BaseUtils.generate32Id());
            webAdminUserService.updateCreateIdAndTime(request, apiDeployment);
        } else {
            removeContainer(url, apiId);
        }
        String vueFile = apiDevelopment.getFile();
        JSONObject containerFile = VueToContainer.getContainerFile(vueFile, apiId, apiDesign.getName(), apiDesign.getRequestUrl(), apiDesign.getMethod(), apiDesign.getFormatType());
        runContainer(url, containerFile);
        apiDeployment.setStatus("1");
        apiDeployment.setDeploymentFile(containerFile.toJSONString());
        apiDeploymentService.insert(apiDeployment);
        apiDesign.setStatus("2");
        apiDesignService.insert(apiDesign);
        iOperationLogService.insert(new OperationLog("API部署", "API部署", "API部署", "部署API" + apiDesign.getName(), 1), request);
        return containerFile.toJSONString();
    }

    /**
     * 获取API设计测试用信息
     *
     * @param id 数据ID
     * @return 查询结果
     * @throws Exception 查询异常处理
     */
    public List<ApiDeployment> findTestList(String id) throws Exception {
        List<ApiDeployment> list = apiDeploymentService.findAllApiDeploymentList(id);
        if (list == null || list.size() == 0) {
            throw new Exception(Constants.HOST_NULL);
        }
        return list;
    }

    /**
     * 撤销部署
     *
     * @param id 数据ID
     */
    public void deleteDeploy(String id, HttpServletRequest request) throws Exception {
        ApiDeployment apiDeployment = apiDeploymentService.findById(id);
        if (apiDeployment == null) {
            throw new Exception(Constants.DATA_NULL);
        }
        removeContainer(apiDeployment.getContainerId(), apiDeployment.getApiDesignId());
        String apiId = apiDeployment.getApiDesignId();
        apiDeploymentService.delete(id);
        if (!apiDeploymentService.existsAllByApiDesignId(apiId)) {
            ApiDesign apiDesign = apiDesignService.findById(apiId);
            apiDesign.setStatus("1");
            apiDesignService.insert(apiDesign);
            iOperationLogService.insert(new OperationLog("API部署", "API部署", "撤销部署", "撤销API" + apiDesign.getName(), 1), request);
        }
    }

    /**
     * 撤销容器上部署的API
     *
     * @param containerUrl 容器ID
     * @param apiId        API ID
     * @throws Exception 异常处理
     */
    private void removeContainer(String containerUrl, String apiId) throws Exception {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://" + containerUrl + containerDeleteApi;
            String relUrl = url + "?id=" + apiId;
            ResponseEntity<JSONObject> result = restTemplate.exchange(relUrl, HttpMethod.DELETE, null, JSONObject.class);
            JSONObject body = result.getBody();
            if (body != null) {
                String resCode = body.getString("code");
                String error = "0";
                if (error.equals(resCode)) {
                    throw new Exception(body.getString("msg"));
                }
            }
        } catch (RestClientException e) {
            throw new Exception(Constants.CONTAINER_NET_ERROR_);
        }

    }

    private void runContainer(String containerUrl, JSONObject data) throws Exception {
        String url = "http://" + containerUrl + containerRunApi;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> res = restTemplate.postForEntity(url, data, JSONObject.class);
        JSONObject json = res.getBody();
        if (json != null) {
            String code = json.getString("code");
            String fail = "0";
            if (fail.equals(code)) {
                throw new Exception(json.getString("msg"));
            }
        }
    }
}
