package d1.project.dataintegration.system.model;

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
    private List<String> roles;

    public MySignInResult(WebAdminUserVm webAdminUserVm, String token) {
        super(webAdminUserVm.getId(), webAdminUserVm.getName(), token);
        setRoles(Arrays.asList(webAdminUserVm.getRoleId().split(",")));

    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
