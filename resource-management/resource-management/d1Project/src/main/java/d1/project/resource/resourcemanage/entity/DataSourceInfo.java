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
@Table(name = "d1_data_source_info")
@ApiModel(value = "DataSourceInfo", description = "数据源管理")
public class DataSourceInfo extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("数据源名称")
    @Column(length = 50)
    private String name;

    @ApiModelProperty("类型（MySQL/PostgreSQL/Oracle）")
    @Column(length = 20)
    private String type;

    @ApiModelProperty("数据库连接IP")
    @Column(length = 50)
    private String host;

    @ApiModelProperty("数据库连接端口号")
    @Column(length = 10)
    private String port;

    @ApiModelProperty("数据库名称")
    @Column(length = 50)
    private String dataName;

    @ApiModelProperty("账号")
    @Column(length = 50)
    private String userName;

    @ApiModelProperty("密码")
    @Column(length = 50)
    private String password;

    @ApiModelProperty("详情")
    @Column(columnDefinition = "TEXT")
    private String detail;

    @ApiModelProperty("状态")
    @Column(length = 3)
    private String status;

    @ApiModelProperty("创建人名称")
    @Column(length = 50)
    private String createName;

    @ApiModelProperty("修改人名称")
    @Column(length = 50)
    private String updateName;

    @ApiModelProperty("修改人名称")
    @Column(length = 50)
    private String groupId;

    @ApiModelProperty("标签名")
    @Column(columnDefinition = "TEXT")
    private String tagName;

    @ApiModelProperty("标签值")
    @Column(columnDefinition = "TEXT")
    private String tagValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
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
}
