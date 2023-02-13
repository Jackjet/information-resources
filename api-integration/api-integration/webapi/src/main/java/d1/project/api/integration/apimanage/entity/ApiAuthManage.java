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
@Table(name = "d1_api_auth_manage")
@ApiModel(value = "ApiAuthManage", description = "授权管理")
public class ApiAuthManage extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("应用ID")
    @Column(length = 32)
    private String appId;

    @ApiModelProperty("API ID")
    @Column(length = 32)
    private String apiId;

    @ApiModelProperty("访问限制类型(0黑名单，1白名单)")
    @Column(length = 1)
    private Integer listType;

    @ApiModelProperty("应用访问名单（IP地址，多个请以“,”分开）")
    @Column(columnDefinition = "TEXT")
    private String listContent;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public Integer getListType() {
        return listType;
    }

    public void setListType(Integer listType) {
        this.listType = listType;
    }

    public String getListContent() {
        return listContent;
    }

    public void setListContent(String listContent) {
        this.listContent = listContent;
    }
}
