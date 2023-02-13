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
@Subselect("SELECT d1_application.id AS id," +
        "d1_application.name AS app_name," +
        "table1.num AS auth_num," +
        "table2.num AS visit_num," +
        "d1_application.create_time as create_time " +
        "FROM d1_application " +
        "LEFT JOIN ( " +
        "SELECT d1_api_auth_manage.app_id AS app_id," +
        "COUNT(d1_api_auth_manage.app_id) AS num " +
        "FROM d1_api_auth_manage GROUP BY app_id " +
        ") AS table1 " +
        "ON d1_application.id = table1.app_id " +
        "LEFT JOIN( " +
        "SELECT d1_api_log_record.app_id AS app_id," +
        "COUNT(d1_api_log_record.app_id) AS num " +
        "FROM d1_api_log_record GROUP BY app_id " +
        ") AS table2 " +
        "ON d1_application.id = table2.app_id")
public class AppAnalysisList {
    @Id
    private String id;

    @ApiModelProperty("APP 名称")
    private String appName;

    @ApiModelProperty("授权信息")
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

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
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
