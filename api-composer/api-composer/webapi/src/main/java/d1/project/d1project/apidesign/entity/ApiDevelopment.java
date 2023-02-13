package d1.project.d1project.apidesign.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * API开发信息
 *
 * @author baozh
 */

@Entity
@Table(name = "d1_api_development")
@ApiModel(value = "ApiDevelopment", description = "API开发信息")
public class ApiDevelopment extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("api设计ID")
    @Column(length = 50)
    private String apiDesignId;

    @ApiModelProperty("开发文件")
    @Column(columnDefinition = "TEXT")
    private String file;

    public String getApiDesignId() {
        return apiDesignId;
    }

    public void setApiDesignId(String apiDesignId) {
        this.apiDesignId = apiDesignId;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
