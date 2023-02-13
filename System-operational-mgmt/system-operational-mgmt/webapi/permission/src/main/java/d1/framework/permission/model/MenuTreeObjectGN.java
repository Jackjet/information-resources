package d1.framework.permission.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "MenuTreeObjectGN", description = "GN功能树")
public class MenuTreeObjectGN {
    @ApiModelProperty(value = "实体")
    private MenuTreeGN entity;
    @ApiModelProperty(value = "子集")
    private List<MenuTreeObjectGN> childs;

    public MenuTreeGN getEntity() {
        return entity;
    }

    public void setEntity(MenuTreeGN entity) {
        this.entity = entity;
    }

    public List<MenuTreeObjectGN> getChilds() {
        return childs;
    }

    public void setChilds(List<MenuTreeObjectGN> childs) {
        this.childs = childs;
    }
}
