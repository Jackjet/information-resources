package d1.project.dcrun.center.webapi.task.controller;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.dcrun.center.webapi.task.model.vm.TaskGroupInsertVm;
import d1.project.dcrun.center.webapi.task.model.vm.TaskGroupTree;
import d1.project.dcrun.center.webapi.task.model.vm.TaskGroupUpdateVm;
import d1.project.dcrun.center.webapi.task.service.TaskGroupService;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author zhengyang
 */
@Auth("integration")
@RestController
@RequestMapping("/webadmin/task/group")
public class TaskGroupController {
    private final TaskGroupService taskGroupService;

    public TaskGroupController(TaskGroupService taskGroupService) {
        this.taskGroupService = taskGroupService;
    }

    /**
     * 获取树形结构
     */
    @GetMapping(value = "/getTree")
    public Result<List<TaskGroupTree>> getTree(String name) {
        try {
            return ResultUtil.success("SUCCESS", taskGroupService.findTreeList(name));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

    /**
     * 添加信息
     */
    @PostMapping(value = "/insert")
    public Result<String> insert(@RequestBody @Valid TaskGroupInsertVm insertVm, HttpServletRequest request) {
        try {
            taskGroupService.insert(insertVm, request);
            return ResultUtil.success("SUCCESS");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

    /**
     * 修改名称
     */
    @PutMapping(value = "/update")
    public Result<String> update(@RequestBody TaskGroupUpdateVm updateVm, HttpServletRequest request) {
        try {
            taskGroupService.update(updateVm, request);
            return ResultUtil.success("SUCCESS");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

    /**
     * 删除名称
     */
    @DeleteMapping(value = "/deleteById")
    public Result<String> deleteById(String id, HttpServletRequest request) {
        try {
            taskGroupService.deleteById(id, request);
            return ResultUtil.success("SUCCESS");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }
}

class TaskGroupFindAllVm extends PageableVm {
    @ApiModelProperty("组名称")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}