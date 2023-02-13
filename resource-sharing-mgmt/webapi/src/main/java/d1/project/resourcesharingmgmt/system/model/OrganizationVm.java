package d1.project.resourcesharingmgmt.system.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.persistence.Column;

@ApiModel(value = "OrganizationVm", description = "组织机构")
public class OrganizationVm {

    @ApiModelProperty("部门名称")
    @Column(length = 100)
    private String orgNm;

    @ApiModelProperty("部门简称")
    private String orgAlias;

    @ApiModelProperty("部门ID")
    @Column(length = 100)
    private Integer orgId;

    @ApiModelProperty("上级id")
    @Column(length = 32)
    private String parOrgId;

    @ApiModelProperty("部门编号")
    @Column(length = 100)
    private String orgCd;

    @ApiModelProperty("级别")
    @Column(length = 1)
    private int level;

    @ApiModelProperty("序号")
    private int dispalySn;

    public String getOrgNm() {
        return orgNm;
    }

    public void setOrgNm(String orgNm) {
        this.orgNm = orgNm;
    }

    public String getOrgAlias() {
        return orgAlias;
    }

    public void setOrgAlias(String orgAlias) {
        this.orgAlias = orgAlias;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getParOrgId() {
        return parOrgId;
    }

    public void setParOrgId(String parOrgId) {
        this.parOrgId = parOrgId;
    }

    public String getOrgCd() {
        return orgCd;
    }

    public void setOrgCd(String orgCd) {
        this.orgCd = orgCd;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDispalySn() {
        return dispalySn;
    }

    public void setDispalySn(int dispalySn) {
        this.dispalySn = dispalySn;
    }
}
