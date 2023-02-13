package com.digitalchina.resourcecatalog.db.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

/**
 * <p>
 * 用户机构关联表
 * </p>
 *
 * @author wangmh
 * @since 2020-05-09
 */
public class SysUserOrgRel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * userID
     */
    private Integer userId;

    /**
     * org_id 关联cata_org中org_id
     */
    private Integer orgId;

    public SysUserOrgRel(Integer userId, Integer orgId) {
        this.userId = userId;
        this.orgId = orgId;
    }

    public SysUserOrgRel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public static final String ID = "id";

    public static final String USER_ID = "user_id";

    public static final String ORG_ID = "org_id";

    @Override
    public String toString() {
        return "SysUserOrgRel{" +
        "id=" + id +
        ", userId=" + userId +
        ", orgId=" + orgId +
        "}";
    }
}
