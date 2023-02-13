package d1.project.api.integration.apimanage.view.entity;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Immutable
@Subselect("SELECT " +
        "d1_kong_api.id as id," +
        "d1_kong_api.service_id as service_id," +
        "d1_kong_api.route_id as route_id," +
        "d1_kong_api.route_info as route_info," +
        "d1_kong_api.per_minute as per_minute," +
        "d1_kong_api.every_hour as every_hour," +
        "d1_kong_api.every_day as every_day," +
        "d1_kong_api.single_size as single_size," +
        "d1_kong_api.methods as methods," +
        "d1_kong_api.auth_id as auth_id," +
        "d1_kong_api.verification_mode as verification_mode," +
        "d1_kong_api.acl_id as acl_id," +
        "d1_kong_api.acl_group as acl_group," +
        "d1_kong_api.rate_limiting_id as rate_limiting_id," +
        "d1_kong_api.request_size_limiting_id as request_size_limiting_id," +
        "d1_kong_api.api_id as api_id," +
        "d1_api_base_info.container as container " +
        "FROM d1_kong_api " +
        "LEFT JOIN d1_api_base_info ON d1_api_base_info.id = d1_kong_api.api_id")
public class KongApiView {
    @Id
    private String id;

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

    private String container;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getRateLimitingId() {
        return rateLimitingId;
    }

    public void setRateLimitingId(String rateLimitingId) {
        this.rateLimitingId = rateLimitingId;
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

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }
}
