package d1.project.d1project.system.service;

import d1.framework.webapi.configuration.DoValidException;
import d1.project.d1project.system.configuration.SSOConfig;
import d1.project.d1project.system.entity.WebAdminUserEntity;
import d1.project.d1project.system.model.sso.SSOToken;
import d1.project.d1project.system.model.sso.SSOUserToken;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.core.Response;
import java.util.*;

/**
 * @author lin
 */
@Service
public class SSOService {
//    @Value("${sso.clientId}")
//    private String clientId;
//    @Value("${sso.realm}")
//    private String realm;
//    @Value("${sso.clientAdminSecret}")
//    private String clientSecret;
//    @Value("${sso.url}")
//    private String url;

    final
    SSOConfig ssoConfig;

    public SSOService(SSOConfig ssoConfig) {
        this.ssoConfig = ssoConfig;
    }

    private RealmResource init() {
        RealmResource realmResource;
        Keycloak keycloak = Keycloak.getInstance(ssoConfig.getUrl() + "/auth/", ssoConfig.getRealm(), ssoConfig.getClientAdminId(), getToken().getAccessToken());
        realmResource = keycloak.realm(ssoConfig.getRealm());
        return realmResource;
    }

    /**
     * 获取权限token
     */
    public SSOToken getToken() {
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        //  请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //  封装参数，千万不要替换为Map与HashMap，否则参数无法传递
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        //  也支持中文
        multiValueMap.add("client_id", ssoConfig.getClientAdminId());
        multiValueMap.add("grant_type", "client_credentials");
        multiValueMap.add("client_secret", ssoConfig.getClientAdminSecret());
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(multiValueMap, headers);
        //  执行HTTP请求
        ResponseEntity<SSOToken> response = client.postForEntity(ssoConfig.getUrl() + "/auth/realms/master/protocol/openid-connect/token", requestEntity, SSOToken.class);
        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            throw new RuntimeException("连接异常");
        }
        return response.getBody();
    }

    public List<UserRepresentation> findAll() throws Exception {
        try {
            return init().users().list();
        } catch (Exception e) {
            throw new DoValidException("查询失败");
        }
    }


    /**
     * 为sso创建用户
     */
    public WebAdminUserEntity createUser(WebAdminUserEntity webAdminUserEntity) throws DoValidException {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setCreatedTimestamp(webAdminUserEntity.getCreateTime().getTimeInMillis());
        userRepresentation.setFirstName(webAdminUserEntity.getName());
        userRepresentation.setEmail(webAdminUserEntity.getEmail());
        userRepresentation.setUsername(webAdminUserEntity.getAccount());
        userRepresentation.setEnabled(webAdminUserEntity.isEnable());
        userRepresentation.setEmailVerified(false);
        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("attributes", Collections.singletonList("zh-CN"));
        userRepresentation.setAttributes(attributes);
        List<CredentialRepresentation> credentials = getCredentialRepresentations();
        userRepresentation.setCredentials(credentials);
        // 创建用户
        Response result = init().users().create(userRepresentation);
        if (201 == result.getStatus()) {
            //用户Id只能在response head location 截取
            String userIdString = result.getHeaders().getFirst("location").toString();
            String[] userIdArray = userIdString.split("/");
            webAdminUserEntity.setSsoId(userIdArray[userIdArray.length - 1]);
            return webAdminUserEntity;
        } else if (409 == result.getStatus()) {
            throw new DoValidException("用户已存在");
        }
        throw new DoValidException("创建失败");
    }

    private List<CredentialRepresentation> getCredentialRepresentations() {
        List<CredentialRepresentation> credentials = new ArrayList<>();
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue("123456");
        credentialRepresentation.setTemporary(false);
        credentials.add(credentialRepresentation);
        return credentials;
    }

    public void updateUser(WebAdminUserEntity webAdminUserEntity) {
        UserResource userResource = findUserById(webAdminUserEntity.getSsoId());
        UserRepresentation userRepresentation = userResource.toRepresentation();
        userRepresentation.setFirstName(webAdminUserEntity.getName());
        userRepresentation.setEmail(webAdminUserEntity.getEmail());
        userResource.update(userRepresentation);
    }
    public void updatePassword(WebAdminUserEntity webAdminUserEntity) {
        UserResource userResource = findUserById(webAdminUserEntity.getSsoId());
        UserRepresentation userRepresentation = userResource.toRepresentation();
        if (!StringUtils.isEmpty(webAdminUserEntity.getPassword())) {
            // 设置密码
            List<CredentialRepresentation> credentials = getCredentialRepresentations();
            userRepresentation.setCredentials(credentials);
        }
        userResource.update(userRepresentation);
    }

    public void updateEnable(WebAdminUserEntity webAdminUserEntity) {
        UserResource userResource = findUserById(webAdminUserEntity.getSsoId());
        UserRepresentation userRepresentation = userResource.toRepresentation();
        if (!StringUtils.isEmpty(webAdminUserEntity.isEnable())) {
            userRepresentation.setEnabled(webAdminUserEntity.isEnable());
        }
        userResource.update(userRepresentation);
    }

    public UserResource findUserById(String userId) {
        return init().users().get(userId);
    }

    public UserRepresentation findUserByName(String userName) {
        List<UserRepresentation> getUser = init().users().search(userName);
        if (getUser.size() > 0) {
            return getUser.get(0);
        }
        return null;
    }

    public void deleteUser(String userId) {
        init().users().delete(userId);
    }


    public Boolean existsFindByName(String userName) {
        List<UserRepresentation> getUser = init().users().search(userName);
        return getUser.size() > 0;
    }

    public ResponseEntity<SSOUserToken> ssoSignIn(String ssoToken) throws DoValidException {
        RestTemplate restTemplate = new RestTemplate();
        String rootUrl = ssoConfig.getUrl() + "/auth/realms/" + ssoConfig.getRealm() + "/protocol/openid-connect/userinfo";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Authorization", "bearer " + ssoToken);

        return restTemplate.exchange(rootUrl, HttpMethod.GET, new HttpEntity<String>(headers), SSOUserToken.class);


    }

}
