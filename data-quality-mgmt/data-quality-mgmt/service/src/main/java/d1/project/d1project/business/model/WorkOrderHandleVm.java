package d1.project.d1project.business.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(value = "WorkOrderHandleVm", description = "处理工单")
public class WorkOrderHandleVm {

    @ApiModelProperty("id")
    @NotBlank(message = "规则模板id不可为空")
    private String id;

    @ApiModelProperty("状态：0处理中、1已通过、2已驳回")
    @NotNull(message = "工单状态不可为空")
    @Range(min = 0, max = 2, message = "工单状态")
    private int status;

    @ApiModelProperty("处理意见")
    private String handlerOpinion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getHandlerOpinion() {
        return handlerOpinion;
    }

    public void setHandlerOpinion(String handlerOpinion) {
        this.handlerOpinion = handlerOpinion;
    }
}
