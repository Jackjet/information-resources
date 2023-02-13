package d1.project.dcrun.center.webapi.dcapi.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.dcrun.center.webapi.common.CommonFunction;
import d1.project.dcrun.center.webapi.common.service.mqtt.MqttClient;
import d1.project.dcrun.center.webapi.common.service.operation.record.OperationRecordService;
import d1.project.dcrun.center.webapi.common.service.sys.user.SysUser;
import d1.project.dcrun.center.webapi.common.service.sys.user.SysUserService;
import d1.project.dcrun.center.webapi.common.service.system.SysService;
import d1.project.dcrun.center.webapi.common.service.system.SysServiceService;
import d1.project.dcrun.center.webapi.common.util.MyUtils;
import d1.project.dcrun.center.webapi.common.util.OperationType;
import d1.project.dcrun.center.webapi.dcapi.dao.ApiRouteInfoDao;
import d1.project.dcrun.center.webapi.dcapi.dao.ApiViewDao;
import d1.project.dcrun.center.webapi.dcapi.entity.ApiRouteInfo;
import d1.project.dcrun.center.webapi.dcapi.entity.view.ApiView;
import d1.project.dcrun.center.webapi.dcapi.model.SyncRoutesVm;
import d1.project.dcrun.center.webapi.dcapi.utils.DCApiUtils;
import net.dcrun.component.mqtt.client.IMqttClientService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author lin
 */
@Service
public class ApiRouteInfoService {

    @Value("${file.server.uri}")
    private String fileDownload;

    @Autowired
    IMqttClientService mqttClientService;
    @Autowired
    private ApiRouteInfoDao apiRouteInfoDao;
    @Autowired
    private ApiViewDao apiViewDao;
    @Autowired
    private CommonFunction commonService;
    @Autowired
    private MqttClient mqttClient;
    @Autowired
    private SysServiceService sysServiceService;
    @Autowired
    private OperationRecordService operationRecordService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private TokenService tokenService;

    /**
     * 查询API信息
     *
     * @param params api查询params
     */
    public Page<ApiView> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<ApiView> builder = new SpecificationBuilder<ApiView>();
        Specification specification = builder.init(params)
                .sEqual("integrationId", "integrationId")
                .sEqual("sysServiceId", "sysServiceId")
                .sContain("name", "name")
                .sEqual("sourcePath", "sourcePath")
                .sEqual("targetPath", "targetPath")
                .dOrder("createTime").build();

