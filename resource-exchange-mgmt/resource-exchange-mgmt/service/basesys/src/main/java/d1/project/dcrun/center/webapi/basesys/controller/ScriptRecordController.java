package d1.project.dcrun.center.webapi.basesys.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.dcrun.center.webapi.basesys.model.ServiceComponent;
import d1.project.dcrun.center.webapi.basesys.service.ScriptRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import net.dcrun.component.file.server.IFileServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 因不需要操作数据库，因此不继承统一父类
 *
 * @author libin
 */
@Auth("integration")
@RestController
@RequestMapping("/integration/script")
@Api(value = "/integration/script", tags = "应用脚本管理")
public class ScriptRecordController {
    @Autowired
    private ScriptRecordService scriptRecordService;
    @Autowired
    private IFileServerService fileServerService;

    @ApiOperation(value = "微应用文件树", notes = "微应用文件树")
    @RequestMapping(value = "/file/find", method = RequestMethod.GET)
    public Result<JSONObject> fileTree(@Valid ScriptFileTreeGetVm model) {
        try {
            JSONObject fileTree = JSONObject.parseObject(this.scriptRecordService.fileTree(model.getIntegrationId(), model.getAppid()));
            return ResultUtil.success("文件树获取成功", fileTree);
        } catch (Exception e) {
            return ResultUtil.fail("文件树获取失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "新增文件/文件夹", notes = "新增文件/文件夹")
    @RequestMapping(value = "/file/new", method = RequestMethod.POST)
    public Result<String> fileNew(@Valid @RequestBody ScriptFileNewPostVm model) {
        try {
            this.scriptRecordService.fileNew(model.getType(), model.getPath());
            return ResultUtil.success("新增文件成功");
        } catch (Exception e) {
            return ResultUtil.fail("新增文件失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "删除文件/文件夹", notes = "删除文件/文件夹")
    @RequestMapping(value = "/file/delete", method = RequestMethod.DELETE)
    public Result<String> fileDelete(String path) {
        try {
            this.scriptRecordService.fileDelete(path);
            return ResultUtil.success("删除文件成功");
        } catch (Exception e) {
            return ResultUtil.fail("删除文件失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "保存应用脚本", notes = "保存应用脚本")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result<String> save(@Valid @RequestBody ScriptSavePostVm model) {
        try {
            this.scriptRecordService.save(model.getPath(), model.getContent());
            return ResultUtil.success("保存成功");
        } catch (Exception e) {
            return ResultUtil.fail("保存失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "读出脚本内容", notes = "读出脚本内容")
    @RequestMapping(value = "/read", method = RequestMethod.GET)
    public Result<String> read(@Valid ScriptReadGetVm model) {
        try {
            return ResultUtil.success("读取成功", this.scriptRecordService.read(model.getPath()));
        } catch (Exception e) {
            return ResultUtil.fail("读取失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "上传脚本", notes = "上传脚本")
    @RequestMapping(value = "/uploadScript", method = RequestMethod.POST)
    public Result<String> uploadScript(@RequestParam("file") MultipartFile file, String path) {
        try {
            fileServerService.uploadFileWithSpecialName(file, path, "none", file.getOriginalFilename());
            return ResultUtil.success("上传成功");
        } catch (Exception e) {
            return ResultUtil.fail("上传失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "重命名文件/文件夹", notes = "重命名文件/文件夹")
    @RequestMapping(value = "/rename", method = RequestMethod.PUT)
    public Result<String> rename(@Valid @RequestBody ScriptRenamePutVm model) {
        try {
            this.scriptRecordService.rename(model.getOldPath(), model.getName());
            return ResultUtil.success("重命名成功");
        } catch (Exception e) {
            return ResultUtil.fail("重命名失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "脚本版本号", notes = "脚本版本号")
    @RequestMapping(value = "/version", method = RequestMethod.GET)
    public Result<String> version(@Valid ScriptIdVm model) {
        try {
            return ResultUtil.success("获取成功", scriptRecordService.version((JSONObject) JSON.toJSON(model)));
        } catch (Exception e) {
            return ResultUtil.fail("获取失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "日志订阅地址信息", notes = "日志订阅地址信息")
    @RequestMapping(value = "/wsInfo", method = RequestMethod.GET)
    public Result<JSONObject> wsInfo(@Valid ScriptIdVm model) {
        try {
            return ResultUtil.success("获取成功", scriptRecordService.getWsInfo((JSONObject) JSON.toJSON(model)));
        } catch (Exception e) {
            return ResultUtil.fail("获取失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "修改调试模式")
    @RequestMapping(value = "/updateDebug", method = RequestMethod.PUT)
    public Result<String> updateDebug(@Valid @RequestBody ScriptDebugPutVm model) {
        try {
            this.scriptRecordService.updateDebug((JSONObject) JSON.toJSON(model));
            return ResultUtil.success("调试模式修改成功");
        } catch (Exception e) {
            return ResultUtil.fail("调试模式修改失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "查看调试模式")
    @RequestMapping(value = "/findDebug", method = RequestMethod.GET)
    public Result<String> findDebug(@Valid ScriptDebugGetVm model) {
        try {
            return ResultUtil.success("获取成功", scriptRecordService.findDebug((JSONObject) JSON.toJSON(model)));
        } catch (Exception e) {
            return ResultUtil.fail("获取失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "根据系统服务appid，查询系统服务所选择的组件信息")
    @RequestMapping(value = "/findByAppid", method = RequestMethod.GET)
    public Result<List<ServiceComponent>> findByAppid(@Valid ScriptComponentVm model) {
        try {
            return ResultUtil.success("获取成功", scriptRecordService.findByAppid((JSONObject) JSON.toJSON(model)));
        } catch (Exception e) {
            return ResultUtil.fail("获取失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "脚本运行服务的IDE自动提示", notes = "包含所有选择组件的描述")
    @RequestMapping(value = "/getComponentsHints", method = RequestMethod.GET)
    public Result<JSONObject> getComponentsHints(String integrationId, String appid) {
        try {
            return ResultUtil.success("获取成功", scriptRecordService.getComponentsHints(integrationId, appid));
        } catch (Exception e) {
            return ResultUtil.fail("获取失败：" + e.getMessage(), e);
        }
    }
}

@ApiModel(value = "ScriptRenamePutVm", description = "微应用开发页面重命名")
class ScriptRenamePutVm {

    @NotBlank(message = "原始文件/文件夹全路径名不能为空")
    @ApiModelProperty(value = "原始文件/文件夹全路径名")
    private String oldPath;

    @NotBlank(message = "新文件/文件夹名不能为空")
    @ApiModelProperty(value = "新文件/文件夹名")
    private String name;

    public String getOldPath() {
        return oldPath;
    }

    public void setOldPath(String oldPath) {
        this.oldPath = oldPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

@ApiModel(value = "ScriptIdVm", description = "IdVm")
class ScriptIdVm {
    @NotBlank(message = "系统服务appid不能为空")
    @ApiModelProperty(value = "系统服务appid")
    private String appid;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
}

@ApiModel(value = "ScriptFileTreeGetVm", description = "微应用开发页面文件树")
class ScriptFileTreeGetVm {
    @NotBlank(message = "用户id不能为空")
    @ApiModelProperty(value = "用户id")
    private String integrationId;

    @NotBlank(message = "系统服务appid不能为空")
    @ApiModelProperty(value = "系统服务appid")
    private String appid;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
    }
}

@ApiModel(value = "ScriptFileNewPostVm", description = "新增文件/文件夹")
class ScriptFileNewPostVm {
    @NotBlank(message = "文件/文件夹全路径")
    @ApiModelProperty(value = "文件/文件夹文件全路径")
    private String path;

    @NotBlank(message = "文件/文件夹的名称不可为空")
    @ApiModelProperty(value = "文件/文件夹名字")
    private String name;

    @NotBlank(message = "文件/文件夹的类型不可为空")
    @ApiModelProperty(value = "类型，dir 、js、html、kjb、css、json")
    private String type;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

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

@ApiModel(value = "ScriptSavePostVm", description = "保存应用脚本")
class ScriptSavePostVm {
    @NotBlank(message = "脚本本地文件路径不能为空")
    @ApiModelProperty(value = "文件路径")
    private String path;

    @ApiModelProperty(value = "文件内容")
    private String content;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

@ApiModel(value = "ScriptReadGetVm", description = "读出文件内容")
class ScriptReadGetVm {
    @NotBlank(message = "脚本本地文件路径不能为空")
    @ApiModelProperty(value = "文件路径")
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

@ApiModel(value = "ScriptDebugPutVm", description = "修改脚本调试模式")
class ScriptDebugPutVm {
    @NotBlank(message = "用户id不能为空")
    @ApiModelProperty(value = "用户id")
    private String integrationId;
    @NotBlank(message = "系统服务appid不能为空")
    @ApiModelProperty(value = "系统服务appid")
    private String appid;
    @NotBlank(message = "调试模式")
    @ApiModelProperty(value = "调试模式不能为空")
    private String debug;

    public String getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }
}

@ApiModel(value = "ScriptDebugGetVm", description = "查询脚本调试模式")
class ScriptDebugGetVm {
    @NotBlank(message = "用户id不能为空")
    @ApiModelProperty(value = "用户id")
    private String integrationId;
    @NotBlank(message = "系统服务appid不能为空")
    @ApiModelProperty(value = "系统服务appid")
    private String appid;

    public String getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
}

@ApiModel(value = "ScriptComponentVm", description = "根据用户id和appid，查询组件信息")
class ScriptComponentVm {
    @NotBlank(message = "用户id不能为空")
    @ApiModelProperty(value = "用户id")
    private String integrationId;
    @NotBlank(message = "系统服务appid不能为空")
    @ApiModelProperty(value = "系统服务appid")
    private String appid;

    public String getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
}
