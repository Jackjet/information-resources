package d1.project.dcrun.center.webapi.dcapi.dao;

import d1.project.dcrun.center.webapi.dcapi.entity.FlowControlPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author lin
 */
public interface FlowControlPlanDao extends JpaRepository<FlowControlPlan, String>, JpaSpecificationExecutor<FlowControlPlan> {
    /**
     * 判断流量控制策略名称是否重复
     *
     * @param integrationId 系统项目id
     * @param sysServiceId  系统服务id
     * @param name          流量控制策略名称
     * @return 返回结果
     */
    Boolean existsByIntegrationIdAndSysServiceIdAndName(String integrationId, String sysServiceId, String name);

    /**
     * 判断流量控制策略名称是否重复
     *
     * @param integrationId 系统项目id
     * @param sysServiceId  系统服务id
     * @param name          流量控制策略名称
     * @param id            主键
     * @return 返回结果
     */
    Boolean existsByIntegrationIdAndSysServiceIdAndNameAndIdNot(String integrationId, String sysServiceId, String name, String id);
}
