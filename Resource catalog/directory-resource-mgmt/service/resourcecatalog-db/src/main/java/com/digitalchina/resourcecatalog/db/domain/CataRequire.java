package com.digitalchina.resourcecatalog.db.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 信息需求目录
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
public class CataRequire implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 序号
     */
    @TableField(exist = false)
    private Integer number;

    /**
     * 所属部门ID
     */
    @NotNull(message = "所属部门ID不能为空")
    @ApiModelProperty("所属部门ID")
    private Integer deptId;

    /**
     * 所属部门名称
     */
    @TableField(exist = false)
    private String deptName;

    /**
     * 信息资源名称
     */
    @NotNull(message = "信息资源名称不能为空")
    @ApiModelProperty("信息资源名称")
    private String name;

    /**
     * 信息资源代码
     */
    @NotNull(message = "信息资源代码不能为空")
    @ApiModelProperty("信息资源代码")
    private String code;

    /**
     * 信息资源提供方机构ID
     */
    @ApiModelProperty("信息资源提供方机构ID")
    private Integer belongTo;
    /**
     * 信息资源提供方机构名称
     */
    @TableField(exist = false)
    @ApiModelProperty("信息资源提供方机构名称")
    private String belongToName;
    /**
     * 信息资源提供方机构代码
     */
    @ApiModelProperty("信息资源提供方机构代码")
    private String belongToCode;
    /**
     * 信息资源提供方部门ID
     */
    @ApiModelProperty("信息资源提供方部门ID")
    private Integer provOrgId;
    /**
     * 信息资源提供方部门名称
     */
    @TableField(exist = false)
    @ApiModelProperty("信息资源提供方部门名称")
    private String provOrgName;
    /**
     * 信息资源提供方部门代码
     */
    @ApiModelProperty("信息资源提供方部门代码")
    private String provOrgCode;

    /**
     * 是否实时获取 0-是 1-否
     */
    @NotNull(message = "是否实时获取必选")
    @ApiModelProperty("是否实时获取 0-是 1-否")
    private String isAccess;

    /**
     * 建议获取方式 与dict关联
     */
    @NotNull(message = "建议获取方式必选")
    @ApiModelProperty("建议获取方式")
    private String accessWay;

    /**
     * 期望数据使用频率 与dict关联
     */
    @NotNull(message = "期望数据使用频率必选")
    @ApiModelProperty("期望数据使用频率")
    private String useFrequency;

    /**
     * 信息资源描述
     */
    @ApiModelProperty("信息资源描述")
    private String remark;

    /**
     * 用途
     */
    @ApiModelProperty("用途")
    private String purpose;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新者id
     */
    private Integer updateId;

    /**
     * 更新者名称
     */
    private String updateName;

    /**
     * 更新日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 逻辑删除 0-否 1-是
     */
    private Integer deleted;

    /**
     * 所属机构ID
     */
    @ApiModelProperty("所属机构ID")
    private Integer orgId;

    @TableField(exist = false)
    private String orgName;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getProvOrgName() {
        return provOrgName;
    }

    public void setProvOrgName(String provOrgName) {
        this.provOrgName = provOrgName;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIsAccess() {
        return isAccess;
    }

    public void setIsAccess(String isAccess) {
        this.isAccess = isAccess;
    }

    public String getAccessWay() {
        return accessWay;
    }

    public String getBelongToName() {
        return belongToName;
    }

    public void setBelongToName(String belongToName) {
        this.belongToName = belongToName;
    }

    public void setAccessWay(String accessWay) {
        this.accessWay = accessWay;
    }

    public String getUseFrequency() {
        return useFrequency;
    }

    public void setUseFrequency(String useFrequency) {
        this.useFrequency = useFrequency;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Integer updateId) {
        this.updateId = updateId;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public Integer getBelongTo() {
        return belongTo;
    }

    public void setBelongTo(Integer belongTo) {
        this.belongTo = belongTo;
    }

    public String getBelongToCode() {
        return belongToCode;
    }

    public void setBelongToCode(String belongToCode) {
        this.belongToCode = belongToCode;
    }

    public Integer getProvOrgId() {
        return provOrgId;
    }

    public void setProvOrgId(Integer provOrgId) {
        this.provOrgId = provOrgId;
    }

    public String getProvOrgCode() {
        return provOrgCode;
    }

    public void setProvOrgCode(String provOrgCode) {
        this.provOrgCode = provOrgCode;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String CODE = "code";

    public static final String BELONG_TO_CODE = "belong_to_code";

    public static final String BELONG_TO = "belong_to";

    public static final String PROV_ORG_ID = "prov_org_id";

    public static final String PROV_ORG_CODE = "prov_org_code";

    public static final String IS_ACCESS = "is_access";

    public static final String ACCESS_WAY = "access_way";

    public static final String USE_FREQUENCY = "use_frequency";

    public static final String REMARK = "remark";

    public static final String PURPOSE = "purpose";

    public static final String CREATE_BY = "create_by";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_ID = "update_id";

    public static final String UPDATE_NAME = "update_name";

    public static final String UPDATE_TIME = "update_time";

    public static final String DELETED = "deleted";

    public static final String DEPT_ID = "dept_id";

    public static final String ORG_ID = "org_id";

    @Override
    public String toString() {
        return "CataRequire{" +
                "id=" + id +
                ", name=" + name +
                ", code=" + code +
                ", belongTo=" + belongTo +
                ", belongToCode=" + belongToCode +
                ", provOrgId=" + provOrgId +
                ", provOrgCode=" + provOrgCode +
                ", isAccess=" + isAccess +
                ", accessWay=" + accessWay +
                ", useFrequency=" + useFrequency +
                ", remark=" + remark +
                ", purpose=" + purpose +
                ", createBy=" + createBy +
                ", createTime=" + createTime +
                ", updateId=" + updateId +
                ", updateName=" + updateName +
                ", updateTime=" + updateTime +
                ", deleted=" + deleted +
                ", deptId=" + deptId +
                ", orgId=" + orgId +
                "}";
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
