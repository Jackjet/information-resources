package d1.project.resourcesharingmgmt.resource.dao;

import d1.project.resourcesharingmgmt.resource.entity.WayRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author dy
 */
public interface WayRoleDao extends JpaRepository<WayRoleEntity, String>, JpaSpecificationExecutor<WayRoleEntity> {

    /**
     * 通过角色Id获取列表
     *
     * @param roleId 角色Id
     * @return
     */
    List<WayRoleEntity> findAllByRoleIdAndType(String roleId,String type);

    /**
     * 通过指引Id获取列表
     *
     * @param wayId
     * @return
     */
    List<WayRoleEntity> findAllByWayId(String wayId);
}
