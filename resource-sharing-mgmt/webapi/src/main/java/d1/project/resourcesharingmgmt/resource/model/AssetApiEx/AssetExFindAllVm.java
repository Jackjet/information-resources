package d1.project.resourcesharingmgmt.resource.model.AssetApiEx;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.model.PageableVm;

/**
 * @author JungYoung
 */

@ApiModel(value = "AssetApiExFindAllVm", description = "云接口查询所有")
public class AssetExFindAllVm extends PageableVm {
    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 机构
     */
    private Integer orgId;

    /**
     * 信息资源ID
     */
    @ApiModelProperty("信息资源ID")
    private String uviewId;

    /**
     * 信息资源代码
     */
    @ApiModelProperty("信息资源代码")
    private String uviewNo;

    /**
     * 信息资源名称
     */
    @ApiModelProperty("信息资源名称")
    private String uviewNm;

    /**
     * 云文件名称
     */
    @ApiModelProperty("云文件名称")
    private String name;

    /**
     * 类别，0文件，1数据库
     */
    @ApiModelProperty("类别，0文件，1数据库")
    private Integer type;

    /**
     * 审批状态，0草稿，1待初审，2终审，3驳回
     */
    @ApiModelProperty("审批状态，0草稿，1待初审，2终审，3驳回")
    private Integer status;

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
