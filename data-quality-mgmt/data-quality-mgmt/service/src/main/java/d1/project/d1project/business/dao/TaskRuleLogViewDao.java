package d1.project.d1project.business.dao;

import d1.project.d1project.business.view.TaskRuleLogView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TaskRuleLogViewDao extends JpaRepository<TaskRuleLogView,String>, JpaSpecificationExecutor<TaskRuleLogView> {
}
