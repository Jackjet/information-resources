package d1.project.resourcesharingmgmt.resource.model.AssetDict;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

/**
 * @author JungYoung
 */

@ApiModel(value = "AssetDictFindAllVm", description = "目录字典查询所有")
public class AssetDictFindAllVm {
    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 值
     */
    @ApiModelProperty("值")
    private String value;

    /**
     * 类别
     */
    @ApiModelProperty("类别")
    private String type;

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
}
