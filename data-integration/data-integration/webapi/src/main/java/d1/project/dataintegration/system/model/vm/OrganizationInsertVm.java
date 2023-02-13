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
@ApiModel(value = "OrganizationInsertVm", description = "管理端_组织机构新增")
public class OrganizationInsertVm {
    @ApiModelProperty("组织机构名称")
    @Length(message = "组织机构名称长度超过100字符", min = 1, max = 100)
    @NotBlank(message = "组织机构名称不可为空")
    private String name;

    @ApiModelProperty("上级id")
    @NotBlank(message = "上级id不可为空")
    private String parentId;

    @ApiModelProperty("上级名称")
    @Length(message = "上级名称长度超过100字符", min = 1, max = 100)
    @NotBlank(message = "上级名称不可为空")
    private String parentName;

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
