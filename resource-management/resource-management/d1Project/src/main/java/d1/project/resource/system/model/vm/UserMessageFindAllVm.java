package d1.project.resource.system.model.vm;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.project.resource.common.model.TimePageableVm;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-15 11:09
 */
@ApiModel(value = "UserMessageFindAllVm", description = "信息用户查询")
public class UserMessageFindAllVm extends TimePageableVm {

    @ApiModelProperty("标题")
    private String messageTitle;

    @ApiModelProperty("模块:系统模块")
    private String messageModule;

    @ApiModelProperty("状态0未读，1已读")
    private String status;

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageModule() {
        return messageModule;
    }

    public void setMessageModule(String messageModule) {
        this.messageModule = messageModule;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

