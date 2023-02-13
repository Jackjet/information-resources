package d1.project.api.integration.apimanage.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author libin
 */
@Entity
@Table(name = "d1_kong_api")
@ApiModel(value = "KongApi", description = "API在kong中的配置")
public class KongApi extends BaseEntity {
    /**
     * API 路由ID （KONG使用）
     */
    private String serviceId;
    /**
     * API 路由ID （KONG使用）
     */
    private String routeId;
    /**
     * 路由信息
     */
    private String routeInfo;
    /**
     * 每分钟请求限制 默认1000
     */
    private Long perMinute = 1000L;
    /**
     * 每小时请求限制 默认6000
     */
    private Long everyHour = 6000L;
    /**
     * 每分钟请求限制 默认10000
     */
    private Long everyDay = 10000L;
    /**
     * 单次请求大小限制 默认1  单位MB
     */
    private Long singleSize = 1L;

    /**
     * 请求方式 （KONG使用），以,分隔
     */
    private String methods;

    /**
     * auth插件Id
     */
    private String authId;

    /**
     * 验证方式  0 hmac   1  key
     */
    private String verificationMode;

    /**
     * acl插件Id
     */
    private String aclId;

    /**
     * acl插件Id
     */
    private String aclGroup;

    /**
     * 限流插件Id
     */
    private String rateLimitingId;

    /**
     * 限制请求大小插件Id
     */
    private String requestSizeLimitingId;

    private String apiId;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteInfo() {
        return routeInfo;
    }

    public void setRouteInfo(String routeInfo) {
        this.routeInfo = routeInfo;
    }

    public Long getPerMinute() {
        return perMinute;
    }

    public void setPerMinute(Long perMinute) {
        this.perMinute = perMinute;
    }

    public Long getEveryHour() {
        return everyHour;
    }

    public void setEveryHour(Long everyHour) {
        this.everyHour = everyHour;
    }

    public Long getEveryDay() {
        return everyDay;
    }

    public void setEveryDay(Long everyDay) {
        this.everyDay = everyDay;
    }

    public Long getSingleSize() {
        return singleSize;
    }

    public void setSingleSize(Long singleSize) {
        this.singleSize = singleSize;
    }

    public String getMethods() {
        return methods;
    }

    public void setMethods(String methods) {
        this.methods = methods;
    }

    public String getRateLimitingId() {
        return rateLimitingId;
    }

    public void setRateLimitingId(String rateLimitingId) {
        this.rateLimitingId = rateLimitingId;
    }

    public String getAclId() {
        return aclId;
    }

    public void setAclId(String aclId) {
        this.aclId = aclId;
    }

    public String getAclGroup() {
        return aclGroup;
    }

    public void setAclGroup(String aclGroup) {
        this.aclGroup = aclGroup;
    }

    public String getRequestSizeLimitingId() {
        return requestSizeLimitingId;
    }

    public void setRequestSizeLimitingId(String requestSizeLimitingId) {
        this.requestSizeLimitingId = requestSizeLimitingId;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getVerificationMode() {
        return verificationMode;
    }

    public void setVerificationMode(String verificationMode) {
        this.verificationMode = verificationMode;
    }
}
