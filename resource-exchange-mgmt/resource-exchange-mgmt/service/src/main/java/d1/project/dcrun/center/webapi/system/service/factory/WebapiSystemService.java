package d1.project.dcrun.center.webapi.system.service.factory;

import com.alibaba.fastjson.JSONObject;
import d1.project.dcrun.center.webapi.common.service.management.config.ManagementConfig;
import d1.project.dcrun.center.webapi.common.service.management.config.ManagementConfigService;
import d1.project.dcrun.center.webapi.common.service.system.SysService;
import d1.project.dcrun.center.webapi.common.util.Constants;
import d1.project.dcrun.center.webapi.common.util.MyUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * 基于webapi的脚本运行服务，实现基类的抽象方法
 *
 * @author Buter
 * @date 2020/5/10 19:44
 */
@Service
public class WebapiSystemService extends AbstractSystemService {
    @Autowired
    private ManagementConfigService managementConfigService;

    @Override
    public void createPartOfConfigFiles(String id, String appkey, String configPath) throws Exception {
        // application.properties文件路径
        String applicationPath = configPath + "application.properties";
        File applicationFile = new File(applicationPath);

        JSONObject params = new JSONObject();
        params.put("{##appid##}", id);
        params.put("{##appkey##}", appkey);
        params.put("{##server.port##}", "9001");

        // 拷贝文件，并执行替换操作
        ClassPathResource classPathResource = new ClassPathResource(Constants.TEMPLATE_WEBAPI_APP);
        FileUtils.copyInputStreamToFile(classPathResource.getInputStream(), applicationFile);
        MyUtils.replaceForTemplate(applicationFile, params);

        // 创建空白的config.json文件
        File configFile = new File(configPath, "config.json");
        FileUtils.write(configFile, "{}", StandardCharsets.UTF_8);
    }

    @Override
    public void specialOperations(SysService sysService) throws Exception {
        createScript(Constants.getServiceRootPath(sysService.getIntegrationId(), sysService.getTemplateType(), sysService.getId()));
        createLogbackXml(sysService);
    }

    /**
     * @param rootPath 根目录
     * @throws Exception 抛出异常
     */
    private void createScript(String rootPath) throws Exception {
        // 新增script文件夹
        String scriptRootPath = rootPath + "script" + File.separator;
        String scriptJs = scriptRootPath + "js";
        String scriptKettle = scriptRootPath + "kettle";
        String scriptWeb = scriptRootPath + "web";
        String scriptJsApp = scriptRootPath + "js" + File.separator + "app.js";

        // 在script下创建js和kettle及web目录，并生成空白的app.js文件
        FileUtils.forceMkdir(new File(scriptJs));
        FileUtils.forceMkdir(new File(scriptKettle));
        FileUtils.forceMkdir(new File(scriptWeb));
        File app = new File(scriptJsApp);
        app.createNewFile();

        // 创建timestamp文件，用于存储脚本时间戳
        File scriptFile = new File(scriptRootPath, "timestamp");
        FileUtils.write(scriptFile, "--", StandardCharsets.UTF_8);
    }

    /**
     * @param sysService 系统服务
     * @throws Exception 抛出异常
     */
    private void createLogbackXml(SysService sysService) throws Exception {
        ManagementConfig managementConfig = managementConfigService.findByConfigKey("manage.center.mq.url");
        String configRootPath = Constants.getSysServiceConfigPath(sysService.getIntegrationId(), sysService.getTemplateType(), sysService.getId());

        // 拷贝文件，并执行替换操作
        ClassPathResource classPathResource = new ClassPathResource(Constants.TEMPLATE_WEBAPI_LOGBACK);
        File logbackFile = new File(configRootPath, "logback.xml");
        FileUtils.copyInputStreamToFile(classPathResource.getInputStream(), logbackFile);

        String topic = sysService.getIntegrationId() + "/" + sysService.getTemplateType() + "/" + sysService.getId() + "/log";
        //替换xml文件
        JSONObject xmlParam = new JSONObject();
        xmlParam.put("{##broker##}", managementConfig.getConfigValue());
        xmlParam.put("{##username##}", sysService.getId());
        xmlParam.put("{##password##}", sysService.getAppkey());
        xmlParam.put("{##topic##}", topic);
        xmlParam.put("{##debug##}", "true");
        MyUtils.replaceForTemplate(logbackFile, xmlParam);
    }
    @Override
    public void updateSpecialOperations(JSONObject json) throws Exception {

    }
}
