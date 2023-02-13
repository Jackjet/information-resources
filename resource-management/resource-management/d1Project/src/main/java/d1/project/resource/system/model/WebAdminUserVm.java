package d1.project.resource.system.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import java.util.Calendar;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-08 14:29
 */
@ApiModel(value = "WebAdminUserVm", description = "用户信息")
public class WebAdminUserVm extends WebAdminUserBase {

    private String id;

    @ApiModelProperty("启用")
    private boolean enable;

    @ApiModelProperty("组织名称")
    private String organizationName;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("创建时间")
    private Calendar createTime;

    @ApiModelProperty("账户")
    private String account;

    public WebAdminUserVm() {
    }

    public WebAdminUserVm(String id, String organizationName, String roleName, String account, String phone) {
        this.id = id;
        this.organizationName = organizationName;
        this.roleName = roleName;
    }


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Calendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }
}
