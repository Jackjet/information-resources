package d1.project.api.integration.system.dao;

import d1.project.api.integration.common.model.IId;
import d1.project.api.integration.system.entity.RoleUserEntity;
import d1.project.api.integration.system.model.vm.WebAdminUserBaseVm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-08 21:33
 */
public interface RoleUserDao extends JpaRepository<RoleUserEntity, String>, JpaSpecificationExecutor<RoleUserEntity> {

    /**
     * 根据角色id查找
     *
     * @param roleId 角色id
     * @return 列表
     */
    List<RoleUserEntity> findByRoleId(String roleId);

    Page<RoleUserEntity> findByRoleId(String roleId, Pageable pageable);

    Stream<IId> findAllByRoleId(String roleId);

    boolean existsByRoleId(String roleId);

    /**
     * 根据用户id删除
     *
     * @param userId 用户id
     */
    void deleteByUserId(String userId);

    List<RoleUserEntity> findByUserId(String userId);

    void deleteByUserIdIn(Collection<String> userId);

    void deleteByRoleId(String roleId);


    @Query(value = "select new d1.project.api.integration.system.model.vm.WebAdminUserBaseVm(waue.id, waue.name, waue.phone, waue.account, waue.organizationName, waue.roleName) " +
            " from  WebAdminUserEntity as waue left join RoleUserEntity as rue on rue.userId = waue.id " +
            " where rue.id is not null and rue.roleId = ?1 and waue.name like %?2 and waue.organizationName like %?3")
    Page<WebAdminUserBaseVm> findAllByRoleIdIsNotNullAndUserNameAndRoleName(String roleId, String name, String organizationName, Pageable pageable);

    @Query(value = "select new d1.project.api.integration.system.model.vm.WebAdminUserBaseVm(waue.id, waue.name, waue.phone, waue.account, waue.organizationName, waue.roleName) " +
            " from  WebAdminUserEntity as waue left join RoleUserEntity as rue on rue.userId = waue.id " +
            " where rue.id is null OR rue.roleId != ?1 and waue.name like %?2 and waue.organizationName like %?3")
    Page<WebAdminUserBaseVm> findAllByRoleIdIsNullAndUserNameAndRoleName(String roleId, String name, String organizationName, Pageable pageable);
}
