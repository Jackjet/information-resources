package d1.project.api.integration.apianalysis.view.entity;

import d1.framework.webapi.annotation.ApiModelProperty;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Calendar;

/**
 * APP下授权的API表
 *
 * @author baozh
 */
@Entity
@Immutable
@Subselect("SELECT d1_api_auth_manage.id AS id," +
        "d1_api_auth_manage.app_id AS app_id," +
        "d1_api_auth_manage.api_id AS api_id," +
        "d1_api_base_info.name AS api_name," +
        "d1_api_base_info.route_info AS route_info," +
        "d1_api_auth_manage.create_time AS create_time " +
        "FROM d1_api_base_info INNER JOIN d1_api_auth_manage " +
        "ON d1_api_auth_manage.api_id = d1_api_base_info.id ")
public class AppApiList {
    @Id
    private String id;

    @ApiModelProperty("应用ID")
    private String appId;

    @ApiModelProperty("API ID")
    private String apiId;

    @ApiModelProperty("API名称")
    private String apiName;

    @ApiModelProperty("路由信息")
    private String routeInfo;

    @ApiModelProperty("创建时间")
    private Calendar createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getRouteInfo() {
        return routeInfo;
    }

    public void setRouteInfo(String routeInfo) {
        this.routeInfo = routeInfo;
    }

    public Calendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }
}
