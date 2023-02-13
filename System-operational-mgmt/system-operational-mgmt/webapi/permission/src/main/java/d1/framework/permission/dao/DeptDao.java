package d1.framework.permission.dao;

import d1.framework.permission.entity.Dept;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeptDao extends JpaRepository<Dept, String> {

    Dept findFirstByParentIdOrderByCreateTimeDesc(String parentId);
}
