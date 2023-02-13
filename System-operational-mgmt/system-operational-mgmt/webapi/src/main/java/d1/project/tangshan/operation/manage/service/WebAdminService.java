package d1.project.tangshan.operation.manage.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.authz.configuration.AuthzConfig;
import d1.framework.authz.service.AuthzMyInfoService;
import d1.framework.permission.model.UserRolePostVm;
import d1.framework.permission.service.UserRoleService;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.model.SignInResult;
import d1.framework.webapi.utils.SignInRetryLimitService;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.tangshan.operation.manage.Constants;
import d1.project.tangshan.operation.manage.dao.WebAdminUserDao;
import d1.project.tangshan.operation.manage.entity.ModuleMonitorEntity;
import d1.project.tangshan.operation.manage.entity.WebAdminUser;
import d1.project.tangshan.operation.manage.service.operations.log.AuditLogService;
import d1.project.tangshan.operation.manage.service.operations.log.OperationLogService;
import d1.project.tangshan.operation.manage.utils.AlarmWay;
import d1.project.tangshan.operation.manage.utils.MyUtils;
import net.dcrun.component.email.IEmailService;
import net.dcrun.component.sms.ucpass.ISmsUcpassService;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 针对webadmin的登录等操作
 *
 * @author Buter
 * @date 2020/3/16 6:50
 */
@Service
public class WebAdminService {
    private final WebAdminUserDao webAdminUserDao;
    private final TokenService tokenService;
    private final SignInRetryLimitService signInRetryLimitService;
    private final OperationLogService operationLogService;
    private final AuditLogService auditLogService;
    private final UserRoleService userRoleService;
    private final ModuleMonitorService moduleMonitorService;
    private final ISmsUcpassService smsUcpassService;
    private final IEmailService emailService;
    private final SSOService ssoService;
    private final ReceiverService receiverService;
    @Autowired
    private AuthzConfig authzConfig;
    @Autowired
    private AuthzMyInfoService authzMyInfoService;
    private String authType = "webadmin";

    private final Logger logger = LoggerFactory.getLogger(WebAdminService.class);

    @Value("${authentication.auth-server-url}")
    private String ssoUrl;
    @Value("${sso.weadmin.realm}")
    private String realm;
    @Value("${authentication.admin.resource}")
    private String client_id;


    @Value("${email.host}")
    private String host;
    @Value("${email.from}")
    private String from;
    @Value("${email.account}")
    private String account;
    @Value("${email.password}")
    private String password;
    @Value("${email.ssl}")
    private String ssl;
    @Value("${email.port}")
    private String port;

    @Autowired
    public WebAdminService(WebAdminUserDao webAdminUserDao, TokenService tokenService, SignInRetryLimitService signInRetryLimitService, OperationLogService operationLogService, AuditLogService auditLogService, UserRoleService userRoleService, ModuleMonitorService moduleMonitorService, ISmsUcpassService smsUcpassService, IEmailService emailService, SSOService ssoService, ReceiverService receiverService) {
        this.webAdminUserDao = webAdminUserDao;
        this.tokenService = tokenService;
        this.signInRetryLimitService = signInRetryLimitService;
        this.operationLogService = operationLogService;
        this.auditLogService = auditLogService;
        this.userRoleService = userRoleService;
        this.moduleMonitorService = moduleMonitorService;
        this.smsUcpassService = smsUcpassService;
        this.emailService = emailService;
        this.ssoService = ssoService;
        this.receiverService = receiverService;
    }


    public SignInResult signIn(String userName, String password, HttpServletRequest request) throws Exception {
        try {
            if (!authzConfig.isEnabled()) {
                if (StringUtils.isEmpty(userName)) {
                    throw new ValidException("用户名不可为空");
                }
                if (StringUtils.isEmpty(password)) {
                    throw new ValidException("密码不可为空");
                }
                WebAdminUser user = signUser(userName, password, request);
                String token = tokenService.createToken(authType, (JSONObject) JSON.toJSON(user), Constants.TOKEN_EXPIRE_TIME);
                return new SignInResult(user.getId(), user.getName(), token);
            } else {
                return ssoLogin(request);
            }

        } catch (Exception e) {
            throw new ValidException(e.getMessage());
        }
    }

