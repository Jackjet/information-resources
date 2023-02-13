package d1.project.tangshan.operation.manage.entity.remote;

import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @author lin
 */
@Entity
@Table(name = "d1_command_type")
@ApiModel(value = "CommandType", description = "指令分类")
public class CommandType extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("被授权的角色id(以,分隔)")
    @Lob
    @Column(columnDefinition = "TEXT")
    private String roleIds;

    @ApiModelProperty("被授权的角色名字(以,分隔)")
    @Lob
    @Column(columnDefinition = "TEXT")
    private String roleNames;

    @ApiModelProperty("备注")
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    public String getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
