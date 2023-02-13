package d1.project.api.integration.kong.business;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.project.api.integration.apimanage.entity.KongApi;
import d1.project.api.integration.apimanage.entity.KongSourceApi;
import d1.project.api.integration.application.entity.NormalConsumer;
import d1.project.api.integration.common.service.INormalConsumerService;
import d1.project.api.integration.kong.entity.DefaultConsumer;
import d1.project.api.integration.kong.entity.KongConsumer;
import d1.project.api.integration.kong.service.DefaultConsumerService;
import d1.project.api.integration.kong.service.KongService;
import d1.project.api.integration.kong.utils.KongUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

@Service
public class KongBusiness {
    private final KongService kongService;
    private final DefaultConsumerService defaultConsumerService;
    private final INormalConsumerService iNormalConsumerService;

    public KongBusiness(KongService kongService, DefaultConsumerService defaultConsumerService, INormalConsumerService iNormalConsumerService) {
        this.kongService = kongService;
        this.defaultConsumerService = defaultConsumerService;
        this.iNormalConsumerService = iNormalConsumerService;
    }

    /**
     * 保存源API
     *
     * @param sourceApi
     * @throws Exception
     */
    public void saveSourceApi(String container, KongSourceApi sourceApi) throws Exception {
        //创建上游
        this.addUpStream(container,sourceApi);

        //创建target
        this.addTarget(container,sourceApi);

        //创建service
        this.addService(container,sourceApi);
    }

    /**
     * 删除源API
     *
     * @param sourceApi
     * @throws Exception
     */
    public void deleteSourceApi(String container, KongSourceApi sourceApi) throws Exception {
        //删除target
        JSONArray targets = JSON.parseArray(sourceApi.getHost());
        for (Object obj : targets) {
            JSONObject target = (JSONObject) obj;
            kongService.deleteTarget(container, sourceApi.getUpstreamId(), target.getString("id"));
        }

        kongService.deleteUpstream(container, sourceApi.getUpstreamId());
    }

    /**
     * 保存API
     *
     * @param api
     * @throws Exception
     */
    public void saveApi(String container, KongApi api) throws Exception {
        //添加路由
        this.addRoute(container,api);

        //添加鉴权插件
        this.addAuth(container,api);

        //添加分组插件
        this.addGroup(container,api);

        //安装限流插件
        this.addRateLimiting(container,api);

        //安装限制访问大小插件
        this.addRequestSizeLimiting(container,api);
    }

    /**
     * 删除API
     *
     * @param api
     * @throws Exception
     */
    public void deleteApi(String container, KongApi api) throws Exception {
        kongService.deletePlugin(container,"routes", api.getRouteId(), api.getAclId());
        kongService.deletePlugin(container,"routes", api.getRouteId(), api.getAuthId());
        kongService.deletePlugin(container,"routes", api.getRouteId(), api.getRateLimitingId());
        kongService.deletePlugin(container, "routes", api.getRouteId(), api.getRequestSizeLimitingId());

        kongService.deleteRoute(container, api.getServiceId(), api.getRouteId());
    }

    /**
     * 添加用户
     *
     * @param consumer
     * @throws Exception
     */
    public void addConsumer(String container, NormalConsumer consumer) throws Exception {
        //创建consumer
        JSONObject addConsumerResult = kongService.addConsumer(container, consumer.getUsername());
        consumer.setConsumerId(addConsumerResult.getString("id"));

        //创建密钥
        //添加hmac密钥
        JSONObject addHmacKeyResult = kongService.addHmacKey(container,consumer);
        consumer.setHmacId(addHmacKeyResult.getString("id"));

        //添加key-auth密钥
        JSONObject addKeyAuthResult = kongService.addKeyAuth(container,consumer);
        consumer.setKeyAuthId(addKeyAuthResult.getString("id"));

        //设置consumer的组
        kongService.addConsumerGroup(container, consumer.getConsumerId(), consumer.getGroups());

        //添加ip插件
        JSONObject config = new JSONObject();
        List<String> allow = new Vector<>();
        allow.add("127.0.0.1");
        config.put("allow",allow);
        JSONObject addIpPluginResult = kongService.addPlugin(container, "consumers", consumer.getConsumerId(), "ip-restriction", config,false);
        consumer.setIpPluginId(addIpPluginResult.getString("id"));

        iNormalConsumerService.save(consumer);
    }