    public void updatePassword(HttpServletRequest request, String id, String oldPassword, String newPassword, String realPassword, String realOldPassword) throws
            Exception {
        try {
            if (StringUtils.isEmpty(id) || StringUtils.isEmpty(newPassword)) {
                throw new ValidException("新密码不能为空");
            }
            Optional<WebAdminUser> userOptional = webAdminUserDao.findById(id);
            if (!userOptional.isPresent()) {
                throw new ValidException("用户不存在:" + id);
            }
            WebAdminUser user = userOptional.get();
//            if (!verifyOldPassword(user.getName(), realOldPassword)) {
//                throw new ValidException("旧密码错误");
//            }
            //sso修改密码
            String userInfo = authzMyInfoService.getCurrentUser(request);
            JSONObject jsonObject = JSONObject.parseObject(userInfo);
            updateSSOUser(jsonObject.getString("sub"), realPassword, null);

            user.setPassword(newPassword);

            webAdminUserDao.save(user);
        } catch (Exception e) {
            throw new ValidException(e.getMessage());
        }

    }

    public Optional<WebAdminUser> findById(String id) {
        return webAdminUserDao.findById(id);
    }

    public void singOut(HttpServletRequest request) throws Exception {
        request.logout();
        tokenService.removeToken(request);
    }


    public void insert(JSONObject userRelative, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String userId = tokenService.getUserByToken(request).getString("id");
        WebAdminUser userRelative1 = JSON.parseObject(userRelative.toJSONString(), WebAdminUser.class);
        String name = userRelative1.getName();
        try {
            name = insertUser(userRelative1, userId);
            operationLogService.setLog("用户管理—添加", userName, "运维行为管理—用户管理", "添加了一个用户:" + name, "1", request);
            //auditLogService.setLog("创建用户", userName, "2", "1", request);
        } catch (Exception e) {
            operationLogService.setLog("用户管理—添加", userName, "运维行为管理—用户管理", "添加了一个用户:" + name, "0", request);
            //auditLogService.setLog("创建用户", userName, "2", "0", request);
            throw new Exception(e.getMessage());
        }

    }

    public void delete(String id, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String name = "未知";
        try {
            name = deleteUser(id);

            operationLogService.setLog("用户管理—删除", userName, "运维行为管理—用户管理", "删除了一个用户:" + name, "1", request);
            //auditLogService.setLog("删除用户", userName, "3", "1", request);
        } catch (Exception e) {
            operationLogService.setLog("用户管理—删除", userName, "运维行为管理—用户管理", "删除了一个用户:" + name, "0", request);
            //auditLogService.setLog("删除用户", userName, "3", "0", request);
            throw new ValidException(e.getMessage());
        }
    }


    public void update(HttpServletRequest request, JSONObject model) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String name = "未知";
        try {
            WebAdminUser webAdminUser = MyUtils.model2Entity(model, WebAdminUser.class);
            WebAdminUser oldWebAdminUser = webAdminUserDao.findById(webAdminUser.getId()).orElseThrow(() -> new Exception("该用户不存在"));
            name = oldWebAdminUser.getName();
            updateUser(request, model);
            operationLogService.setLog("用户管理—编辑", userName, "运维行为管理—用户管理", "编辑了一个用户:" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("用户管理—编辑", userName, "运维行为管理—用户管理", "编辑了一个用户:" + name, "0", request);
            throw new ValidException(e.getMessage());
        }

    }

