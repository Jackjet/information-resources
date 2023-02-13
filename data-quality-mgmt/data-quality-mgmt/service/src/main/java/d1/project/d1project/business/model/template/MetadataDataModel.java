package d1.project.d1project.business.model.template;

import d1.framework.webapi.annotation.ApiModel;

import java.util.List;

@ApiModel(value = "MetadataDataModel", description = "元数据数据")
public class MetadataDataModel {
    /**
     * 数据库类型
     */
    private String type;

    /**
     * 数据源信息
     */
    private String sourceId;

    /**
     * 元数据
     */
    private TableModel source;

    /**
     * 比较元数据
     */
    private TableModel target;

    /**
     * 操作符
     */
    private List<String> operators;

    /**
     * 值列表（目标值）
     */
    private List<String> values;

    /**
     * 联合为空
     */
    private boolean union;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TableModel getSource() {
        return source;
    }

    public void setSource(TableModel source) {
        this.source = source;
    }

    public TableModel getTarget() {
        return target;
    }

    public void setTarget(TableModel target) {
        this.target = target;
    }

    public List<String> getOperators() {
        return operators;
    }

    public void setOperators(List<String> operators) {
        this.operators = operators;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public boolean isUnion() {
        return union;
    }

    public void setUnion(boolean union) {
        this.union = union;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }
}



