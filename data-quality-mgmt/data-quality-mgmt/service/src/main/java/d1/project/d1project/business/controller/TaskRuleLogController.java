package d1.project.d1project.business.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.d1project.business.model.task.log.TaskRuleLogFindAllGetVm;
import d1.project.d1project.business.service.task.TaskRuleLogService;
import d1.project.d1project.business.view.TaskRuleLogView;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 规则任务日志
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/task/rule/log")
public class TaskRuleLogController {
    private final TaskRuleLogService taskRuleLogService;

    public TaskRuleLogController(TaskRuleLogService taskRuleLogService) {
        this.taskRuleLogService = taskRuleLogService;
    }

    /**
     * 查询规则日志列表
     */
    @GetMapping("/findAll")
    public Result<Page<TaskRuleLogView>> findAll(@Valid TaskRuleLogFindAllGetVm param) {
        try {
            return ResultUtil.success("成功", taskRuleLogService.findAll((JSONObject) JSON.toJSON(param)));
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }

    /**
     * 查询规则日志详情
     */
    @GetMapping("/findById")
    public Result<TaskRuleLogView> findById(@Valid @NotBlank(message = "id不能为空") String id) {
        try {
            return ResultUtil.success("成功", taskRuleLogService.findById(id));
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }

    /**
     * 首页指标统计
     */
    @GetMapping("/statistics")
    public Result<String> statisticsCountGroupByRuleTemplateId(@Valid @NotBlank(message = "日期不能为空") String day) {
        try {
            return ResultUtil.success("成功", taskRuleLogService.statisticsCountGroupByRuleTemplateId(day));
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }
}
