package d1.project.tangshan.operation.manage.controller.webadmin.deployment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.tangshan.operation.manage.entity.InstallPathEntity;
import d1.project.tangshan.operation.manage.model.PageableVm;
import d1.project.tangshan.operation.manage.service.InstallPathService;
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
 * @Author chengh
 **/
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/installPath")
@Api(value = "/webadmin/installPath", tags = "安装路径管理")
public class InstallPathController {

    private final InstallPathService installPathService;

    @Autowired
    public InstallPathController(InstallPathService installPathService) {
        this.installPathService = installPathService;
    }

    @ApiOperation(value = "查询", notes = "查询所有数据")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Page<InstallPathEntity>> findAll(HttpServletRequest request, @Valid InstallPathFindAllVm model) {
        try {
            Page<InstallPathEntity> result = installPathService.findAll((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success("查询成功", result);
        } catch (Exception e) {
            return ResultUtil.fail("查询失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "详情")
    @GetMapping(value = "/findById")
    public Result<InstallPathEntity> findById(String id) {
        try {
            return ResultUtil.success("成功", installPathService.findById(id));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "插入", notes = "插入数据")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result<String> insert(HttpServletRequest request, @Valid @RequestBody InstallPathInsertVm model) {
        try {
            this.installPathService.insert((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success("添加成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "更新", notes = "更新数据")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<String> update(HttpServletRequest request, @Valid @RequestBody InstallPathUpdateVm model) {
        try {
            this.installPathService.update((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success("编辑成功");
        } catch (Exception e) {
            return ResultUtil.fail( e.getMessage(), e);
        }
    }

    @ApiOperation(value = "删除", notes = "删除数据")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<String> delete(@Valid @RequestBody InstallPathDeleteVm model, HttpServletRequest request) {
        try {
            this.installPathService.delete(model.getId(), request);
            return ResultUtil.success("删除成功");
        } catch (Exception e) {
            return ResultUtil.fail( e.getMessage(), e);
        }
    }
}

class InstallPathFindAllVm extends PageableVm {
    @ApiModelProperty("节点id")
    private String nodeId;
    @ApiModelProperty("系统id")
    private String sysId;

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
}

class InstallPathInsertVm {
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
    @ApiModelProperty("安装路径")
    @NotBlank(message = "安装路径不能为空")
    private String path;
    @ApiModelProperty("操作系统，windows、centos7")
    private String os;
    @ApiModelProperty("备注")
    private String remark;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

class InstallPathUpdateVm {
    @NotBlank(message = "id不能为空")
    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("安装路径")
    private String path;
    @ApiModelProperty("操作系统，windows、centos7")
    private String os;
    @ApiModelProperty("备注")
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

class InstallPathDeleteVm {
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

