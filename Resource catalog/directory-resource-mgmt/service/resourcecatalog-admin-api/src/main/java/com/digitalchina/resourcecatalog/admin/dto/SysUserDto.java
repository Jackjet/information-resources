package com.digitalchina.resourcecatalog.admin.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author: create by wangmh
 * @name: SysUserDto.java
 * @description:
 * @date:2020/5/9
 **/
@ApiModel("用户实体类")
public class SysUserDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 管理员名称
     */
    @NotNull(message = "账号名称不能为空")
    @ApiModelProperty("账号名称")
    private String username;

    /**
     * 姓名
     */
    @NotNull(message = "姓名不能为空")
    @ApiModelProperty("姓名")
    private String name;

    /**
     * 联系电话
     */
    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("备注")
    private String remark;

    @NotNull(message = "用户角色不能为空")
    @ApiModelProperty("用户角色id")
    private Integer[] roleId;

    @NotNull(message = "所属部门不能为空")
    @ApiModelProperty("所属部门id")
    private Integer[] orgId;
   
    @ApiModelProperty("头像")
    private String avatar;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer[] getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = new Integer[]{roleId};
    }

    public Integer[] getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer[] orgId) {
        this.orgId = orgId;
    }

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
    
}
