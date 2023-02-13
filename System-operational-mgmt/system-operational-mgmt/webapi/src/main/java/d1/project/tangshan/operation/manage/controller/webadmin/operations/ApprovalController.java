package d1.project.tangshan.operation.manage.controller.webadmin.operations;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.tangshan.operation.manage.entity.operations.Approval;
import d1.project.tangshan.operation.manage.service.operations.ApprovalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author lin
 */
@Auth("webadmin")
@RestController("/webadmin/operations/approval")
@RequestMapping("/webadmin/operations/approval")
@Api(value = "/webadmin/operations/approval", tags = "审批管理")
public class ApprovalController {
    private final ApprovalService approvalService;

    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @ApiOperation(value = "审批列表（分页）")
    @GetMapping(value = "/findAll")
    public Result<Page<Approval>> findAll(ApprovalFindAllVm model) {
        try {
            Page<Approval> result = approvalService.findAll((JSONObject) JSON.toJSON(model));
            return ResultUtil.success("成功", result);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "同意/驳回审批")
    @PutMapping(value = "/updateStatus")
    public Result<String> updateStatus(@Valid @RequestBody UpdateStatusVm model, HttpServletRequest request) {
        try {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(model);
            approvalService.updateStatus(jsonObject, request);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }
}

@ApiModel(value = "ApprovalFindAllVm", description = "查询")
class ApprovalFindAllVm extends PageableVm {
    @ApiModelProperty("申请人姓名")
    private String applicant;
    @ApiModelProperty("开始时间")
    private String startTime;
    @ApiModelProperty("结束时间")
    private String endTime;
    @ApiModelProperty("状态(stay待审批agree已同意reject已驳回)")
    private String status;

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

@ApiModel(value = "UpdateStatusVm", description = "审批")
class UpdateStatusVm {

    @ApiModelProperty("审批id")
    @NotBlank(message = "审批id不可为空")
    private String id;

    @ApiModelProperty("状态(agree已同意reject已驳回)")
    @NotBlank(message = "状态不可为空")
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
