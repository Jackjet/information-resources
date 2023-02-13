package d1.project.tangshan.operation.manage.entity.operations.module;

import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author lin
 */
@Entity
@Table(name = "d1_node")
@ApiModel(value = "Node", description = "节点")
public class Node extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty(value = "公网IP",notes = "实际是内网IP")
    private String publicIp;
    @ApiModelProperty(value = "内网IP",notes = "实际是公网IP")
    private String intranetIp;
    @ApiModelProperty("用途")
    private String purpose;
    @ApiModelProperty("配置")
    private String configuration;
    @ApiModelProperty("备注")
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublicIp() {
        return publicIp;
    }

    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
    }

    public String getIntranetIp() {
        return intranetIp;
    }

    public void setIntranetIp(String intranetIp) {
        this.intranetIp = intranetIp;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
