package d1.project.tangshan.operation.manage.service.operations;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.tangshan.operation.manage.dao.operations.ApprovalDao;
import d1.project.tangshan.operation.manage.entity.operations.Approval;
import d1.project.tangshan.operation.manage.service.database.RemoteExecutionService;
import d1.project.tangshan.operation.manage.service.operations.log.OperationLogService;
import d1.project.tangshan.operation.manage.service.remote.RemoteConsoleService;
import d1.project.tangshan.operation.manage.utils.MyUtils;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lin
 */
@Service
public class ApprovalService {
    private final ApprovalDao approvalDao;
    private final TokenService tokenService;
    private final OperationLogService operationLogService;
    @Autowired
    private RemoteExecutionService remoteExecutionService;
    @Autowired
    private RemoteConsoleService remoteConsoleService;

    public ApprovalService(ApprovalDao approvalDao, TokenService tokenService, OperationLogService operationLogService) {
        this.approvalDao = approvalDao;
        this.tokenService = tokenService;
        this.operationLogService = operationLogService;
    }

    public Page<Approval> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<Approval> builder = new SpecificationBuilder<>();
        Specification<Approval> specification = builder.init(params)
                .sContain("applicant", "applicant")
                .sEqual("status", "status")
                .tThanEqual("createTime", "startTime", "yyyy-MM-dd HH:mm:ss")
                .tLessEqual("createTime", "endTime", "yyyy-MM-dd HH:mm:ss")
                .dOrder("createTime")
                .build();
        return approvalDao.findAll(specification, builder.getPageable());
    }

    public void updateStatus(JSONObject model, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        Approval approval = MyUtils.model2Entity(model, Approval.class);
        String status = "agree".equals(approval.getStatus()) ? "??????" : "??????";
        try {
            Approval oldApproval = approvalDao.findById(approval.getId()).orElseThrow(() -> new ValidException("??????????????????"));
            oldApproval.setStatus(approval.getStatus());
            approvalDao.save(oldApproval);
            //sql?????? ?????????  ????????????sql
            if ("sql".equals(oldApproval.getType()) && "agree".equals(approval.getStatus())) {
                JSONObject execute = JSONObject.parseObject(oldApproval.getContent());
                execute.put("isPermissions",1);
                operationLogService.setLog("???????????????" + status, userName, "??????????????????-????????????", status + "???????????????", "1", request);
                remoteExecutionService.executeApproval(execute,null);
            }
            if ("control".equals(oldApproval.getType()) && "agree".equals(approval.getStatus())) {
                JSONObject execute = JSONObject.parseObject(oldApproval.getContent());
                operationLogService.setLog("???????????????" + status, userName, "??????????????????-????????????", status + "???????????????", "1", request);
                remoteConsoleService.executeApproval(execute,null);
            }
            if("reject".equals(approval.getStatus())){
                operationLogService.setLog("???????????????" + status, userName, "??????????????????-????????????", status + "???????????????:", "1", request);
            }

        } catch (Exception e) {
            operationLogService.setLog("???????????????" + status, userName, "??????????????????-????????????", status + "???????????????:", "0", request);
            throw new ValidException(e.getMessage());
        }
    }

    public void insert(Approval approval) {
        approvalDao.save(approval);
    }
}
