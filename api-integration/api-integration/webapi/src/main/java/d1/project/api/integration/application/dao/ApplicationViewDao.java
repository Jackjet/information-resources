package d1.project.api.integration.application.dao;

import d1.project.api.integration.application.model.ApplicationView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ApplicationViewDao extends JpaRepository<ApplicationView, String>, JpaSpecificationExecutor<ApplicationView> {
    List<ApplicationView> findAllByNameContainsAndContainerContains(String name, String container);
}
