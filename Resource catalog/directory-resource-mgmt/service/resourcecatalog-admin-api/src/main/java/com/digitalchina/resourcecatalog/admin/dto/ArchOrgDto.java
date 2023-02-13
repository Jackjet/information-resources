package com.digitalchina.resourcecatalog.admin.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 组织机构
 * </p>
 *
 * @author wangmh
 * @since 2020-05-09
 */
@ApiModel("组织机构")
public class ArchOrgDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("组织机构id")
    private Integer orgId;
    /**
     * 组织机构名称
     */
    @ApiModelProperty("组织机构名称")
    @NotNull(message = "组织机构名称不能为空")
    private String orgNm;

    /**
     * 机构编码
     */
    @ApiModelProperty("机构编码")
    @NotNull(message = "机构编码不能为空")
    private String orgCd;

    /**
     * 简称
     */
    @ApiModelProperty("简称")
//    @NotNull(message = "简称不能为空")
    private String orgAlias;

    /**
     * 统一社会信用代码
     */
    @ApiModelProperty("统一社会信用代码")
//    @NotNull(message = "统一社会信用代码不能为空")
    private String socialCreditCd;

    /**
     * 显示顺序
     */
    @ApiModelProperty("排序序号")
    @NotNull(message = "排序序号不能为空")
    private Integer dispalySn;

    /**
     * 父节点ID
     */
    @ApiModelProperty("父节点id")
//    @NotNull(message = "父节点id不能为空")
    private Integer parOrgId;

    public String getOrgNm() {
        return orgNm;
    }

    public void setOrgNm(String orgNm) {
        this.orgNm = orgNm;
    }

    public String getOrgCd() {
        return orgCd;
    }

    public void setOrgCd(String orgCd) {
        this.orgCd = orgCd;
    }

    public String getOrgAlias() {
        return orgAlias;
    }

    public void setOrgAlias(String orgAlias) {
        this.orgAlias = orgAlias;
    }

    public String getSocialCreditCd() {
        return socialCreditCd;
    }

    public void setSocialCreditCd(String socialCreditCd) {
        this.socialCreditCd = socialCreditCd;
    }

    public Integer getDispalySn() {
        return dispalySn;
    }

    public void setDispalySn(Integer dispalySn) {
        this.dispalySn = dispalySn;
    }

    public Integer getParOrgId() {
        return parOrgId;
    }

    public void setParOrgId(Integer parOrgId) {
        this.parOrgId = parOrgId;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }
}
