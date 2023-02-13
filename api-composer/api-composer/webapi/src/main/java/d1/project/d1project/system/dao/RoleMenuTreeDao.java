package d1.project.d1project.system.dao;

import d1.project.d1project.system.entity.RoleMenuTreeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-10 19:04
 */
public interface RoleMenuTreeDao extends JpaRepository<RoleMenuTreeEntity, String>, JpaSpecificationExecutor<RoleMenuTreeEntity> {

    boolean existsByRoleId(String roleId);

    List<RoleMenuTreeEntity> findByRoleId(String roleId);

    List<RoleMenuTreeEntity> findByRoleIdIn(Collection<String> roleId);

    void deleteAllByRoleId(String roleId);

    void deleteByMenuTreeId(String menuTreeId);

    boolean existsByMenuTreeId(String menuTreeId);

    void deleteByRoleId(String roleId);
}
