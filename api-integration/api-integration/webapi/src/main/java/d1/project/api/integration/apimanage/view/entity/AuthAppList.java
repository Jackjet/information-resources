package d1.project.api.integration.apimanage.view.entity;

import d1.framework.webapi.annotation.ApiModelProperty;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Calendar;

/**
 * @author baozh
 */

@Entity
@Immutable
@Subselect("SELECT " +
        "d1_application.id as id," +
        "d1_application.name as app_name," +
        "d1_web_admin_user.name as user_name," +
        "d1_application.create_time as create_time " +
        "FROM d1_application " +
        "LEFT JOIN d1_web_admin_user ON d1_application.create_by_id = d1_web_admin_user.id")
public class AuthAppList {
    @Id
    private String id;

    @ApiModelProperty("APP名称")
    private String appName;

    @ApiModelProperty("创建人名称")
    private String userName;

    @ApiModelProperty("创建时间")
    private Calendar createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Calendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }
}
