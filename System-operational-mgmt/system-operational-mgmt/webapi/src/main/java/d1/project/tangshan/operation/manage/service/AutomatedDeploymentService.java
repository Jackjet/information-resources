package d1.project.tangshan.operation.manage.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.tangshan.operation.manage.dao.AutomatedDeploymentDao;
import d1.project.tangshan.operation.manage.entity.AutomatedDeploymentEntity;
import d1.project.tangshan.operation.manage.entity.AutomatedDeploymentLogEntity;
import d1.project.tangshan.operation.manage.entity.InstallPathEntity;
import d1.project.tangshan.operation.manage.entity.operations.module.Node;
import d1.project.tangshan.operation.manage.entity.operations.module.Services;
import d1.project.tangshan.operation.manage.service.operations.log.OperationLogService;
import d1.project.tangshan.operation.manage.service.operations.module.NodeService;
import d1.project.tangshan.operation.manage.service.operations.module.ServicesService;
import d1.project.tangshan.operation.manage.utils.MyUtils;
import io.swagger.annotations.ApiModelProperty;
import net.dcrun.component.http.IHttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.util.*;

/**
 * @author Buter
 * @date 2020/3/16 6:50
 */
@Service
public class AutomatedDeploymentService {
    private final Logger logger = LoggerFactory.getLogger(AutomatedDeploymentService.class);
    private final AutomatedDeploymentDao automatedDeploymentDao;
    private final IHttpService httpService;
    private final NodeService nodeService;
    private final ServicesService servicesService;
    private final BigDataApiService bigDataApiService;
    private final InstallPathService installPathService;
    private final AutomatedDeploymentLogService automatedDeploymentLogService;
    private final TokenService tokenService;
    private final OperationLogService operationLogService;

    @Value("${service.host}")
    private String serviceHost;
    @Value("${auto.deploy}")
    private String autoDeploy;
    @Value("${auto.callbackUrl}")
    private String callbackUrl;
    @Autowired
    AsyncService asyncService;

    @Autowired
    public AutomatedDeploymentService(AutomatedDeploymentDao automatedDeploymentDao, IHttpService httpService, NodeService nodeService, ServicesService servicesService, BigDataApiService bigDataApiService, InstallPathService installPathService, AutomatedDeploymentLogService automatedDeploymentLogService, TokenService tokenService, OperationLogService operationLogService) {
        this.automatedDeploymentDao = automatedDeploymentDao;
        this.httpService = httpService;
        this.nodeService = nodeService;
        this.servicesService = servicesService;
        this.bigDataApiService = bigDataApiService;
        this.installPathService = installPathService;
        this.automatedDeploymentLogService = automatedDeploymentLogService;
        this.tokenService = tokenService;
        this.operationLogService = operationLogService;
    }

    public Page<AutomatedDeploymentEntity> findAll(JSONObject params, HttpServletRequest request) throws Exception {
        SpecificationBuilder<AutomatedDeploymentEntity> builder = new SpecificationBuilder<>();
        Specification specification = builder.init(params)
                .sEqual("nodeId", "nodeId")
                .sEqual("sysId", "sysId")
                .sEqual("type", "type")
                .sIn("typeStage", "typeStageIn")
                .dOrder("createTime").build();
        return this.automatedDeploymentDao.findAll(specification, builder.getPageable());
    }

    public AutomatedDeploymentEntity findById(String id) throws Exception {
        return automatedDeploymentDao.findById(id).orElseThrow(() -> new ValidException("找不到记录：" + id));
    }


    public void insert(JSONObject params, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        AutomatedDeploymentEntity entity = JSON.toJavaObject(params, AutomatedDeploymentEntity.class);
        String nodeName = entity.getNodeName();
        try {
            if (automatedDeploymentDao.existsByVersionNumberGreaterThanEqualAndSysId(entity.getVersionNumber(), entity.getSysId())) {
                throw new ValidException("版本号应大于当前版本");
            }

            Optional<AutomatedDeploymentEntity> notDeployed = automatedDeploymentDao.findFirstBySysIdAndTypeOrderByCreateTimeDesc(entity.getSysId(), entity.getType());
            if (notDeployed.isPresent()) {
                AutomatedDeploymentEntity automatedDeploymentEntity = notDeployed.get();
                if("NotDeployed".equals(automatedDeploymentEntity.getTypeStage())){
                    throw new ValidException("请先部署未发布版本");
                }

            }
    
            if ("upgrade".equals(entity.getType()) || "configUpdate".equals(entity.getType())) {
                if (!automatedDeploymentDao.existsBySysId(entity.getSysId())) {
                    throw new ValidException("请先部署版本");
                }
            }
            entity.setTypeStage("NotDeployed");
            entity.setId(MyUtils.generate32Id());
            tokenService.updateCreateIdAndTime(request, entity);

            entity.setTypeStage("NotDeployed");
            automatedDeploymentDao.save(entity);

            operationLogService.setLog("自动化部署管理—添加", userName, "自动化部署-自动化部署管理", "添加了一个:" + nodeName + "下的自动化部署", "1", request);
        } catch (Exception e) {
            operationLogService.setLog("自动化部署管理—添加", userName, "自动化部署-自动化部署管理", "添加了一个:" + nodeName + "下的自动化部署", "0", request);
            throw new ValidException(e.getMessage());
        }
    }


