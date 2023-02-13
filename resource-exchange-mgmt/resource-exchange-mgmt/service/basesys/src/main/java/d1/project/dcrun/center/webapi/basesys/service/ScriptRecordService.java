package d1.project.dcrun.center.webapi.basesys.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.project.dcrun.center.webapi.basesys.model.ServiceComponent;
import d1.project.dcrun.center.webapi.basesys.model.tree.FileTree;
import d1.project.dcrun.center.webapi.basesys.model.tree.FileType;
import d1.project.dcrun.center.webapi.common.service.management.config.ManagementConfig;
import d1.project.dcrun.center.webapi.common.service.management.config.ManagementConfigService;
import d1.project.dcrun.center.webapi.common.service.system.SysService;
import d1.project.dcrun.center.webapi.common.service.system.SysServiceService;
import d1.project.dcrun.center.webapi.common.util.Constants;
import d1.project.dcrun.center.webapi.common.util.RepoType;
import d1.project.dcrun.center.webapi.common.util.ServiceType;
import net.dcrun.component.http.IHttpService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author libin
 */
@Service
public class ScriptRecordService {
    private static Logger logger = LoggerFactory.getLogger(ScriptRecordService.class);

    @Autowired
    private SysServiceService sysServiceService;
    @Autowired
    private ManagementConfigService managementConfigService;
    @Autowired
    private IHttpService httpService;

    /**
     * 生成文件树
     *
     * @param integrationId 用户id不能为空
     * @param appid         系统服务id
     * @return 树结构json对象
     * @throws Exception 异常
     */
    public String fileTree(String integrationId, String appid) throws Exception {
        // 微应用文件基础路径
        File scriptFile = new File(Constants.getSysServiceScriptPath(integrationId, ServiceType.WEBAPI.getName(), appid));
        if (!scriptFile.exists()) {
            throw new ValidException("目录不存在：" + scriptFile.getAbsolutePath());
        }

        FileTree fileTree = new FileTree();
        fileTree.setName("script");
        fileTree.setPath(scriptFile.getPath() + File.separator);
        fileTree.setFileType(FileType.DIR.getName());
        fileTree.setPermission("0,0,0");

        generateFileTree(fileTree, scriptFile, scriptFile, scriptFile.getPath());

        // 对文件树内的文件按照名字排序
        if (fileTree.getChildren() != null && fileTree.getChildren().size() > 0) {
            fileTreeSort(fileTree.getChildren());
        }

        return JSON.toJSONString(fileTree);
    }

    /**
     * 文件树内文件排序
     *
     * @param fileTreeVmList 文件树
     */
    public void fileTreeSort(List<FileTree> fileTreeVmList) {
        Collections.sort(fileTreeVmList, Comparator.comparing(FileTree::getName));
        for (FileTree fileTreeVm : fileTreeVmList) {
            if (fileTreeVm.getChildren() != null && fileTreeVm.getChildren().size() > 0) {
                fileTreeSort(fileTreeVm.getChildren());
            }
        }
    }

    /**
     * 新增文件/文件夹
     *
     * @param type 文件类型 {@link FileType}
     * @param path 文件全路径
     * @throws Exception 异常
     */
    public void fileNew(String type, String path) throws Exception {
        // 文件路径
        File file = new File(path);
        if (FileType.DIR.getName().equals(type)) {
            FileUtils.forceMkdir(file);
        } else {
            FileUtils.forceMkdir(file.getParentFile());
            file.createNewFile();
        }
    }

    /**
     * 删除文件/文件夹
     *
     * @param path 文件/文件夹路径
     * @throws Exception 异常
     */
    public void fileDelete(String path) {
        FileUtils.deleteQuietly(new File(path));
    }

    /**
     * 保存文件内容
     *
     * @param path    文件路径
     * @param content 文件内容
     * @throws Exception 异常
     */
    public void save(String path, String content) throws Exception {
        File file = new File(path);
        if (!file.exists()) {
            throw new ValidException("该文件不存在");
        }
        FileUtils.write(file, content, StandardCharsets.UTF_8);
    }

    /**
     * 读出文件内容
     *
     * @param path 文件路径
     * @return 文件内容
     * @throws Exception 异常
     */
    public String read(String path) throws Exception {
        File file = new File(path);
        if (!file.exists()) {
            throw new ValidException("该文件不存在");
        }
        // 读取文件内容
        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }

