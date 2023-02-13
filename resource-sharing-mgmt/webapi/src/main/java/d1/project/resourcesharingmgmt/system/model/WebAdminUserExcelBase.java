package d1.project.resourcesharingmgmt.system.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

/**
 * excel导入导出
 *
 * @author Buter
 * @date 2020/3/15 18:05
 */
public class WebAdminUserExcelBase {

    @ExcelProperty(value = "姓名", index = 0)
    @ColumnWidth(value = 18)
    private String name;

    @ExcelProperty(value = "账号", index = 1)
    @ColumnWidth(value = 18)
    private String account;

    @ExcelProperty(value = "手机号", index = 2)
    @ColumnWidth(value = 18)
    private String phone;

    @ExcelProperty(value = "身份证号", index = 3)
    @ColumnWidth(value = 20)
    private String idCard;

    /**
     * 数据库性别对应字段
     */
    @ExcelProperty(value = "性别(默认:女)", index = 4)
    @ColumnWidth(value = 25)
    private String sex;

    @ExcelProperty(value = "组织机构(例:总部/一级/二级)", index = 5)
    @ColumnWidth(value = 40)
    private String organizationName;

    @ExcelProperty(value = "角色权限", index = 6)
    @ColumnWidth(value = 30)
    private String roleName;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
