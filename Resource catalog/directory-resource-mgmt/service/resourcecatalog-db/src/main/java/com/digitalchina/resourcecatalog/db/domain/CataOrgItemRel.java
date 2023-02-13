package com.digitalchina.resourcecatalog.db.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

/**
 * <p>
 * 部门与权责清单关联表
 * </p>
 *
 * @author wangmh
 * @since 2020-05-09
 */
public class CataOrgItemRel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 部门id 与cata_org 中org_id关联
     */
    private Integer orgId;

    /**
     * 权责清单id 与cata_bus_item id关联
     */
    private Integer itemId;

    private Integer deptId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public static final String ID = "id";

    public static final String ORG_ID = "org_id";

    public static final String ITEM_ID = "item_id";

    @Override
    public String toString() {
        return "CataOrgItemRel{" +
                "id=" + id +
                ", orgId=" + orgId +
                ", itemId=" + itemId +
                "}";
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }
}
