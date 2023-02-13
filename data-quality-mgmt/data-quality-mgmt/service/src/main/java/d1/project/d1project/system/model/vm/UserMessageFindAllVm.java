package d1.project.d1project.system.model.vm;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.project.d1project.common.model.TimePageableVm;

/**
 * 查询信息用户
 *
 * @author kikki
 * @date 2020-09-15 11:09
 */
@ApiModel(value = "UserMessageFindAllVm", description = "查询信息用户")
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

