package d1.project.d1project.business.dao;

import d1.project.d1project.business.entity.RuleTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RuleTemplateDao extends JpaRepository<RuleTemplate, String>, JpaSpecificationExecutor<RuleTemplate> {
}
