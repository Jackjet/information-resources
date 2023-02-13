package d1.project.dcrun.center.webapi.common.service.sys.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

/**
 * @author maoyy
 */

public interface SysUserDao extends JpaRepository<SysUser, String>, JpaSpecificationExecutor<SysUser> {

    Optional<SysUser> findByIntegrationIdAndAppid(String integrationId, String appid);

    Optional<SysUser> findAllByIntegrationIdAndName(String integrationId, String name);

    List<SysUser> findAllByIntegrationId(String integrationId);
}
