package d1.project.tangshan.operation.manage.controller.webadmin.deployment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.tangshan.operation.manage.entity.AutomatedDeploymentEntity;
import d1.project.tangshan.operation.manage.model.PageableVm;
import d1.project.tangshan.operation.manage.service.AutomatedDeploymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @Author chengh
 **/
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/deployment")
@Api(value = "/webadmin/deployment", tags = "自动化部署管理")
public class AutomatedDeploymentController {

    private final AutomatedDeploymentService automatedDeploymentService;

    @Autowired
    public AutomatedDeploymentController(AutomatedDeploymentService automatedDeploymentService) {
        this.automatedDeploymentService = automatedDeploymentService;
    }

    @ApiOperation(value = "查询", notes = "查询所有数据")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Page<AutomatedDeploymentEntity>> findAll(HttpServletRequest request, @Valid DeploymentFindAllVm model) {
        try {
            Page<AutomatedDeploymentEntity> result = automatedDeploymentService.findAll((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success("查询成功", result);
        } catch (Exception e) {
            return ResultUtil.fail("查询失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "详情")
    @GetMapping(value = "/findById")
    public Result<AutomatedDeploymentEntity> findById(String id) {
        try {
            return ResultUtil.success("成功", automatedDeploymentService.findById(id));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "插入", notes = "插入数据")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result<String> insert(HttpServletRequest request, @Valid @RequestBody DeploymentInsertVm model) {
        try {
            this.automatedDeploymentService.insert((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success("添加成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "更新", notes = "更新数据")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<String> update(HttpServletRequest request, @Valid @RequestBody DeploymentUpdateVm model) {
        try {
            this.automatedDeploymentService.update((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success("编辑成功");
        } catch (Exception e) {
            return ResultUtil.fail( e.getMessage(), e);
        }
    }

    @ApiOperation(value = "删除", notes = "删除数据")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<String> delete(@Valid @RequestBody DeploymentDeleteVm model,HttpServletRequest request) {
        try {
            this.automatedDeploymentService.delete(model.getId(),request);
            return ResultUtil.success("删除成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "部署")
    @RequestMapping(value = "/deployment", method = RequestMethod.GET)
    public Result<String> deployment(HttpServletRequest request, @Valid @NotBlank(message = "id不能为空") String id) {
        try {
            this.automatedDeploymentService.deployment(id, request);
            return ResultUtil.success("执行成功");
        } catch (Exception e) {
            return ResultUtil.fail( e.getMessage(), e);
        }
    }

    @ApiOperation(value = "版本回滚", notes = "执行")
    @RequestMapping(value = "/rollback", method = RequestMethod.GET)
    public Result<String> rollback(HttpServletRequest request, @Valid @NotBlank(message = "id不能为空") String id) {
        try {
            this.automatedDeploymentService.rollback(id, request);
            return ResultUtil.success("执行成功");
        } catch (Exception e) {
            return ResultUtil.fail( e.getMessage(), e);
        }
    }

}

class DeploymentFindAllVm extends PageableVm {
    @ApiModelProperty("节点id")
    private String nodeId;
    @ApiModelProperty("系统id")
    private String sysId;
    @ApiModelProperty("部署类型")
    private String type;
    /*部署阶段:未部署:NotDeployed,部署中:Deploying,已部署:Deployed,不可部署:NotDeployAble,初次部署InitDeployAble*/
    @ApiModelProperty("部署阶段:未部署:NotDeployed,部署中:Deploying,已部署:Deployed,不可部署:NotDeployAble,初次部署InitDeployAble")
    private List<String> typeStageIn;



    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getTypeStageIn() {
        return typeStageIn;
    }

    public void setTypeStageIn(List<String> typeStageIn) {
        this.typeStageIn = typeStageIn;
    }
}

class DeploymentInsertVm {
    @NotBlank(message = "节点id不能为空")
    @ApiModelProperty("节点id")
    private String nodeId;
    @NotBlank(message = "节点名称不能为空")
    @ApiModelProperty("节点名称")
    private String nodeName;
    @NotBlank(message = "系统id不能为空")
    @ApiModelProperty("系统id")
    private String sysId;
    @NotBlank(message = "系统名称不能为空")
    @ApiModelProperty("系统名称")
    private String sysName;
    @ApiModelProperty("接口")
    @NotBlank(message = "接口不能为空")
    private String apiUrl;
    @ApiModelProperty("接口请求方式：get，post，put，delete")
    @NotBlank(message = "接口请求方式不能为空")
    private String apiMethod;

    @ApiModelProperty("部署类型")
    private String type;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("安装路径id")
    @NotBlank(message = "安装路径id不能为空")
    private String installPathId;
    @NotBlank(message = "版本号不能为空")
    @ApiModelProperty("版本号")
    private String versionNumber;
    @NotBlank(message = "部署包路径不能为空")
    @ApiModelProperty("部署包路径")
    private String automatedPackPath;

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getApiMethod() {
        return apiMethod;
    }

    public void setApiMethod(String apiMethod) {
        this.apiMethod = apiMethod;
    }

    //    @ApiModelProperty("接口id")
//    @NotBlank(message = "接口id不能为空")
//    private String apiId;
//    @ApiModelProperty("接口名称")
//    @NotBlank(message = "接口名称不能为空")
//    private String apiName;

    public String getInstallPathId() {
        return installPathId;
    }

    public void setInstallPathId(String installPathId) {
        this.installPathId = installPathId;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getAutomatedPackPath() {
        return automatedPackPath;
    }

    public void setAutomatedPackPath(String automatedPackPath) {
        this.automatedPackPath = automatedPackPath;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
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

class DeploymentUpdateVm {
    @NotBlank(message = "id不能为空")
    @ApiModelProperty("id")
    private String id;
    @NotBlank(message = "节点id不能为空")
    @ApiModelProperty("节点id")
    private String nodeId;
    @NotBlank(message = "节点名称不能为空")
    @ApiModelProperty("节点名称")
    private String nodeName;
    @NotBlank(message = "系统id不能为空")
    @ApiModelProperty("系统id")
    private String sysId;
    @NotBlank(message = "系统名称不能为空")
    @ApiModelProperty("系统名称")
    private String sysName;
    @ApiModelProperty("接口")
    @NotBlank(message = "接口不能为空")
    private String apiUrl;
    @ApiModelProperty("接口请求方式：get，post，put，delete")
    @NotBlank(message = "接口请求方式不能为空")
    private String apiMethod;
    @ApiModelProperty("部署类型")
    private String type;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("安装路径id")
    @NotBlank(message = "安装路径id不能为空")
    private String installPathId;
    @NotBlank(message = "部署包路径不能为空")
    @ApiModelProperty("部署包路径")
    private String automatedPackPath;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getInstallPathId() {
        return installPathId;
    }

    public void setInstallPathId(String installPathId) {
        this.installPathId = installPathId;
    }

    public String getAutomatedPackPath() {
        return automatedPackPath;
    }

    public void setAutomatedPackPath(String automatedPackPath) {
        this.automatedPackPath = automatedPackPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getApiMethod() {
        return apiMethod;
    }

    public void setApiMethod(String apiMethod) {
        this.apiMethod = apiMethod;
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

class DeploymentDeleteVm {
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

class ExecuteUpdateVm {
    @NotBlank(message = "id不能为空")
    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("请求参数")
    private String params;
    @ApiModelProperty("请求体")
    private String body;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}

