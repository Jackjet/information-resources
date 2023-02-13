package d1.project.d1project.business.cron;

import d1.project.d1project.business.entity.Task;
import d1.project.d1project.business.entity.VerifyRule;
import d1.project.d1project.business.model.task.TaskRuleService;
import d1.project.d1project.business.service.task.TaskService;
import d1.project.d1project.business.utils.Constants;
import d1.project.d1project.common.service.IVerifyRuleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduleConfig implements CommandLineRunner {
    private final CronTaskRegistrar taskRegistrar;
    private final TaskService taskService;
    private final IVerifyRuleService iVerifyRuleService;
    private final TaskRuleService taskRuleService;

    public ScheduleConfig(CronTaskRegistrar taskRegistrar, TaskService taskService, IVerifyRuleService iVerifyRuleService, TaskRuleService taskRuleService) {
        this.taskRegistrar = taskRegistrar;
        this.taskService = taskService;
        this.iVerifyRuleService = iVerifyRuleService;
        this.taskRuleService = taskRuleService;
    }

    @Override
    public void run(String... args) {
        List<Task> tasks = taskService.findAll();
        for (Task task : tasks) {
            //启用状态，查询质量检查规则
            if (Constants.TASK_STATUS_ENABLE.equals(task.getStatus())) {
                List<String> ruleIds = taskRuleService.getRuleIds(task.getId());
                if (ruleIds != null) {
                    List<VerifyRule> verifyRules = iVerifyRuleService.findAllByIdIn(ruleIds);
                    SchedulingRunnable schedulingRunnable = new SchedulingRunnable(taskService, task, verifyRules);
                    taskRegistrar.addCronTask(task.getId(), schedulingRunnable, task.getCronStr());
                }
            }
        }
    }

}
