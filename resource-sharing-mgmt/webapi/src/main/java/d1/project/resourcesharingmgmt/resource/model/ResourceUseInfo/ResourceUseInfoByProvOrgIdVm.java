package d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo;


/**
 * @author zhengyang
 */
public class ResourceUseInfoByProvOrgIdVm {
    /**
     * 申请单位id
     */
    private String createDeptId;

    /**
     * 申请单位名称
     */
    private String createDeptName;

    /**
     * 申请资源数量
     */
    private long archNum;

    /**
     * 申请接口数量
     */
    private long apiNum;

    /**
     * 申请文件数量
     */
    private long fileNum;

    /**
     * 申请数据库数量
     */
    private long dataNum;

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

    public long getArchNum() {
        return archNum;
    }

    public void setArchNum(long archNum) {
        this.archNum = archNum;
    }

    public long getApiNum() {
        return apiNum;
    }

    public void setApiNum(long apiNum) {
        this.apiNum = apiNum;
    }

    public long getFileNum() {
        return fileNum;
    }

    public void setFileNum(long fileNum) {
        this.fileNum = fileNum;
    }

    public long getDataNum() {
        return dataNum;
    }

    public void setDataNum(long dataNum) {
        this.dataNum = dataNum;
    }
}
