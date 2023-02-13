package d1.project.d1project.business.dao;

import d1.project.d1project.business.entity.VerifyRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface VerifyRuleDao extends JpaRepository<VerifyRule, String>, JpaSpecificationExecutor<VerifyRule> {

    @Query(value = "SELECT count(id) FROM d1_verify_rule WHERE create_time >= ?1 AND create_time < ?2 ", nativeQuery = true)
    long getRuleCount(LocalDateTime startTime, LocalDateTime endTime);

    @Query(value = "SELECT rule_template_name, count(id)  FROM d1_verify_rule WHERE create_time >= ?1 AND create_time < ?2 GROUP BY rule_template_name", nativeQuery = true)
    List<Map<Integer, Long>> getCountGroupByRuleTemplateName(LocalDateTime startTime, LocalDateTime endTime);

    List<VerifyRule> findAllByIdIn(List<String> ids);

    Optional<VerifyRule> findFirstByName(String name);
}
