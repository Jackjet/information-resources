package d1.project.tangshan.operation.manage.controller.webadmin.repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.tangshan.operation.manage.entity.repository.KnowledgeModel;
import d1.project.tangshan.operation.manage.service.repository.KnowledgeModelService;
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
@RestController("/webadmin/repository/knowledgeModel")
@RequestMapping("/webadmin/repository/knowledgeModel")
@Api(value = "/webadmin/repository/knowledgeModel", tags = "模型管理")
public class KnowledgeModelController {

    private final KnowledgeModelService modelService;

    public KnowledgeModelController(KnowledgeModelService modelService) {
        this.modelService = modelService;
    }


    @ApiOperation(value = "新增")
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody ModelInsertVm model, HttpServletRequest request) {
        try {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(model);
            modelService.insert(jsonObject, request);
            return ResultUtil.success("添加成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "删除模型")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(String id, HttpServletRequest request) {
        try {
            modelService.delete(id, request);
            return ResultUtil.success("删除成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "模型编辑")
    @PutMapping(value = "/update")
    public Result<String> update(HttpServletRequest request, @Valid @RequestBody ModelUpdateVm model) {
        try {
            modelService.update(request, (JSONObject) JSON.toJSON(model));
            return ResultUtil.success("编辑成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "模型列表")
    @GetMapping(value = "/findAll")
    public Result<Page<KnowledgeModel>> findAll(ModelFindAllVm model) {
        try {
            Page<KnowledgeModel> result = modelService.findAll((JSONObject) JSON.toJSON(model));
            return ResultUtil.success("成功", result);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "详情")
    @GetMapping(value = "/findById")
    public Result<KnowledgeModel> findById(String id) {
        try {
            return ResultUtil.success("成功", modelService.findById(id));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

}

@ApiModel(value = "ModelInsertVm", description = "新增")
class ModelInsertVm {
    @ApiModelProperty("名称")
    @NotBlank(message = "模型名称不可为空")
    private String name;

    @ApiModelProperty("类型")
    @NotBlank(message = "模型类型不可为空")
    private String type;

    @ApiModelProperty("备注")
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

@ApiModel(value = "ModelFindAllVm", description = "编辑")
class ModelUpdateVm {

    @ApiModelProperty("模型id")
    @NotBlank(message = "模型id不可为空")
    private String id;

    @ApiModelProperty("名称")
    @NotBlank(message = "模型名称不可为空")
    private String name;

    @ApiModelProperty("内容")
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

@ApiModel(value = "ModelFindAllVm", description = "设备查询")
class ModelFindAllVm extends PageableVm {
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "类型")
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
