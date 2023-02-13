package d1.framework.permission.dao;

import d1.framework.permission.entity.MenuTree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

/**
 * @author all
 */
public interface MenuTreeDao extends JpaRepository<MenuTree, String> {

    @Query(value = "SELECT * FROM d1_menu_tree ORDER BY order_num+0 ASC", nativeQuery = true)
    List<MenuTree> findAllOrderById();

    @Query(value = "SELECT * FROM d1_menu_tree WHERE id IN ?1 ORDER BY order_num+0 ASC", nativeQuery = true)
    List<MenuTree> findOrderById(Collection<String> ids);

    boolean existsByOrderNumAndParentId(Integer orderNum, String parentId);

    boolean existsByParentId(String parentId);
}
