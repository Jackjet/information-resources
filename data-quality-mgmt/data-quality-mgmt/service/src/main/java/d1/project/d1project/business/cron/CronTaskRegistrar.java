package d1.project.d1project.business.cron;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 添加定时任务注册类，用来增加、删除定时任务。
 *
 * @author libin
 */
@Component
public class CronTaskRegistrar implements DisposableBean {

    private final Map<String, ScheduledTask> scheduledTasks = new ConcurrentHashMap<>();

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    public ThreadPoolTaskScheduler getScheduler() {
        return this.taskScheduler;
    }

    public void addCronTask(String cronId, Runnable task, String cronExpression) {
        //加入新任务后，如果线程池容量不够，则扩容
        if (taskScheduler.getPoolSize() < taskScheduler.getActiveCount() + 1) {
            taskScheduler.setPoolSize(taskScheduler.getActiveCount() + 1);
        }
        addCronTask(cronId, new CronTask(task, cronExpression));
    }

    public void addCronTask(String cronId, CronTask cronTask) {
        if (cronTask != null) {
            if (this.scheduledTasks.containsKey(cronId)) {
                removeCronTask(cronId);
            }
            this.scheduledTasks.put(cronId, scheduleCronTask(cronTask));
        }
    }

    public void removeCronTask(String cronId) {
        ScheduledTask scheduledTask = this.scheduledTasks.remove(cronId);
        if (scheduledTask != null) {
            scheduledTask.cancel();
        }

        //如果线程池容量减活跃线程数大于10，则线程池容量减10
        if (taskScheduler.getPoolSize() - taskScheduler.getActiveCount() > 10) {
            taskScheduler.setPoolSize(taskScheduler.getPoolSize() - 10);
        }
    }

    public ScheduledTask scheduleCronTask(CronTask cronTask) {
        ScheduledTask scheduledTask = new ScheduledTask();
        scheduledTask.future = this.taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());
        return scheduledTask;
    }


    @Override
    public void destroy() {
        for (ScheduledTask task : this.scheduledTasks.values()) {
            task.cancel();
        }
        this.scheduledTasks.clear();
    }
}