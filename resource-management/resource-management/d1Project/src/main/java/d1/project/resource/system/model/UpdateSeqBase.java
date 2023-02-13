package d1.project.resource.system.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-10 16:40
 */
@ApiModel(value = "UpdateSeqVm", description = "序列动作")
public class UpdateSeqBase {

    @ApiModelProperty("动作:0->上移->1下移")
    @NotNull(message = "动作不可为空")
    @Range(min = 0, max = 1, message = "动作错误")
    private int action;

    @ApiModelProperty("id")
    @NotBlank(message = "id不可为空")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

}