    public void update(JSONObject params, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String nodeName = "未知";
        try {
            String id = params.getString("id");
            AutomatedDeploymentEntity entity = automatedDeploymentDao.findById(id).orElseThrow(() -> new ValidException("找不到记录：" + id));
            nodeName = entity.getNodeName();
            entity.setApiUrl(params.getString("apiUrl"));
            entity.setApiMethod(params.getString("apiMethod"));
            entity.setType(params.getString("type"));
            entity.setInstallPathId(params.getString("installPathId"));
            entity.setAutomatedPackPath(params.getString("automatedPackPath"));
            entity.setNodeId(params.getString("nodeId"));
            entity.setNodeName(params.getString("nodeName"));
            entity.setSysId(params.getString("sysId"));
            entity.setSysName(params.getString("sysName"));
            entity.setRemark(params.getString("remark"));
            tokenService.updateUpdateIdAndTime(request, entity);
            automatedDeploymentDao.save(entity);

            operationLogService.setLog("自动化部署管理—编辑", userName, "自动化部署-自动化部署管理", "编辑了一个:" + nodeName + "下的自动化部署", "1", request);
        } catch (ValidException e) {
            operationLogService.setLog("自动化部署管理—编辑", userName, "自动化部署-自动化部署管理", "编辑了一个:" + nodeName + "下的自动化部署", "0", request);
            throw new ValidException(e.getMessage());
        }
    }


    public void delete(String id, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String nodeName = "未知";
        try {
            AutomatedDeploymentEntity entity = automatedDeploymentDao.findById(id).orElseThrow(() -> new ValidException("找不到记录：" + id));
            nodeName = entity.getNodeName();
            automatedDeploymentDao.delete(entity);

            operationLogService.setLog("自动化部署管理—删除", userName, "自动化部署-自动化部署管理", "删除了一个:" + nodeName + "下的自动化部署", "1", request);
        } catch (ValidException e) {
            operationLogService.setLog("自动化部署管理—删除", userName, "自动化部署-自动化部署管理", "删除了一个:" + nodeName + "下的自动化部署", "0", request);
            throw new ValidException(e.getMessage());
        }
    }


    public void deployment(String id, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        AutomatedDeploymentEntity automatedDeploymentEntity = automatedDeploymentDao.findById(id).orElseThrow(() -> new ValidException("找不到记录：" + id));
        if (automatedDeploymentDao.existsByVersionNumberGreaterThanAndSysIdAndType(automatedDeploymentEntity.getVersionNumber(), automatedDeploymentEntity.getSysId(),automatedDeploymentEntity.getType())) {
            throw new ValidException("请发布最新版本");
        }
        Node node = nodeService.findById(automatedDeploymentEntity.getNodeId());
        String nodeName = node.getName();
        try {

            pushDeploy(request, automatedDeploymentEntity, node, false);
            operationLogService.setLog("版本管理及部署—部署", userName, "自动化部署-版本管理及部署", "执行:" + nodeName + "下运行管理系统自动安装", "1", request);
        } catch (Exception e) {
            operationLogService.setLog("版本管理及部署—部署", userName, "自动化部署-版本管理及部署", "执行:" + nodeName + "下运行管理系统自动安装", "0", request);
            throw new ValidException(e.getMessage());
        }
    }

