package d1.project.dcrun.center.webapi.system.service.factory;

import com.alibaba.fastjson.JSONObject;
import d1.project.dcrun.center.webapi.common.service.system.SysService;
import d1.project.dcrun.center.webapi.common.util.Constants;
import d1.project.dcrun.center.webapi.emq.utils.EmqUtils;
import d1.project.dcrun.center.webapi.system.entity.SysInstallPack;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * 基于emq的消息集成服务，实现基类的抽象方法
 *
 * @author Buter
 * @date 2020/5/10 19:44
 */
@Service
public class EmqSystemService extends AbstractSystemService {
    @Override
    public void createBaseFiles(SysService sysService, String rootPath, SysInstallPack sysInstallPack) throws Exception {
        File versionFile = new File(EmqUtils.getServiceRootPath(sysService.getIntegrationId(), sysService.getId()), "version");
        createVersionFile(versionFile, "3.1.2");
    }

    @Override
    public void createVersionFile(File versionFile, String version) throws Exception {
        FileUtils.write(versionFile, version, StandardCharsets.UTF_8);
    }

    @Override
    public void createConfigFiles(String integrationId, String type, String id, String appkey) {
        // do nothing
    }

    @Override
    public void createPartOfConfigFiles(String id, String appkey, String configPath) {
        // do nothing
    }

    @Override
    public void specialOperations(SysService sysService) throws Exception {
        //系统服务下创建emq_config.json文件
        String rootPath = Constants.getServiceRootPath(sysService.getIntegrationId(), sysService.getTemplateType(), sysService.getId());
        String emqConfigPath = rootPath + "emq_config.json";
        File emqConfigFile = new File(emqConfigPath);

        // 拷贝文件
        ClassPathResource classPathResource = new ClassPathResource(Constants.TEMPLATE_EMQ_CONFIG);
        FileUtils.copyInputStreamToFile(classPathResource.getInputStream(), emqConfigFile);
    }

    @Override
    public void updateSpecialOperations(JSONObject json) throws Exception {

    }
}
