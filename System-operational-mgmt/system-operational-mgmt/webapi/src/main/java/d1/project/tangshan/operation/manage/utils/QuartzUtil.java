package d1.project.tangshan.operation.manage.utils;

import d1.project.tangshan.operation.manage.entity.BigDataApiEntity;
import d1.project.tangshan.operation.manage.entity.BigDataApiPlanEntity;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;

/**
 * 定时任务管理类
 * https://blog.csdn.net/Macbethh/article/details/80596925
 *
 * @Author chengh
 */
public class QuartzUtil {
    private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();

    /**
     * 添加一个定时任务
     *
     * @throws Exception
     */
    public static void addJob(BigDataApiPlanEntity apiPlan, BigDataApiEntity api) throws Exception {
        Scheduler scheduler = gSchedulerFactory.getScheduler();
        JobDetail jobDetail = JobBuilder.newJob(InterfaceJob.class).withDescription(apiPlan.getRemark()).withIdentity(apiPlan.getId()).build();
        jobDetail.getJobDataMap().put("apiPlan",apiPlan);
        jobDetail.getJobDataMap().put("api",api);
        // 触发器名,触发器组
        CronTriggerImpl trigger = new CronTriggerImpl();
        trigger.setName(apiPlan.getId());
        trigger.setCronExpression(apiPlan.getCron());

        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();
    }

    /**
     * 修改一个任务的触发时间
     *
     */
    public static void modifyJobTime(BigDataApiPlanEntity apiPlan,BigDataApiEntity api) throws Exception {
        Scheduler scheduler = gSchedulerFactory.getScheduler();
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(TriggerKey.triggerKey(api.getId()));
        if (trigger == null) {
            return;
        }
        String oldTime = trigger.getCronExpression();
        if (!oldTime.equalsIgnoreCase(apiPlan.getCron())) {
            removeJob(apiPlan.getId());
            addJob(apiPlan,api);
        }

    }

    /**
     * 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
     *
     * @param jobName
     */
    public static void removeJob(String jobName) {
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            TriggerKey trigger = TriggerKey.triggerKey(jobName);
            // 停止触发器
            sched.pauseTrigger(trigger);
            // 移除触发器
            sched.unscheduleJob(trigger);
            // 删除任务
            sched.deleteJob(JobKey.jobKey(jobName));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
