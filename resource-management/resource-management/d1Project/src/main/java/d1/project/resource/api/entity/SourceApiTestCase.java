package d1.project.resource.api.entity;

import d1.framework.webapi.entity.BaseCreateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @author libin
 * 源API
 */
@Entity
@Table(name = "d1_source_api_test_case")
public class SourceApiTestCase extends BaseCreateEntity {
    /**
     * 调用测试API的设备IP
     */
    private String ip;
    /**
     * 是否保存测试结果 0 否 1 是
     */
    private Integer saveResult;

    /**
     * 请求方式
     */
    private String method;
    /**
     * 测试用例内容
     */
    @Lob
    @Column(columnDefinition = "TEXT")
    @org.hibernate.annotations.Type(type = "org.hibernate.type.TextType")
    private String content;

    /**
     * 创建日（用于分组）
     */
    private String createDay;
    /**
     * 源APIId
     */
    private String sourceApiId;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getSaveResult() {
        return saveResult;
    }

    public void setSaveResult(Integer saveResult) {
        this.saveResult = saveResult;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateDay() {
        return createDay;
    }

    public void setCreateDay(String createDay) {
        this.createDay = createDay;
    }

    public String getSourceApiId() {
        return sourceApiId;
    }

    public void setSourceApiId(String sourceApiId) {
        this.sourceApiId = sourceApiId;
    }
}
