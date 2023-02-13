package d1.project.d1project.system.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "d1_button")
@ApiModel(value = "ButtonEntity", description = "按钮")
public class ButtonEntity extends BaseCreateAndUpdateEntity {

    @ApiModelProperty("按钮编码")
    private String code;

    @ApiModelProperty("按钮名称")
    private String name;

    @ApiModelProperty("所属页面")
    private String pageId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }
}
