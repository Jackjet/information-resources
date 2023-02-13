package d1.framework.permission.entity;

import d1.framework.webapi.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;


@Entity
@Table(name = "d1_dept")
@ApiModel(value = "Dept", description = "部门")
public class Dept extends BaseEntity {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "上级Id")
    private String parentId;

    @ApiModelProperty(value = "路径")
    private String path;

    @ApiModelProperty(value = "级别", example = "从0开始")
    private Integer level;

    @ApiModelProperty(value = "创建时间", example = "2018-01-01 01:01:01")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
