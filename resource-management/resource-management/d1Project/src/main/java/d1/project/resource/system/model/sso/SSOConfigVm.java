package d1.project.resource.system.model.sso;

import d1.project.resource.system.configuration.SSOConfig;

/**
 * MY_JAR_NAME
 *
 * @author kikki
 * @date 2020-11-26 11:11
 */
public class SSOConfigVm {
    private String use;

    private String url;

    private String realm;

    private String clientId;
    private String clientAdminId;


    private boolean sso;

    public SSOConfigVm() {
    }

    public SSOConfigVm(SSOConfig ssoConfig) {
        this.use = ssoConfig.getUse();
        this.url = ssoConfig.getUrl();
        this.clientId = ssoConfig.getClientId();
        this.clientAdminId = ssoConfig.getClientAdminId();
        this.realm = ssoConfig.getRealm();
        this.sso = ssoConfig.isSso();
    }

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

    public boolean isSso() {
        return sso;
    }

    public String getClientAdminId() {
        return clientAdminId;
    }

    public void setClientAdminId(String clientAdminId) {
        this.clientAdminId = clientAdminId;
    }
}
