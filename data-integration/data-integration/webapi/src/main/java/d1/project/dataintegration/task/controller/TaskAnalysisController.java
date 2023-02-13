package d1.project.dataintegration.task.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.dataintegration.common.annotation.ApiAuth;
import d1.project.dataintegration.task.entity.TaskAnalysis;
import d1.project.dataintegration.task.service.TaskAnalysisService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhengyang
 */
//@Auth("webadmin")
@ApiAuth("webadmin")
@RestController
@RequestMapping("/webadmin/task/analysis")
public class TaskAnalysisController {
    private final TaskAnalysisService taskAnalysisService;

    public TaskAnalysisController(TaskAnalysisService taskAnalysisService) {
        this.taskAnalysisService = taskAnalysisService;
    }

    /**
     * 当月明细(天)
     */
    @GetMapping(value = "/findAnalysisByDay")
    public Result<Page<TaskAnalysis>> findAnalysisByDay(PageableVm model) throws Exception {
        return ResultUtil.success("", taskAnalysisService.findAnalysisByDay((JSONObject) JSON.toJSON(model)));
    }

    /**
     * 明细(月)
     */
    @GetMapping(value = "/findAnalysisByMonth")
    public Result<Page<TaskAnalysis>> findAnalysisByMonth(PageableVm model) throws Exception {
        return ResultUtil.success("", taskAnalysisService.findAnalysisByMonth((JSONObject) JSON.toJSON(model)));
    }
}