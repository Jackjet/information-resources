package d1.project.api.integration.system.model.vm;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-09 14:23
 */
@ApiModel(value = "OrganizationUserInsertVm", description = "组织机构_组织机构用户新增")
public class OrganizationUserInsertVm {

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("用户id")
    private String userName;

    @ApiModelProperty("组织机构id")
    private String organizationId;

    @ApiModelProperty("组织机构名称")
    private String organizationName;

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
