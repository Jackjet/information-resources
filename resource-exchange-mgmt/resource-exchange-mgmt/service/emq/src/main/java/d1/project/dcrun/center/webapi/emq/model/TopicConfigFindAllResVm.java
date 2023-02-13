package d1.project.dcrun.center.webapi.emq.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author xuaa
 */
@ApiModel(value = "TopicConfigFindAllResVm", description = "Topic配置表返回值")
public class TopicConfigFindAllResVm {
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "集成项目id")
    private String integrationId;
    @ApiModelProperty(value = "开发者id")
    private String appid;
    @ApiModelProperty(value = "开发者名称")
    private String name;
    @ApiModelProperty(value = "topic名称")
    private String topicId;
    @ApiModelProperty(value = "权限")
    private String permission;
    @ApiModelProperty(value = "是否选中 true/false")
    private String checked;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }
}
