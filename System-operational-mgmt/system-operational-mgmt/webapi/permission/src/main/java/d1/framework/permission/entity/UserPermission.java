package d1.framework.permission.entity;

import d1.framework.webapi.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "d1_user_permission")
@ApiModel(value = "UserPermission", description = "用户权限表")
public class UserPermission extends BaseEntity {
    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "功能树Id")
    private String menuTreeId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMenuTreeId() {
        return menuTreeId;
    }

    public void setMenuTreeId(String menuTreeId) {
        this.menuTreeId = menuTreeId;
    }
}
