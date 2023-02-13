package d1.project.api.integration.apimanage.model;

import d1.framework.webapi.annotation.ApiModelProperty;
import d1.project.api.integration.apimanage.entity.SourceApi;

import javax.persistence.Column;

public class ApiInsertVm {
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

    @ApiModelProperty("组id")
    private String groupId;

    @ApiModelProperty("标签，[{key0:value0},{key1:value1}]")
    private String metas;

    @ApiModelProperty("验证方式，0 Hmac, 1 Key-Auth")
    private String verificationMode;

    private SourceApi sourceApi;

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

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMetas() {
        return metas;
    }

    public void setMetas(String metas) {
        this.metas = metas;
    }

    public SourceApi getSourceApi() {
        return sourceApi;
    }

    public void setSourceApi(SourceApi sourceApi) {
        this.sourceApi = sourceApi;
    }

    public String getVerificationMode() {
        return verificationMode;
    }

    public void setVerificationMode(String verificationMode) {
        this.verificationMode = verificationMode;
    }
}
