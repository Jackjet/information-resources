package d1.project.d1project.business.model.template;

import java.util.List;

/**
 * 表
 */
public class TableModel {

    /**
     * 表名称
     */
    private String name;

    /**
     * 字段列表
     */
    private List<FieldModel> fields;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FieldModel> getFields() {
        return fields;
    }

    public void setFields(List<FieldModel> fields) {
        this.fields = fields;
    }
}
