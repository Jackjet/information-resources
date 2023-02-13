package d1.project.d1project.business.cron;

import d1.project.d1project.business.entity.Task;
import d1.project.d1project.business.entity.VerifyRule;
import d1.project.d1project.business.service.task.TaskService;
import d1.project.d1project.business.workers.bean.RunTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.List;

public class SchedulingRunnable implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(SchedulingRunnable.class);

    private final TaskService taskService;

    private final RunTask runTask = new RunTask();
    private final List<VerifyRule> verifyRules;

    public SchedulingRunnable(TaskService taskService, Task task, List<VerifyRule> verifyRules) {
        this.taskService = taskService;
        this.verifyRules = verifyRules;
        BeanUtils.copyProperties(task, runTask);
    }

    @Override
    public void run() {
        try {
            taskService.joinRunTask(verifyRules, runTask);
        } catch (Exception e) {
            logger.error("添加任务出错", e);
        }
    }
}
