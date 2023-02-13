package d1.project.d1project.business.cron;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 添加执行定时任务的线程池配置类
 * @author libin
 */
@Configuration
public class SchedulerThreadPoolConfig {
    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        // 定时任务执行线程池核心线程数
        taskScheduler.setPoolSize(10);
        //设置任务注册器的调度器
        taskScheduler.setRemoveOnCancelPolicy(true);
        //设置线程名称前缀
        taskScheduler.setThreadNamePrefix("SchedulerThreadPool-");
        return taskScheduler;
    }
}