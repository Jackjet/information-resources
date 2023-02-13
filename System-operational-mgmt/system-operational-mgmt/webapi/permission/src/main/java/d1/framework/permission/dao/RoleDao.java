package d1.framework.permission.dao;

import d1.framework.permission.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoleDao extends JpaRepository<Role, String>, JpaSpecificationExecutor<Role> {
    Role findByName(String name);

    Boolean existsByName(String name);
}
