package d1.project.d1project.system.model.vm;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.project.d1project.system.model.RoleBase;
import org.hibernate.validator.constraints.Length;

/**
 * 更新角色
 *
 * @author kikki
 * @date 2020-09-09 10:01
 */
@ApiModel(value = "RoleUpdateVm", description = "更新角色")
public class RoleUpdateVm extends RoleBase {

    @ApiModelProperty("id")
    @Length(message = "id长度超过100字符", min = 0, max = 2000)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
