package d1.project.resourcesharingmgmt.resource.entity;

import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 脱敏规则
 * @author zhengyang
 */
@Entity
@Table(name = "d1_replace_rule")
public class ReplaceRuleEntity extends BaseCreateAndUpdateEntity {
    /**
     * 规则名称
     */
    @Column(length = 100)
    private String name;

    /**
     * 脱敏方式（掩码）
     */
    @Column(length = 100)
    private String type;

    /**
     * 索引类型（1位数，2字符）
     */
    @Column(length = 32)
    private String indexType;

    /**
     * 起始值
     */
    @Column(length = 225)
    private String indexOf;

    /**
     * 结束值
     */
    @Column(length = 100)
    private String lastIndexOf;

    /**
     * 替换字符
     */
    @Column(length = 100)
    private String replaceValue;

    /**
     * 生成后的Sql
     */
    @Column(length = 2000)
    private String sqlValue;

    /**
     * 描述
     */
    @Column(columnDefinition = "TEXT")
    private String describe;

    @ApiModelProperty("是否是系统初始化 0 否、1 是")
    @Column(length = 10)
    private String hasSystem;

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

    public String getSqlValue() {
        return sqlValue;
    }

    public void setSqlValue(String sqlValue) {
        this.sqlValue = sqlValue;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getHasSystem() {
        return hasSystem;
    }

    public void setHasSystem(String hasSystem) {
        this.hasSystem = hasSystem;
    }
}
