package d1.project.api.integration.external.model;

import d1.project.api.integration.apimanage.entity.SourceApi;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ApiExtInsertPostVm {
    /**
     * 用户id
     */
    @NotBlank(message = "用户id")
    private String userId;
    /**
     * 资源目录ID，防止router相同
     */
    @NotBlank(message = "用户id")
    private String uviewId;
    /**
     * 用户名称，拼音
     */
    @NotBlank(message = "用户名称不能为空")
    private String username;
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
     * 源API
     */
    private SourceApi sourceApi;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUviewId() {
        return uviewId;
    }

    public void setUviewId(String uviewId) {
        this.uviewId = uviewId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public SourceApi getSourceApi() {
        return sourceApi;
    }

    public void setSourceApi(SourceApi sourceApi) {
        this.sourceApi = sourceApi;
    }
}
