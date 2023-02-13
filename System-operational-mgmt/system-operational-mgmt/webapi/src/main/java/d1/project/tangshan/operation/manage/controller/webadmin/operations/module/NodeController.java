package d1.project.tangshan.operation.manage.controller.webadmin.operations.module;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.tangshan.operation.manage.entity.operations.module.Node;
import d1.project.tangshan.operation.manage.entity.repository.KnowledgeModel;
import d1.project.tangshan.operation.manage.service.operations.module.NodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author lin
 */
@Auth("webadmin")
@RestController("/webadmin/operations/node")
@RequestMapping("/webadmin/operations/node")
@Api(value = "/webadmin/operations/node", tags = "节点管理")
public class NodeController {
    private final NodeService nodeService;

    public NodeController(NodeService nodeService) {
        this.nodeService = nodeService;
    }


    @ApiOperation(value = "新增")
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody NodeInsertVm model, HttpServletRequest request) {
        try {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(model);
            nodeService.insert(jsonObject, request);
            return ResultUtil.success("添加成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "删除节点")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(String id, HttpServletRequest request) {
        try {
            nodeService.delete(id, request);
            return ResultUtil.success("删除成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "节点编辑")
    @PutMapping(value = "/update")
    public Result<String> update(HttpServletRequest request, @Valid @RequestBody NodeUpdateVm model) {
        try {
            nodeService.update(request, (JSONObject) JSON.toJSON(model));
            return ResultUtil.success("编辑成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "节点列表（分页）")
    @GetMapping(value = "/findAll")
    public Result<Page<Node>> findAll(NodeFindAllVm model) {
        try {
            Page<Node> result = nodeService.findAll((JSONObject) JSON.toJSON(model));
            return ResultUtil.success("成功", result);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "详情")
    @GetMapping(value = "/findById")
    public Result<KnowledgeModel> findById(String id) {
        try {
            return ResultUtil.success("成功", nodeService.findById(id));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "节点列表")
    @GetMapping(value = "/findAllNodes")
    public Result<List<Node>> findAllNodes() {
        try {
            List<Node> result = nodeService.findAllNodes();
            return ResultUtil.success("成功", result);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }
}

@ApiModel(value = "NodeInsertVm", description = "新增")
class NodeInsertVm {
    @ApiModelProperty("名称")
    @NotBlank(message = "节点名称不可为空")
    private String name;
    @ApiModelProperty("公网IP")
    private String publicIp;
    @ApiModelProperty("内网IP")
    private String intranetIp;
    @ApiModelProperty("用途")
    private String purpose;
    @ApiModelProperty("配置")
    private String configuration;
    @ApiModelProperty("备注")
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublicIp() {
        return publicIp;
    }

    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
    }

    public String getIntranetIp() {
        return intranetIp;
    }

    public void setIntranetIp(String intranetIp) {
        this.intranetIp = intranetIp;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

@ApiModel(value = "NodeUpdateVm", description = "编辑")
class NodeUpdateVm {

    @ApiModelProperty("节点id")
    @NotBlank(message = "节点id不可为空")
    private String id;
    @ApiModelProperty("公网IP")
    private String publicIp;
    @ApiModelProperty("内网IP")
    private String intranetIp;
    @ApiModelProperty("用途")
    private String purpose;
    @ApiModelProperty("配置")
    private String configuration;
    @ApiModelProperty("备注")
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublicIp() {
        return publicIp;
    }

    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
    }

    public String getIntranetIp() {
        return intranetIp;
    }

    public void setIntranetIp(String intranetIp) {
        this.intranetIp = intranetIp;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

@ApiModel(value = "NodeFindAllVm", description = "查询")
class NodeFindAllVm extends PageableVm {
    @ApiModelProperty(value = "节点名称")
    private String name;
    @ApiModelProperty("公网IP")
    private String publicIp;
    @ApiModelProperty("内网IP")
    private String intranetIp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublicIp() {
        return publicIp;
    }

    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
    }

    public String getIntranetIp() {
        return intranetIp;
    }

    public void setIntranetIp(String intranetIp) {
        this.intranetIp = intranetIp;
    }
}
