package com.digitalchina.resourcecatalog.db.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 权责清单与信息资源关联表
 * </p>
 *
 * @author wangmh
 * @since 2020-05-09
 */
@ApiModel("权责清单与信息资源关联表")
public class CataBusInfoRel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 信息资源id
     */
    @ApiModelProperty("信息资源id")
    private Integer infoId;

    /**
     * 权责清单id
     */
    @ApiModelProperty("权责清单id")
    @NotNull(message = "权责清单id不能为空")
    private Integer busId;

    /**
     * 类型  0 产生的信息资源 1 需要的信息资源
     */
    @ApiModelProperty("类型")
    @NotNull(message = "类型不能为空")
    private Integer type;

    @TableField(exist = false)
    @ApiModelProperty("信息资源id集合")
    @NotNull(message = "信息资源id集合不能为空")
    private List<Integer> infoIds;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getInfoId() {
        return infoId;
    }

    public void setInfoId(Integer infoId) {
        this.infoId = infoId;
    }

    public Integer getBusId() {
        return busId;
    }

    public void setBusId(Integer busId) {
        this.busId = busId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public static final String ID = "id";

    public static final String INFO_ID = "info_id";

    public static final String BUS_ID = "bus_id";

    public static final String TYPE = "type";

    @Override
    public String toString() {
        return "CataBusInfoRel{" +
                "id=" + id +
                ", infoId=" + infoId +
                ", busId=" + busId +
                ", type=" + type +
                "}";
    }

    public List<Integer> getInfoIds() {
        return infoIds;
    }

    public void setInfoIds(List<Integer> infoIds) {
        this.infoIds = infoIds;
    }
}
