package com.digitalchina.resourcecatalog.db.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

/**
 * <p>
 * 权责清单与应用系统关联表
 * </p>
 *
 * @author wangmh
 * @since 2020-05-09
 */
public class CataAppBusRel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 应用系统id 关联 cata_app  id
     */
    private Integer appId;

    /**
     * 权责清单id 关联 cata_bus_item id
     */
    private Integer itemId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public static final String ID = "id";

    public static final String APP_ID = "app_id";

    public static final String ITEM_ID = "item_id";

    @Override
    public String toString() {
        return "CataAppBusRel{" +
                "id=" + id +
                ", appId=" + appId +
                ", itemId=" + itemId +
                "}";
    }
}
