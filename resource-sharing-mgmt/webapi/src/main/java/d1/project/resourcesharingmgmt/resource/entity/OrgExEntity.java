package d1.project.resourcesharingmgmt.resource.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 机构和机构码关联
 *
 * @author zhengyang
 */
@Entity
@Table(name = "d1_org_ex")
@ApiModel(value = "OrgExEntity", description = "机构和机构码关联")
public class OrgExEntity extends BaseEntity {
    @ApiModelProperty("机构名称")
    @Column(length = 32)
    private String name;

    @ApiModelProperty("机构码")
    @Column(length = 50)
    private String orgCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }
}
