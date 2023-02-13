package d1.project.api.integration.system.dao;

import d1.project.api.integration.system.entity.OrganizationUserEntity;
import d1.project.api.integration.system.model.vm.WebAdminUserBaseVm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

/**
 * 组织用户
 * d1project
 *
 * @author kikki
 * @date 2020-09-09 11:24
 */
public interface OrganizationUserDao extends JpaRepository<OrganizationUserEntity, String>, JpaSpecificationExecutor<OrganizationUserEntity> {

    boolean existsByOrganizationId(String organizationId);

    boolean existsByOrganizationIdIn(Collection<String> organizationId);

    List<OrganizationUserEntity> findByOrganizationId(String organizationId);

    void deleteByUserId(String userId);

    List<OrganizationUserEntity> findByUserId(String userId);

    void deleteByUserIdIn(Collection<String> userId);

    @Query(value = "select new d1.project.api.integration.system.model.vm.WebAdminUserBaseVm(waue.id, waue.name, waue.phone, waue.account, waue.organizationName, waue.roleName) " +
            " from  WebAdminUserEntity as waue left join OrganizationUserEntity as oue on oue.userId = waue.id " +
            " where oue.id is not null and oue.organizationId = ?1 and waue.name like %?2 and waue.roleName like %?3")
    Page<WebAdminUserBaseVm> findAllByOrganizationIdIsNotNullAndUserNameAndRoleName(String organizationId, String name, String roleName, Pageable pageable);

    @Query(value = "select new d1.project.api.integration.system.model.vm.WebAdminUserBaseVm(waue.id, waue.name, waue.phone, waue.account, waue.organizationName, waue.roleName) " +
            " from  WebAdminUserEntity as waue left join OrganizationUserEntity as oue on oue.userId = waue.id " +
            " where oue.id is null OR oue.organizationId != ?1 and waue.name like %?2 and waue.roleName like %?3")
    Page<WebAdminUserBaseVm> findAllByOrganizationIdIsNullAndUserNameAndRoleName(String organizationId, String name, String roleName, Pageable pageable);
//    class SpecBuilder {
//        public static Specification<OrganizationUserEntity> buildBackendList(String name, String age) {
//            return (Specification<OrganizationUserEntity>) (root, query, cb) -> {
//                List<WebAdminUserEntity> list = new ArrayList<>();
//                if (!StringUtils.isEmpty(name)) {
//                    list.add(cb.equal(root.get("name"), name));
//                }
//                if (!StringUtils.isEmpty(age)) {
//                    //如果是模糊查询就是
//                    //list.add(
//                    // cb.like(root.get("***").as(String.class), "%" + *** + "%")
//                    // );
//                    list.add(cb.equal(root.get("age"), age));
//                }
//                WebAdminUserEntity[] p = new WebAdminUserEntity[list.size()];
//                return cb.and(list.toArray(p));
//            };
//        }
//    }
}
