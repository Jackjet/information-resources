package d1.project.api.integration.kong.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.project.api.integration.apimanage.entity.KongApi;
import d1.project.api.integration.kong.entity.KongConsumer;
import net.dcrun.component.http.HttpService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class KongService {
    /**
     * 添加upstream
     *
     * @param container
     * @return
     * @throws Exception
     */
    public JSONObject addUpstream(String container) throws Exception {
        String name = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String url = getKongUrl(container) + "upstreams/";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);

        Map<String, Object> healthchecks = new HashMap<>();
        healthchecks.put("threshold", 10);

        parameters.put("healthchecks", healthchecks);

        return doPost(url, parameters);
    }

    /**
     * 删除upstream
     *
     * @param container
     * @param id
     * @throws Exception
     */
    public void deleteUpstream(String container, String id) throws Exception {
        String url = getKongUrl(container) + "upstreams/" + id;
        doDelete(url);
    }

    /**
     * 添加target
     *
     * @param container
     * @param upstreamId
     * @param target
     * @return
     * @throws Exception
     */
    public JSONObject addTarget(String container, String upstreamId, String target) throws Exception {
        String url = getKongUrl(container) + "upstreams/" + upstreamId + "/targets/";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("target", target);
        parameters.put("weight", 100);

        return doPost(url, parameters);
    }

    public JSONArray getAllTarget(String container, String upstreamId) throws Exception {
        String url = getKongUrl(container) + "upstreams/" + upstreamId + "/targets/";
        JSONObject result = doGet(url);
        return result.getJSONArray("data");
    }

    public void deleteTarget(String container, String upstreamId, String id) throws Exception {
        String url = getKongUrl(container) + "upstreams/" + upstreamId + "/targets/" + id;
        doDelete(url);
    }

    public JSONObject addService(String container, String upstreamName, String name, String path, String protocol) throws Exception {
        String url = getKongUrl(container) + "services/";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("host", upstreamName);
        parameters.put("path", path);
        parameters.put("name", name);
        parameters.put("protocol", protocol.toLowerCase());

        return doPost(url, parameters);
    }

    public JSONObject addRoute(String container, String serviceId, List<String> paths, List<String> methods) throws Exception {
        String url = getKongUrl(container) + "services/" + serviceId + "/routes/";

        Map<String, Object> parameters = new HashMap<>();
        String name = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        parameters.put("name", name);
        parameters.put("paths", paths);
        parameters.put("methods", methods);

        return doPost(url, parameters);
    }

    public void deleteRoute(String container, String serviceId, String routeId) throws Exception {
        String url = getKongUrl(container) + "services/" + serviceId + "/routes/" + routeId;
        doDelete(url);
    }

    public JSONObject addPlugin(String container, String pos, String posId, String pluginName, JSONObject config, boolean enabled) throws Exception {
        String url = "";
        if (StringUtils.isEmpty(pos) || StringUtils.isEmpty(posId)) {
            url = getKongUrl(container) + "plugins";
        } else {
            url = getKongUrl(container) + pos + "/" + posId + "/plugins";
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", pluginName);
        parameters.put("enabled", enabled);
        if (config != null) {
            parameters.put("config", config);
        }

        return doPost(url, parameters);
    }

    public JSONObject updatePlugin(String container, String pos, String posId, String pluginId, String pluginName, JSONObject config, boolean enabled) throws Exception {
        String url = getKongUrl(container) + pos + "/" + posId + "/plugins/" + pluginId;

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", pluginName);
        parameters.put("enabled", enabled);
        if (config != null) {
            parameters.put("config", config);
        }

        return doPut(url, parameters);
    }

    public void deletePlugin(String container, String pos, String posId, String pluginId) throws Exception {
        String url = getKongUrl(container) + pos + "/" + posId + "/plugins/" + pluginId;
        doDelete(url);
    }

    /**
     * kong插件查询是分页的，所以需要自身迭代
     * @param container
     * @param path
     * @return
     * @throws Exception
     */
    public JSONArray getPluginData(String container, String path) throws Exception {
        JSONObject getPluginResult = getPlugin(container, path);
        JSONArray dataArray = getPluginResult.getJSONArray("data");
        String next = getPluginResult.getString("next");
        if (next != null) {
            //kong这里不能用双//，所以把第一个字符删掉
            next = next.substring(1);
            dataArray.addAll(getPluginData(container, next));
        }
        return dataArray;
    }

    public JSONObject getPlugin(String container, String path) throws Exception {
        String url = getKongUrl(container) + path;
        return doGet(url);
    }

    public JSONObject addConsumer(String container, String username) throws Exception {
        String url = getKongUrl(container) + "consumers";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("username", username);

        return doPost(url, parameters);
    }

    public void deleteConsumer(String container, String id) throws Exception {
        String url = getKongUrl(container) + "consumers/" + id;
        doDelete(url);
    }

    public JSONObject getConsumer(String container, String consumerId) throws Exception {
        String url = getKongUrl(container) + "consumers/" + consumerId;
        return doGet(url);
    }

    public JSONObject addHmacKey(String container, KongConsumer consumer) throws Exception {
        String url = getKongUrl(container) + "consumers/" + consumer.getConsumerId() + "/hmac-auth";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("username", consumer.getUsername());
        parameters.put("secret", consumer.getKey());

        return doPost(url, parameters);
    }

    public JSONObject updateHmacKey(String container, KongConsumer consumer) throws Exception {
        String url = getKongUrl(container) + "consumers/" + consumer.getConsumerId() + "/hmac-auth/" + consumer.getHmacId();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("username", consumer.getUsername());
        parameters.put("secret", consumer.getKey());

        return doPut(url, parameters);
    }

    public void deleteHmacKey(String container, KongConsumer consumer) throws Exception {
        String url = getKongUrl(container) + "consumers/" + consumer.getConsumerId() + "/hmac-auth/" + consumer.getHmacId();
        doDelete(url);
    }

    public JSONObject addKeyAuth(String container, KongConsumer consumer) throws Exception {
        String url = getKongUrl(container) + "consumers/" + consumer.getConsumerId() + "/key-auth";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("key", consumer.getKey());

        return doPost(url, parameters);
    }

    public JSONObject updateKeyAuth(String container, KongConsumer consumer) throws Exception {
        String url = getKongUrl(container) + "consumers/" + consumer.getConsumerId() + "/key-auth/" + consumer.getKeyAuthId();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("key", consumer.getKey());

        return doPut(url, parameters);
    }

    public void deleteKeyAuth(String container, KongConsumer consumer) throws Exception {
        String url = getKongUrl(container) + "consumers/" + consumer.getConsumerId() + "/key-auth/" + consumer.getKeyAuthId();
        doDelete(url);
    }


    public JSONObject addConsumerGroup(String container, String consumerId, String groupName) throws Exception {
        String url = getKongUrl(container) + "consumers/" + consumerId + "/acls";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("group", groupName);

        return doPost(url, parameters);
    }

    //===========================================================

    private void checkMessage(String response) throws DoValidException {
        if (!StringUtils.isEmpty(response)) {
            JSONObject responseJson = JSONObject.parseObject(response);
            if(responseJson.containsKey("message")) {
                String message = responseJson.getString("message");
                if (!StringUtils.isEmpty(message)) {
                    throw new DoValidException(JSONObject.parseObject(response).getString("message"));
                }
            }
        }
    }

    private String getKongUrl(String container) {
        return "http://" + container + "/";
    }

    private JSONObject doGet(String url) throws Exception {
        HttpService httpService = new HttpService();
        httpService.setTimeout(18000, 18000, 18000);

        Map<String, Object> headers = new HashMap<>();
        Map<String, Object> parameters = new HashMap<>();

        String response = httpService.get(url, headers, parameters);
        checkMessage(response);

        return JSON.parseObject(response);
    }

    private JSONObject doPost(String url, Map<String, Object> parameters) throws Exception {
        HttpService httpService = new HttpService();
        httpService.setTimeout(18000, 18000, 18000);

        Map<String, Object> headers = new HashMap<>();
        String contentType = "application/json";

        String response = httpService.post(url, headers, parameters, contentType);
        checkMessage(response);

        return JSON.parseObject(response);
    }

    private JSONObject doPut(String url, Map<String, Object> parameters) throws Exception {
        HttpService httpService = new HttpService();
        httpService.setTimeout(18000, 18000, 18000);

        Map<String, Object> headers = new HashMap<>();
        String contentType = "application/json";

        String response = httpService.put(url, headers, parameters, contentType);
        checkMessage(response);

        return JSON.parseObject(response);
    }

    private JSONObject doDelete(String url) throws Exception {
        HttpService httpService = new HttpService();
        httpService.setTimeout(18000, 18000, 18000);

        Map<String, Object> headers = new HashMap<>();
        String contentType = "application/json";

        Map<String, Object> parameters = new HashMap<>();

        String response = httpService.delete(url, headers, parameters, contentType);
        checkMessage(response);

        return JSON.parseObject(response);
    }
}