    public void pushDeploy(HttpServletRequest request, AutomatedDeploymentEntity automatedDeploymentEntity, Node node, boolean rollbackBoolean) throws Exception {

        Services services = servicesService.findById(automatedDeploymentEntity.getSysId());
        InstallPathEntity installPathEntity = installPathService.findById(automatedDeploymentEntity.getInstallPathId());


        /*部署阶段:未部署:NotDeployed,部署中:Deploying,已部署:Deployed,不可部署:NotDeployAble,初次部署InitDeployAble*/
        automatedDeploymentEntity.setTypeStage("Deploying");
        AutomatedDeploymentLogEntity entity = new AutomatedDeploymentLogEntity();
        entity.setApiUrl(automatedDeploymentEntity.getApiUrl());
        entity.setApiMethod(automatedDeploymentEntity.getApiMethod());
        entity.setId(MyUtils.generate32Id());
        entity.setNodeId(node.getId());
        entity.setNodeName(node.getName());
        entity.setSysId(services.getId());
        entity.setSysName(services.getName());
        entity.setType(automatedDeploymentEntity.getType());
        if (rollbackBoolean) {
            entity.setType("rollback");
        }
        entity.setAutomatedPackPath(automatedDeploymentEntity.getAutomatedPackPath());
        entity.setVersionNumber(automatedDeploymentEntity.getVersionNumber());
        entity.setInstallPath(installPathEntity.getPath());
        entity.setInstallPathId(installPathEntity.getId());
        entity.setInstallPathOs(installPathEntity.getOs());
        entity.setBeginTime(Calendar.getInstance());
        entity.setAutomatedDeploymentEntityId(automatedDeploymentEntity.getId());
        tokenService.updateCreateIdAndTime(request, entity);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", entity.getId());
        parameters.put("installPath", installPathEntity.getPath());
//        if(!rollbackBoolean){
        String automatedPackPath = automatedDeploymentEntity.getAutomatedPackPath();

        String value = serviceHost + "/webadmin/service/file/" + automatedPackPath + "?type=" + automatedDeploymentEntity.getType()+"&nodeId="+automatedDeploymentEntity.getNodeId()+"&systemId="+automatedDeploymentEntity.getSysId()+"&versionNumber="+automatedDeploymentEntity.getVersionNumber();
        parameters.put("downloadUrl", value);
        String[] split = automatedPackPath.split("/");
        parameters.put("fileName", split[split.length - 1]);
//        }

        parameters.put("callbackUrl", serviceHost + callbackUrl);
        parameters.put("isRollBack", rollbackBoolean);

        entity.setApiBody(parameters.toString());
        automatedDeploymentLogService.save(entity);


        logger.info("执行方法：请求参数：" + parameters);

        automatedDeploymentEntity.setTypeStage("Deploying");
        automatedDeploymentDao.save(automatedDeploymentEntity);
        // dcrun的执行接口地址url
//            String url = "http://" + node.getIntranetIp() + autoDeploy;
        asyncService.pushHttp(automatedDeploymentEntity, parameters);
    }

    public void pushHttp(AutomatedDeploymentEntity automatedDeploymentEntity, Map<String, Object> parameters) throws Exception {
        httpService.setTimeout(10 * 60, 10 * 60, 10 * 60);
        String contentType = "application/json";
        switch (automatedDeploymentEntity.getApiMethod()) {
            case "get":
                httpService.get(automatedDeploymentEntity.getApiUrl(), null, parameters);
                break;
            case "post":
                httpService.post(automatedDeploymentEntity.getApiUrl(), null, parameters, contentType);
                break;
            case "put":
                httpService.put(automatedDeploymentEntity.getApiUrl(), null, parameters, contentType);
                break;
            case "delete":
                httpService.delete(automatedDeploymentEntity.getApiUrl(), null, parameters, contentType);
                break;
            default:
                throw new Exception("无效的请求方式:get/post/put/delete");
        }
    }

    public void rollback(String id, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        AutomatedDeploymentEntity automatedDeploymentEntity = automatedDeploymentDao.findById(id).orElseThrow(() -> new ValidException("找不到记录：" + id));
        Node node = nodeService.findById(automatedDeploymentEntity.getNodeId());
        String nodeName = node.getName();
        try {
            pushDeploy(request, automatedDeploymentEntity, node, true);
            operationLogService.setLog("版本管理及部署—回滚", userName, "自动化部署-版本管理及部署", "执行:" + nodeName + "下运行管理系统回滚", "1", request);
        } catch (Exception e) {
            operationLogService.setLog("版本管理及部署—回滚", userName, "自动化部署-版本管理及部署", "执行:" + nodeName + "下运行管理系统回滚", "0", request);
            throw new ValidException(e.getMessage());
        }
    }

}
