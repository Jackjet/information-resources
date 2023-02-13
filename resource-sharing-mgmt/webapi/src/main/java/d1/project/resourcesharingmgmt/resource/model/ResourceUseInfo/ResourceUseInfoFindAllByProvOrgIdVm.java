package d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.project.resourcesharingmgmt.common.model.TimePageableVm;

import javax.persistence.Column;

/**
 * @author JungYoung
 */

@ApiModel(value = "ResourceUseInfoFindAllByProvOrgIdVm", description = "云接口申请单位查询")
public class ResourceUseInfoFindAllByProvOrgIdVm extends TimePageableVm {
    /**
     * 申请部门id
     */
    @ApiModelProperty("申请部门id")
    private String createDeptId;

    /**
     * 信息资源名称
     */
    @ApiModelProperty("信息资源名称")
    private String uviewNm;

    /**
     * 信息资源代码
     */
    @ApiModelProperty("信息资源代码")
    private String uviewNo;

    /**
     * 资源类型,1api 2文件3数据库
     */
    @ApiModelProperty("申请类型,1api 2文件3数据库")
    private String type;

    public String getCreateDeptId() {
        return createDeptId;
    }

    public void setCreateDeptId(String createDeptId) {
        this.createDeptId = createDeptId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
