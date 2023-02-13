package d1.project.dcrun.center.webapi.task.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.dcrun.center.webapi.common.utils.BaseUtils;
import d1.project.dcrun.center.webapi.task.dao.TaskAnalysisDao;
import d1.project.dcrun.center.webapi.task.dao.TaskLogsDao;
import d1.project.dcrun.center.webapi.task.entity.TaskAnalysis;
import d1.project.dcrun.center.webapi.task.model.TaskAnalysisVm;
import d1.project.dcrun.center.webapi.task.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


/**
 * @author zhengyang
 */
@Service
public class TaskAnalysisService {
    private final TaskAnalysisDao taskAnalysisDao;
    private final TaskLogsDao taskLogsDao;

    public TaskAnalysisService(TaskAnalysisDao taskAnalysisDao, TaskLogsDao taskLogsDao) {
        this.taskAnalysisDao = taskAnalysisDao;
        this.taskLogsDao = taskLogsDao;
    }

    /**
     * 明细(天)
     */
    public Page<TaskAnalysis> findAnalysisByDay(JSONObject model) throws Exception {
        String strDateFormat = "yyyy-MM-dd";
        model.put("type", 1);
        model.put("taskDate", DateUtils.getMonth());
        SpecificationBuilder<TaskAnalysis> builder = new SpecificationBuilder<>();
        Specification<TaskAnalysis> specification = builder.init(model)
                .sEqual("type", "type")
                .sContain("taskDate","taskDate")
                .dOrder("taskDate").build();
        return taskAnalysisDao.findAll(specification, builder.getPageable());
    }

    /**
     * 明细(月)
     */
    public Page<TaskAnalysis> findAnalysisByMonth(JSONObject model) throws Exception {
        model.put("type", 2);
        SpecificationBuilder<TaskAnalysis> builder = new SpecificationBuilder<>();
        Specification<TaskAnalysis> specification = builder.init(model)
                .sEqual("type", "type")
                .dOrder("taskDate").build();
        return taskAnalysisDao.findAll(specification, builder.getPageable());
    }

    /**
     * 每天日志分析
     */
    public void taskAnalysisByDay() {
        String yestoday = DateUtils.getYestoday();
        TaskAnalysis taskAnalysis = new TaskAnalysis();
        TaskAnalysisVm taskAnalysisVm = taskLogsDao.findByTaskAnalysisByDay(yestoday);
        if (taskAnalysisVm.getRealTimeTaskNum() == 0 && taskAnalysisVm.getTaskFailNum() == 0 &&
                taskAnalysisVm.getTaskSuccessNum() == 0 && taskAnalysisVm.getTimingTaskDataNum() == 0 &&
                taskAnalysisVm.getTimingTaskNum() == 0){
            return;
        }
        BeanUtils.copyProperties(taskAnalysisVm, taskAnalysis);
        taskAnalysis.setId(BaseUtils.generate32Id());
        taskAnalysis.setTaskDate(yestoday);
        taskAnalysis.setType(1);
        taskAnalysisDao.save(taskAnalysis);
    }

    /**
     * 每月日志分析
     */
    public void taskAnalysisByMonth() {
        String lastMonth = DateUtils.getLastMonth();
        TaskAnalysis taskAnalysis = new TaskAnalysis();
        TaskAnalysisVm taskAnalysisVm = taskLogsDao.findByTaskAnalysisByMonth(lastMonth);
        if (taskAnalysisVm.getRealTimeTaskNum() == 0 && taskAnalysisVm.getTaskFailNum() == 0 &&
                taskAnalysisVm.getTaskSuccessNum() == 0 && taskAnalysisVm.getTimingTaskDataNum() == 0 &&
                taskAnalysisVm.getTimingTaskNum() == 0){
            return;
        }
        BeanUtils.copyProperties(taskAnalysisVm, taskAnalysis);
        taskAnalysis.setId(BaseUtils.generate32Id());
        taskAnalysis.setTaskDate(lastMonth);
        taskAnalysis.setType(2);
        taskAnalysisDao.save(taskAnalysis);
    }
}
