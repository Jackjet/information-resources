package d1.project.dcrun.center.webapi.system.controller.integration;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.dcrun.center.webapi.common.PageableVm;
import d1.project.dcrun.center.webapi.common.util.Constants;
import d1.project.dcrun.center.webapi.system.entity.SysInstallPack;
import d1.project.dcrun.center.webapi.system.service.SysInstallPackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import net.dcrun.component.file.server.IFileServerService;
import net.dcrun.component.file.server.UploadResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.File;
import java.util.List;

/**
 * @author maorui
 */
@Auth("integration")
@RestController
@RequestMapping("/integration/sysServiceInstallPack")
@Api(value = "/integration/sysServiceInstallPack", tags = "系统服务安装包管理")
public class SysServiceInstallPackController {
    @Autowired
    private SysInstallPackService sysInstallPackService;

    @Autowired
    private IFileServerService fileServerService;

    @ApiOperation(value = "查询所有上传的插件安装包", notes = "查询所有上传的插件安装包")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Page<SysInstallPack>> findAll(HttpServletRequest request, SysServiceTemplateFindAllGetVm model) {
        try {
            Page<SysInstallPack> result = this.sysInstallPackService.findAllByUserAndNameAndType(request, model.getName(), model.getType(), model.getPage(), model.getSize());
            return ResultUtil.success("获取成功", result);
        } catch (Exception e) {
            return ResultUtil.fail("获取失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有上传的插件安装包的版本信息", notes = "查询所有上传的插件安装包的版本信息")
    @RequestMapping(value = "/findAllVersion", method = RequestMethod.GET)
    public Result<List<SysInstallPack>> findAllVersion(HttpServletRequest request, String type) {
        try {
            List<SysInstallPack> result = this.sysInstallPackService.findAllVersion(request, type);
            return ResultUtil.success("获取成功", result);
        } catch (Exception e) {
            return ResultUtil.fail("获取失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "新增系统服务模板", notes = "新增系统服务模板")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result<String> insert(HttpServletRequest request, @Valid @RequestBody SysServiceTemplateInsertPostVm model) {
        try {
            this.sysInstallPackService.insert(request, model.getName(), model.getType(), model.getVersion(), model.getFilename(), model.getRemark());
            return ResultUtil.success("创建成功");
        } catch (Exception e) {
            return ResultUtil.fail("创建失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "编辑系统服务模板", notes = "编辑系统服务模板")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<String> update(HttpServletRequest request, @Valid @RequestBody SysServiceTemplateUpdatePutVm model) {
        try {
            this.sysInstallPackService.update(request, model.getId(), model.getName(), model.getRemark());
            return ResultUtil.success("修改成功");
        } catch (Exception e) {
            return ResultUtil.fail("修改失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "上传插件安装包", notes = "上传插件安装包")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public UploadResult upload(@RequestParam("file") MultipartFile file, @NotBlank(message = "安装包类型不能为空") String type) throws Exception {
        return fileServerService.uploadFileWithSpecialName(file, Constants.getBasicInstallPath(type), type + File.separator + "install", file.getOriginalFilename());
    }
}

@ApiModel(value = "SysServiceTemplateFindAllGetVm", description = "查询系统服务安装包")
class SysServiceTemplateFindAllGetVm extends PageableVm {
    @ApiModelProperty(value = "安装包名称")
    private String name;

    @ApiModelProperty(value = "安装包类型")
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

@ApiModel(value = "SysServiceTemplateInsertPostVm", description = "新增系统服务安装包")
class SysServiceTemplateInsertPostVm {
    @ApiModelProperty(value = "安装包名称")
    @NotBlank(message = "安装包名称不能为空")
    private String name;

    @ApiModelProperty(value = "服务类型")
    @NotBlank(message = "服务类型不能为空")
    private String type;

    @ApiModelProperty(value = "服务版本")
    @NotBlank(message = "服务版本不能为空")
    private String version;

    @ApiModelProperty(value = "服务安装包文件名")
    @NotBlank(message = "服务安装包不能为空")
    private String filename;

    @ApiModelProperty(value = "备注")
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}


@ApiModel(value = "SysServiceTemplateUpdatePutVm", description = "编辑系统服务安装包")
class SysServiceTemplateUpdatePutVm {
    @ApiModelProperty(value = "唯一标识")
    @NotBlank(message = "唯一标识不能为空")
    private String id;

    @ApiModelProperty(value = "安装包名称")
    @NotBlank(message = "安装包名称不能为空")
    private String name;

    @ApiModelProperty(value = "备注")
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
