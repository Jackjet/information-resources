package d1.project.tangshan.operation.manage.service;

import com.alibaba.fastjson.JSONObject;
import d1.project.tangshan.operation.manage.dao.*;
import d1.project.tangshan.operation.manage.dao.operations.module.DatabaseDao;
import d1.project.tangshan.operation.manage.dao.operations.module.NodeDao;
import d1.project.tangshan.operation.manage.dao.operations.module.ServicesDao;
import d1.project.tangshan.operation.manage.dao.remote.DatabaseMonitoringDao;
import d1.project.tangshan.operation.manage.entity.*;
import d1.project.tangshan.operation.manage.entity.database.DatabaseMonitorLog;
import d1.project.tangshan.operation.manage.entity.database.DatabaseMonitoring;
import d1.project.tangshan.operation.manage.entity.operations.module.Database;
import d1.project.tangshan.operation.manage.entity.operations.module.Node;
import d1.project.tangshan.operation.manage.entity.operations.module.Services;
import d1.project.tangshan.operation.manage.model.BaseEnvironmentMonitorVm;
import d1.project.tangshan.operation.manage.model.ResourceMonitorEntityVm;
import d1.project.tangshan.operation.manage.utils.AlarmWay;
import d1.project.tangshan.operation.manage.utils.ModuleMonitorType;
import d1.project.tangshan.operation.manage.utils.MyUtils;
import d1.project.tangshan.operation.manage.utils.StatusType;
import net.dcrun.component.email.IEmailService;
import net.dcrun.component.sms.ucpass.ISmsUcpassService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Service
public class WarningService {
    private final NodeDao nodeDao;
    private final ServicesDao servicesDao;
    private final BaseEnvironmentMonitorDao baseEnvironmentMonitorDao;
    private final SystemMonitorDao systemMonitorDao;
    private final ResourceMonitorDao resourceMonitorDao;
    private final BaseEnvironmentMonitorAlarmDao baseEnvironmentMonitorAlarmDao;
    private final ModuleMonitorDao moduleMonitorDao;
    private final DatabaseMonitorLogDao databaseMonitorLogDao;
    private final DatabaseMonitoringDao databaseMonitoringDao;
    private final DatabaseDao databaseDao;
    private final ReceiverService receiverService;

    private final ISmsUcpassService smsUcpassService;
    private final IEmailService emailService;

    private final Logger logger = LoggerFactory.getLogger(WarningService.class);
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

    public WarningService(NodeDao nodeDao, ServicesDao servicesDao, BaseEnvironmentMonitorDao baseEnvironmentMonitorDao, SystemMonitorDao systemMonitorDao, ResourceMonitorDao resourceMonitorDao, BaseEnvironmentMonitorAlarmDao baseEnvironmentMonitorAlarmDao, ModuleMonitorDao moduleMonitorDao, DatabaseMonitorLogDao databaseMonitorLogDao, DatabaseMonitoringDao databaseMonitoringDao, DatabaseDao databaseDao, ReceiverService receiverService, ISmsUcpassService smsUcpassService, IEmailService emailService) {
        this.nodeDao = nodeDao;
        this.servicesDao = servicesDao;
        this.baseEnvironmentMonitorDao = baseEnvironmentMonitorDao;
        this.systemMonitorDao = systemMonitorDao;
        this.resourceMonitorDao = resourceMonitorDao;
        this.baseEnvironmentMonitorAlarmDao = baseEnvironmentMonitorAlarmDao;
        this.moduleMonitorDao = moduleMonitorDao;
        this.databaseMonitorLogDao = databaseMonitorLogDao;
        this.databaseMonitoringDao = databaseMonitoringDao;
        this.databaseDao = databaseDao;
        this.receiverService = receiverService;
        this.smsUcpassService = smsUcpassService;
        this.emailService = emailService;
    }

