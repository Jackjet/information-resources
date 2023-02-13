package d1.project.resourcesharingmgmt.resource.model.DemandedInfo;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.project.resourcesharingmgmt.common.model.TimePageableVm;

/**
 * @author maoyuying
 */

@ApiModel(value = "DemandedInfoFindAllVm", description = "需求管理分页查询")
public class DemandedInfoFindAllVm extends TimePageableVm {
    @ApiModelProperty("需求标题")
    private String title;
    @ApiModelProperty("受理部门Id")
    private String acceptDeptId;
    @ApiModelProperty("受理部门")
    private String acceptDept;
    @ApiModelProperty("提交人部门")
    private String createDeptName;
    @ApiModelProperty("需求类型")
    private String requestType;
    @ApiModelProperty("受理状态,0未受理1已受理2已驳回")
    private String status;
    @ApiModelProperty("创建人Id")
    private String createById;
    @ApiModelProperty("提交人部门Id")
    private String createDeptId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getCreateDeptName() {
        return createDeptName;
    }

    public void setCreateDeptName(String createDeptName) {
        this.createDeptName = createDeptName;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateById() {
        return createById;
    }

    public void setCreateById(String createById) {
        this.createById = createById;
    }

    public String getCreateDeptId() {
        return createDeptId;
    }

    public void setCreateDeptId(String createDeptId) {
        this.createDeptId = createDeptId;
    }
}
