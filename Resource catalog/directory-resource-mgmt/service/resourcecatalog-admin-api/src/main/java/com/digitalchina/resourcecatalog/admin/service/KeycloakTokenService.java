package com.digitalchina.resourcecatalog.admin.service;

import com.digitalchina.resourcecatalog.admin.util.HttpClientUtil;
import com.digitalchina.resourcecatalog.core.util.JacksonUtil;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author dy
 */
@Service
public class KeycloakTokenService {

    @Value("${keycloakTokenCheck.auth-server-url}")
    String authServerUrl;

    @Value("${keycloakTokenCheck.realm}")
    String realm;

    @Value("${keycloakTokenCheck.resource}")
    String resource;

    public String getUserNameByToken(String token) {
        String username = null;
//		String url = "http://localhost:8080/auth/realms/myrealm/protocol/openid-connect/userinfo";
        String url = authServerUrl + "/realms/" + realm + "/protocol/openid-connect/userinfo" ;
        BasicHeader headerAccept = new BasicHeader("Accept", "application/json");
        BasicHeader headerAuthorization = new BasicHeader("Authorization", token);
        Header[] headers = {headerAccept, headerAuthorization};
        try {
            String rs = HttpClientUtil.doGet(url, 20000, headers, "utf-8");
            username = JacksonUtil.parseString(rs, "preferred_username");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return username;
    }
}
