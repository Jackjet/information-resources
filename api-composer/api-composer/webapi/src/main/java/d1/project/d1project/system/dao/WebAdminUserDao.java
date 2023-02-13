package d1.project.d1project.system.dao;

import d1.project.d1project.system.entity.WebAdminUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-07 15:21
 */
public interface WebAdminUserDao extends JpaRepository<WebAdminUserEntity, String>, JpaSpecificationExecutor<WebAdminUserEntity> {

    /**
     * 查找账户
     *
     * @param account 账户
     */
    Optional<WebAdminUserEntity> findByAccount(String account);

    boolean existsByAccount(String account);

    /**
     * 根据id和密码查找
     *
     * @param id       账号
     * @param password 密码
     */
    Optional<WebAdminUserEntity> findByIdAndPassword(String id, String password);

    List<WebAdminUserEntity> findByOrganizationNameLike(String organizationName);

    List<WebAdminUserEntity> findByRoleNameLike(String organizationName);

    WebAdminUserEntity findFirstBySsoId(String ssoId);
}
