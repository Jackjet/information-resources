package d1.project.dataintegration.log.model;

import com.alibaba.excel.annotation.ExcelProperty;

public class OperationLogExcel extends LogExcelBase {

    @ExcelProperty(value = "操作模块", index = 1)
    private String module;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
}