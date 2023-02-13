package com.digitalchina.resourcecatalog.admin.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 部门职能职责
 * </p>
 *
 * @author wangmh
 * @since 2020-05-09
 */
@ApiModel("部门职能职责")
public class ArchOrgEditDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("组织机构id")
    @NotNull(message = "组织机构id不能为空")
    private Integer orgId;

    /**
     * 联系人电话
     */
    @ApiModelProperty("联系人电话")
//    @NotNull(message = "联系人电话不能为空")
    private String orgTel;

    /**
     * 联系人姓名
     */
    @ApiModelProperty("联系人姓名")
    private String orgLinkman;

    /**
     * FAX
     */
    @ApiModelProperty("传真")
//    @NotNull(message = "传真不能为空")
    private String orgFax;

    /**
     * EMAIL
     */
    @ApiModelProperty("EMAIL")
//    @NotNull(message = "EMAIL不能为空")
    private String orgMail;

    /**
     * 职能职责
     */
    @ApiModelProperty("职能职责")
    @NotNull(message = "职能职责不能为空")
    private String orgDuty;

    /**
     * 通讯地址
     */
    @ApiModelProperty("通讯地址")
//    @NotNull(message = "通讯地址不能为空")
    private String addr;

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getOrgTel() {
        return orgTel;
    }

    public void setOrgTel(String orgTel) {
        this.orgTel = orgTel;
    }

    public String getOrgFax() {
        return orgFax;
    }

    public void setOrgFax(String orgFax) {
        this.orgFax = orgFax;
    }

    public String getOrgMail() {
        return orgMail;
    }

    public void setOrgMail(String orgMail) {
        this.orgMail = orgMail;
    }

    public String getOrgDuty() {
        return orgDuty;
    }

    public void setOrgDuty(String orgDuty) {
        this.orgDuty = orgDuty;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getOrgLinkman() {
        return orgLinkman;
    }

    public void setOrgLinkman(String orgLinkman) {
        this.orgLinkman = orgLinkman;
    }
}
