package d1.project.dcrun.center.webapi.basesys.model;

import io.swagger.annotations.ApiModelProperty;

/**
 * 脚本运行服务选择的组件信息
 *
 * @author maorui
 */
public class ServiceComponent {
    @ApiModelProperty(value = "自定义脚本运行服务id")
    private String customId;
    @ApiModelProperty(value = "开发者id")
    private String developerId;
    @ApiModelProperty(value = "组件库地址")
    private String repository;
    @ApiModelProperty(value = "组件组")
    private String componentGroup;
    @ApiModelProperty(value = "组件名")
    private String componentName;
    @ApiModelProperty(value = "组件版本")
    private String componentVersion;
    @ApiModelProperty(value = "组件所属库类型")
    private String repoType;
    @ApiModelProperty(value = "组件依赖库地址")
    private String dependenceRepository;
    @ApiModelProperty(value = "适用平台")
    private String platform;
    @ApiModelProperty(value = "备注")
    private String remark;

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public String getDeveloperId() {
        return developerId;
    }

    public void setDeveloperId(String developerId) {
        this.developerId = developerId;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getComponentGroup() {
        return componentGroup;
    }

    public void setComponentGroup(String componentGroup) {
        this.componentGroup = componentGroup;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentVersion() {
        return componentVersion;
    }

    public void setComponentVersion(String componentVersion) {
        this.componentVersion = componentVersion;
    }

    public String getRepoType() {
        return repoType;
    }

    public void setRepoType(String repoType) {
        this.repoType = repoType;
    }

    public String getDependenceRepository() {
        return dependenceRepository;
    }

    public void setDependenceRepository(String dependenceRepository) {
        this.dependenceRepository = dependenceRepository;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
