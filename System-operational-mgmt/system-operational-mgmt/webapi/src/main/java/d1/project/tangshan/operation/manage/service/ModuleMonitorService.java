package d1.project.tangshan.operation.manage.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.tangshan.operation.manage.dao.ModuleMonitorDao;
import d1.project.tangshan.operation.manage.entity.ModuleMonitorEntity;
import d1.project.tangshan.operation.manage.service.operations.log.OperationLogService;
import d1.project.tangshan.operation.manage.utils.ModuleMonitorType;
import d1.project.tangshan.operation.manage.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Buter
 * @date 2020/3/16 6:50
 */
@Service
public class ModuleMonitorService {
    private final ModuleMonitorDao moduleMonitorDao;
    private final TokenService tokenService;
    private final OperationLogService operationLogService;

    @Autowired
    public ModuleMonitorService(ModuleMonitorDao moduleMonitorDao, TokenService tokenService, OperationLogService operationLogService) {
        this.moduleMonitorDao = moduleMonitorDao;
        this.tokenService = tokenService;
        this.operationLogService = operationLogService;
    }

    public Page<ModuleMonitorEntity> findAll(JSONObject params, HttpServletRequest request) throws Exception {
        SpecificationBuilder<ModuleMonitorEntity> builder = new SpecificationBuilder<>();
        Specification specification = builder.init(params)
                .sEqual("type", "type")
                .sContain("nodeName", "nodeName")
                .dOrder("createTime").build();

        return moduleMonitorDao.findAll(specification, builder.getPageable());
    }

    public List<ModuleMonitorEntity> findAllByType(String type) throws Exception {
        JSONObject params = new JSONObject();
        params.put("type", type);
        SpecificationBuilder<ModuleMonitorEntity> builder = new SpecificationBuilder<>();
        Specification specification = builder.init(params).sEqual("type", "type")
                .aOrder("alarmWay").build();

        return this.moduleMonitorDao.findAll(specification);
    }

    public ModuleMonitorEntity findById(String id) throws Exception {
        return moduleMonitorDao.findById(id).orElseThrow(() -> new ValidException("??????????????????" + id));
    }

    public void insert(JSONObject params, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        // ?????????????????????????????????
        params.put("peopleNotified", params.getString("peopleNotified").replace("???", ","));
        ModuleMonitorEntity entity = JSON.toJavaObject(params, ModuleMonitorEntity.class);
        String name = "??????";
        String nodeName = entity.getNodeName();
        switch (entity.getType()) {
            case "performance":
                name = "??????????????????";
                break;
            case "data":
                name = "????????????";
                break;
            case "resources":
                name = "????????????";
                break;
            default:
                break;
        }
        try {
            if ("performance".equals(entity.getType()) && "account".equals(entity.getAlarmWay())) {
                MyUtils.throwMsg(moduleMonitorDao.existsByAlarmWayAndType("account", "performance"), "?????????????????????????????????");
            }
            entity.setId(MyUtils.generate32Id());
            tokenService.updateCreateIdAndTime(request, entity);
            moduleMonitorDao.save(entity);
            if ("performance".equals(entity.getType())) {
                operationLogService.setLog(name + "?????????", userName, "????????????-" + name, "??????????????????????????????" + name, "1", request);
            } else {
                operationLogService.setLog(name + "?????????", userName, "????????????-" + name, "?????????" + nodeName + "???????????????????????????" + name, "1", request);
            }

        } catch (Exception e) {
            if ("performance".equals(entity.getType())) {
                operationLogService.setLog(name + "?????????", userName, "????????????-" + name, "??????????????????????????????" + name, "0", request);
            } else {
                operationLogService.setLog(name + "?????????", userName, "????????????-" + name, "?????????" + nodeName + "???????????????????????????" + name, "0", request);
            }

            throw new ValidException(e.getMessage());
        }
    }


