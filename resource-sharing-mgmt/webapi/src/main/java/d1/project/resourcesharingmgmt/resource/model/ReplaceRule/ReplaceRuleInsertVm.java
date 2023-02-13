package d1.project.resourcesharingmgmt.resource.model.ReplaceRule;

import d1.framework.webapi.annotation.ApiModel;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "ReplaceRuleInsertVm", description = "脱敏规则添加")
public class ReplaceRuleInsertVm {
    /**
     * 规则名称
     */
    @NotBlank(message = "规则名称不可为空")
    private String name;

    /**
     * 脱敏方式（掩码）
     */
    @NotBlank(message = "脱敏方式不可为空")
    private String type;

    /**
     * 索引类型（1位数，2字符）
     */
    @NotBlank(message = "索引类型不可为空")
    private String indexType;

    /**
     * 起始值(当索引为1的时候为保留前几位)
     */
    @NotBlank(message = "起始值不可为空")
    private String indexOf;

    /**
     * 结束值(当索引为1的时候为保留后几位)
     */
    @NotBlank(message = "结束值不可为空")
    private String lastIndexOf;

    /**
     * 替换字符
     */
    @NotBlank(message = "替换字符不可为空")
    private String replaceValue;

    /**
     * 描述
     */
    private String describe;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIndexType() {
        return indexType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }

    public String getIndexOf() {
        return indexOf;
    }

    public void setIndexOf(String indexOf) {
        this.indexOf = indexOf;
    }

    public String getLastIndexOf() {
        return lastIndexOf;
    }

    public void setLastIndexOf(String lastIndexOf) {
        this.lastIndexOf = lastIndexOf;
    }

    public String getReplaceValue() {
        return replaceValue;
    }

    public void setReplaceValue(String replaceValue) {
        this.replaceValue = replaceValue;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
