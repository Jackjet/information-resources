package d1.project.tangshan.operation.manage.service.operations.log;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.tangshan.operation.manage.dao.operations.log.AuditLogDao;
import d1.project.tangshan.operation.manage.entity.operations.log.AuditLog;
import d1.project.tangshan.operation.manage.utils.MyUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author lin
 */
@Service
public class AuditLogService {
    private final AuditLogDao auditLogDao;
    private final TokenService tokenService;

    public AuditLogService(AuditLogDao auditLogDao, TokenService tokenService) {
        this.auditLogDao = auditLogDao;
        this.tokenService = tokenService;
    }

    public Page<AuditLog> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<AuditLog> builder = new SpecificationBuilder<>();
        Specification<AuditLog> specification = builder.init(params)
                .sContain("operator", "operator")
                .sContain("type", "type")
                .tThanEqual("createTime", "startTime", "yyyy-MM-dd HH:mm:ss")
                .tLessEqual("createTime", "endTime", "yyyy-MM-dd HH:mm:ss")
                .dOrder("createTime")
                .build();
        return auditLogDao.findAll(specification, builder.getPageable());
    }

    public void insert(AuditLog auditLog) {
        auditLogDao.save(auditLog);
    }

    public void setLog(String type, String userName, String level, String result, HttpServletRequest request) {
        JSONObject user = tokenService.getUserByToken(request);
        String userId = null;
        if (user != null) {
            userId = user.getString("id");
        }
        AuditLog log = new AuditLog();
        log.setId(MyUtils.generate32Id());
        log.setCreateById(userId);
        log.setCreateTime(Calendar.getInstance());
        log.setType(type);
        log.setOperator(userName);
        log.setOperand("运行管理平台");
        log.setLevel(level);
        log.setResult(result);
        log.setLoginIp(MyUtils.getIpAddress(request));
        insert(log);
    }

    public Integer countAllByOperatorAndLevel(String operator, String level) {
        return auditLogDao.countAllByOperatorAndLevel(operator, level);
    }

    public Integer countAllByOperatorAndLevelAndCreateTimeBetween(JSONObject params) throws Exception {
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        SpecificationBuilder<AuditLog> builder = new SpecificationBuilder<>();
        Specification specification = builder.init(params)
                .sEqual("operator", "operator")
                .sEqual("level", "level")
                .sEqual("result", "result")
                .tBetween("createTime", "startTime", "endTime", dateFormat).build();
        return auditLogDao.findAll(specification).size();
    }

    public Integer countAllByOperatorAndLevelAndResultAndCreateTimeBetween(String operator, String level, String result, Calendar startTime, Calendar endTime) {
        return auditLogDao.countAllByOperatorAndLevelAndResultAndCreateTimeBetween(operator, level, result, startTime, endTime);
    }

    public List<Map<String, Object>> countSystemNum(Calendar startTime, Calendar endTime) {
        return auditLogDao.countSystemNum(startTime, endTime);
    }
}
