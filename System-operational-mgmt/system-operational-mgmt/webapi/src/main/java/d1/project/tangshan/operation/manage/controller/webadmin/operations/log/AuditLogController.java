package d1.project.tangshan.operation.manage.controller.webadmin.operations.log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.tangshan.operation.manage.entity.operations.log.AuditLog;
import d1.project.tangshan.operation.manage.service.operations.log.AuditLogService;
import d1.project.tangshan.operation.manage.utils.MyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Calendar;

/**
 * @author lin
 */
@Auth("webadmin")
@RestController("/webadmin/log/auditLog")
@RequestMapping("/webadmin/log/auditLog")
@Api(value = "/webadmin/log/auditLog", tags = "审计日志管理")
public class AuditLogController {
    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }
    
    @Auth("noauth")
    @ApiOperation(value = "新增")
    @PostMapping(value = "/insertLog")
    public Result<String> insertLog(@Valid @RequestBody AuditLogInsertVm model) {
        try {
            AuditLog auditLog = MyUtils.model2Entity(model, AuditLog.class);
            auditLog.setId(MyUtils.generate32Id());
            if (!StringUtils.isEmpty(model.getCreateTime())) {
                auditLog.setCreateTime(MyUtils.stringToCalendar(model.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            } else {
                auditLog.setCreateTime(Calendar.getInstance());
            }
            auditLogService.insert(auditLog);
            return ResultUtil.success("添加成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }
    
    @ApiOperation(value = "审计日志列表（分页）")
    @GetMapping(value = "/findAll")
    public Result<Page<AuditLog>> findAll(AuditLogFindAllVm model) {
        try {
            Page<AuditLog> result = auditLogService.findAll((JSONObject) JSON.toJSON(model));
            return ResultUtil.success("成功", result);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }
}

@ApiModel(value = "AuditLogFindAllVm", description = "查询")
class AuditLogFindAllVm extends PageableVm {
    @ApiModelProperty(value = "操作员")
    private String operator;
    @ApiModelProperty("事件源")
    private String type;
    @ApiModelProperty("开始时间")
    private String startTime;
    @ApiModelProperty("结束时间")
    private String endTime;

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
@ApiModel(value = "AuditLogInsertVm", description = "新增")
class AuditLogInsertVm {
    @ApiModelProperty("事件类型")
    private String type;
    @ApiModelProperty("事件源")
    private String source;
    @ApiModelProperty("操作对象")
    private String operand;
    @ApiModelProperty("操作结果 0失败1成功")
    private String result;
    @ApiModelProperty("详细描述")
    private String description;
    @ApiModelProperty("操作员")
    private String operator;
    @ApiModelProperty("操作员id")
    private String createById;
    @ApiModelProperty("登录IP")
    private String loginIp;
    @ApiModelProperty("事件级别")
    private String level;
    @ApiModelProperty("创建时间")
    private String createTime;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getOperand() {
        return operand;
    }

    public void setOperand(String operand) {
        this.operand = operand;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getCreateById() {
        return createById;
    }

    public void setCreateById(String createById) {
        this.createById = createById;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}