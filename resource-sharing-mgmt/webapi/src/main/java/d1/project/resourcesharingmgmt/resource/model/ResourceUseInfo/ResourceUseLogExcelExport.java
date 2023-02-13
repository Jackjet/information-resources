package d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

/**
 * excel导入导出
 *
 * @author Buter
 * @date 2020/3/15 18:05
 */
public class ResourceUseLogExcelExport {
    @ExcelProperty(value = "操作类型")
    @ColumnWidth(value = 40)
    private String processName;

    @ExcelProperty(value = "审核时间")
    @ColumnWidth(value = 40)
    private String createTime;

    @ExcelProperty(value = "审核人")
    @ColumnWidth(value = 40)
    private String createByName;

    @ExcelProperty(value = "审核意见")
    private String auditDesc;

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public String getAuditDesc() {
        return auditDesc;
    }

    public void setAuditDesc(String auditDesc) {
        this.auditDesc = auditDesc;
    }
}
