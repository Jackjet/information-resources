package com.digitalchina.resourcecatalog.db.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * <p>
 * 管理员表
 * </p>
 *
 * @author wangmh
 * @since 2020-04-30
 */
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 管理员名称
     */
    private String username;

    /**
     * 管理员密码
     */
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 类型
     */
    private String type;
    
    private Integer disabled;

    /**
     * 最近一次登录IP地址
     */
    private String lastLoginIp;

    /**
     * 最近一次登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 头像图片
     */
    private String avatar;

    /**
     * 创建时间
     */
    private LocalDateTime addTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    private Integer deleted;

    @TableField(exist = false)
    private List<Integer> typeIds;

    /**
     * 角色列表
     */
//    @TableField(exist = false)
//    private Integer[] roleIds;
    @TableField(exist = false)
    private List<SysRole> roles;
    /**
     * 用户对应的组织结构
     */
    @TableField(exist = false)
    private List<ArchOrg> orgs;
    /**
     * 用户对应的部门
     */
    @TableField(exist = false)
    private List<DefaultKeyValue<Integer, String>> depts;
    
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public LocalDateTime getAddTime() {
        return addTime;
    }

    public void setAddTime(LocalDateTime addTime) {
        this.addTime = addTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public static final String ID = "id";

    public static final String USERNAME = "username";

    public static final String PASSWORD = "password";

    public static final String NAME = "name";

    public static final String DISABLED = "disabled";

    public static final String PHONE = "phone";

    public static final String TYPE = "type";

    public static final String LAST_LOGIN_IP = "last_login_ip";

    public static final String LAST_LOGIN_TIME = "last_login_time";

    public static final String AVATAR = "avatar";

    public static final String ADD_TIME = "add_time";

    public static final String UPDATE_TIME = "update_time";

    public static final String DELETED = "deleted";

    public static final String EMAIL = "email";

    public Integer getDisabled() {
        return disabled;
    }

    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<SysRole> getRoles() {
		return roles;
	}

	public void setRoles(List<SysRole> roles) {
		this.roles = roles;
	}

	public List<ArchOrg> getOrgs() {
		return orgs;
	}

	public void setOrgs(List<ArchOrg> orgs) {
		this.orgs = orgs;
	}

	public List<DefaultKeyValue<Integer, String>> getDepts() {
		return depts;
	}

	public void setDepts(List<DefaultKeyValue<Integer, String>> depts) {
		this.depts = depts;
	}

	@Override
	public String toString() {
		return "SysUser [id=" + id + ", username=" + username + ", password=" + password + ", name=" + name + ", phone="
				+ phone + ", email=" + email + ", type=" + type + ", disabled=" + disabled + ", lastLoginIp="
				+ lastLoginIp + ", lastLoginTime=" + lastLoginTime + ", avatar=" + avatar + ", addTime=" + addTime
				+ ", updateTime=" + updateTime + ", deleted=" + deleted + ", roles=" + roles + ", orgs=" + orgs
				+ ", depts=" + depts + "]";
	}

    public List<Integer> getTypeIds() {
        return typeIds;
    }

    public void setTypeIds(List<Integer> typeIds) {
        this.typeIds = typeIds;
    }
}
