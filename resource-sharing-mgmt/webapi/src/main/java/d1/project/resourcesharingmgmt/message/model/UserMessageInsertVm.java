package d1.project.resourcesharingmgmt.message.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.project.resourcesharingmgmt.common.model.UserMessage;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-24 16:03
 */
@ApiModel(value = "UserMessageInsertVm", description = "用户信息新增")
public class UserMessageInsertVm {
    @NotNull(message = "消息不能为空")
    private UserMessage message;

    @Size(min = 1, message = "用户id列表不能为空")
    private Collection<String> userIds;

    public UserMessage getMessage() {
        return message;
    }

    public void setMessage(UserMessage message) {
        this.message = message;
    }

    public Collection<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(Collection<String> userIds) {
        this.userIds = userIds;
    }
}