        Page result = apiViewDao.findAll(specification, builder.getPageable());
        return result;
    }

    /**
     * 通过id查询
     *
     * @param id id
     */
    public ApiView find(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        } else {
            Optional<ApiView> t = apiViewDao.findById(id);
            return !t.isPresent() ? null : apiViewDao.findById(id).get();
        }
    }

    /**
     * 添加源API信息
     *
     * @param params  源API地址添加params
     * @param request 浏览器请求
     */
    public void insert(JSONObject params, HttpServletRequest request) throws Exception {
        ApiRouteInfo data = JSONObject.parseObject(params.toJSONString(), ApiRouteInfo.class);
        // 路由地址，服务地址去除两边空格
        data.setSourcePath(MyUtils.eliminateSpaces(data.getSourcePath()));
        data.setTargetPath(MyUtils.eliminateSpaces(data.getTargetPath()));

        data.setId(MyUtils.generatePrimaryKeyId());
        beforeInsert(data);
        tokenService.updateCreateIdAndTime(request, data);
        apiRouteInfoDao.save(data);
    }

    /**
     * 修改源API信息
     *
     * @param params  源API地址修改params
     * @param request 浏览器请求
     */
    public void update(JSONObject params, HttpServletRequest request) throws Exception {
        ApiRouteInfo data = JSONObject.parseObject(params.toJSONString(), ApiRouteInfo.class);
        // 路由地址，服务地址去除两边空格
        data.setSourcePath(MyUtils.eliminateSpaces(data.getSourcePath()));
        data.setTargetPath(MyUtils.eliminateSpaces(data.getTargetPath()));
        beforeUpdate(data);
        tokenService.updateUpdateIdAndTime(request, data);
        apiRouteInfoDao.save(data);
    }

    /**
     * 同步路由数据
     *
     * @param params 集成项目id和系统服务id
     */
    public void syncRoutes(JSONObject params) throws Exception {
        String integrationId = params.getString("integrationId");
        String appid = params.getString("sysServiceId");
        String hosts = commonService.getGetHostsSupplier().get();

        List<Map<String, Object>> apiList = apiRouteInfoDao.findAllByIntegrationIdAndSysServiceId(integrationId, appid);
        String jsonObject1 = JSONObject.toJSON(apiList).toString();
        List<SyncRoutesVm> syncRoutesVms = JSONObject.parseArray(jsonObject1, SyncRoutesVm.class);

        JSONArray routes = new JSONArray();
        for (SyncRoutesVm syncRoutes : syncRoutesVms) {
            JSONObject route = new JSONObject();
            route.put("id", syncRoutes.getId());
            //路由名称  (路由表name字段值)
            route.put("name", syncRoutes.getName());
            //此路由允许的协议，取值http或https，多个用","隔开 先写死http
            route.put("protocols", "http");
            //此路由允许的方法，多个用","隔开 (路由表sourceMethod字段值)
            route.put("methods", syncRoutes.getSourceMethod());
            //此路由允许的域名，多个用","隔开 (控制中心表hosts字段值)
            route.put("hosts", hosts);
            //此路由匹配的path，多个用","隔开 (路由表sourcePath字段值)
            route.put("paths", syncRoutes.getSourcePath());

            JSONObject accessControlPolicy = new JSONObject();
            //访问控制策略表id字段值
            accessControlPolicy.put("id", syncRoutes.getAccessStrategyId());
            //访问类型 0：IP地址、1：账户名  (访问控制策略表type字段值)
            accessControlPolicy.put("type", syncRoutes.getAccessType());
            //访问动作 0：允许、1：禁止  (访问控制策略表operation字段值)
            accessControlPolicy.put("operation", syncRoutes.getOperation());
            switch (syncRoutes.getAccessType()) {
                case "0":
                    //允许/禁止的ip，多个用","隔开  (访问控制策略表ips字段值)
                    accessControlPolicy.put("ips", syncRoutes.getIps());
                    accessControlPolicy.put("appIds", "");
                    break;
                case "1":
                    //允许/禁止的微应用id，多个用","隔开  (访问控制策略表appIds字段值)
                    accessControlPolicy.put("ips", "");
                    accessControlPolicy.put("appIds", syncRoutes.getAppIds());
                    break;
                default:
                    break;
            }

            //访问控制策略
            route.put("accessControlPolicy", accessControlPolicy);

            JSONObject flowControlPolicy = new JSONObject();
            //流量控制策略表id字段值
            flowControlPolicy.put("id", syncRoutes.getAccessStrategyId());
            //流量控制类型 0：既不限制用户流量控制也不限制ip流量控制 1：用户流量控制  2: ip流量控制
            flowControlPolicy.put("type", syncRoutes.getFlowType());
            //时长(一天为单位)
            flowControlPolicy.put("time", syncRoutes.getTime());
            //api访问次数上限，如果-1表示不限制
            flowControlPolicy.put("totalCount", syncRoutes.getApiFlowLimit());
            //当type=1：表示单个用户访问次数，不能超过api访问次数上限
            //当type=2：表示单个ip访问次数，不能超过api访问次数上限
            switch (syncRoutes.getFlowType()) {
                case "0":
                    flowControlPolicy.put("count", -1);
                    break;
                case "1":
                    if (StringUtils.isEmpty(syncRoutes.getAppIdFlowLimit())) {
                        flowControlPolicy.put("count", 0);
                    } else {
                        flowControlPolicy.put("count", syncRoutes.getAppIdFlowLimit());
                    }
                    break;
                case "2":
                    if (StringUtils.isEmpty(syncRoutes.getIpFlowLimit())) {
                        flowControlPolicy.put("count", 0);
                    } else {
                        flowControlPolicy.put("count", syncRoutes.getIpFlowLimit());
                    }
                    break;
                default:
                    break;
            }
            //访问控制策略
            route.put("flowControlPolicy", flowControlPolicy);

            JSONObject service = new JSONObject();
            service.put("id", "");
            //api名称
            service.put("name", "");
            //通讯协议，取值http或https
            service.put("protocol", "http");
            //此服务允许的方法 (路由表targetMethod字段值)
            String targetPath = syncRoutes.getTargetPath();
            String[] targetPathArr = validTargetPath(targetPath);

            service.put("methods", syncRoutes.getTargetMethod());
            //判断地址是否有端口号
            if (targetPathArr[2].contains(":")) {
                //服务主机 (路由表targetPath字段值拆分)
                service.put("host", targetPathArr[2].split(":")[0]);
                //服务端口 (路由表targetPath字段值拆分)
                service.put("port", targetPathArr[2].split(":")[1]);
            } else {
                service.put("host", targetPathArr[2]);
                service.put("port", "");
            }
            //服务请求中的路径，必须以/开头  (路由表targetPath字段值拆分)
            if (targetPathArr.length - 3 == 0) {
                service.put("path", "");
            } else {
                StringBuilder path = new StringBuilder();
                for (int i = 0; i < targetPathArr.length - 3; i++) {
                    path.append("/").append(targetPathArr[i + 3]);
                }
                service.put("path", path);
            }
            //对应的api服务
            route.put("service", service);
            routes.add(route);
        }

        //创建文件
        File routerJsonFile = new File(DCApiUtils.getRouterJsonOutputPath(appid));
        FileUtils.write(routerJsonFile, JSON.toJSONString(routes), StandardCharsets.UTF_8);

        SysService sysService = sysServiceService.findById(appid);
        String operationId = operationRecordService.insert(sysService, OperationType.SYNC_ROUTES);

        String topic = DCApiUtils.getSyncRouterTopic(integrationId, sysService.getRunNodeId(), appid);
        String url = fileDownload + "downloadDcapiRoutes/" + routerJsonFile.getName();
        mqttClient.publish(topic, appid, url, "", operationId);

        //发送同步开发者
        syncApiRouteUser(params);
    }

    public void syncApiRouteUser(JSONObject param) throws Exception {
        String integrationId = param.getString("integrationId");
        String appid = param.getString("sysServiceId");

        List<SysUser> sysUsers = sysUserService.findAllByIntegrationId(integrationId);

        //创建文件
        File userJsonFile = new File(DCApiUtils.getUserJsonOutputPath(appid));
        FileUtils.write(userJsonFile, JSON.toJSONString(sysUsers), StandardCharsets.UTF_8);

        SysService sysService = sysServiceService.findById(appid);

        String operationId = operationRecordService.insert(sysService, OperationType.SYNC_USER);

        String topic = DCApiUtils.getSyncUserTopic(integrationId, sysService.getRunNodeId(), appid);
        String url = fileDownload + "downloadDcapiRoutes/" + userJsonFile.getName();
        mqttClient.publish(topic, appid, url, "", operationId);
    }


    protected void beforeInsert(ApiRouteInfo data) throws Exception {
        boolean flag = this.apiRouteInfoDao.existsByIntegrationIdAndSysServiceIdAndName(data.getIntegrationId(), data.getSysServiceId(), data.getName());
        if (flag) {
            throw new ValidException("路由名称已存在");
        }

        validTargetPath(data.getTargetPath());
    }

    protected void beforeUpdate(ApiRouteInfo data) throws Exception {
        if (!apiRouteInfoDao.existsById(data.getId())) {
            throw new ValidException("找不到这个id对应的资源");
        }
        ApiRouteInfo oldData = findById(data.getId());
        String name = data.getName();
        String oldName = oldData.getName();

        boolean flag = this.apiRouteInfoDao.existsByIntegrationIdAndSysServiceIdAndName(data.getIntegrationId(), data.getSysServiceId(), data.getName());
        if (!name.equals(oldName) && flag) {
            throw new ValidException("路由名称已存在");
        }

        validTargetPath(data.getTargetPath());
    }

    protected void beforeDelete(String id) throws Exception {
        if (!apiRouteInfoDao.existsById(id)) {
            throw new ValidException("未找到id=" + id + "的资源");
        }
    }

    public ApiRouteInfo findById(String id) {
        Optional<ApiRouteInfo> apiRoteInfoOptional = apiRouteInfoDao.findById(id);
        return apiRoteInfoOptional.orElse(null);
    }

    public void delete(String id) throws Exception {
        beforeDelete(id);
        apiRouteInfoDao.deleteById(id);
    }


    /**
     * 校验目标api地址信息格式
     *
     * @param targetPath 目标api地址
     * @return
     * @throws Exception
     */
    private String[] validTargetPath(String targetPath) throws Exception {
        String[] targetPathArr = targetPath.split("/");
        if (targetPathArr.length < 2) {
            throw new ValidException("目标api地址信息格式不正确");
        }
        return targetPathArr;
    }


}
