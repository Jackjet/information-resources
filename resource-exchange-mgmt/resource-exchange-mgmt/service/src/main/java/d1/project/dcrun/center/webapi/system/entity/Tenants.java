package d1.project.dcrun.center.webapi.system.entity;

import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author libin
 */
@Entity
@Table(name = "d1_tenants")
@ApiModel(value = "Tenants", description = "租户表")
public class Tenants extends BaseCreateAndUpdateEntity {
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "集成项目数量")
    private Integer integrationProjectAmount;
    @ApiModelProperty(value = "备注")
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getIntegrationProjectAmount() {
        return integrationProjectAmount;
    }

    public void setIntegrationProjectAmount(Integer integrationProjectAmount) {
        this.integrationProjectAmount = integrationProjectAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
