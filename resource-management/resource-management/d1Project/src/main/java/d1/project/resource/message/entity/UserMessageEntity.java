package d1.project.resource.message.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateEntity;
import d1.project.resource.common.model.UserMessage;
import d1.project.resource.common.utils.BaseUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Calendar;

/**
 * @author kiki
 */
@Entity
@Table(name = "d1_message_user")
@ApiModel(value = "UserMessage", description = "用户消息")
public class UserMessageEntity extends BaseCreateEntity {

    @ApiModelProperty("用户id")
    @Column(length = 32)
    private String userId;

    @ApiModelProperty("状态:0未读，1已读")
    @Column(length = 1)
    private int status;

    @ApiModelProperty("标题")
    @Column(length = 100)
    private String messageTitle;

    @ApiModelProperty("模块:系统模块")
    @Column(length = 100)
    private String messageModule;

    @ApiModelProperty("模块:系统模块Id")
    @Column(length = 32)
    private String messageModuleId;

    @ApiModelProperty("内容")
    @Column(columnDefinition = "TEXT")
    private String messageContent;


    public UserMessageEntity() {
    }

    public UserMessageEntity(UserMessage message, String userId, String senderId) {
        this.setId(BaseUtils.generate32Id());
        this.userId = userId;
        this.messageTitle = message.getTitle();
        this.messageModule = message.getModule();
        this.messageModuleId = message.getModuleId();
        /*0未读，1已读*/
        this.status = 0;
        if (senderId != null) {
            this.setCreateById(senderId);
        }
        this.setCreateTime(Calendar.getInstance());
        this.messageContent = message.getContent();
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

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

    public String getMessageModuleId() {
        return messageModuleId;
    }

    public void setMessageModuleId(String messageModuleId) {
        this.messageModuleId = messageModuleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
