package d1.project.d1project.apidesign.dao;

import d1.project.d1project.apidesign.entity.ApiDeployment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author baozh
 */
public interface ApiDeploymentDao extends JpaRepository<ApiDeployment, String>, JpaSpecificationExecutor<ApiDeployment> {
    /**
     * 根据API ID 查询是否还有部署的容器
     *
     * @param apiId apiId
     * @return 查询结果
     */
    boolean existsAllByApiDesignId(String apiId);

    /**
     * 根据API ID和容器ID 查询是否还有部署的容器
     *
     * @param apiId       apiId
     * @param containerId 容器ID
     * @return 查询结果
     */
    boolean existsAllByApiDesignIdAndContainerId(String apiId, String containerId);

    /**
     * 根据API ID和容器ID 查询是否还有部署的容器
     *
     * @param apiId       apiId
     * @param containerId 容器ID
     * @return 查询结果
     */
    ApiDeployment findFirstByApiDesignIdAndContainerId(String apiId, String containerId);

    /**
     * 根据容器ID查询所有部署信息
     *
     * @param containerId 容器ID
     * @return 查询结果
     */
    List<ApiDeployment> findAllByContainerId(String containerId);

    /**
     * 根据APIID查询所有部署信息
     *
     * @param apiId 容器ID
     * @return 查询结果
     */
    List<ApiDeployment> findAllByApiDesignId(String apiId);
}
