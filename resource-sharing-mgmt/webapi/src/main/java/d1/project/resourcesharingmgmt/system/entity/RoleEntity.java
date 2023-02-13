package d1.project.resourcesharingmgmt.system.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-08 21:33
 */
@Entity
@Table(name = "d1_role")
@ApiModel(value = "RoleEntity", description = "角色")
public class RoleEntity extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("名称")
    @Column(length = 100)
    private String name;

    @ApiModelProperty("备注")
    @Column(length = 2000)
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
