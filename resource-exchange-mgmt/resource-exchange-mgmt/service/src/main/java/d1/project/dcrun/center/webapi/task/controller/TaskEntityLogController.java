package d1.project.dcrun.center.webapi.task.controller;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.dcrun.center.webapi.task.entity.TaskEntityLog;
import d1.project.dcrun.center.webapi.task.model.vm.TaskEntityLogInsertVm;
import d1.project.dcrun.center.webapi.task.service.TaskEntityLogService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhengyang
 */
@RestController
@RequestMapping("/webadmin/task/entityLogs")
public class TaskEntityLogController {
    private final TaskEntityLogService taskEntityLogService;

    public TaskEntityLogController(TaskEntityLogService taskEntityLogService) {
        this.taskEntityLogService = taskEntityLogService;
    }

    /**
     * 添加信息
     */
    @Auth("noauth")
    @PostMapping(value = "/insert")
    public Result<String> insert(@RequestBody TaskEntityLog taskEntityLog, HttpServletRequest request) {
        try {
            taskEntityLogService.insert(taskEntityLog);
            return ResultUtil.success("SUCCESS");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

    /**
     * 接收结束消息，解析任务日志
     */
    @Auth("noauth")
    @PostMapping(value = "/end")
    public Result<String> insert(@RequestBody TaskEntityLogInsertVm taskEntityLogInsertVm, HttpServletRequest request) {
        try {
            taskEntityLogService.end(taskEntityLogInsertVm);
            return ResultUtil.success("SUCCESS");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }
}