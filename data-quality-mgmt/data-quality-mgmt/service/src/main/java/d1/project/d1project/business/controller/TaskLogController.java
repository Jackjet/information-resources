package d1.project.d1project.business.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.d1project.business.entity.TaskLog;
import d1.project.d1project.business.model.task.log.TaskLogFindAllGetVm;
import d1.project.d1project.business.service.task.TaskLogService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 任务日志
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/task/log")
public class TaskLogController {
    private final TaskLogService taskLogService;

    public TaskLogController(TaskLogService taskLogService) {
        this.taskLogService = taskLogService;
    }

    /**
     * 查询日志
     */
    @GetMapping("/findAll")
    public Result<Page<TaskLog>> findAll(@Valid TaskLogFindAllGetVm param){
        try{
            return ResultUtil.success("成功",taskLogService.findAll((JSONObject) JSON.toJSON(param)));
        }catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }
}
