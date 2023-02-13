package d1.project.api.integration.apianalysis.view.entity;

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
        "d1_api_auth_manage.id as id," +
        "d1_api_auth_manage.api_id AS api_id," +
        "d1_api_auth_manage.app_id AS app_id," +
        "d1_application.name AS app_name," +
        "d1_api_auth_manage.create_time AS create_time " +
        "FROM d1_application " +
        "INNER JOIN d1_api_auth_manage ON d1_application.id = d1_api_auth_manage.app_id ")
public class ApiAppList {
    @Id
    private String id;

    @ApiModelProperty("API ID")
    private String apiId;

    @ApiModelProperty("APP名称")
    private String appName;

    @ApiModelProperty("应用ID")
    private String appId;

    @ApiModelProperty("创建时间")
    private Calendar createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Calendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }
}
