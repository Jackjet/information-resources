package d1.project.api.integration.external.dao;

import d1.project.api.integration.external.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ApplicationUserDao extends JpaRepository<ApplicationUser,String>, JpaSpecificationExecutor<ApplicationUser> {
    ApplicationUser findFirstByUserId(String userId);
}
