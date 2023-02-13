package d1.project.dcrun.center.webapi.common.service.management.config;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author libin
 */
public interface ManagementConfigDao extends JpaRepository<ManagementConfig, String>, JpaSpecificationExecutor<ManagementConfig> {
    /**
     * 根据配置key查询
     *
     * @param configKey 配置key
     * @return
     */
    List<ManagementConfig> findAllByConfigKeyIn(List<String> configKey);

    /**
     * 根据key查找数据
     *
     * @param configkey key值
     * @return 返回值
     */
    ManagementConfig findByConfigKey(String configkey);

    /**
     * 查询所有配置，根据id正序
     *
     * @return
     */
    List<ManagementConfig> findAllByOrderByIdAsc();
}
