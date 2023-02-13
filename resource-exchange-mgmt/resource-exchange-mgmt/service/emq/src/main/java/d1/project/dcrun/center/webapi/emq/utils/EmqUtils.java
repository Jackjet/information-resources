package d1.project.dcrun.center.webapi.emq.utils;

import d1.project.dcrun.center.webapi.common.util.Constants;
import d1.project.dcrun.center.webapi.common.util.OperationType;
import d1.project.dcrun.center.webapi.common.util.ServiceType;

import java.io.File;

/**
 * 消息集成服务的工具类
 *
 * @author maorui
 */
public class EmqUtils {


    /**
     * 获取 消息集成服务 service 根路径
     *
     * @param integrationId 租户id（原集成项目id）
     * @param appId         系统服务appId
     * @return service 根路径
     */
    public static String getServiceRootPath(String integrationId, String appId) {
        return Constants.getServiceRootPath(integrationId, ServiceType.EMQ.getName(), appId);
    }


    /**
     * 获取消息集成服务 service 目录下面的 config文件夹路径
     *
     * @param integrationId 租户id（原集成项目id）
     * @param appId         系统服务appId
     * @return 消息集成服务 service 目录下面的 config文件夹路径
     */
    public static String getServiceConfigPath(String integrationId, String appId) {
        return getServiceRootPath(integrationId, appId) + "config" + File.separator;
    }


    /**
     * 获取 emq_config.json 文件路径
     *
     * @param integrationId 租户id（原集成项目id）
     * @param appId         系统服务appId
     * @return emq_config.json 文件路径
     */
    public static String getConfigJsonPath(String integrationId, String appId) {
        return getServiceRootPath(integrationId, appId) + "emq_config.json";
    }


    /**
     * 获取emq_config.json的输出路径
     *
     * @param appId 系统服务appId
     * @return emq_config.json的输出路径，appId_emq_config.json
     */
    public static String getConfigJsonOutputPath(String appId) {
        return Constants.FILES_DOWNLOAD_EMQ + appId + "_emq_config.json";
    }


    /**
     * 获取permission.json的输出路径
     *
     * @param appId 系统服务appId
     * @return 输出路径，appId_permission.json
     */
    public static String getPermissionJsonOutputPath(String appId) {
        return Constants.FILES_DOWNLOAD_EMQ + appId + "_permission.json";
    }

    /**
     * 获取emq_user.json的输出路径
     *
     * @param appId 系统服务appId
     * @return 输出路径，appId_emq_user.json
     */
    public static String getUserJsonOutputPath(String appId) {
        return Constants.FILES_DOWNLOAD_EMQ + appId + "_emq_user.json";
    }


    public static String getInstallTopic(String integrationId, String nodeId, String appId) {
        return integrationId + "/" + nodeId + "/" + ServiceType.EMQ.getName() + "/" + appId + "/" + OperationType.INSTALL.getName();
    }

    public static String getSyncTopic(String integrationId, String nodeId, String appId) {
        return integrationId + "/" + nodeId + "/" + ServiceType.EMQ.getName() + "/" + appId + "/" + OperationType.SYNC.getName();
    }

    public static String getSyncPermissionTopic(String integrationId, String nodeId, String appId) {
        return integrationId + "/" + nodeId + "/" + ServiceType.EMQ.getName() + "/" + appId + "/" + OperationType.SYNC_TOPIC_PERMISSION.getName();
    }

    public static String getSyncUserTopic(String integrationId, String nodeId, String appId) {
        return integrationId + "/" + nodeId + "/" + ServiceType.EMQ.getName() + "/" + appId + "/" + OperationType.SYNC_USER.getName();
    }
}
