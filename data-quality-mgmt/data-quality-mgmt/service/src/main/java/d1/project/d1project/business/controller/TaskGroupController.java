package d1.project.d1project.business.controller;

import com.alibaba.fastjson.JSONArray;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.d1project.business.model.task.group.TaskGroupInsertPostVm;
import d1.project.d1project.business.model.task.group.TaskGroupUpdatePutVm;
import d1.project.d1project.business.service.task.TaskGroupService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 任务组
 *
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/task")
public class TaskGroupController {
    private final TaskGroupService taskGroupService;

    public TaskGroupController(TaskGroupService taskGroupService) {
        this.taskGroupService = taskGroupService;
    }

    /**
     * 查询任务组
     */
    @GetMapping("/getTaskGroup")
    public Result<JSONArray> getTaskGroup() {
        try {
            return ResultUtil.success("成功", taskGroupService.getTaskGroup());
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }

    /**
     * 添加任务组
     */
    @PostMapping("/groupInsert")
    public Result<String> groupInsert(@Valid @RequestBody TaskGroupInsertPostVm params) {
        try {
            taskGroupService.insert(params);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }

    /**
     * 修改任务组
     */
    @PutMapping("/groupUpdate")
    public Result<String> groupUpdate(@Valid @RequestBody TaskGroupUpdatePutVm params) {
        try {
            taskGroupService.update(params);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }

    /**
     * 删除任务组
     */
    @DeleteMapping("/groupDelete")
    public Result<String> groupDelete(@Valid @NotBlank(message = "组id不能为空") String id) {
        try {
            taskGroupService.delete(id);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }
}