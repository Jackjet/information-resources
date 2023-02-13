package d1.project.tangshan.operation.manage.service.operations.log;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.tangshan.operation.manage.dao.operations.log.OperationLogDao;
import d1.project.tangshan.operation.manage.entity.operations.log.OperationLog;
import d1.project.tangshan.operation.manage.utils.MyUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

/**
 * @author lin
 */
@Service
public class OperationLogService {
    private final OperationLogDao operationLogDao;
    private final TokenService tokenService;


    public OperationLogService(OperationLogDao operationLogDao, TokenService tokenService) {
        this.operationLogDao = operationLogDao;
        this.tokenService = tokenService;
    }

    public void insert(OperationLog operationLog) {
        operationLogDao.save(operationLog);
    }

    public void setLog(String type, String userName, String operand, String description, String result, HttpServletRequest request) {
        OperationLog log = new OperationLog();
        log.setId(MyUtils.generate32Id());
        log.setType(type);
        log.setOperator(userName);
        tokenService.updateCreateIdAndTime(request, log);
        log.setSource("运行管理平台");
        log.setOperand(operand);
        log.setDescription(description);
        log.setResult(result);
        insert(log);
    }

    public void setLog(String type, String userName, String operand, String description, String result, String userId) {
        OperationLog log = new OperationLog();
        log.setId(MyUtils.generate32Id());
        log.setType(type);
        log.setOperator(userName);
        log.setCreateById(userId);
        log.setCreateTime(Calendar.getInstance());
        log.setSource("运行管理平台");
        log.setOperand(operand);
        log.setDescription(description);
        log.setResult(result);
        insert(log);
    }

    public void setCtrlLog(String id, String type, String userName, String operand, String description, String result, HttpServletRequest request) {
        OperationLog log = new OperationLog();
        log.setId(id);
        log.setType(type);
        log.setOperator(userName);
        tokenService.updateCreateIdAndTime(request, log);
        log.setSource("运行管理平台");
        log.setOperand(operand);
        log.setDescription(description);
        log.setResult(result);
        insert(log);
    }

    public void setCtrlLog(String id, String type, String userName, String operand, String description, String result, String userId) {
        OperationLog log = new OperationLog();
        log.setId(id);
        log.setType(type);
        log.setOperator(userName);
        log.setCreateById(userId);
        log.setCreateTime(Calendar.getInstance());
        log.setSource("运行管理平台");
        log.setOperand(operand);
        log.setDescription(description);
        log.setResult(result);
        insert(log);
    }

    public Page<OperationLog> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<OperationLog> builder = new SpecificationBuilder<>();
        Specification<OperationLog> specification = builder.init(params)
                .sContain("operator", "operator")
                .sContain("source", "source")
                .sContain("type", "type")
                .sContain("operand", "operand")
                .tThanEqual("createTime", "startTime", "yyyy-MM-dd HH:mm:ss")
                .tLessEqual("createTime", "endTime", "yyyy-MM-dd HH:mm:ss")
                .dOrder("createTime")
                .build();
        return operationLogDao.findAll(specification, builder.getPageable());
    }

    public OperationLog findById(String id) throws Exception {
        return operationLogDao.findById(id).orElseThrow(() -> new ValidException("该操作日志不存在"));
    }

    public void updateLog(JSONObject model) throws Exception {
        OperationLog operationLog = MyUtils.model2Entity(model, OperationLog.class);
        OperationLog oldOperationLog = findById(operationLog.getId());
        if (!StringUtils.isEmpty(operationLog.getResult())) {
            oldOperationLog.setResult(operationLog.getResult());
        }
        if (!StringUtils.isEmpty(operationLog.getDescription())) {
            oldOperationLog.setDescription(oldOperationLog.getDescription() + operationLog.getDescription());

        }
        operationLogDao.save(oldOperationLog);
    }

}
