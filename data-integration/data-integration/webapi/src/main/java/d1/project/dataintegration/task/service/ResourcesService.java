package d1.project.dataintegration.task.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


/**
 * 数据源
 *
 * @author zhengyang
 */
@Service
public class ResourcesService {
    @Value("${d1.project.resourcesRoot}")
    private String resourcesRoot;
    private String metaPath = "/webadmin/tagInfo/findAll";
    private String dataSourcePath = "/webadmin/dataSourceInfo/findAll";
    private String containerPath = "/webadmin/containerInfo/findAll";
    private String apiDesignPath = "webadmin/sourceApi/apiDesignInsert";

    public ResourcesService() {
    }

    public JSONObject findAllMetas(String name) throws Exception {
        String url = resourcesRoot + metaPath + "?page=1&size=1000";
        if(name!=null){
            url+="&name=" + name;
        }
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String result = responseEntity.getBody();
        return (JSONObject) JSONObject.parseObject(result).get("data");
    }

    public JSONObject findAllDataSource() throws Exception {
        String url = resourcesRoot + dataSourcePath + "?page=1&size=1000";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String result = responseEntity.getBody();
        return (JSONObject) JSONObject.parseObject(result).get("data");
    }

    public JSONObject findAllContainer() throws Exception {
        String url = resourcesRoot + containerPath + "?page=1&size=1000&type=1";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String result = responseEntity.getBody();
        return (JSONObject) JSONObject.parseObject(result).get("data");
    }

    public JSONObject apiDesignInsert(JSONObject model) throws Exception {
        String url = resourcesRoot + apiDesignPath;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "token ");
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(url, model, JSONObject.class);
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
}
