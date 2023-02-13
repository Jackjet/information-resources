package d1.project.resource.tag.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 分组信息
 *
 * @author baozh
 */

@Entity
@Table(name = "d1_tag_info")
@ApiModel(value = "TagInfo", description = "标签管理")
public class TagInfo extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("标签名称")
    @Column(length = 50)
    private String name;

    @ApiModelProperty("详情")
    @Column(columnDefinition = "TEXT")
    private String detail;

    @ApiModelProperty("分组ID")
    @Column(length = 50)
    private String groupId;

    @ApiModelProperty("创建人名称")
    @Column(length = 50)
    private String createName;

    @ApiModelProperty("更新人名称")
    @Column(length = 50)
    private String updateName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }
}
