package d1.project.d1project.system.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.authz.service.AuthzUserService;
import d1.framework.webapi.configuration.DoValidException;
import d1.project.d1project.system.entity.WebAdminUserEntity;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author lin
 */
@Service
public class SSOService {
    @Autowired
    private AuthzUserService authzUserService;

    /**
     * 用户列表
     *
     * @param search    匹配用户名、姓、名或者邮箱地址中的字符串
     * @param username  登录名
     * @param email     邮箱
     * @param firstName 姓名
     * @param first     第几页 从0开始
     * @param max       每页条数
     * @return
     * @throws Exception
     */
    public List<UserRepresentation> findAll() throws DoValidException {
        String user = null;
        try {
            user = authzUserService.findAll("", "", "", "", 0, 1000);
            return JSONArray.parseArray(user, UserRepresentation.class);
        } catch (Exception e) {
            throw new DoValidException(e.getMessage());
        }

    }


    /**
     * 为sso创建用户
     */
    /**
     * 添加用户
     *
     * @param username  登录名，建议使用英文
     * @param firstName 姓名
     * @param pwd       密码
     * @return
     * @throws Exception
     */
    public WebAdminUserEntity createUser(WebAdminUserEntity webAdminUserEntity) throws DoValidException {
        // 创建用户
        String ssoId = null;
        try {
            ssoId = authzUserService.post(webAdminUserEntity.getAccount(), webAdminUserEntity.getName(), "123456");
            webAdminUserEntity.setSsoId(ssoId);
            return webAdminUserEntity;
        } catch (Exception e) {
            throw new DoValidException(e.getMessage());
        }

    }

    /**
     * 编辑用户
     *
     * @param id        用户id
     * @param username  登录名，建议使用英文
     * @param firstName 姓名
     * @param pwd       密码
     * @throws Exception
     */
    public void updateUser(WebAdminUserEntity webAdminUserEntity) throws DoValidException {
        try {
            authzUserService.put(webAdminUserEntity.getSsoId(), "", webAdminUserEntity.getName(), "");
        } catch (Exception e) {
            throw new DoValidException(e.getMessage());
        }
    }

    public void updatePassword(WebAdminUserEntity webAdminUserEntity, String password) throws DoValidException {
        if (!StringUtils.isEmpty(webAdminUserEntity.getPassword())) {
            try {
                authzUserService.put(webAdminUserEntity.getSsoId(), "", webAdminUserEntity.getName(), password);
            } catch (Exception e) {
                throw new DoValidException(e.getMessage());
            }
        }
    }

    public void updateEnable(WebAdminUserEntity webAdminUserEntity) throws DoValidException {
        UserResource userResource = findUserById(webAdminUserEntity.getSsoId());
        UserRepresentation userRepresentation = userResource.toRepresentation();
        if (!StringUtils.isEmpty(webAdminUserEntity.isEnable())) {
            userRepresentation.setEnabled(webAdminUserEntity.isEnable());
        }
        userResource.update(userRepresentation);
    }

    public UserResource findUserById(String userId) throws DoValidException {
        String user = null;
        try {
            user = authzUserService.findById(userId);
            return JSONObject.parseObject(user, UserResource.class);
        } catch (Exception e) {
            throw new DoValidException(e.getMessage());
        }

    }


    public void deleteUser(String userId) throws DoValidException {
        try {
            authzUserService.delete(userId);
        } catch (Exception e) {
            throw new DoValidException(e.getMessage());
        }
    }


    public Boolean existsFindByName(String account) throws Exception {
        String user = null;

        user = authzUserService.findAll("", account, "", "", 0, 1000);
        List<UserRepresentation> getUser = JSONArray.parseArray(user, UserRepresentation.class);
        return getUser.size() > 0;

    }

//    public ResponseEntity<SSOUserToken> ssoSignIn(String ssoToken) throws DoValidException {
//        RestTemplate restTemplate = new RestTemplate();
//        String rootUrl = ssoConfig.getUrl() + "/auth/realms/" + ssoConfig.getRealm() + "/protocol/openid-connect/userinfo";
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Accept", "application/json");
//        headers.add("Authorization", "bearer " + ssoToken);
//
//        return restTemplate.exchange(rootUrl, HttpMethod.GET, new HttpEntity<String>(headers), SSOUserToken.class);
//    }

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