    /**
     * 修改密钥
     *
     * @param consumer
     * @throws Exception
     */
    public void updateKey(String container, KongConsumer consumer) throws Exception {
        kongService.updateHmacKey(container, consumer);
        kongService.updateKeyAuth(container, consumer);
    }

    /**
     * 删除应用
     *
     * @param consumer
     * @throws Exception
     */
    public void deleteConsumer(String container, NormalConsumer consumer) throws Exception {
        //删除密钥
        if(!StringUtils.isEmpty(consumer.getHmacId())) {
            kongService.deleteHmacKey(container, consumer);
        }

        if(!StringUtils.isEmpty(consumer.getKeyAuthId())) {
            kongService.deleteKeyAuth(container, consumer);
        }

        if(!StringUtils.isEmpty(consumer.getConsumerId())) {
            //删除ip插件
            if (!StringUtils.isEmpty(consumer.getIpPluginId())) {
                kongService.deletePlugin(container, "consumers", consumer.getConsumerId(), consumer.getIpPluginId());
            }

            kongService.deleteConsumer(container, consumer.getConsumerId());
        }
    }

    /**
     * 授权   aclGroup  格式  group1,group2, 为应用表里的consumerId字段
     */
    public void setPermission(String container, KongApi api) throws Exception {
        JSONObject aclConfig = new JSONObject();
        List<String> allow = new Vector<>();
        if (!StringUtils.isEmpty(api.getAclGroup())) {
            allow = KongUtils.getInstance().stringToList(api.getAclGroup());
        }

        //设置默认允许的用户
        DefaultConsumer defaultConsumer = defaultConsumerService.findByContainer(container);
        allow.add(defaultConsumer.getGroups());
        aclConfig.put("allow",allow);

        //更新所属分组
        kongService.updatePlugin(container,"routes", api.getRouteId(), api.getAclId(), "acl", aclConfig,true);

    }

    /**
     * ip白名单
     */
    public void setIpWhiteListOrBlackList(String container, NormalConsumer consumer, Integer type, String ipList) throws Exception {
        if (!StringUtils.isEmpty(ipList)) {
            JSONObject ipConfig = new JSONObject();
            List<String> ips = KongUtils.getInstance().stringToList(ipList);
            if (type == 0) {
                ipConfig.put("deny", ips);
            } else {
                ipConfig.put("allow", ips);
            }

            kongService.updatePlugin(container,"consumers", consumer.getConsumerId(), consumer.getIpPluginId(), "ip-restriction", ipConfig,true);
        }
    }

    public Boolean checkAuth(String container, String consumerId) throws Exception {
        Boolean result = true;
        JSONObject getConsumerResult = this.kongService.getConsumer(container, consumerId);
        String id = getConsumerResult.getString("id");
        if (StringUtils.isEmpty(id) || !consumerId.equals(id)) {
            result = false;
        }

        return result;
    }



    //============================================================================================
    private void addRoute(String container,KongApi api) throws Exception {
        //添加路由
        List<String> paths = new Vector<>();
        paths.add(api.getRouteInfo());
        List<String> methods = KongUtils.getInstance().stringToList(api.getMethods());

        JSONObject addRouteResult = kongService.addRoute(container, api.getServiceId(), paths, methods);
        String routeId = addRouteResult.getString("id");
        api.setRouteId(routeId);
    }

