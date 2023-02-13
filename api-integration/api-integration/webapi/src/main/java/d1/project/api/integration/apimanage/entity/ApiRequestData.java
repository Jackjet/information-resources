package d1.project.api.integration.apimanage.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author baozh
 */

@Entity
@Table(name = "d1_api_request_data")
@ApiModel(value = "ApiRequestData", description = "API请求参数")
public class ApiRequestData extends BaseCreateAndUpdateEntity {

    @ApiModelProperty("API ID")
    @Column(length = 32)
    private String apiId;

    @ApiModelProperty("参数名称")
    @Column(length = 100)
    private String name;

    @ApiModelProperty("参数位置 (0:Parameter,1:Head)")
    @Column(length = 1)
    private Integer location;

    @ApiModelProperty("参数类型 (0:Number;1:String;2:Enum)")
    @Column(length = 1)
    private Integer type;

    @ApiModelProperty("是否必填 (0否1是)")
    @Column(length = 1)
    private Integer requiredNeed;

    @ApiModelProperty("是否有默认值 (0否1是)")
    @Column(length = 1)
    private Integer defaultNeed;

    @ApiModelProperty("路由信息")
    @Column(columnDefinition = "TEXT")
    private String defaultValue;

    @ApiModelProperty("正则验证(正确的正则验证字符串)")
    private String regularVerification;

    @ApiModelProperty("描述")
    @Column(columnDefinition = "TEXT")
    private String detail;

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

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getRequiredNeed() {
        return requiredNeed;
    }

    public void setRequiredNeed(Integer requiredNeed) {
        this.requiredNeed = requiredNeed;
    }

    public Integer getDefaultNeed() {
        return defaultNeed;
    }

    public void setDefaultNeed(Integer defaultNeed) {
        this.defaultNeed = defaultNeed;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getRegularVerification() {
        return regularVerification;
    }

    public void setRegularVerification(String regularVerification) {
        this.regularVerification = regularVerification;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
