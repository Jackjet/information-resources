package d1.framework.permission.dao;

import d1.framework.permission.entity.DeptPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.util.List;

public interface DeptPermissionDao extends JpaRepository<DeptPermission, String> {

    @Transactional
    @Modifying
    Integer deleteAllByDeptId(String deptId);

    List<DeptPermission> findAllByDeptId(String deptId);
}
