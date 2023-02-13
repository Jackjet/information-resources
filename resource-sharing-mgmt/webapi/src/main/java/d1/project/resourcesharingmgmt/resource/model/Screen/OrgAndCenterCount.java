package d1.project.resourcesharingmgmt.resource.model.Screen;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import java.util.List;

/**
 * 部门使用
 *
 * @author zhengyang
 */
@ApiModel(value = "OrgAndCenterCount", description = "前置机动态交换图")
public class OrgAndCenterCount {
    /**
     * 数据库
     */
    @ApiModelProperty("数据库")
    private long dataBase;

    /**
     * api
     */
    @ApiModelProperty("api")
    private long api;

    /**
     * 文件
     */
    @ApiModelProperty("文件")
    private long file;

    /**
     * 资源目录
     */
    @ApiModelProperty("资源目录")
    private long arch;

    /**
     * 机构
     */
    @ApiModelProperty("机构")
    private long org;

    /**
     * 机构列表
     */
    private List<String> orgs;

    public long getDataBase() {
        return dataBase;
    }

    public void setDataBase(long dataBase) {
        this.dataBase = dataBase;
    }

    public long getApi() {
        return api;
    }

    public void setApi(long api) {
        this.api = api;
    }

    public long getFile() {
        return file;
    }

    public void setFile(long file) {
        this.file = file;
    }

    public long getArch() {
        return arch;
    }

    public void setArch(long arch) {
        this.arch = arch;
    }

    public long getOrg() {
        return org;
    }

    public void setOrg(long org) {
        this.org = org;
    }

    public List<String> getOrgs() {
        return orgs;
    }

    public void setOrgs(List<String> orgs) {
        this.orgs = orgs;
    }
}
