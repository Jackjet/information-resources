package d1.project.tangshan.operation.manage.service.share;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.project.tangshan.operation.manage.entity.ResourceMonitorEntity;
import d1.project.tangshan.operation.manage.entity.operations.module.Node;
import d1.project.tangshan.operation.manage.model.DateAndNumber;
import d1.project.tangshan.operation.manage.model.ResourceMonitorEntityVm;
import d1.project.tangshan.operation.manage.model.SystemVisitNum;
import d1.project.tangshan.operation.manage.model.UtilizationVm;
import d1.project.tangshan.operation.manage.service.LoginInfoService;
import d1.project.tangshan.operation.manage.service.ResourceMonitorService;
import d1.project.tangshan.operation.manage.service.operations.log.AuditLogService;
import d1.project.tangshan.operation.manage.service.operations.module.NodeService;
import d1.project.tangshan.operation.manage.utils.MyUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author lin
 */
@Service
public class ShareService {
    private final ResourceMonitorService resourceMonitorService;
    private final NodeService nodeService;
    private final LoginInfoService loginInfoService;
    private final AuditLogService auditLogService;

    public ShareService(ResourceMonitorService resourceMonitorService, NodeService nodeService, LoginInfoService loginInfoService, AuditLogService auditLogService) {
        this.resourceMonitorService = resourceMonitorService;
        this.nodeService = nodeService;
        this.loginInfoService = loginInfoService;
        this.auditLogService = auditLogService;
    }


    public UtilizationVm getUtilization(String nodeId) throws Exception {
        UtilizationVm utilizationVm = new UtilizationVm();
        Node node = nodeService.findById(nodeId);
        ResourceMonitorEntityVm monitorEntity = resourceMonitorService.findFirstByIntranetIpOrderByLogTimeDesc(node.getIntranetIp());
        if (monitorEntity == null) {
            utilizationVm.setConfiguration(node.getConfiguration());
            utilizationVm.setCpu(0);
            utilizationVm.setRam(0L);
            utilizationVm.setRom(0L);
            return utilizationVm;
        }
        utilizationVm.setConfiguration(node.getConfiguration());
        utilizationVm.setCpu(monitorEntity.getCpu());
        utilizationVm.setRam(monitorEntity.getRam());
        utilizationVm.setRom(monitorEntity.getRom());
        return utilizationVm;
    }

    public JSONObject getUserNum() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = sdf.format(Calendar.getInstance().getTime());
        Calendar nowStart = MyUtils.stringToCalendar(nowDate + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        Calendar nowEnd = MyUtils.stringToCalendar(nowDate + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        Long nowNum = loginInfoService.countUserNumByLoginTime(nowStart, nowEnd);

        Calendar seven = Calendar.getInstance();
        seven.add(Calendar.DAY_OF_MONTH, -7);
        String sevenDate = sdf.format(seven.getTime());
        Calendar sevenStart = MyUtils.stringToCalendar(sevenDate + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        Calendar sevenEnd = MyUtils.stringToCalendar(nowDate + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        Long sevenNum = loginInfoService.countUserNumByLoginTime(sevenStart, sevenEnd);

        Calendar thirty = Calendar.getInstance();
        thirty.add(Calendar.DAY_OF_MONTH, -30);
        String thirtyDate = sdf.format(thirty.getTime());
        Calendar thirtyStart = MyUtils.stringToCalendar(thirtyDate + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        Calendar thirtyEnd = MyUtils.stringToCalendar(nowDate + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        Long thirtyNum = loginInfoService.countUserNumByLoginTime(thirtyStart, thirtyEnd);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("now", nowNum);
        jsonObject.put("seven", sevenNum);
        jsonObject.put("thirty", thirtyNum);

        return jsonObject;

    }

    public JSONObject getSystemVisit(String startTime, String endTime) {
        Calendar start = MyUtils.stringToCalendar(startTime + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        Calendar end = MyUtils.stringToCalendar(endTime + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        List<Map<String, Object>> list = auditLogService.countSystemNum(start, end);
        String json = JSON.toJSON(list).toString();
        List<SystemVisitNum> oldList = JSON.parseArray(json, SystemVisitNum.class);
        String[] system = {"信息安全与支撑平台", "信息资源共享交换", "共享网站", "数据开放网站", "中心库管理平台", "基础数据库", "运行管理平台", "审计日志系统", "大数据平台","微服务平台","集成门户"};
        List<SystemVisitNum> newList = new ArrayList<>();
        for (String name : system) {
            SystemVisitNum visitNum = new SystemVisitNum();
            visitNum.setName(name);
            visitNum.setNumber(0L);
            newList.add(visitNum);
        }

        for (SystemVisitNum visitNum : newList) {
            for (SystemVisitNum visitNum1 : oldList) {
                if (visitNum.getName().equals(visitNum1.getName())) {
                    visitNum.setNumber(visitNum1.getNumber());
                }
            }
        }
        List<String> names = new ArrayList<>();
        List<Long> nums = new ArrayList<>();
        for (SystemVisitNum visitNum : newList) {
            names.add(visitNum.getName());
            nums.add(visitNum.getNumber());
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", names);
        jsonObject.put("num", nums);

        return jsonObject;
    }

    public JSONObject getLoginNum(String startTime, String endTime) throws Exception {
        Calendar start = MyUtils.stringToCalendar(startTime + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        Calendar end = MyUtils.stringToCalendar(endTime + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        List<Map<String, Object>> list = loginInfoService.countLoginNumByDay(start, end);
        String json = JSON.toJSON(list).toString();
        List<DateAndNumber> oldList = JSON.parseArray(json, DateAndNumber.class);
        List<String> allDays = MyUtils.getDayBetween(startTime, endTime);
        List<DateAndNumber> newList = new ArrayList<>();
        for (String day : allDays) {
            DateAndNumber dateAndNumber = new DateAndNumber();
            dateAndNumber.setDate(day);
            dateAndNumber.setNumber(0L);
            newList.add(dateAndNumber);
        }

        for (DateAndNumber dateAndNumber : newList) {
            for (DateAndNumber dateAndNumber1 : oldList) {
                if (dateAndNumber.getDate().equals(dateAndNumber1.getDate())) {
                    dateAndNumber.setNumber(dateAndNumber1.getNumber());
                }
            }
        }
        List<String> date = new ArrayList<>();
        List<Long> nums = new ArrayList<>();
        for (DateAndNumber dateAndNumber : newList) {
            date.add(dateAndNumber.getDate());
            nums.add(dateAndNumber.getNumber());
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", date);
        jsonObject.put("num", nums);

        return jsonObject;
    }

    public List<Node> findAllNodes() {
        return nodeService.findAllNodes();
    }
}
