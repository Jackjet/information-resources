package d1.project.d1project.apidesign.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * API部署信息
 *
 * @author baozh
 */

@Entity
@Table(name = "d1_api_deployment")
@ApiModel(value = "ApiDeployment", description = "API部署信息")
public class ApiDeployment extends BaseCreateEntity {
    @ApiModelProperty("api设计ID")
    @Column(length = 50)
    private String apiDesignId;

    @ApiModelProperty("容器ID")
    @Column(length = 50)
    private String containerId;

    @ApiModelProperty("容器名称")
    @Column(length = 50)
    private String name;

    @ApiModelProperty("容器地址")
    @Column(length = 255)
    private String url;

    @ApiModelProperty("部署文件")
    @Column(columnDefinition = "TEXT")
    private String deploymentFile;

    @ApiModelProperty("状态(0：停止，1：运行)")
    @Column(length = 50)
    private String status;

    public String getApiDesignId() {
        return apiDesignId;
    }

    public void setApiDesignId(String apiDesignId) {
        this.apiDesignId = apiDesignId;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeploymentFile() {
        return deploymentFile;
    }

    public void setDeploymentFile(String deploymentFile) {
        this.deploymentFile = deploymentFile;
    }
}
