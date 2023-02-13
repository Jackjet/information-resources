package d1.project.dcrun.center.webapi.backup.controller;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.dcrun.center.webapi.backup.entity.Backup;
import d1.project.dcrun.center.webapi.backup.service.BackupService;
import d1.project.dcrun.center.webapi.common.PageableVm;
import d1.project.dcrun.center.webapi.common.model.OperationLog;
import d1.project.dcrun.center.webapi.common.service.IOperationLogService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * @author xuaa
 */
@Auth("integration")
@RestController
@RequestMapping("/integration/backup")
@Api(value = "/integration/backup", tags = "备份管理")
public class BackupController {

    @Autowired
    private BackupService backupService;
    @Autowired
    private IOperationLogService iOperationLogService;

    @ApiOperation(value = "查询所有备份管理", notes = "查询所有备份管理")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Page<Backup>> findAll(BackupFindAllGetVm params) {
        try {
            return ResultUtil.success("获取成功", this.backupService.findAll((JSONObject) JSONObject.toJSON(params)));
        } catch (Exception e) {
            return ResultUtil.fail("获取失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "获取数据详细信息", notes = "根据id来获取详细信息")
    @ApiImplicitParam(name = "id", value = "数据ID", required = true, dataType = "String")
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public Result<Backup> findById(String id) {
        Backup backup = backupService.findById(id);
        return backup != null ? ResultUtil.success("获取详细信息", backup) : ResultUtil.fail("数据为空或获取失败!");
    }


    @ApiOperation(value = "删除数据", notes = "根据id来指定删除数据")
    @ApiImplicitParam(name = "id", value = "数据ID", required = true, dataType = "String")
    @RequestMapping(value = "/deleteById", method = RequestMethod.DELETE)
    public Result delete(String id, HttpServletRequest request) throws Exception {
        try {
            backupService.delete(id);
            iOperationLogService.insert(new OperationLog("备份管理-删除", "备份管理", "删除了备份", "删除了备份:" + id, 1), request);
        } catch (Exception e) {
            iOperationLogService.insert(new OperationLog("备份管理-删除", "备份管理", "删除了备份", "删除了备份:" + id, 0), request);
            return ResultUtil.fail("删除失败:" + e.getMessage());
        }
        return ResultUtil.success("删除成功!");
    }
}


@ApiModel(value = "TasksFindAllGetVm", description = "查询任务信息")
class BackupFindAllGetVm extends PageableVm {

    @ApiModelProperty(value = "任务名称")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
