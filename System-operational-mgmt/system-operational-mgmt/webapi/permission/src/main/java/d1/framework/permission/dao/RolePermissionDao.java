package d1.framework.permission.dao;

import d1.framework.permission.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface RolePermissionDao extends JpaRepository<RolePermission, String> {

    @Query(value = "select a.* from d1_role_permission a left join d1_user_role b on a.role_id = b.role_id where b.user_id=?1", nativeQuery = true)
    List<RolePermission> findRolePermissionByUserId(String userId);

    @Transactional
    @Modifying
    Integer deleteAllByRoleId(String roleId);

    List<RolePermission> findAllByRoleId(String roleId);
}
