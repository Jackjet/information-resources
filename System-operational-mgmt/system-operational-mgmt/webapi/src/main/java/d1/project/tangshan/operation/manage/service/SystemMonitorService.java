package d1.project.tangshan.operation.manage.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.tangshan.operation.manage.dao.SystemMonitorDao;
import d1.project.tangshan.operation.manage.entity.BaseEnvironmentMonitorAlarmEntity;
import d1.project.tangshan.operation.manage.entity.ModuleMonitorEntity;
import d1.project.tangshan.operation.manage.entity.SystemMonitorEntity;
import d1.project.tangshan.operation.manage.entity.operations.log.SystemLog;
import d1.project.tangshan.operation.manage.service.operations.log.SystemLogService;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author Buter
 * @date 2020/3/16 6:50
 */
@Service
public class SystemMonitorService {
    private final SystemMonitorDao systemMonitorDao;
    private final TokenService tokenService;
    private final BaseEnvironmentMonitorAlarmService baseEnvironmentMonitorAlarmService;
    private final ISmsUcpassService smsUcpassService;
    private final IEmailService emailService;
    private final ModuleMonitorService moduleMonitorService;
    private final SystemLogService systemLogService;
    private final Logger logger = LoggerFactory.getLogger(SystemMonitorService.class);

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

    public SystemMonitorService(SystemMonitorDao systemMonitorDao, TokenService tokenService, BaseEnvironmentMonitorAlarmService baseEnvironmentMonitorAlarmService, ISmsUcpassService smsUcpassService, IEmailService emailService, ModuleMonitorService moduleMonitorService, SystemLogService systemLogService) {
        this.systemMonitorDao = systemMonitorDao;
        this.tokenService = tokenService;
        this.baseEnvironmentMonitorAlarmService = baseEnvironmentMonitorAlarmService;
        this.smsUcpassService = smsUcpassService;
        this.emailService = emailService;
        this.moduleMonitorService = moduleMonitorService;
        this.systemLogService = systemLogService;
    }

    public Page<SystemMonitorEntity> findAll(JSONObject params, HttpServletRequest request) throws Exception {
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        SpecificationBuilder<SystemMonitorEntity> builder = new SpecificationBuilder<>();
        Specification specification = builder.init(params)
                .sContain("nodeName", "nodeName")
                .tBetween("logTime", "beginTime", "endTime", dateFormat)
                .dOrder("logTime").build();

        return this.systemMonitorDao.findAll(specification, builder.getPageable());
    }

    public Page<SystemMonitorEntity> findAll(JSONObject params) throws Exception {
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        SpecificationBuilder<SystemMonitorEntity> builder = new SpecificationBuilder<>();
        Specification<SystemMonitorEntity> specification = builder.init(params)
                .sContain("nodeName", "nodeName")
                .sEqual("nodeId", "nodeId")
                .sContain("system", "system")
                .sEqual("systemId", "systemId")
                .sIn("systemId", "systemIdIn")
                .tBetween("logTime", "startTime", "endTime", dateFormat)
                .dOrder("logTime").build();

        return systemMonitorDao.findAll(specification, builder.getPageable());
    }

    public void insert(SystemMonitorEntity systemMonitorEntity) throws Exception {
        systemMonitorDao.save(systemMonitorEntity);
        if(systemMonitorEntity.getStatus().equals(StatusType.STOP.getName())){
            SystemLog systemLog = new SystemLog();
            systemLog.setId(MyUtils.generate32Id());
            systemLog.setSource(systemMonitorEntity.getSystem());
            systemLog.setInfo(systemMonitorEntity.getSystem() + "运行");
            systemLog.setResult("服务运行");
            systemLog.setCreateTime(Calendar.getInstance());

            systemLogService.insert(systemLog);
        }
    }

    List<Map<String, Object>> findByDay(String systemId, Calendar startTime, Calendar endTime){
        return systemMonitorDao.findByDay( systemId,  startTime,  endTime);
    }

}
