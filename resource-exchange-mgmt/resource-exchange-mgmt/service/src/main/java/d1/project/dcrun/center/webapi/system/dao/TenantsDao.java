package d1.project.dcrun.center.webapi.system.dao;

import d1.project.dcrun.center.webapi.system.entity.Tenants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author libin
 */
public interface TenantsDao extends JpaRepository<Tenants, String>, JpaSpecificationExecutor<Tenants> {
    /**
     * 根据租户名称查询
     *
     * @param name 租户名称
     * @return 租户信息
     */
    Tenants findFirstByName(String name);
}
