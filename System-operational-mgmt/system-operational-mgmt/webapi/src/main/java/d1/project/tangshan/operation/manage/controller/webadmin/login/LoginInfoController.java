package d1.project.tangshan.operation.manage.controller.webadmin.login;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.tangshan.operation.manage.Constants;
import d1.project.tangshan.operation.manage.entity.LoginInfoEntity;
import d1.project.tangshan.operation.manage.model.DateAndNumber;
import d1.project.tangshan.operation.manage.model.PageableVm;
import d1.project.tangshan.operation.manage.service.LoginInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @Author chengh
 **/
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/loginInfo")
@Api(value = "/webadmin/loginInfo", tags = "用户登录信息")
public class LoginInfoController {
    private final LoginInfoService loginInfoService;

    @Autowired
    public LoginInfoController(LoginInfoService loginInfoService) {
        this.loginInfoService = loginInfoService;
    }

    @ApiOperation(value = "查询", notes = "查询所有数据")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Page<LoginInfoEntity>> findAll(HttpServletRequest request, LoginInfoFindAllVm model) {
        try {
            Page<LoginInfoEntity> result = loginInfoService.findAll((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success("查询成功", result);
        } catch (Exception e) {
            return ResultUtil.fail("查询失败：" + e.getMessage(), e);
        }
    }

    @Auth("noauth")
    @ApiOperation(value = "添加用户信息")
    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public Result<String> insert(@RequestBody LoginInfoEntity model) {
        try {
            this.loginInfoService.insert((JSONObject) JSON.toJSON(model));
            return ResultUtil.success(Constants.SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "excel导出")
    @GetMapping(value = "/excelExport")
    public Result<String> excelExport(HttpServletRequest request, HttpServletResponse response, LoginInfoFindAllVm model) {
        try {
            if (!StringUtils.isEmpty(model.getStartTime())) {
                model.setStartTime(model.getStartTime() + " 00:00:00");
            }
            if (!StringUtils.isEmpty(model.getEndTime())) {
                model.setEndTime(model.getEndTime() + " 23:59:59");
            }
            loginInfoService.excelExport((JSONObject) JSON.toJSON(model), response, request);
            return null;
        } catch (Exception e) {
            return ResultUtil.fail("导出失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "pdf导出")
    @GetMapping(value = "/pdfExport")
    public Result<String> pdfExport(HttpServletRequest request, HttpServletResponse response, LoginInfoFindAllVm model) {
        try {
            if (!StringUtils.isEmpty(model.getStartTime())) {
                model.setStartTime(model.getStartTime() + " 00:00:00");
            }
            if (!StringUtils.isEmpty(model.getEndTime())) {
                model.setEndTime(model.getEndTime() + " 23:59:59");
            }
            loginInfoService.pdfExport((JSONObject) JSON.toJSON(model), response, request);
            return null;
        } catch (Exception e) {
            return ResultUtil.fail("导出失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "用户访问趋势图", notes = "用户访问趋势图")
    @RequestMapping(value = "/accessTrend", method = RequestMethod.GET)
    public Result<List<DateAndNumber>> accessTrend(AccessTrendVm model) {
        try {
            List<DateAndNumber> result = loginInfoService.accessTrend((JSONObject) JSON.toJSON(model));
            return ResultUtil.success("查询成功", result);
        } catch (Exception e) {
            return ResultUtil.fail("查询失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "在线用户监控", notes = "在线用户监控")
    @RequestMapping(value = "/online", method = RequestMethod.GET)
    public Result<Page<LoginInfoEntity>> online(OnlineVm model) {
        try {
            Page<LoginInfoEntity> result = loginInfoService.online((JSONObject) JSON.toJSON(model));
            return ResultUtil.success("查询成功", result);
        } catch (Exception e) {
            return ResultUtil.fail("查询失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "用户活跃度", notes = "用户活跃度")
    @RequestMapping(value = "/activity", method = RequestMethod.GET)
    public Result<List<DateAndNumber>> activity(ActivityVm model) {
        try {
            List<DateAndNumber> result = loginInfoService.activity((JSONObject) JSON.toJSON(model));
            return ResultUtil.success("查询成功", result);
        } catch (Exception e) {
            return ResultUtil.fail("查询失败：" + e.getMessage(), e);
        }
    }
}

class LoginInfoFindAllVm extends PageableVm {
    @ApiModelProperty("开始时间")
    private String startTime;
    @ApiModelProperty("结束时间")
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

class AccessTrendVm {
    @ApiModelProperty("开始时间")
    private String startTime;
    @ApiModelProperty("结束时间")
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

class OnlineVm extends PageableVm {
}

class ActivityVm {
    @ApiModelProperty("用户名称")
    private String name;
    @NotBlank(message = "统计单位不能为空")
    @ApiModelProperty("统计单位，日(day),月(month),年(year),默认是日")
    private String unit;
    @ApiModelProperty("开始时间")
    private String startTime;
    @ApiModelProperty("结束时间")
    private String endTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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
