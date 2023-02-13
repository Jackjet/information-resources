package d1.project.d1project.apidesign.model;

import d1.framework.webapi.annotation.ApiModelProperty;

import javax.persistence.Column;

/**
 * @author baozh
 */
public class ApiTestDetailVm {
    @ApiModelProperty("apiId")
    private String apiId;

    @ApiModelProperty("API名称")
    private String name;

    @ApiModelProperty("HOST")
    private String host;

    @ApiModelProperty("Path")
    private String path;

    @ApiModelProperty("请求方法")
    private String method;

    @ApiModelProperty("应用Key")
    private String key;

    @ApiModelProperty("路由信息")
    private String routeInfo;

    @ApiModelProperty("请求参数")
    @Column(columnDefinition = "TEXT")
    private String params;

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRouteInfo() {
        return routeInfo;
    }

    public void setRouteInfo(String routeInfo) {
        this.routeInfo = routeInfo;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
}
