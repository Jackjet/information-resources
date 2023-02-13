package d1.project.api.integration.system.model.vm;

import d1.framework.webapi.annotation.ApiModelProperty;
import d1.project.api.integration.system.model.RoleBase;
import org.hibernate.validator.constraints.Length;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-09 10:01
 */
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
