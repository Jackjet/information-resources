package d1.project.api.integration.external.entity;

import d1.framework.webapi.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author libin
 * 应用与用户关联表
 */
@Entity
@Table(name = "d1_application_user")
public class ApplicationUser extends BaseEntity {
    private String userId;
    private String appid;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
}