    public Page<WebAdminUser> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<WebAdminUser> builder = new SpecificationBuilder<>();
        Specification<WebAdminUser> specification = builder.init(params)
                .sContain("name", "name")
                .sContain("tel", "tel")
                .sContain("organization", "organization")
                .sEqual("roleId", "roleId")
                .sEqual("status", "status")
                .dOrder("createTime")
                .build();
        return webAdminUserDao.findAll(specification, builder.getPageable());
    }

    public void updateStatus(JSONObject model, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        WebAdminUser webAdminUser = MyUtils.model2Entity(model, WebAdminUser.class);
        String status = "1".equals(webAdminUser.getStatus()) ? "启用" : "禁用";
        String name = "未知";
        try {
            WebAdminUser oldWebAdminUser = webAdminUserDao.findById(webAdminUser.getId()).orElseThrow(() -> new Exception("该用户不存在"));
            MyUtils.throwMsg("admin".equals(oldWebAdminUser.getId()), "admin不可禁用");
            name = oldWebAdminUser.getName();
            if ("0".equals(webAdminUser.getStatus())) {
                setEnabled(oldWebAdminUser.getId(), false);
                oldWebAdminUser.setStatus("0");
            } else {
                if (checkEnable(oldWebAdminUser.getName())) {
                    setEnabled(oldWebAdminUser.getId(), true);
                    oldWebAdminUser.setStatus("1");
                } else {
                    throw new ValidException("用户登录次数以达上限,不可启用");
                }
            }
            webAdminUserDao.save(oldWebAdminUser);

            operationLogService.setLog("用户管理—" + status, userName, "运维行为管理-用户管理", status + "了" + name + "用户", "1", request);
        } catch (Exception e) {
            operationLogService.setLog("用户管理—" + status, userName, "运维行为管理-用户管理", status + "了" + name + "用户", "0", request);
            throw new ValidException(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    String insertUser(WebAdminUser userRelative1, String userId) throws Exception {
        //对接单点登录系统创建用户
        WebAdminUser adminUser1 = MyUtils.initInsert(userRelative1, userId);
        if(!StringUtils.isEmpty(userRelative1.getTel())){
            MyUtils.throwMsg(webAdminUserDao.existsByTel(adminUser1.getTel()), "手机号已存在");
        }
        MyUtils.throwMsg(webAdminUserDao.existsByName(adminUser1.getName()), "用户名称已存在");
        String id = insertSSOUser(userRelative1.getName());
        adminUser1.setId(id);
        adminUser1.setIsWarn("0");
        adminUser1.setIsEmile("0");
        adminUser1.setIsSms("0");
        adminUser1.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8)));
        adminUser1.setStatus("1");
        webAdminUserDao.save(adminUser1);

        UserRolePostVm userRolePostVm = new UserRolePostVm();
        List<String> roleIds = Collections.singletonList(adminUser1.getRoleId());
        userRolePostVm.setUserId(adminUser1.getId());
        userRolePostVm.setRoleIds(roleIds);
        userRoleService.insert(userRolePostVm);
        return adminUser1.getName();
    }

    @Transactional(rollbackFor = Exception.class)
    String deleteUser(String id) throws Exception {
        MyUtils.throwMsg(StringUtils.isEmpty(id), "id不能为空");
        MyUtils.throwMsg("admin".equals(id), "admin用户不可删除");

        WebAdminUser webAdminUser = webAdminUserDao.findById(id).orElseThrow(() -> new Exception("该用户不存在"));
        //对接sso删除用户
        try {
            deleteSSOUser(webAdminUser.getId());
        } catch (Exception e) {

        }

        webAdminUserDao.deleteById(id);
        UserRolePostVm userRolePostVm = new UserRolePostVm();
        userRolePostVm.setUserId(id);
        userRoleService.insert(userRolePostVm);

        return webAdminUser.getName();
    }

    @Transactional(rollbackFor = Exception.class)
    void updateUser(HttpServletRequest request, JSONObject model) throws Exception {
        WebAdminUser webAdminUser = MyUtils.model2Entity(model, WebAdminUser.class);

        WebAdminUser oldWebAdminUser = webAdminUserDao.findById(webAdminUser.getId()).orElseThrow(() -> new Exception("该用户不存在"));
        tokenService.updateUpdateIdAndTime(request, oldWebAdminUser);
        if (!StringUtils.isEmpty(model.getString("tel"))) {
            if (!oldWebAdminUser.getTel().equals(webAdminUser.getTel())) {
                MyUtils.throwMsg(webAdminUserDao.existsByTel(webAdminUser.getTel()), "手机号已存在");
                oldWebAdminUser.setTel(model.getString("tel"));
            } else {
                oldWebAdminUser.setTel(webAdminUser.getTel());
            }
        } else {
            oldWebAdminUser.setTel("");
        }
        oldWebAdminUser.setRoleId(webAdminUser.getRoleId());
        oldWebAdminUser.setRoleName(webAdminUser.getRoleName());
        oldWebAdminUser.setOrganization(webAdminUser.getOrganization());
        oldWebAdminUser.setRemark(webAdminUser.getRemark());
        webAdminUserDao.save(oldWebAdminUser);

        UserRolePostVm userRolePostVm = new UserRolePostVm();
        List<String> roleIds = Collections.singletonList(oldWebAdminUser.getRoleId());
        userRolePostVm.setUserId(oldWebAdminUser.getId());
        userRolePostVm.setRoleIds(roleIds);
        userRoleService.insert(userRolePostVm);
    }

    public void resetPassword(HttpServletRequest request, String id) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String name = "未知";
        try {
            WebAdminUser oldWebAdminUser = webAdminUserDao.findById(id).orElseThrow(() -> new Exception("该用户不存在"));
            if ("admin".equals(oldWebAdminUser.getId())) {
                throw new ValidException("admin用户不可重置密码");
            }
            resetSSOUser(oldWebAdminUser.getId());
            tokenService.updateUpdateIdAndTime(request, oldWebAdminUser);
            name = oldWebAdminUser.getName();
            oldWebAdminUser.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8)));
            webAdminUserDao.save(oldWebAdminUser);

            operationLogService.setLog("用户管理—重置密码", userName, "运维行为管理—用户管理", "重置了用户:" + name + "密码", "1", request);
        } catch (Exception e) {
            operationLogService.setLog("用户管理—重置密码", userName, "运维行为管理—用户管理", "重置了用户:" + name + "密码", "0", request);
            throw new Exception(e.getMessage());
        }

    }

    public void resetUserInfo() {
        try {
            List<WebAdminUser> allUser = webAdminUserDao.findAllByStatus("0");
            for (WebAdminUser adminUser : allUser) {
                adminUser.setStatus("1");
            }
            webAdminUserDao.saveAll(allUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            List<UserRepresentation> userList = ssoService.findAll();
            for (UserRepresentation user : userList) {
                if (!user.isEnabled()) {
                    setEnabled(user.getId(), true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetWebUserInfo() {
        try {
            List<WebAdminUser> allUser = webAdminUserDao.findAll();
            for (WebAdminUser adminUser : allUser) {
                adminUser.setIsSms("0");
                adminUser.setIsEmile("0");
                adminUser.setIsWarn("0");
            }
            webAdminUserDao.saveAll(allUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    WebAdminUser signUser(String userName, String password, HttpServletRequest request) throws Exception {
        WebAdminUser user = webAdminUserDao.findByName(userName);
        if (user == null) {
            throw new ValidException("用户不存在:" + userName);
        }
        if ("0".equals(user.getStatus())) {
            throw new ValidException("用户已禁用");
        }

        signInRetryLimitService.signInSuccess(userName);

        user.setLastSignInTime(Calendar.getInstance());

        webAdminUserDao.save(user);

        return user;
    }

    public void checkLogin() {
        try {
            List<UserRepresentation> userList = ssoService.findAll();
            logger.error("用户列表   " + JSONObject.toJSONString(userList));
            for (UserRepresentation user : userList) {
                performance(user.getUsername(), user.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void performance(String userName, String userId) throws Exception {
        if (!"admin".equals(userName)) {
            WebAdminUser webAdminUser = webAdminUserDao.findByName(userName);
            JSONObject params = new JSONObject();
            params.put("operator", userName);
            params.put("level", "1");
            params.put("result", "1");
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DATE);
            String today = year + "-" + month + "-" + day;
            params.put("startTime", today + " 00:00:00");
            params.put("endTime", today + " 23:59:59");
            int loginNumber = auditLogService.countAllByOperatorAndLevelAndCreateTimeBetween(params);
            List<ModuleMonitorEntity> list = moduleMonitorService.findAllByType("performance");
            for (ModuleMonitorEntity entity : list) {
                if (loginNumber + 1 > entity.getAccess()) {
                    if ("account".equals(entity.getAlarmWay())) {
                        updateSSOUserStatus(userId, null, false);
                        if (webAdminUser != null) {
                            webAdminUser.setStatus("0");
                            webAdminUserDao.save(webAdminUser);
                            if ("1".equals(webAdminUser.getIsWarn())) {
                                continue;
                            }
                        }
                        if (entity.getPeopleNotified().contains("@")) {
                            String msg = userName + "用户已锁定，请及时关注。";
                            JSONObject json = new JSONObject();
                            json.put("host", this.host);
                            json.put("from", this.from);
                            json.put("to", entity.getPeopleNotified().split(","));
                            json.put("account", this.account);
                            json.put("password", this.password);
                            json.put("ssl", this.ssl);
                            json.put("port", this.port);

                            JSONObject content = new JSONObject();
                            content.put("subject", "访问锁定通知");
                            content.put("content", msg);

                            json.put("content", content);
                            try {
                                receiverService.sendEmail(entity.getPeopleNotified().split(","), "访问锁定通知", msg);
                                //emailService.send(json.toJSONString());
                                if (webAdminUser != null) {
                                    webAdminUser.setIsWarn("1");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                for (String mobile : entity.getPeopleNotified().split(",")) {
                                    receiverService.sendSms(mobile, "553061", userName);
                                    //smsUcpassService.send(userName, mobile, "553061");
                                }
                                if (webAdminUser != null) {
                                    webAdminUser.setIsWarn("1");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        if (AlarmWay.SMS.getName().equals(entity.getAlarmWay())) {
                            if (webAdminUser != null) {
                                if ("1".equals(webAdminUser.getIsSms())) {
                                    continue;
                                }
                            }

                            try {
                                for (String mobile : entity.getPeopleNotified().split(",")) {
                                    receiverService.sendSms(mobile, "553061", userName);
                                    //smsUcpassService.send(userName, mobile, "553061");
                                }
                                if (webAdminUser == null) {
                                    continue;
                                }
                                webAdminUser.setIsSms("1");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (AlarmWay.EMAIL.getName().equals(entity.getAlarmWay())) {
                            if (webAdminUser != null) {
                                if ("1".equals(webAdminUser.getIsEmile())) {
                                    continue;
                                }
                            }
                            String msg = userName + "用户访问次数已达到上限，请及时关注。";
                            JSONObject json = new JSONObject();
                            json.put("host", this.host);
                            json.put("from", this.from);
                            json.put("to", entity.getPeopleNotified().split(","));
                            json.put("account", this.account);
                            json.put("password", this.password);
                            json.put("ssl", this.ssl);
                            json.put("port", this.port);

                            JSONObject content = new JSONObject();
                            content.put("subject", "访问次数上限通知");
                            content.put("content", msg);

                            json.put("content", content);
                            try {
                                receiverService.sendEmail(entity.getPeopleNotified().split(","), "访问次数上限通知", msg);
                                //emailService.send(json.toJSONString());
                                if (webAdminUser != null) {
                                    webAdminUser.setIsEmile("1");
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            if (webAdminUser != null) {
                webAdminUserDao.save(webAdminUser);
            }
        }
    }

    public SignInResult ssoSignIn(String ssoToken, HttpServletRequest request) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String rootUrl = ssoUrl + "/auth/realms/" + realm + "/protocol/openid-connect/userinfo";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Authorization", "bearer " + ssoToken);
        ResponseEntity<JSONObject> response = restTemplate.exchange(rootUrl, HttpMethod.GET, new HttpEntity<String>(headers), JSONObject.class);
        if (response.getStatusCodeValue() == 200) {
            String userName = response.getBody().getString("preferred_username");
            WebAdminUser user = webAdminUserDao.findByName(userName);
            if (user == null) {
                WebAdminUser newUser = new WebAdminUser();
                newUser.setStatus("1");
                newUser.setIsWarn("0");
                newUser.setIsEmile("0");
                newUser.setIsSms("0");
                newUser.setId(response.getBody().getString("sub"));
                newUser.setName(userName);
                newUser.setCreateById("admin");
                newUser.setCreateTime(Calendar.getInstance());
                webAdminUserDao.save(newUser);
                return signIn(userName, null, request);
            }
            return signIn(userName, user.getPassword(), request);
        }

        throw new ValidException("登陆失败");
    }

    public String insertSSOUser(String userName) throws Exception {
        return ssoService.createUser(userName);
    }

    public void syncSsoUser() {
        try {
            List<UserRepresentation> userList = ssoService.findAll();

            List<WebAdminUser> users = new ArrayList<>();
            for (UserRepresentation user : userList) {
                WebAdminUser user1 = webAdminUserDao.findByName(user.getUsername());
                if (user1 == null) {
                    WebAdminUser newUser = new WebAdminUser();
                    if (user.isEnabled()) {
                        newUser.setStatus("1");
                    } else {
                        newUser.setStatus("0");
                    }
                    newUser.setIsWarn("0");
                    newUser.setIsEmile("0");
                    newUser.setIsSms("0");
                    newUser.setId(user.getId());
                    newUser.setName(user.getUsername());
                    newUser.setCreateById("admin");
                    newUser.setCreateTime(Calendar.getInstance());
                    users.add(newUser);
                } else {
                    if (user.isEnabled()) {
                        user1.setStatus("1");
                    } else {
                        user1.setStatus("0");
                    }
                    users.add(user1);
                }
            }
            webAdminUserDao.saveAll(users);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateSSOUser(String userId, String password, Boolean enable) throws Exception {
        try {
            ssoService.updateUser(userId, password, enable);
        } catch (Exception e) {
            throw new ValidException("修改密码失败");
        }
    }

    private void resetSSOUser(String userId) throws Exception {
        try {
            ssoService.resetSSOUser(userId);
        } catch (Exception e) {
            throw new ValidException("重置密码失败");
        }
    }

    private void updateSSOUserStatus(String userId, String password, Boolean enable) throws Exception {
        try {
            ssoService.updateUser(userId, password, enable);
        } catch (Exception e) {
            throw new ValidException("修改用户状态");
        }
    }

    private void deleteSSOUser(String userId) throws Exception {
        ssoService.deleteUser(userId);
    }

    private Boolean verifyOldPassword(String userName, String password) throws Exception {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String rootUrl = ssoUrl + "/realms/" + realm + "/protocol/openid-connect/token";

            MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
            postParameters.add("client_id", client_id);
            postParameters.add("grant_type", "password");
            postParameters.add("username", userName);
            postParameters.add("password", password);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/x-www-form-urlencoded");
            headers.add("Accept", "application/json");
            HttpEntity<MultiValueMap<String, Object>> r = new HttpEntity<>(postParameters, headers);
            restTemplate.postForObject(rootUrl, r, JSONObject.class);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            if ("400 Bad Request".equals(e.getMessage())) {
                throw new ValidException("账号已锁定不可修改密码");
            }
            return false;
        }
    }

    private Boolean checkEnable(String userName) throws Exception {
        if (!"admin".equals(userName)) {
            JSONObject params = new JSONObject();
            params.put("operator", userName);
            params.put("level", "1");
            params.put("result", "1");
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DATE);
            String today = year + "-" + month + "-" + day;
            params.put("startTime", today + " 00:00:00");
            params.put("endTime", today + " 23:59:59");
            int loginNumber = auditLogService.countAllByOperatorAndLevelAndCreateTimeBetween(params);
            List<ModuleMonitorEntity> list = moduleMonitorService.findAllByType("performance");
            for (ModuleMonitorEntity entity : list) {
                if (loginNumber + 1 > entity.getAccess()) {
                    if (AlarmWay.ACCOUNT.getName().equals(entity.getAlarmWay())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void setEnabled(String id, Boolean enabled) throws Exception {
        ssoService.setEnabled(id, enabled);
    }

    /**
     * 登录
     */
    protected SignInResult ssoLogin(HttpServletRequest request) throws Exception {
        String userInfo = "";
        try {
            userInfo = authzMyInfoService.getCurrentUser(request);
        } catch (Exception e) {
            throw new ValidException("获取数据异常，" + e.getMessage());
        }

        JSONObject jsonObject = JSONObject.parseObject(userInfo);

        String name = jsonObject.getString("preferred_username");
        WebAdminUser user = webAdminUserDao.findByName(name);
        if (user == null) {
            user = new WebAdminUser();
            user.setId(jsonObject.getString("sub"));
            user.setName(jsonObject.getString("preferred_username"));
            user.setStatus("1");
            user.setIsWarn("0");
            user.setIsEmile("0");
            user.setIsSms("0");
            user.setCreateById("admin");
            user.setCreateTime(Calendar.getInstance());
            webAdminUserDao.save(user);
        }

        /*登录成功*/
        String token = tokenService.createToken(authType, (JSONObject) JSON.toJSON(user), Constants.TOKEN_EXPIRE_TIME);

        return new SignInResult(user.getId(), user.getName(), token);
    }

    /**
     * 获取当前登录用户
     */
    public JSONObject getLoginUser(HttpServletRequest request) throws Exception {
        return JSONObject.parseObject(authzMyInfoService.getCurrentUser(request));
    }

    /**
     * 清空SsoCookie
     */
    public void clearSsoCookie(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Cookie cookie = new Cookie("JSESSIONID",null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}