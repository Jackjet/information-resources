package d1.project.tangshan.operation.manage.controller.webadmin.share;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.tangshan.operation.manage.model.UtilizationVm;
import d1.project.tangshan.operation.manage.service.share.ShareService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author lin
 */
@RestController
@RequestMapping("/webadmin/share")
@Api(value = "/webadmin/share", tags = "共享交换平台数据驾驶舱")
public class ShareController {
    private final ShareService shareService;

    public ShareController(ShareService shareService) {
        this.shareService = shareService;
    }

    @ApiOperation(value = "服务器占用率")
    @GetMapping(value = "/utilization")
    public Result<UtilizationVm> utilization(String nodeId) {
        try {
            return ResultUtil.success("成功", shareService.getUtilization(nodeId));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "累计活跃用户数")
    @GetMapping(value = "/totalUserNum")
    public Result<JSONObject> totalUserNum() {
        try {
            return ResultUtil.success("成功", shareService.getUserNum());
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "系统访问量")
    @GetMapping(value = "/getSystemVisit")
    public Result<JSONObject> getSystemVisit(@Valid SystemVisitVm model) {
        try {
            return ResultUtil.success("成功", shareService.getSystemVisit(model.getStartTime(),model.getEndTime()));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "系统在线人数")
    @GetMapping(value = "/getLoginNum")
    public Result<JSONObject> getLoginNum(@Valid SystemVisitVm model) {
        try {
            return ResultUtil.success("成功", shareService.getLoginNum(model.getStartTime(),model.getEndTime()));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "查询节点")
    @GetMapping(value = "/findAllNodes")
    public Result<JSONObject> findAllNodes() {
        try {
            return ResultUtil.success("成功", shareService.findAllNodes());
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }


}
class SystemVisitVm {
    @NotBlank(message = "开始时间不能为空")
    @ApiModelProperty(value = "开始时间", example = "2018-01-01")
    private String startTime;
    @NotBlank(message = "结束时间不能为空")
    @ApiModelProperty(value = "结束时间", example = "2018-01-01")
    private String endTime;

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