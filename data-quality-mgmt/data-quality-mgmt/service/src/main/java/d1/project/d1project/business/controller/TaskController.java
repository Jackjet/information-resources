package d1.project.d1project.business.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.d1project.business.model.task.TaskEnableOrForbiddenPutVm;
import d1.project.d1project.business.model.task.TaskFindAllGetVm;
import d1.project.d1project.business.model.task.TaskInsertPostVm;
import d1.project.d1project.business.model.task.TaskUpdatePutVm;
import d1.project.d1project.business.service.task.TaskService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 接入任务
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/task")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * 查询接入任务
     */
    @GetMapping("/findAll")
    public Result<JSONObject> findAll(TaskFindAllGetVm params) {
        try {
            return ResultUtil.success("成功", taskService.findAll((JSONObject) JSON.toJSON(params)));
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }

    /**
     * 添加接入任务
     */
    @PostMapping("/insert")
    public Result<String> insert(HttpServletRequest request, @Valid @RequestBody TaskInsertPostVm params) {
        try {
            taskService.insert(request, params);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }

    /**
     * 修改接入任务
     */
    @PutMapping("/update")
    public Result<String> update(@Valid @RequestBody TaskUpdatePutVm params) {
        try {
            taskService.update(params);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }

    /**
     * 启用或禁止接入任务
     */
    @PutMapping("/taskEnableOrForbidden")
    public Result<String> taskEnableOrForbidden(@Valid @RequestBody TaskEnableOrForbiddenPutVm params) {
        try {
            taskService.taskEnableOrForbidden(params.getId());
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }

    /**
     * 执行一次
     */
    @GetMapping("/excuteOnce")
    public Result<String> excuteOnce(@Valid @NotBlank(message = "id不能为空") String id ) {
        try {
            taskService.executeTask(id);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }

    /**
     * 查询接入任务详情
     */
    @GetMapping("/findById")
    public Result<JSONObject> findById(@Valid @NotBlank(message = "id不能为空") String id) {
        try {
            return ResultUtil.success("成功", taskService.findById(id));
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }

    /**
     * 删除接入任务
     */
    @DeleteMapping("/deleteById")
    public Result<String> deleteById(@Valid @NotBlank(message = "id不能为空") String id) {
        try {
            taskService.deleteById(id);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }
}