package d1.project.dcrun.center.webapi.common.util;

import java.io.File;

/**
 * 常量工具类
 *
 * @author chengh
 **/
public class Constants {

    /**
     * dcrun文件结构。具体详情参考 项目根目录下dcrun.txt文件
     */
    public static String TEMP_ROOT = "." + File.separator + "temp" + File.separator;

    public static String FILES_ROOT = "." + File.separator + "files" + File.separator;

    public static String FILES_BASIC = FILES_ROOT + "basic" + File.separator;
    public static String FILES_SERVICE = FILES_ROOT + "service" + File.separator;
    public static String FILES_DOWNLOAD = FILES_ROOT + "download" + File.separator;
    public static String FILES_BUILD = FILES_ROOT + "build" + File.separator;

    public static String FILES_BASIC_NODE = FILES_BASIC + ServiceType.NODE.getName() + File.separator;
    public static String FILES_BASIC_WEBAPI = FILES_BASIC + ServiceType.WEBAPI.getName() + File.separator;
    public static String FILES_BASIC_EMQ = FILES_BASIC + ServiceType.EMQ.getName() + File.separator;
    public static String FILES_BASIC_DCAPI = FILES_BASIC + ServiceType.DCAPI.getName() + File.separator;
    public static String FILES_BASIC_DATACACHE = FILES_BASIC + ServiceType.DATACACHE.getName() + File.separator;

    public static String FILES_BASIC_NODE_INSTALL = FILES_BASIC_NODE + "install" + File.separator;

    public static String FILES_BASIC_WEBAPI_SOURCE = FILES_BASIC_WEBAPI + "source" + File.separator;
    public static String FILES_BASIC_WEBAPI_INSTALL = FILES_BASIC_WEBAPI + "install" + File.separator;
    public static String FILES_BASIC_WEBAPI_COMPONENTS = FILES_BASIC_WEBAPI + "components" + File.separator;

    public static String FILES_BASIC_DCAPI_INSTALL = FILES_BASIC_DCAPI + "install" + File.separator;
    public static String FILES_BASIC_DATACACHE_INSTALL = FILES_BASIC_DATACACHE + "install" + File.separator;

    public static String FILES_DOWNLOAD_NODE = FILES_DOWNLOAD + ServiceType.NODE.getName() + File.separator;
    public static String FILES_DOWNLOAD_WEBAPI = FILES_DOWNLOAD + ServiceType.WEBAPI.getName() + File.separator;
    public static String FILES_DOWNLOAD_EMQ = FILES_DOWNLOAD + ServiceType.EMQ.getName() + File.separator;
    public static String FILES_DOWNLOAD_DCAPI = FILES_DOWNLOAD + ServiceType.DCAPI.getName() + File.separator;
    public static String FILES_DOWNLOAD_DATACACHE = FILES_DOWNLOAD + ServiceType.DATACACHE.getName() + File.separator;

    public static String FILES_BUILD_WEBAPI = FILES_BUILD + ServiceType.WEBAPI.getName() + File.separator;
    public static String FILES_BUILD_WEBAPI_TEMP = FILES_BUILD_WEBAPI + "temp" + File.separator;

    /**
     * resources文件夹中各个模板文件的路径
     */
    public static String TEMPLATE_DATACACHE_APP = "template" + File.separator + ServiceType.DATACACHE.getName() + File.separator + "application.properties";
    public static String TEMPLATE_DATACACHE_CONFIG = "template" + File.separator + ServiceType.DATACACHE.getName() + File.separator + "data_cache_config.json";

    public static String TEMPLATE_DCAPI_POSTGRESQL_APP = "template" + File.separator + ServiceType.DCAPI.getName() + File.separator + "postgresql" + File.separator + "application.properties";

    public static String TEMPLATE_EMQ_CONFIG = "template" + File.separator + ServiceType.EMQ.getName() + File.separator + "emq_config.json";

    public static String TEMPLATE_WEBAPI_APP = "template" + File.separator + ServiceType.WEBAPI.getName() + File.separator + "application.properties";
    public static String TEMPLATE_WEBAPI_LOGBACK = "template" + File.separator + ServiceType.WEBAPI.getName() + File.separator + "logback.xml";

