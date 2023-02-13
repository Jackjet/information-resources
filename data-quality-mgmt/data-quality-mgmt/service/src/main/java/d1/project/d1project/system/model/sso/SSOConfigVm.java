package d1.project.d1project.system.model.sso;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.project.d1project.system.configuration.SSOConfig;

/**
 * SSO配置信息
 *
 * @author kikki
 * @date 2020-11-26 11:11
 */
@ApiModel("SSO配置信息")
public class SSOConfigVm {

    @ApiModelProperty("partialUse部分使用,useAll全部使用(未实现),doNotUse不使用")
    private String use;
    /**
     * @mock @url
     */
    @ApiModelProperty("单点登录地址")
    private String url;
    /**
     * @mock @string(0,100)
     */
    @ApiModelProperty("单点登录领域")
    private String realm;
    /**
     * @mock @guid
     */
    @ApiModelProperty("单点登录客户id")
    private String clientId;
    /**
     * @mock @guid
     */
    @ApiModelProperty("单点登录客户管理员id")
    private String clientAdminId;

    @ApiModelProperty("是否启用sso")
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
