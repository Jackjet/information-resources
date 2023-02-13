package d1.project.d1project.business.dao;

import d1.project.d1project.business.entity.TaskRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface TaskRuleDao extends JpaRepository<TaskRule, String>, JpaSpecificationExecutor<TaskRule> {
    int countAllByTaskId(String taskId);

    TaskRule findFirstByTaskId(String taskId);

    void deleteAllByRuleIdIn(List<String> ruleIds);

    List<TaskRule> findAllByTaskId(String taskId);

    Optional<TaskRule> findFirstByRuleId(String ruleId);
    
    void deleteAllByTaskId(String taskId);
}
