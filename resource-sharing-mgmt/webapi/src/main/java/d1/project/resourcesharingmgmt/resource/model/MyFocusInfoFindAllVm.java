package d1.project.resourcesharingmgmt.resource.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.model.PageableVm;


/**
 * @author JungYoung
 */

@ApiModel(value = "MyFocusInfoFindAllVm", description = "我的收藏查询全部")
public class MyFocusInfoFindAllVm extends PageableVm {
    @ApiModelProperty("信息资源名称")
    private String uviewNm;

    @ApiModelProperty("信息资源代码")
    private String uviewNo;

    @ApiModelProperty("共享类型,01无条件共享，02有条件共享，03不予共享")
    private String shareLv;

    @ApiModelProperty("创建人Id")
    private String createById;

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

    public String getCreateById() {
        return createById;
    }

    public void setCreateById(String createById) {
        this.createById = createById;
    }
}
