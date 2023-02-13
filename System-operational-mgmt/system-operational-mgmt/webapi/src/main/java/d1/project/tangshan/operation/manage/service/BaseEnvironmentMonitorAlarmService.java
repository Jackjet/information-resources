package d1.project.tangshan.operation.manage.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.TokenService;
import d1.project.tangshan.operation.manage.dao.BaseEnvironmentMonitorAlarmDao;
import d1.project.tangshan.operation.manage.dao.BaseEnvironmentMonitorDao;
import d1.project.tangshan.operation.manage.dao.operations.log.OperationLogDao;
import d1.project.tangshan.operation.manage.entity.BaseEnvironmentMonitorAlarmEntity;
import d1.project.tangshan.operation.manage.entity.operations.module.Node;
import d1.project.tangshan.operation.manage.entity.operations.module.Services;
import d1.project.tangshan.operation.manage.model.BaseEnvironmentMonitorVm;
import d1.project.tangshan.operation.manage.model.ModuleCountVm;
import d1.project.tangshan.operation.manage.model.NodeStatusAnalysisVm;
import d1.project.tangshan.operation.manage.model.SystemMonitorEntityVm;
import d1.project.tangshan.operation.manage.service.operations.log.OperationLogService;
import d1.project.tangshan.operation.manage.service.operations.module.NodeService;
import d1.project.tangshan.operation.manage.service.operations.module.ServicesService;
import d1.project.tangshan.operation.manage.utils.AlarmModule;
import d1.project.tangshan.operation.manage.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Buter
 * @date 2020/3/16 6:50
 */
@Service
public class BaseEnvironmentMonitorAlarmService {

    private final BaseEnvironmentMonitorAlarmDao baseEnvironmentMonitorDao;
    private final TokenService tokenService;
    private final BaseEnvironmentMonitorDao environmentMonitorDao;
    private final NodeService nodeService;
    private final BaseEnvironmentMonitorAlarmDao baseEnvironmentMonitorAlarmDao;
    private final OperationLogDao operationLogDao;
    private final OperationLogService operationLogService;


    @Autowired
    SystemMonitorService systemMonitorService;

    @Autowired
    ServicesService servicesService;

    @Autowired
    public BaseEnvironmentMonitorAlarmService(BaseEnvironmentMonitorAlarmDao baseEnvironmentMonitorDao, TokenService tokenService, BaseEnvironmentMonitorDao environmentMonitorDao, NodeService nodeService, BaseEnvironmentMonitorAlarmDao baseEnvironmentMonitorAlarmDao, OperationLogDao operationLogDao, OperationLogService operationLogService) {
        this.baseEnvironmentMonitorDao = baseEnvironmentMonitorDao;
        this.tokenService = tokenService;
        this.environmentMonitorDao = environmentMonitorDao;
        this.nodeService = nodeService;
        this.baseEnvironmentMonitorAlarmDao = baseEnvironmentMonitorAlarmDao;
        this.operationLogDao = operationLogDao;
        this.operationLogService = operationLogService;
    }

    public BaseEnvironmentMonitorAlarmEntity find(JSONObject params, HttpServletRequest request) throws Exception {
        String createById = tokenService.getUserByToken(request).getString("id");
        String type = params.getString("type");
        return baseEnvironmentMonitorDao.findByCreateByIdAndType(createById, type).orElse(new BaseEnvironmentMonitorAlarmEntity());
    }

    public void update(JSONObject params, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String type = params.getString("type");
        String name = "未知";
        switch (type) {
            case "environment":
                name = "基础环境监控";
                break;
            case "system":
                name = "系统监控";
                break;
            default:
                break;
        }
        try {
            // 手机号和邮箱的中文逗号替换成英文逗号
            params.put("email", params.getString("email").replace("，", ","));
            params.put("mobile", params.getString("mobile").replace("，", ","));


            String nodeId = params.getString("nodeId");
            String systemId = params.getString("systemId");
            BaseEnvironmentMonitorAlarmEntity entity = null;
            if (AlarmModule.ENVIRONMENT.getName().equals(type)) {
                entity = baseEnvironmentMonitorDao.findFirstByNodeIdAndType(nodeId, type);
            } else {
                entity = baseEnvironmentMonitorDao.findFirstByNodeIdAndSystemIdAndType(nodeId, systemId, type);
            }
            if (entity == null) {
                entity = JSON.toJavaObject(params, BaseEnvironmentMonitorAlarmEntity.class);
                entity.setId(MyUtils.generate32Id());
                tokenService.updateCreateIdAndTime(request, entity);
            } else {
                entity.setEmail(params.getString("email"));
                entity.setMobile(params.getString("mobile"));
                tokenService.updateUpdateIdAndTime(request, entity);
            }
            baseEnvironmentMonitorDao.save(entity);

            operationLogService.setLog(name + "—设置预警", userName, "基础环境监控-" + name, "设置了" + name + "预警", "1", request);
        } catch (Exception e) {
            operationLogService.setLog(name + "—设置预警", userName, "基础环境监控-" + name, "设置了" + name + "预警", "0", request);
            throw new ValidException(e.getMessage());
        }
    }

