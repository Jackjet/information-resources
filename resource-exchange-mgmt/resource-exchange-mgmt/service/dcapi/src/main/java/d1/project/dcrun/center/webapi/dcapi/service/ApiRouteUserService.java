package d1.project.dcrun.center.webapi.dcapi.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.dcrun.center.webapi.common.service.mqtt.MqttClient;
import d1.project.dcrun.center.webapi.common.service.operation.record.OperationRecordService;
import d1.project.dcrun.center.webapi.common.service.sys.user.SysUserService;
import d1.project.dcrun.center.webapi.common.service.system.SysServiceService;
import d1.project.dcrun.center.webapi.common.util.MyUtils;
import d1.project.dcrun.center.webapi.dcapi.dao.ApiRouteUserDao;
import d1.project.dcrun.center.webapi.dcapi.entity.ApiRouteUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author xuaa
 */
@Service
public class ApiRouteUserService {
    @Autowired
    private ApiRouteUserDao apiRouteUserDao;
    @Autowired
    private MqttClient mqttClient;
    @Value("${file.server.uri}")
    private String fileDownload;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private OperationRecordService operationRecordService;

    @Autowired
    private SysServiceService sysServiceService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 查询开发者管理信息
     *
     * @param params 查询params
     */
    public Page<ApiRouteUser> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<ApiRouteUser> builder = new SpecificationBuilder<>();
        Specification specification = builder.init(params)
                .sEqual("integrationId", "integrationId")
                .sEqual("sysServiceId", "sysServiceId")
                .sEqual("appid", "appid")
                .dOrder("createTime").build();

        Page result = apiRouteUserDao.findAll(specification, builder.getPageable());
        return result;
    }

    /**
     * 添加开发者管理信息
     *
     * @param params  开发者管理params
     * @param request 浏览器请求
     */
    public void insert(JSONObject params, HttpServletRequest request) throws Exception {

        ApiRouteUser data = JSONObject.parseObject(params.toJSONString(), ApiRouteUser.class);
        // 开发者id，开发者密钥去除两边空格
        data.setAppid(MyUtils.eliminateSpaces(data.getAppid()));
        data.setAppkey(MyUtils.eliminateSpaces(data.getAppkey()));

        ApiRouteUser apiRouteUser = apiRouteUserDao.findByIntegrationIdAndSysServiceIdAndAppid(data.getIntegrationId(), data.getSysServiceId(), data.getAppid());
        if (apiRouteUser != null) {
            throw new ValidException("该集成项目下的appid已存在");
        }

        data.setId(MyUtils.generatePrimaryKeyId());
        tokenService.updateCreateIdAndTime(request, data);
        apiRouteUserDao.save(data);
    }

    public ApiRouteUser findById(String id) {
        Optional<ApiRouteUser> apiRouteUserOptional = apiRouteUserDao.findById(id);
        return apiRouteUserOptional.orElse(null);
    }

    public void delete(String id) {
        apiRouteUserDao.deleteById(id);
    }
}

