package com.digitalchina.resourcecatalog.admin.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 应用系统信息
 * </p>
 *
 * @author wangmh
 * @since 2020-05-09
 */
@ApiModel("应用系统信息")
public class ArchAppSysDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键id")
    private Integer appsysId;

    /**
     * 部门ID
     */
    @ApiModelProperty("部门ID")
    @NotNull(message = "部门ID不能为空！")
    private Integer provOrgId;

    /**
     * 下设机构
     */
    @ApiModelProperty("内设部门名称")
//    @NotNull(message = "内设部门名称不能为空！")
    private Integer belongTo;

    /**
     * 应用系统代码
     */
    @ApiModelProperty("应用系统代码")
    @NotNull(message = "应用系统代码不能为空！")
    private String appsysNo;

    /**
     * 应用系统名称
     */
    @ApiModelProperty("应用系统名称")
    @NotNull(message = "应用系统名称不能为空！")
    private String appsysNm;

    /**
     * 建设性质
     */
    @ApiModelProperty("建设性质")
//    @NotNull(message = "建设性质不能为空！")
    private String nature;

    /**
     * 应用系统简介
     */
    @ApiModelProperty("应用系统简介")
    private String appsysDesc;

    /**
     * 部署位置
     */
    @ApiModelProperty("部署位置")
//    @NotNull(message = "部署位置不能为空！")
    private String position;

    /**
     * 接入网络类型
     */
    @ApiModelProperty("接入网络类型")
//    @NotNull(message = "接入网络类型不能为空！")
    private String netType;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /***
     * 权责清单ids
     */
    @ApiModelProperty("权责清单ids")
    private List<Integer> busiIds;

    public Integer getAppsysId() {
        return appsysId;
    }

    public void setAppsysId(Integer appsysId) {
        this.appsysId = appsysId;
    }

    public Integer getProvOrgId() {
        return provOrgId;
    }

    public void setProvOrgId(Integer provOrgId) {
        this.provOrgId = provOrgId;
    }

    public Integer getBelongTo() {
        return belongTo;
    }

    public void setBelongTo(Integer belongTo) {
        this.belongTo = belongTo;
    }

    public String getAppsysNo() {
        return appsysNo;
    }

    public void setAppsysNo(String appsysNo) {
        this.appsysNo = appsysNo;
    }

    public String getAppsysNm() {
        return appsysNm;
    }

    public void setAppsysNm(String appsysNm) {
        this.appsysNm = appsysNm;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getAppsysDesc() {
        return appsysDesc;
    }

    public void setAppsysDesc(String appsysDesc) {
        this.appsysDesc = appsysDesc;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "ArchAppSys{" +
                "appsysId=" + appsysId +
                ", provOrgId=" + provOrgId +
                ", belongTo=" + belongTo +
                ", appsysNo=" + appsysNo +
                ", appsysNm=" + appsysNm +
                ", nature=" + nature +
                ", appsysDesc=" + appsysDesc +
                ", position=" + position +
                ", netType=" + netType +
                ", remark=" + remark +
                "}";
    }

    public List<Integer> getBusiIds() {
        return busiIds;
    }

    public void setBusiIds(List<Integer> busiIds) {
        this.busiIds = busiIds;
    }
}
