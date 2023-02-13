package d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.util.Calendar;

/**
 * @author JungYoung
 */
@ApiModel(value = "ResourceUseInfoInsertVm", description = "申请资源使用")
public class ResourceUseInfoInsertVm {
    @ApiModelProperty("信息资源ID")
    @NotBlank(message = "信息资源ID不可为空")
    private String uviewId;

    @ApiModelProperty("资源目录和云数据关联表ID")
    private String uviewApiId;

    @ApiModelProperty("云数据名称")
    private String sourceApiName;

    @ApiModelProperty("云数据描述")
    private String sourceApiDesc;

    @ApiModelProperty("资源用途")
    private String resourceUse;

    @ApiModelProperty("支持业务")
    private String supportBusiness;

    @ApiModelProperty("使用期限开始时间")
    private Calendar useStartTime;

    @ApiModelProperty("使用期限结束时间")
    private Calendar useEndTime;

    @ApiModelProperty("说明")
    private String instructions;

    @ApiModelProperty("联系人")
    @NotBlank(message = "联系人不可为空")
    private String contacts;

    @ApiModelProperty("联系电话")
    private String mobilePhone;

    @ApiModelProperty("电子邮件")
    private String email;

    @ApiModelProperty("申请依据")
    private String fileUrl;

    @ApiModelProperty("申请类型,1接口2文件3数据库")
    @NotBlank(message = "申请类型不可为空")
    private String type;

    /**
     * dataId
     */
    private String dataId;

    /**
     * 表名
     */
    @ApiModelProperty("表名")
    private String tableName;

    /**
     * fileId
     */
    private String fileId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件描述
     */
    private String detail;

    public String getUviewId() {
        return uviewId;
    }

    public void setUviewId(String uviewId) {
        this.uviewId = uviewId;
    }

    public String getUviewApiId() {
        return uviewApiId;
    }

    public void setUviewApiId(String uviewApiId) {
        this.uviewApiId = uviewApiId;
    }

    public String getSourceApiName() {
        return sourceApiName;
    }

    public void setSourceApiName(String sourceApiName) {
        this.sourceApiName = sourceApiName;
    }

    public String getSourceApiDesc() {
        return sourceApiDesc;
    }

    public void setSourceApiDesc(String sourceApiDesc) {
        this.sourceApiDesc = sourceApiDesc;
    }

    public String getResourceUse() {
        return resourceUse;
    }

    public void setResourceUse(String resourceUse) {
        this.resourceUse = resourceUse;
    }

    public String getSupportBusiness() {
        return supportBusiness;
    }

    public void setSupportBusiness(String supportBusiness) {
        this.supportBusiness = supportBusiness;
    }

    public Calendar getUseStartTime() {
        return useStartTime;
    }

    public void setUseStartTime(Calendar useStartTime) {
        this.useStartTime = useStartTime;
    }

    public Calendar getUseEndTime() {
        return useEndTime;
    }

    public void setUseEndTime(Calendar useEndTime) {
        this.useEndTime = useEndTime;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
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

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
