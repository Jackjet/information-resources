package d1.project.dcrun.center.webapi.system.service;

import d1.framework.webapi.utils.TokenService;
import d1.project.dcrun.center.webapi.common.service.management.config.ManagementConfig;
import d1.project.dcrun.center.webapi.common.service.management.config.ManagementConfigDao;
import d1.project.dcrun.center.webapi.common.util.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author libin
 */
@Service
public class ManageConfigService {
    @Autowired
    private ManagementConfigDao managementConfigDao;
    @Autowired
    private TokenService tokenService;

    /**
     * 查询管理中心服务配置
     *
     * @return 管理中心服务配置
     */
    public List<ManagementConfig> findAll() {
        return this.managementConfigDao.findAllByOrderByIdAsc();
    }

    /**
     * 修改管理中心服务配置
     *
     * @param request
     * @param model   管理中心服务配置信息
     */
    public void update(HttpServletRequest request, Map<String, String> model) {
        List<ManagementConfig> managementConfigs = this.managementConfigDao.findAll();

        for (ManagementConfig managementConfig : managementConfigs) {
            String value = MyUtils.eliminateSpaces(model.get(managementConfig.getConfigKey()));
            if (!StringUtils.isEmpty(value)) {
                managementConfig.setConfigValue(value);
                tokenService.updateUpdateIdAndTime(request, managementConfig);
            }
        }

        this.managementConfigDao.saveAll(managementConfigs);
    }

    public ManagementConfig findById(String id) {
        return managementConfigDao.findById(id).orElse(null);
    }

    public void delete(String id) {
        managementConfigDao.deleteById(id);
    }
}
