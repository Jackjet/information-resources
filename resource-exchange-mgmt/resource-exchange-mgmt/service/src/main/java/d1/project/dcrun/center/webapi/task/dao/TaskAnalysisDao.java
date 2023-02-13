package d1.project.dcrun.center.webapi.task.dao;

import d1.project.dcrun.center.webapi.task.entity.TaskAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author zhengyang
 */
public interface TaskAnalysisDao extends JpaRepository<TaskAnalysis, String>, JpaSpecificationExecutor<TaskAnalysis> {
}
