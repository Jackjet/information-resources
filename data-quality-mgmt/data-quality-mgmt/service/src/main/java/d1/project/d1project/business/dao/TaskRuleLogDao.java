package d1.project.d1project.business.dao;

import d1.project.d1project.business.entity.TaskRuleLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface TaskRuleLogDao extends JpaRepository<TaskRuleLog, String>, JpaSpecificationExecutor<TaskRuleLog> {

    /**
     * 首页指标统计
     *
     * @param day 当天时间
     * @return 统计数据
     */
    @Query(value = "SELECT log.template_id, t.name, SUM(data_size) AS total_data_size, SUM(error_data_size) AS total_error_data_size FROM d1_task_rule_log log, d1_rule_template t WHERE log.template_id = t.id AND log.create_day = ?1 GROUP BY log.template_id, t.name", nativeQuery = true)
    List<Map<Integer, Long>> statisticsCountGroupByRuleTemplateId(String day);

}
