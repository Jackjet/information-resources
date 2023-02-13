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

@Entity
@Immutable
@Subselect("SELECT " +
        "d1_api_group_info.id as id," +
        "d1_api_group_info.name as name," +
        "d1_api_group_info.detail as detail," +
        "table1.num as num," +
        "d1_web_admin_user.name as user_name," +
        "d1_api_group_info.update_time as update_time " +
        "FROM d1_api_group_info " +
        "LEFT JOIN " +
        "(SELECT group_id,COUNT(group_id) as num FROM d1_group_api GROUP BY group_id) as table1 " +
        "ON d1_api_group_info.id = table1.group_id " +
        "LEFT JOIN d1_web_admin_user ON d1_api_group_info.create_by_id = d1_web_admin_user.id")
public class ApiGroupList {
    @Id
    private String id;

    @ApiModelProperty("组名称")
    private String name;

    @ApiModelProperty("组描述")
    private String detail;

    @ApiModelProperty("API数量")
    private Long num;

    @ApiModelProperty("创建人")
    private String userName;

    @ApiModelProperty("更新时间")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar updateTime;

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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Long getNum() {
        if (num == null) {
            num = 0L;
        }
        return num;
    }

    public void setNum(Long num) {
        if (num == null) {
            num = 0L;
        }
        this.num = num;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Calendar getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Calendar updateTime) {
        this.updateTime = updateTime;
    }
}