    public static String TEMPLATE_NODE_INSTALL = "template" + File.separator + ServiceType.NODE.getName() + File.separator + "install.sh";
    public static String TEMPLATE_NODE_RUNSH = "template" + File.separator + ServiceType.NODE.getName() + File.separator + "run.sh";
    public static String TEMPLATE_NODE_RUNBAT = "template" + File.separator + ServiceType.NODE.getName() + File.separator + "run.bat";


    /**
     * 获取系统服务的rootPath根路径
     *
     * @param type
     * @return
     */
    public static String getDownloadPathByType(String type) {
        return FILES_DOWNLOAD + type + File.separator;
    }

    /**
     * 获取系统服务的rootPath根路径
     *
     * @param integrationId
     * @param type
     * @param appid
     * @return
     */
    public static String getServiceRootPath(String integrationId, String type, String appid) {
        return FILES_SERVICE + integrationId + File.separator + type + File.separator + appid + File.separator;
    }

    /**
     * 获取不同系统服务的install目录
     *
     * @param type
     * @return
     */
    public static String getBasicInstallPath(String type) {
        return FILES_BASIC + type + File.separator + "install" + File.separator;
    }

    /**
     * 获取lib-js文件夹
     *
     * @param rootPath
     * @return
     */
    public static String getLibJsPath(String rootPath) {
        return rootPath + "dcrun" + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "lib-js" + File.separator;
    }

    /**
     * 获取系统服务config文件夹路径
     *
     * @param appid
     * @return
     */
    public static String getSysServiceConfigPath(String integrationId, String type, String appid) {
        return getServiceRootPath(integrationId, type, appid) + "config" + File.separator;
    }

    /**
     * 获取系统服务config.json文件路径
     *
     * @param appid
     * @return
     */
    public static String getConfigJsonPath(String integrationId, String type, String appid) {
        return getSysServiceConfigPath(integrationId, type, appid) + "config.json";
    }


    /**
     * 获取系统服务的application.properties文件的路径
     *
     * @param appid
     * @return
     */
    public static String getApplicationPropertiesPath(String integrationId, String type, String appid) {
        return getSysServiceConfigPath(integrationId, type, appid) + "application.properties";
    }

    /**
     * 获取系统服务的日志文件的路径
     *
     * @param appid
     * @return
     */
    public static String getLogbackXmlPath(String integrationId, String type, String appid) {
        return getSysServiceConfigPath(integrationId, type, appid) + "logback.xml";
    }

    //----------------------华丽的分割线---------------------

    /**
     * 验签用的appid和appkey
     */
    public static String VERIFY_SIGN_APPID = "11111111";
    public static String VERIFY_SIGN_APPKEY = "11111111";

    /**
     * 获取系统服务script文件夹路径
     *
     * @param appid
     * @return
     */
    public static String getSysServiceScriptPath(String integrationId, String type, String appid) {
        return getServiceRootPath(integrationId, type, appid) + "script" + File.separator;
    }

    /**
     * 获取在脚本文件夹下，不能编辑和删除的文件或文件夹路径
     * 不能编辑的文件或文件夹有四个
     * ./file/services/appid/script
     * ./file/services/appid/script/js
     * ./file/services/appid/script/kettle
     * ./file/services/appid/script/js/app.js
     *
     * @param appid
     * @return
     */
    public static String getCannotEditOrDelete(String integrationId, String type, String appid) {
        return getSysServiceScriptPath(integrationId, type, appid) + ","
                + getSysServiceScriptPath(integrationId, type, appid) + "js" + File.separator + ","
                + getSysServiceScriptPath(integrationId, type, appid) + "kettle" + File.separator + ","
                + getSysServiceScriptPath(integrationId, type, appid) + "js" + File.separator + "app.js";
    }

    /**
     * 获取在脚本文件夹下，不能展示在页面中的文件
     * ./file/services/appid/script/timestamp
     *
     * @param appid
     * @return
     */
    public static String getHiddenFolderOrFile(String integrationId, String type, String appid) {
        return getSysServiceScriptPath(integrationId, type, appid) + "timestamp";
    }
}
