package d1.project.resourcesharingmgmt.resource.model.AssetDict;

import d1.framework.webapi.annotation.ApiModelProperty;

/**
 * 目录分类管理
 *
 * @author zhengyang
 */
public class AssetDictVm {
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("值")
    private String value;

    @ApiModelProperty("类别")
    private String type;

    @ApiModelProperty("排序字段")
    private Integer sort;

    @ApiModelProperty("pid")
    private Integer pid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }
}
