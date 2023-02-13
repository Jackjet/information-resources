package d1.project.dcrun.center.webapi.dcapi.dao;

import d1.project.dcrun.center.webapi.dcapi.entity.AccessControlPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author lin
 */
public interface AccessControlPlanDao extends JpaRepository<AccessControlPlan, String>, JpaSpecificationExecutor<AccessControlPlan> {
    /**
     * 判断访问控制策略名称是否重复
     *
     * @param name         访问控制策略名称
     * @param sysServiceId 系统服务id
     * @return
     */
    Boolean existsByNameAndSysServiceId(String name, String sysServiceId);

    /**
     * 判断访问控制策略名称是否重复
     *
     * @param name         访问控制策略名称
     * @param sysServiceId 系统服务id
     * @param id           主键
     * @return
     */
    Boolean existsByNameAndSysServiceIdAndIdNot(String name, String sysServiceId, String id);
}
