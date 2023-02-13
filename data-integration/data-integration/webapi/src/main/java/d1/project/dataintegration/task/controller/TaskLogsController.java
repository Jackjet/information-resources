package d1.project.dataintegration.task.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.dataintegration.common.annotation.ApiAuth;
import d1.project.dataintegration.task.entity.TaskLogs;
import d1.project.dataintegration.task.model.TaskLogsAnalysisVm;
import d1.project.dataintegration.task.model.TaskLogsInfoVm;
import d1.project.dataintegration.task.model.vm.TaskLogsInsertVm;
import d1.project.dataintegration.task.service.TaskLogsService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhengyang
 */
@RestController
@RequestMapping("/webadmin/task/logs")
public class TaskLogsController {
    private final TaskLogsService taskLogsService;

    public TaskLogsController(TaskLogsService taskLogsService) {
        this.taskLogsService = taskLogsService;
    }

//    @Auth("webadmin")
    @ApiAuth("webadmin")
    @GetMapping(value = "/findAll")
    public Result<Page<TaskLogs>> findAll(TaskLogsFindAllVm model) throws Exception {
        return ResultUtil.success("", taskLogsService.findAll((JSONObject) JSON.toJSON(model)));
    }

//    @Auth("webadmin")
    @ApiAuth("webadmin")
    @GetMapping(value = "/findInfo")
    public Result<TaskLogsInfoVm> findInfo(String taskId) throws Exception {
        return ResultUtil.success("", taskLogsService.findInfo(taskId));
    }

//    @Auth("webadmin")
    @ApiAuth("webadmin")
    @GetMapping(value = "/findAnalysisInfo")
    public Result<TaskLogsAnalysisVm> findAnalysisInfo(int mode) throws Exception {
        return ResultUtil.success("", taskLogsService.findAnalysisInfo(mode));
    }

    /**
     * 添加信息
     */
    @Auth("noauth")
    @PostMapping(value = "/insert")
    public Result<String> insert(@RequestBody TaskLogsInsertVm insertVm, HttpServletRequest request) throws DoValidException {
        try {
            taskLogsService.insert(insertVm, request);
            return ResultUtil.success("SUCCESS");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }
}

class TaskLogsFindAllVm extends PageableVm {
    @ApiModelProperty("任务id")
    private String taskId;

    @ApiModelProperty("执行状态")
    private String status;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}