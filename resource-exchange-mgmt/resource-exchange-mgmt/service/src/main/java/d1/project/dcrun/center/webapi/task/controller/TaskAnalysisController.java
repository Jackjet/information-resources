package d1.project.dcrun.center.webapi.task.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.dcrun.center.webapi.task.entity.TaskAnalysis;
import d1.project.dcrun.center.webapi.task.service.TaskAnalysisService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhengyang
 */
@Auth("integration")
@RestController
@RequestMapping("/webadmin/task/analysis")
public class TaskAnalysisController {
    private final TaskAnalysisService taskAnalysisService;

    public TaskAnalysisController(TaskAnalysisService taskAnalysisService) {
        this.taskAnalysisService = taskAnalysisService;
    }

    /**
     * 明细(天)
     */
    @GetMapping(value = "/findAnalysisByDay")
    public Result<Page<TaskAnalysis>> findAnalysisByDay(Pageable model) {
        try {
            return ResultUtil.success("", taskAnalysisService.findAnalysisByDay((JSONObject) JSON.toJSON(model)));
        }catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    /**
     * 明细(月)
     */
    @GetMapping(value = "/findAnalysisByMonth")
    public Result<Page<TaskAnalysis>> findAnalysisByMonth(Pageable model) {
        try {
            return ResultUtil.success("", taskAnalysisService.findAnalysisByMonth((JSONObject) JSON.toJSON(model)));
        }catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }
}