package d1.project.resourcesharingmgmt.resource.entity;

import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 纠错表
 * @author maoyuying
 */
@Entity
@Table(name = "d1_correction")
public class CorrectionEntity extends BaseCreateAndUpdateEntity {
    /**
     * 提交部门ID
     */
    @Column(length = 32)
    private String orgId;

    /**
     * 提交部门名称
     */
    @Column(length = 32)
    private String orgName;

    /**
     * 信息资源ID
     */
    @Column(length = 32)
    private String uviewId;

    /**
     * 信息资源名称
     */
    @Column(length = 225)
    private String uviewNm;

    /**
     * 信息资源代码
     */
    @Column(length = 100)
    private String uviewNo;

    /**
     * 反馈类别，0其他，1数据与实际情况不符，2资源过时，3数据无法下载，
     */
    @Column(length = 100)
    private int type;

    /**
     * 描述
     */
    @Column(columnDefinition = "TEXT")
    private String describe;

    /**
     * 状态，0未处理，1已处理，2已驳回
     */
    private int status;

    /**
     * 纠错部门ID
     */
    @Column(length = 32)
    private String correctionOrgId;

    /**
     * 纠错部门名称
     */
    @Column(length = 32)
    private String correctionOrgName;

    /**
     * 驳回原因
     */
    @Column(columnDefinition = "TEXT")
    private String reject;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCorrectionOrgId() {
        return correctionOrgId;
    }

    public void setCorrectionOrgId(String correctionOrgId) {
        this.correctionOrgId = correctionOrgId;
    }

    public String getCorrectionOrgName() {
        return correctionOrgName;
    }

    public void setCorrectionOrgName(String correctionOrgName) {
        this.correctionOrgName = correctionOrgName;
    }

    public String getReject() {
        return reject;
    }

    public void setReject(String reject) {
        this.reject = reject;
    }
}
