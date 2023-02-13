package d1.project.dataintegration.system.model.vm;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-09 10:01
 */
@ApiModel(value = "OrganizationUpdateVm", description = "管理端_组织机构")
public class OrganizationUpdateVm {

    @ApiModelProperty("组织机构名称")
    @Length(message = "组织机构名称长度超过100字符", min = 1, max = 100)
    @NotBlank(message = "组织机构名称不可为空")
    private String name;

    @ApiModelProperty("id")
    @NotBlank(message = "id不可为空")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
