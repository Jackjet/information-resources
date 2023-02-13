package d1.project.resource.api.model;

/**
 * @author baozh
 */
public class DesignInfo {

    /**
     * 名称
     */
    private String id;
    /**
     * 名称
     */
    private String name;
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
     * 常量
     */
    private String constants;
    /**
     * 返回结果
     */
    private String response;
    /**
     * 标签名
     */
    private String tagName;
    /**
     * 标签值
     */
    private String tagValue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getConstants() {
        return constants;
    }

    public void setConstants(String constants) {
        this.constants = constants;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }
}
