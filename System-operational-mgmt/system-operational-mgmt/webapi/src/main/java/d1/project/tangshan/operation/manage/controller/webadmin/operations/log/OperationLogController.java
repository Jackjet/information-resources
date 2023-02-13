package d1.project.tangshan.operation.manage.controller.webadmin.operations.log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.tangshan.operation.manage.entity.database.Instances;
import d1.project.tangshan.operation.manage.entity.operations.log.OperationLog;
import d1.project.tangshan.operation.manage.service.operations.log.OperationLogService;
import d1.project.tangshan.operation.manage.utils.MyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Calendar;

/**
 * @author lin
 */
@Auth("webadmin")
@RestController("/webadmin/log/operationLog")
@RequestMapping("/webadmin/log/operationLog")
@Api(value = "/webadmin/log/operationLog", tags = "操作日志管理")
public class OperationLogController {
    private final OperationLogService operationLogService;

    public OperationLogController(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

    @ApiOperation(value = "操作日志列表（分页）")
    @GetMapping(value = "/findAll")
    public Result<Page<OperationLog>> findAll(OperationLogFindAllVm model) {
        try {
            Page<OperationLog> result = operationLogService.findAll((JSONObject) JSON.toJSON(model));
            return ResultUtil.success("成功", result);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }
    
    @Auth("noauth")
    @ApiOperation(value = "新增")
    @PostMapping(value = "/insertLog")
    public Result<String> insertLog(@Valid @RequestBody OperationLogInsertVm model) {
        try {
            OperationLog operationLog = MyUtils.model2Entity(model, OperationLog.class);
            operationLog.setId(MyUtils.generate32Id());
            if (!StringUtils.isEmpty(model.getCreateTime())) {
                operationLog.setCreateTime(MyUtils.stringToCalendar(model.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            } else {
                operationLog.setCreateTime(Calendar.getInstance());
            }
            operationLog.setCreateById(model.getOperatorId());
            operationLogService.insert(operationLog);
            return ResultUtil.success("添加成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @Auth("noauth")
    @ApiOperation(value = "回调")
    @PostMapping(value = "/updateLog")
    public Result<String> updateLog(@Valid @RequestBody OperationLogUpdateVm model) {
        try {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(model);
            operationLogService.updateLog(jsonObject);
            return ResultUtil.success("添加成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "详情")
    @GetMapping(value = "/findById")
    public Result<OperationLog> findById(String id) {
        try {
            return ResultUtil.success("成功", operationLogService.findById(id));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }
}

@ApiModel(value = "OperationLogFindAllVm", description = "查询")
class OperationLogFindAllVm extends PageableVm {
    @ApiModelProperty(value = "操作员")
    private String operator;
    @ApiModelProperty("事件类型")
    private String type;
    @ApiModelProperty("事件源")
    private String source;
    @ApiModelProperty("操作对象")
    private String operand;
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

@ApiModel(value = "OperationLogInsertVm", description = "新增")
class OperationLogInsertVm {
    @ApiModelProperty("事件类型")
    @NotBlank(message = "事件类型不可为空")
    private String type;
    @ApiModelProperty("事件源")
    @NotBlank(message = "事件源不可为空")
    private String source;
    @ApiModelProperty("操作对象")
    private String operand;
    @ApiModelProperty("操作结果 0失败1成功")
    private String result;
    @ApiModelProperty("详细描述")
    private String description;
    @ApiModelProperty("操作员")
    @NotBlank(message = "操作员不可为空")
    private String operator;
    @ApiModelProperty("操作员id")
    @NotBlank(message = "操作员id不可为空")
    private String operatorId;
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

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}

@ApiModel(value = "OperationLogUpdateVm", description = "回调")
class OperationLogUpdateVm {
    private String id;
    @ApiModelProperty("操作结果 0失败1成功")
    private String result;
    @ApiModelProperty("详细描述")
    private String description;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}