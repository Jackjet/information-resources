package d1.project.d1project.business.model.task;

import d1.project.d1project.business.dao.TaskRuleDao;
import d1.project.d1project.business.entity.TaskRule;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author libin
 */
@Service
public class TaskRuleService {
    private final TaskRuleDao taskRuleDao;

    public TaskRuleService(TaskRuleDao taskRuleDao) {
        this.taskRuleDao = taskRuleDao;
    }

    public List<String> getRuleIds(String taskId){
        List<TaskRule> taskRules = taskRuleDao.findAllByTaskId(taskId);
        return taskRules.stream().map(TaskRule::getRuleId).collect(Collectors.toList());
    }
}
