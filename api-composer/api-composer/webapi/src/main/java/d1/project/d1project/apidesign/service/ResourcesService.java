package d1.project.d1project.apidesign.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.project.d1project.apidesign.dao.ApiDeploymentDao;
import d1.project.d1project.apidesign.dao.ApiDesignDao;
import d1.project.d1project.apidesign.entity.ApiDeployment;
import d1.project.d1project.apidesign.entity.ApiDesign;
import d1.project.d1project.apidesign.model.ApiDesignInsert;
import d1.project.d1project.apidesign.model.DesignInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 数据源
 *
 * @author zhengyang
 */
@Service
public class ResourcesService {
    private static final String metaPath = "/webadmin/tagInfo/findAll";
    private static final String dataSourcePath = "/webadmin/dataSourceInfo/findAll";
    private static final String containerPath = "/webadmin/containerInfo/findAll";
    private static final String apiDesignPath = "/webadmin/sourceApi/apiDesignInsert";

    @Value("${d1.project.resourcesRoot}")
    private String resourcesRoot;

    private final ApiDesignDao apiDesignDao;
    private final ApiDeploymentDao apiDeploymentDao;
    private final WebAdminUserService webAdminUserService;

    public ResourcesService(ApiDesignDao apiDesignDao, ApiDeploymentDao apiDeploymentDao, WebAdminUserService webAdminUserService) {
        this.apiDesignDao = apiDesignDao;
        this.apiDeploymentDao = apiDeploymentDao;
        this.webAdminUserService = webAdminUserService;
    }

    public JSONObject findAllMetas(String name) throws Exception {
        String url = resourcesRoot + metaPath + "?page=1&size=1000";
        if (name != null) {
            url += "&name=" + name;
        }
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String result = responseEntity.getBody();
        return (JSONObject) JSONObject.parseObject(result).get("data");
    }

    public JSONObject findAllDataSource() throws Exception {
        String url = resourcesRoot + dataSourcePath + "?page=1&size=1000";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> responseEntity = restTemplate.getForEntity(url, JSONObject.class);
        JSONObject result = responseEntity.getBody();
        return result.getJSONObject("data");
    }

    public JSONObject findAllContainer() throws Exception {
        String url = resourcesRoot + containerPath + "?page=1&size=1000&type=0";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> responseEntity = restTemplate.getForEntity(url, JSONObject.class);
        JSONObject result = responseEntity.getBody();
        return result.getJSONObject("data");
    }

    public JSONObject apiDesignInsert(String apiId, HttpServletRequest request) throws Exception {
        String token = webAdminUserService.getTokenByHttpServletRequest(request);
        ApiDesign apiDesign = apiDesignDao.findById(apiId).orElseThrow(() -> new DoValidException("菜单不存在"));
        List<ApiDeployment> apiDeploymentList = apiDeploymentDao.findAllByApiDesignId(apiDesign.getId());
        ApiDesignInsert apiDesignInsert = apiToApiDesignInsert(apiDesign, apiDeploymentList);
        String url = resourcesRoot + apiDesignPath;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "token " + token);
        HttpEntity httpEntity = new HttpEntity(JSONObject.toJSON(apiDesignInsert), headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, JSONObject.class);
        JSONObject json = responseEntity.getBody();
        if (json != null) {
            String code = json.getString("code");
            String fail = "0";
            if (fail.equals(code)) {
                throw new Exception(json.getString("msg"));
            }
        }
        return json.getJSONObject("data");
    }

    private ApiDesignInsert apiToApiDesignInsert(ApiDesign apiDesign, List<ApiDeployment> apiDeploymentList) throws Exception {
        DesignInfo designInfo = new DesignInfo();
        ApiDesignInsert apiDesignInsert = new ApiDesignInsert();
        JSONArray hostInfo = new JSONArray();
        for (ApiDeployment apiDeployment : apiDeploymentList) {
            hostInfo.add(apiDeployment.getUrl());
        }

        designInfo.setId(apiDesign.getId());
        designInfo.setName(apiDesign.getName());
        designInfo.setTagName(apiDesign.getMetaKey());
        designInfo.setTagValue(apiDesign.getMetaValue());
        designInfo.setProtocol(apiDesign.getProtocol());
        designInfo.setMethod(apiDesign.getMethod());
        designInfo.setFormatType(apiDesign.getFormatType());
        designInfo.setBody(apiDesign.getBody());
        designInfo.setParams(apiDesign.getParams());
        designInfo.setConstants("");
        designInfo.setResponse(apiDesign.getResponse());
        designInfo.setPath(apiDesign.getRequestUrl());

        apiDesignInsert.setDesignInfo(designInfo);
        apiDesignInsert.setHostInfo(hostInfo.toArray(new String[]{}));
        return apiDesignInsert;
    }
}