    /**
     * ????????????????????????
     */
    public void baseEnvironmentWarning() {
        List<Node> nodes = nodeDao.findAll();
        for (Node node : nodes) {
            Map<String, Object> result = baseEnvironmentMonitorDao.firstByIntranetIpOrderByCreateTimeDesc(node.getIntranetIp());
            BaseEnvironmentMonitorVm baseEnvironmentMonitorEntity = MyUtils.model2Entity(result, BaseEnvironmentMonitorVm.class);
            if (baseEnvironmentMonitorEntity != null) {
                BaseEnvironmentMonitorAlarmEntity baseEnvironmentMonitorAlarmEntity =
                        baseEnvironmentMonitorAlarmDao.findFirstByNodeIdAndType(baseEnvironmentMonitorEntity.getNodeId(), "environment");

                if (baseEnvironmentMonitorAlarmEntity != null) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    String items = "";
                    if (baseEnvironmentMonitorEntity.getStatus().equals(StatusType.ABNORMAL.getName())) {
                        items += "??????";
                    }
                    if (baseEnvironmentMonitorEntity.getNetwork().equals(StatusType.ABNORMAL.getName())) {
                        if (!items.equals("")) {
                            items += "???";
                        }
                        items += "??????";
                    }
                    if (baseEnvironmentMonitorEntity.getDb().equals(StatusType.ABNORMAL.getName())) {
                        if (!items.equals("")) {
                            items += "???";
                        }
                        items += "?????????";
                    }
                    if (baseEnvironmentMonitorEntity.getSystemSoftware().contains(StatusType.ABNORMAL.getName())) {
                        if (!items.equals("")) {
                            items += "???";
                        }
                        items += "????????????";
                    }

                    if (!"".equals(items)) {
                        String message = baseEnvironmentMonitorEntity.getNodeName() + "???" + items + "??? " +
                                format.format(baseEnvironmentMonitorEntity.getLogTime().getTime());
                        try {
                            for (String mobile : baseEnvironmentMonitorAlarmEntity.getMobile().split(",")) {
                                receiverService.sendSms(mobile, "553579", message);
                                //smsUcpassService.send(message, mobile, "553579");
                            }

                        } catch (Exception e) {
                            logger.error("?????????????????????" + e.getMessage());
                        }

                        String emailMessage = "?????????" + message + "???????????????????????????????????????";
                        try {
                            JSONObject json = new JSONObject();
                            json.put("host", host);
                            json.put("from", from);
                            json.put("to", baseEnvironmentMonitorAlarmEntity.getEmail().split(","));
                            json.put("account", account);
                            json.put("password", password);
                            json.put("ssl", ssl);
                            json.put("port", port);

                            JSONObject content = new JSONObject();
                            content.put("subject", "??????????????????");
                            content.put("content", emailMessage);

                            json.put("content", content);
                            //emailService.send(json.toJSONString());
                            receiverService.sendEmail(baseEnvironmentMonitorAlarmEntity.getEmail().split(","), "??????????????????", emailMessage);
                        } catch (Exception e) {
                            logger.error("?????????????????????" + e.getMessage());
                        }
                    }
                }
            }
        }
    }

    /**
     * ????????????
     */
    public void systemWarning() {
        List<Services> services = servicesDao.findAll();
        for (Services service : services) {
            SystemMonitorEntity systemMonitorEntity = systemMonitorDao.findFirstByNodeIdAndSystemIdOrderByCreateTimeDesc(service.getNodeId(), service.getId());
            if (systemMonitorEntity != null) {
                BaseEnvironmentMonitorAlarmEntity baseEnvironmentMonitorAlarmEntity = baseEnvironmentMonitorAlarmDao.findFirstByNodeIdAndSystemIdAndType(service.getNodeId(), service.getId(), "system");
                if (baseEnvironmentMonitorAlarmEntity != null && systemMonitorEntity.getStatus().equals(StatusType.STOP.getName())) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String message = systemMonitorEntity.getNodeName() + "??????"
                            + systemMonitorEntity.getSystem()
                            + "???" + format.format(systemMonitorEntity.getLogTime().getTime());
                    try {
                        for (String mobile : baseEnvironmentMonitorAlarmEntity.getMobile().split(",")) {
                            receiverService.sendSms(mobile, "553579", message);
                            //smsUcpassService.send(message, mobile, "553579");
                        }
                    } catch (Exception e) {
                        logger.error("?????????????????????" + e.getMessage());
                    }

                    String emailMessage = "?????????" + message + "???????????????????????????????????????";
                    try {
                        JSONObject json = new JSONObject();
                        json.put("host", host);
                        json.put("from", from);
                        json.put("to", baseEnvironmentMonitorAlarmEntity.getEmail().split(","));
                        json.put("account", account);
                        json.put("password", password);
                        json.put("ssl", ssl);
                        json.put("port", port);

                        JSONObject content = new JSONObject();
                        content.put("subject", "????????????");
                        content.put("content", emailMessage);

                        json.put("content", content);
                        //emailService.send(json.toJSONString());
                        receiverService.sendEmail(baseEnvironmentMonitorAlarmEntity.getEmail().split(","), "????????????", emailMessage);
                    } catch (Exception e) {
                        logger.error("?????????????????????" + e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * ????????????
     */
    public void resourceWarning() {
        List<Node> nodes = nodeDao.findAll();
        for (Node node : nodes) {
            Map<String, Object> result = resourceMonitorDao.findFirstByIntranetIpOrderByLogTimeDesc(node.getIntranetIp());
            ResourceMonitorEntityVm resourceMonitor = MyUtils.model2Entity(result, ResourceMonitorEntityVm.class);
            if (resourceMonitor != null) {
                List<ModuleMonitorEntity> moduleMonitorEntitys = this.moduleMonitorDao.findAllByTypeAndNodeId("resources", node.getId());
                for (ModuleMonitorEntity moduleMonitorEntity : moduleMonitorEntitys) {
                    String items = "";
                    if ((100 - resourceMonitor.getCpu()) > moduleMonitorEntity.getCpu()) {
                        items += "cpu";
                    }
                    if (resourceMonitor.getRam() > moduleMonitorEntity.getRam()) {
                        if (!items.equals("")) {
                            items += "???";
                        }
                        items += "??????";
                    }
                    if (resourceMonitor.getRom() > moduleMonitorEntity.getRom()) {
                        if (!items.equals("")) {
                            items += "???";
                        }
                        items += "??????";
                    }

                    if (!items.equals("")) {
                        String message = resourceMonitor.getNodeName() + "???" + items;

                        switch (moduleMonitorEntity.getAlarmWay()) {
                            case "sms":
                                try {
                                    for (String mobile : moduleMonitorEntity.getPeopleNotified().split(",")) {
                                        receiverService.sendSms(mobile, "553583", message);
                                        //smsUcpassService.send(message, mobile, "553583");
                                    }
                                } catch (Exception e) {
                                    logger.error("?????????????????????" + e.getMessage());
                                }
                                break;
                            case "email":
                                try {
                                    String emailMessage = "?????????" + message + "???????????????????????????????????????";
                                    JSONObject json = new JSONObject();
                                    json.put("host", host);
                                    json.put("from", from);
                                    json.put("to", moduleMonitorEntity.getPeopleNotified().split(","));
                                    json.put("account", account);
                                    json.put("password", password);
                                    json.put("ssl", ssl);
                                    json.put("port", port);

                                    JSONObject content = new JSONObject();
                                    content.put("subject", "??????????????????");
                                    content.put("content", emailMessage);

                                    json.put("content", content);
                                    //emailService.send(json.toJSONString());
                                    receiverService.sendEmail(moduleMonitorEntity.getPeopleNotified().split(","), "??????????????????", emailMessage);
                                } catch (Exception e) {
                                    logger.error("?????????????????????" + e.getMessage());
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
    }

    /**
     * ????????????
     */
    public void dataWarning() {
        List<Services> services = servicesDao.findAll();
        for (Services service : services) {
            SystemMonitorEntity systemMonitorEntity = systemMonitorDao.findFirstByNodeIdAndSystemIdOrderByCreateTimeDesc(service.getNodeId(), service.getId());
            if (systemMonitorEntity != null) {
                List<ModuleMonitorEntity> moduleMonitorEntitys = this.moduleMonitorDao.findAllByTypeAndNodeIdAndSysId(ModuleMonitorType.DATA.getName(), service.getNodeId(), service.getId());
                for (ModuleMonitorEntity moduleMonitorEntity : moduleMonitorEntitys) {
                    String items = "";
                    if (Double.parseDouble(systemMonitorEntity.getDataSource()) > (double) moduleMonitorEntity.getRom()) {
                        items += "????????????";
                    }
                    if (Double.parseDouble(systemMonitorEntity.getMemorySource()) > (double) moduleMonitorEntity.getRam()) {
                        if (!items.equals("")) {
                            items += "???";
                        }
                        items += "????????????";
                    }

                    if (!items.equals("")) {
                        String message = systemMonitorEntity.getNodeName() + "???" + systemMonitorEntity.getSystem() + "???" + items;
                        if (AlarmWay.SMS.getName().equals(moduleMonitorEntity.getAlarmWay())) {
                            try {
                                for (String mobile : moduleMonitorEntity.getPeopleNotified().split(",")) {
                                    //smsUcpassService.send(message, mobile, "553583");
                                    receiverService.sendSms(mobile, "553583", message);
                                }
                            } catch (Exception e) {
                                logger.error("?????????????????????" + e.getMessage());
                            }
                        } else if (AlarmWay.EMAIL.getName().equals(moduleMonitorEntity.getAlarmWay())) {
                            try {
                                String emailMessage = "?????????" + message + "???????????????????????????????????????";
                                JSONObject json = new JSONObject();
                                json.put("host", this.host);
                                json.put("from", this.from);
                                json.put("to", moduleMonitorEntity.getPeopleNotified().split(","));
                                json.put("account", this.account);
                                json.put("password", this.password);
                                json.put("ssl", this.ssl);
                                json.put("port", this.port);

                                JSONObject content = new JSONObject();
                                content.put("subject", "??????????????????");
                                content.put("content", emailMessage);

                                json.put("content", content);

                                //emailService.send(json.toJSONString());
                                receiverService.sendEmail(moduleMonitorEntity.getPeopleNotified().split(","), "??????????????????", emailMessage);
                            } catch (Exception e) {
                                logger.error("?????????????????????" + e.getMessage());
                            }
                        }
                    }
                }
            }
        }
    }

    public void dataBaseWarning() {
        List<Node> nodes = nodeDao.findAll();
        for (Node node : nodes) {
            List<Database> databases = databaseDao.findAllByNodeId(node.getId());
            for (Database database : databases) {
                DatabaseMonitorLog databaseMonitorLog = databaseMonitorLogDao.findFirstByNodeIdAndDatabaseIdOrderByCreateTimeDesc(database.getNodeId(), database.getId());
                if (databaseMonitorLog != null) {
                    List<DatabaseMonitoring> databaseMonitorings = databaseMonitoringDao.findAllByNodeIdAndDatabaseId(database.getNodeId(), database.getId());
                    for (DatabaseMonitoring databaseMonitoring : databaseMonitorings) {

                        String items = "";
                        if (Double.parseDouble(databaseMonitorLog.getDataSource()) > (double) databaseMonitoring.getRom()) {
                            items += "????????????";
                        }
                        if (Double.parseDouble(databaseMonitorLog.getMemorySource()) > (double) databaseMonitoring.getRam()) {
                            if (!items.equals("")) {
                                items += "???";
                            }
                            items += "????????????";
                        }

                        if (!items.equals("")) {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String message = databaseMonitorLog.getNodeName() + "??????"
                                    + databaseMonitorLog.getDatabase() + "???" + items
                                    + "???" + format.format(databaseMonitorLog.getLogTime().getTime());

                            if (AlarmWay.SMS.getName().equals(databaseMonitoring.getAlarmWay())) {
                                try {
                                    for (String mobile : databaseMonitoring.getPeopleNotified().split(",")) {
                                        //smsUcpassService.send(message, mobile, "553583");
                                        receiverService.sendSms(mobile, "553583", message);
                                    }
                                } catch (Exception e) {
                                    logger.error("?????????????????????" + e.getMessage());
                                }
                            } else {
                                try {
                                    String emailMessage = "?????????" + message + "???????????????????????????????????????";
                                    JSONObject json = new JSONObject();
                                    json.put("host", host);
                                    json.put("from", from);
                                    json.put("to", databaseMonitoring.getPeopleNotified().split(","));
                                    json.put("account", account);
                                    json.put("password", password);
                                    json.put("ssl", ssl);
                                    json.put("port", port);

                                    JSONObject content = new JSONObject();
                                    content.put("subject", "?????????????????????");
                                    content.put("content", emailMessage);

                                    json.put("content", content);
                                    //emailService.send(json.toJSONString());
                                    receiverService.sendEmail(databaseMonitoring.getPeopleNotified().split(","), "?????????????????????", emailMessage);
                                } catch (Exception e) {
                                    logger.error("?????????????????????" + e.getMessage());
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
