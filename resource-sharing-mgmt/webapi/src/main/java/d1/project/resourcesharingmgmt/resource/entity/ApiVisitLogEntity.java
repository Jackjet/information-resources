package d1.project.resourcesharingmgmt.resource.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseEntity;

import javax.persistence.*;
import java.util.Calendar;

/**
 * API访问日志
 *
 * @author zhengyang
 */
@Entity
@Table(name = "d1_api_visit_log")
@ApiModel(value = "ApiVisitLogEntity", description = "API访问日志")
public class ApiVisitLogEntity extends BaseEntity {
    @ApiModelProperty("API_ID")
    @Column(length = 32)
    private String apiId;

    @ApiModelProperty("所属容器")
    private String container;

    @ApiModelProperty("客户端IP/请求IP")
    @Column(length = 50)
    private String requestIp;

    @ApiModelProperty("请求时间")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar requestTime;

    @ApiModelProperty("响应状态码")
    @Column(length = 10)
    private String responseCode;

    @ApiModelProperty("状态")
    @Column(length = 10)
    private Integer status;

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

    public Calendar getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Calendar requestTime) {
        this.requestTime = requestTime;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
