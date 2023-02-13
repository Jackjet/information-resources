package d1.project.dcrun.center.webapi.common.service.system;

import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * SysService 系统服务
 *
 * @author xuaa
 */
@Entity
@Table(name = "d1_sys_service")
@ApiModel(value = "SysService", description = "SysService 系统服务")
public class SysService extends BaseCreateAndUpdateEntity {
    @ApiModelProperty(value = "系统服务appkey")
    private String appkey;

    @ApiModelProperty(value = "用户id")
    private String integrationId;

    @ApiModelProperty(value = "系统服务所在的运行节点id")
    private String runNodeId;

    @ApiModelProperty(value = "备注")
    private String nodeName;

    @ApiModelProperty(value = "系统服务模版id")
    private String templateId;

    @ApiModelProperty(value = "系统服务模板名称")
    private String templateName;

    @ApiModelProperty(value = "系统服务模板类型")
    private String templateType;

    @ApiModelProperty(value = "系统服务模板版本号")
    private String templateVersion;

    @ApiModelProperty(value = "系统服务名称")
    private String name;

    @ApiModelProperty(value = "打包类型")
    private String type;

    @ApiModelProperty(value = "系统服务版本号")
    private String version;

    @ApiModelProperty(value = "系统服务url")
    private String url;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "备注")
    private String remark;

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
    }

    public String getRunNodeId() {
        return runNodeId;
    }

    public void setRunNodeId(String runNodeId) {
        this.runNodeId = runNodeId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getTemplateVersion() {
        return templateVersion;
    }

    public void setTemplateVersion(String templateVersion) {
        this.templateVersion = templateVersion;
    }

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
}
