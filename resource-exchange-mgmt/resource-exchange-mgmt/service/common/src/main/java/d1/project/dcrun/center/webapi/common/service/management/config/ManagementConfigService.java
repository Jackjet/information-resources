package d1.project.dcrun.center.webapi.common.service.management.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author libin
 */
@Service
public class ManagementConfigService {

    private final ManagementConfigDao managementConfigDao;

    @Autowired
    public ManagementConfigService(ManagementConfigDao managementConfigDao) {
        this.managementConfigDao = managementConfigDao;
    }

    public List<ManagementConfig> findAllByConfigKeyIn(List<String> configKey) {
        return managementConfigDao.findAllByConfigKeyIn(configKey);
    }

    public ManagementConfig findByConfigKey(String configkey) {
        return managementConfigDao.findByConfigKey(configkey);
    }

    public List<ManagementConfig> findAll() {
        return managementConfigDao.findAll();
    }
}

