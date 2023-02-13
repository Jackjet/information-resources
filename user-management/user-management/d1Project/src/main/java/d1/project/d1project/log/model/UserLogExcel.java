package d1.project.d1project.log.model;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * @author kikki
 */
public class UserLogExcel extends LogExcelBase {
    @ExcelProperty(value = "来源IP", index = 1)
    private String sourceIp;

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }
}
