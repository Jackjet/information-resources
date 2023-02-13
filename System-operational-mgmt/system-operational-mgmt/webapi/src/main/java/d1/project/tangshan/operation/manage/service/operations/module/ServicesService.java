package d1.project.tangshan.operation.manage.service.operations.module;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.tangshan.operation.manage.dao.operations.module.ServicesDao;
import d1.project.tangshan.operation.manage.entity.operations.module.Services;
import d1.project.tangshan.operation.manage.service.operations.log.OperationLogService;
import d1.project.tangshan.operation.manage.utils.MyUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author lin
 */
@Service
public class ServicesService {
    private final ServicesDao servicesDao;
    private final OperationLogService operationLogService;
    private final TokenService tokenService;

    public ServicesService(ServicesDao servicesDao, OperationLogService operationLogService, TokenService tokenService) {
        this.servicesDao = servicesDao;
        this.operationLogService = operationLogService;
        this.tokenService = tokenService;
    }


    public void insert(JSONObject model, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        Services services = MyUtils.model2Entity(model, Services.class);
        String name = services.getName();
        try {
            MyUtils.throwMsg(servicesDao.existsByName(services.getName()), "系统名称已存在");
            if (!StringUtils.isEmpty(services.getPort())) {
                MyUtils.throwMsg(servicesDao.existsByNodeIdAndPort(services.getNodeId(), services.getPort()), "该系统节点下端口已存在");
            }

            services.setId(MyUtils.generate32Id());
            tokenService.updateCreateIdAndTime(request, services);

            servicesDao.save(services);
            operationLogService.setLog("系统管理—添加", userName, "运维行为管理-组件管理-系统管理", "添加了一个系统:" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("系统管理—添加", userName, "运维行为管理-组件管理-系统管理", "添加了一个系统:" + name, "0", request);
            throw new ValidException(e.getMessage());
        }
    }

    public void delete(String id, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String name = "未知";
        try {
            MyUtils.throwMsg(StringUtils.isEmpty(id), "id不能为空");
            Services services = servicesDao.findById(id).orElseThrow(() -> new ValidException("该系统不存在"));
            name = services.getName();
            servicesDao.deleteById(id);
            operationLogService.setLog("系统管理—删除", userName, "运维行为管理-组件管理-系统管理", "删除了一个系统:" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("系统管理—删除", userName, "运维行为管理-组件管理-系统管理", "删除了一个系统:" + name, "0", request);
            throw new ValidException(e.getMessage());
        }
    }

    public void update(HttpServletRequest request, JSONObject model) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String name = "未知";
        try {
            Services services = MyUtils.model2Entity(model, Services.class);

            Services oldServices = servicesDao.findById(services.getId()).orElseThrow(() -> new ValidException("该系统不存在"));
            name=oldServices.getName();
            tokenService.updateUpdateIdAndTime(request, oldServices);
            if (!oldServices.getPort().equals(services.getPort())) {
                if (!StringUtils.isEmpty(services.getPort())) {
                    MyUtils.throwMsg(servicesDao.existsByNodeIdAndPort(oldServices.getNodeId(), services.getPort()), "该系统节点下端口已存在");
                }
                oldServices.setPort(services.getPort());
            }
            oldServices.setProcessName(services.getProcessName());
            oldServices.setSystemAndPlatform(services.getSystemAndPlatform());
            oldServices.setRemark(services.getRemark());
            servicesDao.save(oldServices);
            operationLogService.setLog("系统管理—编辑", userName, "运维行为管理-组件管理-系统管理", "编辑了一个系统:" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("系统管理—编辑", userName, "运维行为管理-组件管理-系统管理", "编辑了一个系统:" + name, "0", request);
            throw new ValidException(e.getMessage());
        }

    }

    public Page<Services> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<Services> builder = new SpecificationBuilder<>();
        Specification<Services> specification = builder.init(params)
                .sContain("name", "name")
                .sEqual("nodeId", "nodeId")
                .sContain("nodeName", "nodeName")
                .sContain("systemAndPlatform", "systemAndPlatform")
                .tBetween("createTime", "startTime", "endTime", "yyyy-MM-dd HH:mm:ss")
                .dOrder("createTime")
                .build();
        return servicesDao.findAll(specification, builder.getPageable());
    }

    public Services findById(String id) throws Exception {
        return servicesDao.findById(id).orElseThrow(() -> new ValidException("该系统不存在"));
    }

    public List<Services> findAllServices() {
        return servicesDao.findAll();
    }

    public List<Services> findAllServicesByNodeId(String nodeId) {
        return servicesDao.findAllByNodeIdOrderByCreateTimeDesc(nodeId);
    }
}
