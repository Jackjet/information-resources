package d1.project.api.integration.apimanage.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author baozh
 */

@Entity
@Table(name = "d1_api_test_case")
@ApiModel(value = "ApiTestCase", description = "API测试用例")
public class ApiTestCase extends BaseCreateEntity {
    @ApiModelProperty("ip")
    @Column(length = 50)
    private String ip;

    @ApiModelProperty("API ID")
    @Column(length = 32)
    private String apiId;

    @ApiModelProperty("方法 (GET/POST/PUT/DELETE)")
    @Column(length = 10)
    private String method;

    @ApiModelProperty("是否保存测试结果 0否，1是")
    @Column(length = 1)
    private Integer saveResult;

    @ApiModelProperty("测试用例内容")
    @Column(columnDefinition = "TEXT")
    private String content;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }
}
