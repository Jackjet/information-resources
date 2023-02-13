package d1.project.api.integration.apimanage.view.entity;

/**
 * @author baozh
 */

import d1.framework.webapi.annotation.ApiModelProperty;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Calendar;
import java.util.Date;

@Entity
@Immutable
@Subselect("SELECT " +
        "d1_api_base_info.id as id," +
        "d1_api_base_info.name as name," +
        "d1_api_base_info.update_time as update_time," +
        "d1_api_base_info.group_id as group_id," +
        "d1_api_base_info.container as container," +
        "d1_kong_api.methods as methods," +
        "d1_web_admin_user.name as update_name," +
        "d1_api_group_info.name as group_name " +
        "FROM d1_api_base_info " +
        "LEFT JOIN d1_api_group_info ON d1_api_group_info.id = d1_api_base_info.group_id " +
        "LEFT JOIN d1_web_admin_user ON d1_web_admin_user.id = d1_api_base_info.update_by_id " +
        "LEFT JOIN d1_kong_api ON d1_kong_api.api_id = d1_api_base_info.id ")
public class GroupApiList {
    @Id
    private String id;
    @ApiModelProperty("API名称")
    private String name;
    @ApiModelProperty("更新时间")
    private Date updateTime;
    @ApiModelProperty("组Id")
    private String groupId;
    @ApiModelProperty("请求方式")
    private String methods;

    @ApiModelProperty("更新人")
    private String updateName;

    @ApiModelProperty("分组名称")
    private String groupName;

    @ApiModelProperty("容器")
    private String container;

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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMethods() {
        return methods;
    }

    public void setMethods(String methods) {
        this.methods = methods;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }
}
