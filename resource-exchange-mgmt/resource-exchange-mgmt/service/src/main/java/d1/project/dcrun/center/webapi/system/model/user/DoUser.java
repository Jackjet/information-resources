package d1.project.dcrun.center.webapi.system.model.user;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author libin
 */
public class DoUser {
    @ApiModelProperty("唯一标识")
    private String id;
    @ApiModelProperty("唯一登录名")
    private String name;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("授权token")
    private String accessToken;
    @ApiModelProperty("更新token")
    private String refreshToken;
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(
            value = "授权token过期时间",
            example = "2018-01-23 09:12:32"
    )
    private Date tokenExpire;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Date getTokenExpire() {
        return tokenExpire;
    }

    public void setTokenExpire(Date tokenExpire) {
        this.tokenExpire = tokenExpire;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
