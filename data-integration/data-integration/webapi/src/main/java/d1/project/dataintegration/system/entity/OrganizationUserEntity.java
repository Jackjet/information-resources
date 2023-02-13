package d1.project.dataintegration.system.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "d1_organization_user")
@ApiModel(value = "OrganizationUserEntity", description = "组织机构用户关联")
public class OrganizationUserEntity extends BaseCreateEntity {

    @ApiModelProperty("用户id")
    @Column(length = 32)
    private String userId;

    @ApiModelProperty("组织机构id")
    @Column(length = 32)
    private String organizationId;


    public OrganizationUserEntity() {
    }

    public OrganizationUserEntity(String userId, String organizationId) {
        this.userId = userId;
        this.organizationId = organizationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
}
