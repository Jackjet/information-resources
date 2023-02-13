package d1.project.resource.resourcemanage.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author baozh
 */
@Entity
@Table(name = "d1_container_info")
@ApiModel(value = "ContainerInfo", description = "容器管理")
public class ContainerInfo extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("容器名称")
    @Column(length = 50)
    private String name;

    @ApiModelProperty("容器IP地址")
    @Column(length = 50)
    private String ip;

    @ApiModelProperty("端口号")
    @Column(length = 10)
    private String port;

    @ApiModelProperty("详情")
    @Column(columnDefinition = "TEXT")
    private String detail;

    @ApiModelProperty("状态(0,不可用，1，可用)")
    @Column(length = 3)
    private String status;

    @ApiModelProperty("创建人名称")
    @Column(length = 50)
    private String createName;

    @ApiModelProperty("修改人名称")
    @Column(length = 50)
    private String updateName;

    @ApiModelProperty("分组ID")
    @Column(length = 50)
    private String groupId;

    @ApiModelProperty("标签名")
    @Column(length = 50)
    private String tagName;

    @ApiModelProperty("标签值")
    @Column(length = 50)
    private String tagValue;

    @ApiModelProperty("0:API运行,1:数据,2:消息,3:API网关,4:其他。")
    @Column(length = 3)
    private String type;

    @ApiModelProperty("0:未初始化,1:已初始化。")
    @Column(length = 3)
    private String isInit;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsInit() {
        return isInit;
    }

    public void setIsInit(String isInit) {
        this.isInit = isInit;
    }
}
