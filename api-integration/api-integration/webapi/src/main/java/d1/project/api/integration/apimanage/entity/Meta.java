package d1.project.api.integration.apimanage.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author libin
 */
@Entity
@Table(name = "d1_meta")
@ApiModel(value = "Meta", description = "标签")
public class Meta extends BaseEntity {
    @ApiModelProperty("键")
    private String key;
    @ApiModelProperty("值")
    private String value;
    @ApiModelProperty("排序")
    private int seq;
    @ApiModelProperty("关联对象id")
    private String associationsId;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getAssociationsId() {
        return associationsId;
    }

    public void setAssociationsId(String associationsId) {
        this.associationsId = associationsId;
    }
}
