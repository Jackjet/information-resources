package d1.project.resourcesharingmgmt.resource.model.DemandedInfo;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import java.util.Calendar;

/**
 * 需求
 *
 * @author zhengyang
 */
@ApiModel(value = "DemandedInfoVm", description = "需求表")
public class DemandedInfoVm extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("需求标题")
    private String title;

    @ApiModelProperty("缘由")
    private String reason;

    @ApiModelProperty("缘由说明")
    private String reasonDesc;

    @ApiModelProperty("受理单位id")
    private String acceptDeptId;

    @ApiModelProperty("受理部门")
    private String acceptDept;

    @ApiModelProperty("需求类型，0:目录资源变更;1:目录资源新增;2:云数据变更;3:云数据新增")
    private String requestType;

    @ApiModelProperty("需求描述")
    private String describe;

    @ApiModelProperty("联系人")
    private String contacts;

    @ApiModelProperty("联系电话")
    private String mobilePhone;

    @ApiModelProperty("期望解决时间")
    private Calendar expectTime;

    @ApiModelProperty("电子邮件")
    private String email;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("附件")
    private String fileUrl;

    @ApiModelProperty("申请类型,1接口2文件3数据库")
    private String type;

    @ApiModelProperty("受理状态,0未受理1已受理2已驳回")
    private String status;

    @ApiModelProperty("受理意见")
    private String opinion;

    @ApiModelProperty("提交人部门id")
    private String createDeptId;

    @ApiModelProperty("提交人部门名称")
    private String createDeptName;

    @ApiModelProperty("创建人名称")
    private String createByName;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getCreateDeptId() {
        return createDeptId;
    }

    public void setCreateDeptId(String createDeptId) {
        this.createDeptId = createDeptId;
    }

    public String getCreateDeptName() {
        return createDeptName;
    }

    public void setCreateDeptName(String createDeptName) {
        this.createDeptName = createDeptName;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
