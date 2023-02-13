package d1.project.tangshan.operation.manage.service;

import com.alibaba.fastjson.JSONArray;
import d1.framework.authz.service.AuthzUserService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lin
 */
@Service
public class SSOService {
    @Autowired
    private AuthzUserService authzUserService;

    public List<UserRepresentation> findAll() throws Exception {
        String user = authzUserService.findAll("", "", "", "", 0, 1000);
        return JSONArray.parseArray(user, UserRepresentation.class);
    }


    public String createUser(String userName) throws Exception {
        return authzUserService.post(userName, userName, "123456");
    }

    /**
     * 编辑用户
     *
     * @param userId    用户id
     * @param username  登录名，建议使用英文
     * @param firstName 姓名
     * @param password  密码
     * @throws Exception
     */
    public void updateUser(String userId, String password, Boolean enabled) throws Exception {
        authzUserService.put(userId, "", "", password);

    }


    public void deleteUser(String userId) throws Exception {
        authzUserService.delete(userId);
    }

    public void resetSSOUser(String userId) throws Exception {
        authzUserService.resetPassword(userId, "123456");
    }
    /**
     * 禁用
     * @param id 用户Id
     * @param enabled true启用、false禁用
     * @throws Exception
     */
    public void setEnabled(String id, Boolean enabled) throws Exception {
        authzUserService.setEnabled(id, enabled);
    }


}
