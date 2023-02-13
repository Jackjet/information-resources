package d1.project.resourcesharingmgmt.resource.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import d1.framework.webapi.entity.BaseCreateEntity;

import javax.persistence.*;
import java.util.Calendar;

/**
 * 我的关注
 *
 * @author zhengyang
 */
@Entity
@Table(name = "d1_my_focus_info")
@ApiModel(value = "MyFocusInfoEntity", description = "我的关注")
public class MyFocusInfoEntity extends BaseCreateEntity {
    @ApiModelProperty("信息资源ID")
    @Column(length = 32)
    private String uviewId;

    @ApiModelProperty("信息资源名称")
    @Column(length = 100)
    private String uviewNm;

    @ApiModelProperty("信息资源代码")
    @Column(length = 100)
    private String uviewNo;

    @ApiModelProperty("共享类型,1无条件共享，2有条件共享，3不予共享")
    @Column(length = 4)
    private String shareLv;

    @ApiModelProperty("共享条件")
    @Column(length = 1000)
    private String shareCondition;

    @ApiModelProperty("发布日期")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar pubDt;

    @ApiModelProperty("信息资源摘要")
    @Column(length = 1000)
    private String uviewDesc;

    @ApiModelProperty("更新周期")
    @Column(length = 100)
    private String updateCyc;

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

    public String getUviewNo() {
        return uviewNo;
    }

    public void setUviewNo(String uviewNo) {
        this.uviewNo = uviewNo;
    }

    public String getShareLv() {
        return shareLv;
    }

    public void setShareLv(String shareLv) {
        this.shareLv = shareLv;
    }

    public String getShareCondition() {
        return shareCondition;
    }

    public void setShareCondition(String shareCondition) {
        this.shareCondition = shareCondition;
    }

    public Calendar getPubDt() {
        return pubDt;
    }

    public void setPubDt(Calendar pubDt) {
        this.pubDt = pubDt;
    }

    public String getUviewDesc() {
        return uviewDesc;
    }

    public void setUviewDesc(String uviewDesc) {
        this.uviewDesc = uviewDesc;
    }

    public String getUpdateCyc() {
        return updateCyc;
    }

    public void setUpdateCyc(String updateCyc) {
        this.updateCyc = updateCyc;
    }
}
