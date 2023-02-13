package d1.project.tangshan.operation.manage.service.database;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.TokenService;
import d1.project.tangshan.operation.manage.dao.DatabaseMonitorLogDao;
import d1.project.tangshan.operation.manage.entity.database.DatabaseMonitorLog;
import d1.project.tangshan.operation.manage.entity.database.DatabaseMonitoring;
import d1.project.tangshan.operation.manage.service.ModuleMonitorService;
import d1.project.tangshan.operation.manage.utils.AlarmWay;
import d1.project.tangshan.operation.manage.utils.MyUtils;
import net.dcrun.component.email.IEmailService;
import net.dcrun.component.sms.ucpass.ISmsUcpassService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Service
public class DatabaseMonitorLogService {
    private final DatabaseMonitorLogDao databaseMonitorLogDao;
    private final TokenService tokenService;
    private final DatabaseMonitoringService databaseMonitoringService;
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

    public DatabaseMonitorLogService(DatabaseMonitorLogDao databaseMonitorLogDao, TokenService tokenService, DatabaseMonitoringService databaseMonitoringService, ISmsUcpassService smsUcpassService, IEmailService emailService) {
        this.databaseMonitorLogDao = databaseMonitorLogDao;
        this.tokenService = tokenService;
        this.databaseMonitoringService = databaseMonitoringService;
        this.smsUcpassService = smsUcpassService;
        this.emailService = emailService;
    }

    public void insert (JSONObject param) throws Exception {
        DatabaseMonitorLog databaseMonitorLog = JSONObject.toJavaObject(param,DatabaseMonitorLog.class);
        databaseMonitorLog.setId(MyUtils.generate32Id());
        databaseMonitorLog.setCreateTime(Calendar.getInstance());
        databaseMonitorLog.setLogTime(Calendar.getInstance());
        databaseMonitorLogDao.save(databaseMonitorLog);
    }
}
