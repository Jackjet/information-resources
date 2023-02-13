package d1.project.dcrun.center.webapi.system.service.factory;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.project.dcrun.center.webapi.common.service.emq.EmqBaseService;
import d1.project.dcrun.center.webapi.common.service.system.SysService;
import d1.project.dcrun.center.webapi.common.service.system.SysServiceService;
import d1.project.dcrun.center.webapi.common.util.Constants;
import d1.project.dcrun.center.webapi.system.entity.SysInstallPack;
import net.dcrun.component.compression.ICompressService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.MappedSuperclass;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 所有系统服务（webapi，api网关，消息集成等等系统服务）的基类
 *
 * @author Buter
 * @date 2020/5/10 19:36
 */
@MappedSuperclass
public abstract class AbstractSystemService {
    protected static Logger logger = LoggerFactory.getLogger(AbstractSystemService.class);
    @Autowired
    ICompressService compressService;
    @Autowired
    private EmqBaseService emqBaseService;

    /**
     * 创建系统服务的基础文件
     *
     * @param sysService 系统服务
     * @param rootPath   系统服务根路径
     * @throws Exception
     */
    public void createBaseFiles(SysService sysService, String rootPath, SysInstallPack template) throws Exception {
        String version = String.valueOf(template.getVersion());
        sysService.setTemplateId(template.getId());
        sysService.setTemplateName(template.getName());
        sysService.setTemplateVersion(version);

        //通过系统服务安装包压缩文件，解压缩获得基础文件
        compressService.unzip(Constants.getBasicInstallPath(template.getType()) + template.getFilename(), rootPath);

        // 修改jar包名字
        for (File tempFile : new File(rootPath).listFiles()) {
            if (tempFile.isFile() && tempFile.getName().endsWith(".jar")) {
                String jarName = "dcrun-" + sysService.getId() + "-" + tempFile.getName();
                tempFile.renameTo(new File(rootPath, jarName));
                break;
            }
        }

        // 生成一个version文件，记录安装包的版本
        File versionFile = new File(rootPath, "version");
        createVersionFile(versionFile, version);
    }

    /**
     * 创建基础安装包的版本号文件
     *
     * @param versionFile 版本文件
     * @param version     版本号
     * @throws Exception
     */
    public void createVersionFile(File versionFile, String version) throws Exception {
        FileUtils.write(versionFile, version, StandardCharsets.UTF_8);
    }

    /**
     * 创建config文件夹下的内容
     *
     * @param id     系统服务id
     * @param appkey 系统服务appkey
     * @throws Exception
     */
    public void createConfigFiles(String integrationId, String type, String id, String appkey) throws Exception {
        // 创建config文件夹
        String configPath = Constants.getSysServiceConfigPath(integrationId, type, id);
        File configFile = new File(configPath);
        configFile.mkdirs();

        // 创建config文件夹下的内容
        createPartOfConfigFiles(id, appkey, configPath);

        // 创建timestamp文件，用于存储配置的时间戳(即版本号)
        File configTimestamp = new File(configPath, "timestamp");
        FileUtils.write(configTimestamp, "--", StandardCharsets.UTF_8);
    }

    /**
     * 创建config文件夹下的内容
     *
     * @param id         系统服务id
     * @param appkey     系统服务appkey
     * @param configPath config文件夹路径
     * @throws Exception
     */
    public abstract void createPartOfConfigFiles(String id, String appkey, String configPath) throws Exception;

    /**
     * 自动创建appid(id)，appkey并作为内置emq的用户，同时授权“用户id/系统服务类型/系统服务appid/+”的所有权限
     *
     * @param id            appid
     * @param appkey        appkey
     * @param integrationId integrationId
     * @param type          系统服务类型
     * @throws Exception Exception
     */
    public void createEmqUserAndAuthorizePermission(String id, String appkey, String integrationId, String type) throws Exception {
        //添加内置用户
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> mapUser = new HashMap<>(2);
        mapUser.put("userName", id);
        mapUser.put("password", appkey);
        list.add(mapUser);
        //授权
        String topic = integrationId + "/" + type + "/" + id + "/+";
        List<Map<String, String>> listTopic = new ArrayList<>();
        Map<String, String> mapTopic = new HashMap<>(3);
        mapTopic.put("userName", id);
        mapTopic.put("access", "2");
        mapTopic.put("topic", topic);
        listTopic.add(mapTopic);
        emqBaseService.addUserAndPermission(list, listTopic);
    }

    /**
     * 每种系统服务独有的处理逻辑，归属于其他处理部分
     *
     * @param sysService 系统服务对象
     * @throws Exception
     */
    public abstract void specialOperations(SysService sysService) throws Exception;

    /**
     * 编辑校验
     *
     * @param sysServiceService
     * @param old               旧服务
     * @param params            传入参数
     * @throws Exception
     */
    public void updateVerify(SysServiceService sysServiceService, SysService old, JSONObject params) throws Exception {
        SysService sysService = JSONObject.parseObject(params.toJSONString(), SysService.class);
        //修改名称是否重名
        if (!sysService.getName().equals(old.getName())) {
            if (sysServiceService.existsByNameAndRunNodeId(sysService.getName(), old.getRunNodeId())) {
                throw new ValidException("该系统服务名称已存在");
            }
        }
    }

    /**
     * 特殊操作
     *
     * @param json 传入参数
     * @throws Exception
     */
    public abstract void updateSpecialOperations(JSONObject json) throws Exception;
}
