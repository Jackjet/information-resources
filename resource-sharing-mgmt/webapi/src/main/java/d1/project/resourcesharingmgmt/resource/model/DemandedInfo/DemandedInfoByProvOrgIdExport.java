package d1.project.resourcesharingmgmt.resource.model.DemandedInfo;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

/**
 * @author zhengyang
 */
public class DemandedInfoByProvOrgIdExport {
    @ExcelProperty(value = "申请单位名称", index = 0)
    @ColumnWidth(value = 40)
    private String createDeptName;

    @ExcelProperty(value = "申请的需求数量", index = 1)
    @ColumnWidth(value = 20)
    private long demandedNum;

    @ExcelProperty(value = "已受理数量", index = 2)
    @ColumnWidth(value = 20)
    private long processed;

    @ExcelProperty(value = "未受理数量", index = 3)
    @ColumnWidth(value = 20)
    private long untreated;

    @ExcelProperty(value = "已驳回数量", index = 4)
    @ColumnWidth(value = 20)
    private long reject;

    public String getCreateDeptName() {
        return createDeptName;
    }

    public void setCreateDeptName(String createDeptName) {
        this.createDeptName = createDeptName;
    }

    public long getDemandedNum() {
        return demandedNum;
    }

    public void setDemandedNum(long demandedNum) {
        this.demandedNum = demandedNum;
    }

    public long getUntreated() {
        return untreated;
    }

    public void setUntreated(long untreated) {
        this.untreated = untreated;
    }

    public long getProcessed() {
        return processed;
    }

    public void setProcessed(long processed) {
        this.processed = processed;
    }

    public long getReject() {
        return reject;
    }

    public void setReject(long reject) {
        this.reject = reject;
    }
}
