package d1.project.d1project.business.model.template;

/**
 * 字段
 */
public class FieldModel {
    /**
     * 字段名称
     */
    private String name;

    /**
     * 字段类型
     */
    private String type;

    /**
     * 字段长度
     */
    private int length;

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

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
