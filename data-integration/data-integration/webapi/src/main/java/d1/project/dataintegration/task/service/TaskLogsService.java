package d1.project.dataintegration.task.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.dataintegration.common.utils.BaseUtils;
import d1.project.dataintegration.task.dao.TaskDao;
import d1.project.dataintegration.task.dao.TaskLogsDao;
import d1.project.dataintegration.task.entity.Task;
import d1.project.dataintegration.task.entity.TaskLogs;
import d1.project.dataintegration.task.model.TaskLogsAnalysisVm;
import d1.project.dataintegration.task.model.TaskLogsInfoVm;
import d1.project.dataintegration.task.model.vm.TaskLogsInsertVm;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.NumberFormat;

/**
 * @author zhengyang
 */
@Service
public class TaskLogsService {
    private final TaskLogsDao taskLogsDao;
    private final TaskDao taskDao;

    public TaskLogsService(TaskLogsDao taskLogsDao, TaskDao taskDao) {
        this.taskLogsDao = taskLogsDao;
        this.taskDao = taskDao;
    }

    /**
     * 查询所有
     *
     * @param model
     */
    public Page<TaskLogs> findAll(JSONObject model) throws Exception {
        SpecificationBuilder<TaskLogs> builder = new SpecificationBuilder<>();
        Specification<TaskLogs> specification = builder.init(model)
                .sEqual("taskId","taskId")
                .sEqual("status", "status")
                .dOrder("runTime")
                .build();
        return taskLogsDao.findAll(specification, builder.getPageable());
    }

    /**
     * 查询所有任务日志详情
     *
     * @param taskId
     */
    public TaskLogsInfoVm findInfo(String taskId) throws Exception {
        Task task = taskDao.findById(taskId).orElseThrow(() -> new DoValidException("任务不存在"));
        String taskFrequency = "";
        String repeats = task.getRepeats();
        if(!StringUtils.isEmpty(repeats)) {
            repeats = task.getRepeats().equals("Y") ? "(重复)" : "(不重复)";
        }
        if(task.getMode() == 1)
        {
            switch (task.getSchedulerType()) {
                case "1":
                    //时间
                    taskFrequency = repeats + "每" + task.getIntervalMinutes() +"分" + task.getIntervalSeconds() + "秒/次";
                    break;
                case "2":
                    taskFrequency = repeats + "每天" + task.getDayOfHour() +"点" + task.getDayOfMinutes() + "分/次";
                    break;
                case "3":
                    taskFrequency = repeats + "每周星期" + task.getWeekDay() + task.getDayOfHour() +"点" + task.getDayOfMinutes() + "分/次";
                    break;
                case "4":
                    taskFrequency = repeats + "每月" + task.getDayOfMonth() + "号" + task.getDayOfHour() +"点" + task.getDayOfMinutes() + "分/次";
                    break;
                default: ;
                    break;
            }
        }else if (task.getMode() == 2){
            taskFrequency = "实时";
        }else {
            taskFrequency = "未知";
        }
        TaskLogsInfoVm taskLogsInfoVm = taskLogsDao.findByTaskLogsInfo(taskId);
        taskLogsInfoVm.setName(task.getName());
        taskLogsInfoVm.setTaskFrequency(taskFrequency);
        return taskLogsInfoVm;
    }

    /**
     * 查询所有任务日志详情
     *
     * @param mode 0实时，1定时
     */
    public TaskLogsAnalysisVm findAnalysisInfo(int mode) throws Exception {
        TaskLogsAnalysisVm taskLogsAnalysisVm = new TaskLogsAnalysisVm();
        long taskNum = 0;
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        switch (mode) {
            case 0:
                taskNum = taskDao.count();
                taskLogsAnalysisVm = taskLogsDao.findByTaskLogsAnalysis();
                break;
            case 1:
                taskNum = taskDao.countByMode(1);
                taskLogsAnalysisVm = taskLogsDao.findByTaskLogsAnalysis(mode);
                break;
            case 2:
                taskNum = taskDao.countByMode(2);
                taskLogsAnalysisVm = taskLogsDao.findByTaskLogsAnalysis(mode);
                break;
            default:
                break;
        }
        taskLogsAnalysisVm.setTaskNum(taskNum);
        if(taskLogsAnalysisVm.getSuccessNum()==0){
            taskLogsAnalysisVm.setRateOfSuccess("暂无数据");
        }else {
            String rateOfSuccess = numberFormat.format(taskLogsAnalysisVm.getLogNum() / taskLogsAnalysisVm.getSuccessNum() * 100);
            taskLogsAnalysisVm.setRateOfSuccess(rateOfSuccess);
        }
        return taskLogsAnalysisVm;
    }

    /**
     * 新增
     *
     * @param model 传输模型
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(TaskLogsInsertVm model, HttpServletRequest request) throws DoValidException {
        Task task = taskDao.findById(model.getTaskId()).orElseThrow(() -> new DoValidException("任务不存在"));
        TaskLogs taskLogs = new TaskLogs();
        BeanUtils.copyProperties(model, taskLogs);
        taskLogs.setMode(task.getMode());
        taskLogs.setId(BaseUtils.generate32Id());
        taskLogsDao.save(taskLogs);
    }
}
