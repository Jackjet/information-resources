package d1.project.resourcesharingmgmt.resource.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author dy
 */
@Entity
@Table(name = "d1_way")
@ApiModel(value = "WayEntity", description = "代办模块、服务指引")
public class WayEntity extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("类型（0代办、1服务指引）")
    @Column(length = 10)
    private String type;

    @ApiModelProperty("名称")
    @Column(length = 200)
    private String name;

    @ApiModelProperty("路径")
    @Column(length = 200)
    private String path;

    @ApiModelProperty("序列")
    @Column(length = 2)
    private Integer seq;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }
}
