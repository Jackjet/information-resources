package d1.project.resource.api.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.util.Date;

/**
 * @author libin
 */
@Entity
@Immutable
@Subselect("SELECT d1_source_api.id AS id," +
        "d1_source_api.name AS name," +
        "d1_source_api.host AS host," +
        "d1_source_api.load_balancing AS load_balancing," +
        "d1_source_api.path AS path," +
        "d1_source_api.protocol AS protocol," +
        "d1_source_api.method AS method," +
        "d1_source_api.format_type AS format_type," +
        "d1_source_api.body AS body," +
        "d1_source_api.params AS params," +
        "d1_source_api.response AS response," +
        "d1_source_api.constants AS constants," +
        "d1_source_api.update_time AS update_time," +
        "d1_source_api.create_time AS create_time," +
        "d1_source_api.group_id AS group_id," +
        "d1_source_api.tag_name AS tag_name," +
        "d1_source_api.tag_value AS tag_value," +
        "update_user.name AS update_by_name " +
        "FROM d1_source_api " +
        "LEFT JOIN d1_web_admin_user as update_user ON d1_source_api.update_by_id = update_user.id")
public class SourceApiView {
    @Id
    @ExcelIgnore
    private String id;
    /**
     * 名称
     */
    @ExcelProperty("名称")
    private String name;
    /**
     * Host
     */
    @ExcelProperty("Host")
    private String host;
    /**
     * 是否支持负载均衡
     */
    @ExcelProperty("是否支持负载均衡")
    private Boolean loadBalancing;
    /**
     * Path
     */
    @ExcelProperty("Path")
    private String path;
    /**
     * 请求协议
     */
    @ExcelProperty("请求协议")
    private String protocol;
    /**
     * 请求方式
     */
    @ExcelProperty("请求方式")
    private String method;
    /**
     * 内容格式类型
     */
    @ExcelProperty("内容格式类型")
    private String formatType;
    /**
     * body
     */
    @Lob
    @Column(columnDefinition = "TEXT")
    @org.hibernate.annotations.Type(type = "org.hibernate.type.TextType")
    @ExcelProperty("body")
    private String body;
    /**
     * 入参内容
     */
    @Lob
    @Column(columnDefinition = "TEXT")
    @org.hibernate.annotations.Type(type = "org.hibernate.type.TextType")
    @ExcelProperty("入参内容")
    private String params;
    /**
     * 返回值
     */
    @Lob
    @Column(columnDefinition = "TEXT")
    @org.hibernate.annotations.Type(type = "org.hibernate.type.TextType")
    @ExcelProperty("返回值")
    private String response;
    /**
     * 常量
     */
    @Lob
    @Column(columnDefinition = "TEXT")
    @org.hibernate.annotations.Type(type = "org.hibernate.type.TextType")
    @ExcelProperty("常量")
    private String constants;
    @ExcelIgnore
    @ExcelProperty("修改时间")
    private Date updateTime;
    @ExcelIgnore
    @ExcelProperty("创建时间")
    private Date createTime;
    /**
     * 提供方名称
     */
    @ExcelProperty("标签名")
    private String tagName;
    /**
     * 提供方名称
     */
    @ExcelProperty("标签值")
    private String tagValue;
    /**
     * 更新人
     */
    @ExcelIgnore
    @ExcelProperty("更新人")
    private String updateByName;

    /**
     * 更新人
     */
    @ExcelIgnore
    @ExcelProperty("分组ID")
    private String groupId;


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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Boolean getLoadBalancing() {
        return loadBalancing;
    }

    public void setLoadBalancing(Boolean loadBalancing) {
        this.loadBalancing = loadBalancing;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateByName() {
        return updateByName;
    }

    public void setUpdateByName(String updateByName) {
        this.updateByName = updateByName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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
