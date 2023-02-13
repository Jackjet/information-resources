package d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo;

/**
 * @author maoyuying
 */
public class SourceApiVm {
    /**
     * 名称
     */
    private String name;
    /**
     * Host
     */
    private String host;
    /**
     * Path
     */
    private String path;
    /**
     * 请求协议
     */
    private String protocol;
    /**
     * 请求方式
     */
    private String method;

    /**
     * 内容格式类型
     */
    private String formatType;
    /**
     * body
     */
    private String body;
    /**
     * 入参内容
     */
    private String params;
    /**
     * 返回值
     */
    private String response;
    /**
     * 常量
     */
    private String constants;
    /**
     * apiId
     */
    private String apiId;

    /**
     * 容器
     */
    private String container;

    /**
     * 路由信息
     */
    private String routeInfo;

    /**
     * KONG_API_ID
     */
    private String kongApiId;

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

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getFormatType() {
        return formatType;
    }

    public void setFormatType(String formatType) {
        this.formatType = formatType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getConstants() {
        return constants;
    }

    public void setConstants(String constants) {
        this.constants = constants;
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

    public String getRouteInfo() {
        return routeInfo;
    }

    public void setRouteInfo(String routeInfo) {
        this.routeInfo = routeInfo;
    }

    public String getKongApiId() {
        return kongApiId;
    }

    public void setKongApiId(String kongApiId) {
        this.kongApiId = kongApiId;
    }
}
