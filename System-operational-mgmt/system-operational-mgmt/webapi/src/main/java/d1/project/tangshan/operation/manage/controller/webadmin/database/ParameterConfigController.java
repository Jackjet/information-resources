package d1.project.tangshan.operation.manage.controller.webadmin.database;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.tangshan.operation.manage.entity.database.ParameterConfig;
import d1.project.tangshan.operation.manage.service.database.ParameterConfigService;
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
@RestController("/webadmin/databaseOperation/parameterConfig")
@RequestMapping("/webadmin/databaseOperation/parameterConfig")
@Api(value = "/webadmin/databaseOperation/parameterConfig", tags = "参数配置管理")
public class ParameterConfigController {
    private final ParameterConfigService parameterConfigService;

    public ParameterConfigController(ParameterConfigService parameterConfigService) {
        this.parameterConfigService = parameterConfigService;
    }


    @ApiOperation(value = "新增")
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody ParameterConfigInsertVm model, HttpServletRequest request) {
        try {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(model);
            parameterConfigService.insert(jsonObject, request);
            return ResultUtil.success("添加成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "删除参数配置")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(String id, HttpServletRequest request) {
        try {
            parameterConfigService.delete(id, request);
            return ResultUtil.success("删除成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "参数配置编辑")
    @PutMapping(value = "/update")
    public Result<String> update(HttpServletRequest request, @Valid @RequestBody ParameterConfigUpdateVm model) {
        try {
            parameterConfigService.update(request, (JSONObject) JSON.toJSON(model));
            return ResultUtil.success("编辑成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "参数配置列表（分页）")
    @GetMapping(value = "/findAll")
    public Result<Page<ParameterConfig>> findAll(ParameterConfigFindAllVm model) {
        try {
            Page<ParameterConfig> result = parameterConfigService.findAll((JSONObject) JSON.toJSON(model));
            return ResultUtil.success("成功", result);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "详情")
    @GetMapping(value = "/findById")
    public Result<ParameterConfig> findById(String id) {
        try {
            return ResultUtil.success("成功", parameterConfigService.findById(id));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }
}
@ApiModel(value = "ParameterConfigInsertVm", description = "新增")
class ParameterConfigInsertVm {
    @ApiModelProperty("名称")
    @NotBlank(message = "类型名称不可为空")
    private String name;
    @ApiModelProperty("配置内容")
    private String content;
    @ApiModelProperty("备注")
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

@ApiModel(value = "ParameterConfigUpdateVm", description = "编辑")
class ParameterConfigUpdateVm {

    @ApiModelProperty("参数配置id")
    @NotBlank(message = "参数配置id不可为空")
    private String id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("配置内容")
    private String content;
    @ApiModelProperty("备注")
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

@ApiModel(value = "ParameterConfigFindAllVm", description = "查询")
class ParameterConfigFindAllVm extends PageableVm {
    @ApiModelProperty(value = "名称")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
