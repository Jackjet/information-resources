package d1.framework.permission.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author all
 */
@ApiModel(value = "MenuTreeGN", description = "GN功能树entity")
public class MenuTreeGN {
    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "父级Id")
    private String parentId;

    @ApiModelProperty(value = "父级名称")
    private String parentName;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "图标选中")
    private String icon;

    @ApiModelProperty(value = "类型  menu菜单、action操作")
    private String type;

    @ApiModelProperty(value = "路由", notes = "当类型是menu时，route值是vue的页面地址。当类型是action时，route值是后台controller中方法的路由值")
    private String route;

    @ApiModelProperty(value = "图标未选中")
    private String icon2;

    @ApiModelProperty(value = "排序")
    private Integer orderNum;

    @ApiModelProperty(value = "页面是否外部打开")
    private String outOpen;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "所属平台")
    private String platform;

    @ApiModelProperty(value = "是否有子集")
    private String haveChild;

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getIcon2() {
        return icon2;
    }

    public void setIcon2(String icon2) {
        this.icon2 = icon2;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getOutOpen() {
        return outOpen;
    }

    public void setOutOpen(String outOpen) {
        this.outOpen = outOpen;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getHaveChild() {
        return haveChild;
    }

    public void setHaveChild(String haveChild) {
        this.haveChild = haveChild;
    }
}
