package d1.project.d1project.apidesign.model;

import d1.framework.webapi.annotation.ApiModelProperty;


/**
 * @author baozh
 */
public class ApiTestCaseInsertVm {
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("ip")
    private String ip;

    @ApiModelProperty("API ID")
    private String apiId;

    @ApiModelProperty("方法 (GET/POST/PUT/DELETE)")
    private String method;

    @ApiModelProperty("是否保存测试结果 0否，1是")
    private Integer saveResult;

    @ApiModelProperty("测试用例内容")
    private String content;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getSaveResult() {
        return saveResult;
    }

    public void setSaveResult(Integer saveResult) {
        this.saveResult = saveResult;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
