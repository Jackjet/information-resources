package d1.project.d1project.system.model;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * excel导入导出
 *
 * @author Buter
 * @date 2020/3/15 18:05
 */
public class WebAdminUserExcelBase {

    @ExcelProperty(value = "姓名", index = 0)
    private String name;

    @ExcelProperty(value = "账号", index = 1)
    private String account;

    @ExcelProperty(value = "手机号", index = 2)
    private String phone;

    /**
     * 数据库性别对应字段
     */
    @ExcelProperty(value = "性别(默认:女)", index = 3)
    private String sex;

    @ExcelProperty(value = "组织机构(例:总部/一级/二级)", index = 4)
    private String organizationName;

    @ExcelProperty(value = "角色权限", index = 5)
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
