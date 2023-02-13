package d1.project.resourcesharingmgmt.resource.model.AssetApiEx;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author zhengyang
 */
@ApiModel(value = "AssetExExamineVm", description = "资源挂接审批")
public class AssetExExamineVm {
    /**
     * id
     */
    @ApiModelProperty("ID")
    @NotBlank(message = "ID不可为空")
    private String id;

    /**
     * 审批描述
     */
    @ApiModelProperty("描述")
    private String detail;

    /**
     * 审核状态，0通过，1驳回
     */
    @ApiModelProperty("审核状态，0通过，1驳回")
    private int status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
