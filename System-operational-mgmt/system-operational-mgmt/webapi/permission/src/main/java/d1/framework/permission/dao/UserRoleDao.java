package d1.framework.permission.dao;

import d1.framework.permission.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRoleDao extends JpaRepository<UserRole, String> {

    List<UserRole> findAllByUserId(String userId);

    @Query(value = "select r.name from d1_role r left join d1_user_role ur on r.id = ur.role_id where ur.user_id=?1", nativeQuery = true)
    List<String> findAllRoleNameByUserId(String userId);

    List<UserRole> findAllByRoleId(String roleId);

}
