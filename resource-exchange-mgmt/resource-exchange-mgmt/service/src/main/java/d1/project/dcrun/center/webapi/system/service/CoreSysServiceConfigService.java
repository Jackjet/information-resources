package d1.project.dcrun.center.webapi.system.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.TokenService;
import d1.project.dcrun.center.webapi.common.service.system.SysService;
import d1.project.dcrun.center.webapi.common.service.system.SysServiceService;
import d1.project.dcrun.center.webapi.common.util.Constants;
import d1.project.dcrun.center.webapi.common.util.MyPropertiesUtil;
import d1.project.dcrun.center.webapi.common.util.MyUtils;
import d1.project.dcrun.center.webapi.common.util.ServiceType;
import d1.project.dcrun.center.webapi.system.entity.SysInstallPack;
import net.dcrun.component.compression.ICompressService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

/**
 * @author xuaa
 */
@Service
public class CoreSysServiceConfigService {
    @Autowired
    private SysServiceService sysServiceService;
    @Autowired
    private ICompressService compressService;
    @Value("${file.server.uri}")
    private String fileServerUri;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private SysInstallPackService sysInstallPackService;

    /**
     * @param params
     * @return
     * @throws Exception
     */
    public JSONObject findAllConfig(JSONObject params) throws Exception {
        String appid = params.getString("appid");

        SysService entity = sysServiceService.findById(appid);
        JSONObject result = (JSONObject) JSONObject.toJSON(entity);

        //读取 files/services/appid/config/application.properties文件内容转化为对象传给前端；
        String path = Constants.getApplicationPropertiesPath(entity.getIntegrationId(), entity.getTemplateType(), appid);
        Properties p = new Properties();
        InputStream inputStream = new BufferedInputStream(new FileInputStream(path));
        //从输入流中读取属性列表（键和元素对）
        p.load(inputStream);
        //得到配置文件的名字
        for (Map.Entry<Object, Object> entry : p.entrySet()) {
            result.put((String) entry.getKey(), entry.getValue());
        }
        return result;
    }

    /**
     * 系统服务配置编辑
     *
     * @param params 修改实体
     * @throws Exception 抛出异常
     */
    public void updateSysConfig(JSONObject params, HttpServletRequest request) throws Exception {
        String appid = params.getString("appid");
        String serverPort = MyUtils.eliminateSpaces(params.getString("serverPort"));

        SysService sysService = sysServiceService.findById(appid);
        if (!StringUtils.isEmpty(serverPort)) {
            // 服务端口
            String propertiesPath = Constants.getApplicationPropertiesPath(sysService.getIntegrationId(), sysService.getTemplateType(), sysService.getId());
            Properties p = MyPropertiesUtil.init(propertiesPath);
            MyPropertiesUtil.update("server.port", serverPort, propertiesPath, p);
        }

        // 重选服务操作
        String templateId = params.getString("templateId");
        //不一样才会去重选服务
        if (!StringUtils.isEmpty(templateId) && !templateId.equals(sysService.getTemplateId())) {
            //copy jar 和 component.json
            SysInstallPack sysInstallPack = reselectService(sysService.getIntegrationId(), templateId, sysService.getId());
            //修改数据库记录
            sysService.setTemplateId(templateId);
            sysService.setTemplateName(sysInstallPack.getName());
            sysService.setTemplateVersion(sysInstallPack.getVersion() + "");
        }

        if (!StringUtils.isEmpty(params.getString("name"))) {
            sysService.setName(params.getString("name"));
        }
        if (!StringUtils.isEmpty(params.getString("url"))) {
            sysService.setUrl(params.getString("url"));
        }
        if (!StringUtils.isEmpty(params.getString("remark"))) {
            sysService.setRemark(params.getString("remark"));
        }

        tokenService.updateUpdateIdAndTime(request, sysService);
        sysServiceService.insert(sysService);
    }

    /**
     * 查询系统服务配置版本号
     *
     * @param params 信息
     * @return 版本号
     * @throws Exception 异常
     */
    public String version(JSONObject params) throws Exception {
        SysService sysService = sysServiceService.findById(params.getString("appid"));

        File buildTimestampFile = new File(Constants.getSysServiceConfigPath(sysService.getIntegrationId(), sysService.getTemplateType(), params.getString("appid")), "timestamp");
        String currentVersion = FileUtils.readFileToString(buildTimestampFile, StandardCharsets.UTF_8);
        if (StringUtils.isEmpty(sysService.getVersion())) {
            throw new ValidException("版本号不存在");
        }
        String remoteVersion = sysService.getVersion().split(",")[1];
        return currentVersion + " / " + remoteVersion;
    }

    /**
     * @param integrationId 用户id
     * @param templateId    服务模板id
     * @param sysServiceId  系统服务id
     * @return 返回模板信息
     */
    private SysInstallPack reselectService(String integrationId, String templateId, String sysServiceId) throws Exception {
        String rootPath = Constants.getServiceRootPath(integrationId, ServiceType.WEBAPI.getName(), sysServiceId);
        // 临时目录，解压文件到临时目录
        String tempPath = Constants.TEMP_ROOT + System.currentTimeMillis() + File.separator;

        //通过templateId 找到模板路径
        SysInstallPack sysInstallPack = sysInstallPackService.findById(templateId);
        //通过系统服务安装包压缩文件，解压缩获得基础文件
        compressService.unzip(Constants.getBasicInstallPath(sysInstallPack.getType()) + sysInstallPack.getFilename(), tempPath);

        // 删除系统服务下的jar包
        for (File file : new File(rootPath).listFiles()) {
            if (file.getName().endsWith(".jar")) {
                file.delete();
                break;
            }
        }
        // 从临时文件夹中拷贝jar包，components.json文件到系统服务下
        for (File file : new File(tempPath).listFiles()) {
            // 拷贝jar包
            if (file.getName().endsWith(".jar")) {
                // 重命名jar包
                String fileName = "dcrun-" + sysServiceId + "-" + file.getName();
                FileUtils.copyFile(file, new File(rootPath, fileName));
            }
            // 拷贝components.json
            if ("components.json".equals(file.getName())) {
                FileUtils.copyFile(file, new File(rootPath, "components.json"));
            }
        }

        // 删除临时目录
        FileUtils.deleteDirectory(new File(tempPath));

        return sysInstallPack;
    }
}

