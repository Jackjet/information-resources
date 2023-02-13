package d1.project.tangshan.operation.manage.controller.webadmin.bigdata;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.tangshan.operation.manage.entity.BigDataApiPlanEntity;
import d1.project.tangshan.operation.manage.service.BigDataApiPlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 数据服务接口计划任务管理
 *
 * @author chenghan
 **/
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/dataApiPlan")
@Api(value = "/webadmin/dataApiPlan", tags = "数据服务接口计划任务管理")
public class BigDataApiPlanController {

    private final BigDataApiPlanService bigDataApiPlanService;

    @Autowired
    public BigDataApiPlanController(BigDataApiPlanService bigDataApiPlanService) {
        this.bigDataApiPlanService = bigDataApiPlanService;
    }

    @ApiOperation(value = "查询", notes = "查询所有数据")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Page<BigDataApiPlanEntity>> findAll(HttpServletRequest request, BigDataPlanFindAllVm model) {
        try {
            Page<BigDataApiPlanEntity> result = bigDataApiPlanService.findAll((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success("查询成功", result);
        } catch (Exception e) {
            return ResultUtil.fail("查询失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "详情")
    @GetMapping(value = "/findById")
    public Result<BigDataApiPlanEntity> findById(String id) {
        try {
            return ResultUtil.success("成功", bigDataApiPlanService.findById(id));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "插入", notes = "插入数据")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result<String> insert(HttpServletRequest request, @Valid @RequestBody BigDataPlanInsertVm model) {
        try {
            this.bigDataApiPlanService.insert((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success("添加成功");
        } catch (Exception e) {
            return ResultUtil.fail( e.getMessage(), e);
        }
    }

    @ApiOperation(value = "更新", notes = "更新数据")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<String> update(HttpServletRequest request, @Valid @RequestBody BigDataPlanUpdateVm model) {
        try {
            this.bigDataApiPlanService.update((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success("编辑成功");
        } catch (Exception e) {
            return ResultUtil.fail( e.getMessage(), e);
        }
    }

    @ApiOperation(value = "删除", notes = "删除数据")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<String> delete(@Valid @RequestBody BigDataPlanIdVm model, HttpServletRequest request) {
        try {
            this.bigDataApiPlanService.delete(model.getId(),request);
            return ResultUtil.success("删除成功");
        } catch (Exception e) {
            return ResultUtil.fail( e.getMessage(), e);
        }
    }

    @ApiOperation(value = "启动", notes = "启动")
    @RequestMapping(value = "/start", method = RequestMethod.PUT)
    public Result<String> start(HttpServletRequest request, @Valid @RequestBody BigDataPlanIdVm model) {
        try {
            this.bigDataApiPlanService.start((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success("启动成功");
        } catch (Exception e) {
            return ResultUtil.fail("启动失败", e);
        }
    }

    @ApiOperation(value = "停止", notes = "停止")
    @RequestMapping(value = "/stop", method = RequestMethod.PUT)
    public Result<String> stop(HttpServletRequest request, @Valid @RequestBody BigDataPlanIdVm model) {
        try {
            this.bigDataApiPlanService.stop((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success("停止成功");
        } catch (Exception e) {
            return ResultUtil.fail( e.getMessage(), e);
        }
    }
}

class BigDataPlanFindAllVm extends PageableVm {
    @ApiModelProperty(value = "计划名称")
    private String name;
    @ApiModelProperty("接口名称")
    private String apiName;
    @ApiModelProperty("任务状态，已启动(started)，已暂停(paused)")
    private String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

class BigDataPlanInsertVm {
    @NotBlank(message = "计划名称不能为空")
    @ApiModelProperty("计划名称")
    private String name;
    @NotBlank(message = "接口id不能为空")
    @ApiModelProperty("接口id")
    private String apiId;
    @NotBlank(message = "接口名称不能为空")
    @ApiModelProperty("接口名称")
    private String apiName;
    @NotBlank(message = "cron表达式不能为空")
    @ApiModelProperty("cron表达式")
    private String cron;
    @ApiModelProperty("url参数")
    private String params;
    @ApiModelProperty("请求body")
    private String body;
    @ApiModelProperty("备注")
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

class BigDataPlanUpdateVm {
    @NotBlank(message = "id不能为空")
    @ApiModelProperty("id")
    private String id;
    @NotBlank(message = "cron表达式不能为空")
    @ApiModelProperty("cron表达式")
    private String cron;
    @ApiModelProperty("url参数")
    private String params;
    @ApiModelProperty("请求body")
    private String body;
    @ApiModelProperty("备注")
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

class BigDataPlanIdVm {
    @NotBlank(message = "id不能为空")
    @ApiModelProperty("id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

