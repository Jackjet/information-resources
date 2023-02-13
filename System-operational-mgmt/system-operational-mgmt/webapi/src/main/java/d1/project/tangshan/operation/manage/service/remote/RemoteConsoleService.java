package d1.project.tangshan.operation.manage.service.remote;

import com.alibaba.fastjson.JSONObject;
import d1.framework.permission.entity.UserRole;
import d1.framework.permission.service.UserRoleService;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.TokenService;
import d1.project.tangshan.operation.manage.entity.WebAdminUser;
import d1.project.tangshan.operation.manage.entity.operations.Approval;
import d1.project.tangshan.operation.manage.entity.operations.module.Node;
import d1.project.tangshan.operation.manage.entity.remote.CommandType;
import d1.project.tangshan.operation.manage.entity.remote.Commands;
import d1.project.tangshan.operation.manage.service.WebAdminService;
import d1.project.tangshan.operation.manage.service.operations.ApprovalService;
import d1.project.tangshan.operation.manage.service.operations.log.OperationLogService;
import d1.project.tangshan.operation.manage.service.operations.module.NodeService;
import d1.project.tangshan.operation.manage.utils.MyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class RemoteConsoleService {
    private final CommandsService commandsService;
    private final UserRoleService userRoleService;
    private final TokenService tokenService;
    private final CommandTypeService commandTypeService;
    private final WebAdminService webAdminService;
    private final NodeService nodeService;
    private final OperationLogService operationLogService;
    @Autowired
    private ApprovalService approvalService;

    @Value("${d1.console.url}")
    private String consoleUrl;


    public RemoteConsoleService(CommandsService commandsService, UserRoleService userRoleService, TokenService tokenService, CommandTypeService commandTypeService, WebAdminService webAdminService, NodeService nodeService, OperationLogService operationLogService) {
        this.commandsService = commandsService;
        this.userRoleService = userRoleService;
        this.tokenService = tokenService;
        this.commandTypeService = commandTypeService;
        this.webAdminService = webAdminService;
        this.nodeService = nodeService;
        this.operationLogService = operationLogService;
    }

    public void checkPermissions(String commandsId, HttpServletRequest request) throws Exception {
        String userId = tokenService.getUserByToken(request).getString("id");
        Commands commands = commandsService.findById(commandsId);
        CommandType commandType = commandTypeService.findById(commands.getTypeId());
        String roles = commandType.getRoleIds();
        List<UserRole> userRoles = userRoleService.findAllByUserId(userId);
        if (!"admin".equals(userId) && !check(roles, userRoles)) {
            throw new ValidException("没有该指令的执行权限，是否提交申请?");
        }
    }

    public List<Commands> findCommands(String content) {
        return commandsService.findAllByContentLike(content);
    }

    public void execute(JSONObject jsonObject, HttpServletRequest request) throws Exception {
        String userName;
        String userId;
        if (request == null) {
            userName = jsonObject.getString("userName");
            userId = jsonObject.getString("userId");
        } else {
            userName = tokenService.getUserByToken(request).getString("name");
            userId = tokenService.getUserByToken(request).getString("id");
        }
        String commandsId = jsonObject.getString("commandsId");
        String command = jsonObject.getString("command");
        String isPermissions = jsonObject.getString("isPermissions");
        String nodeId = jsonObject.getString("nodeId");
        String nodeName = "未知";
        String logId = MyUtils.generate32Id();
        try {
            Node node = nodeService.findById(nodeId);
            nodeName = node.getName();
            Commands commands = new Commands();
            if (!StringUtils.isEmpty(commandsId)) {
                commands = commandsService.findById(commandsId);
                command = commands.getContent();
            }
            if ("0".equals(isPermissions)) {
                WebAdminUser webAdminUser = webAdminService.findById(userId).orElseThrow(() -> new ValidException("该用户不存在"));
                Approval approval = new Approval();
                approval.setId(MyUtils.generate32Id());
                approval.setStatus("stay");
                approval.setType("control");
                approval.setApplicant(webAdminUser.getName());
                approval.setTel(webAdminUser.getTel());
                approval.setOrganization(webAdminUser.getOrganization());
                JSONObject approvalContent = new JSONObject();
                approvalContent.put("nodeId", node.getId());
                approvalContent.put("command", command);
                approvalContent.put("commandsId", commandsId);
                approvalContent.put("userName", userName);
                approvalContent.put("userId", userId);
                approval.setContent(approvalContent.toJSONString());
                tokenService.updateCreateIdAndTime(request, approval);
                approvalService.insert(approval);
                operationLogService.setLog("远程控制台-执行", userName, "远程控制—远程控制台", "申请了" + nodeName + "下运行管理系统的远程控制台", "1", userId);
                throw new ValidException("您没有执行该指令的权限已为您提交执行指令的申请");
            }
            try {
                operationLogService.setCtrlLog(logId, "远程控制台-执行", userName, "远程控制—远程控制台", "执行了" + nodeName + "下运行管理系统的远程控制台", "", userId);
                RestTemplate restTemplate = new RestTemplate();
                String rootUrl = "http://" + node.getIntranetIp() + consoleUrl + "?command=" + command + "&id=" + logId;
                restTemplate.getForObject(rootUrl, String.class);
            } catch (Exception e) {
                throw new ValidException("执行失败");
            }
        } catch (Exception e) {
            if ("1".equals(isPermissions)) {
                operationLogService.setCtrlLog(logId, "远程控制台-执行", userName, "远程控制—远程控制台", "执行了" + nodeName + "下运行管理系统的远程控制台", "0", userId);
            }
            throw new ValidException(e.getMessage());
        }
    }

    public void executeApproval(JSONObject jsonObject, HttpServletRequest request) throws Exception {
        String userName;
        String userId;
        if (request == null) {
            userName = jsonObject.getString("userName");
            userId = jsonObject.getString("userId");
        } else {
            userName = tokenService.getUserByToken(request).getString("name");
            userId = tokenService.getUserByToken(request).getString("id");
        }
        String commandsId = jsonObject.getString("commandsId");
        String command = jsonObject.getString("command");
        String nodeId = jsonObject.getString("nodeId");
        String nodeName = "未知";
        String logId = MyUtils.generate32Id();
        try {
            Node node = nodeService.findById(nodeId);
            nodeName = node.getName();
            Commands commands = new Commands();
            if (!StringUtils.isEmpty(commandsId)) {
                commands = commandsService.findById(commandsId);
                command = commands.getContent();
            }
            operationLogService.setCtrlLog(logId, "远程控制台-执行", userName, "远程控制—远程控制台", "执行了" + nodeName + "下运行管理系统的远程控制台", "", userId);
            try {
                RestTemplate restTemplate = new RestTemplate();
//                String rootUrl = "http://" + node.getIntranetIp() + ":8081/commandExcute/dcrun/microapp/get/commandExcute?command=" + command + "&id=" + logId;
//                String rootUrl = "http://" + node.getPublicIp() + ":8081/dcrun/microapp/get/commandExcute?command=" + command + "&id=" + logId;
                String rootUrl = "http://" + node.getPublicIp() + consoleUrl+"?command=" + command + "&id=" + logId;

                System.out.println("rootUrl::" + rootUrl);
                restTemplate.getForObject(rootUrl, String.class);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ValidException("执行失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            operationLogService.setCtrlLog(logId, "远程控制台-执行", userName, "远程控制—远程控制台", "执行了" + nodeName + "下运行管理系统的远程控制台", "0", userId);
        }
    }

    private Boolean check(String roles, List<UserRole> userRoles) {
        for (UserRole userRole : userRoles) {
            if (roles.contains(userRole.getRoleId())) {
                return true;
            }
        }
        return false;
    }


}
