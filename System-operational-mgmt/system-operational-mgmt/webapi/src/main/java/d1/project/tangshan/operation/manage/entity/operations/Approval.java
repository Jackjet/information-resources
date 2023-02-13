package d1.project.tangshan.operation.manage.entity.operations;

import d1.framework.webapi.entity.BaseCreateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author lin
 */
@Entity
@Table(name = "d1_approval")
@ApiModel(value = "Approval", description = "审批")
public class Approval extends BaseCreateEntity {
    @ApiModelProperty("申请人姓名")
    private String applicant;
    @ApiModelProperty("手机号")
    private String tel;
    @ApiModelProperty("单位")
    private String organization;
    @ApiModelProperty("申请类型（远程控制 control 远程SQL sql ）")
    private String type;
    @ApiModelProperty("申请内容")
    private String content;
    @ApiModelProperty("状态(stay待审批agree已同意reject已驳回)")
    private String status;

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
