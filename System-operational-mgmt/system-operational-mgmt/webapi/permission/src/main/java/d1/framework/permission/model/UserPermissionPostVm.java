package d1.framework.permission.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


@ApiModel(value = "UserPermissionPostVm", description = "添加用户权限的model")
public class UserPermissionPostVm {
    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "菜单id")
    private List<String> menuTreeIds;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getMenuTreeIds() {
        return menuTreeIds;
    }

    public void setMenuTreeIds(List<String> menuTreeIds) {
        this.menuTreeIds = menuTreeIds;
    }
}
