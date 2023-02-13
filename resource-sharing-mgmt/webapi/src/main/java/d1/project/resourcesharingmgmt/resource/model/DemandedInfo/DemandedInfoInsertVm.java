package d1.project.resourcesharingmgmt.resource.model.DemandedInfo;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.util.Calendar;

/**
 * @author zhengyang
 */
@ApiModel(value = "DemandedInfoInsertVm", description = "需求申请")
public class DemandedInfoInsertVm {
    @ApiModelProperty("需求标题")
    @NotBlank(message = "需求标题不可为空")
    private String title;

    @ApiModelProperty("缘由")
    @NotBlank(message = "缘由不可为空")
    private String reason;

    @ApiModelProperty("缘由说明")
    @NotBlank(message = "缘由说明不可为空")
    private String reasonDesc;

    @ApiModelProperty("受理单位id")
    @NotBlank(message = "受理单位不可为空")
    private String acceptDeptId;

    @ApiModelProperty("受理部门")
    @NotBlank(message = "受理部门不可为空")
    private String acceptDept;

    @ApiModelProperty("需求类型")
    @NotBlank(message = "需求类型不可为空")
    private String requestType;

    @ApiModelProperty("需求描述")
    @NotBlank(message = "需求描述不可为空")
    private String describe;

    @ApiModelProperty("联系人")
    @NotBlank(message = "联系人不可为空")
    private String contacts;

    @ApiModelProperty("联系电话")
    @NotBlank(message = "联系电话不可为空")
    private String mobilePhone;

    @ApiModelProperty("期望解决时间")
    private Calendar expectTime;

    @ApiModelProperty("电子邮件")
    private String email;

    @ApiModelProperty("附件")
    private String fileUrl;

    @ApiModelProperty("申请类型,1接口2文件3数据库")
    private String type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReasonDesc() {
        return reasonDesc;
    }

    public void setReasonDesc(String reasonDesc) {
        this.reasonDesc = reasonDesc;
    }

    public String getAcceptDeptId() {
        return acceptDeptId;
    }

    public void setAcceptDeptId(String acceptDeptId) {
        this.acceptDeptId = acceptDeptId;
    }

    public String getAcceptDept() {
        return acceptDept;
    }

    public void setAcceptDept(String acceptDept) {
        this.acceptDept = acceptDept;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Calendar getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(Calendar expectTime) {
        this.expectTime = expectTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
