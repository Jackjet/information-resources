package d1.project.d1project.apidesign.model;

import d1.framework.webapi.annotation.ApiModelProperty;

/**
 * @author zhengyang
 */
public class DesignInfo {
    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("标签")
    private String tagName;
    @ApiModelProperty("标签值")
    private String tagValue;
    @ApiModelProperty("请求协议")
    private String protocol;
    @ApiModelProperty("请求方法")
    private String method;
    @ApiModelProperty("内容格式类型")
    private String formatType;
    @ApiModelProperty("body")
    private String body;
    @ApiModelProperty("参数")
    private String params;
    @ApiModelProperty("常量")
    private String constants;
    @ApiModelProperty("返回结果")
    private String response;
    @ApiModelProperty("请求路径")
    private String path;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
