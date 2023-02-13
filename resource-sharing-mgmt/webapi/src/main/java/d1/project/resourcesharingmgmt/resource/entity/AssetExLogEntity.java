package d1.project.resourcesharingmgmt.resource.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 资源挂接审批日志表
 *
 * @author zhengyang
 */
@Entity
@Table(name = "d1_asset_ex_log")
@ApiModel(value = "AssetExLogEntity", description = "资源挂接审批日志表")
public class AssetExLogEntity extends BaseCreateEntity {
    @ApiModelProperty("信息资源ID")
    @Column(length = 32)
    private String uviewId;

    @ApiModelProperty("信息资源名称")
    @Column(length = 225)
    private String uviewNm;

    @ApiModelProperty("审批ID")
    @Column(length = 32)
    private String assetExId;

    @ApiModelProperty("描述")
    @Column(columnDefinition = "TEXT")
    private String detail;

    @ApiModelProperty("类别，0文件，1数据库")
    private Integer type;

    @ApiModelProperty("审批状态描述")
    private String statusDetail;

    @ApiModelProperty("审批状态描述")
    private String createByName;

    public String getUviewId() {
        return uviewId;
    }

    public void setUviewId(String uviewId) {
        this.uviewId = uviewId;
    }

    public String getUviewNm() {
        return uviewNm;
    }

    public void setUviewNm(String uviewNm) {
        this.uviewNm = uviewNm;
    }

    public String getAssetExId() {
        return assetExId;
    }

    public void setAssetExId(String assetExId) {
        this.assetExId = assetExId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getStatusDetail() {
        return statusDetail;
    }

    public void setStatusDetail(String statusDetail) {
        this.statusDetail = statusDetail;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }
}
