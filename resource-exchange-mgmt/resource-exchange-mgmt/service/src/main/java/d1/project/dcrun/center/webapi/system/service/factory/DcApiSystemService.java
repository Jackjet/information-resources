package d1.project.dcrun.center.webapi.system.service.factory;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.project.dcrun.center.webapi.common.service.system.SysService;
import d1.project.dcrun.center.webapi.common.service.system.SysServiceService;
import d1.project.dcrun.center.webapi.common.util.Constants;
import d1.project.dcrun.center.webapi.common.util.MyPropertiesUtil;
import d1.project.dcrun.center.webapi.common.util.MyUtils;
import d1.project.dcrun.center.webapi.dcapi.utils.DCApiUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Properties;

/**
 * 基于dc api网关的API集成服务，实现基类的抽象方法
 *
 * @author Buter
 * @date 2020/5/10 19:44
 */
@Service
public class DcApiSystemService extends AbstractSystemService {
    @Override
    public void createPartOfConfigFiles(String id, String appkey, String configPath) throws Exception {
        // application.properties文件路径
        String applicationPath = configPath + "application.properties";
        File applicationFile = new File(applicationPath);

        JSONObject params = new JSONObject();
        params.put("{##appid##}", id);
        params.put("{##appkey##}", appkey);
        params.put("{##server.port##}", "18081");

        // 拷贝文件，并执行替换操作
        ClassPathResource classPathResource = new ClassPathResource(Constants.TEMPLATE_DCAPI_POSTGRESQL_APP);
        FileUtils.copyInputStreamToFile(classPathResource.getInputStream(), applicationFile);
        MyUtils.replaceForTemplate(applicationFile, params);
    }

    @Override
    public void specialOperations(SysService sysService) {
        // do nothing
    }

    @Override
    public void updateVerify (SysServiceService sysServiceService, SysService old, JSONObject params) throws Exception {
        super.updateVerify(sysServiceService, old, params);
        String serverPort = params.getString("serverPort");
        if (serverPort == null || "".equals(serverPort)) {
            throw new ValidException("服务端口号不能为空");
        }
    }
    @Override
    public void updateSpecialOperations(JSONObject json) throws Exception {
        String integrationId = json.getString("integrationId");
        String id = json.getString("id");
        String port = json.getString("port");
        String propertiesPath = DCApiUtils.getApplicationPropertiesPath(integrationId, id);
        Properties p = MyPropertiesUtil.init(propertiesPath);
        MyPropertiesUtil.update("server.port", port, propertiesPath, p);
    }
}
