package d1.project.resourcesharingmgmt.resource.model.ArchBusiUviewEx;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.model.PageableVm;

/**
 * @author JungYoung
 */

@ApiModel(value = "ArchBusiUviewExAllVm", description = "信息资源目录查询所有")
public class ArchBusiUviewExFindAllVm extends PageableVm {
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
     * 共享类型,01无条件共享，02有条件共享，03不予共享
     */
    @ApiModelProperty("共享类型,01无条件共享，02有条件共享，03不予共享")
    private String shareLv;

    /**
     * 信息资源提供方部门ID
     */
    @ApiModelProperty("信息资源提供方部门ID")
    private String provOrgId;

    /**
     * 所属类型ID
     */
    @ApiModelProperty("所属类型ID")
    private String typId;

    /**
     * 挂接状态,0未挂接 1已挂接
     */
    @ApiModelProperty("挂接状态,0未挂接 1已挂接")
    private String isHook;

    /**
     * 挂接类型,1接口,2文件，3数据库
     */
    @ApiModelProperty("挂接类型,1接口,2文件，3数据库")
    private String hookType;

    /**
     * 下架状态,0未下架 1已下架
     */
    @ApiModelProperty("下架状态,0未下架 1已下架")
    private String status;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private String order;

    /**
     * 排序方式，0顺序，1倒序
     */
    @ApiModelProperty("排序方式，0顺序，1倒序")
    private String desc;

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

    public String getShareLv() {
        return shareLv;
    }

    public void setShareLv(String shareLv) {
        this.shareLv = shareLv;
    }

    public String getProvOrgId() {
        return provOrgId;
    }

    public void setProvOrgId(String provOrgId) {
        this.provOrgId = provOrgId;
    }

    public String getTypId() {
        return typId;
    }

    public void setTypId(String typId) {
        this.typId = typId;
    }

    public String getIsHook() {
        return isHook;
    }

    public void setIsHook(String isHook) {
        this.isHook = isHook;
    }

    public String getHookType() {
        return hookType;
    }

    public void setHookType(String hookType) {
        this.hookType = hookType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
