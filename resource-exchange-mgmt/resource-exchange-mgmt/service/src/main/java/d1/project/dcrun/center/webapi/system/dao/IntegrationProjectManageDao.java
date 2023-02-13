package d1.project.dcrun.center.webapi.system.dao;

import d1.project.dcrun.center.webapi.system.entity.IntegrationProjectManage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author libin
 */
public interface IntegrationProjectManageDao extends JpaRepository<IntegrationProjectManage, String>, JpaSpecificationExecutor<IntegrationProjectManage> {
    /**
     * 根据管理员账号查询
     *
     * @param code 管理员账号
     * @return 集成项目管理信息
     */
    IntegrationProjectManage findFirstByCode(String code);
}
