package d1.project.dataintegration.task.service;

import d1.framework.webapi.configuration.DoValidException;
import d1.project.dataintegration.common.utils.BaseUtils;
import d1.project.dataintegration.task.dao.TaskDao;
import d1.project.dataintegration.task.dao.TaskEntityLogDao;
import d1.project.dataintegration.task.dao.TaskLogsDao;
import d1.project.dataintegration.task.entity.Task;
import d1.project.dataintegration.task.entity.TaskEntityLog;
import d1.project.dataintegration.task.entity.TaskLogs;
import d1.project.dataintegration.task.model.TaskLogsVm;
import d1.project.dataintegration.task.model.vm.TaskEntityLogInsertVm;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author baozh
 */
@Service
public class TaskEntityLogService {
    private final TaskEntityLogDao taskEntityLogDao;
    private final TaskLogsDao taskLogsDao;
    private final TaskDao taskDao;

    public TaskEntityLogService(TaskEntityLogDao taskEntityLogDao, TaskLogsDao taskLogsDao, TaskDao taskDao) {
        this.taskEntityLogDao = taskEntityLogDao;
        this.taskLogsDao = taskLogsDao;
        this.taskDao = taskDao;
    }

    public void insert(TaskEntityLog taskEntityLog) {
        taskEntityLog.setId(BaseUtils.generate32Id());
        taskEntityLogDao.save(taskEntityLog);
    }

    public void end(TaskEntityLogInsertVm taskEntityLogInsertVm) throws DoValidException {
        Task task = taskDao.findById(taskEntityLogInsertVm.getTaskId()).orElseThrow(() -> new DoValidException("任务不存在"));
        TaskLogsVm taskLogsVm = taskEntityLogDao.findByTaskIdAndChannelIdAndGroupId(taskEntityLogInsertVm.getTaskId(),
                taskEntityLogInsertVm.getChannelId(), taskEntityLogInsertVm.getGroupId());
        if (taskLogsVm == null) {
            return;
        }
        long sumTime = taskLogsVm.getEndTime().getTimeInMillis()-taskLogsVm.getStartTime().getTimeInMillis();
        TaskLogs taskLogs = new TaskLogs();
        BeanUtils.copyProperties(taskLogsVm, taskLogs);
        taskLogs.setId(BaseUtils.generate32Id());
        taskLogs.setSumTime(String.valueOf(sumTime/1000));
        taskLogs.setMode(task.getMode());
        taskLogsDao.save(taskLogs);
    }
}
