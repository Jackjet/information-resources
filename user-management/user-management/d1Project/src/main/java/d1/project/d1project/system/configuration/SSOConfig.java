package d1.project.d1project.system.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * MY_JAR_NAME
 *
 * @author kikki
 * @date 2020-11-26 11:11
 */
@Configuration
@ConfigurationProperties(prefix = "sso")
public class SSOConfig {
    private String use;

    private String url;

    private String realm;

    private String clientId;
    private String clientAdminId;
    private String clientAdminSecret;

    private boolean sso;

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setSso(boolean sso) {
        this.sso = sso;
    }

    public String getClientAdminSecret() {
        return clientAdminSecret;
    }

    public void setClientAdminSecret(String clientAdminSecret) {
        this.clientAdminSecret = clientAdminSecret;
    }

    public String getClientAdminId() {
        return clientAdminId;
    }

    public void setClientAdminId(String clientAdminId) {
        this.clientAdminId = clientAdminId;
    }



    public boolean isSso() {
        switch (use) {
            case "partialUse":
                return true;
            case "useAll":
                return true;
            case "doNotUse":
                return false;
            default:
                return false;
        }
    }

}
