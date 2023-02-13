package d1.project.resourcesharingmgmt.system.model.vm;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-16 09:41
 */
@ApiModel(value = "IdBaseVm", description = "id")
public class IdBaseVm {
    @ApiModelProperty("id")
    @NotBlank(message = "id不可为空")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
