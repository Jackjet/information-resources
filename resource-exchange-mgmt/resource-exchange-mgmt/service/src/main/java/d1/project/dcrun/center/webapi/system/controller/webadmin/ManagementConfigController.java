package d1.project.dcrun.center.webapi.system.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.dcrun.center.webapi.common.service.management.config.ManagementConfig;
import d1.project.dcrun.center.webapi.system.service.ManageConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author libin
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/managementConfig")
@Api(value = "/webadmin/managementConfig", tags = "管理中心服务配置")
public class ManagementConfigController {
    @Autowired
    private ManageConfigService manageConfigService;

    @ApiOperation(value = "查询管理中心服务配置", notes = "查询管理中心服务配置")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<List<ManagementConfig>> findAll() {
        try {
            List<ManagementConfig> result = manageConfigService.findAll();
            return ResultUtil.success("获取成功", result);
        } catch (Exception e) {
            return ResultUtil.fail("获取失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "编辑管理中心服务配置", notes = "编辑管理中心服务配置")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<String> update(HttpServletRequest request, @RequestBody Map<String, String> model) {
        try {
            manageConfigService.update(request, model);
            return ResultUtil.success("修改成功");
        } catch (Exception e) {
            return ResultUtil.fail("修改失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "获取数据详细信息", notes = "根据id来获取详细信息")
    @ApiImplicitParam(name = "id", value = "数据ID", required = true, dataType = "String")
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public Result<ManagementConfig> findById(String id) {
        ManagementConfig mUser = manageConfigService.findById(id);
        return mUser != null ? ResultUtil.success("获取详细信息", mUser) : ResultUtil.fail("数据为空或获取失败!");
    }


    @ApiOperation(value = "删除数据", notes = "根据id来指定删除数据")
    @ApiImplicitParam(name = "id", value = "数据ID", required = true, dataType = "String")
    @RequestMapping(value = "/deleteById", method = RequestMethod.DELETE)
    public Result delete(String id) {
        try {
            manageConfigService.delete(id);
        } catch (Exception e) {
            return ResultUtil.fail("删除失败:" + e.getMessage());
        }
        return ResultUtil.success("删除成功!");
    }
}
