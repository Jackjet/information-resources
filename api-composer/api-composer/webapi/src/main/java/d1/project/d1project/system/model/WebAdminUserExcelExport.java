package d1.project.d1project.system.model;

import com.alibaba.excel.annotation.ExcelProperty;

import java.util.Date;

/**
 * excel导入导出
 *
 * @author Buter
 * @date 2020/3/15 18:05
 */
public class WebAdminUserExcelExport extends WebAdminUserExcelBase {

    @ExcelProperty(value = "创建时间")
    private Date createTime;

    public Date getCreateTime() {
        if (createTime == null) {
            return null;
        }
        return (Date) createTime.clone();
    }

    public void setCreateTime(Date createTime) {
        if (createTime == null) {
            this.createTime = null;
        } else {
            this.createTime = (Date) createTime.clone();
        }
    }

}
