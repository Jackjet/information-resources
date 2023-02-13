package d1.project.api.integration.apimanage.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author baozh
 */
@Entity
@Table(name = "d1_api_base_info")
@ApiModel(value = "ApiBaseInfo", description = "API基本信息")
public class ApiBaseInfo extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("API名称")
    private String name;

    @ApiModelProperty("描述")
    @Column(columnDefinition = "TEXT")
    private String detail;

    @ApiModelProperty("路由信息")
    private String routeInfo;

    @ApiModelProperty("每分钟请求限制 默认1000")
    private Long perMinute = 1000L;

    @ApiModelProperty("每小时请求限制 默认6000")
    private Long everyHour = 6000L;

    @ApiModelProperty("每分钟请求限制 默认10000")
    private Long everyDay = 10000L;

    @ApiModelProperty("单次请求大小限制 默认1  单位MB")
    private Long singleSize = 1L;
    @ApiModelProperty("容器")
    private String container;

    @ApiModelProperty("发布状态 0  未发布，1  已发布")
    private String status;

    @ApiModelProperty("组id")
    private String groupId;

    @ApiModelProperty("验证方式，0 Hmac, 1 Key-Auth")
    private String verificationMode;

    @ApiModelProperty("请求方式")
    private String methods;

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

    public String getRouteInfo() {
        return routeInfo;
    }

    public void setRouteInfo(String routeInfo) {
        this.routeInfo = routeInfo;
    }

    public Long getPerMinute() {
        if (perMinute == null) {
            perMinute = 1000L;
        }
        return perMinute;
    }

    public void setPerMinute(Long perMinute) {
        if (perMinute == null) {
            perMinute = 1000L;
        }
        this.perMinute = perMinute;
    }

    public Long getEveryHour() {
        if (everyHour == null) {
            everyHour = 6000L;
        }
        return everyHour;
    }

    public void setEveryHour(Long everyHour) {
        if (everyHour == null) {
            everyHour = 6000L;
        }
        this.everyHour = everyHour;
    }

    public Long getEveryDay() {
        if (everyDay == null) {
            everyDay = 10000L;
        }
        return everyDay;
    }

    public void setEveryDay(Long everyDay) {
        if (everyDay == null) {
            everyDay = 10000L;
        }
        this.everyDay = everyDay;
    }

    public Long getSingleSize() {
        if (singleSize == null) {
            singleSize = 1L;
        }
        return singleSize;
    }

    public void setSingleSize(Long singleSize) {
        this.singleSize = singleSize;
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

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getVerificationMode() {
        return verificationMode;
    }

    public void setVerificationMode(String verificationMode) {
        this.verificationMode = verificationMode;
    }

    public String getMethods() {
        return methods;
    }

    public void setMethods(String methods) {
        this.methods = methods;
    }
}
