package d1.project.d1project.system.model.vm;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import java.util.List;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-09 14:23
 */
@ApiModel(value = "OrganizationUserInsertAllVm", description = "组织机构_组织机构用户新增")
public class OrganizationUserInsertAllVm {

    @ApiModelProperty("用户id")
    private List<String> userIds;

    @ApiModelProperty("组织机构id")
    private String organizationId;

    @ApiModelProperty("组织机构名称")
    private String organizationName;

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}
