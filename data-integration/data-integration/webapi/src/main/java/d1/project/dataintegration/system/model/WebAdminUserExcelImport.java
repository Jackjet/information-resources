package d1.project.dataintegration.system.model;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * excel导入导出
 *
 * @author Buter
 * @date 2020/3/15 18:05
 */
public class WebAdminUserExcelImport extends WebAdminUserExcelBase {

    @ExcelProperty(value = "邮箱")
    private String email;

    @ExcelProperty(value = "备注")
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
