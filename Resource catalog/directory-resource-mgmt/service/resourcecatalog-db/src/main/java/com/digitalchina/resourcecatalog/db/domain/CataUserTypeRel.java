package com.digitalchina.resourcecatalog.db.domain;

import java.io.Serializable;

/**
 * <p>
 * 用户与目录分类关联表
 * </p>
 *
 * @author wangmh
 * @since 2020-05-09
 */
public class CataUserTypeRel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 类型ID
     */
    private Integer typeId;

    private Integer isLeaf;

    public CataUserTypeRel() {
    }

    public CataUserTypeRel(Integer userId, Integer typeId, Integer isLeaf) {
        this.userId = userId;
        this.typeId = typeId;
        this.isLeaf = isLeaf;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public static final String USER_ID = "user_id";

    public static final String TYPE_ID = "type_id";

    @Override
    public String toString() {
        return "CataUserTypeRel{" +
                "userId=" + userId +
                ", typeId=" + typeId +
                "}";
    }

    public Integer getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(Integer isLeaf) {
        this.isLeaf = isLeaf;
    }
}
