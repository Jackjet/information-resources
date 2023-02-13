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
@Subselect("SELECT  " +
        "d1_api_auth_manage.id as id," +
        "d1_api_auth_manage.api_id as api_id," +
        "d1_api_auth_manage.app_id as app_id," +
        "d1_application.name as app_name," +
        "d1_application.key as key," +
        "d1_api_auth_manage.list_type as type," +
        "d1_api_auth_manage.list_content as content," +
        "d1_api_auth_manage.create_time as create_time " +
        "FROM d1_api_auth_manage " +
        "LEFT JOIN d1_application ON d1_application.id = d1_api_auth_manage.app_id")
public class ApiAuthApp {
    @Id
    private String id;

    @ApiModelProperty("API ID")
    private String apiId;

    @ApiModelProperty("APP名称")
    private String appName;

    @ApiModelProperty("应用ID")
    private String appId;

    @ApiModelProperty("应用密钥")
    private String key;

    @ApiModelProperty("类型")
    private Integer type;

    @ApiModelProperty("内容")
    private String content;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Calendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
