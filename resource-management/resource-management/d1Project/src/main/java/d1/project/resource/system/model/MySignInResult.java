package d1.project.resource.system.model;

import d1.framework.webapi.model.SignInResult;

import java.util.Arrays;
import java.util.List;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-15 21:56
 */
public class MySignInResult extends SignInResult {
    private String account;
    private List<String> roles;

    public MySignInResult(WebAdminUserVm webAdminUserVm, String token) {
        super(webAdminUserVm.getId(), webAdminUserVm.getName(), token);
        setAccount(webAdminUserVm.getAccount());
        setRoles(Arrays.asList(webAdminUserVm.getRoleId().split(",")));

    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
