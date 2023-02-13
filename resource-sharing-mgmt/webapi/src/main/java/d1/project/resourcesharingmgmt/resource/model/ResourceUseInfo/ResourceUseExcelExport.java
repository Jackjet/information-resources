package d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

import java.util.Date;

/**
 * excel导入导出
 *
 * @author Buter
 * @date 2020/3/15 18:05
 */
public class ResourceUseExcelExport {
    @ExcelProperty(value = "申请单位")
    @ColumnWidth(value = 40)
    private String createDeptName;


    @ExcelProperty(value = "信息资源名称")
    @ColumnWidth(value = 40)
    private String uviewNm;

    @ExcelProperty(value = "信息资源代码")
    @ColumnWidth(value = 30)
    private String uviewNo;


    @ExcelProperty(value = "提供单位")
    @ColumnWidth(value = 30)
    private String provOrgName;

    @ExcelProperty(value = "申请日期")
    @ColumnWidth(value = 16)
    private Date createTime;

    @ExcelProperty(value = "挂接类型")
    private String type;

    public String getCreateDeptName() {
        return createDeptName;
    }

    public void setCreateDeptName(String createDeptName) {
        this.createDeptName = createDeptName;
    }

    public String getUviewNo() {
        return uviewNo;
    }

    public void setUviewNo(String uviewNo) {
        this.uviewNo = uviewNo;
    }

    public String getUviewNm() {
        return uviewNm;
    }

    public void setUviewNm(String uviewNm) {
        this.uviewNm = uviewNm;
    }

    public String getProvOrgName() {
        return provOrgName;
    }

    public void setProvOrgName(String provOrgName) {
        this.provOrgName = provOrgName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
