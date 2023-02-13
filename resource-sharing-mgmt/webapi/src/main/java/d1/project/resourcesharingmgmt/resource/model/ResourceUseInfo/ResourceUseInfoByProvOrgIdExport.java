package d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

/**
 * @author zhengyang
 */
public class ResourceUseInfoByProvOrgIdExport {
    @ExcelProperty(value = "申请单位名称", index = 0)
    @ColumnWidth(value = 40)
    private String createDeptName;

    @ExcelProperty(value = "申请资源数量", index = 1)
    @ColumnWidth(value = 20)
    private long archNum;

    @ExcelProperty(value = "申请接口数量", index = 2)
    @ColumnWidth(value = 20)
    private long apiNum;

    @ExcelProperty(value = "申请文件数量", index = 3)
    @ColumnWidth(value = 20)
    private long fileNum;

    @ExcelProperty(value = "申请数据库数量", index = 4)
    @ColumnWidth(value = 20)
    private long dataNum;

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
