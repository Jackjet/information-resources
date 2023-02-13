package d1.project.api.integration.apimanage.view.entity;

import d1.framework.webapi.annotation.ApiModelProperty;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

/**
 * @author baozh
 */
@Entity
@Immutable
@Subselect("SELECT " +
        "d1_api_base_info.id as id," +
        "d1_api_base_info.name as name," +
        "d1_api_base_info.route_info as route_info," +
        "d1_api_base_info.container as container," +
        "d1_api_base_info.status as status," +
        "d1_api_base_info.group_id as group_id," +
        "d1_api_base_info.update_time as update_time," +
        "d1_web_admin_user.name as user_name," +
        "d1_source_api.name as source_name " +
        "FROM d1_api_base_info " +
        "LEFT JOIN d1_web_admin_user ON d1_api_base_info.update_by_id = d1_web_admin_user.id " +
        "LEFT JOIN d1_source_api ON d1_api_base_info.id = d1_source_api.api_id")
public class ApiWithSourceApi {
    @Id
    private String id;

    @ApiModelProperty("API名称")
    private String name;

    @ApiModelProperty("路由信息")
    private String routeInfo;

    @ApiModelProperty("容器")
    private String container;

    @ApiModelProperty("发布状态 0  未发布，1  已发布")
    private String status;

    @ApiModelProperty("组id")
    private String groupId;

    private Date updateTime;

    @ApiModelProperty("更新人")
    private String userName;

    @ApiModelProperty("接口资源名称")
    private String sourceName;

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

    public String getRouteInfo() {
        return routeInfo;
    }

    public void setRouteInfo(String routeInfo) {
        this.routeInfo = routeInfo;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
}
