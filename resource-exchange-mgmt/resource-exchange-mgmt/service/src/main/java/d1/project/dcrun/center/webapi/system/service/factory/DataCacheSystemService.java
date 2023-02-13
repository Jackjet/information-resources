package d1.project.dcrun.center.webapi.system.service.factory;

import com.alibaba.fastjson.JSONObject;
import d1.project.dcrun.center.webapi.common.service.system.SysService;
import d1.project.dcrun.center.webapi.common.util.Constants;
import d1.project.dcrun.center.webapi.common.util.MyUtils;
import d1.project.dcrun.center.webapi.datacache.utils.DataCacheUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * 基于ehcache的数据缓存服务，实现基类的抽象方法
 *
 * @author Buter
 * @date 2020/5/10 19:44
 */
@Service
public class DataCacheSystemService extends AbstractSystemService {
    @Override
    public void createPartOfConfigFiles(String id, String appkey, String configPath) throws Exception {
        // application.properties文件路径
        File applicationFile = new File(configPath, "application.properties");

        JSONObject params = new JSONObject();
        params.put("{##appid##}", id);
        params.put("{##appkey##}", appkey);
        params.put("{##server.port##}", "9002");

        // 拷贝文件，并执行替换操作
        ClassPathResource classPathResource = new ClassPathResource(Constants.TEMPLATE_DATACACHE_APP);
        FileUtils.copyInputStreamToFile(classPathResource.getInputStream(), applicationFile);
        MyUtils.replaceForTemplate(applicationFile, params);
    }

    @Override
    public void specialOperations(SysService sysService) throws Exception {
        //系统服务下创建data_cache_config.json文件
        File dataCacheConfigFile = new File(DataCacheUtils.getConfigJsonPath(sysService.getIntegrationId(), sysService.getId()));

        // 拷贝文件
        ClassPathResource classPathResource = new ClassPathResource(Constants.TEMPLATE_DATACACHE_CONFIG);
        FileUtils.copyInputStreamToFile(classPathResource.getInputStream(), dataCacheConfigFile);
    }

    @Override
    public void updateSpecialOperations(JSONObject json) throws Exception {

    }
}
