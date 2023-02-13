package d1.framework.permission.dao;

import d1.framework.permission.entity.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.util.List;

public interface UserPermissionDao extends JpaRepository<UserPermission, String> {
    @Transactional
    @Modifying
    Integer deleteAllByUserId(String userId);

    List<UserPermission> findAllByUserId(String userId);
}
