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
@Subselect("SELECT d1_api_base_info.id," +
        "d1_api_base_info.name as api_name," +
        "d1_api_base_info.route_info as route_info," +
        "table1.num as auth_num," +
        "table2.num as visit_num," +
        "d1_api_base_info.create_time as create_time " +
        "FROM d1_api_base_info " +
        "LEFT JOIN (" +
        "SELECT d1_api_auth_manage.api_id AS api_id," +
        "COUNT(d1_api_auth_manage.api_id) AS num " +
        "FROM d1_api_auth_manage GROUP BY api_id " +
        ") AS table1 " +
        "ON d1_api_base_info.id = table1.api_id " +
        "LEFT JOIN( " +
        "SELECT d1_api_log_record.api_id AS api_id," +
        "COUNT(d1_api_log_record.api_id) AS num " +
        "FROM d1_api_log_record GROUP BY api_id " +
        ") AS table2 " +
        "ON d1_api_base_info.id = table2.api_id")
public class ApiAnalysisList {
    @Id
    private String id;

    @ApiModelProperty("API 名称")
    private String apiName;

    @ApiModelProperty("路由信息")
    private String routeInfo;

    @ApiModelProperty("授权次数")
    private Long authNum;

    @ApiModelProperty("访问信息")
    private Long visitNum;

    @ApiModelProperty("创建时间")
    private Calendar createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Long getAuthNum() {
        if (authNum == null) {
            authNum = 0L;
        }
        return authNum;
    }

    public void setAuthNum(Long authNum) {
        this.authNum = authNum;
    }

    public Long getVisitNum() {
        if (visitNum == null) {
            visitNum = 0L;
        }
        return visitNum;
    }

    public void setVisitNum(Long visitNum) {
        this.visitNum = visitNum;
    }

    public Calendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }
}
