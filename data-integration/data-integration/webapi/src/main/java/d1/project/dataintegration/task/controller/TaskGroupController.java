package d1.project.dataintegration.task.controller;

import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.dataintegration.common.annotation.ApiAuth;
import d1.project.dataintegration.task.model.vm.TaskGroupInsertVm;
import d1.project.dataintegration.task.model.vm.TaskGroupTree;
import d1.project.dataintegration.task.model.vm.TaskGroupUpdateVm;
import d1.project.dataintegration.task.service.TaskGroupService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author zhengyang
 */
//@Auth("webadmin")
@ApiAuth("webadmin")
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
    public Result<String> insert(@RequestBody @Valid TaskGroupInsertVm insertVm, HttpServletRequest request) throws DoValidException {
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
    public Result<String> update(@RequestBody TaskGroupUpdateVm updateVm, HttpServletRequest request) throws DoValidException {
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
    public Result<String> deleteById(String id) throws DoValidException {
        try {
            taskGroupService.deleteById(id);
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