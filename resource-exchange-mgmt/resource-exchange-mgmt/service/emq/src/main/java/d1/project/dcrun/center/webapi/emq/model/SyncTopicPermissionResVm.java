package d1.project.dcrun.center.webapi.emq.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author xuaa
 */
@ApiModel(value = "SyncTopicPermissionResVm", description = "同步开发者信息")
public class SyncTopicPermissionResVm {
    @ApiModelProperty(value = "开发者id")
    private String appid;
    @ApiModelProperty(value = "topic名称")
    private String topicId;
    @ApiModelProperty(value = "权限")
    private String permission;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