    public void update(JSONObject params, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String name = "??????";
        String nodeName = "??????";
        String type = "";
        try {
            String id = params.getString("id");

            ModuleMonitorEntity entity = moduleMonitorDao.findById(id).orElseThrow(() -> new ValidException("??????????????????" + id));
            type = entity.getType();
            switch (entity.getType()) {
                case "performance":
                    name = "??????????????????";
                    break;
                case "data":
                    name = "????????????";
                    break;
                case "resources":
                    name = "????????????";
                    break;
                default:
                    break;
            }
            nodeName = entity.getNodeName();
            if ("performance".equals(entity.getType()) &&!entity.getAlarmWay().equals(params.getString("alarmWay"))) {
                MyUtils.throwMsg(moduleMonitorDao.existsByAlarmWayAndType("account", "performance"), "?????????????????????????????????");
            }
            ModuleMonitorType monitorType = ModuleMonitorType.getByName(entity.getType());
            switch (monitorType) {
                case PERFORMANCE:
                    entity.setAccess(params.getInteger("access"));
                    break;
                case DATA:
                    entity.setRam(params.getLong("ram"));
                    entity.setRom(params.getLong("rom"));
                    break;
                case RESOURCES:
                    entity.setRam(params.getLong("ram"));
                    entity.setRom(params.getLong("rom"));
                    entity.setCpu(params.getInteger("cpu"));
                    break;
                default:
                    break;
            }
            entity.setAlarmWay(params.getString("alarmWay"));
            entity.setPeopleNotified(params.getString("peopleNotified").replace("???", ","));
            tokenService.updateUpdateIdAndTime(request, entity);
            moduleMonitorDao.save(entity);
            if ("performance".equals(type)) {
                operationLogService.setLog(name + "?????????", userName, "????????????-" + name, "??????????????????????????????" + name, "1", request);
            } else {
                operationLogService.setLog(name + "?????????", userName, "????????????-" + name, "?????????" + nodeName + "???????????????????????????" + name, "1", request);
            }

        } catch (Exception e) {
            if ("performance".equals(type)) {
                operationLogService.setLog(name + "?????????", userName, "????????????-" + name, "??????????????????????????????" + name, "0", request);
            } else {
                operationLogService.setLog(name + "?????????", userName, "????????????-" + name, "?????????" + nodeName + "???????????????????????????" + name, "0", request);
            }
            throw new ValidException(e.getMessage());
        }
    }


    public void delete(String id, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String name = "??????";
        String nodeName = "??????";
        String type = "";
        try {
            ModuleMonitorEntity entity = moduleMonitorDao.findById(id).orElseThrow(() -> new ValidException("??????????????????" + id));
            type = entity.getType();
            switch (type) {
                case "performance":
                    name = "??????????????????";
                    break;
                case "data":
                    name = "????????????";
                    break;
                case "resources":
                    name = "????????????";
                    break;
                default:
                    break;
            }
            nodeName = entity.getNodeName();
            moduleMonitorDao.delete(entity);
            if ("performance".equals(type)) {
                operationLogService.setLog(name + "?????????", userName, "????????????-" + name, "??????????????????????????????" + name, "1", request);
            } else {
                operationLogService.setLog(name + "?????????", userName, "????????????-" + name, "?????????" + nodeName + "???????????????????????????" + name, "1", request);
            }
        } catch (Exception e) {
            if ("performance".equals(type)) {
                operationLogService.setLog(name + "?????????", userName, "????????????-" + name, "??????????????????????????????" + name, "0", request);
            } else {
                operationLogService.setLog(name + "?????????", userName, "????????????-" + name, "?????????" + nodeName + "???????????????????????????" + name, "0", request);
            }
            throw new ValidException(e.getMessage());
        }
    }

    public ModuleMonitorEntity findByTypeAndNodeId(String type, String nodeId) {
        return this.moduleMonitorDao.findFirstByTypeAndNodeId(type, nodeId);
    }

    public ModuleMonitorEntity findByTypeAndNodeIdAndSysId(String type, String nodeId, String systemId) {
        return this.moduleMonitorDao.findFirstByTypeAndNodeIdAndSysId(type, nodeId, systemId);
    }
}
