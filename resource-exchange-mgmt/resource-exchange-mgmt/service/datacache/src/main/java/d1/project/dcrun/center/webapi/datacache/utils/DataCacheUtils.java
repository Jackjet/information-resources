package d1.project.dcrun.center.webapi.datacache.utils;

import d1.project.dcrun.center.webapi.common.util.Constants;
import d1.project.dcrun.center.webapi.common.util.OperationType;
import d1.project.dcrun.center.webapi.common.util.ServiceType;

import java.io.File;

/**
 * 数据缓存服务的工具类
 *
 * @author maorui
 */
public class DataCacheUtils {


    /**
     * 获取 数据缓存服务 service 根路径
     *
     * @param integrationId 租户id（原集成项目id）
     * @param appId         系统服务appId
     * @return service 根路径
     */
    public static String getServiceRootPath(String integrationId, String appId) {
        return Constants.getServiceRootPath(integrationId, ServiceType.DATACACHE.getName(), appId);
    }


    /**
     * 获取数据缓存服务 service 目录下面的 config文件夹路径
     *
     * @param integrationId 租户id（原集成项目id）
     * @param appId         系统服务appId
     * @return 数据缓存服务 service 目录下面的 config文件夹路径
     */
    public static String getServiceConfigPath(String integrationId, String appId) {
        return getServiceRootPath(integrationId, appId) + "config" + File.separator;
    }


    /**
     * 获取 data_cache_config.json 文件路径
     *
     * @param integrationId 租户id（原集成项目id）
     * @param appId         系统服务appId
     * @return data_cache_config.json 文件路径
     */
    public static String getConfigJsonPath(String integrationId, String appId) {
        return getServiceRootPath(integrationId, appId) + "data_cache_config.json";
    }


    /**
     * 获取key.json的输出路径
     *
     * @param appId         系统服务appId
     * @return key.json的输出路径，appId_key.json
     */
    public static String getKeyJsonOutputPath(String appId) {
        return Constants.FILES_DOWNLOAD_DATACACHE + appId + "_key.json";
    }


    public static String getInstallTopic(String integrationId, String nodeId, String appId) {
        return integrationId + "/" + nodeId + "/" + ServiceType.DATACACHE.getName() + "/" + appId + "/" + OperationType.INSTALL.getName();
    }

    public static String getSyncKeyTopic(String integrationId, String nodeId, String appId) {
        return integrationId + "/" + nodeId + "/" + ServiceType.DATACACHE.getName() + "/" + appId + "/" + OperationType.SYNC_KEY.getName();
    }
}
