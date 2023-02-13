package d1.project.resourcesharingmgmt.resource.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 信息项
 *
 * @author zhengyang
 */
@Entity
@Table(name = "d1_arch_busi_uview_str")
@ApiModel(value = "ArchBusiUviewStrExEntity", description = "信息项")
public class ArchBusiUviewStrExEntity extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("资源目录信息项ID")
    @Column(length = 32)
    private String uviewstrId;

    @ApiModelProperty("资源目录ID,信息资源目录外键")
    @Column(length = 32)
    private String uviewId;

    @ApiModelProperty("信息项名称")
    @Column(length = 200)
    private String srcField;

    @ApiModelProperty("信息项英文标识")
    @Column(length = 200)
    private String engCd;

    @ApiModelProperty("信息项数据类型")
    @Column(length = 20)
    private String srcDataTyp;

    @ApiModelProperty("信息项数据长度")
    @Column(length = 20)
    private String dataLen;

    @ApiModelProperty("信息项描述")
    @Column(columnDefinition = "TEXT")
    private String itemRemark;

    @ApiModelProperty("显示序号")
    @Column(length = 20)
    private String sno;

    @ApiModelProperty("扩展1")
    private String ext1;

    @ApiModelProperty("扩展2")
    private String ext2;

    @ApiModelProperty("扩展3")
    private String ext3;

    @ApiModelProperty("扩展4")
    private String ext4;

    @ApiModelProperty("扩展5")
    private String ext5;

    @ApiModelProperty("扩展6")
    private String ext6;

    @ApiModelProperty("扩展7")
    private String ext7;

    @ApiModelProperty("扩展8")
    private String ext8;

    @ApiModelProperty("扩展9")
    private String ext9;

    @ApiModelProperty("扩展10")
    private String ext10;

    @ApiModelProperty("扩展11")
    private String ext11;

    @ApiModelProperty("扩展12")
    private String ext12;

    @ApiModelProperty("扩展13")
    private String ext13;

    @ApiModelProperty("扩展14")
    private String ext14;

    @ApiModelProperty("扩展15")
    private String ext15;

    @ApiModelProperty("扩展16")
    private String ext16;

    @ApiModelProperty("扩展17")
    private String ext17;

    @ApiModelProperty("扩展18")
    private String ext18;

    @ApiModelProperty("扩展19")
    private String ext19;

    @ApiModelProperty("扩展20")
    private String ext20;

    public String getUviewstrId() {
        return uviewstrId;
    }

    public void setUviewstrId(String uviewstrId) {
        this.uviewstrId = uviewstrId;
    }

    public String getUviewId() {
        return uviewId;
    }

    public void setUviewId(String uviewId) {
        this.uviewId = uviewId;
    }

    public String getSrcField() {
        return srcField;
    }

    public void setSrcField(String srcField) {
        this.srcField = srcField;
    }

    public String getEngCd() {
        return engCd;
    }

    public void setEngCd(String engCd) {
        this.engCd = engCd;
    }

    public String getSrcDataTyp() {
        return srcDataTyp;
    }

    public void setSrcDataTyp(String srcDataTyp) {
        this.srcDataTyp = srcDataTyp;
    }

    public String getDataLen() {
        return dataLen;
    }

    public void setDataLen(String dataLen) {
        this.dataLen = dataLen;
    }

    public String getItemRemark() {
        return itemRemark;
    }

    public void setItemRemark(String itemRemark) {
        this.itemRemark = itemRemark;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    public String getExt3() {
        return ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }

    public String getExt4() {
        return ext4;
    }

    public void setExt4(String ext4) {
        this.ext4 = ext4;
    }

    public String getExt5() {
        return ext5;
    }

    public void setExt5(String ext5) {
        this.ext5 = ext5;
    }

    public String getExt6() {
        return ext6;
    }

    public void setExt6(String ext6) {
        this.ext6 = ext6;
    }

    public String getExt7() {
        return ext7;
    }

    public void setExt7(String ext7) {
        this.ext7 = ext7;
    }

    public String getExt8() {
        return ext8;
    }

    public void setExt8(String ext8) {
        this.ext8 = ext8;
    }

    public String getExt9() {
        return ext9;
    }

    public void setExt9(String ext9) {
        this.ext9 = ext9;
    }

    public String getExt10() {
        return ext10;
    }

    public void setExt10(String ext10) {
        this.ext10 = ext10;
    }

    public String getExt11() {
        return ext11;
    }

    public void setExt11(String ext11) {
        this.ext11 = ext11;
    }

    public String getExt12() {
        return ext12;
    }

    public void setExt12(String ext12) {
        this.ext12 = ext12;
    }

    public String getExt13() {
        return ext13;
    }

    public void setExt13(String ext13) {
        this.ext13 = ext13;
    }

    public String getExt14() {
        return ext14;
    }

    public void setExt14(String ext14) {
        this.ext14 = ext14;
    }

    public String getExt15() {
        return ext15;
    }

    public void setExt15(String ext15) {
        this.ext15 = ext15;
    }

    public String getExt16() {
        return ext16;
    }

    public void setExt16(String ext16) {
        this.ext16 = ext16;
    }

    public String getExt17() {
        return ext17;
    }

    public void setExt17(String ext17) {
        this.ext17 = ext17;
    }

    public String getExt18() {
        return ext18;
    }

    public void setExt18(String ext18) {
        this.ext18 = ext18;
    }

    public String getExt19() {
        return ext19;
    }

    public void setExt19(String ext19) {
        this.ext19 = ext19;
    }

    public String getExt20() {
        return ext20;
    }

    public void setExt20(String ext20) {
        this.ext20 = ext20;
    }
}
