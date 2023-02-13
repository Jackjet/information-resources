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
 * 权责清单
 * </p>
 *
 * @author wangmh
 * @since 2020-05-09
 */
@ApiModel("权责清单（saveOrUpdate）")
public class ArchBusiDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("busiId 主键")
    @TableId(type = IdType.AUTO)
    private Integer busiId;

    /**
     * 权责清单编码
     */
    @ApiModelProperty("权责清单编码")
    @NotNull(message = "权责清单编码不能为空")
    private String busiNo;

    /**
     * 权责清单名称
     */
    @ApiModelProperty("权责清单名称")
    @NotNull(message = "权责清单名称不能为空")
    private String busiNm;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    @NotNull(message = "部门id不能为空")
    private Integer deptId;

    /**
     * 服务对象 与dict关联
     */
    @ApiModelProperty("服务对象")
//    @NotNull(message = "服务对象不能为空")
    private String serviceObj;
    /**
     * 职权类别
     */
    @ApiModelProperty("职权类别")
    private String busiType;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("父id")
    private Integer pId;

    @ApiModelProperty("appIds")
    private List<Integer> appIds;

    public Integer getBusiId() {
        return busiId;
    }

    public void setBusiId(Integer busiId) {
        this.busiId = busiId;
    }

    public String getBusiNo() {
        return busiNo;
    }

    public void setBusiNo(String busiNo) {
        this.busiNo = busiNo;
    }

    public String getBusiNm() {
        return busiNm;
    }

    public void setBusiNm(String busiNm) {
        this.busiNm = busiNm;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getServiceObj() {
        return serviceObj;
    }

    public void setServiceObj(String serviceObj) {
        this.serviceObj = serviceObj;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public List<Integer> getAppIds() {
        return appIds;
    }

    public void setAppIds(List<Integer> appIds) {
        this.appIds = appIds;
    }

    public String getBusiType() {
        return busiType;
    }

    public void setBusiType(String busiType) {
        this.busiType = busiType;
    }
}
