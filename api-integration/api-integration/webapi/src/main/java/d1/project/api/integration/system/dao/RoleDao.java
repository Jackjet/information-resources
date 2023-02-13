package d1.project.api.integration.system.dao;

import d1.project.api.integration.system.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-08 21:33
 */
public interface RoleDao extends JpaRepository<RoleEntity, String>, JpaSpecificationExecutor<RoleEntity> {

    /**
     * 按名称验证
     *
     * @param name 名称
     */
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, String id);

    Optional<RoleEntity> findByName(String name);
}
