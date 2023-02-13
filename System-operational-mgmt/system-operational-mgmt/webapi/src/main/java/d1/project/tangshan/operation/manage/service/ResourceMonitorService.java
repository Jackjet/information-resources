package d1.project.tangshan.operation.manage.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.tangshan.operation.manage.dao.ResourceMonitorDao;
import d1.project.tangshan.operation.manage.entity.ModuleMonitorEntity;
import d1.project.tangshan.operation.manage.entity.ResourceMonitorEntity;
import d1.project.tangshan.operation.manage.entity.operations.module.Node;
import d1.project.tangshan.operation.manage.model.ResourceMonitorEntityVm;
import d1.project.tangshan.operation.manage.service.operations.module.NodeService;
import d1.project.tangshan.operation.manage.utils.MyUtils;
import net.dcrun.component.email.IEmailService;
import net.dcrun.component.sms.ucpass.ISmsUcpassService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author Buter
 * @date 2020/3/16 6:50
 */
@Service
public class ResourceMonitorService {
    private final ResourceMonitorDao resourceMonitorDao;
    private final NodeService nodeService;
    private final TokenService tokenService;
    private final ModuleMonitorService moduleMonitorService;
    private final ISmsUcpassService smsUcpassService;
    private final IEmailService emailService;

    @Value("${email.host}")
    private String host;
    @Value("${email.from}")
    private String from;
    @Value("${email.account}")
    private String account;
    @Value("${email.password}")
    private String password;
    @Value("${email.ssl}")
    private String ssl;
    @Value("${email.port}")
    private String port;

    public ResourceMonitorService(ResourceMonitorDao resourceMonitorDao, NodeService nodeService, TokenService tokenService, ModuleMonitorService moduleMonitorService, ISmsUcpassService smsUcpassService, IEmailService emailService) {
        this.resourceMonitorDao = resourceMonitorDao;
        this.nodeService = nodeService;
        this.tokenService = tokenService;
        this.moduleMonitorService = moduleMonitorService;
        this.smsUcpassService = smsUcpassService;
        this.emailService = emailService;
    }

    public Page<ResourceMonitorEntity> findAll(JSONObject params) throws Exception {
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        SpecificationBuilder<ResourceMonitorEntity> builder = new SpecificationBuilder<>();
        Specification specification = builder.init(params)
                .sContain("nodeName", "nodeName")
                .tBetween("logTime", "beginTime", "endTime", dateFormat)
                .dOrder("logTime").build();

        return this.resourceMonitorDao.findAll(specification, builder.getPageable());
    }

    public JSONObject resourceMonitoring(JSONObject params) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", params.get("nodeName"));
        jsonObject.put("page", params.get("page"));
        jsonObject.put("size", params.get("size"));
        Page<Node> all = nodeService.findAll(jsonObject);

        String beginTime = params.getString("beginTime")+" 00:00:00";
        String endTime = params.getString("endTime")+" 23:59:59";
        List<Map<String, Object>> maps = new ArrayList<>();
        for (Node node : all) {
            Calendar start = MyUtils.stringToCalendar(beginTime, "yyyy-MM-dd HH:mm:ss");
            Calendar end = MyUtils.stringToCalendar(endTime, "yyyy-MM-dd HH:mm:ss");
            List<Map<String, Object>> list = resourceMonitorDao.findByDay(node.getIntranetIp(), start, end);

            String json = JSON.toJSON(list).toString();
            List<ResourceMonitorEntityVm> entities = JSON.parseArray(json, ResourceMonitorEntityVm.class);

            List<String> datetime = new ArrayList<>();
            List<String> allTime = MyUtils.getDayBetween(beginTime, endTime);
            List<Long> ram = new ArrayList<>();
            List<Long> rom = new ArrayList<>();
            List<Integer> cpu = new ArrayList<>();

            Map<String, Object> stringStringMap1 = new HashMap<>(1);
            stringStringMap1.put("datetime", datetime);
            stringStringMap1.put("ram", ram);
            stringStringMap1.put("rom", rom);
            stringStringMap1.put("cpu", cpu);

            List<ResourceMonitorEntityVm> entities1 = new ArrayList<>();
            for (String time : allTime) {
                ResourceMonitorEntityVm vm = new ResourceMonitorEntityVm();
                vm.setDate(time);
                vm.setCpu(0);
                vm.setRam(0L);
                vm.setRom(0L);
                entities1.add(vm);
            }
            for (ResourceMonitorEntityVm vm : entities1) {
                for (ResourceMonitorEntityVm vm1 : entities) {
                    if (vm.getDate().equals(vm1.getDate())) {
                        if(StringUtils.isEmpty(vm1.getRom())){
                            vm.setRom(0L);
                        }else{
                            vm.setRom(vm1.getRom());
                        }
                        if(StringUtils.isEmpty(vm1.getRam())){
                            vm.setRam(0L);
                        }else{
                            vm.setRam(vm1.getRam());
                        }
                        if(StringUtils.isEmpty(vm1.getCpu())){
                            vm.setCpu(0);
                        }else{
                            vm.setCpu(vm1.getCpu());
                        }
                    }
                }
            }
            for (ResourceMonitorEntityVm vm : entities1) {
                datetime.add(vm.getDate());
                ram.add(vm.getRam());
                rom.add(vm.getRom());
                cpu.add(vm.getCpu());
            }

            Map<String, Object> stringStringMap = new HashMap<>(3);
            stringStringMap.put("id", node.getId());
            stringStringMap.put("name", node.getName());
            stringStringMap.put("data", stringStringMap1);

            maps.add(stringStringMap);
        }

        JSONObject result = new JSONObject();
        result.put("lists", maps);
        result.put("number", all.getTotalElements());
        return result;
    }

    public void insert(JSONObject params) throws Exception {
        ResourceMonitorEntity entity = JSON.toJavaObject(params, ResourceMonitorEntity.class);
        Node node = this.nodeService.findByIntranetIp(entity.getIntranetIp());
        if (node == null) {
            throw new Exception("节点信息不存在"+entity.getIntranetIp());
        }
        entity.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        Calendar calendar = Calendar.getInstance();
        entity.setLogTime(calendar);
        entity.setCreateTime(calendar);
        this.resourceMonitorDao.save(entity);
    }

    public List<ResourceMonitorEntity> findByNodeIdAndCreateTime(JSONObject params) throws Exception {
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        SpecificationBuilder<ResourceMonitorEntity> builder = new SpecificationBuilder<>();
        Specification specification = builder.init(params)
                .sContain("intranetIp", "intranetIp")
                .tBetween("createTime", "startTime", "endTime", dateFormat)
                .dOrder("createTime").build();

        return this.resourceMonitorDao.findAll(specification);
    }

    public ResourceMonitorEntityVm findFirstByIntranetIpOrderByLogTimeDesc(String nodeId){
        Map<String, Object> result = resourceMonitorDao.findFirstByIntranetIpOrderByLogTimeDesc(nodeId);
        ResourceMonitorEntityVm resourceMonitorEntityVm = MyUtils.model2Entity(result, ResourceMonitorEntityVm.class);
        return resourceMonitorEntityVm;
    }
}