    /**
     * 节点状态分析
     *
     * @param params 查询条件
     * @return void
     * @author Kikki  2020/6/24 15:18
     */
    public JSONObject nodeStatusAnalysis(JSONObject params) throws Exception {
        /*初始化数据*/
        String beginTime = params.getString("beginTime");
        String endTime = params.getString("endTime");

        List<String> monthBetween = MyUtils.getMonthBetween(beginTime, endTime);
        List<Map<String, Object>> maps = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", params.get("nodeName"));
        jsonObject.put("startTime", params.get("beginTime"));
        jsonObject.put("page", params.get("page"));
        jsonObject.put("size", params.get("size"));
        /*节点查询数据*/
        Page<Node> all = nodeService.findAll(jsonObject);
        /*节点拼接数据*/
        all.forEach(node -> {
            String id = node.getId();
            String name = node.getName();
            List<Map<String, Object>> result = environmentMonitorDao.nodeStatusAnalysis(id, beginTime + " 00:00:00", endTime + " 23:59:59");
            List<NodeStatusAnalysisVm> nodeStatusAnalysisVms = MyUtils.listEntity2Model(result, NodeStatusAnalysisVm.class);
            Map<String, Object> stringStringMap1 = new HashMap<>(1);
            long[] normalList = new long[monthBetween.size()];
            long[] unusualList = new long[monthBetween.size()];
            stringStringMap1.put("month", monthBetween);
            stringStringMap1.put("normal", normalList);
            stringStringMap1.put("unusual", unusualList);

            nodeStatusAnalysisVms.forEach(nodeStatusAnalysisVm -> {
                int i = monthBetween.indexOf(nodeStatusAnalysisVm.getMonth());
                long[] unusual;
                if ("正常".equals(nodeStatusAnalysisVm.getStatus())) {
                    unusual = (long[]) stringStringMap1.get("normal");
                } else {
                    unusual = (long[]) stringStringMap1.get("unusual");
                }
                unusual[i] += nodeStatusAnalysisVm.getCount();
            });
            Map<String, Object> stringObjectMap = new HashMap<>();
            stringObjectMap.put("id", id);
            stringObjectMap.put("name", name);
            stringObjectMap.put("data", stringStringMap1);
            maps.add(stringObjectMap);

        });

        JSONObject result = new JSONObject();
        result.put("lists", maps);
        result.put("number", all.getTotalElements());
        return result;
    }

    /**
     * 系统状态分析
     *
     * @param params 查询条件
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @author Kikki  2020/6/26 19:37
     */
    public JSONObject systemStatusAnalysis(JSONObject params) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nodeName", params.get("nodeName"));
        jsonObject.put("name", params.get("systemName"));
        jsonObject.put("startTime", params.get("beginTime"));
        jsonObject.put("page", params.get("page"));
        jsonObject.put("size", params.get("size"));
        Page<Services> all = servicesService.findAll(jsonObject);

        String beginTime = params.getString("beginTime");
        String endTime = params.getString("endTime");

        List<Map<String, Object>> maps = new ArrayList<>();
        for (Services services : all) {
            Calendar start = MyUtils.stringToCalendar(beginTime + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
            Calendar end = MyUtils.stringToCalendar(endTime + " 23:59:59", "yyyy-MM-dd HH:mm:ss");

            List<Map<String, Object>> list = systemMonitorService.findByDay(services.getId(), start, end);

            System.out.println(JSONObject.toJSONString(list));
            String json = JSON.toJSON(list).toString();
            List<SystemMonitorEntityVm> entities = JSON.parseArray(json, SystemMonitorEntityVm.class);

            List<String> allTime = MyUtils.getDayBetween(beginTime, endTime);
            List<String> datetime = new ArrayList<>();
            List<String> ram = new ArrayList<>();
            List<String> storage = new ArrayList<>();

            Map<String, Object> stringStringMap1 = new HashMap<>(1);
            stringStringMap1.put("datetime", datetime);
            stringStringMap1.put("memorySource", ram);
            stringStringMap1.put("dataSource", storage);

            List<SystemMonitorEntityVm> entities1 = new ArrayList<>();
            for (String time : allTime) {
                SystemMonitorEntityVm vm = new SystemMonitorEntityVm();
                vm.setDate(time);
                vm.setDataSource("0");
                vm.setMemorySource("0");
                entities1.add(vm);
            }
            for (SystemMonitorEntityVm vm : entities1) {
                for (SystemMonitorEntityVm vm1 : entities) {
                    if (vm.getDate().equals(vm1.getDate())) {
                        vm.setMemorySource(vm1.getMemorySource());
                        vm.setDataSource(vm1.getDataSource());
                    }
                }
            }
            for (SystemMonitorEntityVm vm : entities1) {
                datetime.add(vm.getDate());
                ram.add(vm.getMemorySource());
                storage.add(vm.getDataSource());
            }

            Map<String, Object> stringStringMap = new HashMap<>(3);
            stringStringMap.put("id", services.getId());
            stringStringMap.put("name", services.getName());
            stringStringMap.put("nodeName", services.getNodeName());
            stringStringMap.put("nodeId", services.getNodeId());
            stringStringMap.put("systemAndPlatform", services.getSystemAndPlatform());
            stringStringMap.put("data", stringStringMap1);

            maps.add(stringStringMap);
        }

        JSONObject result = new JSONObject();
        result.put("lists", maps);
        result.put("number", all.getTotalElements());
        return result;
    }

