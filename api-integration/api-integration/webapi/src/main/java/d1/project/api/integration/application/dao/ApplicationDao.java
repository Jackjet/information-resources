package d1.project.api.integration.application.dao;

import d1.project.api.integration.application.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ApplicationDao extends JpaRepository<Application, String>, JpaSpecificationExecutor<Application> {
    Boolean existsByName(String name);

    Boolean existsByNameAndIdNot(String name, String id);

    List<Application> findAllByIdIn(List<String> ids);
}
