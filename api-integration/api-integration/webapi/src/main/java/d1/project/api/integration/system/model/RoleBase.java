package d1.project.api.integration.system.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-08 14:29
 */
@ApiModel(value = "RoleBaseVm", description = "角色_基础")
public class RoleBase {

    @ApiModelProperty("角色名称")
    @Length(message = "角色名称长度超过100字符", min = 1, max = 100)
    @NotBlank(message = "角色名称不可为空")
    private String name;

    @ApiModelProperty("角色备注")
    @Length(message = "角色备注长度超过100字符", min = 0, max = 2000)
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
