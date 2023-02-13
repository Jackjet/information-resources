package d1.framework.permission.entity;

import d1.framework.webapi.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "d1_dept_permission")
@ApiModel(value = "DeptPermission", description = "部门权限表")
public class DeptPermission extends BaseEntity {

    @ApiModelProperty(value = "部门ID")
    private String deptId;

    @ApiModelProperty(value = "功能树Id")
    private String menuTreeId;

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getMenuTreeId() {
        return menuTreeId;
    }

    public void setMenuTreeId(String menuTreeId) {
        this.menuTreeId = menuTreeId;
    }
}
