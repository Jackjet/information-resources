package d1.project.dcrun.center.webapi.dcapi.utils;

import d1.project.dcrun.center.webapi.common.util.Constants;
import d1.project.dcrun.center.webapi.common.util.OperationType;
import d1.project.dcrun.center.webapi.common.util.ServiceType;

import java.io.File;

/**
 * dcapi utils
 *
 * @author maorui
 */
public class DCApiUtils {

    /**
     * 获取 dcapi service 根路径
     *
     * @param integrationId 租户id（原集成项目id）
     * @param appId         系统服务appId
     * @return service 根路径
     */
    public static String getServiceRootPath(String integrationId, String appId) {
        return Constants.getServiceRootPath(integrationId, ServiceType.DCAPI.getName(), appId);
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
     * 获取系统服务的application.properties文件的路径
     *
     * @param appid
     * @return
     */
    public static String getApplicationPropertiesPath(String integrationId, String appid) {
        return getServiceConfigPath(integrationId, appid) + "application.properties";
    }


    /**
     * 获取application.properties的输出路径
     *
     * @param appId 系统服务appId
     * @return application.properties 的输出路径，appId_application.properties
     */
    public static String getApplicationPropertiesOutputPath(String appId) {
        return Constants.FILES_DOWNLOAD_DCAPI + appId + "_application.properties";
    }

    /**
     * 获取router.json的输出路径
     *
     * @param appId 系统服务appId
     * @return 输出路径，appId_api_route.json
     */
    public static String getRouterJsonOutputPath(String appId) {
        return Constants.FILES_DOWNLOAD_DCAPI + appId + "_api_route.json";
    }

    /**
     * 获取api_user.json的输出路径
     *
     * @param appId 系统服务appId
     * @return 输出路径，appId_api_user.json
     */
    public static String getUserJsonOutputPath(String appId) {
        return Constants.FILES_DOWNLOAD_DCAPI + appId + "_api_user.json";
    }


    public static String getInstallTopic(String integrationId, String nodeId, String appId) {
        return integrationId + "/" + nodeId + "/" + ServiceType.DCAPI.getName() + "/" + appId + "/" + OperationType.INSTALL.getName();
    }

    public static String getSyncTopic(String integrationId, String nodeId, String appId) {
        return integrationId + "/" + nodeId + "/" + ServiceType.DCAPI.getName() + "/" + appId + "/" + OperationType.SYNC.getName();
    }

    public static String getSyncRouterTopic(String integrationId, String nodeId, String appId) {
        return integrationId + "/" + nodeId + "/" + ServiceType.DCAPI.getName() + "/" + appId + "/" + OperationType.SYNC_ROUTES.getName();
    }

    public static String getSyncUserTopic(String integrationId, String nodeId, String appId) {
        return integrationId + "/" + nodeId + "/" + ServiceType.DCAPI.getName() + "/" + appId + "/" + OperationType.SYNC_USER.getName();
    }

}
