package d1.framework.permission.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.permission.dao.OperationLogDao1;
import d1.framework.permission.entity.OperationLog1;
import d1.framework.permission.utils.MyUtils;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lin
 */
@Service
public class OperationLogService1 {
    @Autowired
    private OperationLogDao1 operationLogDao;
    @Autowired
    private TokenService tokenService;


    public void insert(OperationLog1 operationLog) {
        operationLogDao.save(operationLog);
    }

    public void setLog(String type, String userName, String operand, String description, String result, HttpServletRequest request) {
        OperationLog1 log = new OperationLog1();
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

    public void setCtrlLog(String id, String type, String userName, String operand, String description, String result, HttpServletRequest request) {
        OperationLog1 log = new OperationLog1();
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

    public Page<OperationLog1> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<OperationLog1> builder = new SpecificationBuilder<>();
        Specification<OperationLog1> specification = builder.init(params)
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

    public OperationLog1 findById(String id) throws Exception {
        return operationLogDao.findById(id).orElseThrow(() -> new ValidException("该操作日志不存在"));
    }

    public void updateLog(JSONObject model) throws Exception {
        OperationLog1 operationLog = MyUtils.model2Entity(model, OperationLog1.class);
        OperationLog1 oldOperationLog = findById(operationLog.getId());
        if (!StringUtils.isEmpty(operationLog.getResult())) {
            oldOperationLog.setResult(operationLog.getResult());
        }
        if (!StringUtils.isEmpty(operationLog.getDescription())) {
            oldOperationLog.setDescription(operationLog.getDescription());
        }
        operationLogDao.save(oldOperationLog);
    }

}