    /**
     * 重命名文件/文件夹
     *
     * @param oldPath 旧的路径
     * @param name    新名字
     */
    public void rename(String oldPath, String name) {
        File originalFile = new File(oldPath);

        String path = FilenameUtils.getPath(oldPath);
        originalFile.renameTo(new File(path, name));
    }

    /**
     * 查询版本号
     *
     * @param params 信息
     * @return 版本号
     * @throws Exception 异常
     */
    public String version(JSONObject params) throws Exception {
        String appid = params.getString("appid");
        SysService sysService = sysServiceService.findById(appid);

        File buildTimestampFile = new File(Constants.getSysServiceScriptPath(sysService.getIntegrationId(), sysService.getTemplateType(), appid) + "timestamp");
        String currentVersion = FileUtils.readFileToString(buildTimestampFile, StandardCharsets.UTF_8);
        if (StringUtils.isEmpty(sysService.getVersion())) {
            throw new ValidException("版本号不存在");
        }
        String remoteVersion = sysService.getVersion().split(",")[2];
        return currentVersion + " / " + remoteVersion;
    }

    /**
     * 递归调用，生成文件树
     *
     * @param fileTreeVm 父文件对象
     * @param file       文件/文件夹
     * @param basePath   文件/文件夹路径
     */
    private void generateFileTree(FileTree fileTreeVm, File root, File file, String basePath) {
        // 如果是文件夹，则遍历得出所有的子文件/子文件夹
        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            if (subFiles != null) {
                List<FileTree> children = new ArrayList<>(subFiles.length);
                for (File subFile : subFiles) {
                    FileTree vm = new FileTree();
                    String path;
                    if (subFile.isDirectory()) {
                        path = basePath + File.separator + subFile.getName();
                        processFilePermission(vm, root, path, true);
                        vm.setFileType(FileType.DIR.getName());
                        generateFileTree(vm, root, subFile, path);
                    } else {
                        path = basePath + File.separator + subFile.getName();
                        processFilePermission(vm, root, path, false);
                        vm.setFileType(FilenameUtils.getExtension(subFile.getName()));
                    }


                    vm.setPath(path);
                    vm.setName(subFile.getName());
                    children.add(vm);
                    fileTreeVm.setChildren(children);
                }
            }
        }
    }

    private void processFilePermission(FileTree vm, File basePath, String path, boolean isDirectory) {
        if (isDirectory) {
            vm.setPermission("1,1,1");
        } else {
            vm.setPermission("0,1,1");
        }

//        script/js           1,0,0
//        script/js/app.js    0,0,0
//        script/kettle       1,0,0
//        script/timestamp    0,0,0
//        script/web          1,0,0

        if ((basePath + File.separator + "js").equals(path)) {
            vm.setPermission("1,0,0");
        } else if ((basePath + File.separator + "js" + File.separator + "app.js").equals(path)) {
            vm.setPermission("0,0,0");
        } else if ((basePath + File.separator + "kettle").equals(path)) {
            vm.setPermission("1,0,0");
        } else if ((basePath + File.separator + "timestamp").equals(path)) {
            //隐藏 timestamp 文件
            vm.setHidden(true);
            vm.setPermission("0,0,0");
        } else if ((basePath + File.separator + "web").equals(path)) {
            vm.setPermission("1,0,0");
        }
    }

    /**
     * 日志订阅地址信息
     *
     * @param params
     * @return
     * @throws Exception
     */
    public JSONObject getWsInfo(JSONObject params) throws Exception {
        SysService sysService = sysServiceService.findById(params.getString("appid"));
        ManagementConfig mqWsUrl = managementConfigService.findByConfigKey("manage.center.mq.ws.url");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appid", sysService.getId());
        jsonObject.put("appkey", sysService.getAppkey());
        jsonObject.put("mqWsUrl", mqWsUrl);
        jsonObject.put("topicType", sysService.getTemplateType());
        return jsonObject;
    }

    /**
     * 修改调试模式
     *
     * @param params
     */
    public void updateDebug(JSONObject params) throws Exception {
        String logbackPath = Constants.getLogbackXmlPath(params.getString("integrationId"), ServiceType.WEBAPI.getName(), params.getString("appid"));
        File logbackFile = new File(logbackPath);
        String context = FileUtils.readFileToString(logbackFile, StandardCharsets.UTF_8);

        // debug行内容
        boolean debug = params.getBoolean("debug");
        String debugStr = "<debug>" + debug + "</debug>";
        if (!context.contains(debugStr)) {
            context = context.replace("<debug>" + !debug + "</debug>", "<debug>" + debug + "</debug>");
        }
        FileUtils.write(logbackFile, context, StandardCharsets.UTF_8);
    }

    /**
     * 查询调试模式
     *
     * @param params
     */
    public String findDebug(JSONObject params) throws Exception {
        String logbackPath = Constants.getLogbackXmlPath(params.getString("integrationId"), ServiceType.WEBAPI.getName(), params.getString("appid"));
        File logbackFile = new File(logbackPath);
        String context = FileUtils.readFileToString(logbackFile, StandardCharsets.UTF_8);
        // debug行内容
        int beginIndex = context.indexOf("<debug>");
        int endIndex = context.indexOf("</debug>");
        return context.substring(beginIndex + "<debug>".length(), endIndex);
    }

    public List<ServiceComponent> findByAppid(JSONObject params) throws Exception {
        String integrationId = params.getString("integrationId");
        String appid = params.getString("appid");

        String rootPath = Constants.getServiceRootPath(integrationId, ServiceType.WEBAPI.getName(), appid);
        File componentFile = new File(rootPath, "components.json");
        String content = FileUtils.readFileToString(componentFile, StandardCharsets.UTF_8);
        JSONObject jsonObj = JSON.parseObject(content);

        // 从components.json文件中读取组件信息
        List<ServiceComponent> serviceComponentList = new ArrayList<>();
        JSONArray array = jsonObj.getJSONArray("dependencies");
        for (Object obj : array) {
            JSONObject tempObj = (JSONObject) obj;

            ServiceComponent component = new ServiceComponent();
            component.setComponentGroup(tempObj.getString("group"));
            component.setComponentName(tempObj.getString("name"));
            component.setComponentVersion(tempObj.getString("version"));
            String[] groups = tempObj.getString("group").split("\\.");
            component.setDeveloperId(groups[0]);
            component.setRepoType(tempObj.getString("repoType"));
            serviceComponentList.add(component);
        }

        // add dcrun
        ServiceComponent component = new ServiceComponent();
        component.setDeveloperId("net");
        component.setComponentGroup("net.dcrun.component");
        component.setComponentName("dcrun");
        component.setComponentVersion("1.0.0");
        component.setRepoType(RepoType.OFFICIAL.getName());
        serviceComponentList.add(0, component);

        return serviceComponentList;
    }

    public JSONObject getComponentsHints(String integrationId, String appid) throws Exception {
        if (StringUtils.isEmpty(integrationId) || StringUtils.isEmpty(appid)) {
            throw new ValidException("获取一个系统服务的组件IDE提示，用户id或系统服务id不能为空");
        }
        //一个脚本运行服务在IDE里编辑代码的时候需要有所选所有组件的代码自动提示
        JSONObject result = new JSONObject();
        JSONArray requires = new JSONArray();
        JSONArray classes = new JSONArray();
        JSONArray methods = new JSONArray();

        result.put("requires", requires);
        result.put("classes", classes);
        result.put("methods", methods);
        //1. 根据用户id，appid查出对应的components.json文件
        String serviceRootPath = Constants.getServiceRootPath(integrationId, ServiceType.WEBAPI.getName(), appid);
        File hintDir = new File(serviceRootPath, "hint");
        if (!hintDir.exists()) {
            return result;
        }

        //2. 把所有组件对应的hint json文件合并起来
        File[] hintFiles = hintDir.listFiles();
        for (int i = 0; i < hintFiles.length; i++) {
            File jsonFile = hintFiles[i];
            String content = FileUtils.readFileToString(jsonFile, StandardCharsets.UTF_8);
            JSONObject temp = JSON.parseObject(content);
            if (!StringUtils.isEmpty(temp.getString("require"))) {
                requires.add(temp.getString("require"));
            }
            classes.addAll(temp.getJSONArray("classes"));
            methods.addAll(temp.getJSONArray("methods"));
        }

        return result;
    }
}
