package d1.project.dcrun.center.webapi.system.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.authz.configuration.AuthzConfig;
import d1.framework.authz.service.AuthzMyInfoService;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.TokenService;
import d1.project.dcrun.center.webapi.common.util.MyUtils;
import d1.project.dcrun.center.webapi.system.dao.IntegrationProjectManageDao;
import d1.project.dcrun.center.webapi.system.dao.TenantsDao;
import d1.project.dcrun.center.webapi.system.dao.WebAdminUserDao;
import d1.project.dcrun.center.webapi.system.entity.IntegrationProjectManage;
import d1.project.dcrun.center.webapi.system.entity.Tenants;
import d1.project.dcrun.center.webapi.system.entity.WebAdminUser;
import d1.project.dcrun.center.webapi.system.model.user.DoUser;
import d1.project.dcrun.center.webapi.system.model.user.SignInResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author libin
 */
@Service
public class UserService {
    @Autowired
    private WebAdminUserDao userDao;
    @Autowired
    private TenantsDao tenantsDao;
    @Autowired
    private IntegrationProjectManageDao integrationProjectManageDao;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthzConfig authzConfig;
    @Autowired
    private AuthzMyInfoService authzMyInfoService;

    /**
     * 管理员登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录信息
     * @throws Exception 登录异常
     */
    public SignInResult signIn(String username, String password, String type) throws Exception {
        SignInResult result = new SignInResult();
        WebAdminUser user = userDao.findByName(username);
        if (user == null) {
            throw new ValidException("用户：" + username + "不存在");
        }
        if (!user.getPassword().equals(password)) {
            throw new ValidException("密码不对");
        }

        DoUser doUser = new DoUser();
        doUser.setId(user.getId());
        doUser.setName(user.getName());
        doUser.setPassword(user.getPassword());

        //有效期30天
        String accessToken = tokenService.createToken(type, (JSONObject) JSON.toJSON(doUser), 30 * 24 * 60 * 60);
        result.setId(user.getId());
        result.setToken(accessToken);
        result.setName(user.getName());
        return result;
    }

    /**
     * 租户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录信息
     * @throws Exception 登录异常
     */
    public SignInResult signInTenantsUser(String username, String password, String type) throws Exception {
        SignInResult result = new SignInResult();
        Tenants user = tenantsDao.findFirstByName(username);
        if (user == null) {
            throw new ValidException("用户：" + username + "不存在");
        }
        if (!user.getPassword().equals(password)) {
            throw new ValidException("密码不对");
        }

        DoUser doUser = new DoUser();
        doUser.setId(user.getId());
        doUser.setName(user.getName());
        doUser.setPassword(user.getPassword());

        //有效期30天
        String accessToken = tokenService.createToken(type, (JSONObject) JSON.toJSON(doUser), 30 * 24 * 60 * 60);
        result.setId(user.getId());
        result.setToken(accessToken);
        result.setName(user.getName());
        return result;
    }

    /**
     * 登录
     *
     */
    public SignInResult ssoLogin(String type, HttpServletRequest request) throws Exception {
        SignInResult result = new SignInResult();
        String userInfo="";
        try {
            userInfo=authzMyInfoService.getCurrentUser(request);
        }
        catch (Exception e){
            throw new Exception("获取数据异常，"+e.getMessage());
        }

        JSONObject jsonObject = JSONObject.parseObject(userInfo);
        String name = jsonObject.getString("preferred_username");
        IntegrationProjectManage user = integrationProjectManageDao.findFirstByCode(name);
        if (user == null) {
            user.setId(MyUtils.generatePrimaryKeyId());
            user.setCode(name);
            integrationProjectManageDao.save(user);
        }

        DoUser doUser = new DoUser();
        doUser.setId(user.getId());
        doUser.setPassword(user.getPassword());
        doUser.setName(user.getCode());

        //有效期30天
        String accessToken = tokenService.createToken(type, (JSONObject) JSON.toJSON(doUser), 30 * 24 * 60 * 60);
        result.setId(user.getId());
        result.setToken(accessToken);
        result.setName(user.getCode());
        return result;
    }

    /**
     * 项目管理员登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录信息
     * @throws Exception 登录异常
     */
    public SignInResult signInIntegrationUser(String username, String password, String type) throws Exception {
        SignInResult result = new SignInResult();
        IntegrationProjectManage user = integrationProjectManageDao.findFirstByCode(username);
        if (user == null) {
            throw new ValidException("用户：" + username + "不存在");
        }
        if (!user.getPassword().equals(password)) {
            throw new ValidException("密码不对");
        }

        DoUser doUser = new DoUser();
        doUser.setId(user.getId());
        doUser.setPassword(user.getPassword());
        doUser.setName(user.getCode());

        //有效期30天
        String accessToken = tokenService.createToken(type, (JSONObject) JSON.toJSON(doUser), 30 * 24 * 60 * 60);
        result.setId(user.getId());
        result.setToken(accessToken);
        result.setName(user.getCode());
        return result;
    }

    /**
     * 修改管理员密码
     *
     * @param id       id
     * @param password 密码
     * @throws Exception 校验用户信息
     */
    public void webadminUpdate(String id, String password) throws Exception {
        WebAdminUser user = this.userDao.findById(id).orElseThrow(() -> new ValidException("用户不存在"));
        user.setPassword(password);
        this.userDao.save(user);
    }

    /**
     * 修改租户密码
     *
     * @param id       租户id
     * @param password 密码
     * @throws Exception 校验用户信息
     */
    public void tenantsUserUpdate(String id, String password) throws Exception {
        Tenants user = this.tenantsDao.findById(id).orElseThrow(() -> new ValidException("用户不存在"));
        user.setPassword(password);
        this.tenantsDao.save(user);
    }

    /**
     * 修改项目管理员密码
     *
     * @param id       项目id
     * @param password 密码
     * @throws Exception 校验用户信息
     */
    public void integrationUserUpdate(String id, String password) throws Exception {
        IntegrationProjectManage user = this.integrationProjectManageDao.findById(id).orElseThrow(() -> new ValidException("用户不存在"));
        user.setPassword(password);
        this.integrationProjectManageDao.save(user);
    }

    /**
     * 退出登录
     */
    public void singOut(HttpServletRequest request) throws Exception {
        tokenService.removeToken(request);
    }
}
