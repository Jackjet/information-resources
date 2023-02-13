package d1.project.api.integration.kong.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.entity.BaseEntity;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.api.integration.common.utils.BaseUtils;
import d1.project.api.integration.kong.entity.DefaultConsumer;
import d1.project.api.integration.kong.entity.KongLogDir;
import d1.project.api.integration.kong.service.DefaultConsumerService;
import d1.project.api.integration.kong.service.KongLogDirService;
import d1.project.api.integration.kong.service.KongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/kong")
public class KongController {
    private final KongService kongService;
    private final DefaultConsumerService defaultConsumerService;
    private final KongLogDirService kongLogDirService;

    private static Logger log = LoggerFactory.getLogger(KongController.class);
    @Value("${kong.http.endpoint}")
    private String endpoint;
    @Value("${kong.http.endpoint.method}")
    private String method;

    public KongController(KongService kongService, DefaultConsumerService defaultConsumerService, KongLogDirService kongLogDirService) {
        this.kongService = kongService;
        this.defaultConsumerService = defaultConsumerService;
        this.kongLogDirService = kongLogDirService;
    }

    @GetMapping(value = "/kongInit")
    public Result<String> kongInit(@Valid KongInitGetVm param) throws Exception {
        String logDir = param.getLogDir();
        if(logDir.lastIndexOf("/") == logDir.length() - 1){
            logDir = logDir.substring(0,logDir.lastIndexOf("/"));
        }

        log.info("检查kong是否已安装http-log插件、cors插件...");
        JSONArray dataArray = kongService.getPluginData(param.getContainer(), "plugins");

        Boolean needHttpLogPlugin = true;
        String pluginName = "http-log";
        for (Object obj : dataArray) {
            JSONObject plugin = (JSONObject) obj;
            if (pluginName.equals(plugin.getString("name"))) {
                needHttpLogPlugin = false;
                break;
            }
        }


        if (needHttpLogPlugin) {
            log.info("kong未安装http-log插件，开始安装插件...");
            JSONObject config = new JSONObject();
            config.put("http_endpoint", endpoint + "/" + param.getContainer());
            config.put("method", method);
            config.put("timeout", 10000);
            config.put("keepalive", 60000);
            try {
                kongService.addPlugin(param.getContainer(),"", "", pluginName, config,true);
            }catch (Exception e) {
                return ResultUtil.fail("容器初始化失败");
            }

            log.info("http-log插件安装完毕...");
        }

        Boolean needCorsPlugin = true;
        String corsPluginName = "cors";
        for (Object obj : dataArray) {
            JSONObject plugin = (JSONObject) obj;
            if (corsPluginName.equals(plugin.getString("name"))) {
                needCorsPlugin = false;
                break;
            }
        }

        if (needCorsPlugin) {
            log.info("kong未安装cors插件，开始安装插件...");
            JSONObject config = new JSONObject();
            JSONArray origins = new JSONArray();
            origins.add("*");
            config.put("origins", origins);
            try {
                kongService.addPlugin(param.getContainer(),"", "", corsPluginName, config,true);
            }catch (Exception e) {
                return ResultUtil.fail("容器初始化失败");
            }
            log.info("cors插件安装完毕...");
        }

        //初始化默认用户
        DefaultConsumer defaultConsumer = defaultConsumerService.findByContainer(param.getContainer());
        if(defaultConsumer == null) {
            defaultConsumer = defaultConsumerService.initConsumer(param.getContainer());

            JSONObject addConsumerResult = kongService.addConsumer(param.getContainer(), defaultConsumer.getId());
            defaultConsumer.setConsumerId(addConsumerResult.getString("id"));
            //设置用户所属组
            kongService.addConsumerGroup(param.getContainer(),defaultConsumer.getId(),defaultConsumer.getId());

            //添加hmac密钥
            JSONObject addHmacKeyResult = kongService.addHmacKey(param.getContainer(),defaultConsumer);
            defaultConsumer.setHmacId(addHmacKeyResult.getString("id"));

            //添加key-auth密钥
            JSONObject addKeyAuthResult = kongService.addKeyAuth(param.getContainer(),defaultConsumer);
            defaultConsumer.setKeyAuthId(addKeyAuthResult.getString("id"));

            defaultConsumerService.save(defaultConsumer);
        }

        List<KongLogDir> kongLogDirs = kongLogDirService.findByContainer(param.getContainer());
        if(kongLogDirs.size() > 0){
            kongLogDirService.batchDelete(kongLogDirs);
        }

        KongLogDir access = new KongLogDir();
        access.setId(BaseUtils.generate32Id());
        access.setFileName("access.log");
        access.setContainer(param.getContainer());
        access.setDir(logDir + "/access.log");
        access.setCreateTime(Calendar.getInstance());
        kongLogDirService.save(access);

        KongLogDir adminAccess = new KongLogDir();
        adminAccess.setId(BaseUtils.generate32Id());
        adminAccess.setFileName("admin_access.log");
        adminAccess.setContainer(param.getContainer());
        adminAccess.setDir(logDir + "/admin_access.log");
        adminAccess.setCreateTime(Calendar.getInstance());
        kongLogDirService.save(adminAccess);

        KongLogDir error = new KongLogDir();
        error.setId(BaseUtils.generate32Id());
        error.setFileName("error.log");
        error.setContainer(param.getContainer());
        error.setDir(logDir + "/error.log");
        error.setCreateTime(Calendar.getInstance());
        kongLogDirService.save(error);
        return ResultUtil.success("容器初始化成功");
    }
}

class KongInitGetVm {
    @NotBlank(message = "容器地址不能为空")
    private String container;
    @NotBlank(message = "日志目录不能为空")
    private String logDir;

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getLogDir() {
        return logDir;
    }

    public void setLogDir(String logDir) {
        this.logDir = logDir;
    }
}