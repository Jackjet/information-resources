package d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo;

import d1.framework.webapi.annotation.ApiModelProperty;
import d1.project.resourcesharingmgmt.common.model.TimePageableVm;

/**
 * @author maoyuying
 */
public class ResourceUseInfoFindAllVm extends TimePageableVm {
    @ApiModelProperty("资源名称")
    private String resourceName;

    @ApiModelProperty("信息资源名称")
    private String uviewNm;

    @ApiModelProperty("信息资源代码")
    private String uviewNo;

    @ApiModelProperty("云数据名称")
    private String sourceApiName;

    @ApiModelProperty("审核状态,0未审核1初审通过2已审核3已驳回4审核失败")
    private String status;

    @ApiModelProperty("信息资源提供方部门ID")
    private String provOrgId;

    @ApiModelProperty("信息资源提供方部门代码")
    private String provOrgCode;

    @ApiModelProperty("信息资源提供方部门名称")
    private String provOrgName;

    @ApiModelProperty("提交人部门Id")
    private String createDeptId;

    @ApiModelProperty("提交人部门名称")
    private String createDeptName;

    @ApiModelProperty("创建人Id")
    private String createById;

    @ApiModelProperty("申请类型,1接口2文件3数据库")
    private String type;

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
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

    public String getSourceApiName() {
        return sourceApiName;
    }

    public void setSourceApiName(String sourceApiName) {
        this.sourceApiName = sourceApiName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProvOrgId() {
        return provOrgId;
    }

    public void setProvOrgId(String provOrgId) {
        this.provOrgId = provOrgId;
    }

    public String getProvOrgCode() {
        return provOrgCode;
    }

    public void setProvOrgCode(String provOrgCode) {
        this.provOrgCode = provOrgCode;
    }

    public String getProvOrgName() {
        return provOrgName;
    }

    public void setProvOrgName(String provOrgName) {
        this.provOrgName = provOrgName;
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

    public String getCreateById() {
        return createById;
    }

    public void setCreateById(String createById) {
        this.createById = createById;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