    private void addGroup(String container,KongApi api) throws Exception {
        JSONObject aclConfig = new JSONObject();
        //设置默认允许的用户
        DefaultConsumer defaultConsumer = defaultConsumerService.findByContainer(container);
        List<String> allow = new Vector<>();
        allow.add(defaultConsumer.getGroups());
        aclConfig.put("allow",allow);

        JSONObject addAclResult = kongService.addPlugin(container, "routes", api.getRouteId(), "acl", aclConfig,true);
        api.setAclId(addAclResult.getString("id"));
    }

    private void addRateLimiting(String container,KongApi api) throws Exception {
        if (api.getPerMinute() != 0 || api.getEveryHour() != 0 || api.getEveryDay() == 0) {
            JSONObject rateLimitingConfig = new JSONObject();
            if(api.getPerMinute() != 0) {
                rateLimitingConfig.put("minute", api.getPerMinute());
            }
            if(api.getEveryHour() != 0) {
                rateLimitingConfig.put("hour", api.getEveryHour());
            }
            if(api.getEveryDay() != 0) {
                rateLimitingConfig.put("day", api.getEveryDay());
            }

            JSONObject rateLimitingResult = kongService.addPlugin(container, "routes", api.getRouteId(), "rate-limiting", rateLimitingConfig,true);
            api.setRateLimitingId(rateLimitingResult.getString("id"));
        }
    }

    private void addRequestSizeLimiting(String container,KongApi api) throws Exception {
       if(api.getSingleSize() != 0) {
           JSONObject requestSizeLimitingConfig = new JSONObject();
           requestSizeLimitingConfig.put("allowed_payload_size", api.getSingleSize());
           requestSizeLimitingConfig.put("size_unit", "megabytes");
           JSONObject requestSizeLimitingResult = kongService.addPlugin(container, "routes", api.getRouteId(), "request-size-limiting", requestSizeLimitingConfig,true);
           api.setRequestSizeLimitingId(requestSizeLimitingResult.getString("id"));
       }
    }

    private void addAuth(String container,KongApi api) throws Exception {
        switch (api.getVerificationMode()) {
            case "0":
                //安装Hmac插件
                JSONObject hmacAuthResult = kongService.addPlugin(container, "routes", api.getRouteId(), "hmac-auth", null,true);
                api.setAuthId(hmacAuthResult.getString("id"));
                break;
            case "1":
                //安装Key-Auth插件
                JSONObject config = new JSONObject();
                List<String> keyNames = new Vector<>();
                keyNames.add("apikey");
                config.put("key_names",JSON.toJSON(keyNames));

                JSONObject keyAuthResult = kongService.addPlugin(container, "routes", api.getRouteId(), "key-auth", config,true);
                api.setAuthId(keyAuthResult.getString("id"));
                break;
            default:
                throw new DoValidException("非法鉴权类型");
        }
    }

    private void addUpStream(String container,KongSourceApi sourceApi) throws Exception {
        //创建上游
        JSONObject addUpstreamResult = kongService.addUpstream(container);
        String upstreamId = addUpstreamResult.getString("id");
        String upstreamName = addUpstreamResult.getString("name");
        sourceApi.setUpstreamId(upstreamId);
        sourceApi.setUpstreamName(upstreamName);
    }

    private void addTarget(String container,KongSourceApi sourceApi) throws Exception {
        //创建target
        JSONArray targets = JSON.parseArray(sourceApi.getHost());
        for (Object obj : targets) {
            JSONObject target = (JSONObject) obj;
            JSONObject addTargetResult = kongService.addTarget(container, sourceApi.getUpstreamId(), target.getString("target"));
            target.put("id", addTargetResult.getString("id"));
        }
        sourceApi.setHost(targets.toJSONString());
    }

    private void addService(String container,KongSourceApi sourceApi) throws Exception {
        //创建service
        String name = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        JSONObject addServiceResult = kongService.addService(container, sourceApi.getUpstreamName(), name, sourceApi.getPath(), sourceApi.getProtocol());
        String serviceId = addServiceResult.getString("id");
        sourceApi.setServiceId(serviceId);
    }
}