    /**
     * 添加数据
     *
     * @param params
     */
    public void insert(HttpServletRequest request, JSONObject params) throws Exception {
        String nodeId = params.getString("nodeId");
        String nodeName = params.getString("nodeName");
        String mobile = params.getString("mobile");
        String email = params.getString("email");

        Node node = nodeService.findById(nodeId);
        if (node == null) {
            throw new Exception("节点信息不存在");
        }
        if (mobile.contains("，")) {
            mobile = mobile.replace("，", ",");
        }
        if (email.contains("，")) {
            email = email.replace("，", ",");
        }

        BaseEnvironmentMonitorAlarmEntity baseEnvironmentMonitorAlarmEntity = new BaseEnvironmentMonitorAlarmEntity();

        baseEnvironmentMonitorAlarmEntity.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        baseEnvironmentMonitorAlarmEntity.setNodeId(nodeId);
        baseEnvironmentMonitorAlarmEntity.setNodeName(nodeName);
        baseEnvironmentMonitorAlarmEntity.setMobile(mobile);
        baseEnvironmentMonitorAlarmEntity.setEmail(email);

        this.tokenService.updateCreateIdAndTime(request, baseEnvironmentMonitorAlarmEntity);
        this.tokenService.updateUpdateIdAndTime(request, baseEnvironmentMonitorAlarmEntity);

        this.baseEnvironmentMonitorAlarmDao.save(baseEnvironmentMonitorAlarmEntity);
    }


    public BaseEnvironmentMonitorAlarmEntity findByNodeIdAndType(String nodeId, String type) {
        return this.baseEnvironmentMonitorAlarmDao.findFirstByNodeIdAndType(nodeId, type);
    }

    public BaseEnvironmentMonitorAlarmEntity findByNodeIdAndSystemIdAndType(String nodeId, String systemId, String type) {
        return this.baseEnvironmentMonitorAlarmDao.findFirstByNodeIdAndSystemIdAndType(nodeId, systemId, type);
    }

    public Page<ModuleCountVm> count(JSONObject params) throws Exception {
        // 统计单位
        String unit = params.getString("unit");
        String name = params.getString("name");
        String moduleName = params.getString("moduleName");
        String startTime = params.getString("startTime");
        String endTime = params.getString("endTime");
        Integer page = params.getInteger("page");
        Integer size = params.getInteger("size");

        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        Calendar start = MyUtils.stringToCalendar(startTime, dateFormat);
        Calendar end = MyUtils.stringToCalendar(endTime, dateFormat);

        List<Map<String, Object>> list = new ArrayList<>();
        Pageable pageable = MyUtils.getPageable(page, size, null);
        Long count = 0L;
        switch (unit) {
            case "hour":
                list = operationLogDao.findByHour(name, moduleName, start, end, pageable);
                count = operationLogDao.countByHour(name, moduleName, start, end);
                break;
            case "day":
                list = operationLogDao.findByDay(name, moduleName, start, end, pageable);
                count = operationLogDao.countByDay(name, moduleName, start, end);
                break;
            case "month":
                list = operationLogDao.findByMonth(name, moduleName, start, end, pageable);
                count = operationLogDao.countByMonth(name, moduleName, start, end);
                break;
            case "week":
                list = operationLogDao.findByWeek(name, moduleName, start, end, pageable);
                count = operationLogDao.countByWeek(name, moduleName, start, end);
                List<Map<String, Object>> newMaps = new ArrayList<>();
                for (Map<String, Object> map : list) {
                    Map<String, Object> newMap = new HashMap<>(map);
                    String date = newMap.get("date").toString();
                    newMap.put("date", date.substring(0, 7) + "第" + MyUtils.getWeekOfMonth(date) + "周");
                    newMaps.add(newMap);
                }
                list = newMaps;
                break;
            default:
                throw new ValidException("统计单位错误");
        }
        List<ModuleCountVm> moduleCountVms = JSON.parseArray(JSON.toJSONString(list), ModuleCountVm.class);
        return new PageImpl<>(moduleCountVms, pageable, count);
    }
}
